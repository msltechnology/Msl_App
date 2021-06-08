package com.example.mslapp.RTU.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mslapp.BleMainActivity;
import com.example.mslapp.R;

import static com.example.mslapp.BleMainActivity.mBleContext;
import static com.example.mslapp.RTU.fragment.fragment_RTU_Function.send;
import static com.example.mslapp.RTU.fragment.fragment_RTU_Status.Server_1;
import static com.example.mslapp.RTU.fragment.fragment_RTU_Status.Server_Port_1;
import static com.example.mslapp.RTUMainActivity.DATA_NUM_1;
import static com.example.mslapp.RTUMainActivity.DATA_NUM_2;
import static com.example.mslapp.RTUMainActivity.DATA_SIGN_CHECKSUM;
import static com.example.mslapp.RTUMainActivity.DATA_SIGN_COMMA;
import static com.example.mslapp.RTUMainActivity.DATA_SIGN_CR;
import static com.example.mslapp.RTUMainActivity.DATA_SIGN_LF;
import static com.example.mslapp.RTUMainActivity.DATA_SIGN_START;
import static com.example.mslapp.RTUMainActivity.DATA_TYPE_MUCMD;
import static com.example.mslapp.RTUMainActivity.mRTUContext;

public class dialogFragment_rtu_Status_Server_2_Change extends DialogFragment {

    // 로그 이름 용
    public static final String TAG = "Msl-RTU-Status-Dialog-Server_2_Change";

    Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0,
            btn_cancel, btn_clear, btn_confirm;
    TextView tv_server_1, tv_server_2, tv_server_3, tv_server_4, tv_server_5, tv_server_6, tv_server_7, tv_server_8, tv_server_9, tv_server_10, tv_server_11, tv_server_12,
            tv_port_1, tv_port_2, tv_port_3, tv_port_4, tv_port_5;

    String from = "rtu";

    View view;

