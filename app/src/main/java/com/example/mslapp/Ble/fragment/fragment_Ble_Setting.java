package com.example.mslapp.Ble.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mslapp.Ble.Dialog.Setting.dialogFragment_Ble_Setting_FL_Setting;
import com.example.mslapp.Ble.Dialog.Setting.dialogFragment_Ble_Setting_ID_Setting;
import com.example.mslapp.Ble.Dialog.Setting.dialogFragment_Ble_Setting_Password_Change;
import com.example.mslapp.R;

import static com.example.mslapp.Ble.fragment.fragment_Ble_Password.psEncryptionTable;
import static com.example.mslapp.Ble.fragment.fragment_Ble_Password.readPassword;
import static com.example.mslapp.BleMainActivity.*;
import static com.example.mslapp.BleMainActivity.DATA_REQUEST_STATUS;

public class fragment_Ble_Setting extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Setting";

    TextView selected_FL, selected_ID;

    Button btn_status, btn_FL_Setting, btn_ID_Setting, btn_Password_Change, btn_GPS_ON, btn_GPS_OFF;

    View view;

    private static final int Ble_Setting_dialog_FL = 1;
    private static final int Ble_Setting_dialog_ID = 2;
    private static final String EXTRA_GREETING_MESSAGE = "message";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "fragment_Ble_Setting onCreateView" + this.getTag());

        view = inflater.inflate(R.layout.ble_fragment_setting, null);

        selected_FL = view.findViewById(R.id.tv_ble_framgment_setting_selected_FL);
        selected_ID = view.findViewById(R.id.tv_ble_framgment_setting_selected_ID);

        btnSetting();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void readData(String data) {
        Log.d(TAG, "fragment_Ble_Setting readData!");

        // 비밀번호 변경 시 확인
        String passwordchangeCheck = "$PS,A," + psEncryptionTable(readPassword);

        if(data.contains(passwordchangeCheck))
            Toast.makeText(mBleContext, "Password Change Success", Toast.LENGTH_SHORT).show();

        if (data.substring(1, 6).contains("LISTS")) {
            if (data.startsWith("S", 7)) {

            } else {
                Log.d(TAG, "readData 데이터 읽기");
                String[] data_arr = data.split(",");
                Log.d(TAG, "data_arr length" + data_arr.length);

                selected_ID.setText(data_arr[1]);
                selected_FL.setText(data_arr[6]);
            }
        }
    }

    void btnSetting() {
        btn_status = view.findViewById(R.id.btn_status);
        btn_FL_Setting = view.findViewById(R.id.btn_FL_Setting);
        btn_ID_Setting = view.findViewById(R.id.btn_ID_Setting);
        btn_Password_Change = view.findViewById(R.id.btn_Password_Change);
        btn_GPS_ON = view.findViewById(R.id.btn_GPS_On);
        btn_GPS_OFF = view.findViewById(R.id.btn_GPS_Off);


        btn_status.setOnClickListener(v ->
                BlewriteData(DATA_REQUEST_STATUS));
        btn_FL_Setting.setOnClickListener(v ->
                showDialogFragment_FL());
        btn_ID_Setting.setOnClickListener(v ->
                showDialogFragment_ID());
        btn_Password_Change.setOnClickListener(v ->
                showPasswordChangeDialog());
        btn_GPS_ON.setOnClickListener(v -> {
            btn_GPS_ON.setBackground(ContextCompat.getDrawable(mBleContext,R.drawable.custom_ble_setting_gps_on_clicked));
            btn_GPS_OFF.setBackground(ContextCompat.getDrawable(mBleContext,R.drawable.custom_ble_setting_gps_off));
        });
        btn_GPS_OFF.setOnClickListener(v -> {
            btn_GPS_ON.setBackground(ContextCompat.getDrawable(mBleContext,R.drawable.custom_ble_setting_gps_on));
            btn_GPS_OFF.setBackground(ContextCompat.getDrawable(mBleContext,R.drawable.custom_ble_setting_gps_off_clicked));
        });
    }

    public void setSelected_FL(String selected_fl){
        Log.d(TAG, "fragment_Ble_Setting setSelected_FL : " + selected_fl);
        selected_FL.setText(selected_fl);
    }

    public void setSelected_ID(String selected_fl){
        Log.d(TAG, "fragment_Ble_Setting setSelected_ID : " + selected_fl);
        selected_ID.setText(selected_fl);
    }

    private void showDialogFragment_FL() {

        FragmentManager fm = this.getChildFragmentManager();
        dialogFragment_Ble_Setting_FL_Setting setting_FL_DialogFragment = new dialogFragment_Ble_Setting_FL_Setting();
        setting_FL_DialogFragment.show(fm, "fragment_setting_dialog_FL");
    }

    private void showDialogFragment_ID() {

        FragmentManager fm = this.getChildFragmentManager();
        dialogFragment_Ble_Setting_ID_Setting setting_ID_DialogFragment = new dialogFragment_Ble_Setting_ID_Setting();
        setting_ID_DialogFragment.show(fm, "fragment_setting_dialog_ID");
    }
    private void showPasswordChangeDialog() {

        FragmentManager fm = this.getChildFragmentManager();
        dialogFragment_Ble_Setting_Password_Change setting_PasswordChange_DialogFragment = new dialogFragment_Ble_Setting_Password_Change();
        setting_PasswordChange_DialogFragment.show(fm, "fragment_setting_dialog_Password");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( resultCode != Activity.RESULT_OK ) {
            Log.d(TAG, "fragment_Ble_Setting onActivityResult RESULT_OK : Fail ");
            return;
        }
        Log.d(TAG, "fragment_Ble_Setting onActivityResult RESULT_OK : Success ");
        if( requestCode == Ble_Setting_dialog_FL ) {
            Log.d(TAG, "fragment_Ble_Setting onActivityResult Code : Ble_Setting_dialog_FL ");
            String greeting = data.getStringExtra(EXTRA_GREETING_MESSAGE);
            setSelected_FL(greeting);
        }else if( requestCode == Ble_Setting_dialog_ID ){
            Log.d(TAG, "fragment_Ble_Setting onActivityResult Code : Ble_Setting_dialog_ID ");
            String greeting = data.getStringExtra(EXTRA_GREETING_MESSAGE);
            setSelected_ID(greeting);
        }
    }

    public static Intent newIntent(String message) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_GREETING_MESSAGE, message);
        return intent;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}
