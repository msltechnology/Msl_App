package com.msl.mslapp.ble.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.fragment.app.FragmentManager;

import com.msl.mslapp.BleMainActivity;
import com.msl.mslapp.R;
import com.msl.mslapp.ble.Dialog.setting.dialogFragment_ble_Setting_ModeSelect;

import static com.msl.mslapp.BleMainActivity.readPassword;
import static com.msl.mslapp.ble.fragment.fragment_Ble_Password.psDecryptionTable;
import static com.msl.mslapp.ble.fragment.fragment_Ble_Scan.selectedSerialNum;
import static com.msl.mslapp.Public.StringList.ADMIN_PASSWORD;
import static com.msl.mslapp.BleMainActivity.BlewriteData;
import static com.msl.mslapp.Public.StringList.DATA_DEVICE_RESET;
import static com.msl.mslapp.Public.StringList.DATA_SIGN_CHECKSUM;
import static com.msl.mslapp.Public.StringList.DATA_SIGN_COMMA;
import static com.msl.mslapp.Public.StringList.DATA_SIGN_START;
import static com.msl.mslapp.Public.StringList.DATA_TYPE_LICMD;
import static com.msl.mslapp.Public.StringList.DATA_TYPE_S;
import static com.msl.mslapp.Public.StringList.DATA_TYPE_SNB;
import static com.msl.mslapp.BleMainActivity.disconnectGattServer;
import static com.msl.mslapp.BleMainActivity.navigation_icon_Change;

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

        navigation_icon_Change("function");

        return view;
    }

    Handler postHandler = new Handler(Looper.getMainLooper());
    public void readData(String data) {

        Log.d(TAG, "readData! : " + data);

        if (data.contains("$PS,R,")) {
            Log.d(TAG, "readData : $PS,R");
            readPassword = data.substring(6, 11);

            String deCrypPassword = psDecryptionTable(readPassword);
            BlewriteData("$PS,A," + deCrypPassword + "*");

            postHandler.postDelayed(() -> {
                BlewriteData(ADMIN_PASSWORD);
            }, 200);
            postHandler.postDelayed(() -> {
                BlewriteData("$PS,A," + readPassword + "*");
            }, 200);
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
                BlewriteData(sendData);
                Thread.sleep(100);
                BlewriteData(DATA_DEVICE_RESET);
                Thread.sleep(100);
                disconnectGattServer("fragment_SN_Setting - handleMessage - DATA_DEVICE_RESET");
                ((BleMainActivity) requireActivity()).fragmentChange("fragment_ble_beginning");
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
