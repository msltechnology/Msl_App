package com.msl.mslapp.ble.fragment;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.msl.mslapp.ble.Dialog.Beginning.dialogFragment_Ble_Beginning_LanguageChange;
import com.msl.mslapp.ble.Dialog.setting.dialogFragment_Ble_Setting_FL_Setting;
import com.msl.mslapp.BleMainActivity;
import com.msl.mslapp.R;
import com.msl.mslapp.MenualActivity;
import com.msl.mslapp.RTUMainActivity;


import java.util.Locale;

import static com.msl.mslapp.BleMainActivity.BluetoothStatus;
import static com.msl.mslapp.BleMainActivity.adminApp;
import static com.msl.mslapp.BleMainActivity.bluetooth_permission_check;
import static com.msl.mslapp.BleMainActivity.locationManager;
import static com.msl.mslapp.BleMainActivity.mBleContext;
import static com.msl.mslapp.BleMainActivity.SnFlag;
import static com.msl.mslapp.BleMainActivity.CdsFlag;
import static com.msl.mslapp.BleMainActivity.mBleMain;
import static com.msl.mslapp.BleMainActivity.navigation_icon_Change;
import static com.msl.mslapp.BleMainActivity.requestPermissionBle;
import static com.msl.mslapp.BleMainActivity.tLanguage;
import static com.msl.mslapp.MenualActivity.callMenual;

public class fragment_Ble_Beginning extends Fragment {

    int permissionCheck = 0;

    // ?????? ?????? ?????????
    private long mLastClickTime = 0;


    public fragment_Ble_Beginning() {

    }


