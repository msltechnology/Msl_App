package com.example.mslapp.Ble.Dialog.Status;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mslapp.Ble.fragment.fragment_Ble_Setting;
import com.example.mslapp.Ble.fragment.fragment_Ble_Status;
import com.example.mslapp.BleMainActivity;
import com.example.mslapp.R;

import static com.example.mslapp.BleMainActivity.DATA_REQUEST_BTV;
import static com.example.mslapp.BleMainActivity.DATA_REQUEST_STATUS;
import static com.example.mslapp.BleMainActivity.mBleContext;

public class dialogFragment_Ble_Status_Battery extends DialogFragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Status-Dialog-Battery";

    Button btn_call_status, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0, btn_ce, btn_ok;
    TextView tv_bat_value1, tv_bat_value2, tv_bat_value3, tv_bat_value4, tv_bat_value5, tv_bat_value6;

    fragment_Ble_Status fragment_ble_status;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "dialogFragment_Ble_Status_Battery onCreateView");

        view = inflater.inflate(R.layout.ble_fragment_status_dialog_battery, null);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경 투명화
        getDialog().setCanceledOnTouchOutside(true);

        fragment_ble_status = (fragment_Ble_Status) getParentFragment();

        textSetting();
        btnSetting();

        return view;
    }

    void textSetting(){
        tv_bat_value1 = view.findViewById(R.id.tv_ble_fragment_status_dialog_battery_bat_value1);
        tv_bat_value2 = view.findViewById(R.id.tv_ble_fragment_status_dialog_battery_bat_value2);
        tv_bat_value3 = view.findViewById(R.id.tv_ble_fragment_status_dialog_battery_bat_value3);
        tv_bat_value4 = view.findViewById(R.id.tv_ble_fragment_status_dialog_battery_bat_value4);
        tv_bat_value5 = view.findViewById(R.id.tv_ble_fragment_status_dialog_battery_bat_value5);
        tv_bat_value6 = view.findViewById(R.id.tv_ble_fragment_status_dialog_battery_bat_value6);
    }

    void btnSetting() {
        btn_call_status = view.findViewById(R.id.btn_dialogFragment_Ble_Status_Battery_callStatus);
        btn_call_status.setOnClickListener(v -> ((BleMainActivity) getActivity()).BlewriteData(DATA_REQUEST_BTV));

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();

        // 창크기 지정
        Display display = mBleContext.getDisplay();
        Point size = new Point();
        display.getSize(size);

        final String x = String.valueOf(Math.round((size.x * 0.9)));
        final String y = String.valueOf(Math.round((size.y * 0.35)));
        int dialogWidth = Integer.parseInt(x);
        int dialogHeight = Integer.parseInt(y);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        // 창크기 지정

    }
}