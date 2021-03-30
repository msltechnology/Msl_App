package com.example.mslapp.Ble.fragment;

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

public class fragment_Ble_Setting extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Setting";

    TextView readTv;

    Button btn_status;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "fragment_Ble_Setting onCreateView" + this.getTag());

        view = inflater.inflate(R.layout.ble_fragment_setting, null);

        readTv = view.findViewById(R.id.readData_setting);

        btnSetting();

        return view;
    }

    public void readData(String data) {
        Log.d(TAG, "fragment_Ble_Setting readData!");

        readTv.setText(data);
    }

    void btnSetting(){
        btn_status = view.findViewById(R.id.btn_status);
        btn_status.setOnClickListener(v ->
                ((BleMainActivity) getActivity()).BlewriteData("LICMD,1,255"));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
