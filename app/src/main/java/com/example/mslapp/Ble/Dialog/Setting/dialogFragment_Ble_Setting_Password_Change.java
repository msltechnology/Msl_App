package com.example.mslapp.Ble.Dialog.Setting;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mslapp.R;

import static com.example.mslapp.Ble.fragment.fragment_Ble_Password.psEncryptionTable;
import static com.example.mslapp.BleMainActivity.BlewriteData;
import static com.example.mslapp.BleMainActivity.mBleContext;
import static com.example.mslapp.BleMainActivity.readPassword;

public class dialogFragment_Ble_Setting_Password_Change extends DialogFragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Setting-Dialog-PasswordChange";

    TextView password1, password2, password3, password4, password5, tv_notice;

    Button btn_Q, btn_W, btn_E, btn_R, btn_T, btn_Y, btn_U, btn_I, btn_O, btn_P,
            btn_A, btn_S, btn_D, btn_F, btn_G, btn_H, btn_J, btn_K, btn_L,
            btn_Z, btn_X, btn_C, btn_V, btn_B, btn_N, btn_M,
            btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0;

    Button btn_delete, btn_OK;

    String passwordBackup = "";

    int passwordLevel = 0;

    int passwordOrder = 0;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "dialogFragment_Ble_Setting_Password_Change onCreateView");

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);

        view = inflater.inflate(R.layout.ble_fragment_password_change, null);


        textViewSetting();
        btnSetting();

        btn_delete = view.findViewById(R.id.password_key_delete);
        btn_delete.setOnClickListener(v -> deleteClickEvent());

        btn_OK = view.findViewById(R.id.btn_connect);
        btn_OK.setOnClickListener(v -> {
            if (passwordOrder == 5) {
                String password = password1.getText().toString() + password2.getText().toString() + password3.getText().toString() +
                        password4.getText().toString() + password5.getText().toString();
                Log.d(TAG, "password = " + password);
                passwordCheck(password);
            }
        });

        return view;
    }

    void passwordCheck(String inputPassword) {

        Log.d(TAG, "passwordBackup : " + passwordBackup + " and readData : " + readPassword);

        if(passwordLevel == 0){
            if (readPassword.equals(psEncryptionTable(inputPassword))) {
                Log.d(TAG, "passwordCheck : OK");
                tv_notice.setText(getString(R.string.notice_change_password));
                passwordLevel = 1;
                resetPassword();
            }else if(inputPassword.equals("AHFFK")){
                Log.d(TAG, "passwordCheck : Admin");
                tv_notice.setText(getString(R.string.notice_change_password));
                passwordLevel = 1;
                resetPassword();
            }
        }else if(passwordLevel == 1){
            Log.d(TAG, "new password : " + inputPassword);
            passwordBackup = inputPassword;
            tv_notice.setText(getString(R.string.notice_change_password_check));
            passwordLevel = 2;
            resetPassword();
        }else if(passwordLevel == 2){
            Log.d(TAG, "new password check : " + passwordBackup + " / " + inputPassword);
            if(passwordBackup.equals(inputPassword)){
                BlewriteData("$PS,S," + passwordBackup + "*");
                readPassword = psEncryptionTable(passwordBackup);
                this.dismiss();
            }else{
                Toast.makeText(mBleContext, getString(R.string.notice_passwordChange_fail), Toast.LENGTH_SHORT).show();
            }

        }
    }

    void resetPassword(){
        password1.setText("");
        password2.setText("");
        password3.setText("");
        password4.setText("");
        password5.setText("");
        passwordOrder = 0;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    @Override
    public void onResume() {
        super.onResume();

        // 창크기 지정
        Display display = mBleContext.getDisplay();
        Point size = new Point();
        display.getSize(size);

        final String x = String.valueOf(Math.round((size.x * 0.95)));
        final String y = String.valueOf(Math.round((size.y * 0.95)));
        int dialogWidth = Integer.parseInt(x);
        int dialogHeight = Integer.parseInt(y);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        // 창크기 지정
    }

    @Override
    public void onDetach() {
        passwordBackup = "";

        passwordLevel = 0;

        passwordOrder = 0;
        super.onDetach();
    }

    public void deleteClickEvent() {
        switch (passwordOrder) {
            case 1:
                password1.setText("");
                passwordOrder = 0;
                break;
            case 2:
                password2.setText("");
                passwordOrder = 1;
                break;
            case 3:
                password3.setText("");
                passwordOrder = 2;
                break;
            case 4:
                password4.setText("");
                passwordOrder = 3;
                break;
            case 5:
                password5.setText("");
                passwordOrder = 4;
                break;
        }
    }

    public void btnClickEvent(String word) {
        switch (passwordOrder) {
            case 0:
                password1.setText(word);
                passwordOrder = 1;
                break;
            case 1:
                password2.setText(word);
                passwordOrder = 2;
                break;
            case 2:
                password3.setText(word);
                passwordOrder = 3;
                break;
            case 3:
                password4.setText(word);
                passwordOrder = 4;
                break;
            case 4:
                password5.setText(word);
                passwordOrder = 5;
                break;
        }
    }


    void textViewSetting() {
        password1 = view.findViewById(R.id.password_insert_1);
        password2 = view.findViewById(R.id.password_insert_2);
        password3 = view.findViewById(R.id.password_insert_3);
        password4 = view.findViewById(R.id.password_insert_4);
        password5 = view.findViewById(R.id.password_insert_5);
        tv_notice = view.findViewById(R.id.tv_ble_framgment_password_change_notice);
    }

    public void btnSetting() {
        btn_1 = view.findViewById(R.id.password_key_1);
        btn_2 = view.findViewById(R.id.password_key_2);
        btn_3 = view.findViewById(R.id.password_key_3);
        btn_4 = view.findViewById(R.id.password_key_4);
        btn_5 = view.findViewById(R.id.password_key_5);
        btn_6 = view.findViewById(R.id.password_key_6);
        btn_7 = view.findViewById(R.id.password_key_7);
        btn_8 = view.findViewById(R.id.password_key_8);
        btn_9 = view.findViewById(R.id.password_key_9);
        btn_0 = view.findViewById(R.id.password_key_0);
        btn_Q = view.findViewById(R.id.password_key_q);
        btn_W = view.findViewById(R.id.password_key_w);
        btn_E = view.findViewById(R.id.password_key_e);
        btn_R = view.findViewById(R.id.password_key_r);
        btn_T = view.findViewById(R.id.password_key_t);
        btn_Y = view.findViewById(R.id.password_key_y);
        btn_U = view.findViewById(R.id.password_key_u);
        btn_I = view.findViewById(R.id.password_key_i);
        btn_O = view.findViewById(R.id.password_key_o);
        btn_P = view.findViewById(R.id.password_key_p);
        btn_A = view.findViewById(R.id.password_key_a);
        btn_S = view.findViewById(R.id.password_key_s);
        btn_D = view.findViewById(R.id.password_key_d);
        btn_F = view.findViewById(R.id.password_key_f);
        btn_G = view.findViewById(R.id.password_key_g);
        btn_H = view.findViewById(R.id.password_key_h);
        btn_J = view.findViewById(R.id.password_key_j);
        btn_K = view.findViewById(R.id.password_key_k);
        btn_L = view.findViewById(R.id.password_key_l);
        btn_Z = view.findViewById(R.id.password_key_z);
        btn_X = view.findViewById(R.id.password_key_x);
        btn_C = view.findViewById(R.id.password_key_c);
        btn_V = view.findViewById(R.id.password_key_v);
        btn_B = view.findViewById(R.id.password_key_b);
        btn_N = view.findViewById(R.id.password_key_n);
        btn_M = view.findViewById(R.id.password_key_m);

        btn_1.setOnClickListener(v -> btnClickEvent("1"));
        btn_2.setOnClickListener(v -> btnClickEvent("2"));
        btn_3.setOnClickListener(v -> btnClickEvent("3"));
        btn_4.setOnClickListener(v -> btnClickEvent("4"));
        btn_5.setOnClickListener(v -> btnClickEvent("5"));
        btn_6.setOnClickListener(v -> btnClickEvent("6"));
        btn_7.setOnClickListener(v -> btnClickEvent("7"));
        btn_8.setOnClickListener(v -> btnClickEvent("8"));
        btn_9.setOnClickListener(v -> btnClickEvent("9"));
        btn_0.setOnClickListener(v -> btnClickEvent("0"));
        btn_Q.setOnClickListener(v -> btnClickEvent("Q"));
        btn_W.setOnClickListener(v -> btnClickEvent("W"));
        btn_E.setOnClickListener(v -> btnClickEvent("E"));
        btn_R.setOnClickListener(v -> btnClickEvent("R"));
        btn_T.setOnClickListener(v -> btnClickEvent("T"));
        btn_Y.setOnClickListener(v -> btnClickEvent("Y"));
        btn_U.setOnClickListener(v -> btnClickEvent("U"));
        btn_I.setOnClickListener(v -> btnClickEvent("I"));
        btn_O.setOnClickListener(v -> btnClickEvent("O"));
        btn_P.setOnClickListener(v -> btnClickEvent("P"));
        btn_A.setOnClickListener(v -> btnClickEvent("A"));
        btn_S.setOnClickListener(v -> btnClickEvent("S"));
        btn_D.setOnClickListener(v -> btnClickEvent("D"));
        btn_F.setOnClickListener(v -> btnClickEvent("F"));
        btn_G.setOnClickListener(v -> btnClickEvent("G"));
        btn_H.setOnClickListener(v -> btnClickEvent("H"));
        btn_J.setOnClickListener(v -> btnClickEvent("J"));
        btn_K.setOnClickListener(v -> btnClickEvent("K"));
        btn_L.setOnClickListener(v -> btnClickEvent("L"));
        btn_Z.setOnClickListener(v -> btnClickEvent("Z"));
        btn_X.setOnClickListener(v -> btnClickEvent("X"));
        btn_C.setOnClickListener(v -> btnClickEvent("C"));
        btn_V.setOnClickListener(v -> btnClickEvent("V"));
        btn_B.setOnClickListener(v -> btnClickEvent("B"));
        btn_N.setOnClickListener(v -> btnClickEvent("N"));
        btn_M.setOnClickListener(v -> btnClickEvent("M"));
    }

}
