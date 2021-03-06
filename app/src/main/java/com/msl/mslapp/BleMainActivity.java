

package com.msl.mslapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.msl.mslapp.ble.Dialog.setting.dialogFragment_ble_Setting_ModeSelect;
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

public class BleMainActivity extends AppCompatActivity implements fragment_Ble_Scan.Ble_Scan_Listener, fragment_Ble_Status.Ble_Status_Listener,
        fragment_SN_Setting.SN_Setting_Listener, fragment_CDS_Setting.CDS_Setting_Listener {

    //region ?????? ??? ?????? ??????

    // ?????? ?????? ???
    public static final String TAG = "Msl-Ble-MainAct";

    // ???????????? ??? ??????
    public static final boolean adminApp = false;
    // delaytime ??????????????? // ????????? ??? ????????? ???????????? ?????????
    public static final boolean delaytimeApp = true;

    public static Context mBleContext = null;

    public static AppCompatActivity mBleMain = null;
    public static String tLanguage;
    public BleActivityMainBinding mBleBinding;

    public static BleViewModel bleViewModel;

    // bluetooth ??????
    public static BluetoothAdapter mBluetoothAdapter = null;
    public BluetoothDevice bleConnectDevice = null;
    public static BluetoothGatt bleGatt = null;
    public static BluetoothManager bluetoothManager = null;
    public static String BluetoothStatus = "";
    public static boolean bleConnected = false;

    boolean pauseResumeCheck = false;

    // password ??????
    public static String readPassword = "";

    // gps ??????
    public static LocationManager locationManager = null;
    // requestCode
    public static final int bluetooth_permission_check = 50;
    public static ActivityResultLauncher<Intent> requestPermissionBle;


    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 51;
    private final int MY_PERMISSIONS_REQUEST_BLUETOOTH_SCAN = 52;
    private final int MY_PERMISSIONS_REQUEST_BLUETOOTH_CONNECT = 53;
    // handler msg??????
    public static final int ConnectSuccess = 0;
    public static final int readDataSuccess = 1;
    // Ble connecting
    public static boolean BleConnecting = false;
    //Ble search
    // ?????? ??????????????? ???????????? ?????????, true??? ?????? - ?????????
    public static boolean scanningFlag = false;
    // CDS ?????? ??????
    public static boolean CdsFlag = false;
    // SN ?????? ??????
    public static boolean SnFlag = false;

    // ??????????????? ????????? ?????? ?????????
    public static String readDataOverlapCheck = "";

    // ?????? ?????? ?????????
    public static BluetoothGattCharacteristic respCharacteristic = null;
    public static BluetoothGattCharacteristic cmdCharacteristic = null;
    public static BluetoothGattCharacteristic nameCharacteristic = null;

    // ???????????? ????????? ????????? ???
    public static String readDataTotal = "";
    ConcurrentLinkedQueue<String> readDataQueue = new ConcurrentLinkedQueue<>();

    //????????? BLE UUID Service/Rx/Tx
    //public static String SERVICE_STRING = "6E400001-B5A3-F393-E0A9-E50E24DCCA9E";

    public static String SERVICE_STRING = "6E400001-B5A3-F393-E0A9-E50E24DCCA9E";
    public static String CHARACTERISTIC_COMMAND_STRING = "6E400002-B5A3-F393-E0A9-E50E24DCCA9E";
    public static String CHARACTERISTIC_RESPONSE_STRING = "6E400003-B5A3-F393-E0A9-E50E24DCCA9E";
    // ????????? ??? uuid
    public static String SERVICE_STRING_4 = "0000fff0-0000-1000-8000-00805f9b34fb";
    public static String CHARACTERISTIC_COMMAND_STRING_4 = "0000fff2-0000-1000-8000-00805f9b34fb";
    public static String CHARACTERISTIC_RESPONSE_STRING_4 = "0000fff1-0000-1000-8000-00805f9b34fb";

    // test - device information - serial number string
    public static String Serial_Number_String = "00002a25-0000-1000-8000-00805f9b34fb";
    //BluetoothGattDescriptor ??????
    public String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    //scan filter
    public static ArrayList<ScanFilter> filters = new ArrayList();
    public static ScanFilter scanFilter = new ScanFilter.Builder().build();
    // set ????????? ?????? ?????? ????????? uuid??? ?????? ???(?????? ????????? ?????? ??????????)
    //public static ScanFilter scanFilter = new ScanFilter.Builder().setServiceUuid(new ParcelUuid(UUID.fromString(SERVICE_STRING))).build();
    // .setServiceUuid(new ParcelUuid(UUID.fromString(SERVICE_STRING))) : ?????? uuid ???????????? ??????
    // .setDeviceAddress(address) : address ??? ??????   String[] peripheralAddresses = new String[]{"01:0A:5C:7D:D0:1A"};
    // .setDeviceName(name) : name ??? ??????
    // ??????????????? ????????? ?????? ??????.
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
    // scan ????????? lowpower??? ??????
    public static ScanSettings settings = new ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build();
    //public static ScanSettings settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).setReportDelay(0).setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES).build();


    //endregion

    //endregion ?????? ??? ?????? ?????? ???

    //region layout

    MenuItem ScanItem;
    MenuItem bleDisconnectItem;

    // ?????? ??????????????? ?????????
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


    // fragment?????? ????????? ???????????? ???????????????

    // scan?????? ????????? ??????????????? ??????
    @Override
    public void onSelectBleDevice(BluetoothDevice device) {
        Log.d(TAG, "onSelectBleDevice");
        Log.d(TAG, "????????? ???????????? ?????? : " + device.getName());

        bleConnectDevice = device;
        // ???????????? ????????? ?????????
        connectToDevice(mBleContext, bleConnectDevice, gattClientCallback);
        //bleGatt = bleConnectDevice.connectGatt(mBleContext, false, gattClientCallback);
    }
    public static void connectToDevice(Context context,
                                       BluetoothDevice device,
                                       BluetoothGattCallback mGattCallBack) {
        if (device != null) {
            // ??? os ??? ?????? ??? ?????? ????????? ?????????. ???????????? ?????? ????????? ??????.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(TAG, "connectToDevice - O up");
                bleGatt = device.connectGatt(context, false, mGattCallBack,
                        BluetoothDevice.DEVICE_TYPE_LE, BluetoothDevice.PHY_LE_2M | BluetoothDevice.PHY_LE_1M);
            } else {
                Log.d(TAG, "connectToDevice - M up");
                bleGatt = device.connectGatt(context, false, mGattCallBack,
                        BluetoothDevice.DEVICE_TYPE_LE);
            }
            // ??????????????? ???????????? ????????? ?????????????????? ????????????!
            /*else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.d(TAG, "connectToDevice - M up");
                bleGatt = device.connectGatt(context, false, mGattCallBack,
                        BluetoothDevice.DEVICE_TYPE_LE);
            } else {
                Log.d(TAG, "connectToDevice - M down");
                bleGatt = device.connectGatt(context, false,
                        mGattCallBack);
            }*/

        } else {
            //WiSeSdkFileWritter.writeToFile(fileName, TAG + " FAILED could not find remote device/ remote device is null >>" + macAddress);
        }
    }


    @Override
    public void onPauseFragment_Ble_Scan() {
        Log.d(TAG, "onPauseFragment_Ble_Scan");
        ScanItem.setTitle(R.string.ble_main_scanItem_scan);
    }

    // toolbar ?????? ?????? ???????????????
    @Override
    public void onCreateViewFragment_Ble_Scan() {
        Log.d(TAG, "onCreateViewFragment_Ble_Scan");
        ScanItem.setVisible(true);
    }

    // toolbar ?????? ?????? ?????????
    @Override
    public void onDetachFragment_Ble_Scan() {
        Log.d(TAG, "onDetachFragment_Ble_Scan");
        ScanItem.setTitle(R.string.ble_main_scanItem_stopScanning);
        ScanItem.setVisible(false);
    }

    // status????????? ?????? ??? ???????????????
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

    public void ble_stopscanning() {
        ScanItem.setTitle(R.string.ble_main_scanItem_scan);
    }


    // menu ?????? - ?????? ??? ????????? ??????
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.actionbar_bluetooth, menu);
        ScanItem = menu.findItem(R.id.action_bar_scanBle);
        bleDisconnectItem = menu.findItem(R.id.action_bar_bleDisconnect);

        return true;
    }

    // menu ???
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.bluetoothFragmentSpace);
        fragment_Ble_Scan fragment_bleScan = null;
        // fragment ??? ????????? ?????? ?????? ??? ??????
        if (fragment instanceof fragment_Ble_Scan) {
            fragment_bleScan = (fragment_Ble_Scan) getSupportFragmentManager().findFragmentById(R.id.bluetoothFragmentSpace);
        }

        switch (item.getItemId()) {
            case R.id.action_bar_scanBle:

                // ???????????? ??? gps ?????? ????????? ????????? ???????????? ???????????????. ???????????? ?????? ?????? ??? ?????? ??? ??????.
                if(permissioncheck_Scan()){
                    fragmentChange("fragment_ble_scan");
                }

                return true;

            case R.id.action_bar_bleDisconnect:
                disconnectGattServer("bleMainActivity - onOptionsItemSelected - action_bar_bleDisconnect");

                bleDisconnectItem.setVisible(false);
                ScanItem.setVisible(true);
                ScanItem.setTitle(R.string.ble_main_scanItem_scan);
                toolbar_title.setText("");
                //fragmentChange("fragment_ble_beginning");
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    // toolbar ?????? ????????? ?????? ??? ????????? ??????.
    public static void navigation_icon_Change(String fragment) {
        switch (fragment) {
            case "beginning":
                drawerToggle = new ActionBarDrawerToggle(mBleMain, bleDrawerLayout, toolbarMain, R.string.app_name, R.string.app_name);
                drawerToggle.syncState(); // ?????? ?????? ??????
                bleDrawerLayout.addDrawerListener(drawerToggle); // ?????? ?????? ?????? ??? ???????????? ??????.
                toolbarMain.setNavigationIcon(R.drawable.toolbar_icon);
                toolbar_title.setText("MSL Technology");
                break;
            case "scan":

                toolbarMain.setNavigationOnClickListener(v -> mBleMain.onBackPressed());

                toolbarMain.setNavigationIcon(R.drawable.ble_fragment_back);
                break;
            case "function":
                drawerToggle = new ActionBarDrawerToggle(mBleMain, bleDrawerLayout, toolbarMain, R.string.app_name, R.string.app_name);
                drawerToggle.syncState(); // ?????? ?????? ??????
                bleDrawerLayout.addDrawerListener(drawerToggle); // ?????? ?????? ?????? ??? ???????????? ??????.
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

        // gps on ???????????? ??????
        locationManager = (LocationManager) mBleContext.getSystemService(LOCATION_SERVICE);

        // ???????????? ?????? ??? ??????
        bluetoothManager = (BluetoothManager) mBleContext.getSystemService(Context.BLUETOOTH_SERVICE);

        //region Bluetooth ?????? ??? ????????? ??????
        //mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // ???????????? ?????? ?????????
        requestPermissionBle = mBleMain.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // result.data => intent
                Log.d(TAG, "MainActivity??? ????????????. ");
            }
        });

        // ?????? ??????
        class MainLanguageRunnable implements Runnable {
            public void run() {
                //????????????
                SharedPreferences sfSideBar = getSharedPreferences("option_data", MODE_PRIVATE);
                Locale systemLocale = mBleContext.getResources().getConfiguration().locale; //????????? ?????? ????????? ?????????
                String languageState = sfSideBar.getString("language_mode", systemLocale.getLanguage()); //????????? ????????? ?????????
                Log.d(TAG, "System Language : " + languageState);
                tLanguage = languageState; //???????????? ???????????? ????????? ?????????, ???????????? ????????? ??????
                if (tLanguage != null) setLocale(tLanguage); //?????? ??????
            }
        }

        MainLanguageRunnable nr = new MainLanguageRunnable();
        Thread t = new Thread(nr);
        t.start();


        // ?????? ??????
        toolbarMain = (Toolbar) findViewById(R.id.ble_toolbar_main);
        toolbarMain.setTitle("");
        toolbarMain.setTitleTextColor(getResources().getColor(R.color.ble_toolbarmain));

        toolbar_title = findViewById(R.id.toolbar_title_ble);

        setSupportActionBar(toolbarMain);

        // toolbar ?????? ?????? ??????. true ?????? ?????? ???????????? ???????????? ??????.
        // null ?????? ????????? ????????? nullpoint ?????? ??? ??? ?????????.(????????? ????????? ?????? ??????(?????? ??????) ?????? ??????????????? ?????? ??????????????? ????????? ??????)
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        navigation_Setting();


        // ???????????? ?????? ?????? ?????? ??? ?????? ??????
        class MainBleFilterPermissionRunnable implements Runnable {
            MainBleFilterPermissionRunnable() {

            }

            public void run() {

                // ???????????? ????????? ??????????????? ??? ???????????? ?????????.
                IntentFilter filter1 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                registerReceiver(mBroadcastReceiver1, filter1);

                // ?????? ?????? ????????? ??? ????????? ?????????
        /*IntentFilter filter2 = new IntentFilter();
        filter2.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter2.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter2.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2, filter2);*/

                // ??????????????? ??????????????????, ???????????? ?????????.
                IntentFilter filter3 = new IntentFilter();
                filter3.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
                filter3.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
                registerReceiver(mBroadcastReceiver3, filter3);

                permission_check();
                Log.d(TAG, "bluetooth 5 ?????? ?????? : " + Build.VERSION.SDK_INT + " ?????? O ??? ?????? : " + Build.VERSION_CODES.O);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    // setLegacy ??? ??? ????????? ????????? ???????????? ?????????(????????? ??????????????? ??????)
            /*settings = new ScanSettings.Builder()
                    .setLegacy(false)
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();*/

                    // ???????????? ??????
                    settings = new ScanSettings.Builder().
                            setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).
                            setReportDelay(0).
                            setLegacy(false).build();

                    Log.d(TAG, "bluetooth 5 ?????? ???");

                    /*Log.d(TAG, "BluetoothAdapter isLe2MPhySupported() : " + mBluetoothAdapter.isLe2MPhySupported());
                    Log.d(TAG, "BluetoothAdapter isLeCodedPhySupported() : " + mBluetoothAdapter.isLeCodedPhySupported());
                    Log.d(TAG, "BluetoothAdapter isLeExtendedAdvertisingSupported() : " + mBluetoothAdapter.isLeExtendedAdvertisingSupported());
                    Log.d(TAG, "BluetoothAdapter isLePeriodicAdvertisingSupported() : " + mBluetoothAdapter.isLePeriodicAdvertisingSupported());
                    Log.d(TAG, "BluetoothSetting getLegacy() : " + settings.getLegacy());
                    Log.d(TAG, "BluetoothSetting getPhy() : " + settings.getPhy());
                    Log.d(TAG, "BluetoothSetting getScanMode() : " + settings.getScanMode());
                    Log.d(TAG, "BluetoothSetting getScanResultType() : " + settings.getScanResultType());
                    Log.d(TAG, "BluetoothSetting getCallbackType() : " + settings.getCallbackType());*/
                }

                // ?????? ????????? ??????????????? ??? ??? ??????. => scanFilter ?????? - .setServiceUuid()?????? .setDeviceAddress(MAC_ADDR)??? ????????? Uuid ?????? ?????? mac address??? ??????
                filters.add(scanFilter);
            }
        }

        MainBleFilterPermissionRunnable fpR = new MainBleFilterPermissionRunnable();
        Thread fpT = new Thread(fpR);
        fpT.start();


        //region  layout ??????

        //endregion

        Log.d(TAG, "fragment ??????");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.bluetoothFragmentSpace, new fragment_Ble_Beginning());
        ft.commit();

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.d(TAG, "?????? ??????.....");
        }


        //region layout ??????

        //endregion
    }

    Handler NO_GATT_SUCCESS_Fail_handler = new Handler(Looper.getMainLooper()) {
    };

    void navigation_Setting() {
        // ???????????? ??????
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

        Display display;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            display = mBleContext.getDisplay();
        } else {
            display = ((WindowManager) mBleContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        }
        Point size = new Point();
        display.getSize(size);

        int x_log_on = Math.toIntExact(Math.round((size.x * 0.8)));
        int x_log_off = Math.toIntExact(Math.round((size.x * 0.65)));

        // ?????? ?????? ?????? ??????
        navigationView.getLayoutParams().width = x_log_off;

        // Ble ??????
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

        // RTU??? ??????
        navigationView.findViewById(R.id.ll_Navigation_Move_RTU).setOnClickListener(
                new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        Intent intent = new Intent(mBleContext, RTUMainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                });

        // ???????????? ??????
        navigationView.findViewById(R.id.ll_Navigation_Show_Language).setOnClickListener(
                new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        FragmentManager fm = getSupportFragmentManager();
                        dialogFragment_Ble_Beginning_LanguageChange customDialogLanguageChange = new dialogFragment_Ble_Beginning_LanguageChange();
                        customDialogLanguageChange.show(fm, "fragment_beginning_dialog_LanguageChange");
                    }
                });

        // FL List ??? ??????
        navigationView.findViewById(R.id.ll_Navigation_Show_FL_List).setOnClickListener(
                new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        FragmentManager fm = getSupportFragmentManager();
                        dialogFragment_Ble_Setting_FL_Setting customDialog_FL_Setting = new dialogFragment_Ble_Setting_FL_Setting();
                        customDialog_FL_Setting.show(fm, "dialogFragment_Ble_Setting_FL_Setting");
                    }
                });

        // Log ??????
        navigationView.findViewById(R.id.ll_Navigation_Show_Log).setOnClickListener(v -> {
            navigationView.getLayoutParams().width = x_log_on;
            ll_navigation_log.setVisibility(View.VISIBLE);
            ll_navigation_move.setVisibility(View.GONE);
        });

        // Log ??????
        navigationView.findViewById(R.id.btn_Navigation_Log_Close).setOnClickListener(v -> {
            navigationView.getLayoutParams().width = x_log_off;
            ll_navigation_log.setVisibility(View.INVISIBLE);
            ll_navigation_move.setVisibility(View.VISIBLE);
        });

        // GPS ??????
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

        // ???????????? ??????
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

        // ????????? or ?????? ????????? ?????? ??????
        navigationView.findViewById(R.id.ll_Navigation_ModeSelect).setOnClickListener(v -> {

            if (BleConnecting) {
                postHandler.postDelayed(() -> {
                    FragmentManager fm = getSupportFragmentManager();
                    dialogFragment_ble_Setting_ModeSelect setting_modeSelect_DialogFragment = new dialogFragment_ble_Setting_ModeSelect();
                    setting_modeSelect_DialogFragment.show(fm, "fragment_setting_dialog_ModeSelect");
                }, 200);
            } else {
                Toast.makeText(mBleContext, "bluetooth not connecting", Toast.LENGTH_SHORT).show();
            }
        });


        bleDrawerLayout = findViewById(R.id.ble_drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, bleDrawerLayout, toolbarMain, R.string.app_name, R.string.app_name);
        drawerToggle.syncState(); // ?????? ?????? ??????
        bleDrawerLayout.addDrawerListener(drawerToggle); // ?????? ?????? ?????? ??? ???????????? ??????.
    }


    public static void navigation_GPS_Visible(boolean visble) {
        if (visble) {
            ll_navigation_GPS.setVisibility(View.VISIBLE);
            ll_navigation_PasswordChange.setVisibility(View.VISIBLE);
            ll_navigation_ModeSelect.setVisibility(View.VISIBLE);
            ll_navigation_Language.setVisibility(View.GONE);
        } else {
            ll_navigation_GPS.setVisibility(View.GONE);
            ll_navigation_PasswordChange.setVisibility(View.GONE);
            ll_navigation_ModeSelect.setVisibility(View.GONE);
            ll_navigation_Language.setVisibility(View.VISIBLE);
        }
    }

    // ????????? ????????? ???????????? ??????
    public static void logData_Ble(String data) {
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("hh:mm:ss");
        String getTime = simpleDate.format(mDate);

        log_listViewAdapter.addItem(getTime, data);
        //log_listViewAdapter.refreshAdapter(log_Listview);
        //log_listViewAdapter.notifyDataSetChanged();

    }


    // ?????? ????????? ??????(read, write, error)
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

        // ??? ?????? ??? ????????? ??? ???????????? ?????? ?????? ????????? ?????????(?????? ????????? ?????? ????????????)
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
                // ???????????? ?????? ????????? ????????? ????????? ???????????? ????????? ???(????????? ?????????????????? ??????)
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
            } else if (dataArr[0].contains("AT$$MDN")) {
                log_listViewAdapter.addItem(getTime, "Modem CallNum Request", color);
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

        builder.setTitle("?????? ??????").setMessage("?????? ???????????? GPS ????????? ???????????? ????????????.");

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


    // gps ?????? ??????
    private boolean permission_check() {

        Log.d(TAG, "buile Version : " + Build.VERSION.SDK_INT);

        // ?????? ??? ?????? ????????? ??????. Fine : GPS, ??????????????? ????????? ????????????/ coarse : ??????????????? ????????? ????????????
        if (Build.VERSION.SDK_INT >= 29) {
            Log.d(TAG, "buile Version sdk 29 same or UP : " + Build.VERSION.SDK_INT);
            int permissionCheck = ContextCompat.checkSelfPermission(mBleContext,
                    Manifest.permission.ACCESS_FINE_LOCATION);

            Log.d(TAG, "GPS ?????? ACCESS_FINE_LOCATION" + permissionCheck);

            if (permissionCheck < 0) {
                String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

                requestPermissions(permissions, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                return false;
            }else{
                return permissionCheckingBLUETOOTHSCAN();
            }
        } else {
            int permissionCheck = ContextCompat.checkSelfPermission(mBleContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION);

            if (permissionCheck < 0) {
                String[] permissions = new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION
                };
                requestPermissions(permissions, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                return false;
            }
        }

        return true;
    }



    // ?????? ?????? ????????? ?????? fragment ??? ?????? ??? ????????? ???????????? ????????? ?????? ????????? ???.
    public boolean permissioncheck_Scan(){
        // GPS ??? ??????????????? ??????
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

            //GPS ?????????????????? ??????
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(R.string.ble_main_checkGPSManager_alertDialog_title).setMessage(R.string.ble_main_checkGPSManager_alertDialog_message);

            builder.setPositiveButton("OK", (dialog, id) -> {

                mBleMain.startActivity(intent);
            });

            builder.setNegativeButton("Cancel", (dialog, id) -> {

            });
            AlertDialog alertDialog = builder.create();

            alertDialog.show();

            return false;
        }


        return permission_check();
    }

    // sdk 31 ????????? ?????? ???????????? SCAN ????????? ??????????????? ???????????? ??????.
    public boolean permissionCheckingBLUETOOTHSCAN(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.d(TAG, "buile Version sdk 31 UP : " + Build.VERSION.SDK_INT);

            String[] permissionCheck = new String[]{
                    Manifest.permission.BLUETOOTH_SCAN
            };

            int permissionChecking = ContextCompat.checkSelfPermission(mBleContext,
                    Manifest.permission.BLUETOOTH_SCAN);

            if(permissionChecking < 0){
                Log.d(TAG, "RequestPermissions BLUETOOTH_SCAN : ?????? ??????");
                requestPermissions(permissionCheck, MY_PERMISSIONS_REQUEST_BLUETOOTH_SCAN);
                return false;
            }else{
                Log.d(TAG, "RequestPermissions BLUETOOTH_SCAN : ?????? ??????");
            }

        }


        return true;
    }


    // fragment ??????
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
            Log.e(TAG, "fragmentChange ??? ?????? ??????" + e.toString());
        }
    }

    // onActivityResult ??? deprecated ??? ?????? ?????? ??? (21-10-20)
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case bluetooth_permission_check: // ???????????? ?????? ??????
                if (resultCode == RESULT_OK) {
                    // ?????? GPS ?????? ??????
                    checkGPSserviceOn();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle("?????? ??????").setMessage("???????????? ?????? ????????? ?????????????????????.\n???????????? ?????? ????????? ???????????????.\n?????? ?????????????????????????");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // ?????? ???????????? ??????.
                            checkBluetoothPermission();
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // ?????? GPS ?????? ??????
                            checkGPSserviceOn();
                        }
                    });
                    AlertDialog alertDialog = builder.create();

                    alertDialog.show();
                }
                break;

        }
    }*/

    /*// ?????? ?????? ???
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // ?????? ???
                    Log.d(TAG, "RequestPermissions GPS : ??????");

                } else {
                    // ?????? ???
                    Log.d(TAG, "RequestPermissions GPS : ??????");
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

    // ?????? ?????? ???
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // ?????? ???

                    Log.d(TAG, "RequestPermissions GPS : ??????");

                    permissionCheckingBLUETOOTHSCAN();

                } else {
                    // ?????? ???
                    Log.d(TAG, "RequestPermissions GPS : ??????");
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle(R.string.ble_main_checkPermission_GPS_fail_alertDialog_title).setMessage(R.string.ble_main_checkPermission_GPS_fail_alertDialog_message);


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
            case MY_PERMISSIONS_REQUEST_BLUETOOTH_SCAN: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // ?????? ???
                    Log.d(TAG, "RequestPermissions BLUETOOTH_SCAN : ??????");

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {

                        String[] permissionsCheck = new String[]{
                                Manifest.permission.BLUETOOTH_CONNECT
                        };

                        int permissionChecking = ContextCompat.checkSelfPermission(mBleContext,
                                Manifest.permission.BLUETOOTH_CONNECT);

                        if(permissionChecking < 0){
                            Log.d(TAG, "RequestPermissions BLUETOOTH_CONNECT : ?????? ??????");
                            requestPermissions(permissionsCheck, MY_PERMISSIONS_REQUEST_BLUETOOTH_CONNECT);
                        }else{
                            Log.d(TAG, "RequestPermissions BLUETOOTH_CONNECT : ?????? ??????");
                        }
                    }

                } else {
                    // ?????? ???
                    Log.d(TAG, "RequestPermissions BLUETOOTH_SCAN : ??????");
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_BLUETOOTH_CONNECT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // ?????? ???
                    Log.d(TAG, "RequestPermissions BLUETOOTH_CONNECT ??????");

                } else {
                    // ?????? ???
                    Log.d(TAG, "RequestPermissions BLUETOOTH_CONNECT : ??????");
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    // ???????????? ?????? ??????
    public static boolean checkBluetoothPermission() {
        // ???????????? ?????? ?????? ??????
        Intent intent;

        // ???????????? ????????? ??????`
        if (mBluetoothAdapter.isEnabled()) {
            BluetoothStatus = "On";
            // ?????? GPS ?????? ??????
            return checkGPSserviceOn();
        } else {
            // ???????????? ????????? ?????????
            intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            requestPermissionBle.launch(intent);
            //mBleMain.startActivityForResult(intent, bluetooth_permission_check);
            return false;
        }
        // ???????????? ?????? ?????? ???
    }

    // ????????????(GPS) ??????
    public static boolean checkGPSserviceOn() {

        // ??? ?????? ??? gps??? ????????? ????????? ????????? ??????.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {

            int permissionCheck = ContextCompat.checkSelfPermission(mBleContext,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionCheck < 0) {

                Log.d(TAG, "permissioncheck_no_FINE_LOCATION");
                Toast.makeText(mBleContext, "GPS permission is not found. please check permission.", Toast.LENGTH_LONG).show();
                return false;
            }
            int permissionCheckSCAN = ContextCompat.checkSelfPermission(mBleContext,
                    Manifest.permission.BLUETOOTH_SCAN);

            if(permissionCheckSCAN < 0){
                Log.d(TAG, "permissioncheck_no_BLUETOOTH_SCAN");
                Toast.makeText(mBleContext, "bluetooth permission is not found. please check permission.", Toast.LENGTH_LONG).show();
                return false;
            }

            return true;
        }else{

            int permissionCheck = ContextCompat.checkSelfPermission(mBleContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            if (permissionCheck < 0) {

                Toast.makeText(mBleContext, "GPS permission is not found. please check permission.", Toast.LENGTH_LONG).show();

                Log.d(TAG, "ACCESS_COARSE_LOCATION no");
                return false;
            }
            return true;
        }




        /*// ???????????? on ?????? ??????
        Intent intent;

        // ???????????? ?????? Intent
        intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(mBleContext);

            builder.setTitle(R.string.ble_main_checkPermission_GPS_alertDialog_title).setMessage(R.string.ble_main_checkPermission_GPS_alertDialog_message);

            builder.setPositiveButton("OK", (dialog, id) -> {

                mBleMain.startActivity(intent);
            });

            builder.setNegativeButton("Cancel", (dialog, id) -> {

            });
            AlertDialog alertDialog = builder.create();

            alertDialog.show();

            return false;

        }
        // ???????????? on ?????? ???

        return true;*/
    }

    // ?????? ?????? ??? n??? ?????? ????????????
    public static int connectFail = 0;


    // ???????????? ?????? ??????.
    private final BluetoothGattCallback gattClientCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.d(TAG, "onConnectionStateChange status : " + status);
            if (status == BluetoothGatt.GATT_FAILURE) {
                disconnectGattServer("BleMainActivity - gattClientCallback - GATT_FAILURE");
                fragmentChange("fragment_ble_beginning");
                return;
            } else if (status != BluetoothGatt.GATT_SUCCESS) {
                // ???????????? ????????? ?????? ?????? ?????? ??????. 5??? ???????????? ?????? ??? ????????? ??????(?????? ??? 1 ??? ?????? ????????? ????????? ????????? ?????? ?????? ?????? ??????)
                // 8 : ?????? ???????????? ????????? ????????? ????????? ?????????????????? ??????
                // 133 : 8 ????????? ???????????? ????????? ?????? ??????
                if (status == 8) {
                    logData_Ble("Bluetooth Disconnect - \nproblem between Bluetooth communication(distance or shutdown) - " +
                            "back to the initial screen", "error");
                    connectFail = 0;
                    disconnectGattServer("BleMainActivity - gattClientCallback - NO_GATT_SUCCESS");
                    fragmentChange("fragment_ble_beginning");
                    Log.d(TAG, "onConnectionStateChange state 8 : ???????????? ?????? ?????????(?????? ?????? ????????????)");
                    NO_GATT_SUCCESS_Fail_handler.postDelayed(() -> {
                        Toast.makeText(mBleContext, getString(R.string.bleDisconnect_Connection_Failed), Toast.LENGTH_LONG).show();
                    }, 100);
                    /*if (connectFail < 5) {
                        logData_Ble("Bluetooth Disconnect - \n?????? ????????? - ????????? ??????", "error");
                        connectFail += 1;
                        connectToDevice(mBleContext, bleConnectDevice, gattClientCallback);
                        Log.d(TAG, "NO_GATT_SUCCESS " + connectFail + "??? ?????? : " + status);
                    } else {
                        logData_Ble("Bluetooth Disconnect - \n?????? ????????? - ??????????????????", "error");
                        connectFail = 0;
                        disconnectGattServer("BleMainActivity - gattClientCallback - NO_GATT_SUCCESS");
                        fragmentChange("fragment_ble_beginning");
                        Log.d(TAG, "NO_GATT_SUCCESS 5??? ?????? : " + status);
                    }*/
                } else if (status == 133) {
                    if (connectFail < 5) {
                        logData_Ble("Bluetooth Disconnect - \nConnection Failed - Attempt to reconnect", "error");
                        connectFail += 1;
                        connectToDevice(mBleContext, bleConnectDevice, gattClientCallback);
                        Log.d(TAG, "NO_GATT_SUCCESS " + connectFail + "??? ?????? : " + status);
                    } else {
                        logData_Ble("Bluetooth Disconnect - \n" +
                                "Connection failed 5 times - back to the initial screen", "error");
                        connectFail = 0;
                        disconnectGattServer("BleMainActivity - gattClientCallback - NO_GATT_SUCCESS");
                        fragmentChange("fragment_ble_beginning");
                        Log.d(TAG, "NO_GATT_SUCCESS 5??? ?????? : " + status);

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

        // ????????? ????????? ??????
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

            // uuid, Characteristic ?????????
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
                                    Log.d(TAG, " ??????!!" + nameCharacteristic.getStringValue(0));
                                }
                            } catch (Exception e) {
                                Log.d(TAG, "??????!!" + e.toString());
                            }

                            // bluetooth 5 ??? ????????? ????????????
                            if (characteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_WRITE ||
                                    characteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) {
                                cmdCharacteristic = characteristic;
                                logData_Ble("PROPERTY_WRITE connect");
                                if (characteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_WRITE) {
                                    Log.d(TAG, "PROPERTY_WRITE connect W: " + characteristic.getProperties() + " , " + BluetoothGattCharacteristic.PROPERTY_WRITE);
                                }
                                if (characteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) {
                                    Log.d(TAG, "PROPERTY_WRITE connect No: " + characteristic.getProperties() + " , " + BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE);
                                }

                                if (cmdCharacteristic == null) {
                                    Log.d(TAG, "onServicesDiscovered - cmdCharacteristic : null");
                                    // ?????? ?????? ?????? ???????????????...???????????? ?????? ?????? ?????????(BleWrite????????????) ??????????????????
                                    cmdCharacteristic = BluetoothUtils.findCommandCharacteristic(bleGatt);
                                    logData_Ble("PROPERTY_WRITE Gatt write connect");
                                }

                            } else if (characteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_NOTIFY) {
                                Log.d(TAG, "onServicesDiscovered - PROPERTY_NOTIFY");
                                logData_Ble("PROPERTY_NOTIFY read connect");
                                respCharacteristic = characteristic;
                            }

                            // ????????? ????????????(????????? ??????(PROPERTY_NOTIFY)??? ?????????, ????????? uuid??? ??????)
                            if (characteristic.getUuid().toString().equals(CHARACTERISTIC_COMMAND_STRING_4)) {
                                Log.d(TAG, "CHARACTERISTIC_COMMAND_STRING_4 is");
                                if (cmdCharacteristic == null) {
                                    cmdCharacteristic = characteristic;
                                    logData_Ble("Bluetooth 4 write connect");
                                }
                            } else if (characteristic.getUuid().toString().equals(CHARACTERISTIC_RESPONSE_STRING_4)) {
                                Log.d(TAG, "CHARACTERISTIC_RESPONSE_STRING_4 is : " + characteristic.getProperties() + " , " + BluetoothGattCharacteristic.PROPERTY_NOTIFY);
                                respCharacteristic = characteristic;
                                logData_Ble("Bluetooth 4 read connect");
                            }
                        }
                    }

                }
            }
            // read ????????? ???????????? ?????? ??????
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

            // uuid, Characteristic ???????????????
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

        // ???????????? ????????? ?????? ???????????? ???????????? ??????
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            //Log.d(TAG, "onCharacteristicChanged written successfully : " + new String(characteristic.getValue()));
            readCharacteristic(characteristic);
        }

        // ???????????? ????????? ?????? ???
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

        // ???????????? ????????? ?????? ??? ??????
        private void readCharacteristic(BluetoothGattCharacteristic characteristic) {

            String readCharacter = characteristic.getStringValue(0);

            if (!readDataOverlapCheck.equals(readCharacter)) {
                readDataOverlapCheck = readCharacter;
            } else {
                if (readCharacter.contains(StringList.DATA_TYPE_PS)) {
                    requestPasswordCount += 1;
                    Log.d(TAG, "Request a Password Count : " + requestPasswordCount);
                    // ????????? ?????? ??? ????????? ?????? ??????????????? ????????? ????????????, ????????? ????????? ?????????????????? ???????????? ????????? ?????? ??????. ?????? ???????????? ??????.
                    if (requestPasswordCount >= 5) {
                        // ?????? ?????? ???????????? ?????? ??? ????????? ????????? ?????? ??????.
                        BlewriteData("$PS,A,0000H*");
                        requestPasswordCount = 0;
                        readDataOverlapCheck = "";
                    }
                }

                return;
            }

            /*
            // ??????????????? ?????? ???. readCharacteristic ??? ????????? ???????????? ??????....
            // ?????? ????????? ??? properties ???  read ???????????? ?????? ?????? ?????? ?????? onCharacteristicRead ??? ???????????? ?????????.
            bleGatt.readCharacteristic(nameCharacteristic);

            //???????????? ???????????? ?????? ?????? ????????? ?????? ????????????.
            if(characteristic.equals(nameCharacteristic)){

                Log.d(TAG, "readCharacteristic - nameCharacteristic  : " + readCharacter);
            }*/

            // ??????????????? ????????? 2??? ????????? ????????? ???????????? ???????????? ?????? ?????? ?????? ????????? ??????/
            readDataQueue.offer(readCharacter);

            // ????????? ???????????? ????????? ??????. ?????? ??????
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
                Log.d(TAG, "byteArrayToHexaString n ?????? : " + b);
            if (b == '\r')
                Log.d(TAG, "byteArrayToHexaString r ?????? : " + b);
        }

        for (byte b : bytes) {
            sb.append(String.format("%02X", b & 0xff));
        }

        return sb.toString();
    }


    // ???????????? ?????? ??????
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

    // ?????? ??? ????????? ??????????????? ?????? ??????
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                // ?????? ????????? ??????

            } else if (msg.what == 1) {
                // ???????????? ???????????? ???

                try {
                    if (readDataTotal.trim().equals("")) {
                        Log.d(TAG, "readDataTotal is nothing" + readDataTotal);
                        return;
                    }
                    // RTU ?????? ????????? ????????? ??????($ ??? * ??? ????????????)
                    if (readDataTotal.contains("[ ConfMsg]") && readDataTotal.contains(">")) {

                        int configIndex = 0;
                        int lfIndex = 0;

                        configIndex = readDataTotal.indexOf("[ ConfMsg]");
                        lfIndex = readDataTotal.indexOf(">", configIndex);
                        while (configIndex < lfIndex) {

                            // \n ?????? ??????
                            String readData = readDataTotal.substring(configIndex, lfIndex + 1);

                            Log.d(TAG, "RTU ????????? ?????? ???????????? : " + readData);

                            // Log ?????? ??????
                            String readDataLog = readDataTotal.substring(configIndex, lfIndex);

                            Log.d(TAG, "RTU ????????? : " + readDataLog);

                            try {
                                readDataTotal = readDataTotal.substring(lfIndex + 1);
                            } catch (Exception e) {
                                logData_Ble("readData Error! - TotalReadData : " + readDataTotal, "error");
                                readDataTotal = "";
                                Log.e(TAG, "fragment_BLE_RTU_Status readData Error : " + e.toString());
                            }

                            logData_Ble(readDataLog, "read");

                            // ????????? ??????????????? ?????? ?????????????????? ?????????(???????????? ??????)
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

                            // subString ???????????? ?????? ???????????? ?????? ?????? while ?????????????????? ????????? ??????.
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

                            // \n ?????? ??????
                            String readData = readDataTotal.substring(ModemMsgIndex, lfIndex + 1);

                            Log.d(TAG, "????????? ?????? ???????????? : " + readData);

                            // Log ?????? ??????
                            String readDataLog = readDataTotal.substring(ModemMsgIndex, lfIndex);

                            Log.d(TAG, "????????? : " + readDataLog);

                            try {
                                readDataTotal = readDataTotal.substring(lfIndex + 1);
                            } catch (Exception e) {
                                logData_Ble("readData Error! - TotalReadData : " + readDataTotal, "error");
                                readDataTotal = "";
                                Log.e(TAG, "fragment_BLE_RTU_Status readData Error : " + e.toString());
                            }

                            logData_Ble(readDataLog, "read");

                            // ????????? ??????????????? ?????? ?????????????????? ?????????(???????????? ??????)
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

                            // subString ???????????? ?????? ???????????? ?????? ?????? while ?????????????????? ????????? ??????.
                            ModemMsgIndex = readDataTotal.indexOf("[ModemMsg]");
                            lfIndex = readDataTotal.indexOf(">", ModemMsgIndex);
                            if (ModemMsgIndex < 0 | lfIndex < 0) {
                                break;
                            }

                        }
                    }

                    // ??? ??? ??????
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

                        // $ ??? * ?????? ???????????????.(????????? ???)
                        if (readDataTotal.indexOf(StringList.DATA_SIGN_START) > -1
                                && readDataTotal.indexOf(StringList.DATA_SIGN_CHECKSUM) > -1) {

                            int count = 0;
                            for (int i = 0; i < readDataTotal.length(); i++) {
                                if (readDataTotal.charAt(i) == '$')
                                    count++;
                            }

                            // ?????? $ ???????????? 2???????????????
                            if (count > 1) {
                                // 2?????? $ ????????? ????????? ?????????
                                String organizeData = readDataTotal.substring(0, readDataTotal.indexOf("$", readDataTotal.indexOf("$") + 1));

                                // * ???????????????
                                if (organizeData.contains("*")) {
                                    try {
                                        // * ??? ???????????? ????????? ?????? ????????? ?????????
                                        if (readDataTotal.indexOf("*") + 3 == readDataTotal.indexOf("$", readDataTotal.indexOf("$") + 1)) {

                                        } else {
                                            readDataTotal = readDataTotal.substring(readDataTotal.indexOf("$", readDataTotal.indexOf("$") + 1));
                                        }
                                    } catch (Exception e) {
                                        // * ?????? ????????? ?????? ????????? $ ??? ??????????????? ????????? ?????? ?????????????????? ?????? ???????????? ?????????
                                        Log.d(TAG, "organizeData * check error : " + e.toString());
                                        readDataTotal = readDataTotal.substring(readDataTotal.indexOf("$", readDataTotal.indexOf("$") + 1));
                                    }
                                } else {
                                    // * ??? ????????? ?????? ????????? ?????????????????? ?????? ????????? ?????????
                                    readDataTotal = readDataTotal.substring(readDataTotal.indexOf("$", readDataTotal.indexOf("$") + 1));
                                }
                            }


                            // $??? *?????? ?????? ???????????????(????????? ?????? ????????????)
                            if (readDataTotal.indexOf(StringList.DATA_SIGN_START) < readDataTotal.indexOf(StringList.DATA_SIGN_CHECKSUM)) {
                                try {
                                    if (readDataTotal.substring(readDataTotal.indexOf(StringList.DATA_SIGN_CHECKSUM) + 1, readDataTotal.indexOf(StringList.DATA_SIGN_CHECKSUM) + 3).length() == 2) {
                                        //????????? ????????? ??? ?????? ??????

                                        String data = readDataTotal.substring(readDataTotal.indexOf(StringList.DATA_SIGN_START), readDataTotal.indexOf(StringList.DATA_SIGN_CHECKSUM) + 3);
                                        Log.d(TAG, "????????? Data : " + data);
                                        String[] dataArr = data.split(StringList.DATA_SIGN_COMMA);
                                        // ?????? ????????? ?????????(~~~~*AH)??? ??????????????? ????????? ????????????(????????? ??? ??????)
                                        String csData = dataArr[dataArr.length - 1].substring(dataArr[dataArr.length - 1].indexOf(StringList.DATA_SIGN_CHECKSUM) + 1);

                                        // ???????????? ?????????
                                        if (csData.equals(ToCheckSum(data.substring(data.indexOf(StringList.DATA_SIGN_START), data.indexOf(StringList.DATA_SIGN_CHECKSUM) + 1)))) {
                                            Log.d(TAG, "CheckSum OK!");
                                            currentFragment = getSupportFragmentManager().findFragmentById(R.id.bluetoothFragmentSpace);

                                            logData_Ble(data, "read");


                                            if (data.substring(1, 6).contains(DATA_TYPE_LISTS)) {
                                                if (data.startsWith(DATA_TYPE_S, 7)) {
                                                    if (data.substring(9, 12).contains(DATA_TYPE_BTV)) {
                                                        Log.d(TAG, "readData BTV ?????????");
                                                        // ????????????????????? ???????????? ????????????.
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
                                                            Log.d(TAG, "tv_ble_status_bat_value ?????? ??? ?????? ?????? : " + e);
                                                        }

                                                        try {

                                                            bleViewModel.setBleBattery_iv_1(data_arr[4]);
                                                            bleViewModel.setBleBattery_iv_2(data_arr[5]);
                                                            bleViewModel.setBleBattery_iv_3(data_arr[6]);
                                                            bleViewModel.setBleBattery_iv_4(data_arr[7]);
                                                            bleViewModel.setBleBattery_iv_5(data_arr[8]);
                                                            bleViewModel.setBleBattery_iv_6(tv_bat_value6_value);

                                                            /*// ????????? ?????? ?????? ????????? ??????
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
                                                            Log.d(TAG, "iv_ble_status_bat ?????? ??? ?????? ?????? : " + e);
                                                        }

                                                    } else if (data.substring(9, 12).contains("SLV")) {
                                                        Log.d(TAG, "readData SLV ?????????");
                                                        // ????????????????????? ???????????? ????????????.
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
                                                            Log.d(TAG, "tv_ble_status_bat_value ?????? ??? ?????? ?????? : " + e);
                                                        }

                                                        try {


                                                            bleViewModel.setBleSolarV_iv_1(data_arr[4]);
                                                            bleViewModel.setBleSolarV_iv_2(data_arr[5]);
                                                            bleViewModel.setBleSolarV_iv_3(data_arr[6]);
                                                            bleViewModel.setBleSolarV_iv_4(data_arr[7]);
                                                            bleViewModel.setBleSolarV_iv_5(data_arr[8]);
                                                            bleViewModel.setBleSolarV_iv_6(tv_sol_value6_value);

                                                            /*// ????????? ?????? ?????? ????????? ??????
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
                                                            Log.d(TAG, "iv_ble_status_bat ?????? ??? ?????? ?????? : " + e);
                                                        }

                                                    } else if (data.substring(9, 12).contains("SLC")) {
                                                        Log.d(TAG, "readData SLC ?????????");
                                                        // ????????????????????? ???????????? ????????????.
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
                                                            Log.d(TAG, "tv_ble_status_bat_value ?????? ??? ?????? ?????? : " + e);
                                                        }
                                                    }

                                                } else {
                                                    Log.d(TAG, "readData ????????? ??????");
                                                    String[] data_arr = data.split(",");
                                                    Log.d(TAG, "data_arr length" + data_arr.length);


                                                    bleViewModel.setBleID(data_arr[1]);
                                                    bleViewModel.setBleInputV(data_arr[2] + "V");
                                                    bleViewModel.setBleOutputA(data_arr[3] + "A");
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
                                                    bleViewModel.setBleChargingA(data_arr[10] + "A");
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

                                                    // gmt ?????? ?????? 9?????? ???????????????. 15??? ????????? ???????????? ???????????? ??????
                                                    if (Integer.parseInt(data_arr[13].substring(0, 2)) >= 15) {
                                                        // 15????????? ??? ??????
                                                        receiveTime_hour = (Integer.parseInt(data_arr[13].substring(0, 2)) - 15) + "";
                                                        // 1?????? ??? ????????? ??????
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

                                                    //GPS ?????? ??????
                                                    String[] tempLatitude;
                                                    String lat1, lat2, lat3;
                                                    String[] tempLongitude;
                                                    String lon1, lon2, lon3;

                                                    //??????
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

                                                        bleViewModel.setBleGPSLatitude(lat1 + "?? " + lat2 + "\' " + lat3 + "\"");
                                                    } else {
                                                        bleViewModel.setBleGPSLatitude(data_arr[14].substring(0, 2) + "?? " + data_arr[14].substring(2, 4) + "\' " + data_arr[14].substring(5, 7) + "." + data_arr[14].substring(7) + "\"");
                                                    }

                                                    String longitude = data_arr[15];
                                                    if (longitude.contains("*")) {
                                                        longitude = longitude.substring(0, longitude.indexOf("*"));
                                                    }

                                                    //??????
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

                                                        bleViewModel.setBleGPSLongitude(lon1 + "?? " + lon2 + "\' " + lon3 + "\"");
                                                    } else {
                                                        bleViewModel.setBleGPSLongitude(longitude.substring(0, 3) + "?? " + longitude.substring(3, 5) + "\' " + longitude.substring(6, 8) + "." + longitude.substring(8, 10) + "\"");
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

                                                    // ????????? ?????? ???
                                                    bleViewModel.setBleFirmVer(data_arr[1]);

                                                    // RTU ????????? ??????
                                                    if (Integer.parseInt(data_arr[2]) == 0) {
                                                        bleViewModel.setBleRTUVer(getString(R.string.Lantern));
                                                    } else if (Integer.parseInt(data_arr[2]) == 1) {
                                                        bleViewModel.setBleRTUVer(getString(R.string.RTU));
                                                    }

                                                    // GPS ??????
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
                                                    //viewModel.setDataString(temperature + "??C");
                                                    bleViewModel.setBleTemperature(temperature + "??C");

                                                } catch (Exception e) {

                                                }


                                            }


                                            // ????????? ??????????????? ?????? ?????????????????? ?????????(???????????? ??????)
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


                                            // ????????? ??? ????????? ??? ?????????
                                            try {
                                                readDataTotal = readDataTotal.substring(readDataTotal.indexOf(StringList.DATA_SIGN_CHECKSUM) + 3);
                                                // ??? ?????? ???????????? ???????????? ????????? ?????? ????????? ??? ????????? ???.
                                                if (readDataTotal.indexOf(StringList.DATA_SIGN_START) > -1) {
                                                    Log.d(TAG, "readDataTotal remain : " + readDataTotal);
                                                } else {
                                                    Log.d(TAG, "readDataTotal no contain $ : " + readDataTotal);
                                                    // ?????? ???.????????? ????????? ?????????
                                                    readDataTotal = "";
                                                    return;
                                                }
                                            } catch (Exception e) {
                                                Log.e(TAG, "readDataTotal remain error : " + readDataTotal);
                                                readDataTotal = "";
                                                return;
                                            }

                                        } else {
                                            // ????????? ?????? ????????????
                                            // ?????? $ ???????????? 2???????????????
                                            if (count > 1) {
                                                // ?????? ????????? ??????
                                                readDataTotal = readDataTotal.substring(readDataTotal.indexOf("$", readDataTotal.indexOf("$") + 1));
                                            } else {
                                                // ?????? ??????(?????? ???????????? ?????? ??????) ??? ??????
                                                readDataTotal = "";
                                                whileExit = false;
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.d(TAG, "checksum ?????? ??? ?????? : " + e.toString());
                                    return;
                                }


                            } else {
                                // ???????????? ?????? ?????????.
                                logData_Ble("readData Error - order : " + readDataTotal, "error");

                                Log.d(TAG, "readData Error - order" + readDataTotal);
                                readDataTotal = readDataTotal.substring(readDataTotal.indexOf("$"));
                                Log.d(TAG, "readData Error - order - change" + readDataTotal);
                            }

                        } else {
                            return;
                        }
                    }

                    // ???????????? ?????? ??????(?????? ?????????)
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

            // ????????? ??????????????? ??????
            if (cmdCharacteristic == null) {
                logData_Ble("Unable to find cmd characteristic", "error");
                Log.e(TAG, "Unable to find cmd characteristic(writeData)");
                disconnectGattServer("BleMainActivity - BlewriteData - Unable to find cmd characteristic");
                return;
            }

            // ???????????? ?????? ?????? ?????????????????? ????????? ?????? ?????? ??? ?????????.
            readDataOverlapCheck = "";

            String sendBlewriteData = data + ToCheckSum(data);
            logData_Ble(sendBlewriteData, "write");
            sendBlewriteData = sendBlewriteData + StringList.DATA_SIGN_CR + StringList.DATA_SIGN_LF;
            Log.d(TAG, "BleWriteData data : " + data + ", sendData : " + sendBlewriteData);

            // ???????????? ?????? ????????? ?????? ??? ?????? ?????? ???????????? ????????? ????????????.(5.0??? ???????????????...)
            if (sendBlewriteData.length() < 21) {
                cmdCharacteristic.setValue(sendBlewriteData.getBytes());
                //cmdCharacteristic.setWriteType(1);

                Boolean success = bleGatt.writeCharacteristic(cmdCharacteristic);
                if (!success) {
                    logData_Ble("Failed to write command", "error");
                    Log.e(TAG, "Failed to write command");
                } else {
                    Log.e(TAG, "Success to write command");
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

                    // ?????? write??? ????????? ?????? ??????.
                    Boolean success = bleGatt.writeCharacteristic(cmdCharacteristic);
                    if (!success) {
                        logData_Ble("Failed to write command", "error");
                        Log.e(TAG, "Failed to write command - 2");
                    } else {
                        Log.e(TAG, "Success to write command - 2");
                    }

                    // 0.1 ??? ?????? ?????? ?????? ????????? ????????? ????????? ?????????
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

        // ????????? ??????????????? ??????
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
        //????????????($)??? ????????????
        //??? ??????(*)??? ????????????
        //??????????????? ??? ???????????? ????????? ??????????????????
        if (str.contains(StringList.DATA_SIGN_START)
                && str.indexOf(StringList.DATA_SIGN_CHECKSUM) > -1
                && str.indexOf(StringList.DATA_SIGN_START) < str.indexOf(StringList.DATA_SIGN_CHECKSUM)) {
            for (int n = 1; n < str.indexOf(StringList.DATA_SIGN_CHECKSUM); n++) {
                csInt ^= str.charAt(n);
            }

            csStr = Integer.toString(csInt, 16).toUpperCase();
            if (csStr.length() == 1) {
                //????????? ??? ?????????, ?????? 0??? ??????
                csStr = "0" + csStr;
            }
            Log.d(TAG, "toCheckSum csInt : " + csStr);
            return csStr;
        } else {
            //???????????? ?????? ??????
            return "HH";
        }

    }


    // ??????????????? ????????? ???, ?????? or ????????? ???, ?????? ?????? ?????????.
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

    // ???????????? ?????? or ?????? ?????? ?????? ???
    /*private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

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
    };*/

    // ???????????? ?????? ??? or ?????? ???
    private final BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    Log.d(TAG, "Bluetooth.ACTION_ACL_CONNECTED");
                    // ?????? ???
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    Log.d(TAG, "Bluetooth.ACTION_ACL_DISCONNECTED");
                    // ?????? ???
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

        // ???????????? ???????????? ?????? ???????????? ????????? ?????? ??????????????????
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

            builder.setTitle("Notice").setMessage("?????????????????? ?????????????????????????\nDo you want to return to the initial screen?");

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

        //?????? ?????? ?????? ?????? ?????? ( ?????? ?????? ????????? ?????? ?????? ?????? )
        private static final long MIN_CLICK_INTERVAL = 600;
        private long mLastClickTime = 0;

        public abstract void onSingleClick(View v);

        @Override
        public final void onClick(View v) {
            long currentClickTime = SystemClock.uptimeMillis();
            long elapsedTime = currentClickTime - mLastClickTime;
            mLastClickTime = currentClickTime;

            // ???????????? ?????? ??????
            if (elapsedTime > MIN_CLICK_INTERVAL) {
                onSingleClick(v);
            }
        }
    }

}