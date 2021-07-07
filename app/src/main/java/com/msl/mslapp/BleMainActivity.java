

package com.msl.mslapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.icu.text.SimpleDateFormat;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.msl.mslapp.Ble.BluetoothUtils;
import com.msl.mslapp.Ble.Dialog.Beginning.dialogFragment_Ble_Beginning_LanguageChange;
import com.msl.mslapp.Ble.Dialog.Setting.dialogFragment_Ble_Setting_FL_Setting;
import com.msl.mslapp.Ble.Dialog.Setting.dialogFragment_ble_Setting_GPS_Change;
import com.msl.mslapp.Ble.fragment.fragment_Ble_Beginning;
import com.msl.mslapp.Ble.fragment.fragment_Ble_Scan;
import com.msl.mslapp.Ble.fragment.Function.fragment_Ble_Status;
import com.msl.mslapp.Ble.fragment.Function.fragment_Ble_Function;
import com.msl.mslapp.Ble.fragment.fragment_Ble_Password;
import com.msl.mslapp.Ble.fragment.fragment_CDS_Setting;
import com.msl.mslapp.Ble.fragment.fragment_SN_Setting;
import com.msl.mslapp.Public.Log.log_ListViewAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.msl.mslapp.Ble.BluetoothUtils.findBLECharacteristics;
import static com.msl.mslapp.Ble.fragment.fragment_Ble_Beginning.setLocale;
import static com.msl.mslapp.Ble.fragment.fragment_Ble_Scan.selectedSerialNum;

