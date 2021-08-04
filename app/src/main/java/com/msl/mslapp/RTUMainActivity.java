package com.msl.mslapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.msl.mslapp.Ble.fragment.Function.fragment_Ble_Function;
import com.msl.mslapp.Ble.fragment.fragment_Ble_Beginning;
import com.msl.mslapp.Ble.fragment.fragment_Ble_Password;
import com.msl.mslapp.Ble.fragment.fragment_Ble_Scan;
import com.msl.mslapp.Ble.fragment.fragment_CDS_Setting;
import com.msl.mslapp.Ble.fragment.fragment_SN_Setting;
import com.msl.mslapp.Public.Log.log_ListViewAdapter;
import com.msl.mslapp.RTU.dialog.dialogFragment_rtu_function_LanguageChange;
import com.msl.mslapp.RTU.fragment.fragment_RTU_Function;
import com.msl.mslapp.RTU.fragment.fragment_RTU_Scan;
import com.google.android.material.navigation.NavigationView;

import java.util.Date;

import static com.msl.mslapp.BleMainActivity.mBleContext;
import static com.msl.mslapp.BleMainActivity.mBleMain;

public class RTUMainActivity extends AppCompatActivity {


    // 로그 이름 용
    public static final String TAG = "Msl-RTU-MainAct";

    public static Context mRTUContext = null;
    public static AppCompatActivity mRTUMain = null;

    public static String DATA_SIGN_START = "$"; //시작 기호
    public static String DATA_SIGN_CHECKSUM = "*"; //끝 기호
    public static String DATA_SIGN_COMMA = ","; //구분자
    public static String DATA_SIGN_CR = "\r"; //<CR>
    public static String DATA_SIGN_LF = "\n"; //<LF>
    public static String DATA_SIGN_OK = "OK";

    public static String DATA_TYPE_MUCMD = "MUCMD"; //Modem Unit : 모뎀 유닛
    public static String DATA_TYPE_LISTS = "LISTS"; //상태

    public static String DATA_NUM_0 = "0";
    public static String DATA_NUM_1 = "1";
    public static String DATA_NUM_2 = "2";
    public static String DATA_NUM_3 = "3";
    public static String DATA_NUM_4 = "4";
    public static String DATA_NUM_5 = "5";
    public static String DATA_NUM_6 = "6";
    public static String DATA_NUM_7 = "7";
    public static String DATA_NUM_8 = "8";
    public static String DATA_NUM_9 = "9";
    public static String DATA_NUM_10 = "10";
    public static String DATA_NUM_11 = "11";
    public static String DATA_NUM_14 = "14";
    public static String DATA_NUM_21 = "21";
    public static String DATA_NUM_99 = "99";
    public static String DATA_NUM_255 = "255";

    public static String STATUS_CALL = DATA_SIGN_START + DATA_TYPE_MUCMD + DATA_SIGN_COMMA +
            DATA_NUM_8 + DATA_SIGN_CHECKSUM +
            DATA_NUM_1 + DATA_NUM_1 +
            DATA_SIGN_CR + DATA_SIGN_LF;

    // Tablayout title 이름
    String[] tavTitle = new String[]{"Status", "Setting"};

    //toolbar
    Toolbar toolbarMain;
    NavigationView navigationView;

    ListView log_Listview;
    public static log_ListViewAdapter log_listViewAdapter;
    LinearLayout ll_navigation_move, ll_navigation_log;

    // drawLayout(bluetoothMainlayout)
    private DrawerLayout rtuDrawerLayout;
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
        Log.d(TAG, "onCreate");
        setContentView(R.layout.rtu_activity_main);

        mRTUContext = this;
        mRTUMain = this;

        // 툴바
        toolbarMain = (Toolbar) findViewById(R.id.rtu_toolbar_main);
        toolbarMain.setTitle("");
        toolbarMain.setTitleTextColor(getResources().getColor(R.color.ble_toolbarmain));
        setSupportActionBar(toolbarMain);

        getSupportFragmentManager().beginTransaction().add(R.id.rtu_Fragment_Space, new fragment_RTU_Function(), "devices").commit();

        // 사이드바 관련
        navigationView = findViewById(R.id.rtu_sidebar);
        navigationView.setItemIconTintList(null);

        navigation_Setting();

        toolbarMain.setNavigationIcon(R.drawable.toolbar_icon);

