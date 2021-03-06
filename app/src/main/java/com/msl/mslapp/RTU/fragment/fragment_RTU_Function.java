package com.msl.mslapp.RTU.fragment;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.msl.mslapp.BuildConfig;
import com.msl.mslapp.R;
import com.msl.mslapp.RTUMainActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.Executors;

import static com.msl.mslapp.BleMainActivity.navigation_icon_Change;
import static com.msl.mslapp.BleMainActivity.tLanguage;
import static com.msl.mslapp.RTUMainActivity.logData_RTU;
import static com.msl.mslapp.RTUMainActivity.mRTUContext;
import static com.msl.mslapp.RTUMainActivity.mRTUMain;

public class fragment_RTU_Function extends Fragment implements SerialInputOutputManager.Listener {

    // 로그 이름 용
    public static final String TAG = "Msl-RTU-Function";

    View view;
    // bluetooth 탭 (기능)
    TabLayout tabLayout_rtu;
    // Viewpager
    private ViewPager2 viewPager_rtu;
    adapter_RTU_Tab adapter_RTU_tab;

    // 자식 프래그먼트
    fragment_RTU_Status fragment_RTU_status;
    fragment_RTU_Setting fragment_RTU_setting;
    fragment_RTU_Lantern fragment_RTU_lantern;

    // 하위 프래그먼트에게 데이터 주는 용도
    FragmentManager fragmentManager;

    // Tablayout title 이름
    String[] tavTitle;

    // connect 용
    private enum UsbPermission {Unknown, Requested, Granted, Denied}

    private static final String INTENT_ACTION_GRANT_USB = BuildConfig.APPLICATION_ID + ".GRANT_USB";
    private static final int WRITE_WAIT_MILLIS = 100;
    private static final int READ_WAIT_MILLIS = 200;
    private final int MESSAGE_HANDLER_START = 100;
    private final int MESSAGE_HANDLER_WORK = 101;
    private final int MESSAGE_HANDLER_STOP = 102;

    private int deviceId, portNum, baudRate;

    private final BroadcastReceiver broadcastReceiver;
    private final Handler mainLooper;

    public static UsbSerialPort usbSerialPort;
    public static UsbPermission usbPermission = UsbPermission.Unknown;
    public static boolean connected = false;

    private SerialInputOutputManager serialInputOutputManager;


    // 데이터 요청 및 연결상태
    public static Toast toastStatusCall = Toast.makeText(mRTUMain, "Status Call",Toast.LENGTH_SHORT);
    public static Toast toastDataReceive = Toast.makeText(mRTUMain, "Data Receive Success!", Toast.LENGTH_SHORT);
    Toast toastConnect = Toast.makeText(mRTUMain, "Connect",Toast.LENGTH_SHORT);
    Toast toastDisConnect = Toast.makeText(mRTUMain, "Disconnect",Toast.LENGTH_SHORT);
    public static Toast toastNotConnect = Toast.makeText(mRTUMain, "Not Connect",Toast.LENGTH_SHORT);

    public static void setToastStatusCall(){
        toastDataReceive.cancel();
        toastStatusCall.cancel();
        toastStatusCall.show();
    }
    public static void setToastDataReceive(){
        toastStatusCall.cancel();
        toastDataReceive.cancel();
        toastDataReceive.show();
    }

