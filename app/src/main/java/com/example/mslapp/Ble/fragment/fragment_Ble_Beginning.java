package com.example.mslapp.Ble.fragment;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.mslapp.BleMainActivity;
import com.example.mslapp.R;

import static com.example.mslapp.BleMainActivity.MY_PERMISSIONS_REQUEST_READ_CONTACTS;
import static com.example.mslapp.BleMainActivity.adminApp;
import static com.example.mslapp.BleMainActivity.bluetooth_permission_check;
import static com.example.mslapp.BleMainActivity.cdsFlag;
import static com.example.mslapp.BleMainActivity.locationManager;
import static com.example.mslapp.BleMainActivity.mBluetoothAdapter;
import static com.example.mslapp.BleMainActivity.mBleContext;

public class fragment_Ble_Beginning extends Fragment {

    int permissionCheck = 0;

    public fragment_Ble_Beginning(){

    }


    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Beginning";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ble_fragment_beginning, null);

        Log.d(TAG,"fragment_Ble_Beginning onCreateView");

        Button bleScanBtn = view.findViewById(R.id.bleScan);
        Button bleScanCDSBtn = view.findViewById(R.id.bleScan_cds);
        if(adminApp){
            bleScanCDSBtn.setVisibility(View.VISIBLE);
        }else{
            bleScanCDSBtn.setVisibility(View.GONE);
        }

        bleScanBtn.setOnClickListener(v -> fragmentScanChange());

        bleScanCDSBtn.setOnClickListener(this::onClick);

        return view;
    }

    void fragmentScanChange(){

        Log.d(TAG,"bleScanBtn Click");

        permissionCheck = ContextCompat.checkSelfPermission(mBleContext,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        // 블루투스 및 gps 켜져있는지 확인
        if (mBluetoothAdapter == null || !mBluetoothAdapter.enable()) {
            checkBluetoothPermission();
            return;
        }else if(permissionCheck < 0){
            checkGPSserviceOn();
        }

        ((BleMainActivity)getActivity()).fragmentChange("fragment_ble_scan");
    }

    public void checkBluetoothPermission(){
        // 블루투스 권한 확인 시작

        Log.d(TAG,"checkBluetoothPermission");

        Intent intent;

        if (mBluetoothAdapter.isEnabled()) {
            // 블루투스 관련 실행 진행

            // 이후 GPS 상태 요청
            checkGPSserviceOn();
        } else {
            // 블루투스 활성화 하도록
            intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, bluetooth_permission_check);
        }
        // 블루투스 권한 확인 끝
    }

    // 위치정보(GPS) On
    public void checkGPSserviceOn(){
        Log.d(TAG,"checkGPSserviceOn");

        // 위치정보 on 확인 시작
        Intent intent;

        // 위치정보 설정 Intent
        intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

        if(!locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(mBleContext);

            builder.setTitle("위치정보 ON 요청").setMessage("GPS(위치정보)가 OFF 상태입니다.\n블루투스 스캔 이용이 제한됩니다.\n설정창으로 이동하시겠습니까?");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id)
                {
                    startActivity(intent);
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id)
                {

                }
            });
            AlertDialog alertDialog = builder.create();

            alertDialog.show();

        }

        //
        if(permissionCheck < 0){
            ActivityCompat.requestPermissions(super.getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        // 위치정보 on 확인 끝
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //((BleMainActivity)getActivity()).disconnectGattServer("fragment_Ble_Beginning - onDestroy");
    }

    private void onClick(View v) {
        cdsFlag = true;
        fragmentScanChange();
    }
}
