package com.example.mslapp.RTU.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mslapp.R;

public class fragment_RTU_Setting extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-RTU-Setting";

    TextView readData_tv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "fragment_RTU_Status onCreateView" + this.getTag());
        View view = inflater.inflate(R.layout.rtu_gragment_setting, null);

        readData_tv = view.findViewById(R.id.tv_rtu_setting_readData);

        return view;
    }

    public void readData(String data){
        readData_tv.setText(data);
    }
}
