package com.example.mslapp.Ble.Setting_Dialog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mslapp.R;

public class fragment_Ble_Setting_Dialog_FL extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Setting-Dialog-FL";

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "fragment_Ble_Setting_Dialog_FL onCreateView");

        view = inflater.inflate(R.layout.ble_fragment_setting_dialog_fl_setting_fl_select, null);

        return view;
    }
}
