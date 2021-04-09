package com.example.mslapp.Ble.Setting_Dialog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mslapp.Ble.fragment.fragment_Ble_Status;
import com.example.mslapp.R;

public class fragment_Ble_Setting_Dialog_FL extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Setting-Dialog-FL";

    View view;

    dialogFragment_Ble_Setting_FL_Setting dialogFragment_Ble_Setting_FL_Setting_fragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "fragment_Ble_Setting_Dialog_FL onCreateView");

        view = inflater.inflate(R.layout.ble_fragment_setting_dialog_fl_setting_fl_select, null);

        dialogFragment_Ble_Setting_FL_Setting_fragment = (dialogFragment_Ble_Setting_FL_Setting) getParentFragment();

        btnSetting();

        return view;
    }

    void btnSetting() {
        Button btn_fl_1 = view.findViewById(R.id.btn_dialog_setting_FL_1);
        Button btn_fl_2 = view.findViewById(R.id.btn_dialog_setting_FL_2);
        Button btn_fl_3 = view.findViewById(R.id.btn_dialog_setting_FL_3);
        Button btn_fl_4 = view.findViewById(R.id.btn_dialog_setting_FL_4);
        Button btn_fl_5 = view.findViewById(R.id.btn_dialog_setting_FL_5);
        Button btn_fl_6 = view.findViewById(R.id.btn_dialog_setting_FL_6);
        Button btn_fl_9 = view.findViewById(R.id.btn_dialog_setting_FL_9);

        btn_fl_1.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(2,"1"));
        btn_fl_2.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(2,"2"));
        btn_fl_3.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(2,"3"));
        btn_fl_4.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(2,"4"));
        btn_fl_5.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(2,"5"));
        btn_fl_6.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(2,"6"));
        btn_fl_9.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(2,"9"));
    }


}
