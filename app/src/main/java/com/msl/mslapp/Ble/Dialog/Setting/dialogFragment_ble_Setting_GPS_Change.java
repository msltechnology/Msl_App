package com.msl.mslapp.Ble.Dialog.Setting;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.msl.mslapp.R;

import static com.msl.mslapp.Ble.fragment.Function.fragment_Ble_Function.gps_status;
import static com.msl.mslapp.BleMainActivity.BlewriteData;
import static com.msl.mslapp.BleMainActivity.GPS_SET_OFF;
import static com.msl.mslapp.BleMainActivity.GPS_SET_ON;
import static com.msl.mslapp.BleMainActivity.mBleContext;

public class dialogFragment_ble_Setting_GPS_Change extends DialogFragment {

    // 로그 이름 용
    public static final String TAG = "Msl-RTU-Setting-Dialog-GMT_Change";

    Button btn_On, btn_Off;

    View view;

    Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "dialogFragment_ble_Setting_GPS_Change onCreateView");

        view = inflater.inflate(R.layout.ble_fragment_setting_dialog_gps_change, null);

        mContext = mBleContext;

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btn_Setting();

        if(gps_status){
            btn_On.setTextColor(Color.WHITE);
            btn_On.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_ble_setting_gps_on_clicked));
            btn_Off.setTextColor(Color.BLACK);
            btn_Off.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_ble_setting_gps_off));
        }else{
            btn_On.setTextColor(Color.BLACK);
            btn_On.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_ble_setting_gps_on));
            btn_Off.setTextColor(Color.WHITE);
            btn_Off.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_ble_setting_gps_off_clicked));
        }

        return view;
    }

    void btn_Setting() {
        btn_On = view.findViewById(R.id.btn_GPS_On);
        btn_Off = view.findViewById(R.id.btn_GPS_Off);
        btn_On.setOnClickListener(v -> {
            Toast.makeText(mContext, "GPS 상시 수신하도록 합니다.", Toast.LENGTH_SHORT).show();
            BlewriteData(GPS_SET_ON);
            btn_On.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_ble_setting_gps_on_clicked));
            btn_Off.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_ble_setting_gps_off));
            dismiss();
        });

        btn_Off.setOnClickListener(v -> {
            Toast.makeText(mContext, "등명기 점등시에만 GPS 수신하도록 합니다..", Toast.LENGTH_SHORT).show();
            BlewriteData(GPS_SET_OFF);
            btn_On.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_ble_setting_gps_on));
            btn_Off.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_ble_setting_gps_off_clicked));
            dismiss();
        });
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
        WindowManager wm;
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        final String x = String.valueOf(Math.round((size.x * 0.8)));
        final String y = String.valueOf(Math.round((size.y * 0.2)));
        int dialogWidth = Integer.parseInt(x);
        int dialogHeight = Integer.parseInt(y);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        // 창크기 지정
    }


}
