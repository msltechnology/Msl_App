package com.msl.mslapp.ble.Dialog.setting;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.msl.mslapp.R;

public class fragment_Ble_Setting_Dialog_Second extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Setting-Dialog-Second";

    dialogFragment_Ble_Setting_FL_Setting dialogFragment_Ble_Setting_FL_Setting_fragment;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "fragment_Ble_Setting_Dialog_Second onCreateView");

        view = inflater.inflate(R.layout.ble_fragment_setting_dialog_fl_setting_second_select, null);

        dialogFragment_Ble_Setting_FL_Setting_fragment = (dialogFragment_Ble_Setting_FL_Setting) getParentFragment();

        btnSetting();

        return view;
    }

    void btnSetting(){
        Button btn_second_1 = view.findViewById(R.id.btn_dialog_setting_second_1);
        Button btn_second_2 = view.findViewById(R.id.btn_dialog_setting_second_2);
        Button btn_second_3 = view.findViewById(R.id.btn_dialog_setting_second_3);
        Button btn_second_4 = view.findViewById(R.id.btn_dialog_setting_second_4);
        Button btn_second_5 = view.findViewById(R.id.btn_dialog_setting_second_5);
        Button btn_second_6 = view.findViewById(R.id.btn_dialog_setting_second_6);
        Button btn_second_7 = view.findViewById(R.id.btn_dialog_setting_second_7);
        Button btn_second_8 = view.findViewById(R.id.btn_dialog_setting_second_8);
        Button btn_second_9 = view.findViewById(R.id.btn_dialog_setting_second_9);
        Button btn_second_10 = view.findViewById(R.id.btn_dialog_setting_second_10);
        Button btn_second_12 = view.findViewById(R.id.btn_dialog_setting_second_12);
        Button btn_second_15 = view.findViewById(R.id.btn_dialog_setting_second_15);
        Button btn_second_16 = view.findViewById(R.id.btn_dialog_setting_second_16);
        Button btn_second_20 = view.findViewById(R.id.btn_dialog_setting_second_20);
        Button btn_second_25 = view.findViewById(R.id.btn_dialog_setting_second_25);
        Button btn_second_30 = view.findViewById(R.id.btn_dialog_setting_second_30);
        Button btn_second_0_5 = view.findViewById(R.id.btn_dialog_setting_second_0_5);
        Button btn_second_0_6 = view.findViewById(R.id.btn_dialog_setting_second_0_6);
        Button btn_second_0_75 = view.findViewById(R.id.btn_dialog_setting_second_0_75);
        Button btn_second_1_2 = view.findViewById(R.id.btn_dialog_setting_second_1_2);
        Button btn_second_1_25 = view.findViewById(R.id.btn_dialog_setting_second_1_25);
        Button btn_second_1_5 = view.findViewById(R.id.btn_dialog_setting_second_1_5);
        Button btn_second_2_5 = view.findViewById(R.id.btn_dialog_setting_second_2_5);
        Button btn_second_3_5 = view.findViewById(R.id.btn_dialog_setting_second_3_5);
        Button btn_second_4_3 = view.findViewById(R.id.btn_dialog_setting_second_4_3);
        Button btn_second_4_5 = view.findViewById(R.id.btn_dialog_setting_second_4_5);
        Button btn_second_5_5 = view.findViewById(R.id.btn_dialog_setting_second_5_5);
        Button btn_second_15_75 = view.findViewById(R.id.btn_dialog_setting_second_15_75);

        btn_second_1.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"1"));
        btn_second_2.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"2"));
        btn_second_3.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"3"));
        btn_second_4.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"4"));
        btn_second_5.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"5"));
        btn_second_6.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"6"));
        btn_second_7.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"7"));
        btn_second_8.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"8"));
        btn_second_9.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"9"));
        btn_second_10.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"10"));
        btn_second_12.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"12"));
        btn_second_15.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"15"));
        btn_second_16.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"16"));
        btn_second_20.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"20"));
        btn_second_25.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"25"));
        btn_second_30.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"30"));
        btn_second_0_5.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"0.5"));
        btn_second_0_6.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"0.6"));
        btn_second_0_75.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"0.75"));
        btn_second_1_2.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"1.2"));
        btn_second_1_25.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"1.25"));
        btn_second_1_5.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"1.5"));
        btn_second_2_5.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"2.5"));
        btn_second_3_5.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"3.5"));
        btn_second_4_3.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"4.3"));
        btn_second_4_5.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"4.5"));
        btn_second_5_5.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"5.5"));
        btn_second_15_75.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(1,"15.75"));

        switch (dialogFragment_Ble_Setting_FL_Setting.select_FL) {
            case "1":

                break;
        }





    }

    void btnTextColorSet(Button... btn) {
        for (int i = 0; i < btn.length; i++) {
            btn[i].setTextColor(Color.BLACK);
        }
    }



}
