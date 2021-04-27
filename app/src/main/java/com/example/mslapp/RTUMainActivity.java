package com.example.mslapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
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
import com.example.mslapp.RTU.fragment.fragment_RTU_Function;
import com.example.mslapp.RTU.fragment.fragment_RTU_Scan;
import com.example.mslapp.RTU.fragment.fragment_RTU_Setting;
import com.example.mslapp.RTU.fragment.fragment_RTU_Status;
import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class RTUMainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {


    // 로그 이름 용
    public static final String TAG = "Msl-RTU-MainAct";

    public Context mRTUContext = null;
    public static AppCompatActivity mRTUMain = null;

    // d2xx - RTU 통신 간 필요

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

        /*// 탭바
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
        ).attach();*/

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.rtu_Fragment_Space, new fragment_RTU_Scan(), "devices").commit();
        else
            onBackStackChanged();


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

        Log.d(TAG, "Leave onDestroy");
    }

    @Override
    public void onBackStackChanged() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount()>0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(intent.getAction())) {
            fragment_RTU_Function terminal = (fragment_RTU_Function)getSupportFragmentManager().findFragmentByTag("terminal");
            if (terminal != null)
                terminal.status("USB device detected");
        }
        super.onNewIntent(intent);
    }
}
