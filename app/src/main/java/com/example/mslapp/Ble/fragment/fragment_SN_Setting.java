package com.example.mslapp.Ble.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mslapp.Ble.Dialog.Setting.dialogFragment_Ble_Setting_Password_Change;
import com.example.mslapp.BleMainActivity;
import com.example.mslapp.R;

import java.util.Objects;

import static com.example.mslapp.Ble.fragment.fragment_Ble_Scan.selectedSerialNum;
import static com.example.mslapp.BleMainActivity.ADMIN_PASSWORD;
import static com.example.mslapp.BleMainActivity.BlewriteData;
import static com.example.mslapp.BleMainActivity.CDS_LAMP_OFF_READY;
import static com.example.mslapp.BleMainActivity.CDS_LAMP_OFF_SETTING;
import static com.example.mslapp.BleMainActivity.CDS_LAMP_ON_READY;
import static com.example.mslapp.BleMainActivity.CDS_LAMP_ON_SETTING;
import static com.example.mslapp.BleMainActivity.CdsFlag;
import static com.example.mslapp.BleMainActivity.DATA_DEVICE_RESET;
import static com.example.mslapp.BleMainActivity.DATA_SIGN_CHECKSUM;
import static com.example.mslapp.BleMainActivity.DATA_SIGN_COMMA;
import static com.example.mslapp.BleMainActivity.DATA_SIGN_START;
import static com.example.mslapp.BleMainActivity.DATA_TYPE_LICMD;
import static com.example.mslapp.BleMainActivity.DATA_TYPE_S;
import static com.example.mslapp.BleMainActivity.DATA_TYPE_SNB;
import static com.example.mslapp.BleMainActivity.SnFlag;
import static com.example.mslapp.BleMainActivity.disconnectGattServer;

public class fragment_SN_Setting extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-SN";

    EditText et_SN_Setting;
    TextView tv_SN_Setting;
    Button btn_SN_Setting;

    View view;
    private Activity activity;

    // 로딩바
    ProgressDialog dialog;

    String sendData;

    // 일단 이대로 두고 직접 사용한다음 수정할 예정.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");
        view = inflater.inflate(R.layout.ble_fragment_sn, null);

        ((SN_Setting_Listener) activity).onCreateViewFragment_SN_Setting();

        dialog = new ProgressDialog(view.getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading");
        dialog.show();

        et_SN_Setting = view.findViewById(R.id.et_SN_Setting_input);
        tv_SN_Setting = view.findViewById(R.id.tv_SN_Setting_SerialNum);
        btn_SN_Setting = view.findViewById(R.id.btn_SN_Setting_change);

        tv_SN_Setting.setText(selectedSerialNum);

        btn_SN_Setting.setOnClickListener(v -> changeSerialNum());

        return view;
    }

    public void readData(String data) {

        Log.d(TAG, "readData! : " + data);

        if (data.contains("$PS,R,")) {
            Log.d(TAG, "readData : $PS,R");
            BlewriteData(ADMIN_PASSWORD);
            if(dialog != null){
                Log.d(TAG, "dialog not null");
                dialog.dismiss();
            }else{
                Log.d(TAG, "dialog null");
            }
        }
    }

    void changeSerialNum() {

        String editData = et_SN_Setting.getText().toString();

        editData = editData.trim();

        //8자리 미만인 경우 공백을 계속 추가
        while (editData.length() < 8) {
            editData += " ";
        }
        //8자리 초과인 경우 9번째 부터 데이터를 버림
        if (editData.length() > 8) {
            editData = editData.substring(0, 8);
        }


        sendData = DATA_SIGN_START
                + DATA_TYPE_LICMD + DATA_SIGN_COMMA
                + DATA_TYPE_S + DATA_SIGN_COMMA
                + DATA_TYPE_SNB + DATA_SIGN_COMMA
                + editData + DATA_SIGN_CHECKSUM;


        mSNSendHandler.sendEmptyMessage(0);

    }

    private final Handler mSNSendHandler = new Handler() {
        public void handleMessage(Message message) {

            try {
                if(sendData.length() < 21){
                    BlewriteData(sendData);
                }else{

                    BlewriteData(sendData.substring(0,20));
                    Thread.sleep(100);
                    BlewriteData(sendData.substring(20));
                }
                Thread.sleep(100);
                BlewriteData(DATA_DEVICE_RESET);
                Thread.sleep(1000);
                disconnectGattServer("fragment_SN_Setting - handleMessage - DATA_DEVICE_RESET");
                ((BleMainActivity) Objects.requireNonNull(getActivity())).fragmentChange("fragment_ble_scan");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
        if (context instanceof SN_Setting_Listener) {
            //selecetBleListener = (Ble_Status_Listener) context;
            this.activity = (Activity) context;
        } else {
            throw new RuntimeException((context.toString() + " must implement SelectBleListener"));
        }
    }

    @Override
    public void onDetach() {
        //SnFlag = false;
        dialog.dismiss();
        ((SN_Setting_Listener) activity).onDetachFragment_SN_Setting();
        super.onDetach();
    }

    public interface SN_Setting_Listener {
        void onCreateViewFragment_SN_Setting();

        void onDetachFragment_SN_Setting();
    }


}