    // ?????? ?????? ???
    public static final String TAG = "Msl-Ble-Beginning";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ble_fragment_beginning, null);

        Log.d(TAG, "fragment_Ble_Beginning onCreateView");

        CdsFlag = false;
        SnFlag = false;

        navigation_icon_Change("beginning");

        //Button bleScanBtn = view.findViewById(R.id.bleScan);
        ImageButton bleScanIb = view.findViewById(R.id.ib_ble_Scan);
        Button bleScanCDSBtn = view.findViewById(R.id.btn_beginning_cds);
        Button bleScanSNBtn = view.findViewById(R.id.btn_beginning_SN);
        LinearLayout llScan = view.findViewById(R.id.ll_ble_Scan_Move);
        TextView tvScan = view.findViewById(R.id.tv_ble_Scan_Move);
        LinearLayout llRTU = view.findViewById(R.id.ll_RTU_Move);
        TextView tvRTU = view.findViewById(R.id.tv_RTU_Move);
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

        //tvScan.setText(getString(R.string.ble_Beginning_tvScan));
        //tvRTU.setText(getString(R.string.ble_Beginning_tvRTU));

       /*

       if(tLanguage.equals("ko")){
            tvScan.setText(getString(R.string.ble_Beginning_tvScan));
        }else if(tLanguage.equals("en")){
            tvScan.setText(getString(R.string.ble_Beginning_tvScan));
        }

       if(tLanguage.equals("ko")){
            tvRTU.setText(getString(R.string.ble_Beginning_tvRTU));
        }else if(tLanguage.equals("en")){
            tvRTU.setText("RTU Setup(Wired)");
        }*/

        if (adminApp) {
            bleScanCDSBtn.setVisibility(View.VISIBLE);
            bleScanSNBtn.setVisibility(View.VISIBLE);
            llAdmin.setVisibility(View.VISIBLE);
            llScan.setVisibility(View.VISIBLE);
            llRTU.setVisibility(View.VISIBLE);
            llNoAdmin.setVisibility(View.VISIBLE);

        } else {
            bleScanCDSBtn.setVisibility(View.GONE);
            bleScanSNBtn.setVisibility(View.GONE);
            llAdmin.setVisibility(View.GONE);
            llScan.setVisibility(View.VISIBLE);
            llRTU.setVisibility(View.VISIBLE);
            llNoAdmin.setVisibility(View.VISIBLE);
        }

        //bleScanBtn.setOnClickListener(v -> fragmentScanChange());
        //bleScanIb.setOnClickListener(v -> fragmentScanChange());
        bleScanCDSBtn.setOnClickListener(v -> bleScanCDSBtnOnClick());
        bleScanSNBtn.setOnClickListener(v -> bleScanSNBtnOnClick());
        llScan.setOnClickListener(v -> fragmentScanChange());
        bleScanIb.setOnClickListener(v -> fragmentScanChange());

        llRTU.setOnClickListener(v -> {
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


        // ?????? ?????? ??? ???????????? On ??????
        //BleMainActivity.checkBluetoothPermission();


        return view;
    }


    private void showEditDialog() {
        FragmentManager fm = this.getChildFragmentManager();
        dialogFragment_Ble_Setting_FL_Setting editNameDialogFragment = new dialogFragment_Ble_Setting_FL_Setting();
        editNameDialogFragment.show(fm, "fragment_setting_dialog");
    }


    void permissionCheck(){
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

            //GPS ?????????????????? ??????
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);

            AlertDialog.Builder builder = new AlertDialog.Builder(mBleContext);

            builder.setTitle(R.string.ble_main_checkGPSManager_alertDialog_title).setMessage(R.string.ble_main_checkGPSManager_alertDialog_message);

            builder.setPositiveButton("OK", (dialog, id) -> {

                mBleMain.startActivity(intent);
            });

            builder.setNegativeButton("Cancel", (dialog, id) -> {

            });
            AlertDialog alertDialog = builder.create();

            alertDialog.show();

            return;
        }


        int permissionCheck;
        if (Build.VERSION.SDK_INT >= 29) {
            permissionCheck = ContextCompat.checkSelfPermission(mBleContext,
                    Manifest.permission.ACCESS_FINE_LOCATION);

            if (permissionCheck < 0) {
                Toast.makeText(mBleContext, getString(R.string.permissioncheck_no_FINE_LOCATION), Toast.LENGTH_LONG).show();

                String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

                requestPermissions(permissions, 51);
                return;
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    int permissionCheckSCAN = ContextCompat.checkSelfPermission(mBleContext,
                            Manifest.permission.BLUETOOTH_SCAN);

                    if(permissionCheckSCAN < 0){
                        Toast.makeText(mBleContext, getString(R.string.permissioncheck_no_BLUETOOTH_SCAN), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        } else {
            permissionCheck = ContextCompat.checkSelfPermission(mBleContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            if (permissionCheck < 0) {
                Toast.makeText(mBleContext, getString(R.string.permissioncheck_no_FINE_LOCATION), Toast.LENGTH_LONG).show();

                String[] permissions = new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION
                };
                requestPermissions(permissions, 51);

                return;
            }
        }

        // ???????????? ?????? ?????? ??????
        Intent intent;

        // ???????????? ????????? ??????
        if (BleMainActivity.mBluetoothAdapter.isEnabled()) {
            BluetoothStatus = "On";
            ((BleMainActivity) getActivity()).fragmentChange("fragment_ble_function");
        } else {
            // ???????????? ????????? ?????????
            intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            requestPermissionBle.launch(intent);
        }

        /*if(BleMainActivity.checkBluetoothPermission()){
            ((BleMainActivity) getActivity()).fragmentChange("fragment_ble_function");
        }*/

        /*if(BluetoothStatus.contains("On")){
            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                //GPS ?????????????????? ??????
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                AlertDialog.Builder builder = new AlertDialog.Builder(mBleContext);

                builder.setTitle(R.string.ble_main_checkPermission_GPS_alertDialog_title).setMessage(R.string.ble_main_checkPermission_GPS_alertDialog_message);

                builder.setPositiveButton("OK", (dialog, id) -> startActivity(intent));

                builder.setNegativeButton("Cancel", (dialog, id) -> {

                });
                AlertDialog alertDialog = builder.create();

                alertDialog.show();

            }else{
                ((BleMainActivity) getActivity()).fragmentChange("fragment_ble_function");
            }
        }else{
            checkBluetoothPermission();
        }*/
    }

    void fragmentScanChange() {

        Log.d(TAG, "bleScanIb Click");
        permissionCheck();

    }

    public void checkBluetoothPermission() {
        // ???????????? ?????? ?????? ??????

        Log.d(TAG, "checkBluetoothPermission");

        Intent intent;

        // ???????????? ????????? ?????????
        intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestPermissionBle.launch(intent);
        //startActivityForResult(intent, bluetooth_permission_check);
    }

    // ????????????(GPS) On
    public void checkGPSserviceOn() {
        Log.d(TAG, "checkGPSserviceOn");

        // ???????????? on ?????? ??????


        // ???????????? on ?????? ???
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //((BleMainActivity)getActivity()).disconnectGattServer("fragment_Ble_Beginning - onDestroy");
    }

    private void bleScanCDSBtnOnClick() {
        //showEditDialog();
        CdsFlag = true;
        permissionCheck();
    }
    private void bleScanSNBtnOnClick() {
        //showEditDialog();
        SnFlag = true;

        permissionCheck();
    }

    private void bleLanguageBtnOnClick() {
        // ?????????????????? ?????? ?????? ??????-setlocale ??????
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
        //config.locale = locale; // deprecated
        config.setLocale(locale);
        mBleContext.getResources().updateConfiguration(config, mBleContext.getResources().getDisplayMetrics());
        tLanguage = char_select; //????????? ?????? ?????? ????????? ??????
    }

    public static void ble_Beginning_reset(){
        Intent intent = mBleContext.getPackageManager().getLaunchIntentForPackage(mBleContext.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mBleMain.finish();
        mBleMain.startActivity(intent);
    }


}