@RequiresApi(api = Build.VERSION_CODES.M)
public class BleMainActivity extends AppCompatActivity implements fragment_Ble_Scan.Ble_Scan_Listener, fragment_Ble_Status.Ble_Status_Listener,
        fragment_SN_Setting.SN_Setting_Listener, fragment_CDS_Setting.CDS_Setting_Listener {

    //region 변수 및 상수 정의

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-MainAct";

    // 관리자용 앱 설정
    public static final boolean adminApp = true;

    public static Context mBleContext = null;
    public static AppCompatActivity mBleMain = null;
    public static String tLanguage;
    // bluetooth 관련
    public static BluetoothAdapter mBluetoothAdapter = null;
    public BluetoothDevice bleConnectDevice = null;
    public static BluetoothGatt bleGatt = null;
    public static BluetoothManager bluetoothManager = null;
    public static String BluetoothStatus = "";

    boolean pauseResumeCheck = false;

    // password 관련
    public static String readPassword = "";

    // gps 권한
    public static LocationManager locationManager = null;
    // requestCode
    public static final int bluetooth_permission_check = 50;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 51;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS_RE = 52;
    // handler msg코드
    public static final int ConnectSuccess = 0;
    public static final int readDataSuccess = 1;
    // Ble connecting
    public static boolean BleConnecting = false;
    //Ble search
    // 현재 스캔중인지 확인하는 플래그, true일 경우 - 스캔중
    public static boolean scanningFlag = false;
    // CDS 설정 전용
    public static boolean CdsFlag = false;
    // SN 설정 전용
    public static boolean SnFlag = false;

    // 읽기 쓰기 케릭터
    public static BluetoothGattCharacteristic respCharacteristic = null;
    public static BluetoothGattCharacteristic cmdCharacteristic = null;

    // 블루투스 들어온 데이터 값
    String readDataTotal = "";
    ConcurrentLinkedQueue<String> readDataQueue = new ConcurrentLinkedQueue<String>();

    //사용자 BLE UUID Service/Rx/Tx
    public static String SERVICE_STRING = "6E400001-B5A3-F393-E0A9-E50E24DCCA9E";
    public static String CHARACTERISTIC_COMMAND_STRING = "6E400002-B5A3-F393-E0A9-E50E24DCCA9E";
    public static String CHARACTERISTIC_RESPONSE_STRING = "6E400003-B5A3-F393-E0A9-E50E24DCCA9E";
    // 등명기 용 uuid
    public static String SERVICE_STRING_4 = "0000fff0-0000-1000-8000-00805f9b34fb";
    public static String CHARACTERISTIC_COMMAND_STRING_4 = "0000fff2-0000-1000-8000-00805f9b34fb";
    public static String CHARACTERISTIC_RESPONSE_STRING_4 = "0000fff1-0000-1000-8000-00805f9b34fb";
    //BluetoothGattDescriptor 고정
    public String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    //scan filter
    public static ArrayList<ScanFilter> filters = new ArrayList();
    public static ScanFilter scanFilter = new ScanFilter.Builder().build();
    // set 설정이 되면 해당 서비스 uuid만 검색 됨(근데 그것도 안됨 왜임?ㅋ)
    //public static ScanFilter scanFilter = new ScanFilter.Builder().setServiceUuid(new ParcelUuid(UUID.fromString(SERVICE_STRING))).build();
    // .setServiceUuid(new ParcelUuid(UUID.fromString(SERVICE_STRING))) : 해당 uuid 서비스를 검색
    // .setDeviceAddress(address) : address 값 검색   String[] peripheralAddresses = new String[]{"01:0A:5C:7D:D0:1A"};
    // .setDeviceName(name) : name 값 검색
    // 이런식으로 여러개 검색 가능.
    /*String[] names = new String[]{"Polar H7 391BB014"};
    List<ScanFilter> filters = null;
    if(names != null) {
        filters = new ArrayList<>();
        for (String name : names) {
            ScanFilter filter = new ScanFilter.Builder()
                    .setDeviceName(name)
                    .build();
            filters.add(filter);
        }*/
    // scan 설정을 lowpower로 설정
    public static ScanSettings settings = new ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build();
    //public static ScanSettings settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).setReportDelay(0).setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES).build();

    //region 블루투스 데이터 용
    //데이터 기호
    public static final String DATA_SIGN_START = "$";
    public static final String DATA_SIGN_CHECKSUM = "*";
    public static final String DATA_SIGN_COMMA = ",";
    public static final String DATA_SIGN_CR = "\r"; //<CR>
    public static final String DATA_SIGN_LF = "\n"; //<LF>

    //데이터 타입
    public static final String DATA_TYPE_LICMD = "LICMD"; // 명령 커맨드
    public static final String DATA_TYPE_MUCMD = "MUCMD"; // 명령 커맨드
    public static final String DATA_TYPE_LISTS = "LISTS"; // 상태 정보
    public static final String DATA_TYPE_LISET = "LISET"; // 설정 정보
    public static final String DATA_TYPE_PS = "PS"; //패스워드
    public static final String DATA_TYPE_S = "S"; //설정
    public static final String DATA_TYPE_I = "I"; //설정
    public static final String DATA_TYPE_R = "R"; //Request
    public static final String DATA_TYPE_A = "A"; //
    public static final String DATA_TYPE_W = "W"; //
    public static final String DATA_TYPE_Y = "Y"; //
    public static final String DATA_TYPE_X = "X"; //
    public static final String DATA_TYPE_Z = "Z"; //
    public static final String DATA_TYPE_SID = "SID"; //ID 설정
    public static final String DATA_TYPE_RMC = "RMC"; //리모컨 모드
    public static final String DATA_TYPE_DIP = "DIP"; //DIP SW 모드
    public static final String DATA_TYPE_RST = "RST"; //공장 초기화
    public static final String DATA_TYPE_BTV = "BTV"; //배터리 데이터 확인
    public static final String DATA_TYPE_SLV = "SLV"; //솔라전압 데이터 확인
    public static final String DATA_TYPE_SLC = "SLC"; //태양광 전류 테이터 확인
    public static final String DATA_TYPE_SNB = "SNB"; //시리얼넘버 확인
    public static final String DATA_TYPE_GP1 = "GP1"; //낮동안 GPS 할성화
    public static final String DATA_TYPE_GP0 = "GP0"; //저녁동안에만 활성화
    public static final String DATA_TYPE_DEL = "DEL"; //저녁동안에만 활성화
    public static final String DATA_TYPE_ADMIN = "ZFVVS"; //저녁동안에만 활성화
    public static final String DATA_TYPE_1 = "1"; //상태요청
    public static final String DATA_TYPE_2 = "2"; //강제점등
    public static final String DATA_TYPE_3 = "3"; //강제소등
    public static final String DATA_TYPE_4 = "4"; //리셋
    public static final String DATA_TYPE_5 = "5"; //부동광
    public static final String DATA_TYPE_14 = "14"; //부동광

    //디바이스 ID
    public static final String DATA_ID_255 = "255";

    //$LICMD,1,255* : 상태요청
    public final static String DATA_REQUEST_STATUS = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_1 + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$LICMD,I* : 정보요청(펌웨어 버전 및 GPS 상태, delay time 값 등)
    public final static String DATA_REQUEST_INFORMATION = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_I
            + DATA_SIGN_CHECKSUM;
    //$LICMD,2,255* : 강제점등
    public final static String DATA_LAMP_ON = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_2 + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$LICMD,3,255* : 강제소등
    public final static String DATA_LAMP_OFF = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_3 + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$LICMD,4,255* : 리셋
    public final static String DATA_DEVICE_RESET = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_4 + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$LICMD,5,255* : 부동광
    public final static String DATA_LAMP_FIXED = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_5 + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$LICMD,S,RMC,255* : 리모컨 모드
    public final static String DATA_SET_RMC = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_S + DATA_SIGN_COMMA
            + DATA_TYPE_RMC + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$LICMD,S,DIP,255* : DIP SW 모드
    public final static String DATA_SET_DIP = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_S + DATA_SIGN_COMMA
            + DATA_TYPE_DIP + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$LICMD,S,RST,255* : 공장 초기화
    public final static String DATA_SET_RST = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_S + DATA_SIGN_COMMA
            + DATA_TYPE_RST + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$LICMD,W,255* : 점등 준비
    public final static String CDS_LAMP_ON_READY = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_W + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$LICMD,Y,255* : 점등 설정
    public final static String CDS_LAMP_ON_SETTING = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_Y + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$LICMD,X,255* : 소등 준비
    public final static String CDS_LAMP_OFF_READY = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_X + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$LICMD,Z,255* : 소등 설정
    public final static String CDS_LAMP_OFF_SETTING = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_Z + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;

    //$PS,A,ZFVVS* : 관리자 패스워드
    public final static String ADMIN_PASSWORD = DATA_SIGN_START
            + DATA_TYPE_PS + DATA_SIGN_COMMA
            + DATA_TYPE_A + DATA_SIGN_COMMA
            + DATA_TYPE_ADMIN
            + DATA_SIGN_CHECKSUM;

    // $LICMD,S,BTV,255* : 배터리 각 전압 확인
    public final static String DATA_REQUEST_BTV = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_S + DATA_SIGN_COMMA
            + DATA_TYPE_BTV + DATA_SIGN_COMMA
            + DATA_ID_255 + DATA_SIGN_CHECKSUM;

    // $LICMD,S,SLV,255* : 솔라판 각 전압 확인
    public final static String DATA_REQUEST_SLV = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_S + DATA_SIGN_COMMA
            + DATA_TYPE_SLV + DATA_SIGN_COMMA
            + DATA_ID_255 + DATA_SIGN_CHECKSUM;

    // $LICMD,S,SLV,255* : 솔라판 각 전류 확인
    public final static String DATA_REQUEST_SLC = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_S + DATA_SIGN_COMMA
            + DATA_TYPE_SLC + DATA_SIGN_COMMA
            + DATA_ID_255 + DATA_SIGN_CHECKSUM;

    // $LICMD,S,GP0,255* : GPS 밤에만 작동
    public final static String GPS_SET_OFF = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_S + DATA_SIGN_COMMA
            + DATA_TYPE_GP0 + DATA_SIGN_COMMA
            + DATA_ID_255 + DATA_SIGN_CHECKSUM;


    // $LICMD,S,GP1,255* : GPS 낮에도 작동
    public final static String GPS_SET_ON = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_S + DATA_SIGN_COMMA
            + DATA_TYPE_GP1 + DATA_SIGN_COMMA
            + DATA_ID_255 + DATA_SIGN_CHECKSUM;

    //$MUCMD,14,1*11<CR><LF> : RTU에서 데이터 보내게함
    public final static String DATA_RTU_BLUETOOTH_SENDING = DATA_SIGN_START
            + DATA_TYPE_MUCMD + DATA_SIGN_COMMA
            + DATA_TYPE_14 + DATA_SIGN_COMMA
            + DATA_TYPE_1 + DATA_SIGN_CHECKSUM
            + DATA_TYPE_1 + DATA_TYPE_1;

    //endregion

    //endregion 변수 및 상수 정의 끝

    //region layout

    MenuItem ScanItem;
    MenuItem reFreshItem;
    MenuItem bleDisconnectItem;

    // 현재 프래그먼트 확인용
    public static Fragment currentFragment;

    //toolbar
    public static Toolbar toolbarMain;
    public static NavigationView navigationView;
    public static TextView toolbar_title;

    public static ListView log_Listview;
    public static log_ListViewAdapter log_listViewAdapter;
    LinearLayout ll_navigation_move, ll_navigation_log;

    public static LinearLayout ll_navigation_GPS;


    // drawLayout(bluetoothMainlayout)
    public static DrawerLayout bleDrawerLayout;
    public static ActionBarDrawerToggle drawerToggle;
    private View drawerView;


    //endregion


    // fragment에서 데이터 가져오는 인터페이스

    // scan에서 선택한 디바이스와 연결
    @Override
    public void onSelectBleDevice(BluetoothDevice device) {
        Log.d(TAG, "onSelectBleDevice");
        Log.d(TAG, "선택한 디바이스 이름 : " + device.getName());

        bleConnectDevice = device;
        connectToDevice(mBleContext, bleConnectDevice, gattClientCallback);
        //bleGatt = bleConnectDevice.connectGatt(mBleContext, false, gattClientCallback);
    }

    public static void connectToDevice(Context context,
                                       BluetoothDevice device,
                                       BluetoothGattCallback mGattCallBack) {
        if (device != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Log.d(TAG, "connectToDevice - O up");
                /*bleGatt = device.connectGatt(context, false, mGattCallBack,
                        BluetoothDevice.DEVICE_TYPE_LE,BluetoothDevice.PHY_LE_2M | BluetoothDevice.PHY_LE_1M);*/
                bleGatt = device.connectGatt(context, false, mGattCallBack,
                        BluetoothDevice.DEVICE_TYPE_LE, BluetoothDevice.PHY_LE_2M | BluetoothDevice.PHY_LE_1M);
            } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                Log.d(TAG, "connectToDevice - M up");
                bleGatt = device.connectGatt(context, false, mGattCallBack,
                        BluetoothDevice.DEVICE_TYPE_LE);
            } else {
                Log.d(TAG, "connectToDevice - M down");
                bleGatt = device.connectGatt(context, false,
                        mGattCallBack);
            }
        } else {
            //WiSeSdkFileWritter.writeToFile(fileName, TAG + " FAILED could not find remote device/ remote device is null >>" + macAddress);
        }
    }


    @Override
    public void onPauseFragment_Ble_Scan() {
        Log.d(TAG, "onPauseFragment_Ble_Scan");
        ScanItem.setTitle(R.string.ble_main_scanItem_scan);
    }

    // toolbar 스캔 기능 보이게하기
    @Override
    public void onCreateViewFragment_Ble_Scan() {
        Log.d(TAG, "onCreateViewFragment_Ble_Scan");
        ScanItem.setVisible(true);
        reFreshItem.setVisible(true);
    }

    // toolbar 스캔 기능 가리기
    @Override
    public void onDetachFragment_Ble_Scan() {
        Log.d(TAG, "onDetachFragment_Ble_Scan");
        ScanItem.setTitle(R.string.ble_main_scanItem_stopScanning);
        ScanItem.setVisible(false);
        reFreshItem.setVisible(false);
    }

    // status화면일 경우 텝 보이게하기
    @Override
    public void onCreateViewFragment_Ble_Status() {
        Log.d(TAG, "onCreateViewFragment_Ble_Status");
        bleDisconnectItem.setVisible(true);
        //tabLayout_ble.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetachFragment_Ble_Status() {
        Log.d(TAG, "onDetachFragment_Ble_Status");
        bleDisconnectItem.setVisible(false);
        //tabLayout_ble.setVisibility(View.GONE);
    }

    @Override
    public void onCreateViewFragment_SN_Setting() {
        Log.d(TAG, "onCreateViewFragment_SN_Setting");
        bleDisconnectItem.setVisible(true);
    }

    @Override
    public void onDetachFragment_SN_Setting() {
        Log.d(TAG, "onDetachFragment_SN_Setting");
        bleDisconnectItem.setVisible(false);
    }

    @Override
    public void onCreateViewFragment_CDS_Setting() {
        Log.d(TAG, "onCreateViewFragment_CDS_Setting");
        bleDisconnectItem.setVisible(true);
    }

    @Override
    public void onDetachFragment_CDS_Setting() {
        Log.d(TAG, "onDetachFragment_CDS_Setting");
        bleDisconnectItem.setVisible(false);
    }


    // toolbar 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.actionbar_bluetooth, menu);
        ScanItem = menu.findItem(R.id.action_bar_scanBle);
        reFreshItem = menu.findItem(R.id.action_bar_scanRefresh);
        bleDisconnectItem = menu.findItem(R.id.action_bar_bleDisconnect);

        return true;
    }

    public void ble_stopscanning() {
        ScanItem.setTitle(R.string.ble_main_scanItem_scan);
    }


    //toolbar 기능 선택시
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.bluetoothFragmentSpace);
        fragment_Ble_Scan fragment_bleScan = null;
        // fragment 별 툴바에 기능 있을 시 추가
        if (fragment instanceof fragment_Ble_Scan) {
            fragment_bleScan = (fragment_Ble_Scan) getSupportFragmentManager().findFragmentById(R.id.bluetoothFragmentSpace);
        }

        switch (item.getItemId()) {
            case R.id.action_bar_scanBle:

                if (scanningFlag) {
                    fragment_bleScan.stopScan();
                    ScanItem.setTitle(R.string.ble_main_scanItem_scan);
                } else {
                    fragment_bleScan.Scan();
                    ScanItem.setTitle(R.string.ble_main_scanItem_stopScanning);
                }

                return true;
            case R.id.action_bar_scanRefresh:

                fragment_bleScan.reFresh();
                ScanItem.setTitle(R.string.ble_main_scanItem_stopScanning);

                return true;

            case R.id.action_bar_bleDisconnect:
                disconnectGattServer("bleMainActivity - onOptionsItemSelected - action_bar_bleDisconnect");
                fragmentChange("fragment_ble_beginning");

            case android.R.id.home:
                Toast.makeText(mBleContext, "Test Success", Toast.LENGTH_SHORT).show();
                return true;


            default:
                return super.onOptionsItemSelected(item);

        }
    }


    // toolbar 왼쪽 버튼의 기능 및 이미지 변경.
    public static void navigation_icon_Change(String fragment) {
        switch (fragment) {
            case "beggining":
                drawerToggle = new ActionBarDrawerToggle(mBleMain, bleDrawerLayout, toolbarMain, R.string.app_name, R.string.app_name);
                drawerToggle.syncState(); // 메뉴 버튼 추가
                bleDrawerLayout.addDrawerListener(drawerToggle); // 삼선 메뉴 사용 시 뱅그르르 돈다.
                toolbarMain.setNavigationIcon(R.drawable.toolbar_icon);
                toolbar_title.setText("MSL Technology");
                break;
            case "scan":

                toolbarMain.setNavigationOnClickListener(v -> mBleMain.onBackPressed());

                toolbarMain.setNavigationIcon(R.drawable.ble_fragment_back);
                break;
            case "function":
                drawerToggle = new ActionBarDrawerToggle(mBleMain, bleDrawerLayout, toolbarMain, R.string.app_name, R.string.app_name);
                drawerToggle.syncState(); // 메뉴 버튼 추가
                bleDrawerLayout.addDrawerListener(drawerToggle); // 삼선 메뉴 사용 시 뱅그르르 돈다.
                toolbarMain.setNavigationIcon(R.drawable.toolbar_icon);
                toolbar_title.setText(selectedSerialNum);
                break;

        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    boolean testBool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.ble_activity_main);

        mBleContext = this;
        mBleMain = this;

        //언어설정
        SharedPreferences sfSideBar = getSharedPreferences("option_data", MODE_PRIVATE);
        Locale systemLocale = mBleContext.getResources().getConfiguration().locale; //시스템 설정 상태를 가져옴
        String languageState = sfSideBar.getString("language_mode", systemLocale.getLanguage()); //시스템 언어를 가져옴
        Log.d(TAG, "System Language : " + languageState);
        tLanguage = languageState; //처음에는 시스템에 설정된 언어로, 이후에는 저장된 언어
        if (tLanguage != null) setLocale(tLanguage); //언어 설정

        toolbarMain = (Toolbar) findViewById(R.id.ble_toolbar_main);
        toolbarMain.setTitle("");
        toolbarMain.setTitleTextColor(getResources().getColor(R.color.ble_toolbarmain));

        toolbar_title = findViewById(R.id.toolbar_title_ble);

        setSupportActionBar(toolbarMain);

        // toolbar 왼쪽 버튼 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_reorder_24px);


        navigation_Setting();


        /*toolbarMain.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (testBool) {
                    navigation_Setting();
                    bleDrawerLayout.removeDrawerListener(drawerToggle);
                    *//*setSupportActionBar(toolbarMain);
                    Drawable drawable = ContextCompat.getDrawable(mBleContext,R.drawable.toolbar_icon);
                    toolbarMain.setOverflowIcon(drawable);*//*
                    testBool = false;

                } else {
                    Toast.makeText(mBleContext, "Test Success", Toast.LENGTH_SHORT).show();
                    testBool = true;
                }
                Toast.makeText(mBleContext, "Touch", Toast.LENGTH_SHORT).show();
            }
        });*/


        IntentFilter filter1 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver1, filter1);

        /*IntentFilter filter2 = new IntentFilter();
        filter2.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter2.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter2.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2, filter2);*/

        IntentFilter filter3 = new IntentFilter();
        filter3.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter3.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(mBroadcastReceiver3, filter3);

        permission_check();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // setLegacy 값 안 바꾸면 장거리 블루투스 못찾음(광고형 블루투스를 못봄)
            settings = new ScanSettings.Builder()
                    .setLegacy(false)
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();

            Log.d(TAG, "BluetoothAdapter isLe2MPhySupported() : " + mBluetoothAdapter.isLe2MPhySupported());
            Log.d(TAG, "BluetoothAdapter isLeCodedPhySupported() : " + mBluetoothAdapter.isLeCodedPhySupported());
            Log.d(TAG, "BluetoothAdapter isLeExtendedAdvertisingSupported() : " + mBluetoothAdapter.isLeExtendedAdvertisingSupported());
            Log.d(TAG, "BluetoothAdapter isLePeriodicAdvertisingSupported() : " + mBluetoothAdapter.isLePeriodicAdvertisingSupported());
            Log.d(TAG, "BluetoothSetting getLegacy() : " + settings.getLegacy());
            Log.d(TAG, "BluetoothSetting getPhy() : " + settings.getPhy());
            Log.d(TAG, "BluetoothSetting getScanMode() : " + settings.getScanMode());
            Log.d(TAG, "BluetoothSetting getScanResultType() : " + settings.getScanResultType());
            Log.d(TAG, "BluetoothSetting getCallbackType() : " + settings.getCallbackType());
        }

        // 특정 장치만 스캔하도록 할 수 있다. => scanFilter 부분 - .setServiceUuid()대신 .setDeviceAddress(MAC_ADDR)를 사용해 Uuid 말고 특정 mac address만 스캔
        filters.add(scanFilter);

        //region  layout 정의

        //endregion

        Log.d(TAG, "fragment 작업");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.bluetoothFragmentSpace, new fragment_Ble_Beginning());
        ft.commit();

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.d(TAG, "지원 안함.....");
        }
        //region layout 기능

        //endregion
    }


    void navigation_Setting() {
        // 사이드바 관련
        navigationView = findViewById(R.id.rtu_sidebar);
        navigationView.setItemIconTintList(null);
        log_Listview = navigationView.findViewById(R.id.lv_Navigation_Log);
        ll_navigation_move = navigationView.findViewById(R.id.ll_Navigation_Move);
        ll_navigation_log = navigationView.findViewById(R.id.ll_Navigation_Log);
        ll_navigation_GPS = navigationView.findViewById(R.id.ll_navigation_gps);
        ll_navigation_GPS.setVisibility(View.GONE);
        log_Refresh();

        /*Menu menu = navigationView.getMenu();
        menu.findItem(R.id.menu_bluetooth).setVisible(false);*/

        Display display = mBleContext.getDisplay();
        Point size = new Point();
        display.getSize(size);

        int x_log_on = Math.toIntExact(Math.round((size.x * 0.8)));
        int x_log_off = Math.toIntExact(Math.round((size.x * 0.65)));

        // 초기 화면 크기 결정
        navigationView.getLayoutParams().width = x_log_off;

        // Ble 이동
        navigationView.findViewById(R.id.ll_Navigation_Move_Ble).setOnClickListener(
                new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        Intent intent = new Intent(mBleContext, BleMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        // RTU로 이동
        navigationView.findViewById(R.id.ll_Navigation_Move_RTU).setOnClickListener(
                new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        Intent intent = new Intent(mBleContext, RTUMainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                });

        // 언어변환 키기
        navigationView.findViewById(R.id.ll_Navigation_Show_Language).setOnClickListener(
                new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        FragmentManager fm = getSupportFragmentManager();
                        dialogFragment_Ble_Beginning_LanguageChange customDialogLanguageChange = new dialogFragment_Ble_Beginning_LanguageChange();
                        customDialogLanguageChange.show(fm, "fragment_beginning_dialog_LanguageChange");
                    }
                });

        // FL List 창 키기
        navigationView.findViewById(R.id.ll_Navigation_Show_FL_List).setOnClickListener(
                new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        FragmentManager fm = getSupportFragmentManager();
                        dialogFragment_Ble_Setting_FL_Setting customDialog_FL_Setting = new dialogFragment_Ble_Setting_FL_Setting();
                        customDialog_FL_Setting.show(fm, "dialogFragment_Ble_Setting_FL_Setting");
                    }
                });

        // Log 보기
        navigationView.findViewById(R.id.ll_Navigation_Show_Log).setOnClickListener(v -> {
            navigationView.getLayoutParams().width = x_log_on;
            ll_navigation_log.setVisibility(View.VISIBLE);
            ll_navigation_move.setVisibility(View.GONE);
        });

        // Log 끄기
        navigationView.findViewById(R.id.btn_Navigation_Log_Close).setOnClickListener(v -> {
            navigationView.getLayoutParams().width = x_log_off;
            ll_navigation_log.setVisibility(View.INVISIBLE);
            ll_navigation_move.setVisibility(View.VISIBLE);
        });

        // GPS 설정
        navigationView.findViewById(R.id.ll_navigation_gps).setOnClickListener(v -> {
            BlewriteData(DATA_REQUEST_INFORMATION);

            handler.postDelayed(() -> {
                FragmentManager fm = getSupportFragmentManager();
                dialogFragment_ble_Setting_GPS_Change customDialog_GPS_Change = new dialogFragment_ble_Setting_GPS_Change();
                customDialog_GPS_Change.show(fm, "dialogFragment_ble_Setting_GPS_Change");
            }, 200);


        });


        bleDrawerLayout = findViewById(R.id.ble_drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, bleDrawerLayout, toolbarMain, R.string.app_name, R.string.app_name);
        drawerToggle.syncState(); // 메뉴 버튼 추가
        bleDrawerLayout.addDrawerListener(drawerToggle); // 삼선 메뉴 사용 시 뱅그르르 돈다.

        // custom 사이드바 사용 전
       /* // 사이드바(네비게이션) 아이템 선택 시
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                Log.d(TAG, "onNavigationItemSelected : " + item.getItemId());
                switch (item.getItemId()) {
                    case R.id.menu_bluetooth:
                        intent = new Intent(mBleContext, BleMainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.menu_rtu:
                        intent = new Intent(mBleContext, RTUMainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.menu_language:
                        FragmentManager fm = getSupportFragmentManager();
                        dialogFragment_Ble_Beginning_LanguageChange customDialogLanguageChange = new dialogFragment_Ble_Beginning_LanguageChange();
                        customDialogLanguageChange.show(fm, "fragment_beginning_dialog_LanguageChange");
                        break;
                    default:
                        View itemView = item.getActionView();
                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(mBleContext, "Test Success", Toast.LENGTH_SHORT).show();
                            }
                        });
                }
                // 사이드바 닫기
                bleMainLayout.closeDrawer(navigationView);

                return false;
            }
        });*/
    }


    public static void navigation_GPS_Visible(boolean visble) {
        if (visble) {
            ll_navigation_GPS.setVisibility(View.VISIBLE);
        } else {
            ll_navigation_GPS.setVisibility(View.GONE);
        }
    }

    // 일반적 시스템 데이터일 경우
    public static void logData_Ble(String data) {
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("hh:mm:ss");
        String getTime = simpleDate.format(mDate);

        Log.d(TAG, "log_data : " + getTime);
        log_listViewAdapter.addItem(getTime, data);
        //log_listViewAdapter.refreshAdapter(log_Listview);
        //log_listViewAdapter.notifyDataSetChanged();

    }

    // 특정 데이터 경우(read, write, error)
    public static void logData_Ble(String data, String color) {
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("hh:mm:ss");
        String getTime = simpleDate.format(mDate);

        Log.d(TAG, "log_data : " + getTime);

        if (data.contains("*")) {
            data = data.substring(0, data.indexOf("*"));
            Log.d(TAG, "data *  : " + data);
        }

        String[] dataArr = data.split(DATA_SIGN_COMMA);

        // 각 특정 별 데이터 별 확인해서 로그 값을 다르게 나타냄(해당 데이터 값은 안보여줌)
        if (color.equals("read")) {
            if (dataArr[0].contains(DATA_TYPE_PS)) {
                log_listViewAdapter.addItem(getTime, "Request a Password", color);
                //log_Listview.requestLayout();
                //log_listViewAdapter.notifyDataSetChanged();
                return;
            } else if (dataArr[0].contains(DATA_TYPE_LISTS)) {
                switch (dataArr[1]) {
                    case "S":
                        switch (dataArr[2]) {
                            case DATA_TYPE_BTV:
                                log_listViewAdapter.addItem(getTime, "Battery Status Request Confirm\n" + data, color);
                                break;
                            case DATA_TYPE_SLV:
                                log_listViewAdapter.addItem(getTime, "Solar Status V Request Confirm\n" + data, color);
                                break;
                            case DATA_TYPE_SLC:
                                log_listViewAdapter.addItem(getTime, "Solar Status A Request Confirm\n" + data, color);
                                break;
                        }
                        break;
                    default:
                        log_listViewAdapter.addItem(getTime, "Status Request Confirm : \n" + data, color);
                        break;
                }
                //log_listViewAdapter.notifyDataSetChanged();
                //log_Listview.requestLayout();
                return;
            } else if (dataArr[0].contains(DATA_TYPE_LICMD)) {
                switch (dataArr[1]) {
                    case "2":
                        log_listViewAdapter.addItem(getTime, "Test - Lamp ON Confirm", color);
                        break;
                    case "3":
                        log_listViewAdapter.addItem(getTime, "Test - Lamp OFF Confirm", color);
                        break;
                    case "4":
                        log_listViewAdapter.addItem(getTime, "Test - Reset Confirm", color);
                        break;
                    case "5":
                        log_listViewAdapter.addItem(getTime, "Test - Lamp FIXED Confirm", color);
                        break;
                    case "S":
                        switch (dataArr[2]) {
                            case DATA_TYPE_SID:
                                log_listViewAdapter.addItem(getTime, "Lantern ID : " + dataArr[3] + " Confirm", color);
                                break;
                            case DATA_TYPE_GP0:
                                log_listViewAdapter.addItem(getTime, "GPS Only Night Confirm", color);
                                break;
                            case DATA_TYPE_GP1:
                                log_listViewAdapter.addItem(getTime, "GPS Always Confirm", color);
                                break;
                            default:
                                log_listViewAdapter.addItem(getTime, "FL : " + dataArr[2] + " Confirm", color);
                        }
                        break;
                }
                //log_listViewAdapter.notifyDataSetChanged();
                //log_Listview.requestLayout();
                return;
            } else if (dataArr[0].contains(DATA_TYPE_LISET)) {
                log_listViewAdapter.addItem(getTime, "Information Data Confirm\n" + data, color);
                //log_Listview.requestLayout();
                //log_listViewAdapter.notifyDataSetChanged();
                return;
            }

        } else if (color.equals("write")) {
            if (dataArr[0].contains(DATA_TYPE_PS)) {
                log_listViewAdapter.addItem(getTime, "Enter a Password", color);
                //log_Listview.requestLayout();
                //log_listViewAdapter.notifyDataSetChanged();
                return;
            } else if (dataArr[0].contains(DATA_TYPE_LICMD)) {
                switch (dataArr[1]) {
                    case "1":
                        log_listViewAdapter.addItem(getTime, "Status Request", color);
                        break;
                    case "2":
                        log_listViewAdapter.addItem(getTime, "Test - Lamp ON", color);
                        break;
                    case "3":
                        log_listViewAdapter.addItem(getTime, "Test - Lamp OFF", color);
                        break;
                    case "4":
                        log_listViewAdapter.addItem(getTime, "Test - Reset", color);
                        break;
                    case "5":
                        log_listViewAdapter.addItem(getTime, "Test - Lamp FIXED", color);
                        break;
                    case "S":
                        switch (dataArr[2]) {
                            case DATA_TYPE_BTV:
                                log_listViewAdapter.addItem(getTime, "Battery Status Request", color);
                                break;
                            case DATA_TYPE_SLV:
                                log_listViewAdapter.addItem(getTime, "Solar Status V Request", color);
                                break;
                            case DATA_TYPE_SLC:
                                log_listViewAdapter.addItem(getTime, "Solar Status A Request", color);
                                break;
                            case DATA_TYPE_SID:
                                log_listViewAdapter.addItem(getTime, "Lantern ID : " + dataArr[3], color);
                                break;
                            case DATA_TYPE_GP0:
                                log_listViewAdapter.addItem(getTime, "GPS Only Night", color);
                                break;
                            case DATA_TYPE_GP1:
                                log_listViewAdapter.addItem(getTime, "GPS Always", color);
                                break;
                            case DATA_TYPE_DEL:
                                log_listViewAdapter.addItem(getTime, "Delay Time : " + dataArr[3], color);
                                break;
                            default:
                                log_listViewAdapter.addItem(getTime, "FL : " + dataArr[2], color);
                                break;
                        }
                        //log_Listview.requestLayout();
                        return;
                    case "I":
                        log_listViewAdapter.addItem(getTime, "information Request", color);
                        //log_Listview.requestLayout();
                        return;
                }
                //log_listViewAdapter.notifyDataSetChanged();
                //log_Listview.requestLayout();
                return;
            }
        }

        log_listViewAdapter.addItem(getTime, data, color);
        //log_Listview.requestLayout();
        //log_listViewAdapter.notifyDataSetChanged();
    }

    public void log_Refresh() {
        log_listViewAdapter = new log_ListViewAdapter();
        log_Listview.setAdapter(log_listViewAdapter);
    }

    public void permission_message() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("권한 요청").setMessage("권한 항목에서 GPS 권한을 켜주시기 바랍니다.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent appDetail = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + mBleContext.getPackageName()));
                appDetail.addCategory(Intent.CATEGORY_DEFAULT);
                appDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(appDetail);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

    /*// 권한 확인(GPS 확인, 블루투스 확인)
    public void permission_check() {
        Log.d(TAG, "buile Version : " + Build.VERSION.SDK_INT);

        if (Build.VERSION.SDK_INT >= 29) {
            int permissionCheck = ContextCompat.checkSelfPermission(mBleMain,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionCheck < 0) {
                permission_message();
            }
        } else {
            int permissionCheck = ContextCompat.checkSelfPermission(mBleMain,
                    Manifest.permission.ACCESS_COARSE_LOCATION);

            Log.d(TAG, "GPS 권한 ACCESS_COARSE_LOCATION" + permissionCheck);

            //
            if (permissionCheck < 0) {
                permission_message();
            }
        }



        // gps on 시키는데 사용
        locationManager = (LocationManager) mBleContext.getSystemService(LOCATION_SERVICE);

        bluetoothManager = (BluetoothManager) mBleContext.getSystemService(Context.BLUETOOTH_SERVICE);

        //region Bluetooth 연결 시 필요한 사항
        //mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }*/

    public static void permission_check() {
        Log.d(TAG, "buile Version : " + Build.VERSION.SDK_INT);

        // 버전 별 권한 확인이 다름.
        if (Build.VERSION.SDK_INT >= 29) {
            int permissionCheck = ContextCompat.checkSelfPermission(mBleContext,
                    Manifest.permission.ACCESS_FINE_LOCATION);

            Log.d(TAG, "GPS 권한 ACCESS_FINE_LOCATION" + permissionCheck);

            //
            if (permissionCheck < 0) {
                ActivityCompat.requestPermissions(mBleMain,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            int permissionCheck = ContextCompat.checkSelfPermission(mBleContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION);

            Log.d(TAG, "GPS 권한 ACCESS_COARSE_LOCATION" + permissionCheck);

            if (permissionCheck < 0) {
                ActivityCompat.requestPermissions(mBleMain,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }

        // gps on 시키는데 사용
        locationManager = (LocationManager) mBleContext.getSystemService(LOCATION_SERVICE);

        bluetoothManager = (BluetoothManager) mBleContext.getSystemService(Context.BLUETOOTH_SERVICE);

        //region Bluetooth 연결 시 필요한 사항
        //mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }


    // fragment 변화
    public void fragmentChange(String callFragment) {
        Fragment fr;

        switch (callFragment) {
            case "fragment_ble_beginning":
                fr = new fragment_Ble_Beginning();
                break;
            case "fragment_ble_scan":
                fr = new fragment_Ble_Scan();
                break;
            case "fragment_ble_password":
                fr = new fragment_Ble_Password();
                break;
            case "fragment_ble_function":
                fr = new fragment_Ble_Function();
                break;
            case "fragment_cds_setting":
                fr = new fragment_CDS_Setting();
                break;
            case "fragment_sn_setting":
                fr = new fragment_SN_Setting();
                break;
            default:
                fr = new fragment_Ble_Status();
                break;
        }

        try {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.bluetoothFragmentSpace, fr);
            fragmentTransaction.commit();
        } catch (Exception e) {
            Log.e(TAG, "fragmentChange 간 문제 발생" + e.toString());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case bluetooth_permission_check: // 블루투스 권한 확인
                if (resultCode == RESULT_OK) {
                    // 이후 GPS 상태 요청
                    checkGPSserviceOn();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle("권한 요청").setMessage("블루투스 권한 요청을 거절하셨습니다.\n블루투스 스캔 이용이 제한됩니다.\n다시 확인하시겠습니까?");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // 다시 확인한다 하면.
                            checkBluetoothPermission();
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // 이후 GPS 상태 요청
                            checkGPSserviceOn();
                        }
                    });
                    AlertDialog alertDialog = builder.create();

                    alertDialog.show();
                }
                break;

        }
    }

    /*// 권한 요청 시
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 허용 시
                    Log.d(TAG, "RequestPermissions GPS : 허용");

                } else {
                    // 거부 시
                    Log.d(TAG, "RequestPermissions GPS : 거부");
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle(R.string.ble_main_checkPermission_GPS_fail_alertDialog_title).setMessage(R.string.ble_main_checkPermission_GPS_fail_alertDialog_message);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            Intent appDetail = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                            appDetail.addCategory(Intent.CATEGORY_DEFAULT);
                            appDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(appDetail);

                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    AlertDialog alertDialog = builder.create();

                    alertDialog.show();
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_READ_CONTACTS_RE: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle(R.string.ble_main_checkPermission_GPS_reFail_alertDialog_title).setMessage(R.string.ble_main_checkPermission_GPS_reFail_alertDialog_message);
                builder.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent appDetail = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                        appDetail.addCategory(Intent.CATEGORY_DEFAULT);
                        appDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(appDetail);

                    }
                });


                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog alertDialog = builder.create();

                alertDialog.show();

            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }*/

    // 권한 요청 시
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 허용 시
                    Log.d(TAG, "RequestPermissions GPS : 허용");

                } else {
                    // 거부 시
                    Log.d(TAG, "RequestPermissions GPS : 거부");
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle(R.string.ble_main_checkPermission_GPS_fail_alertDialog_title).setMessage(R.string.ble_main_checkPermission_GPS_fail_alertDialog_message);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            if (Build.VERSION.SDK_INT >= 29) {
                                // 필요없어 보임
                                /*int permissionCheck = ContextCompat.checkSelfPermission(mBleMain,
                                        Manifest.permission.ACCESS_FINE_LOCATION);*/

                                ActivityCompat.requestPermissions(mBleMain,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_READ_CONTACTS_RE);


                            } else {
                                ActivityCompat.requestPermissions(mBleMain,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_READ_CONTACTS_RE);
                            }

                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    AlertDialog alertDialog = builder.create();

                    alertDialog.show();
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_READ_CONTACTS_RE: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle(R.string.ble_main_checkPermission_GPS_reFail_alertDialog_title).setMessage(R.string.ble_main_checkPermission_GPS_reFail_alertDialog_message);
                builder.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent appDetail = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                        appDetail.addCategory(Intent.CATEGORY_DEFAULT);
                        appDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(appDetail);

                    }
                });


                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog alertDialog = builder.create();

                alertDialog.show();

            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    // 블루투스 권한 확인
    public static void checkBluetoothPermission() {
        // 블루투스 권한 확인 시작
        Intent intent;

        if (mBluetoothAdapter.isEnabled()) {
            BluetoothStatus = "On";
            // 이후 GPS 상태 요청
            checkGPSserviceOn();
        } else {
            // 블루투스 활성화 하도록
            intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mBleMain.startActivityForResult(intent, bluetooth_permission_check);
        }
        // 블루투스 권한 확인 끝
    }

    // 위치정보(GPS) 확인
    public static void checkGPSserviceOn() {

        // 위치정보 on 확인 시작
        Intent intent;

        // 위치정보 설정 Intent
        intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

        if (!locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(mBleContext);

            builder.setTitle(R.string.ble_main_checkPermission_GPS_alertDialog_title).setMessage(R.string.ble_main_checkPermission_GPS_alertDialog_message);

            builder.setPositiveButton("OK", (dialog, id) -> mBleMain.startActivity(intent));

            builder.setNegativeButton("Cancel", (dialog, id) -> {

            });
            AlertDialog alertDialog = builder.create();

            alertDialog.show();

        }
        // 위치정보 on 확인 끝
    }

    // 블루투스 각종 기능.
    private BluetoothGattCallback gattClientCallback = new BluetoothGattCallback() {


        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (status == BluetoothGatt.GATT_FAILURE) {
                disconnectGattServer("BleMainActivity - gattClientCallback - GATT_FAILURE");
                fragmentChange("fragment_ble_beginning");
                return;
            } else if (status != BluetoothGatt.GATT_SUCCESS) {
                logData_Ble("Bluetooth Disconnect", "error");
                disconnectGattServer("BleMainActivity - gattClientCallback - NO_GATT_SUCCESS");
                fragmentChange("fragment_ble_beginning");
                return;
            }
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d(TAG, "Connected to the GATT server");
                handler.sendEmptyMessage(ConnectSuccess);
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                disconnectGattServer("BleMainActivity - gattClientCallback - STATE_DISCONNECTED");

                fragmentChange("fragment_ble_beginning");
            }
        }

        // 연결이 완료될 경우
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);

            if (status != BluetoothGatt.GATT_SUCCESS) {
                logData_Ble("Device service discovery failed", "error");
                Log.e(TAG, "Device service discovery failed, status: $status");

                return;
            }
            Log.d(TAG, "Services discovery is successful");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(TAG, "gatt.readPhy()");
                gatt.readPhy();
            }


            // uuid, Characteristic 모르면
            BluetoothDevice device = gatt.getDevice();
            String address = device.getAddress();


            List<BluetoothGattService> services = gatt.getServices();

            if (services != null) {
                for (BluetoothGattService service : services) {
                    List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                    if (characteristics != null) {
                        for (BluetoothGattCharacteristic characteristic : characteristics) {
                            // bluetooth 5 및 일반적 블루투스
                            if (characteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_WRITE ||
                                    characteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) {
                                cmdCharacteristic = characteristic;
                                if (cmdCharacteristic == null) {
                                    Log.d(TAG, "onServicesDiscovered - cmdCharacteristic : null");
                                    // 해당 값을 하면 되긴하는데...처음부터 해서 에러 메세지(BleWrite부분에서) 하지않게하기
                                    cmdCharacteristic = BluetoothUtils.findCommandCharacteristic(bleGatt);
                                }

                            } else if (characteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_NOTIFY) {
                                respCharacteristic = characteristic;
                            }

                            // 등명기 블루투스(서비스 이름(PROPERTY_NOTIFY)를 못찾음, 그래서 uuid로 연결)
                            if (characteristic.getUuid().toString().equals(CHARACTERISTIC_COMMAND_STRING_4)) {
                                cmdCharacteristic = characteristic;
                            } else if (characteristic.getUuid().toString().equals(CHARACTERISTIC_RESPONSE_STRING_4)) {
                                respCharacteristic = characteristic;
                            }
                        }
                    }

                }
            }
            // read 케릭터 못찾으면 연결 종료
            if (respCharacteristic == null) {
                logData_Ble("respCharacteristic null", "error");
                Log.e(TAG, "respCharacteristic null");
                disconnectGattServer("BleMainActivity - onServicesDiscovered - respCharacteristic null");
                fragmentChange("fragment_ble_beginning");
                return;
            }

            gatt.setCharacteristicNotification(respCharacteristic, true);
            BluetoothGattDescriptor descriptor = respCharacteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(descriptor);

            for (BluetoothGattCharacteristic characteristic : findBLECharacteristics(gatt)) {
                Log.d(TAG, "charateristic uuid : " + characteristic.getUuid().toString());
            }

            // uuid, Characteristic 알고있다면
            /*BluetoothGattCharacteristic respCharacteristic = findResponseCharacteristic(gatt);
            if(respCharacteristic == null){
                Log.e(TAG, "Unable to find cmd characteristic");
                disconnectGattServer();
                return;
            }
            gatt.setCharacteristicNotification(respCharacteristic, true);
            BluetoothGattDescriptor descriptor = respCharacteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(descriptor);*/
        }

        // 블루투스 데이터 읽는 데이터가 변경되면 감지
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            readCharacteristic(characteristic);
        }

        // 블루투스 데이터 보낼 시
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //logData_Ble("Characteristic written successfully");
                Log.d(TAG, "Characteristic written successfully : " + characteristic.getStringValue(0));

            } else {
                logData_Ble("Characteristic write unsuccessful", "error");
                Log.e(TAG, "Characteristic write unsuccessful, status: $status");
                disconnectGattServer("BleMainActivity - gattClientCallback - onCharacteristicWrite unsuccessful");
                fragmentChange("fragment_ble_beginning");
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                logData_Ble("Characteristic read successfully");
                Log.d(TAG, "Characteristic read successfully");
                readCharacteristic(characteristic);
            } else {
                logData_Ble("Characteristic read unsuccessful", "error");
                Log.e(TAG, "Characteristic read unsuccessful, status: $status");
                // Trying to read from the Time Characteristic? It doesnt have the property or permissions
                // set to allow this. Normally this would be an error and you would want to:
                // disconnectGattServer();
            }
        }

        // 블루투스 데이터 읽기 시 처리
        private void readCharacteristic(BluetoothGattCharacteristic characteristic) {
            String readCharacter = characteristic.getStringValue(0);

            // 블루투스가 한번에 2개 이상을 동시에 보내는걸 대비하여 먼저 큐에 넣고 나중에 정리/
            readDataQueue.offer(readCharacter);

            // 들어온 데이터를 토탈에 합침. 이후 확인
            for (int i = 0; i < readDataQueue.size(); i++) {
                readDataTotal += readDataQueue.poll();
            }
            Log.d(TAG, "readCharacteristic String : " + new String(characteristic.getValue()));
            Log.d(TAG, "readDataTotal : " + readDataTotal);
            handler.sendEmptyMessage(readDataSuccess);
        }
    };

    public String byteArrayToHexaString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {
            if (b == '\n')
                Log.d(TAG, "byteArrayToHexaString n 포함 : " + b);
            if (b == '\r')
                Log.d(TAG, "byteArrayToHexaString r 포함 : " + b);
        }

        for (byte b : bytes) {
            sb.append(String.format("%02X", b & 0xff));
        }

        return sb.toString();
    }


    // 블루투스 연결 끊기
    public static void disconnectGattServer(String route) {
        //logData_Ble("Bluetooth Disconnect");
        Log.d(TAG, "disconnectGattServer - " + route);
        cmdCharacteristic = null;
        respCharacteristic = null;
        if (bleGatt != null) {
            BleConnecting = false;

            bleGatt.disconnect();
            bleGatt.close();
        }
    }

    // 연결 및 데이터 들어온거에 따른 결과
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                // 연결 되었을 경우

            } else if (msg.what == 1) {
                // 데이터가 들어왔을 때

                try {


                    // RTU 관련 데이터 들어올 경우($ 및 * 이 안들어감)
                    if (readDataTotal.contains("[ ConfMsg]") && readDataTotal.contains(">")) {

                        int configIndex = 0;
                        int lfIndex = 0;

                        configIndex = readDataTotal.indexOf("[ ConfMsg]");
                        lfIndex = readDataTotal.indexOf(">", configIndex);
                        while (configIndex < lfIndex) {

                            // \n 까지 포함
                            String readData = readDataTotal.substring(configIndex, lfIndex + 1);

                            // Log 용은 삭제
                            String readDataLog = readDataTotal.substring(configIndex, lfIndex);

                            try {
                                readDataTotal = readDataTotal.substring(lfIndex + 1);
                            } catch (Exception e) {
                                readDataTotal = "";
                                logData_Ble("readData Error! - TotalReadData", "error");
                                Log.e(TAG, "fragment_BLE_RTU_Status readData Error : " + e.toString());
                            }

                            logData_Ble(readDataLog, "read");

                            // 들어온 데이터값을 해당 프래그먼트로 보내기(그쪽에서 처리)
                            if (currentFragment instanceof fragment_Ble_Function) {
                                Log.d(TAG, "fragment_Ble_Function OK!");
                                fragment_Ble_Function fragment_ble_function = (fragment_Ble_Function) getSupportFragmentManager().findFragmentById(R.id.bluetoothFragmentSpace);
                                fragment_ble_function.readData(readData);
                            } else if (currentFragment instanceof fragment_Ble_Password) {
                                Log.d(TAG, "fragment_Ble_password OK!");
                                fragment_Ble_Password fragment_ble_password = (fragment_Ble_Password) getSupportFragmentManager().findFragmentById(R.id.bluetoothFragmentSpace);
                                fragment_ble_password.readData(readData);
                            } else if (currentFragment instanceof fragment_CDS_Setting) {
                                Log.d(TAG, "fragment_Ble_CDS_Setting OK!");
                                fragment_CDS_Setting fragment_cds_setting = (fragment_CDS_Setting) getSupportFragmentManager().findFragmentById(R.id.bluetoothFragmentSpace);
                                fragment_cds_setting.readData(readData);
                            } else if (currentFragment instanceof fragment_SN_Setting) {
                                Log.d(TAG, "fragment_Ble_SN_Setting OK!");
                                fragment_SN_Setting fragment_sn_setting = (fragment_SN_Setting) getSupportFragmentManager().findFragmentById(R.id.bluetoothFragmentSpace);
                                fragment_sn_setting.readData(readData);
                            }

                            // subString 했는데도 해당 데이터가 있을 경우 while 벗어나지않고 한번더 한다.
                            configIndex = readDataTotal.indexOf("[ ConfMsg]");
                            lfIndex = readDataTotal.indexOf("\n", configIndex);
                            if (configIndex < 0 | lfIndex < 0) {
                                break;
                            }

                        }
                    }

                    if (readDataTotal.contains("[ModemMsg]") && readDataTotal.contains(">")) {

                        int ModemMsgIndex = 0;
                        int lfIndex = 0;

                        ModemMsgIndex = readDataTotal.indexOf("[ModemMsg]");
                        lfIndex = readDataTotal.indexOf(">", ModemMsgIndex);
                        while (ModemMsgIndex < lfIndex) {

                            // \n 까지 포함
                            String readData = readDataTotal.substring(ModemMsgIndex, lfIndex + 1);

                            // Log 용은 삭제
                            String readDataLog = readDataTotal.substring(ModemMsgIndex, lfIndex);

                            try {
                                readDataTotal = readDataTotal.substring(lfIndex + 1);
                            } catch (Exception e) {
                                readDataTotal = "";
                                logData_Ble("readData Error! - TotalReadData", "error");
                                Log.e(TAG, "fragment_BLE_RTU_Status readData Error : " + e.toString());
                            }

                            logData_Ble(readDataLog, "read");

                            // 들어온 데이터값을 해당 프래그먼트로 보내기(그쪽에서 처리)
                            if (currentFragment instanceof fragment_Ble_Function) {
                                Log.d(TAG, "fragment_Ble_Function OK!");
                                fragment_Ble_Function fragment_ble_function = (fragment_Ble_Function) getSupportFragmentManager().findFragmentById(R.id.bluetoothFragmentSpace);
                                fragment_ble_function.readData(readData);
                            } else if (currentFragment instanceof fragment_Ble_Password) {
                                Log.d(TAG, "fragment_Ble_password OK!");
                                fragment_Ble_Password fragment_ble_password = (fragment_Ble_Password) getSupportFragmentManager().findFragmentById(R.id.bluetoothFragmentSpace);
                                fragment_ble_password.readData(readData);
                            } else if (currentFragment instanceof fragment_CDS_Setting) {
                                Log.d(TAG, "fragment_Ble_CDS_Setting OK!");
                                fragment_CDS_Setting fragment_cds_setting = (fragment_CDS_Setting) getSupportFragmentManager().findFragmentById(R.id.bluetoothFragmentSpace);
                                fragment_cds_setting.readData(readData);
                            } else if (currentFragment instanceof fragment_SN_Setting) {
                                Log.d(TAG, "fragment_Ble_SN_Setting OK!");
                                fragment_SN_Setting fragment_sn_setting = (fragment_SN_Setting) getSupportFragmentManager().findFragmentById(R.id.bluetoothFragmentSpace);
                                fragment_sn_setting.readData(readData);
                            }

                            // subString 했는데도 해당 데이터가 있을 경우 while 벗어나지않고 한번더 한다.
                            ModemMsgIndex = readDataTotal.indexOf("[ModemMsg]");
                            lfIndex = readDataTotal.indexOf("\n", ModemMsgIndex);
                            if (ModemMsgIndex < 0 | lfIndex < 0) {
                                break;
                            }

                        }
                    }

                    // 그 외 경우
                    if (readDataTotal.contains("\\n") || readDataTotal.contains("\\r")) {
                        Log.d(TAG, "readDataTotal contain n or r " + readDataTotal);
                        readDataTotal = readDataTotal.replaceAll("(\\n|\\r)", "");
                    }
                    if (readDataTotal.contains("\n") || readDataTotal.contains("\r")) {
                        Log.d(TAG, "readDataTotal contain n or r 2" + readDataTotal);
                        readDataTotal = readDataTotal.replaceAll("(\n|\r)", "");
                    }

                    // $ 및 * 등이 포함됐는지.(처음과 끝)
                    if (readDataTotal.indexOf(DATA_SIGN_START) > -1
                            && readDataTotal.indexOf(DATA_SIGN_CHECKSUM) > -1) {
                        // $이 *보다 먼저 들어왔는지(데이터 순서 꼬였는지)
                        if (readDataTotal.indexOf(DATA_SIGN_START) < readDataTotal.indexOf(DATA_SIGN_CHECKSUM)) {

                            if (String.valueOf(readDataTotal.charAt(readDataTotal.length() - 3)).equals(DATA_SIGN_CHECKSUM)) {

                                // * 뒤의 체크섬값이 2자리로 잘 들어왔는지
                                if (readDataTotal.substring(readDataTotal.indexOf(DATA_SIGN_CHECKSUM) + 1, readDataTotal.indexOf(DATA_SIGN_CHECKSUM) + 3).length() == 2) {
                                    //데이터 처음과 끝 받기 완료

                                    String data = readDataTotal.substring(readDataTotal.indexOf(DATA_SIGN_START), readDataTotal.indexOf(DATA_SIGN_CHECKSUM) + 3);
                                    Log.d(TAG, "정리된 Data : " + data);
                                    String[] dataArr = data.split(DATA_SIGN_COMMA);
                                    // 가장 마지막 데이터(~~~~*AH)의 체크섬이후 데이터 받아오기(체크섬 값 부분)
                                    String csData = dataArr[dataArr.length - 1].substring(dataArr[dataArr.length - 1].indexOf(DATA_SIGN_CHECKSUM) + 1);

                                    // 체크섬이 맞는지
                                    if (csData.equals(ToCheckSum(data.substring(data.indexOf(DATA_SIGN_START), data.indexOf(DATA_SIGN_CHECKSUM) + 1)))) {
                                        Log.d(TAG, "CheckSum OK!");
                                        currentFragment = getSupportFragmentManager().findFragmentById(R.id.bluetoothFragmentSpace);

                                        logData_Ble(data, "read");

                                        // 들어온 데이터값을 해당 프래그먼트로 보내기(그쪽에서 처리)
                                        if (currentFragment instanceof fragment_Ble_Function) {
                                            Log.d(TAG, "fragment_Ble_Function OK!");
                                            fragment_Ble_Function fragment_ble_function = (fragment_Ble_Function) getSupportFragmentManager().findFragmentById(R.id.bluetoothFragmentSpace);
                                            fragment_ble_function.readData(data);
                                        } else if (currentFragment instanceof fragment_Ble_Password) {
                                            Log.d(TAG, "fragment_Ble_password OK!");
                                            fragment_Ble_Password fragment_ble_password = (fragment_Ble_Password) getSupportFragmentManager().findFragmentById(R.id.bluetoothFragmentSpace);
                                            fragment_ble_password.readData(data);
                                        } else if (currentFragment instanceof fragment_CDS_Setting) {
                                            Log.d(TAG, "fragment_Ble_CDS_Setting OK!");
                                            fragment_CDS_Setting fragment_cds_setting = (fragment_CDS_Setting) getSupportFragmentManager().findFragmentById(R.id.bluetoothFragmentSpace);
                                            fragment_cds_setting.readData(data);
                                        } else if (currentFragment instanceof fragment_SN_Setting) {
                                            Log.d(TAG, "fragment_Ble_SN_Setting OK!");
                                            fragment_SN_Setting fragment_sn_setting = (fragment_SN_Setting) getSupportFragmentManager().findFragmentById(R.id.bluetoothFragmentSpace);
                                            fragment_sn_setting.readData(data);
                                        }

                                    }

                                    // 작업 끝.들어온 데이터 초기화
                                    readDataTotal = "";
                                }
                            } else {
                                logData_Ble("readData Error - checksum", "error");
                                Log.d(TAG, "readData Error - checksum - 3 / " + readDataTotal.length() + " and " + readDataTotal.charAt(readDataTotal.length() - 3));

                                readDataTotal = "";
                            }
                        } else {
                            // 데이터가 잘못 들어옴. 초기화
                            logData_Ble("readData Error - order", "error");
                            Log.d(TAG, "readData Error - order");
                            readDataTotal = "";
                        }
                    }
                    // 체크섬만 있을 경우(순서 이상함)
                    if (readDataTotal.indexOf(DATA_SIGN_CHECKSUM) > -1 && !(readDataTotal.indexOf(DATA_SIGN_START) > -1)) {
                        logData_Ble("readData Error - No include DATA_SIGN_START", "error");
                        Log.d(TAG, "readData Error - no include DATA_SIGN_START");
                        readDataTotal = "";
                    }

                } catch (Exception e) {
                    logData_Ble("readData Error! - " + e.toString(), "error");
                }

            }

        }
    };

    public static void BlewriteData(String data) {

        if (cmdCharacteristic == null) {
            logData_Ble("cmdCharacteristic : null", "error");
            Log.d(TAG, "BlewriteData - cmdCharacteristic : null");
            cmdCharacteristic = BluetoothUtils.findCommandCharacteristic(bleGatt);
        }

        // 연결이 끊겨있다면 종료
        if (cmdCharacteristic == null) {
            logData_Ble("Unable to find cmd characteristic", "error");
            Log.e(TAG, "Unable to find cmd characteristic(writeData)");
            disconnectGattServer("BleMainActivity - BlewriteData - Unable to find cmd characteristic");
            return;
        }

        String sendBlewriteData = data + ToCheckSum(data);
        logData_Ble("Write : " + sendBlewriteData, "write");
        sendBlewriteData = sendBlewriteData + DATA_SIGN_CR + DATA_SIGN_LF;
        Log.d(TAG, "BleWriteData data : " + data + ", sendData : " + sendBlewriteData);

        // 데이터가 길면 한번에 보낼 수 있는 양을 초과해서 나눠서 보내야함.(5.0은 상관없지만...)
        if (sendBlewriteData.length() < 21) {
            cmdCharacteristic.setValue(sendBlewriteData.getBytes());

            Boolean success = bleGatt.writeCharacteristic(cmdCharacteristic);
            if (!success) {
                logData_Ble("Failed to write command", "error");
                Log.e(TAG, "Failed to write command");
            }
        } else {
            Log.d(TAG, "BleWriteData data length : " + sendBlewriteData.length());


            String finalSendBlewriteData = sendBlewriteData;

            Log.d(TAG, "Test Data 1 : " + finalSendBlewriteData);

            for (int i = 0; i < (finalSendBlewriteData.length() / 20) + 1; i++) {
                if (finalSendBlewriteData.length() - (i * 20) < 20) {
                    cmdCharacteristic.setValue(finalSendBlewriteData.substring(i * 20).getBytes());
                } else {
                    if (((i + 1) * 20) < finalSendBlewriteData.length()) {
                        Log.d(TAG, "Test Data : " + ((i + 1) * 20) + " , length : " + finalSendBlewriteData.length());
                        cmdCharacteristic.setValue(finalSendBlewriteData.substring(i * 20, (i + 1) * 20).getBytes());
                    } else {
                        cmdCharacteristic.setValue(finalSendBlewriteData.substring(i * 20).getBytes());
                    }
                }

                Log.d(TAG, "Test Data 1-1");

                // 해당 write가 있어야 값을 보냄.
                Boolean success = bleGatt.writeCharacteristic(cmdCharacteristic);
                if (!success) {
                    logData_Ble("Failed to write command", "error");
                    Log.e(TAG, "Failed to write command");
                }

                // 0.1 초 정도 텀을 둬서 데이터 보내고 이어서 보내기
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void BlewriteDataNoChecksum(String data) {

        if (cmdCharacteristic == null) {
            logData_Ble("cmdCharacteristic : null", "error");
            Log.d(TAG, "BlewriteData - cmdCharacteristic : null");
            cmdCharacteristic = BluetoothUtils.findCommandCharacteristic(bleGatt);
        }

        // 연결이 끊겨있다면 종료
        if (cmdCharacteristic == null) {
            logData_Ble("Unable to find cmd characteristic", "error");
            Log.e(TAG, "Unable to find cmd characteristic(writeData)");
            disconnectGattServer("BleMainActivity - BlewriteData - Unable to find cmd characteristic");
            return;
        }

        String sendBlewriteData = data;
        logData_Ble("Write : " + sendBlewriteData, "write");
        sendBlewriteData = sendBlewriteData + DATA_SIGN_CR + DATA_SIGN_LF;
        Log.d(TAG, "BleWriteData data : " + data + ", sendData : " + sendBlewriteData);

        cmdCharacteristic.setValue(sendBlewriteData.getBytes());


        Boolean success = bleGatt.writeCharacteristic(cmdCharacteristic);
        if (!success) {
            logData_Ble("Failed to write command", "error");
            Log.e(TAG, "Failed to write command");
        }
    }

    public static String ToCheckSum(String str) {
        int csInt = 0;
        String csStr = "";
        //시작지점($)가 존재하며
        //끝 지점(*)가 존재하며
        //시작지점은 끝 지점보다 앞쪽에 위치하여야함
        if (str.contains(DATA_SIGN_START)
                && str.indexOf(DATA_SIGN_CHECKSUM) > -1
                && str.indexOf(DATA_SIGN_START) < str.indexOf(DATA_SIGN_CHECKSUM)) {
            for (int n = 1; n < str.indexOf(DATA_SIGN_CHECKSUM); n++) {
                csInt ^= str.charAt(n);
            }

            Log.d(TAG, "toCheckSum csInt : " + csInt);

            csStr = String.valueOf(Integer.toString(csInt, 16)).toUpperCase();
            if (csStr.length() == 1) {
                //한자리 수 인경우, 앞에 0을 추가
                csStr = "0" + csStr;
            }
            return csStr;
        } else {
            //체크섬을 하지 않음
            return "HH";
        }

    }


    // 블루투스가 꺼지는 중, 꺼짐 or 켜지는 중, 켜짐 상태 알려줌.
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        logData_Ble("Bluetooth OFF");
                        Log.d(TAG, "Bluetooth.STATE_OFF");
                        BluetoothStatus = "Off";
                        fragmentChange("fragment_ble_beginning");
                        Toast.makeText(mBleContext, getString(R.string.toastMsg_bluetooth_Off), Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "Bluetooth.STATE_TURNING_OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        logData_Ble("Bluetooth ON");
                        Log.d(TAG, "Bluetooth.STATE_ON");
                        BluetoothStatus = "On";
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "Bluetooth.STATE_TURNING_ON");
                        break;
                }

            }
        }
    };

    // 블루투스 검색 or 검색 중이 아닐 때
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "Bluetooth.SCAN_MODE_CONNECTABLE_DISCOVERABLE");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "Bluetooth.SCAN_MODE_CONNECTABLE");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "Bluetooth.SCAN_MODE_NONE");
                        break;
                }
            }
        }
    };

    // 블루투스 연결 시 or 해제 시
    private final BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    Log.d(TAG, "Bluetooth.ACTION_ACL_CONNECTED");
                    // 연결 시

                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    Log.d(TAG, "Bluetooth.ACTION_ACL_DISCONNECTED");
                    // 해제 시
                    break;
            }
        }
    };


    @Override
    public void onStart() {
        super.onStart();
        // 어플 실행 시 블루투스 On 요청
        checkBluetoothPermission();
    }

    @Override
    public void onResume() {
        super.onResume();
        // bluetooth Low Energy 지원안할 경우

        if (pauseResumeCheck != BleConnecting) {
            if (!BleConnecting) {
                fragmentChange("fragment_ble_beginning");
            }
        }
        //permission_check();

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.d(TAG, "BLUETOOTH_LE no sup");
            //finish();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseResumeCheck = BleConnecting;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
        //unregisterReceiver(mBroadcastReceiver2);
        unregisterReceiver(mBroadcastReceiver3);
        disconnectGattServer("BleMainActivity - onDestroy");
    }

    @Override
    public void onBackPressed() {

        Log.d(TAG, "onBackPressed!");

        if (bleDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            bleDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        } else {

        }

        currentFragment = getSupportFragmentManager().findFragmentById(R.id.bluetoothFragmentSpace);

        if (currentFragment instanceof fragment_Ble_Function) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Notice").setMessage("블루투스 연결을 종료하시겠습니까?\nDo you want to disconnect the Bluetooth connection?");

            builder.setPositiveButton("OK", (dialog, id) -> {
                disconnectGattServer("bleMainActivity - onBackPressed - fragment_Ble_Function");
                fragmentChange("fragment_ble_beginning");
            });

            builder.setNegativeButton("Cancel", (dialog, id) -> {

            });
            AlertDialog alertDialog = builder.create();

            alertDialog.show();
        } else if (currentFragment instanceof fragment_Ble_Password) {
            disconnectGattServer("bleMainActivity - onBackPressed - fragment_Ble_Password");
            fragmentChange("fragment_ble_beginning");
        } else if (currentFragment instanceof fragment_CDS_Setting) {
            disconnectGattServer("bleMainActivity - onBackPressed - fragment_CDS_Setting");
            fragmentChange("fragment_ble_beginning");
        } else if (currentFragment instanceof fragment_SN_Setting) {
            disconnectGattServer("bleMainActivity - onBackPressed - fragment_SN_Setting");
            fragmentChange("fragment_ble_beginning");
        } else if (currentFragment instanceof fragment_Ble_Beginning) {
            finish();
        } else if (currentFragment instanceof fragment_Ble_Scan) {
            fragmentChange("fragment_ble_beginning");
        }
    }


    public abstract static class OnSingleClickListener implements View.OnClickListener {

        //중복 클릭 방지 시간 설정 ( 해당 시간 이후에 다시 클릭 가능 )
        private static final long MIN_CLICK_INTERVAL = 600;
        private long mLastClickTime = 0;

        public abstract void onSingleClick(View v);

        @Override
        public final void onClick(View v) {
            long currentClickTime = SystemClock.uptimeMillis();
            long elapsedTime = currentClickTime - mLastClickTime;
            mLastClickTime = currentClickTime;

            // 중복클릭 아닌 경우
            if (elapsedTime > MIN_CLICK_INTERVAL) {
                onSingleClick(v);
            }
        }
    }

}