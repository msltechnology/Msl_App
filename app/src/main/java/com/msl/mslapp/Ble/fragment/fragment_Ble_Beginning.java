package com.msl.mslapp.Ble.fragment;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.msl.mslapp.Ble.Dialog.Beginning.dialogFragment_Ble_Beginning_LanguageChange;
import com.msl.mslapp.Ble.Dialog.Setting.dialogFragment_Ble_Setting_FL_Setting;
import com.msl.mslapp.BleMainActivity;
import com.msl.mslapp.R;
import com.msl.mslapp.MenualActivity;
import com.msl.mslapp.RTUMainActivity;


import java.util.Locale;

import static com.msl.mslapp.BleMainActivity.BluetoothStatus;
import static com.msl.mslapp.BleMainActivity.DATA_TYPE_DIP;
import static com.msl.mslapp.BleMainActivity.MY_PERMISSIONS_REQUEST_READ_CONTACTS;
import static com.msl.mslapp.BleMainActivity.adminApp;
import static com.msl.mslapp.BleMainActivity.bluetooth_permission_check;
import static com.msl.mslapp.BleMainActivity.locationManager;
import static com.msl.mslapp.BleMainActivity.mBleContext;
import static com.msl.mslapp.BleMainActivity.SnFlag;
import static com.msl.mslapp.BleMainActivity.CdsFlag;
import static com.msl.mslapp.BleMainActivity.mBleMain;
import static com.msl.mslapp.BleMainActivity.navigation_icon_Change;
import static com.msl.mslapp.BleMainActivity.tLanguage;
import static com.msl.mslapp.MenualActivity.callMenual;

public class fragment_Ble_Beginning extends Fragment {

    int permissionCheck = 0;

    // 중복 클릭 방지용
    private long mLastClickTime = 0;


    public fragment_Ble_Beginning() {

    }


    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Beginning";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ble_fragment_beginning, null);

        Log.d(TAG, "fragment_Ble_Beginning onCreateView");

        CdsFlag = false;
        SnFlag = false;

        navigation_icon_Change("beggining");

        //Button bleScanBtn = view.findViewById(R.id.bleScan);
        ImageButton bleScanIb = view.findViewById(R.id.ib_ble_Scan);
        Button bleScanCDSBtn = view.findViewById(R.id.btn_beginning_cds);
        Button bleScanSNBtn = view.findViewById(R.id.btn_beginning_SN);
        Button btnScan = view.findViewById(R.id.btn_ble_Scan_Move);
        Button btnRTU = view.findViewById(R.id.btn_RTU_Move);
        LinearLayout llAdmin = view.findViewById(R.id.ll_beginning_Admin);
        LinearLayout llNoAdmin = view.findViewById(R.id.ll_beginning_noAdmin);
        TextView version = view.findViewById(R.id.app_versionName);

        PackageInfo packageInfo;
        try {
            packageInfo = mBleContext.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(mBleContext.getApplicationContext().getPackageName(), 0 );

            version.setText("Ver " + packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if(tLanguage.equals("ko")){
            btnScan.setText("등명기 모니터링");
        }else if(tLanguage.equals("en")){
            btnScan.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            btnScan.setText("Lantern\nMonitoring");
        }

        if(tLanguage.equals("ko")){
            btnRTU.setText("RTU 설정");
        }else if(tLanguage.equals("en")){
            btnRTU.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            btnRTU.setText("RTU Setup");
        }

        if (adminApp) {
            bleScanCDSBtn.setVisibility(View.VISIBLE);
            bleScanSNBtn.setVisibility(View.VISIBLE);
            llAdmin.setVisibility(View.VISIBLE);
            btnScan.setVisibility(View.VISIBLE);
            btnRTU.setVisibility(View.VISIBLE);
            llNoAdmin.setVisibility(View.VISIBLE);

        } else {
            bleScanCDSBtn.setVisibility(View.GONE);
            bleScanSNBtn.setVisibility(View.GONE);
            llAdmin.setVisibility(View.GONE);
            btnScan.setVisibility(View.VISIBLE);
            btnRTU.setVisibility(View.VISIBLE);
            llNoAdmin.setVisibility(View.VISIBLE);
        }

        //bleScanBtn.setOnClickListener(v -> fragmentScanChange());
        //bleScanIb.setOnClickListener(v -> fragmentScanChange());
        bleScanCDSBtn.setOnClickListener(v -> bleScanCDSBtnOnClick());
        bleScanSNBtn.setOnClickListener(v -> bleScanSNBtnOnClick());
        btnScan.setOnClickListener(v -> fragmentScanChange());
        btnRTU.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            Intent intent = new Intent(mBleContext, RTUMainActivity.class);
            startActivity(intent);
            mBleMain.finish();
        });

