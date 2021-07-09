package com.msl.mslapp.RTU.dialog;

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

import com.msl.mslapp.BleMainActivity;
import com.msl.mslapp.R;

import static com.msl.mslapp.BleMainActivity.mBleContext;
import static com.msl.mslapp.BleMainActivity.tLanguage;
import static com.msl.mslapp.RTU.fragment.fragment_RTU_Function.send;
import static com.msl.mslapp.RTU.fragment.fragment_RTU_Status.lantern_id;
import static com.msl.mslapp.RTU.fragment.fragment_RTU_Status.rtu_id;
import static com.msl.mslapp.RTUMainActivity.DATA_NUM_1;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_CHECKSUM;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_COMMA;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_CR;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_LF;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_START;
import static com.msl.mslapp.RTUMainActivity.DATA_TYPE_MUCMD;
import static com.msl.mslapp.RTUMainActivity.mRTUContext;

public class dialogFragment_rtu_Status_Lantern_ID_Change extends DialogFragment {

    // 로그 이름 용
    public static final String TAG = "Msl-RTU-Status-Dialog-Lantern_Change";

    Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0,
            btn_cancel, btn_clear, btn_confirm;
    TextView tv_1, tv_2, tv_3;

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
        }catch (Exception e){
            Log.d(TAG, "Bundle Error : " + e.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "dialogFragment_rtu_Status_RTU_ID_Change onCreateView");

        view = inflater.inflate(R.layout.rtu_fragment_status_dialog_lantern_id_change, null);

        if(from.equals("ble")){
            mContext = mBleContext;
        }else{
            mContext = mRTUContext;
        }

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        text_setting();
        btn_Setting();

        return view;
    }

    void text_setting() {
        tv_1 = view.findViewById(R.id.tv_rtu_status_lantern_id_change_1);
        tv_2 = view.findViewById(R.id.tv_rtu_status_lantern_id_change_2);
        tv_3 = view.findViewById(R.id.tv_rtu_status_lantern_id_change_3);
        TextView name = view.findViewById(R.id.tv_rtu_status_lantern_dialog_name);
        name.setText("Lantern ID");
    }

    void btn_Setting() {
        btn_1 = view.findViewById(R.id.btn_rtu_status_lantern_id_change_1);
        btn_2 = view.findViewById(R.id.btn_rtu_status_lantern_id_change_2);
        btn_3 = view.findViewById(R.id.btn_rtu_status_lantern_id_change_3);
        btn_4 = view.findViewById(R.id.btn_rtu_status_lantern_id_change_4);
        btn_5 = view.findViewById(R.id.btn_rtu_status_lantern_id_change_5);
        btn_6 = view.findViewById(R.id.btn_rtu_status_lantern_id_change_6);
        btn_7 = view.findViewById(R.id.btn_rtu_status_lantern_id_change_7);
        btn_8 = view.findViewById(R.id.btn_rtu_status_lantern_id_change_8);
        btn_9 = view.findViewById(R.id.btn_rtu_status_lantern_id_change_9);
        btn_0 = view.findViewById(R.id.btn_rtu_status_lantern_id_change_0);

        btn_cancel = view.findViewById(R.id.btn_rtu_status_lantern_id_change_cancel);
        btn_clear = view.findViewById(R.id.btn_rtu_status_lantern_id_change_clear);
        btn_confirm = view.findViewById(R.id.btn_rtu_status_lantern_id_change_confirm);

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
                    tv_1.setText("");
                    tv_2.setText("");
                    tv_3.setText("");
                }
        );
        btn_confirm.setOnClickListener(v -> {
            if (tv_3.getText().toString().equals("")) {
                if(tLanguage.equals("ko")){
                    Toast.makeText(mContext, "3자리를 채워주세요", Toast.LENGTH_SHORT).show();
                }else if(tLanguage.equals("en")){
                    Toast.makeText(mContext, "Please fill in 3 places", Toast.LENGTH_SHORT).show();
                }
            } else {
                lantern_id = tv_1.getText().toString() + tv_2.getText().toString() + tv_3.getText().toString();

                if(from.equals("ble")){
                    String data = DATA_SIGN_START + DATA_TYPE_MUCMD + DATA_SIGN_COMMA +
                            DATA_NUM_1 + DATA_SIGN_COMMA +
                            mArgs.getString("rtu_id") + DATA_SIGN_COMMA +
                            lantern_id + DATA_SIGN_CHECKSUM +
                            DATA_NUM_1 + DATA_NUM_1 +
                            DATA_SIGN_CR + DATA_SIGN_LF;
                    ((BleMainActivity) getActivity()).BlewriteData("<"+data);
                }else{
                    String data = DATA_SIGN_START + DATA_TYPE_MUCMD + DATA_SIGN_COMMA +
                            DATA_NUM_1 + DATA_SIGN_COMMA +
                            rtu_id + DATA_SIGN_COMMA +
                            lantern_id + DATA_SIGN_CHECKSUM +
                            DATA_NUM_1 + DATA_NUM_1 +
                            DATA_SIGN_CR + DATA_SIGN_LF;
                    send(data);
                }
                if(tLanguage.equals("ko")){
                    Toast.makeText(mContext, "명령어를 보냈습니다.", Toast.LENGTH_SHORT).show();
                }else if(tLanguage.equals("en")){
                    Toast.makeText(mContext, "command sent.", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });
    }

    void btn_Click(String selected) {

        TextView tv_current;
        TextView[] textViews = {tv_1, tv_2, tv_3};

        Log.d(TAG, "btn_Click " + tv_1.getText().toString());
        Log.d(TAG, "btn_Click " + tv_2.getText().toString());
        Log.d(TAG, "btn_Click " + tv_3.getText().toString());

        int selected_int = Integer.parseInt(selected);

        for (int i = 0; i < textViews.length; i++) {
            if (tv_1.getText().toString().equals("") && i == 0 && selected_int > 2) {
                if(tLanguage.equals("ko")){
                    Toast.makeText(mContext, "최댓값은 255입니다.", Toast.LENGTH_SHORT).show();
                }else if(tLanguage.equals("en")){
                    Toast.makeText(mContext, "The maximum is 255.", Toast.LENGTH_SHORT).show();
                }
                return;
            } else if (tv_2.getText().toString().equals("") && i == 1 && selected_int > 5) {
                if (Integer.parseInt(tv_1.getText().toString()) == 2){
                    if(tLanguage.equals("ko")){
                        Toast.makeText(mContext, "최댓값은 255입니다.", Toast.LENGTH_SHORT).show();
                    }else if(tLanguage.equals("en")){
                        Toast.makeText(mContext, "The maximum is 255.", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
            } else if (i == 2 && selected_int > 5) {
                if(Integer.parseInt(tv_1.getText().toString()) == 2 && Integer.parseInt(tv_2.getText().toString()) == 5){
                    if(tLanguage.equals("ko")){
                        Toast.makeText(mContext, "최댓값은 255입니다.", Toast.LENGTH_SHORT).show();
                    }else if(tLanguage.equals("en")){
                        Toast.makeText(mContext, "The maximum is 255.", Toast.LENGTH_SHORT).show();
                    }
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
        final String y = String.valueOf(Math.round((size.y * 0.5)));
        int dialogWidth = Integer.parseInt(x);
        int dialogHeight = Integer.parseInt(y);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        // 창크기 지정
    }


}