        /*// 사이드바(네비게이션) 아이템 선택 시
        navigationView.setNavigationItemSelectedListener(item -> {
            Intent intent;
            if (item.getItemId() == R.id.menu_bluetooth) {
                intent = new Intent(mRTUContext, BleMainActivity.class);
                try {
                    fragment_RTU_Function fragment_RTU_function = (fragment_RTU_Function) getSupportFragmentManager().findFragmentById(R.id.rtu_Fragment_Space);
                    fragment_RTU_function.disconnect();
                } catch (Exception e) {
                    Log.e(TAG, "RTUMainActivity navigationView Error - Status : " + e.toString());
                }
                startActivity(intent);
                finish();
            } else if (item.getItemId() == R.id.menu_rtu) {
                intent = new Intent(mRTUContext, RTUMainActivity.class);
                startActivity(intent);
                finish();
            } else if (item.getItemId() == R.id.menu_language) {
                FragmentManager fm = getSupportFragmentManager();
                dialogFragment_rtu_function_LanguageChange customDialog_RTU_LanguageChange = new dialogFragment_rtu_function_LanguageChange();
                customDialog_RTU_LanguageChange.show(fm, "fragment_beginning_dialog_LanguageChange");
            }
            // 사이드바 닫기
            rtuDrawerLayout.closeDrawer(navigationView);

            return false;
        });*/

    }

    void navigation_Setting() {
        // 사이드바 관련
        navigationView = findViewById(R.id.rtu_sidebar);
        navigationView.setItemIconTintList(null);
        log_Listview = navigationView.findViewById(R.id.lv_Navigation_Log);
        ll_navigation_move = navigationView.findViewById(R.id.ll_Navigation_Move);
        ll_navigation_log = navigationView.findViewById(R.id.ll_Navigation_Log);
        log_Refresh();

        //Menu menu = navigationView.getMenu();
        //menu.findItem(R.id.menu_bluetooth).setVisible(false);

        Display display = mRTUContext.getDisplay();
        Point size = new Point();
        display.getSize(size);

        int x_log_on = Math.toIntExact(Math.round((size.x * 0.8)));
        int x_log_off = Math.toIntExact(Math.round((size.x * 0.65)));

        // 초기 화면 크기 결정
        navigationView.getLayoutParams().width = x_log_off;

        // Ble 이동
        navigationView.findViewById(R.id.ll_Navigation_Move_Ble).setOnClickListener(v -> {
            Intent intent = new Intent(mRTUContext, BleMainActivity.class);
            try {
                fragment_RTU_Function fragment_RTU_function = (fragment_RTU_Function) getSupportFragmentManager().findFragmentById(R.id.rtu_Fragment_Space);
                fragment_RTU_function.disconnect();
            } catch (Exception e) {
                Log.e(TAG, "RTUMainActivity navigationView Error - Status : " + e.toString());
            }
            startActivity(intent);
            finish();
        });

        // RTU로 이동
        navigationView.findViewById(R.id.ll_Navigation_Move_RTU).setOnClickListener(v -> {
            Intent intent = new Intent(mRTUContext, RTUMainActivity.class);
            startActivity(intent);
            finish();
        });

        // 언어변환 키기
        navigationView.findViewById(R.id.ll_Navigation_Show_Language).setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            dialogFragment_rtu_function_LanguageChange customDialog_RTU_LanguageChange = new dialogFragment_rtu_function_LanguageChange();
            customDialog_RTU_LanguageChange.show(fm, "fragment_beginning_dialog_LanguageChange");
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

        rtuDrawerLayout = findViewById(R.id.rtu_drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, rtuDrawerLayout, toolbarMain, R.string.app_name, R.string.app_name);
        rtuDrawerLayout.addDrawerListener(drawerToggle); // 삼선 메뉴 사용 시 뱅그르르 돈다.
        drawerToggle.syncState(); // 메뉴 버튼 추가
    }

    // 일반적 시스템 데이터일 경우
    public static void logData_RTU(String data) {
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("HH:mm:ss");
        String getTime = simpleDate.format(mDate);

        Log.d(TAG, "log_data : " + getTime);
        log_listViewAdapter.addItem(getTime, data);
        log_listViewAdapter.notifyDataSetChanged();
    }


    // 특정 데이터 경우(read, write, error)
    public static void logData_RTU(String data, String color) {
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("hh:mm:ss");
        String getTime = simpleDate.format(mDate);

        Log.d(TAG, "log_data : " + getTime);

        if (color.equals("read")) {
            if (data.contains("[ ConfMsg]")) {
                log_listViewAdapter.addItem(getTime, data.substring(11), color);
                log_listViewAdapter.notifyDataSetChanged();
                return;
            }
        } else if (color.equals("write")) {
            String[] dataArr = data.split(DATA_SIGN_COMMA);

            try {
                // 해당 순서의 값에 * 이 있을 경우
                if (dataArr[1].contains(DATA_SIGN_CHECKSUM)) {
                    dataArr[1] = dataArr[1].substring(0, dataArr[1].indexOf(DATA_SIGN_CHECKSUM));
                }
            } catch (Exception e) {
                Log.d(TAG, "*이 없음");
            }

            if (dataArr[0].contains(DATA_TYPE_MUCMD)) {
                switch (dataArr[1]) {
                    // RTU ID, Lantern ID 설정
                    case "1":
                        log_listViewAdapter.addItem(getTime, "ID Setting - RTU ID : " + dataArr[2] + ", Lantern ID : " + dataArr[3].substring(0, dataArr[3].indexOf(DATA_SIGN_CHECKSUM)), color);
                        break;
                    // 모국서버 설정
                    case "2":
                        log_listViewAdapter.addItem(getTime,
                                "IP Setting" +
                                        "\nServer 1 : \n"
                                        + dataArr[2] + "," + dataArr[3] + "," + dataArr[4] + "," + dataArr[5] + ":" + dataArr[6] +
                                        "\nServer 2 : \n"
                                        + dataArr[7] + "," + dataArr[8] + "," + dataArr[9] + "," + dataArr[10] + ":" + dataArr[11].substring(0, dataArr[11].indexOf(DATA_SIGN_CHECKSUM)), color);
                        break;
                    // 리셋시간 설정
                    case "3":
                        log_listViewAdapter.addItem(getTime, "ResetTime Setting", color);
                        break;
                    // 프로토콜 설정
                    case "4":
                        if (dataArr[2].startsWith("0")) {
                            log_listViewAdapter.addItem(getTime, "Protocol Setting - public", color);
                        } else if (dataArr[2].startsWith("1")) {
                            log_listViewAdapter.addItem(getTime, "Protocol Setting - private", color);
                        }
                        break;
                    // 전송주기 설정
                    case "5":
                        log_listViewAdapter.addItem(getTime, "Send Cycle Setting : " + dataArr[2].substring(0, dataArr[2].indexOf(DATA_SIGN_CHECKSUM)), color);
                        break;
                    // 리셋
                    case "6":
                        log_listViewAdapter.addItem(getTime, "Modem - Reset", color);
                        break;
                    // 모국 전송
                    case "7":
                        log_listViewAdapter.addItem(getTime, "Modem - Send Data", color);
                        break;
                    // 설정값 조회
                    case "8":
                        log_listViewAdapter.addItem(getTime, "Status Request", color);
                        break;
                    // 모뎀 LED 설정
                    case "9":
                        log_listViewAdapter.addItem(getTime, "Modem - Led", color);
                        break;
                    // GMT 설정
                    case "10":
                        if (dataArr[2].startsWith("0")) {
                            log_listViewAdapter.addItem(getTime, "GMT Setting - KOR", color);
                        } else if (dataArr[2].startsWith("1")) {
                            log_listViewAdapter.addItem(getTime, "GMT Setting - ENG", color);
                        }
                        break;
                    // 모뎀 파워 설정
                    case "11":
                        if (dataArr[2].startsWith("0")) {
                            log_listViewAdapter.addItem(getTime, "Modem - Power : OFF", color);
                        } else if (dataArr[2].startsWith("1")) {
                            log_listViewAdapter.addItem(getTime, "Modem - Power : ON", color);
                        }
                        break;
                    // 관리자 번호 설정
                    case "12":
                        log_listViewAdapter.addItem(getTime, "Admin Setting", color);
                        break;
                    // Low Power 설정
                    case "13":
                        log_listViewAdapter.addItem(getTime, "Low Power Setting", color);
                        break;
                }
                log_listViewAdapter.notifyDataSetChanged();
                return;
            } else if (dataArr[0].contains("AT$$TCP_STATE??")) {
                // TCP 통신 상태 확인
                log_listViewAdapter.addItem(getTime, "TCP State Call", color);
            } else if (dataArr[0].contains("AT$$MDN")) {
                // 모뎀 전화번호 확인[
                log_listViewAdapter.addItem(getTime, "Modem Phone Num Call", color);
            } else if (dataArr[0].contains("$LICMD")) {
                // RTU 와 연결된 등명기 상태 확인
                log_listViewAdapter.addItem(getTime, "Lantern Status Call", color);
            }
            log_listViewAdapter.notifyDataSetChanged();
            return;
        }

        log_listViewAdapter.addItem(getTime, data, color);
        log_listViewAdapter.notifyDataSetChanged();
    }


    public void log_Refresh() {
        log_listViewAdapter = new log_ListViewAdapter();
        log_Listview.setAdapter(log_listViewAdapter);
    }

    // 현재 프래그먼트 확인용
    public static Fragment rtu_currentFragment;


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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

        Log.d(TAG, "onBackPressed!");

        if (rtuDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            rtuDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        } else {
            //super.onBackPressed();
        }

        rtu_currentFragment = getSupportFragmentManager().findFragmentById(R.id.rtu_Fragment_Space);

        if (rtu_currentFragment instanceof fragment_RTU_Function) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Notice").setMessage("초기화면으로 돌아가시겠습니까?\nDo you want to return to the initial screen?");

            builder.setPositiveButton("OK", (dialog, id) -> {
                Intent intent = new Intent(mRTUContext, BleMainActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
            });

            builder.setNegativeButton("Cancel", (dialog, id) -> {

            });
            AlertDialog alertDialog = builder.create();

            alertDialog.show();
        } else if (rtu_currentFragment instanceof fragment_RTU_Scan) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Notice").setMessage("RTU 앱을 종료합니까?");

            builder.setPositiveButton("OK", (dialog, id) -> {
                finish();
            });

            builder.setNegativeButton("Cancel", (dialog, id) -> {

            });
            AlertDialog alertDialog = builder.create();

            alertDialog.show();
        }

    }


    @Override
    protected void onNewIntent(Intent intent) {
        if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(intent.getAction())) {
            fragment_RTU_Function terminal = (fragment_RTU_Function) getSupportFragmentManager().findFragmentByTag("terminal");
            if (terminal != null)
                terminal.status("USB device detected");
        }
        super.onNewIntent(intent);

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
