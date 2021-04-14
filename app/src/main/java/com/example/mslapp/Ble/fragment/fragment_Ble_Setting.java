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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mslapp.Ble.Setting_Dialog.dialogFragment_Ble_Setting_FL_Setting;
import com.example.mslapp.Ble.Setting_Dialog.dialogFragment_Ble_Setting_ID_Setting;
import com.example.mslapp.BleMainActivity;
import com.example.mslapp.R;

import static com.example.mslapp.BleMainActivity.*;
import static com.example.mslapp.BleMainActivity.DATA_REQUEST_STATUS;

public class fragment_Ble_Setting extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Setting";

    TextView readTv, selected_FL, selected_ID;

    Button btn_status, btn_FL_Setting, btn_ID_Setting;

    View view;

    private static final int Ble_Setting_dialog_FL = 1;
    private static final int Ble_Setting_dialog_ID = 2;
    private static final String EXTRA_GREETING_MESSAGE = "message";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "fragment_Ble_Setting onCreateView" + this.getTag());

        view = inflater.inflate(R.layout.ble_fragment_setting, null);

        readTv = view.findViewById(R.id.readData_setting);
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

        readTv.setText(data);
    }

    void btnSetting() {
        btn_status = view.findViewById(R.id.btn_status);
        btn_FL_Setting = view.findViewById(R.id.btn_FL_Setting);
        btn_ID_Setting = view.findViewById(R.id.btn_ID_Setting);


        btn_status.setOnClickListener(v ->
                BlewriteData(DATA_REQUEST_STATUS));
        btn_FL_Setting.setOnClickListener(v ->
                showDialogFragment_FL());

        btn_ID_Setting.setOnClickListener(v ->
                showDialogFragment_ID());
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
