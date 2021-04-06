package com.example.mslapp.Ble.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

import static com.example.mslapp.BleMainActivity.CDS_LAMP_OFF_READY;
import static com.example.mslapp.BleMainActivity.CDS_LAMP_OFF_SETTING;
import static com.example.mslapp.BleMainActivity.CDS_LAMP_ON_READY;
import static com.example.mslapp.BleMainActivity.CDS_LAMP_ON_SETTING;
import static com.example.mslapp.BleMainActivity.DATA_DEVICE_RESET;
import static com.example.mslapp.BleMainActivity.DATA_LAMP_FIXED;
import static com.example.mslapp.BleMainActivity.DATA_LAMP_OFF;
import static com.example.mslapp.BleMainActivity.DATA_LAMP_ON;
import static com.example.mslapp.BleMainActivity.DATA_REQUEST_STATUS;

public class fragment_Ble_Status extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Status";

    TextView readDataTv;

    View view;


    // 상위Activity 에게 데이터 주는 용도
    private Ble_Status_Listener selecetBleListener;
    private Activity activity;


    public fragment_Ble_Status() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "fragment_Ble_Status onCreateView" + this.getTag());
        view = inflater.inflate(R.layout.ble_fragment_status, null);


        ((Ble_Status_Listener) activity).onCreateViewFragment_Ble_Status();

        buttonSetting();

        readDataTv = view.findViewById(R.id.tv_bt_read);


        // 최초 1회 정보 요청
        ((BleMainActivity) getActivity()).BlewriteData(DATA_REQUEST_STATUS);

        return view;
    }

    void buttonSetting() {

        Button statusBtn = view.findViewById(R.id.statusBtn);
        statusBtn.setOnClickListener(v -> ((BleMainActivity) getActivity()).BlewriteData(DATA_REQUEST_STATUS));
        Button lampOnBtn = view.findViewById(R.id.lampOnBtn);
        lampOnBtn.setOnClickListener(v -> ((BleMainActivity) getActivity()).BlewriteData(DATA_LAMP_ON));
        Button lampOffBtn = view.findViewById(R.id.lampOffBtn);
        lampOffBtn.setOnClickListener(v -> ((BleMainActivity) getActivity()).BlewriteData(DATA_LAMP_OFF));
        Button lampFixedBtn = view.findViewById(R.id.lampFixedBtn);
        lampFixedBtn.setOnClickListener(v -> ((BleMainActivity) getActivity()).BlewriteData(DATA_LAMP_FIXED));
        Button resetBtn = view.findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(v -> ((BleMainActivity) getActivity()).BlewriteData(DATA_DEVICE_RESET));

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


    public void readData(String data) {
        readDataTv.setText(data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
        if (context instanceof Ble_Status_Listener) {
            selecetBleListener = (Ble_Status_Listener) context;
            this.activity = (Activity) context;
        } else {
            throw new RuntimeException((context.toString() + " must implement SelectBleListener"));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
        ((Ble_Status_Listener) activity).onDetachFragment_Ble_Status();
        selecetBleListener = null;
    }

    public interface Ble_Status_Listener {
        void onCreateViewFragment_Ble_Status();

        void onDetachFragment_Ble_Status();
    }


}
