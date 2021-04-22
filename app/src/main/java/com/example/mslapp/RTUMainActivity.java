package com.example.mslapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mslapp.RTU.fragment.adapter_RTU_Tab;
import com.example.mslapp.RTU.fragment.fragment_RTU_Setting;
import com.example.mslapp.RTU.fragment.fragment_RTU_Status;
import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class RTUMainActivity extends AppCompatActivity {


    // 로그 이름 용
    public static final String TAG = "Msl-RTU-MainAct";

    public Context mRTUContext = null;
    public static AppCompatActivity mRTUMain = null;

    // d2xx - RTU 통신 간 필요

    private static D2xxManager ftD2xx = null;
    private FT_Device ftDev;

    int BaudSpeed = 115200;
    byte dataBits = 8;
    byte stopBits = 1;
    byte parity = 0;
    byte flowControl = 0;

    static final int READBUF_SIZE  = 256;
    byte[] rbuf  = new byte[READBUF_SIZE];
    char[] rchar = new char[READBUF_SIZE];
    int mReadSize=0;

    boolean mThreadIsStopped = true;

    // rtu 탭 (기능)
    TabLayout tabLayout_rtu;
    // Viewpager
    public static ViewPager2 viewPager_rtu;
    adapter_RTU_Tab adapter_rtu_tab;

    fragment_RTU_Status fragment_rtu_status;
    fragment_RTU_Setting fragment_rtu_setting;

    FragmentManager fragmentManager_rtu;

    // Tablayout title 이름
    String[] tavTitle = new String[]{"Status", "Setting"};

    // data
    String sendData = "";
    String readData = "";

    //toolbar
    Toolbar toolbarMain;
    NavigationView navigationView;

    // drawLayout(bluetoothMainlayout)
    private DrawerLayout rtuMainLayout;
    ActionBarDrawerToggle drawerToggle;


    // String
    public static String DATA_START = "$"; //시작 기호
    public static String DATA_CHECKSUM = "*"; //끝 기호
    public static String DATA_COMMA = ","; //구분자
    public static String DATA_CR = "\r"; //<CR>
    public static String DATA_LF = "\n"; //<LF>



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        setContentView(R.layout.rtu_activity_main);

        mRTUContext = this;
        mRTUMain = this;

        // 툴바
        toolbarMain = (Toolbar) findViewById(R.id.rtu_toolbar_main);
        toolbarMain.setTitle("MSL RTU");
        toolbarMain.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbarMain);

        // 탭바
        tabLayout_rtu = findViewById(R.id.tab_RTU);

        viewPager_rtu = findViewById(R.id.RTU_ViewpageSpace);

        fragmentManager_rtu = getSupportFragmentManager();

        Log.d(TAG, "Viewpage 작업");
        adapter_rtu_tab = new adapter_RTU_Tab(this,2);
        viewPager_rtu.setAdapter(adapter_rtu_tab);
        viewPager_rtu.setCurrentItem(0);
        viewPager_rtu.setOffscreenPageLimit(1);
        viewPager_rtu.setPageTransformer(new ZoomOutPageTransformer());

        new TabLayoutMediator(tabLayout_rtu, viewPager_rtu,
                (tab, position) -> tab.setText(tavTitle[position])
        ).attach();


        // 사이드바 관련
        navigationView = findViewById(R.id.rtu_sidebar);
        navigationView.setItemIconTintList(null);

        rtuMainLayout = findViewById(R.id.rtu_drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, rtuMainLayout, toolbarMain, R.string.app_name, R.string.app_name);
        rtuMainLayout.addDrawerListener(drawerToggle); // 삼선 메뉴 사용 시 뱅그르르 돈다.
        drawerToggle.syncState(); // 메뉴 버튼 추가

        // 사이드바(네비게이션) 아이템 선택 시
        navigationView.setNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.menu_bluetooth){
                Intent intent = new Intent(mRTUContext, BleMainActivity.class);
                startActivity(intent);
                finish();
            }else if(item.getItemId() == R.id.menu_rtu){

            }
            // 사이드바 닫기
            rtuMainLayout.closeDrawer(navigationView);

            return false;
        });

        // RTU 연결
        try {
            ftD2xx = D2xxManager.getInstance(this);
        } catch (D2xxManager.D2xxException ex) {
            Log.e(TAG,ex.toString());
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mUsbReceiver, filter);


    }


    // 데이터 보내기
    public void sendData(String sendData) {
        if(ftDev == null) {
            Log.e(TAG, "ftDev is null");
            return;
        }

        synchronized (ftDev) {
            if(!ftDev.isOpen()) {
                Log.e(TAG, "onClickWrite : Device is not open");
                return;
            }

            ftDev.setLatencyTimer((byte)16);

            byte[] writeByte = sendData.getBytes();
            ftDev.write(writeByte, sendData.length());
            Log.d(TAG, "sendData : " + sendData);
        }
    }


    public void openDevice() {
        if(ftDev != null) {
            if(ftDev.isOpen()) {
                ftDev_setConfig();
                return;
            }
        }

        int devCount;
        devCount = ftD2xx.createDeviceInfoList(this);

        Log.d(TAG, "Device number : "+ devCount);

        D2xxManager.FtDeviceInfoListNode[] deviceList = new D2xxManager.FtDeviceInfoListNode[devCount];
        ftD2xx.getDeviceInfoList(devCount, deviceList);

        if(devCount <= 0) {
            return;
        }

        if(ftDev == null) {
            ftDev = ftD2xx.openByIndex(this, 0);
        } else {
            synchronized (ftDev) {
                ftDev = ftD2xx.openByIndex(this, 0);
            }
        }

        if(ftDev.isOpen()) {
            ftDev_setConfig();
        }else{
            Log.d(TAG, "openDevice - frDev.isOpen - false");
        }
    }

    public void ftDev_setConfig(){
        Log.d(TAG, "ftDev_setConfig start");
        if(mThreadIsStopped) {
            SetConfig(BaudSpeed, (byte)dataBits, (byte)stopBits, (byte)parity, (byte)flowControl);
            ftDev.purge((byte) (D2xxManager.FT_PURGE_TX | D2xxManager.FT_PURGE_RX));
            ftDev.restartInTask();
            new Thread(mLoop).start();
        }
        Log.d(TAG, "ftDev_setConfig end");
    }



    private Runnable mLoop = new Runnable() {
        @Override
        public void run() {
            int i;
            int readSize;
            mThreadIsStopped = false;
            while(true) {
                if(mThreadIsStopped) {
                    Log.d(TAG, "mLoop mThreadIsStopped : true");
                    break;
                }

/*                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
*/
                synchronized (ftDev) {
                    readSize = ftDev.getQueueStatus();
                    if(readSize>0) {
                        mReadSize = readSize;
                        if(mReadSize > READBUF_SIZE) {
                            mReadSize = READBUF_SIZE;
                        }
                        ftDev.read(rbuf,mReadSize);

                        // cannot use System.arraycopy
                        for(i=0; i<mReadSize; i++) {
                            rchar[i] = (char)rbuf[i];
                        }

                        String data = new String(rchar, 0, mReadSize);

                        Log.d(TAG, "readData : " + data);

                        readData(data);

                        // activity의 layout을 변경할려면 이걸 통해서
                        /*mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tvRead.append(String.copyValueOf(rchar,0,mReadSize));
                                tvRead2.setText(data);
                            }
                        });*/

                    } // end of if(readSize>0)
                } // end of synchronized
            }
        }
    };

    public void readData(String data){

        Log.d(TAG, "readData! : " + data);

        switch (viewPager_rtu.getCurrentItem()){
            case 0:
                Log.d(TAG, "currentPage : Status , viewPager_rtu.getCurrentItem() : " + viewPager_rtu.getCurrentItem());
                fragment_rtu_status = (fragment_RTU_Status) fragmentManager_rtu.findFragmentByTag("f"+viewPager_rtu.getCurrentItem());
                try {
                    Log.d(TAG, "Status 정의");
                    fragment_rtu_status.readData(data);
                }catch (Exception e){
                    Log.e(TAG, "readData Error : " + e.toString());
                }
                break;
            case 1:
                fragment_rtu_setting = (fragment_RTU_Setting) fragmentManager_rtu.findFragmentByTag("f1");
                Log.d(TAG, "currentPage : Setting");
                try {
                    fragment_rtu_setting.readData(data);
                }catch (Exception e){
                    Log.e(TAG, "readData Error : " + e.toString());
                }
                break;
        }
    }


    private void closeDevice() {
        Log.d(TAG, "closeDevice start");
        mThreadIsStopped = true;
        if(ftDev != null) {
            ftDev.close();
        }else{
            Log.d(TAG, "closeDevice : ftDev null");
        }
        Log.d(TAG, "closeDevice end");
    }

    public void SetConfig(int baud, byte dataBits, byte stopBits, byte parity, byte flowControl) {
        Log.d(TAG, "SetConfig start");
        if (!ftDev.isOpen()) {
            Log.e(TAG, "SetConfig: device not open");
            return;
        }

        // configure our port
        // reset to UART mode for 232 devices
        ftDev.setBitMode((byte) 0, D2xxManager.FT_BITMODE_RESET);

        ftDev.setBaudRate(baud);

        switch (dataBits) {
            case 7:
                dataBits = D2xxManager.FT_DATA_BITS_7;
                break;
            case 8:
                dataBits = D2xxManager.FT_DATA_BITS_8;
                break;
            default:
                dataBits = D2xxManager.FT_DATA_BITS_8;
                break;
        }

        switch (stopBits) {
            case 1:
                stopBits = D2xxManager.FT_STOP_BITS_1;
                break;
            case 2:
                stopBits = D2xxManager.FT_STOP_BITS_2;
                break;
            default:
                stopBits = D2xxManager.FT_STOP_BITS_1;
                break;
        }

        switch (parity) {
            case 0:
                parity = D2xxManager.FT_PARITY_NONE;
                break;
            case 1:
                parity = D2xxManager.FT_PARITY_ODD;
                break;
            case 2:
                parity = D2xxManager.FT_PARITY_EVEN;
                break;
            case 3:
                parity = D2xxManager.FT_PARITY_MARK;
                break;
            case 4:
                parity = D2xxManager.FT_PARITY_SPACE;
                break;
            default:
                parity = D2xxManager.FT_PARITY_NONE;
                break;
        }

        ftDev.setDataCharacteristics(dataBits, stopBits, parity);

        short flowCtrlSetting;
        switch (flowControl) {
            case 0:
                flowCtrlSetting = D2xxManager.FT_FLOW_NONE;
                break;
            case 1:
                flowCtrlSetting = D2xxManager.FT_FLOW_RTS_CTS;
                break;
            case 2:
                flowCtrlSetting = D2xxManager.FT_FLOW_DTR_DSR;
                break;
            case 3:
                flowCtrlSetting = D2xxManager.FT_FLOW_XON_XOFF;
                break;
            default:
                flowCtrlSetting = D2xxManager.FT_FLOW_NONE;
                break;
        }

        // TODO : flow ctrl: XOFF/XOM
        // TODO : flow ctrl: XOFF/XOM
        ftDev.setFlowControl(flowCtrlSetting, (byte) 0x0b, (byte) 0x0d);
        Log.d(TAG, "SetConfig end");
    }


    // done when ACTION_USB_DEVICE_ATTACHED
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        openDevice();
    }

    BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "mUsbReceiver start and action : " + action);
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                openDevice();
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                closeDevice();
            }
        }
    };



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

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "start onResume");

        Log.d(TAG, "Leave onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "start onDestroy");
        mThreadIsStopped = true;
        unregisterReceiver(mUsbReceiver);
        closeDevice();
        Log.d(TAG, "Leave onDestroy");
    }
}
