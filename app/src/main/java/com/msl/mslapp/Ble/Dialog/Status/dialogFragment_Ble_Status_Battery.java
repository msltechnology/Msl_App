package com.msl.mslapp.Ble.Dialog.Status;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.msl.mslapp.Ble.fragment.Function.fragment_Ble_Status;
import com.msl.mslapp.R;
import com.msl.mslapp.databinding.BleFragmentStatusDialogBatteryBinding;
import com.msl.mslapp.TestViewModel;

import static com.msl.mslapp.BleMainActivity.BlewriteData;
import static com.msl.mslapp.Public.StringList.DATA_REQUEST_BTV;
import static com.msl.mslapp.BleMainActivity.mBleContext;

public class dialogFragment_Ble_Status_Battery extends DialogFragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Status-Dialog-Battery";

    Button btn_call_status;
    public static TextView tv_ble_status_bat_value1, tv_ble_status_bat_value2, tv_ble_status_bat_value3, tv_ble_status_bat_value4, tv_ble_status_bat_value5, tv_ble_status_bat_value6;
    public static ImageView iv_ble_status_bat1, iv_ble_status_bat2, iv_ble_status_bat3, iv_ble_status_bat4, iv_ble_status_bat5, iv_ble_status_bat6;

    fragment_Ble_Status fragment_ble_status;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "dialogFragment_Ble_Status_Battery onCreateView");

        view = inflater.inflate(R.layout.ble_fragment_status_dialog_battery, null);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경 투명화
        getDialog().setCanceledOnTouchOutside(true);

        fragment_ble_status = (fragment_Ble_Status) getParentFragment();

        textSetting();
        btnSetting();
        imageViewSetting();
        // 최초 1회 데이터 요청하기
        BlewriteData(DATA_REQUEST_BTV);

        return view;
    }

    void textSetting(){
        tv_ble_status_bat_value1 = view.findViewById(R.id.tv_ble_fragment_status_dialog_battery_bat_value1);
        tv_ble_status_bat_value2 = view.findViewById(R.id.tv_ble_fragment_status_dialog_battery_bat_value2);
        tv_ble_status_bat_value3 = view.findViewById(R.id.tv_ble_fragment_status_dialog_battery_bat_value3);
        tv_ble_status_bat_value4 = view.findViewById(R.id.tv_ble_fragment_status_dialog_battery_bat_value4);
        tv_ble_status_bat_value5 = view.findViewById(R.id.tv_ble_fragment_status_dialog_battery_bat_value5);
        tv_ble_status_bat_value6 = view.findViewById(R.id.tv_ble_fragment_status_dialog_battery_bat_value6);
    }

    void imageViewSetting(){
        iv_ble_status_bat1 = view.findViewById(R.id.iv_ble_fragment_status_dialog_battery_bat1);
        iv_ble_status_bat2 = view.findViewById(R.id.iv_ble_fragment_status_dialog_battery_bat2);
        iv_ble_status_bat3 = view.findViewById(R.id.iv_ble_fragment_status_dialog_battery_bat3);
        iv_ble_status_bat4 = view.findViewById(R.id.iv_ble_fragment_status_dialog_battery_bat4);
        iv_ble_status_bat5 = view.findViewById(R.id.iv_ble_fragment_status_dialog_battery_bat5);
        iv_ble_status_bat6 = view.findViewById(R.id.iv_ble_fragment_status_dialog_battery_bat6);
    }

    void btnSetting() {
        btn_call_status = view.findViewById(R.id.btn_dialogFragment_Ble_Status_Battery_callStatus);
        btn_call_status.setOnClickListener(v -> BlewriteData(DATA_REQUEST_BTV));
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

        final String x = String.valueOf(Math.round((size.x * 0.8)));
        final String y = String.valueOf(Math.round((size.y * 0.8)));
        int dialogWidth = Integer.parseInt(x);
        int dialogHeight = Integer.parseInt(y);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        // 창크기 지정

    }
}