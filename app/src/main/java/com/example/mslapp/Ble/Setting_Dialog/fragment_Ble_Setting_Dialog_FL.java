package com.example.mslapp.Ble.Setting_Dialog;

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
        Button btn_fl_2_1 = view.findViewById(R.id.btn_dialog_setting_FL_2_1);
        Button btn_fl_ISO = view.findViewById(R.id.btn_dialog_setting_FL_ISO);
        Button btn_fl_LFL = view.findViewById(R.id.btn_dialog_setting_FL_LFL);
        Button btn_fl_FFL = view.findViewById(R.id.btn_dialog_setting_FL_FFL);
        Button btn_fl_MO = view.findViewById(R.id.btn_dialog_setting_FL_MO);
        Button btn_fl_OC = view.findViewById(R.id.btn_dialog_setting_FL_OC);
        Button btn_fl_Q = view.findViewById(R.id.btn_dialog_setting_FL_Q);
        Button btn_fl_VQ = view.findViewById(R.id.btn_dialog_setting_FL_VQ);
        Button btn_fl_AI = view.findViewById(R.id.btn_dialog_setting_FL_AI);

        switch (dialogFragment_Ble_Setting_FL_Setting.select_Sec) {
            case "1":
                btnTextColorSet(btn_fl_Q);
                break;
            case "2":
                btnTextColorSet(btn_fl_1);
                break;
            case "3":
                btnTextColorSet(btn_fl_1, btn_fl_ISO, btn_fl_OC, btn_fl_AI);
                break;
            case "4":
                btnTextColorSet(btn_fl_1, btn_fl_2, btn_fl_ISO, btn_fl_FFL, btn_fl_OC, btn_fl_VQ, btn_fl_AI);
                break;
            case "5":
                btnTextColorSet(btn_fl_1, btn_fl_2, btn_fl_ISO, btn_fl_LFL, btn_fl_OC, btn_fl_Q, btn_fl_VQ);
                break;
            case "6":
                btnTextColorSet(btn_fl_1, btn_fl_2, btn_fl_3, btn_fl_2_1, btn_fl_ISO, btn_fl_LFL, btn_fl_MO, btn_fl_OC, btn_fl_Q);
                break;
            case "7":
                btnTextColorSet(btn_fl_1, btn_fl_2, btn_fl_3, btn_fl_MO, btn_fl_OC, btn_fl_Q);
                break;
            case "8":
                btnTextColorSet(btn_fl_1, btn_fl_2, btn_fl_3, btn_fl_4, btn_fl_ISO, btn_fl_LFL, btn_fl_MO, btn_fl_OC, btn_fl_VQ);
                break;
            case "9":
                btnTextColorSet(btn_fl_1, btn_fl_3, btn_fl_OC);
                break;
            case "10":
                btnTextColorSet(btn_fl_1, btn_fl_2, btn_fl_3, btn_fl_4, btn_fl_2_1, btn_fl_ISO, btn_fl_LFL, btn_fl_MO, btn_fl_OC, btn_fl_Q, btn_fl_VQ);
                break;
            case "12":
                btnTextColorSet(btn_fl_1, btn_fl_2, btn_fl_3, btn_fl_4, btn_fl_6, btn_fl_2_1, btn_fl_LFL, btn_fl_Q);
                break;
            case "15":
                btnTextColorSet(btn_fl_1, btn_fl_2, btn_fl_3, btn_fl_4, btn_fl_6, btn_fl_2_1, btn_fl_LFL, btn_fl_MO, btn_fl_OC, btn_fl_Q, btn_fl_VQ);
                break;
            case "16":
                btnTextColorSet(btn_fl_4, btn_fl_2_1);
                break;
            case "20":
                btnTextColorSet(btn_fl_2, btn_fl_3, btn_fl_4, btn_fl_5, btn_fl_LFL, btn_fl_Q);
                break;
            case "25":
                btnTextColorSet(btn_fl_2);
                break;
            case "30":
                btnTextColorSet(btn_fl_1, btn_fl_4, btn_fl_MO);
                break;
            case "0.5":
                btnTextColorSet(btn_fl_VQ);
                break;
            case "0.6":
                btnTextColorSet(btn_fl_VQ);
                break;
            case "0.75":
                btnTextColorSet(btn_fl_VQ);
                break;
            case "1.2":
                btnTextColorSet(btn_fl_Q);
                break;
            case "1.25":
                btnTextColorSet(btn_fl_OC);
                break;
            case "1.5":
                btnTextColorSet(btn_fl_1);
                break;
            case "2.5":
                btnTextColorSet(btn_fl_1);
                break;
            case "3.5":
                btnTextColorSet(btn_fl_OC);
                break;
            case "4.3":
                btnTextColorSet(btn_fl_1);
                break;
            case "4.5":
                btnTextColorSet(btn_fl_2);
                break;
            case "5.5":
                btnTextColorSet(btn_fl_2);
                break;
            case "15.75":
                btnTextColorSet(btn_fl_MO);
                break;
            default:
                btnTextColorSet(btn_fl_1, btn_fl_2, btn_fl_3, btn_fl_4, btn_fl_5, btn_fl_6, btn_fl_2_1, btn_fl_ISO, btn_fl_LFL, btn_fl_FFL, btn_fl_MO, btn_fl_OC, btn_fl_Q, btn_fl_VQ, btn_fl_AI);
                break;
        }

        btn_fl_1.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(2, "1"));
        btn_fl_2.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(2, "2"));
        btn_fl_3.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(2, "3"));
        btn_fl_4.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(2, "4"));
        btn_fl_5.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(2, "5"));
        btn_fl_6.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(2, "6"));
        btn_fl_2_1.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(2, "2_1"));
        btn_fl_ISO.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(2, "ISO"));
        btn_fl_LFL.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(2, "LFL"));
        btn_fl_FFL.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(2, "FFL"));
        btn_fl_MO.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(2, "MO"));
        btn_fl_OC.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(2, "OC"));
        btn_fl_Q.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(2, "Q"));
        btn_fl_VQ.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(2, "VQ"));
        btn_fl_AI.setOnClickListener(v -> dialogFragment_Ble_Setting_FL_Setting_fragment.btnSetText(2, "AI"));
    }

    void btnTextColorSet(Button... btn) {
        for (int i = 0; i < btn.length; i++) {
            btn[i].setTextColor(Color.BLACK);
        }
    }


}
