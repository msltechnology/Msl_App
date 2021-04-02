package com.example.mslapp.Ble.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.mslapp.BleMainActivity;
import com.example.mslapp.R;
import com.example.mslapp.Ble.blelistview.ListViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.mslapp.BleMainActivity.cdsFlag;
import static com.example.mslapp.BleMainActivity.filters;
import static com.example.mslapp.BleMainActivity.mBluetoothAdapter;
import static com.example.mslapp.BleMainActivity.scanningFlag;
import static com.example.mslapp.BleMainActivity.settings;

public class fragment_Ble_Scan extends Fragment {

    //scan results
    public ArrayList<BluetoothDevice> scanResults = new ArrayList();

    public fragment_Ble_Scan() {

    }

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Scan";

    ListView bleListview;
    ListViewAdapter adapter;

    // 상위Activity 에게 데이터 주는 용도
    private Ble_Scan_Listener selecetBleListener;
    private Activity activity;

    BackThread backThread;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ble_fragment_scan, null);


        ((Ble_Scan_Listener) activity).onCreateViewFragment_Ble_Scan();

        Log.d(TAG, "OnCreateView");

        adapter = new ListViewAdapter();

        bleListview = view.findViewById(R.id.bleList);
        bleListview.setAdapter(adapter);

        bleListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                if (!BleMainActivity.BleConnecting) {
                    BleMainActivity.BleConnecting = true;

                    //BluetoothDevice device =(BluetoothDevice) parent.getItemAtPosition(position);
                    BluetoothDevice device = scanResults.get(position);

                    ((Ble_Scan_Listener) activity).onSelectBleDevice(device);

                    stopScan();
                    if (cdsFlag) {
                        ((BleMainActivity) getActivity()).fragmentChange("fragment_cds_setting");
                    } else {
                        ((BleMainActivity) getActivity()).fragmentChange("fragment_ble_password");
                    }


                }

                /*// get TextView's Text.
                BluetoothDevice device =(BluetoothDevice) parent.getItemAtPosition(position);
                String deviceAddress = device.getAddress();
                String name = device.getName();*/
            }
        });


        Scan();

        return view;
    }


    public void Scan() {
        Log.d(TAG, "Scan");
        scanningFlag = true;
        // 필터(특정 uuid 등 조건으로 검색), 세팅(저전력, 풀파워 검색할지) 등 설정.
        mBluetoothAdapter.getBluetoothLeScanner().startScan(filters, settings, BLEScanCallback);
        // 필터 및 세팅 없이 기본 스캔
        //mBluetoothAdapter.getBluetoothLeScanner().startScan(BLEScanCallback);

        backThread = new BackThread();
        backThread.setDaemon(true);
        backThread.start();
    }

    public void reFresh() {
        Log.d(TAG, "reSearch");
        stopScan();
        scanResults = new ArrayList();
        adapter = new ListViewAdapter();
        bleListview.setAdapter(adapter);
        Scan();
    }

    public void stopScan() {
        Log.d(TAG, "stopScan");
        if (scanningFlag) {
            backThread.interrupt();
            Log.d(TAG, "stopScan in Flag");
            scanningFlag = false;

            ((BleMainActivity) getActivity()).ble_stopscanning();
            mBluetoothAdapter.getBluetoothLeScanner().stopScan(BLEScanCallback);
        }
    }

    class BackThread extends Thread {
        @Override
        public void run() {
            Log.d(TAG, "BackThread");

            try {
                Thread.sleep(10000);
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            stopScan();
        }
    };


    // 블루투스 스캔하기
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public ScanCallback

            BLEScanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            addScanResult(result);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {
                addScanResult(sr);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e(TAG, "Scan Failed - Error Code: " + errorCode);
        }

        private void addScanResult(ScanResult result) {
            try {

                BluetoothDevice device = result.getDevice();
                String deviceAddress = device.getAddress();
                String name = device.getName();

                int rssi = result.getRssi();

                // 중복 체크
                for (BluetoothDevice dev : scanResults) {
                    if (dev.getAddress().equals(deviceAddress)) {
                        return;
                    }
                }

                // userdata 받기(등명기 시리얼)
                byte[] scanRecord = result.getScanRecord().getBytes();
                byte[] advertisedData = Arrays.copyOfRange(scanRecord, 0, scanRecord.length);

                String stringBuffer = new String(advertisedData); //모든 데이터를 문자열로 받아온다
/*

                if (stringBuffer.contains("\n") || stringBuffer.contains("\r")) {
                    Log.d(TAG, "stringBuffer contain n or r");
                    stringBuffer = stringBuffer.replaceAll("(\n|\r)", "");
                }
*/

                Log.d(TAG, "scanResults.size : " + scanResults.size() + " ---- addScanList : " + stringBuffer + " ------ name : " + name + " ------- address : " + deviceAddress);

                String userdataAll = stringBuffer.substring(18);
                String userdata = "";
                char chrInput;

                // userdata의 글자 깨진거 제거
                for (int i = 0; i < userdataAll.length(); i++) {

                    chrInput = userdataAll.charAt(i); // 입력받은 텍스트에서 문자 하나하나 가져와서 체크

                    if (chrInput >= 0x61 && chrInput <= 0x7A) {
                        // 영문(소문자)
                        userdata += chrInput;
                    } else if (chrInput >= 0x41 && chrInput <= 0x5A) {
                        // 영문(대문자)
                        userdata += chrInput;
                    } else if (chrInput >= 0x30 && chrInput <= 0x39) {
                        // 숫자
                        userdata += chrInput;
                    } else {

                    }
                }
                // 중복되지 않은 주소는 list에 추가
                scanResults.add(result.getDevice());

                // 갱신
                //adapter.notifyDataSetChanged();
                //adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, scanResults);
                if (name == null) {
                    userdata = "";
                }
                adapter.addItem(userdata, name, deviceAddress, "신호 세기 : " + rssi);
                adapter.notifyDataSetChanged();
                //bleListview.setAdapter(adapter);
                
            } catch (Exception e) {
                Log.d(TAG, "scanResult Error : " + e.getMessage());
            }
        }

    };

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "onPause");
        ((Ble_Scan_Listener) activity).onPauseFragment_Ble_Scan();
        stopScan();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
        if (context instanceof Ble_Scan_Listener) {
            selecetBleListener = (Ble_Scan_Listener) context;
            this.activity = (Activity) context;
        } else {
            throw new RuntimeException((context.toString() + " must implement SelectBleListener"));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
        ((Ble_Scan_Listener) activity).onDetachFragment_Ble_Scan();
        selecetBleListener = null;
    }

    public interface Ble_Scan_Listener {
        void onSelectBleDevice(BluetoothDevice device);

        void onPauseFragment_Ble_Scan();

        void onCreateViewFragment_Ble_Scan();

        void onDetachFragment_Ble_Scan();
    }


}
