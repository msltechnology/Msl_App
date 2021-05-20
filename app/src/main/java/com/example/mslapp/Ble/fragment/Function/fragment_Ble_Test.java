package com.example.mslapp.Ble.fragment.Function;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mslapp.BleMainActivity;
import com.example.mslapp.R;

import java.util.Objects;

import static com.example.mslapp.BleMainActivity.BlewriteData;
import static com.example.mslapp.BleMainActivity.DATA_DEVICE_RESET;
import static com.example.mslapp.BleMainActivity.DATA_LAMP_FIXED;
import static com.example.mslapp.BleMainActivity.DATA_LAMP_OFF;
import static com.example.mslapp.BleMainActivity.DATA_LAMP_ON;
import static com.example.mslapp.BleMainActivity.DATA_REQUEST_STATUS;
import static com.example.mslapp.BleMainActivity.disconnectGattServer;

public class fragment_Ble_Test extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Test";

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

        Button lampOnBtn = view.findViewById(R.id.lampOnBtn);
        lampOnBtn.setOnClickListener(v -> ((BleMainActivity) getActivity()).BlewriteData(DATA_LAMP_ON));
        Button lampOffBtn = view.findViewById(R.id.lampOffBtn);
        lampOffBtn.setOnClickListener(v -> ((BleMainActivity) getActivity()).BlewriteData(DATA_LAMP_OFF));
        Button lampFixedBtn = view.findViewById(R.id.lampFixedBtn);
        lampFixedBtn.setOnClickListener(v -> ((BleMainActivity) getActivity()).BlewriteData(DATA_LAMP_FIXED));
        Button resetBtn = view.findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(v -> mSNSendHandler.sendEmptyMessage(0));

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


    public void readData(String data){
        Log.d(TAG, "fragment_Ble_Test readData!");
    }

    private final Handler mSNSendHandler = new Handler() {
        public void handleMessage(Message message) {

            try {
                BlewriteData(DATA_DEVICE_RESET);
                Thread.sleep(100);
                disconnectGattServer("fragment_Ble_Test - handleMessage - DATA_DEVICE_RESET");
                ((BleMainActivity) Objects.requireNonNull(getActivity())).fragmentChange("fragment_ble_beginning");
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
