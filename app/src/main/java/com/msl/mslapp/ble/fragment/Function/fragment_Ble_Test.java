package com.msl.mslapp.ble.fragment.Function;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.msl.mslapp.BleMainActivity;
import com.msl.mslapp.R;

import static com.msl.mslapp.BleMainActivity.BlewriteData;
import static com.msl.mslapp.Public.StringList.DATA_DEVICE_RESET;
import static com.msl.mslapp.Public.StringList.DATA_LAMP_FIXED;
import static com.msl.mslapp.Public.StringList.DATA_LAMP_OFF;
import static com.msl.mslapp.Public.StringList.DATA_LAMP_ON;
import static com.msl.mslapp.BleMainActivity.disconnectGattServer;
import static com.msl.mslapp.BleMainActivity.mBleContext;
import static com.msl.mslapp.Public.StringList.RTU_RESET;

public class fragment_Ble_Test extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Test";

    Button lampOnBtn, lampOffBtn, lampFixedBtn, resetBtn;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "fragment_Ble_Test onCreateView" + this.getTag());

        view = inflater.inflate(R.layout.ble_fragment_test, null);

        buttonSetting();

        return view;
    }

    void buttonSetting() {

        lampOnBtn = view.findViewById(R.id.lampOnBtn);
        lampOnBtn.setOnClickListener(v -> {
            btn_Clicked(1);
            ((BleMainActivity) getActivity()).BlewriteData(DATA_LAMP_ON);
        });
        lampOffBtn = view.findViewById(R.id.lampOffBtn);
        lampOffBtn.setOnClickListener(v -> {
            btn_Clicked(2);
            ((BleMainActivity) getActivity()).BlewriteData(DATA_LAMP_OFF);
        });
        lampFixedBtn = view.findViewById(R.id.lampFixedBtn);
        lampFixedBtn.setOnClickListener(v -> {
            btn_Clicked(3);
            ((BleMainActivity) getActivity()).BlewriteData(DATA_LAMP_FIXED);
        });
        resetBtn = view.findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(v -> {
            btn_Clicked(4);
            mSNSendHandler.sendEmptyMessage(0);
        });

        /*Button testBtn1 = view.findViewById(R.id.test_bt1);
        testBtn1.setOnClickListener(v -> ((BleMainActivity) getActivity()).BlewriteData(CDS_LAMP_ON_READY));
        Button testBtn2 = view.findViewById(R.id.test_bt2);
        testBtn2.setOnClickListener(v -> ((BleMainActivity) getActivity()).BlewriteData(CDS_LAMP_ON_SETTING));
        Button testBtn3 = view.findViewById(R.id.test_bt3);
        testBtn3.setOnClickListener(v -> ((BleMainActivity) getActivity()).BlewriteData(CDS_LAMP_OFF_READY));
        Button testBtn4 = view.findViewById(R.id.test_bt4);
        testBtn4.setOnClickListener(v -> ((BleMainActivity) getActivity()).BlewriteData(CDS_LAMP_OFF_SETTING));
        Button testBtn5 = view.findViewById(R.id.test_bt5);
        testBtn5.setOnClickListener(v -> ((BleMainActivity) getActivity()).BlewriteData(DATA_DEVICE_RESET));*/
    }

    void btn_Clicked(int i){
        switch (i){
            case 1:
                lampOnBtn.setTextColor(ContextCompat.getColor(mBleContext, R.color.white));
                lampOffBtn.setTextColor(ContextCompat.getColor(mBleContext, R.color.ble_scan_list_MSL));
                lampFixedBtn.setTextColor(ContextCompat.getColor(mBleContext, R.color.ble_scan_list_MSL));
                resetBtn.setTextColor(ContextCompat.getColor(mBleContext, R.color.ble_scan_list_MSL));
                lampOnBtn.setBackgroundResource(R.drawable.custom_ble_test_btn_clicked);
                lampOffBtn.setBackgroundResource(R.drawable.custom_ble_test_btn);
                lampFixedBtn.setBackgroundResource(R.drawable.custom_ble_test_btn);
                resetBtn.setBackgroundResource(R.drawable.custom_ble_test_btn);
                break;
            case 2:
                lampOnBtn.setTextColor(ContextCompat.getColor(mBleContext, R.color.ble_scan_list_MSL));
                lampOffBtn.setTextColor(ContextCompat.getColor(mBleContext, R.color.white));
                lampFixedBtn.setTextColor(ContextCompat.getColor(mBleContext, R.color.ble_scan_list_MSL));
                resetBtn.setTextColor(ContextCompat.getColor(mBleContext, R.color.ble_scan_list_MSL));
                lampOnBtn.setBackgroundResource(R.drawable.custom_ble_test_btn);
                lampOffBtn.setBackgroundResource(R.drawable.custom_ble_test_btn_clicked);
                lampFixedBtn.setBackgroundResource(R.drawable.custom_ble_test_btn);
                resetBtn.setBackgroundResource(R.drawable.custom_ble_test_btn);
                break;
            case 3:
                lampOnBtn.setTextColor(ContextCompat.getColor(mBleContext, R.color.ble_scan_list_MSL));
                lampOffBtn.setTextColor(ContextCompat.getColor(mBleContext, R.color.ble_scan_list_MSL));
                lampFixedBtn.setTextColor(ContextCompat.getColor(mBleContext, R.color.white));
                resetBtn.setTextColor(ContextCompat.getColor(mBleContext, R.color.ble_scan_list_MSL));
                lampOnBtn.setBackgroundResource(R.drawable.custom_ble_test_btn);
                lampOffBtn.setBackgroundResource(R.drawable.custom_ble_test_btn);
                lampFixedBtn.setBackgroundResource(R.drawable.custom_ble_test_btn_clicked);
                resetBtn.setBackgroundResource(R.drawable.custom_ble_test_btn);
                break;
            case 4:
                lampOnBtn.setTextColor(ContextCompat.getColor(mBleContext, R.color.ble_scan_list_MSL));
                lampOffBtn.setTextColor(ContextCompat.getColor(mBleContext, R.color.ble_scan_list_MSL));
                lampFixedBtn.setTextColor(ContextCompat.getColor(mBleContext, R.color.ble_scan_list_MSL));
                resetBtn.setTextColor(ContextCompat.getColor(mBleContext, R.color.white));
                lampOnBtn.setBackgroundResource(R.drawable.custom_ble_test_btn);
                lampOffBtn.setBackgroundResource(R.drawable.custom_ble_test_btn);
                lampFixedBtn.setBackgroundResource(R.drawable.custom_ble_test_btn);
                resetBtn.setBackgroundResource(R.drawable.custom_ble_test_btn_clicked);
                break;

        }
    }

    public void readData(String data){
        Log.d(TAG, "fragment_Ble_Test readData!");
    }

    private final Handler mSNSendHandler = new Handler() {
        public void handleMessage(Message message) {

            try {
                // RTU 리셋 명령어 먼저 보낸 후 등명기 리셋 명령
                BlewriteData(RTU_RESET);
                Thread.sleep(500);
                BlewriteData(DATA_DEVICE_RESET);
                Thread.sleep(100);
                disconnectGattServer("fragment_Ble_Test - handleMessage - DATA_DEVICE_RESET");
                ((BleMainActivity) requireActivity()).fragmentChange("fragment_ble_beginning");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