    public fragment_RTU_Function() {
        // 브로드 캐스트 설정(USB 연결 확인)
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "fragment_RTU_Function broadcastReceiver - onReceive");
                Toast.makeText(mRTUMain, "attach",Toast.LENGTH_SHORT).show();
                if (INTENT_ACTION_GRANT_USB.equals(intent.getAction())) {
                    usbPermission = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                            ? UsbPermission.Granted : UsbPermission.Denied;
                    //connect();
                }
            }
        };
        mainLooper = new Handler(Looper.getMainLooper());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "fragment_RTU_Function onCreateView");
        view = inflater.inflate(R.layout.rtu_fragment_function, null);

        fragmentManager = this.getChildFragmentManager();

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tabLayout_rtu = view.findViewById(R.id.tab_rtu);

        viewPager_rtu = view.findViewById(R.id.rtu_Viewpage_Space);

        tavTitle = new String[]{"Status", "Setting", "Lantern"};

        Log.d(TAG, "Viewpage 작업");
        adapter_RTU_tab = new adapter_RTU_Tab(this, 2);
        viewPager_rtu.setAdapter(adapter_RTU_tab);
        viewPager_rtu.setCurrentItem(0);
        viewPager_rtu.setOffscreenPageLimit(1
        );
        viewPager_rtu.setPageTransformer(new ZoomOutPageTransformer());

        new TabLayoutMediator(tabLayout_rtu, viewPager_rtu,
                (tab, position) -> tab.setText(tavTitle[position])
        ).attach();
    }


    ////////////////////////////////////////////////////////////
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 전 프래그먼트에서 보낸 데이터 처리

        try {
            deviceId = getArguments().getInt("device");
            portNum = getArguments().getInt("port");
            baudRate = getArguments().getInt("baud");
        } catch (Exception e) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "fragment_RTU_Function - onDestroy start");
        Log.d(TAG, "fragment_RTU_Function - onDestroy leave");
    }

    @Override
    public void onStart() {
        Log.d(TAG, "fragment_RTU_Function - onStart start");
        Log.d(TAG, "fragment_RTU_Function - onStart leave");
        super.onStart();
    }

    @Override
    public void onStop() {

        Log.d(TAG, "fragment_RTU_Function - onStop start");

        try {

            if (usbSerialPort != null) {
                Log.d(TAG, "fragment_RTU_Function - onPause - usbSerialPort not null");
                disconnect();
                mRTUMain.unregisterReceiver(broadcastReceiver);
            } else {
                Log.d(TAG, "fragment_RTU_Function - onPause - usbSerialPort null");
            }


            /*if (usbSerialPort.isOpen()) {
                Log.d(TAG, "fragment_RTU_Function - onPause Connected");
                disconnect();
                mRTUMain.unregisterReceiver(broadcastReceiver);
            }else{
                Log.d(TAG, "fragment_RTU_Function - onPause notConnected");
            }*/
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "fragment_RTU_Function - onPause - " + e);
            //logData_RTU(e.toString(), "error");
        }

        Log.d(TAG, "fragment_RTU_Function - onStop leave");
        super.onStop();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "fragment_RTU_Function - onResume start");
        super.onResume();