    Bundle mArgs;

    Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mArgs = getArguments();
            from = mArgs.getString("from");
        } catch (Exception e) {
            Log.d(TAG, "Bundle Error : " + e.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "dialogFragment_rtu_Status_Server_2_Change onCreateView");

        view = inflater.inflate(R.layout.rtu_fragment_status_dialog_server_change, null);

        if (from.equals("ble")) {
            mContext = mBleContext;
        } else {
            mContext = mRTUContext;
        }

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        text_setting();
        btn_Setting();

        return view;
    }

    void text_setting() {
        tv_server_1 = view.findViewById(R.id.tv_rtu_status_server_change_1);
        tv_server_2 = view.findViewById(R.id.tv_rtu_status_server_change_2);
        tv_server_3 = view.findViewById(R.id.tv_rtu_status_server_change_3);
        tv_server_4 = view.findViewById(R.id.tv_rtu_status_server_change_4);
        tv_server_5 = view.findViewById(R.id.tv_rtu_status_server_change_5);
        tv_server_6 = view.findViewById(R.id.tv_rtu_status_server_change_6);
        tv_server_7 = view.findViewById(R.id.tv_rtu_status_server_change_7);
        tv_server_8 = view.findViewById(R.id.tv_rtu_status_server_change_8);
        tv_server_9 = view.findViewById(R.id.tv_rtu_status_server_change_9);
        tv_server_10 = view.findViewById(R.id.tv_rtu_status_server_change_10);
        tv_server_11 = view.findViewById(R.id.tv_rtu_status_server_change_11);
        tv_server_12 = view.findViewById(R.id.tv_rtu_status_server_change_12);

        tv_port_1 = view.findViewById(R.id.tv_rtu_status_server_port_change_1);
        tv_port_2 = view.findViewById(R.id.tv_rtu_status_server_port_change_2);
        tv_port_3 = view.findViewById(R.id.tv_rtu_status_server_port_change_3);
        tv_port_4 = view.findViewById(R.id.tv_rtu_status_server_port_change_4);
        tv_port_5 = view.findViewById(R.id.tv_rtu_status_server_port_change_5);

        TextView name = view.findViewById(R.id.tv_rtu_status_dialog_server_name);
        name.setText("Server 2");
    }

    void btn_Setting() {
        btn_1 = view.findViewById(R.id.btn_rtu_status_server_change_1);
        btn_2 = view.findViewById(R.id.btn_rtu_status_server_change_2);
        btn_3 = view.findViewById(R.id.btn_rtu_status_server_change_3);
        btn_4 = view.findViewById(R.id.btn_rtu_status_server_change_4);
        btn_5 = view.findViewById(R.id.btn_rtu_status_server_change_5);
        btn_6 = view.findViewById(R.id.btn_rtu_status_server_change_6);
        btn_7 = view.findViewById(R.id.btn_rtu_status_server_change_7);
        btn_8 = view.findViewById(R.id.btn_rtu_status_server_change_8);
        btn_9 = view.findViewById(R.id.btn_rtu_status_server_change_9);
        btn_0 = view.findViewById(R.id.btn_rtu_status_server_change_0);

        btn_cancel = view.findViewById(R.id.btn_rtu_status_server_change_cancel);
        btn_clear = view.findViewById(R.id.btn_rtu_status_server_change_clear);
        btn_confirm = view.findViewById(R.id.btn_rtu_status_server_change_confirm);

        btn_1.setOnClickListener(v -> btn_Click("1"));
        btn_2.setOnClickListener(v -> btn_Click("2"));
        btn_3.setOnClickListener(v -> btn_Click("3"));
        btn_4.setOnClickListener(v -> btn_Click("4"));
        btn_5.setOnClickListener(v -> btn_Click("5"));
        btn_6.setOnClickListener(v -> btn_Click("6"));
        btn_7.setOnClickListener(v -> btn_Click("7"));
        btn_8.setOnClickListener(v -> btn_Click("8"));
        btn_9.setOnClickListener(v -> btn_Click("9"));
        btn_0.setOnClickListener(v -> btn_Click("0"));

        btn_cancel.setOnClickListener(v -> dismiss());
        btn_clear.setOnClickListener(v -> {
                    tv_server_1.setText("");
                    tv_server_2.setText("");
                    tv_server_3.setText("");
                    tv_server_4.setText("");
                    tv_server_5.setText("");
                    tv_server_6.setText("");
                    tv_server_7.setText("");
                    tv_server_8.setText("");
                    tv_server_9.setText("");
                    tv_server_10.setText("");
                    tv_server_11.setText("");
                    tv_server_12.setText("");
                    tv_port_1.setText("");
                    tv_port_2.setText("");
                    tv_port_3.setText("");
                    tv_port_4.setText("");
                    tv_port_5.setText("");
                }
        );
        btn_confirm.setOnClickListener(v -> {
            if (tv_port_5.getText().toString().equals("")) {
                Toast.makeText(mContext, "Port 번호까지 다 채워주세요.", Toast.LENGTH_SHORT).show();
            } else {
                String Server_2_data = tv_server_1.getText().toString() + tv_server_2.getText().toString() + tv_server_3.getText().toString() + DATA_SIGN_COMMA +
                        tv_server_4.getText().toString() + tv_server_5.getText().toString() + tv_server_6.getText().toString() + DATA_SIGN_COMMA +
                        tv_server_7.getText().toString() + tv_server_8.getText().toString() + tv_server_9.getText().toString() + DATA_SIGN_COMMA +
                        tv_server_10.getText().toString() + tv_server_11.getText().toString() + tv_server_12.getText().toString();
                String Server_Port_2_data = tv_port_1.getText().toString() + tv_port_2.getText().toString() + tv_port_3.getText().toString() +
                        tv_port_4.getText().toString() + tv_port_5.getText().toString();

                if (from.equals("ble")) {
                    String data = DATA_SIGN_START + DATA_TYPE_MUCMD + DATA_SIGN_COMMA +
                            DATA_NUM_2 + DATA_SIGN_COMMA +
                            mArgs.getString("server1") + DATA_SIGN_COMMA +
                            mArgs.getString("server1port") + DATA_SIGN_COMMA +
                            Server_2_data + DATA_SIGN_COMMA +
                            Server_Port_2_data + DATA_SIGN_CHECKSUM +
                            DATA_NUM_1 + DATA_NUM_1 +
                            DATA_SIGN_CR + DATA_SIGN_LF;
                    ((BleMainActivity) getActivity()).BlewriteData("<" + data);
                } else {
                    String data = DATA_SIGN_START + DATA_TYPE_MUCMD + DATA_SIGN_COMMA +
                            DATA_NUM_2 + DATA_SIGN_COMMA +
                            Server_1 + DATA_SIGN_COMMA +
                            Server_Port_1 + DATA_SIGN_COMMA +
                            Server_2_data + DATA_SIGN_COMMA +
                            Server_Port_2_data + DATA_SIGN_CHECKSUM +
                            DATA_NUM_1 + DATA_NUM_1 +
                            DATA_SIGN_CR + DATA_SIGN_LF;
                    send(data);
                }
                Toast.makeText(mContext, "명령어를 보냈습니다.", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }

    void btn_Click(String selected) {

        TextView tv_current;
        TextView[] textViews = {tv_server_1, tv_server_2, tv_server_3, tv_server_4, tv_server_5, tv_server_6, tv_server_7, tv_server_8, tv_server_9, tv_server_10, tv_server_11, tv_server_12,
                tv_port_1, tv_port_2, tv_port_3, tv_port_4, tv_port_5};

        int selected_int = Integer.parseInt(selected);

        for (int i = 0; i < textViews.length; i++) {
            if (tv_server_1.getText().toString().equals("") && i == 0 && selected_int > 2) {
                Toast.makeText(mContext, "각 IP 최댓값은 255입니다.", Toast.LENGTH_SHORT).show();
                return;
            } else if (tv_server_2.getText().toString().equals("") && i == 1 && selected_int > 5) {
                if (Integer.parseInt(tv_server_1.getText().toString()) == 2) {
                    Toast.makeText(mContext, "각 IP 최댓값은 255입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (tv_server_3.getText().toString().equals("") && i == 2 && selected_int > 5) {
                if (Integer.parseInt(tv_server_1.getText().toString()) == 2 && Integer.parseInt(tv_server_2.getText().toString()) == 5) {
                    Toast.makeText(mContext, "각 IP 최댓값은 255입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (tv_server_4.getText().toString().equals("") && i == 3 && selected_int > 2) {
                Toast.makeText(mContext, "각 IP 최댓값은 255입니다.", Toast.LENGTH_SHORT).show();
                return;
            } else if (tv_server_5.getText().toString().equals("") && i == 4 && selected_int > 5) {
                if (Integer.parseInt(tv_server_4.getText().toString()) == 2) {
                    Toast.makeText(mContext, "각 IP 최댓값은 255입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (tv_server_6.getText().toString().equals("") && i == 5 && selected_int > 5) {
                if (Integer.parseInt(tv_server_4.getText().toString()) == 2 && Integer.parseInt(tv_server_5.getText().toString()) == 5) {
                    Toast.makeText(mContext, "각 IP 최댓값은 255입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (tv_server_7.getText().toString().equals("") && i == 6 && selected_int > 2) {
                Toast.makeText(mContext, "각 IP 최댓값은 255입니다.", Toast.LENGTH_SHORT).show();
                return;
            } else if (tv_server_8.getText().toString().equals("") && i == 7 && selected_int > 5) {
                if (Integer.parseInt(tv_server_7.getText().toString()) == 2) {
                    Toast.makeText(mContext, "각 IP 최댓값은 255입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (tv_server_9.getText().toString().equals("") && i == 8 && selected_int > 5) {
                if (Integer.parseInt(tv_server_7.getText().toString()) == 2 && Integer.parseInt(tv_server_8.getText().toString()) == 5) {
                    Toast.makeText(mContext, "각 IP 최댓값은 255입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (tv_server_10.getText().toString().equals("") && i == 9 && selected_int > 2) {
                Toast.makeText(mContext, "각 IP 최댓값은 255입니다.", Toast.LENGTH_SHORT).show();
                return;
            } else if (tv_server_11.getText().toString().equals("") && i == 10 && selected_int > 5) {
                if (Integer.parseInt(tv_server_10.getText().toString()) == 2) {
                    Toast.makeText(mContext, "각 IP 최댓값은 255입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (tv_server_12.getText().toString().equals("") && i == 11 && selected_int > 5) {
                if (Integer.parseInt(tv_server_10.getText().toString()) == 2 && Integer.parseInt(tv_server_11.getText().toString()) == 5) {
                    Toast.makeText(mContext, "각 IP 최댓값은 255입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (tv_port_1.getText().toString().equals("") && i == 12 && selected_int > 6) {
                Toast.makeText(mContext, "Port 최댓값은 65535입니다.", Toast.LENGTH_SHORT).show();
                return;
            } else if (tv_port_2.getText().toString().equals("") && i == 13 && selected_int > 5) {
                if (Integer.parseInt(tv_port_1.getText().toString()) == 6) {
                    Toast.makeText(mContext, "Port 최댓값은 65535입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (tv_port_3.getText().toString().equals("") && i == 14 && selected_int > 5) {
                if (Integer.parseInt(tv_port_1.getText().toString()) == 6 && Integer.parseInt(tv_port_2.getText().toString()) == 5) {
                    Toast.makeText(mContext, "Port 최댓값은 65535입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (tv_port_4.getText().toString().equals("") && i == 15 && selected_int > 3) {
                if (Integer.parseInt(tv_port_1.getText().toString()) == 6 && Integer.parseInt(tv_port_2.getText().toString()) == 5 && Integer.parseInt(tv_port_3.getText().toString()) == 5) {
                    Toast.makeText(mContext, "Port 최댓값은 65535입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (tv_port_5.getText().toString().equals("") && i == 16 && selected_int > 5) {
                if (Integer.parseInt(tv_port_1.getText().toString()) == 6 && Integer.parseInt(tv_port_2.getText().toString()) == 5
                        && Integer.parseInt(tv_port_3.getText().toString()) == 5 && Integer.parseInt(tv_port_4.getText().toString()) == 3) {
                    Toast.makeText(mContext, "Port 최댓값은 65535입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }


            tv_current = textViews[i];
            if (tv_current.getText().toString().equals("")) {
                textViews[i].setText(selected);
                return;
            }
        }
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
        WindowManager wm;
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        final String x = String.valueOf(Math.round((size.x * 0.9)));
        final String y = String.valueOf(Math.round((size.y * 0.7)));
        int dialogWidth = Integer.parseInt(x);
        int dialogHeight = Integer.parseInt(y);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        // 창크기 지정


    }
}
