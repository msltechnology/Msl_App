package com.example.mslapp.Ble.fragment;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mslapp.Ble.Setting_Dialog.dialogFragment_Ble_Setting_FL_Setting;
import com.example.mslapp.Ble.Setting_Dialog.dialogFragment_Ble_Setting_ID_Setting;
import com.example.mslapp.BleMainActivity;
import com.example.mslapp.R;

import static com.example.mslapp.BleMainActivity.BluetoothStatus;
import static com.example.mslapp.BleMainActivity.MY_PERMISSIONS_REQUEST_READ_CONTACTS;
import static com.example.mslapp.BleMainActivity.adminApp;
import static com.example.mslapp.BleMainActivity.bluetooth_permission_check;
import static com.example.mslapp.BleMainActivity.locationManager;
import static com.example.mslapp.BleMainActivity.mBleContext;
import static com.example.mslapp.BleMainActivity.SnFlag;
import static com.example.mslapp.BleMainActivity.CdsFlag;

public class fragment_Ble_Beginning extends Fragment {

    int permissionCheck = 0;

    public fragment_Ble_Beginning() {

    }


    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Beginning";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ble_fragment_beginning, null);

        Log.d(TAG, "fragment_Ble_Beginning onCreateView");

        Button bleScanBtn = view.findViewById(R.id.bleScan);
        Button bleScanCDSBtn = view.findViewById(R.id.btn_beginning_cds);
        Button bleScanSNBtn = view.findViewById(R.id.btn_beginning_SN);


        if (adminApp) {
            bleScanCDSBtn.setVisibility(View.VISIBLE);
            bleScanSNBtn.setVisibility(View.VISIBLE);
        } else {
            bleScanCDSBtn.setVisibility(View.GONE);
            bleScanSNBtn.setVisibility(View.GONE);
        }

        bleScanBtn.setOnClickListener(v -> fragmentScanChange());
        bleScanCDSBtn.setOnClickListener(v -> bleScanCDSBtnOnClick());
        bleScanSNBtn.setOnClickListener(v -> bleScanSNBtnOnClick());
        return view;
    }


    private void showEditDialog() {
        FragmentManager fm = this.getChildFragmentManager();
        dialogFragment_Ble_Setting_FL_Setting editNameDialogFragment = new dialogFragment_Ble_Setting_FL_Setting();
        editNameDialogFragment.show(fm, "fragment_setting_dialog");
    }



    void fragmentScanChange() {

        Log.d(TAG, "bleScanBtn Click");

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
        Intent intent;

        // 위치정보 설정 Intent
        intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

        if (!locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(mBleContext);

            builder.setTitle(R.string.ble_main_checkPermission_GPS_alertDialog_title).setMessage(R.string.ble_main_checkPermission_GPS_alertDialog_message);

            builder.setPositiveButton("OK", (dialog, id) -> startActivity(intent));

            builder.setNegativeButton("Cancel", (dialog, id) -> {

            });
            AlertDialog alertDialog = builder.create();

            alertDialog.show();

            return;
        }

        if (Build.VERSION.SDK_INT >= 29) {
            int permissionCheck = ContextCompat.checkSelfPermission(super.getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION);

            Log.d(TAG, "GPS 권한 ACCESS_FINE_LOCATION" + permissionCheck);

            //
            if (permissionCheck < 0) {
                ActivityCompat.requestPermissions(super.getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {

            int permissionCheck = ContextCompat.checkSelfPermission(super.getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION);

            Log.d(TAG, "GPS 권한 ACCESS_COARSE_LOCATION" + permissionCheck);

            //
            if (permissionCheck < 0) {
                ActivityCompat.requestPermissions(super.getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }

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
}