/*
        if(!usbSerialPort.isOpen()){
            UsbManager usbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
            UsbSerialProber usbDefaultProber = UsbSerialProber.getDefaultProber();
            UsbSerialProber usbCustomProber = CustomProber.getCustomProber();

            for (UsbDevice device : usbManager.getDeviceList().values()) {
                UsbSerialDriver driver = usbDefaultProber.probeDevice(device);
                if (driver == null) {
                    driver = usbCustomProber.probeDevice(device);
                }
                if (driver != null) {
                    if (driver.getPorts().size() == 1) {

                        UsbSerialDriver finalDriver = driver;

                        fragment_RTU_Scan.ListItem item = new fragment_RTU_Scan.ListItem(device, 0, finalDriver);

                        deviceId = item.device.getDeviceId();
                        portNum = item.port;
                        baudRate = 115200;

                        Log.d(TAG, "onResume - driver.getPorts().size() == 1");
                        mainLooper.post(this::connect);
                    }
                }
            }
        }else{
            mRTUMain.registerReceiver(broadcastReceiver, new IntentFilter(INTENT_ACTION_GRANT_USB));

            if (usbPermission == UsbPermission.Unknown || usbPermission == UsbPermission.Granted) {
                Log.d(TAG, "onResume - usbPermission == UsbPermission.Unknown || usbPermission == UsbPermission.Granted");
                mainLooper.post(this::connect);
            }
        }*/
        mainLooper.post(this::connect);

        Log.d(TAG, "fragment_RTU_Function - onResume leave");
    }

    @Override
    public void onPause() {
        Log.d(TAG, "fragment_RTU_Function - onPause start");
        try {

            if (usbSerialPort != null) {
                Log.d(TAG, "fragment_RTU_Function - onPause - usbSerialPort not null");
                disconnect();
                mRTUMain.unregisterReceiver(broadcastReceiver);
            } else {
                Log.d(TAG, "fragment_RTU_Function - onPause - usbSerialPort null");
            }


            /*if (usbSerialPort.isOpen()) {
                Log.d(TAG, "fragment_RTU_Function - onPause Connected");
                disconnect();
                mRTUMain.unregisterReceiver(broadcastReceiver);
            }else{
                Log.d(TAG, "fragment_RTU_Function - onPause notConnected");
            }*/
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "fragment_RTU_Function - onPause - " + e);
            //logData_RTU(e.toString(), "error");
        }
        Log.d(TAG, "fragment_RTU_Function - onPause leave");
        super.onPause();
    }

    /*
     * Serial
     */

    // 시리얼을 통해 들어온 데이터 처리
    @Override
    public void onNewData(byte[] data) {
        mainLooper.post(() -> {
            Log.d(TAG, "onNewData - mainLooper.post");
            receive(data);
        });
    }

    // 에러날 시 작동
    @Override
    public void onRunError(Exception e) {
        mainLooper.post(() -> {
            status("connection lost: " + e.getMessage());
            if (usbSerialPort != null) {
                disconnect();
            }
        });
    }

    /*
     * Serial + UI
     */

    // 시리얼 연결
    private void connect() {
        status("connect start");

        // usbSerialPort 연결 상태 확인할 때 값이 없으면 에러 뜨는 등의 확인하기 위해 연결 요청마다 확인.
        UsbManager usbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        UsbSerialProber usbDefaultProber = UsbSerialProber.getDefaultProber();
        UsbSerialProber usbCustomProber = CustomProber.getCustomProber();

        for (UsbDevice device : usbManager.getDeviceList().values()) {
            UsbSerialDriver driver = usbDefaultProber.probeDevice(device);
            if (driver == null) {
                driver = usbCustomProber.probeDevice(device);
            }
            if (driver != null) {
                if (driver.getPorts().size() == 1) {

                    UsbSerialDriver finalDriver = driver;

                    fragment_RTU_Scan.ListItem item = new fragment_RTU_Scan.ListItem(device, 0, finalDriver);

                    deviceId = item.device.getDeviceId();
                    portNum = item.port;
                    baudRate = 115200;

                    Log.d(TAG, "driver.getPorts().size() == 1");
                }
            }
        }


        UsbDevice device = null;
        //UsbManager usbManager = (UsbManager) mRTUMain.getSystemService(Context.USB_SERVICE);
        for (UsbDevice v : usbManager.getDeviceList().values())
            if (v.getDeviceId() == deviceId)
                device = v;
        if (device == null) {
            status("connection failed: device not found");
            return;
        }
        UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(device);
        if (driver == null) {
            driver = CustomProber.getCustomProber().probeDevice(device);
        }
        if (driver == null) {
            status("connection failed: no driver for device");
            return;
        }
        if (driver.getPorts().size() < portNum) {
            status("connection failed: not enough ports at device");
            return;
        }

        // usb에 연결 된 첫번째 단자와 연결(2개 이상 연결을 잘 안하고 선택창 만들었는데 사람들이 헷갈려하거나 굳이 눌러야하는 등의 상황으로 인해 자동으로 선택)
        usbSerialPort = driver.getPorts().get(0);
        PendingIntent permissionIntent = PendingIntent.getBroadcast(mRTUContext, 0, new Intent(INTENT_ACTION_GRANT_USB), 0);
        usbManager.requestPermission(device, permissionIntent);

        UsbDeviceConnection usbConnection = usbManager.openDevice(driver.getDevice());
        if (usbConnection == null && usbPermission == UsbPermission.Unknown && !usbManager.hasPermission(driver.getDevice())) {
            usbPermission = UsbPermission.Requested;
            PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(mRTUMain, 0, new Intent(INTENT_ACTION_GRANT_USB), 0);
            usbManager.requestPermission(driver.getDevice(), usbPermissionIntent);
            return;
        }
        if (usbConnection == null) {
            if (!usbManager.hasPermission(driver.getDevice()))
                status("connection failed: permission denied");
            else
                status("connection failed: open failed");
            return;
        }


        // usb 연결이 안되어 있을 시
        if (!usbSerialPortOpenCheck()) {
            try {
                usbSerialPort.open(usbConnection);
                // 시리얼에 해당하는 수치 값 설정
                usbSerialPort.setParameters(baudRate, 8, 1, UsbSerialPort.PARITY_NONE);
                // 들어오는 시리얼 데이터를 관리하는
                serialInputOutputManager = new SerialInputOutputManager(usbSerialPort, this);
                Executors.newSingleThreadExecutor().submit(serialInputOutputManager);
                status("connecting");
                connected = true;
                // 연결되면 자동으로 데이터 요청을 하는데 연결 오류가 나서 연결-연결끊김이 오당 수회가 일어나면 에러가 발생하여 주석 처리 => 해결된거 같아 다시 실행
                send("$MUCMD,8,1*11\r\n");
                toastConnect.cancel();
                toastConnect.show();
            } catch (Exception e) {
                status("connection failed: " + e.getMessage());
                disconnect();
            }
        } else {
            status("connect - connected");
        }
        status("connect end");

    }

    // 연결 끊기
    public void disconnect() {
        toastDisConnect.cancel();
        toastDisConnect.show();
        status("disconnect");
        connected = false;
        if (usbSerialPort == null) {
            status("disconnect usbSerialPort null");
            Log.d(TAG, "disconnect - disconnect usbSerialPort null");
            return;
        }
        try {
            usbSerialPort.close();
        } catch (Exception e) {
            status("disconnect usbSerialPort close error : " + e.toString());
        }
        usbSerialPort = null;

    }

    // 데이터 보내는 부분
    public static void send(String str) {
        if (usbSerialPort != null) {
            if (!usbSerialPort.isOpen()) {
                Log.d(TAG, "send : not connected");
                toastNotConnect.cancel();
                toastNotConnect.show();
                return;
            }
            try {
                byte[] data = (str).getBytes();
                usbSerialPort.write(data, WRITE_WAIT_MILLIS);
                Log.d(TAG, "send : " + new String(data));
                logData_RTU(new String(data), "write");
            } catch (Exception e) {
                Log.d(TAG, "send error : " + e);
            }
        } else {
            Log.d(TAG, "send : usbSerialPort null");
        }


    }

    public boolean usbSerialPortOpenCheck() {
        UsbManager usbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        UsbDevice device = null;
        //UsbManager usbManager = (UsbManager) mRTUMain.getSystemService(Context.USB_SERVICE);
        for (UsbDevice v : usbManager.getDeviceList().values())
            if (v.getDeviceId() == deviceId)
                device = v;
        if (device == null) {
            status("connection failed: device not found");
            return false;
        }
        UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(device);
        if (driver == null) {
            driver = CustomProber.getCustomProber().probeDevice(device);
        }
        if (driver == null) {
            status("connection failed: no driver for device");
            return false;
        }
        if (driver.getPorts().size() < portNum) {
            status("connection failed: not enough ports at device");
            return false;
        }

        if (usbSerialPort == null)
            return false;

        return usbSerialPort.isOpen();

    }


    // 수동으로 읽어들이기(비추천) syc 써야하나
    /*public void read() {
        status("read ");
        if (!usbSerialPort.isOpen()) {
            Toast.makeText(mRTUMain, "not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            byte[] buffer = new byte[8192];
            // READ_WAIT_MILLIS 만큼 핸드폰이 멈춤(내가 실력이 딸려서 몰것음)
            int len = usbSerialPort.read(buffer, READ_WAIT_MILLIS);
            if (len == 0) {
                return;
            } else {
                status("len : " + len);
            }
            receive(Arrays.copyOf(buffer, len));

            String readData = new String(Arrays.copyOf(buffer, len));

            if (readData.length() == 0) {
                return;
            }
            switch (viewPager_rtu.getCurrentItem()) {
                case 0:
                    Log.d(TAG, "currentPage : Status");
                    fragment_RTU_status = (fragment_RTU_Status) fragmentManager.findFragmentByTag("f" + viewPager_rtu.getCurrentItem());
                    try {
                        fragment_RTU_status.readData(readData);
                    } catch (Exception e) {
                        Log.e(TAG, "fragment_RTU_Status readData Error - Status : " + e.toString());
                    }
                    break;
                case 1:
                    Log.d(TAG, "currentPage : Setting");
                    fragment_RTU_setting = (fragment_RTU_Setting) fragmentManager.findFragmentByTag("f" + viewPager_rtu.getCurrentItem());
                    try {
                        fragment_RTU_setting.readData(readData);
                    } catch (Exception e) {
                        Log.e(TAG, "fragment_RTU_Setting readData Error - Setting : " + e.toString());
                    }
                    break;
                case 2:
                    Log.d(TAG, "currentPage : lantern");
                    fragment_RTU_lantern = (fragment_RTU_Lantern) fragmentManager.findFragmentByTag("f" + viewPager_rtu.getCurrentItem());
                    try {
                        fragment_RTU_lantern.readData(readData);
                    } catch (Exception e) {
                        Log.e(TAG, "fragment_RTU_Lantern readData Error - Lantern : " + e.toString());
                    }
                    break;
            }


        } catch (IOException e) {
            status("connection lost2: " + e.getMessage());
            disconnect();
        }
    }*/

    // 자동으로 읽기 사용할 때 사용함(근데 핸드폰 멈춤 엌ㅋ)
    /*Handler cycleHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MESSAGE_HANDLER_START:
                    read();
                    Log.d(TAG, "handler check MESSAGE_HANDLER_START");
                    this.removeMessages(MESSAGE_HANDLER_WORK);
                    cycleHandler.sendEmptyMessageDelayed(MESSAGE_HANDLER_WORK, 300);
                    break;
                case MESSAGE_HANDLER_WORK:
                    read();
                    Log.d(TAG, "handler check MESSAGE_HANDLER_WORK");
                    cycleHandler.sendEmptyMessageDelayed(MESSAGE_HANDLER_WORK, 300);
                    break;
                case MESSAGE_HANDLER_STOP:
                    this.removeMessages(MESSAGE_HANDLER_WORK);
                    break;
            }
        }
    };*/


    // 들어온 데이터 처리하는 부분
    public void receive(byte[] data) {
        String receiveData = new String(data);
        if (receiveData.length() == 0) {
            return;
        }
        switch (viewPager_rtu.getCurrentItem()) {
            case 0:
                Log.d(TAG, "currentPage : Status");
                fragment_RTU_status = (fragment_RTU_Status) fragmentManager.findFragmentByTag("f" + viewPager_rtu.getCurrentItem());
                try {
                    fragment_RTU_status.readData(receiveData);
                } catch (Exception e) {
                    Log.e(TAG, "fragment_RTU_Status readData Error - Status : " + e.toString());
                }
                break;
            case 1:
                Log.d(TAG, "currentPage : Setting");
                fragment_RTU_setting = (fragment_RTU_Setting) fragmentManager.findFragmentByTag("f" + viewPager_rtu.getCurrentItem());
                try {
                    fragment_RTU_setting.readData(receiveData);
                } catch (Exception e) {
                    Log.e(TAG, "fragment_RTU_Setting readData Error - Setting : " + e.toString());
                }
                break;
            case 2:
                Log.d(TAG, "currentPage : lantern");
                fragment_RTU_lantern = (fragment_RTU_Lantern) fragmentManager.findFragmentByTag("f" + viewPager_rtu.getCurrentItem());
                try {
                    fragment_RTU_lantern.readData(receiveData);
                } catch (Exception e) {
                    Log.e(TAG, "fragment_RTU_Lantern readData Error - Lantern : " + e.toString());
                }
                break;
        }
    }


    public static void status(String str) {
        Log.d(TAG, "status : " + str);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    public class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }

    // 언어 변경
    public static void setLocale(String char_select) {
        switch (char_select) {
            case "ko":
                break;
            case "en":
                break;
            default:
                char_select = "en";
                break;
        }

        Locale locale = new Locale(char_select);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        mRTUContext.getResources().updateConfiguration(config, mRTUContext.getResources().getDisplayMetrics());
        tLanguage = char_select; //설정된 언어 저장 변수에 저장
    }

    public static void rtu_Beginning_reset() {
        //Intent intent = mRTUContext.getPackageManager().getLaunchIntentForPackage(mRTUContext.getPackageName());
        Intent intent = new Intent(mRTUContext, RTUMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mRTUMain.finish();
        mRTUMain.startActivity(intent);
    }
}
