package com.msl.mslapp.Ble.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.msl.mslapp.BleMainActivity;
import com.msl.mslapp.R;

import static com.msl.mslapp.Ble.fragment.fragment_Ble_Scan.selectedSerialNum;
import static com.msl.mslapp.BleMainActivity.BlewriteData;
import static com.msl.mslapp.BleMainActivity.mBleContext;
import static com.msl.mslapp.BleMainActivity.readPassword;

public class fragment_Ble_Password extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-password";

    View view;

    TextView password1, password2, password3, password4, password5, tv_userdata;

    TextView btn_Q, btn_W, btn_E, btn_R, btn_T, btn_Y, btn_U, btn_I, btn_O, btn_P,
            btn_A, btn_S, btn_D, btn_F, btn_G, btn_H, btn_J, btn_K, btn_L,
            btn_Z, btn_X, btn_C, btn_V, btn_B, btn_N, btn_M,
            btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0;

    TextView btn_connect, btn_change_num, btn_change_key;

    LinearLayout btn_delete, btn_delete_num, llkeyboard, llnumboard;

    int passwordOrder = 0;

    // 로딩바
    ProgressDialog dialog;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "fragment_Ble_Function onCreateView");
        view = inflater.inflate(R.layout.ble_fragment_password, null);


        dialog = new ProgressDialog(view.getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(getString(R.string.ble_password_dialog_message));
        dialog.show();

        textViewSetting();
        btnSetting();

        tv_userdata.setText(selectedSerialNum);



        llkeyboard = view.findViewById(R.id.password_keyboard);
        llnumboard = view.findViewById(R.id.password_numboard);

        btn_delete = view.findViewById(R.id.password_key_delete);
        btn_delete.setOnClickListener(v -> deleteClickEvent());

        btn_delete_num = view.findViewById(R.id.password_num_delete);
        btn_delete_num.setOnClickListener(v -> deleteClickEvent());

        btn_change_num = view.findViewById(R.id.board_change_num);
        btn_change_num.setOnClickListener(v -> {
            llkeyboard.setVisibility(View.VISIBLE);
            llnumboard.setVisibility(View.GONE);
        });

        btn_change_key = view.findViewById(R.id.board_change_key);
        btn_change_key.setOnClickListener(v -> {
            llkeyboard.setVisibility(View.GONE);
            llnumboard.setVisibility(View.VISIBLE);
        });

        btn_connect = view.findViewById(R.id.btn_connect);
        btn_connect.setOnClickListener(v -> {
            if (passwordOrder == 5) {
                String password = password1.getText().toString() + password2.getText().toString() + password3.getText().toString() + password4.getText().toString() + password5.getText().toString();
                Log.d(TAG, "password = " + password);
                passwordCheck(password);
            }
        });


        return view;
    }

    public void readData(String data) {
        Log.d(TAG, "fragment_Ble_password readData! : " + data);


        if (data.contains("$PS,R")) {
            if(dialog.isShowing()){
                dialog.dismiss();
                Toast.makeText(mBleContext, "Bluetooth Connect!", Toast.LENGTH_SHORT).show();
                // 더이상 비밀번호 요청문을 안보냄.
                BlewriteData("$PS,A,0000H*");
            }
            readPassword = data.substring(6, 11);
            Log.d(TAG, "readPassword = " + readPassword);
        }
    }

    void passwordCheck(String inputPassword) {
        if (readPassword.equals(psEncryptionTable(inputPassword))) {
            Log.d(TAG, "passwordCheck : OK");
            BlewriteData("$PS,A," + readPassword + "*");
            // 7마일은 암호화한 비밀번호를 번호를 보내야 한번에 인식하고 아니면 다시 비밀번호 요청 메세지를 보낸다.
            // 근데 3마일은 복호하 비밀번호 보내야해서 걍 일단 2개다 보내고 3마일 펌웨어 다 완료할 경우 암호화(readPassword) 데이터만 보내기로.. - 07-09
            BlewriteData("$PS,A," + inputPassword + "*");
            // 원래는 주석문이었는데 알고보니 3마일은 위와같이 보내야하고 7마일은 비번그딴거 상관없이 어차피 답장함...?..?..?(???) - 07-08
            //BlewriteData("$PS,A," + readPassword + "*");
            ((BleMainActivity) requireActivity()).fragmentChange("fragment_ble_function");
            //readPassword = inputPassword;
        }else if(inputPassword.equals("AHFFK")){
            Log.d(TAG, "passwordCheck : Admin");
            String deCrypPassword = psDecryptionTable(readPassword);
            BlewriteData("$PS,A," + readPassword + "*");
            // 7마일은 암호화한 비밀번호를 번호를 보내야 한번에 인식하고 아니면 다시 비밀번호 요청 메세지를 보낸다.
            // 근데 3마일은 복호하 비밀번호 보내야해서 걍 일단 2개다 보내고 3마일 펌웨어 다 완료할 경우 암호화(readPassword) 데이터만 보내기로.. - 07-09
            BlewriteData("$PS,A," + deCrypPassword + "*");
            ((BleMainActivity) requireActivity()).fragmentChange("fragment_ble_function");
        }else{
            Toast.makeText(mBleContext, "잘못된 비밀번호입니다.\nInvalid password.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDetach() {
        dialog.dismiss();
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
                String password = password1.getText().toString() + password2.getText().toString() + password3.getText().toString() + password4.getText().toString() + password5.getText().toString();
                Log.d(TAG, "password = " + password);
                passwordCheck(password);
                break;
        }
    }


    void textViewSetting() {
        password1 = view.findViewById(R.id.password_insert_1);
        password2 = view.findViewById(R.id.password_insert_2);
        password3 = view.findViewById(R.id.password_insert_3);
        password4 = view.findViewById(R.id.password_insert_4);
        password5 = view.findViewById(R.id.password_insert_5);
        tv_userdata = view.findViewById(R.id.tv_ble_fragment_password_userdata);
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

    public static String psEncryptionTable(String ps) {
        StringBuilder temp = new StringBuilder();
        for (int n = 0; n < ps.length(); n++) {
            switch (ps.charAt(n)) {
                case 'A':
                    temp.append("Z");
                    break;
                case 'B':
                    temp.append("U");
                    break;
                case 'C':
                    temp.append("O");
                    break;
                case 'D':
                    temp.append("N");
                    break;
                case 'E':
                    temp.append("H");
                    break;
                case 'F':
                    temp.append("V");
                    break;
                case 'G':
                    temp.append("C");
                    break;
                case 'H':
                    temp.append("F");
                    break;
                case 'I':
                    temp.append("Q");
                    break;
                case 'J':
                    temp.append("W");
                    break;
                case 'K':
                    temp.append("S");
                    break;
                case 'L':
                    temp.append("B");
                    break;
                case 'M':
                    temp.append("K");
                    break;
                case 'N':
                    temp.append("R");
                    break;
                case 'O':
                    temp.append("I");
                    break;
                case 'P':
                    temp.append("D");
                    break;
                case 'Q':
                    temp.append("G");
                    break;
                case 'R':
                    temp.append("P");
                    break;
                case 'S':
                    temp.append("J");
                    break;
                case 'T':
                    temp.append("X");
                    break;
                case 'U':
                    temp.append("M");
                    break;
                case 'V':
                    temp.append("T");
                    break;
                case 'W':
                    temp.append("L");
                    break;
                case 'X':
                    temp.append("A");
                    break;
                case 'Y':
                    temp.append("E");
                    break;
                case 'Z':
                    temp.append("Y");
                    break;
                case '0':
                    temp.append("5");
                    break;
                case '1':
                    temp.append("3");
                    break;
                case '2':
                    temp.append("9");
                    break;
                case '3':
                    temp.append("4");
                    break;
                case '4':
                    temp.append("2");
                    break;
                case '5':
                    temp.append("7");
                    break;
                case '6':
                    temp.append("1");
                    break;
                case '7':
                    temp.append("6");
                    break;
                case '8':
                    temp.append("0");
                    break;
                case '9':
                    temp.append("8");
                    break;
                default:
                    temp.append('-');
                    break;
            }
        }

        if (ps.length() == temp.length()) {
            return temp.toString();
        }
        return ps;
    }

    public static String psDecryptionTable(String ps) {
        StringBuilder temp = new StringBuilder();
        for (int n = 0; n < ps.length(); n++) {
            switch (ps.charAt(n)) {
                case 'Z':
                    temp.append("A");
                    break;
                case 'U':
                    temp.append("B");
                    break;
                case 'O':
                    temp.append("C");
                    break;
                case 'N':
                    temp.append("D");
                    break;
                case 'H':
                    temp.append("E");
                    break;
                case 'V':
                    temp.append("F");
                    break;
                case 'C':
                    temp.append("G");
                    break;
                case 'F':
                    temp.append("H");
                    break;
                case 'Q':
                    temp.append("I");
                    break;
                case 'W':
                    temp.append("J");
                    break;
                case 'S':
                    temp.append("K");
                    break;
                case 'B':
                    temp.append("L");
                    break;
                case 'K':
                    temp.append("M");
                    break;
                case 'R':
                    temp.append("N");
                    break;
                case 'I':
                    temp.append("O");
                    break;
                case 'D':
                    temp.append("P");
                    break;
                case 'G':
                    temp.append("Q");
                    break;
                case 'P':
                    temp.append("R");
                    break;
                case 'J':
                    temp.append("S");
                    break;
                case 'X':
                    temp.append("T");
                    break;
                case 'M':
                    temp.append("U");
                    break;
                case 'T':
                    temp.append("V");
                    break;
                case 'L':
                    temp.append("W");
                    break;
                case 'A':
                    temp.append("X");
                    break;
                case 'E':
                    temp.append("Y");
                    break;
                case 'Y':
                    temp.append("Z");
                    break;
                case '5':
                    temp.append("0");
                    break;
                case '3':
                    temp.append("1");
                    break;
                case '9':
                    temp.append("2");
                    break;
                case '4':
                    temp.append("3");
                    break;
                case '2':
                    temp.append("4");
                    break;
                case '7':
                    temp.append("5");
                    break;
                case '1':
                    temp.append("6");
                    break;
                case '6':
                    temp.append("7");
                    break;
                case '0':
                    temp.append("8");
                    break;
                case '8':
                    temp.append("9");
                    break;
                default:
                    temp.append('-');
                    break;
            }
        }

        if (ps.length() == temp.length()) {
            return temp.toString();
        }
        return ps;
    }

}
