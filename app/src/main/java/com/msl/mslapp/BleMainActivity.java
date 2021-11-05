

package com.msl.mslapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
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
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.msl.mslapp.ble.BluetoothUtils;
import com.msl.mslapp.ble.Dialog.Beginning.dialogFragment_Ble_Beginning_LanguageChange;
import com.msl.mslapp.ble.Dialog.setting.dialogFragment_Ble_Setting_FL_Setting;
import com.msl.mslapp.ble.Dialog.setting.dialogFragment_Ble_Setting_Password_Change;
import com.msl.mslapp.ble.Dialog.setting.dialogFragment_ble_Setting_GPS_Change;
import com.msl.mslapp.ble.BleViewModel;
import com.msl.mslapp.ble.fragment.fragment_Ble_Beginning;
import com.msl.mslapp.ble.fragment.fragment_Ble_Scan;
import com.msl.mslapp.ble.fragment.Function.fragment_Ble_Status;
import com.msl.mslapp.ble.fragment.Function.fragment_Ble_Function;
import com.msl.mslapp.ble.fragment.fragment_Ble_Password;
import com.msl.mslapp.ble.fragment.fragment_CDS_Setting;
import com.msl.mslapp.ble.fragment.fragment_SN_Setting;
import com.msl.mslapp.Public.Log.log_ListViewAdapter;
import com.google.android.material.navigation.NavigationView;
import com.msl.mslapp.Public.StringList;
import com.msl.mslapp.databinding.BleActivityMainBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.msl.mslapp.ble.BluetoothUtils.findBLECharacteristics;
import static com.msl.mslapp.ble.Dialog.Status.dialogFragment_Ble_Status_Solar.tv_ble_status_sol_value1;
import static com.msl.mslapp.ble.Dialog.Status.dialogFragment_Ble_Status_Solar.tv_ble_status_sol_value2;
import static com.msl.mslapp.ble.Dialog.Status.dialogFragment_Ble_Status_Solar.tv_ble_status_sol_value3;
import static com.msl.mslapp.ble.Dialog.Status.dialogFragment_Ble_Status_Solar.tv_ble_status_sol_value4;
import static com.msl.mslapp.ble.Dialog.Status.dialogFragment_Ble_Status_Solar.tv_ble_status_sol_value5;
import static com.msl.mslapp.ble.Dialog.Status.dialogFragment_Ble_Status_Solar.tv_ble_status_sol_value6;
import static com.msl.mslapp.ble.fragment.fragment_Ble_Beginning.setLocale;
import static com.msl.mslapp.ble.fragment.fragment_Ble_Scan.selectedSerialNum;
import static com.msl.mslapp.Public.StringList.DATA_TYPE_BTV;
import static com.msl.mslapp.Public.StringList.DATA_TYPE_LISET;
import static com.msl.mslapp.Public.StringList.DATA_TYPE_LISTS;
import static com.msl.mslapp.Public.StringList.DATA_TYPE_S;