        /*Drawable roundDrawable = getResources().getDrawable(R.drawable.custom_ble_beginning_btn);
        roundDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            btnScan.setBackgroundDrawable(roundDrawable);
            btnRTU.setBackgroundDrawable(roundDrawable);
        } else {
            btnScan.setBackground(roundDrawable);
            btnRTU.setBackground(roundDrawable);
        }*/



        return view;
    }


    private void showEditDialog() {
        FragmentManager fm = this.getChildFragmentManager();
        dialogFragment_Ble_Setting_FL_Setting editNameDialogFragment = new dialogFragment_Ble_Setting_FL_Setting();
        editNameDialogFragment.show(fm, "fragment_setting_dialog");
    }



    void fragmentScanChange() {

        Log.d(TAG, "bleScanIb Click");

        if(BluetoothStatus.contains("On")){
            ((BleMainActivity) getActivity()).fragmentChange("fragment_ble_scan");
        }else{
            checkBluetoothPermission();
        }
    }

    public void checkBluetoothPermission() {
        // 블루투스 권한 확인 시작

        Log.d(TAG, "checkBluetoothPermission");

        Intent intent;

        // 블루투스 활성화 하도록
        intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, bluetooth_permission_check);
    }

    // 위치정보(GPS) On
    public void checkGPSserviceOn() {
        Log.d(TAG, "checkGPSserviceOn");

        // 위치정보 on 확인 시작


        // 위치정보 on 확인 끝
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //((BleMainActivity)getActivity()).disconnectGattServer("fragment_Ble_Beginning - onDestroy");
    }

    private void bleScanCDSBtnOnClick() {
        //showEditDialog();
        CdsFlag = true;
        fragmentScanChange();
    }
    private void bleScanSNBtnOnClick() {
        //showEditDialog();
        SnFlag = true;
        fragmentScanChange();
    }

    private void bleLanguageBtnOnClick() {
        // 다이어로그를 통해 언어 변환-setlocale 이용
        FragmentManager fm = this.getChildFragmentManager();
        dialogFragment_Ble_Beginning_LanguageChange customDialogLanguageChange = new dialogFragment_Ble_Beginning_LanguageChange();
        customDialogLanguageChange.show(fm,"fragment_beginning_dialog_LanguageChange");


        /*setLocale("ko");
        ble_Beginning_reset();*/
    }
    private void bleManualBtnOnClick() {
        callMenual = true;
        Intent intent = new Intent(mBleContext, MenualActivity.class);
        startActivity(intent);
        getActivity().finish();
    }




    public static void setLocale(String char_select) {
        switch (char_select) {
            case "ko":
                break;
            case "en":
                break;
            default:
                char_select = "en";
        }

        Locale locale = new Locale(char_select);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        mBleContext.getResources().updateConfiguration(config, mBleContext.getResources().getDisplayMetrics());
        tLanguage = char_select; //설정된 언어 저장 변수에 저장
    }

    public static void ble_Beginning_reset(){
        Intent intent = mBleContext.getPackageManager().getLaunchIntentForPackage(mBleContext.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mBleMain.finish();
        mBleMain.startActivity(intent);
    }


}