@RequiresApi(api = Build.VERSION_CODES.M)
public class BleMainActivity extends AppCompatActivity implements fragment_Ble_Scan.Ble_Scan_Listener, fragment_Ble_Status.Ble_Status_Listener,
        fragment_SN_Setting.SN_Setting_Listener, fragment_CDS_Setting.CDS_Setting_Listener {

    //region 변수 및 상수 정의

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-MainAct";

    // 관리자용 앱 설정
    public static final boolean adminApp = false;
    // delaytime 이용고객용
    public static final boolean delaytimeApp = true;

    public static Context mBleContext = null;

    public static AppCompatActivity mBleMain = null;
    public static String tLanguage;
    public BleActivityMainBinding mBleBinding;
    public static BleViewModel bleViewModel;

    // bluetooth 관련
    public static BluetoothAdapter mBluetoothAdapter = null;
    public BluetoothDevice bleConnectDevice = null;
    public static BluetoothGatt bleGatt = null;
    public static BluetoothManager bluetoothManager = null;
    public static String BluetoothStatus = "";
    public static boolean bleConnected = false;

    boolean pauseResumeCheck = false;

    // password 관련
    public static String readPassword = "";

    // gps 권한
    public static LocationManager locationManager = null;
    // requestCode
    public static final int bluetooth_permission_check = 50;
    public static ActivityResultLauncher<Intent> requestPermissionBle;


    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 51;
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS_RE = 52;
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

    // 중복데이터 읽는거 방지 체크용
    public static String readDataOverlapCheck = "";

    // 읽기 쓰기 케릭터
    public static BluetoothGattCharacteristic respCharacteristic = null;
    public static BluetoothGattCharacteristic cmdCharacteristic = null;
    public static BluetoothGattCharacteristic nameCharacteristic = null;

    // 블루투스 들어온 데이터 값
    public static String readDataTotal = "";
    ConcurrentLinkedQueue<String> readDataQueue = new ConcurrentLinkedQueue<>();

    //사용자 BLE UUID Service/Rx/Tx
    //public static String SERVICE_STRING = "6E400001-B5A3-F393-E0A9-E50E24DCCA9E";

    public static String SERVICE_STRING = "6E400001-B5A3-F393-E0A9-E50E24DCCA9E";
    public static String CHARACTERISTIC_COMMAND_STRING = "6E400002-B5A3-F393-E0A9-E50E24DCCA9E";
    public static String CHARACTERISTIC_RESPONSE_STRING = "6E400003-B5A3-F393-E0A9-E50E24DCCA9E";
    // 등명기 용 uuid
    public static String SERVICE_STRING_4 = "0000fff0-0000-1000-8000-00805f9b34fb";
    public static String CHARACTERISTIC_COMMAND_STRING_4 = "0000fff2-0000-1000-8000-00805f9b34fb";
    public static String CHARACTERISTIC_RESPONSE_STRING_4 = "0000fff1-0000-1000-8000-00805f9b34fb";

    // test - device information - serial number string
    public static String Serial_Number_String = "00002a25-0000-1000-8000-00805f9b34fb";
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
    public static LinearLayout ll_navigation_Language;
    public static LinearLayout ll_navigation_PasswordChange;
    public static LinearLayout ll_navigation_ModeSelect;


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
        // 블루투스 데이터 활성화
        connectToDevice(mBleContext, bleConnectDevice, gattClientCallback);
        //bleGatt = bleConnectDevice.connectGatt(mBleContext, false, gattClientCallback);
    }

    public static void connectToDevice(Context context,
                                       BluetoothDevice device,
                                       BluetoothGattCallback mGattCallBack) {
        if (device != null) {
            // 각 os 별 연결 시 세부 조정이 다양함. 블루투스 연결 방식을 정함.
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Log.d(TAG, "connectToDevice - O up");
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
    }

    // toolbar 스캔 기능 가리기
    @Override
    public void onDetachFragment_Ble_Scan() {
        Log.d(TAG, "onDetachFragment_Ble_Scan");
        ScanItem.setTitle(R.string.ble_main_scanItem_stopScanning);
        ScanItem.setVisible(false);
    }

    // status화면일 경우 텝 보이게하기
    @Override
    public void ble_unconnect_item_visible() {
        Log.d(TAG, "ble_unconnect_item_visible");
        bleDisconnectItem.setVisible(true);
        //tabLayout_ble.setVisibility(View.VISIBLE);
    }

    @Override
    public void ble_unconnect_item_invisible() {
        Log.d(TAG, "ble_unconnect_item_invisible");
        bleDisconnectItem.setVisible(false);
        //tabLayout_ble.setVisibility(View.GONE);
    }

    @Override
    public void ble_scan_item_visible() {
        Log.d(TAG, "ble_scan_item_visible");
        ScanItem.setTitle(R.string.ble_main_scanItem_scan);
        ScanItem.setVisible(true);
        //tabLayout_ble.setVisibility(View.GONE);
    }

    @Override
    public void ble_scan_item_invisible() {
        Log.d(TAG, "ble_scan_item_invisible");
        ScanItem.setVisible(false);
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

                fragmentChange("fragment_ble_scan");

                // 이전에 사용하던 기능
/*
                if (scanningFlag) {
                    fragment_bleScan.stopScan();
                    ScanItem.setTitle(R.string.ble_main_scanItem_scan);
                } else {
                    fragment_bleScan.Scan();
                    ScanItem.setTitle(R.string.ble_main_scanItem_stopScanning);
                }
*/

                return true;
            case R.id.action_bar_scanRefresh:

                fragment_bleScan.reFresh();
                ScanItem.setTitle(R.string.ble_main_scanItem_stopScanning);

                return true;

            case R.id.action_bar_bleDisconnect:
                disconnectGattServer("bleMainActivity - onOptionsItemSelected - action_bar_bleDisconnect");

                bleDisconnectItem.setVisible(false);
                ScanItem.setVisible(true);
                ScanItem.setTitle(R.string.ble_main_scanItem_scan);
                toolbar_title.setText("");
                //fragmentChange("fragment_ble_beginning");

            case android.R.id.home:
                //Toast.makeText(mBleContext, "Test Success", Toast.LENGTH_SHORT).show();
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
        mBleBinding = DataBindingUtil.setContentView(this, R.layout.ble_activity_main);
        bleViewModel = new ViewModelProvider(this).get(BleViewModel.class);

        mBleBinding.setLifecycleOwner(this);

        mBleBinding.setBleViewModel(bleViewModel);

        mBleContext = this;
        mBleMain = this;

        // gps on 시키는데 사용
        locationManager = (LocationManager) mBleContext.getSystemService(LOCATION_SERVICE);

        // 블루투스 권한 및 사용
        bluetoothManager = (BluetoothManager) mBleContext.getSystemService(Context.BLUETOOTH_SERVICE);

        //region Bluetooth 연결 시 필요한 사항
        //mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // 블루투스 권한 체크용
        requestPermissionBle = mBleMain.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // result.data => intent
                Log.d(TAG, "MainActivity로 돌아왔다. ");
            }
        });

        // 언어 설정
        class MainLanguageRunnable implements Runnable {
            public void run() {
                //언어설정
                SharedPreferences sfSideBar = getSharedPreferences("option_data", MODE_PRIVATE);
                Locale systemLocale = mBleContext.getResources().getConfiguration().locale; //시스템 설정 상태를 가져옴
                String languageState = sfSideBar.getString("language_mode", systemLocale.getLanguage()); //시스템 언어를 가져옴
                Log.d(TAG, "System Language : " + languageState);
                tLanguage = languageState; //처음에는 시스템에 설정된 언어로, 이후에는 저장된 언어
                if (tLanguage != null) setLocale(tLanguage); //언어 설정
            }
        }

        MainLanguageRunnable nr = new MainLanguageRunnable();
        Thread t = new Thread(nr);
        t.start();


        // 툴바 설정
        toolbarMain = (Toolbar) findViewById(R.id.ble_toolbar_main);
        toolbarMain.setTitle("");
        toolbarMain.setTitleTextColor(getResources().getColor(R.color.ble_toolbarmain));

        toolbar_title = findViewById(R.id.toolbar_title_ble);

        setSupportActionBar(toolbarMain);

        // toolbar 왼쪽 버튼 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        navigation_Setting();



        // 블루투스 관련 권한 확인 및 필터 정리
        class MainBleFilterPermissionRunnable implements Runnable {
            MainBleFilterPermissionRunnable() {

            }

            public void run() {
                IntentFilter filter1 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                registerReceiver(mBroadcastReceiver1, filter1);

                // 굳이 해당 정보를 알 필요가 없어서
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
                Log.d(TAG, "bluetooth 5 모드 준비 : " + Build.VERSION.SDK_INT + " 이고 O 의 값은 : " + Build.VERSION_CODES.O);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    // setLegacy 값 안 바꾸면 장거리 블루투스 못찾음(광고형 블루투스를 못봄)
            /*settings = new ScanSettings.Builder()
                    .setLegacy(false)
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();*/

                    // 장거리용 셋팅
                    settings = new ScanSettings.Builder().
                            setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).
                            setReportDelay(0).
                            setLegacy(false).build();

                    Log.d(TAG, "bluetooth 5 모드 온");

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
            }
        }

        MainBleFilterPermissionRunnable fpR = new MainBleFilterPermissionRunnable();
        Thread fpT = new Thread(fpR);
        fpT.start();


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

    Handler NO_GATT_SUCCESS_Fail_handler = new Handler(Looper.getMainLooper()) {};

    void navigation_Setting() {
        // 사이드바 관련
        navigationView = findViewById(R.id.rtu_sidebar);
        navigationView.setItemIconTintList(null);
        log_Listview = navigationView.findViewById(R.id.lv_Navigation_Log);
        ll_navigation_move = navigationView.findViewById(R.id.ll_Navigation_Move);
        ll_navigation_log = navigationView.findViewById(R.id.ll_Navigation_Log);
        ll_navigation_Language = navigationView.findViewById(R.id.ll_Navigation_Show_Language);
        ll_navigation_PasswordChange = navigationView.findViewById(R.id.ll_Navigation_Password_Change);
        ll_navigation_ModeSelect = navigationView.findViewById(R.id.ll_Navigation_ModeSelect);
        ll_navigation_GPS = navigationView.findViewById(R.id.ll_navigation_gps);
        ll_navigation_GPS.setVisibility(View.GONE);
        log_Refresh();

        Handler postHandler = new Handler(Looper.getMainLooper());

        Display display = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            display = mBleContext.getDisplay();
        }else{
            display = ((WindowManager) mBleContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        }
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
            if (BleConnecting) {
                BlewriteData(StringList.DATA_REQUEST_INFORMATION);
                postHandler.postDelayed(() -> {
                    FragmentManager fm = getSupportFragmentManager();
                    dialogFragment_ble_Setting_GPS_Change customDialog_GPS_Change = new dialogFragment_ble_Setting_GPS_Change();
                    customDialog_GPS_Change.show(fm, "dialogFragment_ble_Setting_GPS_Change");
                }, 200);
            } else {
                Toast.makeText(mBleContext, "bluetooth not connecting", Toast.LENGTH_SHORT).show();
            }
        });

        // 비밀번호 변경
        navigationView.findViewById(R.id.ll_Navigation_Password_Change).setOnClickListener(v -> {

            if (BleConnecting) {
                postHandler.postDelayed(() -> {
                    FragmentManager fm = getSupportFragmentManager();
                    dialogFragment_Ble_Setting_Password_Change setting_PasswordChange_DialogFragment = new dialogFragment_Ble_Setting_Password_Change();
                    setting_PasswordChange_DialogFragment.show(fm, "fragment_setting_dialog_Password");
                }, 200);
            } else {
                Toast.makeText(mBleContext, "bluetooth not connecting", Toast.LENGTH_SHORT).show();
            }
        });

        // 비밀번호 변경
        navigationView.findViewById(R.id.ll_Navigation_ModeSelect).setOnClickListener(v -> {

            if (BleConnecting) {
                postHandler.postDelayed(() -> {
                    FragmentManager fm = getSupportFragmentManager();
                    dialogFragment_Ble_Setting_Password_Change setting_PasswordChange_DialogFragment = new dialogFragment_Ble_Setting_Password_Change();
                    setting_PasswordChange_DialogFragment.show(fm, "fragment_setting_dialog_Password");
                }, 200);
            } else {
                Toast.makeText(mBleContext, "bluetooth not connecting", Toast.LENGTH_SHORT).show();
            }
        });


        bleDrawerLayout = findViewById(R.id.ble_drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, bleDrawerLayout, toolbarMain, R.string.app_name, R.string.app_name);
        drawerToggle.syncState(); // 메뉴 버튼 추가
        bleDrawerLayout.addDrawerListener(drawerToggle); // 삼선 메뉴 사용 시 뱅그르르 돈다.
    }


    public static void navigation_GPS_Visible(boolean visble) {
        if (visble) {
            ll_navigation_GPS.setVisibility(View.VISIBLE);
            ll_navigation_PasswordChange.setVisibility(View.VISIBLE);
            //ll_navigation_ModeSelect.setVisibility(View.VISIBLE);
            ll_navigation_Language.setVisibility(View.GONE);
        } else {
            ll_navigation_GPS.setVisibility(View.GONE);
            ll_navigation_PasswordChange.setVisibility(View.GONE);
            //ll_navigation_ModeSelect.setVisibility(View.GONE);
            ll_navigation_Language.setVisibility(View.VISIBLE);
        }
    }

    // 일반적 시스템 데이터일 경우
    public static void logData_Ble(String data) {
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("hh:mm:ss");
        String getTime = simpleDate.format(mDate);

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
        }

        String[] dataArr = data.split(StringList.DATA_SIGN_COMMA);

        // 각 특정 별 데이터 별 확인해서 로그 값을 다르게 나타냄(해당 데이터 값은 안보여줌)
        if (color.equals("read")) {
            if (dataArr[0].contains(StringList.DATA_TYPE_PS)) {
                log_listViewAdapter.addItem(getTime, "Request a Password", color);
                return;
            } else if (dataArr[0].contains(StringList.DATA_TYPE_LISTS)) {
                switch (dataArr[1]) {
                    case "S":
                        switch (dataArr[2]) {
                            case StringList.DATA_TYPE_BTV:
                                log_listViewAdapter.addItem(getTime, "Battery Status Request Confirm\n" + data, color);
                                break;
                            case StringList.DATA_TYPE_SLV:
                                log_listViewAdapter.addItem(getTime, "Solar Status V Request Confirm\n" + data, color);
                                break;
                            case StringList.DATA_TYPE_SLC:
                                log_listViewAdapter.addItem(getTime, "Solar Status A Request Confirm\n" + data, color);
                                break;
                        }
                        break;
                    default:
                        log_listViewAdapter.addItem(getTime, "Status Request Confirm : \n" + data, color);
                        break;
                }
                return;
            } else if (dataArr[0].contains(StringList.DATA_TYPE_LICMD)) {
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
                            case StringList.DATA_TYPE_SID:
                                log_listViewAdapter.addItem(getTime, "Lantern ID : " + dataArr[3] + " Confirm", color);
                                break;
                            case StringList.DATA_TYPE_GP0:
                                log_listViewAdapter.addItem(getTime, "GPS Only Night Confirm", color);
                                break;
                            case StringList.DATA_TYPE_GP1:
                                log_listViewAdapter.addItem(getTime, "GPS Always Confirm", color);
                                break;
                            default:
                                log_listViewAdapter.addItem(getTime, "FL : " + dataArr[2] + " Confirm", color);
                        }
                        break;
                }
                return;
            } else if (dataArr[0].contains(StringList.DATA_TYPE_LISET)) {
                log_listViewAdapter.addItem(getTime, "Information Data Confirm\n" + data, color);
                //log_Listview.requestLayout();
                //log_listViewAdapter.notifyDataSetChanged();
                return;
            } else if (dataArr[0].contains(StringList.DATA_TYPE_RTU_READ)) {
                data = data.replace(StringList.DATA_TYPE_RTU_READ + " ", "");
                log_listViewAdapter.addItem(getTime, "RTU Data Confirm\n" + data, color);
                return;
            } else if (dataArr[0].contains(StringList.DATA_TYPE_RTU_MODEM_READ)) {
                Log.d(TAG, "DATA_TYPE_RTU_MODEM_READ *  : " + data);
                data = data.replace(StringList.DATA_TYPE_RTU_MODEM_READ, ""); //  + " $$MODEM_STATE: "
                Log.d(TAG, "DATA_TYPE_RTU_MODEM_READ Revise  *  : " + data);
                log_listViewAdapter.addItem(getTime, "RTU Modem Data Confirm\n" + data, color);
                return;
            }

        } else if (color.equals("write")) {
            if (dataArr[0].contains(StringList.DATA_TYPE_PS)) {
                // 비밀번호 요청 더이상 보내지 말라는 대답으로 보내는 거(로그에 안보여줄려고 처리)
                if (dataArr[1].contains(StringList.DATA_TYPE_A)) {
                    if (dataArr[2].contains("0000H")) {
                        return;
                    }
                }
                log_listViewAdapter.addItem(getTime, "Enter a Password", color);
                //log_Listview.requestLayout();
                //log_listViewAdapter.notifyDataSetChanged();
                return;
            } else if (dataArr[0].contains(StringList.DATA_TYPE_LICMD)) {
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
                            case StringList.DATA_TYPE_BTV:
                                log_listViewAdapter.addItem(getTime, "Battery Status Request", color);
                                break;
                            case StringList.DATA_TYPE_SLV:
                                log_listViewAdapter.addItem(getTime, "Solar Status V Request", color);
                                break;
                            case StringList.DATA_TYPE_SLC:
                                log_listViewAdapter.addItem(getTime, "Solar Status A Request", color);
                                break;
                            case StringList.DATA_TYPE_SID:
                                log_listViewAdapter.addItem(getTime, "Lantern ID : " + dataArr[3], color);
                                break;
                            case StringList.DATA_TYPE_GP0:
                                log_listViewAdapter.addItem(getTime, "GPS Only Night", color);
                                break;
                            case StringList.DATA_TYPE_GP1:
                                log_listViewAdapter.addItem(getTime, "GPS Always", color);
                                break;
                            case StringList.DATA_TYPE_DEL:
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
            } else if (dataArr[0].contains(StringList.DATA_TYPE_MUCMD)) {
                switch (dataArr[1]) {
                    case "8":
                        log_listViewAdapter.addItem(getTime, "RTU Data Request", color);
                        break;
                    case "14":
                        log_listViewAdapter.addItem(getTime, "RTU Data Set", color);
                        break;
                }
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

    private void permission_check() {
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

    // onActivityResult 가 deprecated 가 되어 변경 함 (21-10-20)
/*
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
    }*/

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
            requestPermissionBle.launch(intent);
            //mBleMain.startActivityForResult(intent, bluetooth_permission_check);
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

    // 연결 실패 시 n회 연결 재시도용
    public static int connectFail = 0;


    // 블루투스 각종 기능.
    private BluetoothGattCallback gattClientCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.d(TAG, "onConnectionStateChange status : " + status);
            if (status == BluetoothGatt.GATT_FAILURE) {
                disconnectGattServer("BleMainActivity - gattClientCallback - GATT_FAILURE");
                fragmentChange("fragment_ble_beginning");
                return;
            } else if (status != BluetoothGatt.GATT_SUCCESS) {
                // 성공하지 않았을 경우 다시 연결 시도. 5회 이후에도 실패 시 실패로 간주(연결 시 1 회 정도 실패가 뜰때가 많아서 다시 시도 기능 추가)
                // 8 : 최초 등명기가 꺼져서 연결의 이상이 생긴경우유선 무선
                // 133 : 8 상태로 계속해서 이상이 있는 경우
                if (status == 8) {
                    logData_Ble("Bluetooth Disconnect - \nproblem between Bluetooth communication(distance or shutdown) - " +
                            "back to the initial screen", "error");
                    connectFail = 0;
                    disconnectGattServer("BleMainActivity - gattClientCallback - NO_GATT_SUCCESS");
                    fragmentChange("fragment_ble_beginning");
                    Log.d(TAG, "onConnectionStateChange state 8 : 블루투스 연결 불안정(거리 혹은 장치종료)");
                    NO_GATT_SUCCESS_Fail_handler.postDelayed(() -> {
                        Toast.makeText(mBleContext, getString(R.string.bleDisconnect_Connection_Failed), Toast.LENGTH_LONG).show();
                    }, 100);
                    /*if (connectFail < 5) {
                        logData_Ble("Bluetooth Disconnect - \n연결 불안정 - 재접속 시도", "error");
                        connectFail += 1;
                        connectToDevice(mBleContext, bleConnectDevice, gattClientCallback);
                        Log.d(TAG, "NO_GATT_SUCCESS " + connectFail + "회 실패 : " + status);
                    } else {
                        logData_Ble("Bluetooth Disconnect - \n연결 불안정 - 초기화면으로", "error");
                        connectFail = 0;
                        disconnectGattServer("BleMainActivity - gattClientCallback - NO_GATT_SUCCESS");
                        fragmentChange("fragment_ble_beginning");
                        Log.d(TAG, "NO_GATT_SUCCESS 5회 실패 : " + status);
                    }*/
                } else if (status == 133) {
                    if (connectFail < 5) {
                        logData_Ble("Bluetooth Disconnect - \nConnection Failed - Attempt to reconnect", "error");
                        connectFail += 1;
                        connectToDevice(mBleContext, bleConnectDevice, gattClientCallback);
                        Log.d(TAG, "NO_GATT_SUCCESS " + connectFail + "회 실패 : " + status);
                    } else {
                        logData_Ble("Bluetooth Disconnect - \n" +
                                "Connection failed 5 times - back to the initial screen", "error");
                        connectFail = 0;
                        disconnectGattServer("BleMainActivity - gattClientCallback - NO_GATT_SUCCESS");
                        fragmentChange("fragment_ble_beginning");
                        Log.d(TAG, "NO_GATT_SUCCESS 5회 실패 : " + status);

                        NO_GATT_SUCCESS_Fail_handler.postDelayed(() -> {
                            Toast.makeText(mBleContext, getString(R.string.bleDisconnect_Connection_Failed), Toast.LENGTH_LONG).show();
                        }, 100);

                    }
                } else if (status == 22) {
                    logData_Ble("Bluetooth Disconnect - \n" +
                            "Terminate the connection", "error");
                } else {
                    logData_Ble("Bluetooth Disconnect - " + status, "error");
                }
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

            bleConnected = true;

            // uuid, Characteristic 모르면
            BluetoothDevice device = gatt.getDevice();
            String address = device.getAddress();


            List<BluetoothGattService> services = gatt.getServices();

            if (services != null) {
                for (BluetoothGattService service : services) {
                    List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                    if (characteristics != null) {
                        for (BluetoothGattCharacteristic characteristic : characteristics) {


                            try {
                                Log.d(TAG, "BluetoothGattCharacteristic characteristic : " + characteristic.getUuid().toString() + " property : " + characteristic.getProperties());
                                if (characteristic.getUuid().toString().equals(Serial_Number_String)) {
                                    nameCharacteristic = characteristic;
                                    Log.d(TAG, " 성공!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + nameCharacteristic.getStringValue(0));

                                }
                            } catch (Exception e) {
                                Log.d(TAG, "실패!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + e.toString());
                            }

                            // bluetooth 5 및 일반적 블루투스
                            if (characteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_WRITE ||
                                    characteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) {
                                cmdCharacteristic = characteristic;
                                logData_Ble("PROPERTY_WRITE connect");
                                if (cmdCharacteristic == null) {
                                    Log.d(TAG, "onServicesDiscovered - cmdCharacteristic : null");
                                    // 해당 값을 하면 되긴하는데...처음부터 해서 에러 메세지(BleWrite부분에서) 하지않게하기
                                    cmdCharacteristic = BluetoothUtils.findCommandCharacteristic(bleGatt);
                                    logData_Ble("PROPERTY_WRITE Gatt write connect");
                                }

                            } else if (characteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_NOTIFY) {
                                Log.d(TAG, "onServicesDiscovered - PROPERTY_NOTIFY");
                                logData_Ble("PROPERTY_NOTIFY read connect");
                                respCharacteristic = characteristic;
                            }

                            // 등명기 블루투스(서비스 이름(PROPERTY_NOTIFY)를 못찾음, 그래서 uuid로 연결)
                            if (characteristic.getUuid().toString().equals(CHARACTERISTIC_COMMAND_STRING_4)) {
                                if(cmdCharacteristic == null){
                                    cmdCharacteristic = characteristic;
                                    logData_Ble("Bluetooth 4 write connect");
                                }
                            } else if (characteristic.getUuid().toString().equals(CHARACTERISTIC_RESPONSE_STRING_4)) {
                                respCharacteristic = characteristic;
                                logData_Ble("Bluetooth 4 read connect");
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
            //Log.d(TAG, "onCharacteristicChanged written successfully : " + new String(characteristic.getValue()));
            readCharacteristic(characteristic);
        }

        // 블루투스 데이터 보낼 시
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //Log.d(TAG, "Characteristic written successfully : " + characteristic.getStringValue(0));
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
            }
        }

        int requestPasswordCount = 0;

        // 블루투스 데이터 읽기 시 처리
        private void readCharacteristic(BluetoothGattCharacteristic characteristic) {

            String readCharacter = characteristic.getStringValue(0);

            if (!readDataOverlapCheck.equals(readCharacter)) {
                readDataOverlapCheck = readCharacter;
            } else {
                if (readCharacter.contains(StringList.DATA_TYPE_PS)) {
                    requestPasswordCount += 1;
                    Log.d(TAG, "Request a Password Count : " + requestPasswordCount);
                    // 데이터 통신 간 오류로 인해 비밀번호가 입력이 안되거나, 연결이 끊겼다 연결되는경우 비밀번호 요청을 예속 보냄. 그에 대비하여 보냄.
                    if (requestPasswordCount >= 5) {
                        // 해당 값을 등명기가 수신 시 더이상 데이터 요청 안함.
                        BlewriteData("$PS,A,0000H*");
                        requestPasswordCount = 0;
                        readDataOverlapCheck = "";
                    }
                }

                return;
            }

            /*
            // 설명용으로 만든 곳. readCharacteristic 를 여기서 사용하면 안됨....
            // 해당 명령어 시 properties 가  read 케릭터일 경우 리드 실행 이후 onCharacteristicRead 로 데이터가 들어옴.
            bleGatt.readCharacteristic(nameCharacteristic);

            //해당하는 케릭터일 경우 해당 데이터 따로 사용가능.
            if(characteristic.equals(nameCharacteristic)){

                Log.d(TAG, "readCharacteristic - nameCharacteristic  : " + readCharacter);
            }*/

            // 블루투스가 한번에 2개 이상을 동시에 보내는걸 대비하여 먼저 큐에 넣고 나중에 정리/
            readDataQueue.offer(readCharacter);

            // 들어온 데이터를 토탈에 합침. 이후 확인
            for (int i = 0; i < readDataQueue.size(); i++) {
                readDataTotal += readDataQueue.poll();
            }
            Log.d(TAG, "readCharacteristic String : " + new String(characteristic.getValue()) + " , readDataTotal : " + readDataTotal);
            ReadDataThread readDataThread = new ReadDataThread();
            readDataThread.start();
        }
    };


    class ReadDataThread extends Thread {
        @Override
        public void run() {
            handler.sendEmptyMessage(readDataSuccess);
        }
    }

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
        bleConnected = false;
        selectedSerialNum = "";
        connectFail = 0;
        readDataOverlapCheck = "";
        if (bleGatt != null) {
            BleConnecting = false;

            bleGatt.disconnect();
            bleGatt.close();
        }
    }

    // 연결 및 데이터 들어온거에 따른 결과
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                // 연결 되었을 경우

            } else if (msg.what == 1) {
                // 데이터가 들어왔을 때

                try {
                    if (readDataTotal.trim().equals("")) {
                        Log.d(TAG, "readDataTotal is nothing" + readDataTotal);
                        return;
                    }
                    // RTU 관련 데이터 들어올 경우($ 및 * 이 안들어감)
                    if (readDataTotal.contains("[ ConfMsg]") && readDataTotal.contains(">")) {

                        int configIndex = 0;
                        int lfIndex = 0;

                        configIndex = readDataTotal.indexOf("[ ConfMsg]");
                        lfIndex = readDataTotal.indexOf(">", configIndex);
                        while (configIndex < lfIndex) {

                            // \n 까지 포함
                            String readData = readDataTotal.substring(configIndex, lfIndex + 1);

                            Log.d(TAG, "RTU 다음줄 내용 포함해서 : " + readData);

                            // Log 용은 삭제
                            String readDataLog = readDataTotal.substring(configIndex, lfIndex);

                            Log.d(TAG, "RTU 로그용 : " + readDataLog);

                            try {
                                readDataTotal = readDataTotal.substring(lfIndex + 1);
                            } catch (Exception e) {
                                logData_Ble("readData Error! - TotalReadData : " + readDataTotal, "error");
                                readDataTotal = "";
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
                            lfIndex = readDataTotal.indexOf(">", configIndex);
                            if (configIndex < 0 | lfIndex < 0) {
                                Log.d(TAG, "[ModemMsg] break! : " + readDataTotal);
                                break;
                            }

                        }
                    }

                    if (readDataTotal.contains("[ModemMsg]") && readDataTotal.contains(">")) {
                        Log.d(TAG, "[ModemMsg] data Come : " + readDataTotal);

                        int ModemMsgIndex = 0;
                        int lfIndex = 0;

                        ModemMsgIndex = readDataTotal.indexOf("[ModemMsg]");
                        lfIndex = readDataTotal.indexOf(">", ModemMsgIndex);
                        while (ModemMsgIndex < lfIndex) {

                            // \n 까지 포함
                            String readData = readDataTotal.substring(ModemMsgIndex, lfIndex + 1);

                            Log.d(TAG, "다음줄 내용 포함해서 : " + readData);

                            // Log 용은 삭제
                            String readDataLog = readDataTotal.substring(ModemMsgIndex, lfIndex);

                            Log.d(TAG, "로그용 : " + readDataLog);

                            try {
                                readDataTotal = readDataTotal.substring(lfIndex + 1);
                            } catch (Exception e) {
                                logData_Ble("readData Error! - TotalReadData : " + readDataTotal, "error");
                                readDataTotal = "";
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
                            lfIndex = readDataTotal.indexOf(">", ModemMsgIndex);
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

                    boolean whileExit = true;

                    while (whileExit) {

                        readDataTotal = readDataTotal.trim();

                        // $ 및 * 등이 포함됐는지.(처음과 끝)
                        if (readDataTotal.indexOf(StringList.DATA_SIGN_START) > -1
                                && readDataTotal.indexOf(StringList.DATA_SIGN_CHECKSUM) > -1) {

                            int count = 0;
                            for (int i = 0; i < readDataTotal.length(); i++) {
                                if (readDataTotal.charAt(i) == '$')
                                    count++;
                            }

                            // 만약 $ 들어간게 2개이상이면
                            if (count > 1) {
                                // 2번쨰 $ 전까지 데이터 나누고
                                String organizeData = readDataTotal.substring(0, readDataTotal.indexOf("$", readDataTotal.indexOf("$") + 1));

                                // * 있는지확인
                                if (organizeData.contains("*")) {
                                    try {
                                        // * 이 있으면서 체크섬 값도 가지고 있다면
                                        if (readDataTotal.indexOf("*") + 3 == readDataTotal.indexOf("$", readDataTotal.indexOf("$") + 1)) {

                                        } else {
                                            readDataTotal = readDataTotal.substring(readDataTotal.indexOf("$", readDataTotal.indexOf("$") + 1));
                                        }
                                    } catch (Exception e) {
                                        // * 뒤에 체크섬 값올 자리에 $ 및 비어있어서 인덱스 값을 오버한거라면 해당 데이터를 잘라라
                                        Log.d(TAG, "organizeData * check error : " + e.toString());
                                        readDataTotal = readDataTotal.substring(readDataTotal.indexOf("$", readDataTotal.indexOf("$") + 1));
                                    }
                                } else {
                                    // * 이 없다면 잘못 들어온 데이터이므로 해당 데이터 자르기
                                    readDataTotal = readDataTotal.substring(readDataTotal.indexOf("$", readDataTotal.indexOf("$") + 1));
                                }
                            }


                            // $이 *보다 먼저 들어왔는지(데이터 순서 꼬였는지)
                            if (readDataTotal.indexOf(StringList.DATA_SIGN_START) < readDataTotal.indexOf(StringList.DATA_SIGN_CHECKSUM)) {
                                try {
                                    if (readDataTotal.substring(readDataTotal.indexOf(StringList.DATA_SIGN_CHECKSUM) + 1, readDataTotal.indexOf(StringList.DATA_SIGN_CHECKSUM) + 3).length() == 2) {
                                        //데이터 처음과 끝 받기 완료

                                        String data = readDataTotal.substring(readDataTotal.indexOf(StringList.DATA_SIGN_START), readDataTotal.indexOf(StringList.DATA_SIGN_CHECKSUM) + 3);
                                        Log.d(TAG, "정리된 Data : " + data);
                                        String[] dataArr = data.split(StringList.DATA_SIGN_COMMA);
                                        // 가장 마지막 데이터(~~~~*AH)의 체크섬이후 데이터 받아오기(체크섬 값 부분)
                                        String csData = dataArr[dataArr.length - 1].substring(dataArr[dataArr.length - 1].indexOf(StringList.DATA_SIGN_CHECKSUM) + 1);

                                        // 체크섬이 맞는지
                                        if (csData.equals(ToCheckSum(data.substring(data.indexOf(StringList.DATA_SIGN_START), data.indexOf(StringList.DATA_SIGN_CHECKSUM) + 1)))) {
                                            Log.d(TAG, "CheckSum OK!");
                                            currentFragment = getSupportFragmentManager().findFragmentById(R.id.bluetoothFragmentSpace);

                                            logData_Ble(data, "read");


                                            if (data.substring(1, 6).contains(DATA_TYPE_LISTS)) {
                                                if (data.startsWith(DATA_TYPE_S, 7)) {
                                                    if (data.substring(9, 12).contains(DATA_TYPE_BTV)) {
                                                        Log.d(TAG, "readData BTV 들어옴");
                                                        // 다이얼로그한테 데이터를 보내야함.
                                                        String[] data_arr = data.split(",");
                                                        String tv_bat_value6_value = "";
                                                        if (data_arr[9].contains("*")) {
                                                            tv_bat_value6_value = data_arr[9].substring(0, data_arr[9].indexOf("*"));
                                                        }
                                                        try {

                                                            bleViewModel.setBleBattery_1(data_arr[4] + "V");
                                                            bleViewModel.setBleBattery_2(data_arr[5] + "V");
                                                            bleViewModel.setBleBattery_3(data_arr[6] + "V");
                                                            bleViewModel.setBleBattery_4(data_arr[7] + "V");
                                                            bleViewModel.setBleBattery_5(data_arr[8] + "V");
                                                            bleViewModel.setBleBattery_6(tv_bat_value6_value + "V");

                                                            /*tv_ble_status_bat_value1.setText(data_arr[4] + "V");
                                                            tv_ble_status_bat_value2.setText(data_arr[5] + "V");
                                                            tv_ble_status_bat_value3.setText(data_arr[6] + "V");
                                                            tv_ble_status_bat_value4.setText(data_arr[7] + "V");
                                                            tv_ble_status_bat_value5.setText(data_arr[8] + "V");
                                                            tv_ble_status_bat_value6.setText(tv_bat_value6_value + "V");*/
                                                        } catch (Exception e) {
                                                            Log.d(TAG, "tv_ble_status_bat_value 설정 간 문제 발생 : " + e);
                                                        }

                                                        try {

                                                            bleViewModel.setBleBattery_iv_1(data_arr[4]);
                                                            bleViewModel.setBleBattery_iv_2(data_arr[5]);
                                                            bleViewModel.setBleBattery_iv_3(data_arr[6]);
                                                            bleViewModel.setBleBattery_iv_4(data_arr[7]);
                                                            bleViewModel.setBleBattery_iv_5(data_arr[8]);
                                                            bleViewModel.setBleBattery_iv_6(tv_bat_value6_value);

                                                            /*// 배터리 값에 따른 이미지 변경
                                                            for (int i = 0; i <= 5; i++) {
                                                                ImageView imageView;
                                                                switch (i) {
                                                                    case 0:
                                                                        imageView = iv_ble_status_bat1;
                                                                        break;
                                                                    case 1:
                                                                        imageView = iv_ble_status_bat2;
                                                                        break;
                                                                    case 2:
                                                                        imageView = iv_ble_status_bat3;
                                                                        break;
                                                                    case 3:
                                                                        imageView = iv_ble_status_bat4;
                                                                        break;
                                                                    case 4:
                                                                        imageView = iv_ble_status_bat5;
                                                                        break;
                                                                    default:
                                                                        imageView = iv_ble_status_bat6;
                                                                        break;
                                                                }

                                                                double bat_value;

                                                                if (i != 5) {
                                                                    bat_value = Double.parseDouble(data_arr[i + 4]);
                                                                } else {
                                                                    bat_value = Double.parseDouble(tv_bat_value6_value);
                                                                }

                                                                if (bat_value >= 4) {
                                                                    //viewModel.setImageViewMutableLiveData(R.drawable.green_battery);
                                                                    imageView.setBackgroundResource(R.drawable.green_battery);
                                                                } else if (bat_value >= 3.8) {
                                                                    imageView.setBackgroundResource(R.drawable.yellow_battery);
                                                                } else if (bat_value >= 3.6) {
                                                                    imageView.setBackgroundResource(R.drawable.brown_battery);
                                                                } else {
                                                                    imageView.setBackgroundResource(R.drawable.red_battery);
                                                                }


                                                            }
*/

                                                        } catch (Exception e) {
                                                            Log.d(TAG, "iv_ble_status_bat 설정 간 문제 발생 : " + e);
                                                        }

                                                    } else if (data.substring(9, 12).contains("SLV")) {
                                                        Log.d(TAG, "readData SLV 들어옴");
                                                        // 다이얼로그한테 데이터를 보내야함.
                                                        String[] data_arr = data.split(",");
                                                        String tv_sol_value6_value = "";
                                                        if (data_arr[9].contains("*")) {
                                                            tv_sol_value6_value = data_arr[9].substring(0, data_arr[9].indexOf("*"));
                                                        }
                                                        try {
                                                            tv_ble_status_sol_value1.setText(data_arr[4] + "V");
                                                            tv_ble_status_sol_value2.setText(data_arr[5] + "V");
                                                            tv_ble_status_sol_value3.setText(data_arr[6] + "V");
                                                            tv_ble_status_sol_value4.setText(data_arr[7] + "V");
                                                            tv_ble_status_sol_value5.setText(data_arr[8] + "V");
                                                            tv_ble_status_sol_value6.setText(tv_sol_value6_value + "V");
                                                        } catch (Exception e) {
                                                            Log.d(TAG, "tv_ble_status_bat_value 설정 간 문제 발생 : " + e);
                                                        }

                                                        try {


                                                            bleViewModel.setBleSolarV_iv_1(data_arr[4]);
                                                            bleViewModel.setBleSolarV_iv_2(data_arr[5]);
                                                            bleViewModel.setBleSolarV_iv_3(data_arr[6]);
                                                            bleViewModel.setBleSolarV_iv_4(data_arr[7]);
                                                            bleViewModel.setBleSolarV_iv_5(data_arr[8]);
                                                            bleViewModel.setBleSolarV_iv_6(tv_sol_value6_value);

                                                            /*// 배터리 값에 따른 이미지 변경
                                                            for (int i = 0; i <= 5; i++) {
                                                                ImageView imageView;
                                                                switch (i) {
                                                                    case 0:
                                                                        imageView = iv_ble_status_sol1;
                                                                        break;
                                                                    case 1:
                                                                        imageView = iv_ble_status_sol2;
                                                                        break;
                                                                    case 2:
                                                                        imageView = iv_ble_status_sol3;
                                                                        break;
                                                                    case 3:
                                                                        imageView = iv_ble_status_sol4;
                                                                        break;
                                                                    case 4:
                                                                        imageView = iv_ble_status_sol5;
                                                                        break;
                                                                    default:
                                                                        imageView = iv_ble_status_sol6;
                                                                        break;
                                                                }

                                                                double sol_value;

                                                                if (i != 5) {
                                                                    sol_value = Double.parseDouble(data_arr[i + 4]);
                                                                } else {
                                                                    sol_value = Double.parseDouble(tv_sol_value6_value);
                                                                }

                                                                if (sol_value >= 5) {
                                                                    imageView.setBackgroundResource(R.drawable.green_battery);
                                                                } else if (sol_value >= 4) {
                                                                    imageView.setBackgroundResource(R.drawable.yellow_battery);
                                                                } else if (sol_value >= 3) {
                                                                    imageView.setBackgroundResource(R.drawable.brown_battery);
                                                                } else {
                                                                    imageView.setBackgroundResource(R.drawable.red_battery);
                                                                }


                                                            }*/

                                                        } catch (Exception e) {
                                                            Log.d(TAG, "iv_ble_status_bat 설정 간 문제 발생 : " + e);
                                                        }

                                                    } else if (data.substring(9, 12).contains("SLC")) {
                                                        Log.d(TAG, "readData SLC 들어옴");
                                                        // 다이얼로그한테 데이터를 보내야함.
                                                        String[] data_arr = data.split(",");
                                                        String tv_sol_value6_value = "";
                                                        if (data_arr[9].contains("*")) {
                                                            tv_sol_value6_value = data_arr[9].substring(0, data_arr[9].indexOf("*"));
                                                        }
                                                        try {
                                                            bleViewModel.setBleSolarA_1(data_arr[4] + "A");
                                                            bleViewModel.setBleSolarA_2(data_arr[5] + "A");
                                                            bleViewModel.setBleSolarA_3(data_arr[6] + "A");
                                                            bleViewModel.setBleSolarA_4(data_arr[7] + "A");
                                                            bleViewModel.setBleSolarA_5(data_arr[8] + "A");
                                                            bleViewModel.setBleSolarA_6(tv_sol_value6_value + "A");

                                                            /*tv_ble_status_sol_value1_a.setText(data_arr[4] + "A");
                                                            tv_ble_status_sol_value2_a.setText(data_arr[5] + "A");
                                                            tv_ble_status_sol_value3_a.setText(data_arr[6] + "A");
                                                            tv_ble_status_sol_value4_a.setText(data_arr[7] + "A");
                                                            tv_ble_status_sol_value5_a.setText(data_arr[8] + "A");
                                                            tv_ble_status_sol_value6_a.setText(tv_sol_value6_value + "A");*/
                                                        } catch (Exception e) {
                                                            Log.d(TAG, "tv_ble_status_bat_value 설정 간 문제 발생 : " + e);
                                                        }
                                                    }

                                                } else {
                                                    Log.d(TAG, "readData 데이터 읽기");
                                                    String[] data_arr = data.split(",");
                                                    Log.d(TAG, "data_arr length" + data_arr.length);


                                                    bleViewModel.setBleID(data_arr[1]);
                                                    bleViewModel.setBleInputV(data_arr[2]);
                                                    bleViewModel.setBleOutputA(data_arr[3]);
                                                    if (data_arr[4].equals("0")) {
                                                        bleViewModel.setBleCDS(getString(R.string.ble_status_cds_0));
                                                    } else {
                                                        bleViewModel.setBleCDS(getString(R.string.ble_status_cds_1));
                                                    }
                                                    if (data_arr[5].equals("0")) {
                                                        bleViewModel.setBleLanternStatus(getString(R.string.ble_status_lantern_status_0));
                                                    } else {
                                                        bleViewModel.setBleLanternStatus(getString(R.string.ble_status_lantern_status_1));
                                                    }
                                                    bleViewModel.setBleFL(data_arr[6]);
                                                    bleViewModel.setBleSolarV(data_arr[7] + "V");
                                                    bleViewModel.setBleBatteryV(data_arr[8] + "V");
                                                    bleViewModel.setBleBatteryV_iv(data_arr[11]);
                                                    bleViewModel.setBleOutputV(data_arr[9] + "V");
                                                    bleViewModel.setBleChargingA(data_arr[10] + "V");
                                                    bleViewModel.setBleBatteryPer(data_arr[11] + "%");
                                                    int battery_percent = Integer.parseInt(data_arr[11]);
                                                    /*if (battery_percent >= 75) {
                                                        iv_ble_status_battery_percent.setImageResource(R.drawable.battery_100);
                                                    } else if (battery_percent >= 50) {
                                                        iv_ble_status_battery_percent.setImageResource(R.drawable.battery_75);
                                                    } else if (battery_percent >= 25) {
                                                        iv_ble_status_battery_percent.setImageResource(R.drawable.battery_50);
                                                    } else {
                                                        iv_ble_status_battery_percent.setImageResource(R.drawable.battery_25);
                                                    }*/


                                                    /*tv_ble_status_id.setText(data_arr[1]);
                                                    tv_ble_status_input_v.setText(data_arr[2] + "V");
                                                    tv_ble_status_output_a.setText(data_arr[3] + "A");
                                                    if (data_arr[4].equals("0")) {
                                                        tv_ble_status_cds.setText(getString(R.string.ble_status_cds_0));
                                                    } else {
                                                        tv_ble_status_cds.setText(getString(R.string.ble_status_cds_1));
                                                    }
                                                    if (data_arr[5].equals("0")) {
                                                        tv_ble_status_lantern_status.setText(getString(R.string.ble_status_lantern_status_0));
                                                    } else {
                                                        tv_ble_status_lantern_status.setText(getString(R.string.ble_status_lantern_status_1));
                                                    }
                                                    tv_ble_status_fl.setText(data_arr[6]);
                                                    tv_ble_status_solar_v.setText(data_arr[7] + "V");
                                                    tv_ble_status_battery_v.setText(data_arr[8] + "V");
                                                    tv_ble_status_output_v.setText(data_arr[9] + "V");
                                                    tv_ble_status_charge_dischar_a.setText(data_arr[10] + "A");
                                                    tv_ble_status_battery_percent.setText(data_arr[11] + "%");
                                                    int battery_percent = Integer.parseInt(data_arr[11]);
                                                    if (battery_percent >= 75) {
                                                        iv_ble_status_battery_percent.setImageResource(R.drawable.battery_100);
                                                    } else if (battery_percent >= 50) {
                                                        iv_ble_status_battery_percent.setImageResource(R.drawable.battery_75);
                                                    } else if (battery_percent >= 25) {
                                                        iv_ble_status_battery_percent.setImageResource(R.drawable.battery_50);
                                                    } else {
                                                        iv_ble_status_battery_percent.setImageResource(R.drawable.battery_25);
                                                    }*/

                                                    String receiveTime_hour;
                                                    String receiveTime_min = data_arr[13].substring(2, 4);
                                                    String receiveTime_sec = data_arr[13].substring(4);

                                                    String[] receiveData = data_arr[12].split("\\.");
                                                    String receiveData_year = receiveData[0];
                                                    String receiveData_mon = receiveData[1].substring(0, 2);
                                                    String receiveData_day = receiveData[1].substring(2);

                                                    // gmt 으로 인한 9시간 추가해야함. 15시 이후면 다음날로 측정하여 계산
                                                    if (Integer.parseInt(data_arr[13].substring(0, 2)) >= 15) {
                                                        // 15시간을 뺀 시간
                                                        receiveTime_hour = (Integer.parseInt(data_arr[13].substring(0, 2)) - 15) + "";
                                                        // 1일을 더 넣어서 계산
                                                        int day = Integer.parseInt(receiveData[1].substring(2));
                                                        switch (receiveData[1].substring(0, 2)) {
                                                            case "01":
                                                                if (day != 31) {
                                                                    receiveData_day = (day + 1) + "";
                                                                } else {
                                                                    receiveData_mon = "02";
                                                                    receiveData_day = "01";
                                                                }
                                                                break;
                                                            case "02":
                                                                if (day != 28) {
                                                                    receiveData_day = (day + 1) + "";
                                                                } else {
                                                                    receiveData_mon = "03";
                                                                    receiveData_day = "01";
                                                                }
                                                                break;
                                                            case "03":
                                                                if (day != 31) {
                                                                    receiveData_day = (day + 1) + "";
                                                                } else {
                                                                    receiveData_mon = "04";
                                                                    receiveData_day = "01";
                                                                }
                                                                break;
                                                            case "04":
                                                                if (day != 30) {
                                                                    receiveData_day = (day + 1) + "";
                                                                } else {
                                                                    receiveData_mon = "05";
                                                                    receiveData_day = "01";
                                                                }
                                                                break;
                                                            case "05":
                                                                if (day != 31) {
                                                                    receiveData_day = (day + 1) + "";
                                                                } else {
                                                                    receiveData_mon = "06";
                                                                    receiveData_day = "01";
                                                                }
                                                                break;
                                                            case "06":
                                                                if (day != 30) {
                                                                    receiveData_day = (day + 1) + "";
                                                                } else {
                                                                    receiveData_mon = "07";
                                                                    receiveData_day = "01";
                                                                }
                                                                break;
                                                            case "07":
                                                                if (day != 31) {
                                                                    receiveData_day = (day + 1) + "";
                                                                } else {
                                                                    receiveData_mon = "08";
                                                                    receiveData_day = "01";
                                                                }
                                                                break;
                                                            case "08":
                                                                if (day != 31) {
                                                                    receiveData_day = (day + 1) + "";
                                                                } else {
                                                                    receiveData_mon = "09";
                                                                    receiveData_day = "01";
                                                                }
                                                                break;
                                                            case "09":
                                                                if (day != 30) {
                                                                    receiveData_day = (day + 1) + "";
                                                                } else {
                                                                    receiveData_mon = "10";
                                                                    receiveData_day = "01";
                                                                }
                                                                break;
                                                            case "10":
                                                                if (day != 31) {
                                                                    receiveData_day = (day + 1) + "";
                                                                } else {
                                                                    receiveData_mon = "11";
                                                                    receiveData_day = "01";
                                                                }
                                                                break;
                                                            case "11":
                                                                if (day != 30) {
                                                                    receiveData_day = (day + 1) + "";
                                                                } else {
                                                                    receiveData_mon = "12";
                                                                    receiveData_day = "01";
                                                                }
                                                                break;
                                                            case "12":
                                                                if (day != 31) {
                                                                    receiveData_day = (day + 1) + "";
                                                                } else {
                                                                    receiveData_year = (Integer.parseInt(receiveData_year) + 1) + "";
                                                                    receiveData_mon = "01";
                                                                    receiveData_day = "01";
                                                                }
                                                                break;
                                                        }

                                                    } else {
                                                        receiveTime_hour = (Integer.parseInt(data_arr[13].substring(0, 2)) + 9) + "";
                                                    }

                                                    bleViewModel.setBleDate(receiveData_year + getString(R.string.year) + " " + receiveData_mon + getString(R.string.month) + " " + receiveData_day + getString(R.string.day));
                                                    bleViewModel.setBleTime(receiveTime_hour + getString(R.string.hour) + " " + receiveTime_min + getString(R.string.min) + " " + receiveTime_sec + getString(R.string.sec));

                                                    //GPS 좌표 계산
                                                    String[] tempLatitude;
                                                    String lat1, lat2, lat3;
                                                    String[] tempLongitude;
                                                    String lon1, lon2, lon3;

                                                    //위도
                                                    if (Float.parseFloat(data_arr[14]) != 0f) {
                                                        tempLatitude = data_arr[14].split("\\.");
                                                        if (tempLatitude[0].charAt(0) != '-') {
                                                            lat1 = tempLatitude[0].substring(0, 2);
                                                            lat2 = tempLatitude[0].substring(2, 4);
                                                            lat1 = "N " + lat1;
                                                        } else {
                                                            lat1 = tempLatitude[0].substring(1, 3);
                                                            lat2 = tempLatitude[0].substring(3, 5);
                                                            lat1 = "S " + lat1;
                                                        }
                                                        lat3 = String.format("%.4f", Double.parseDouble("0." + tempLatitude[1]) * 60);

                                                        bleViewModel.setBleGPSLatitude(lat1 + "° " + lat2 + "\' " + lat3 + "\"");
                                                    } else {
                                                        bleViewModel.setBleGPSLatitude(data_arr[14].substring(0, 2) + "° " + data_arr[14].substring(2, 4) + "\' " + data_arr[14].substring(5, 7) + "." + data_arr[14].substring(7) + "\"");
                                                    }

                                                    String longitude = data_arr[15];
                                                    if (longitude.contains("*")) {
                                                        longitude = longitude.substring(0, longitude.indexOf("*"));
                                                    }

                                                    //경도
                                                    if (Float.parseFloat(longitude) != 0f) {
                                                        tempLongitude = longitude.split("\\.");
                                                        if (tempLongitude[0].charAt(0) != '-') {
                                                            lon1 = tempLongitude[0].substring(0, 3);
                                                            lon2 = tempLongitude[0].substring(3, 5);
                                                            lon1 = "E " + lon1;
                                                        } else {
                                                            lon1 = tempLongitude[0].substring(1, 4);
                                                            lon2 = tempLongitude[0].substring(4, 6);
                                                            lon1 = "W " + lon1;
                                                        }
                                                        lon3 = String.valueOf(String.format("%.4f", Double.valueOf("0." + tempLongitude[1]) * 60));

                                                        bleViewModel.setBleGPSLongitude(lon1 + "° " + lon2 + "\' " + lon3 + "\"");
                                                    } else {
                                                        bleViewModel.setBleGPSLongitude(longitude.substring(0, 3) + "° " + longitude.substring(3, 5) + "\' " + longitude.substring(6, 8) + "." + longitude.substring(8, 10) + "\"");
                                                    }





            /*tv_ble_status_gps_latitude.setText(data_arr[14]);
            String longitude = data_arr[15];
            if (longitude.contains("*")) {
                longitude = longitude.substring(0, longitude.indexOf("*"));
            }
            tv_ble_status_gps_longitude.setText(longitude);*/
                                                }

                                            } else if (data.substring(1, 6).contains(DATA_TYPE_LISET)) {

                                                String[] data_arr = data.split(",");
                                                try {

                                                    // 펌웨어 버전 값
                                                    bleViewModel.setBleFirmVer(data_arr[1]);

                                                    // RTU 용인지 확인
                                                    if (Integer.parseInt(data_arr[2]) == 0) {
                                                        bleViewModel.setBleRTUVer(getString(R.string.Lantern));
                                                    } else if (Integer.parseInt(data_arr[2]) == 1) {
                                                        bleViewModel.setBleRTUVer(getString(R.string.RTU));
                                                    }

                                                    // GPS 속도
                                                    if (Integer.parseInt(data_arr[3]) == 0) {
                                                        bleViewModel.setBleGPSSpeed("9600");
                                                    } else if (Integer.parseInt(data_arr[3]) == 1) {
                                                        bleViewModel.setBleGPSSpeed("4800");
                                                    }

                                                    // GPS Setting
                                                    if (Integer.parseInt(data_arr[4]) == 0) {
                                                        bleViewModel.setBleGPSAlways_Iv_On(0);
                                                        bleViewModel.setBleGPSAlways_Iv_Off(0);
                                                        bleViewModel.setBleGPSAlways("0");
                                                    } else if (Integer.parseInt(data_arr[4]) == 1) {
                                                        bleViewModel.setBleGPSAlways_Iv_On(1);
                                                        bleViewModel.setBleGPSAlways_Iv_Off(1);
                                                        bleViewModel.setBleGPSAlways("1");
                                                    }

                                                    String[] dataArr5 = data_arr[5].split("");

                                                    List<String> list = new ArrayList<String>();

                                                    for (String s : dataArr5) {
                                                        if (s != null && s.length() > 0) {
                                                            list.add(s);
                                                        }
                                                    }
                                                    String result = list.get(0) + list.get(1) + "." + list.get(2) + list.get(3) + " " + getString(R.string.Second_Sec);

                                                    bleViewModel.setBleDelayTime(result);


                                                    String temperature = "";
                                                    if (data_arr[6].contains("*")) {
                                                        temperature = data_arr[6].substring(0, data_arr[6].indexOf("*"));
                                                    } else {
                                                        temperature = data_arr[6];
                                                    }
                                                    //viewModel.setDataString(temperature + "°C");
                                                    bleViewModel.setBleTemperature(temperature + "°C");

                                                } catch (Exception e) {

                                                }


                                            }


                                            // 들어온 데이터값을 해당 프래그먼트로 보내기(그쪽에서 처리)
                                            if (currentFragment instanceof fragment_Ble_Function) {
                                                Log.d(TAG, "fragment_Ble_Function OK!");
                                                /*fragment_Ble_Function fragment_ble_function = (fragment_Ble_Function) getSupportFragmentManager().findFragmentById(R.id.bluetoothFragmentSpace);
                                                fragment_ble_function.readData(data);*/
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


                                            // 데이터 다 나눠준 후 재정리
                                            try {
                                                readDataTotal = readDataTotal.substring(readDataTotal.indexOf(StringList.DATA_SIGN_CHECKSUM) + 3);
                                                // 그 이후 데이터가 들어온게 있다면 다시 실행할 수 있도록 함.
                                                if (readDataTotal.indexOf(StringList.DATA_SIGN_START) > -1) {
                                                    Log.d(TAG, "readDataTotal remain : " + readDataTotal);
                                                } else {
                                                    Log.d(TAG, "readDataTotal no contain $ : " + readDataTotal);
                                                    // 작업 끝.들어온 데이터 초기화
                                                    readDataTotal = "";
                                                    return;
                                                }
                                            } catch (Exception e) {
                                                Log.e(TAG, "readDataTotal remain error : " + readDataTotal);
                                                readDataTotal = "";
                                                return;
                                            }

                                        } else {
                                            // 체크섬 값이 틀릴경우
                                            // 만약 $ 들어간게 2개이상이면
                                            if (count > 1) {
                                                // 다음 데이터 정리
                                                readDataTotal = readDataTotal.substring(readDataTotal.indexOf("$", readDataTotal.indexOf("$") + 1));
                                            } else {
                                                // 아닐 경우(해당 데이터만 있을 경우) 다 삭제
                                                readDataTotal = "";
                                                whileExit = false;
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.d(TAG, "checksum 검사 간 에러 : " + e.toString());
                                    return;
                                }


                            } else {
                                // 데이터가 잘못 들어옴.
                                logData_Ble("readData Error - order : " + readDataTotal, "error");

                                Log.d(TAG, "readData Error - order" + readDataTotal);
                                readDataTotal = readDataTotal.substring(readDataTotal.indexOf("$"));
                                Log.d(TAG, "readData Error - order - change" + readDataTotal);
                            }

                        } else {
                            return;
                        }
                    }

                    // 체크섬만 있을 경우(순서 이상함)
                    if (readDataTotal.indexOf(StringList.DATA_SIGN_CHECKSUM) > -1 && !(readDataTotal.indexOf(StringList.DATA_SIGN_START) > -1)) {
                        logData_Ble("readData Error - No include DATA_SIGN_START : " + readDataTotal, "error");
                        Log.d(TAG, "readData Error - no include DATA_SIGN_START");
                        readDataTotal = "";
                    }

                } catch (Exception e) {
                    logData_Ble("readData Error! - " + e.toString(), "error");
                    Log.e(TAG, "try Check error : " + e.toString());
                }

            }

        }
    };


    public static void BlewriteData(String data) {

        if (data.equals(StringList.DATA_REQUEST_STATUS)) {
            readDataTotal = "";
        }

        if (bleConnected) {

            if (cmdCharacteristic == null) {
                logData_Ble("cmdCharacteristic : null", "error");
                Log.d(TAG, "BlewriteData - cmdCharacteristic : null");
                cmdCharacteristic = BluetoothUtils.findCommandCharacteristic(bleGatt);
                logData_Ble("PROPERTY_WRITE Gatt write connect - null");
            }

            // 연결이 끊겨있다면 종료
            if (cmdCharacteristic == null) {
                logData_Ble("Unable to find cmd characteristic", "error");
                Log.e(TAG, "Unable to find cmd characteristic(writeData)");
                disconnectGattServer("BleMainActivity - BlewriteData - Unable to find cmd characteristic");
                return;
            }

            // 데이터를 보낼 경우 새로들어오는 데이터 중복 체크 값 초기화.
            readDataOverlapCheck = "";

            String sendBlewriteData = data + ToCheckSum(data);
            logData_Ble("Write : " + sendBlewriteData, "write");
            sendBlewriteData = sendBlewriteData + StringList.DATA_SIGN_CR + StringList.DATA_SIGN_LF;
            Log.d(TAG, "BleWriteData data : " + data + ", sendData : " + sendBlewriteData);

            // 데이터가 길면 한번에 보낼 수 있는 양을 초과해서 나눠서 보내야함.(5.0은 상관없지만...)
            if (sendBlewriteData.length() < 21) {
                cmdCharacteristic.setValue(sendBlewriteData.getBytes());
                //cmdCharacteristic.setWriteType(1);

                Boolean success = bleGatt.writeCharacteristic(cmdCharacteristic);
                if (!success) {
                    logData_Ble("Failed to write command", "error");
                    Log.e(TAG, "Failed to write command");
                }
            } else {
                Log.d(TAG, "BleWriteData data length : " + sendBlewriteData.length());


                String finalSendBlewriteData = sendBlewriteData;

                for (int i = 0; i < (finalSendBlewriteData.length() / 20) + 1; i++) {
                    if (finalSendBlewriteData.length() - (i * 20) < 20) {
                        cmdCharacteristic.setValue(finalSendBlewriteData.substring(i * 20).getBytes());
                    } else {
                        if (((i + 1) * 20) < finalSendBlewriteData.length()) {
                            cmdCharacteristic.setValue(finalSendBlewriteData.substring(i * 20, (i + 1) * 20).getBytes());
                        } else {
                            cmdCharacteristic.setValue(finalSendBlewriteData.substring(i * 20).getBytes());
                        }
                    }

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
        sendBlewriteData = sendBlewriteData + StringList.DATA_SIGN_CR + StringList.DATA_SIGN_LF;
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
        if (str.contains(StringList.DATA_SIGN_START)
                && str.indexOf(StringList.DATA_SIGN_CHECKSUM) > -1
                && str.indexOf(StringList.DATA_SIGN_START) < str.indexOf(StringList.DATA_SIGN_CHECKSUM)) {
            for (int n = 1; n < str.indexOf(StringList.DATA_SIGN_CHECKSUM); n++) {
                csInt ^= str.charAt(n);
            }

            csStr = Integer.toString(csInt, 16).toUpperCase();
            if (csStr.length() == 1) {
                //한자리 수 인경우, 앞에 0을 추가
                csStr = "0" + csStr;
            }
            Log.d(TAG, "toCheckSum csInt : " + csStr);
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
    }

    @Override
    public void onResume() {
        super.onResume();

        // 멈출때와 블루투스 연결 상태값이 달라진 경우 초기화면으로
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

            builder.setTitle("Notice").setMessage("초기화면으로 돌아가시겠습니까?\nDo you want to return to the initial screen?");

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
            fragmentChange("fragment_ble_scan");
        } else if (currentFragment instanceof fragment_CDS_Setting) {
            disconnectGattServer("bleMainActivity - onBackPressed - fragment_CDS_Setting");
            fragmentChange("fragment_ble_function");
        } else if (currentFragment instanceof fragment_SN_Setting) {
            disconnectGattServer("bleMainActivity - onBackPressed - fragment_SN_Setting");
            fragmentChange("fragment_ble_function");
        } else if (currentFragment instanceof fragment_Ble_Beginning) {
            super.onBackPressed();
        } else if (currentFragment instanceof fragment_Ble_Scan) {
            fragmentChange("fragment_ble_function");
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