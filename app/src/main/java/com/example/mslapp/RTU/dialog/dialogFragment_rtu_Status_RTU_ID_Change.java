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

import com.example.mslapp.R;

import static com.example.mslapp.RTU.fragment.fragment_RTU_Function.send;
import static com.example.mslapp.RTU.fragment.fragment_RTU_Status.lantern_id;
import static com.example.mslapp.RTU.fragment.fragment_RTU_Status.rtu_id;
import static com.example.mslapp.RTUMainActivity.DATA_NUM_1;
import static com.example.mslapp.RTUMainActivity.DATA_SIGN_CHECKSUM;
import static com.example.mslapp.RTUMainActivity.DATA_SIGN_COMMA;
import static com.example.mslapp.RTUMainActivity.DATA_SIGN_CR;
import static com.example.mslapp.RTUMainActivity.DATA_SIGN_LF;
import static com.example.mslapp.RTUMainActivity.DATA_SIGN_START;
import static com.example.mslapp.RTUMainActivity.DATA_TYPE_MUCMD;
import static com.example.mslapp.RTUMainActivity.mRTUContext;

public class dialogFragment_rtu_Status_RTU_ID_Change extends DialogFragment {

    // 로그 이름 용
    public static final String TAG = "Msl-RTU-Status-Dialog-ID_Change";

    Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0,
            btn_cancel, btn_clear, btn_confirm;
    TextView tv_1, tv_2, tv_3, tv_4, tv_5, tv_6, tv_7;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "dialogFragment_rtu_Status_RTU_ID_Change onCreateView");

        view = inflater.inflate(R.layout.rtu_fragment_status_dialog_rtu_id_change, null);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        text_setting();
        btn_Setting();

        return view;
    }

    void text_setting() {
        tv_1 = view.findViewById(R.id.tv_rtu_status_id_change_1);
        tv_2 = view.findViewById(R.id.tv_rtu_status_id_change_2);
        tv_3 = view.findViewById(R.id.tv_rtu_status_id_change_3);
        tv_4 = view.findViewById(R.id.tv_rtu_status_id_change_4);
        tv_5 = view.findViewById(R.id.tv_rtu_status_id_change_5);
        tv_6 = view.findViewById(R.id.tv_rtu_status_id_change_6);
        tv_7 = view.findViewById(R.id.tv_rtu_status_id_change_7);
        TextView name = view.findViewById(R.id.tv_rtu_status_dialog_name);
        name.setText("RTU ID");
    }

    void btn_Setting() {
        btn_1 = view.findViewById(R.id.btn_rtu_status_id_change_1);
        btn_2 = view.findViewById(R.id.btn_rtu_status_id_change_2);
        btn_3 = view.findViewById(R.id.btn_rtu_status_id_change_3);
        btn_4 = view.findViewById(R.id.btn_rtu_status_id_change_4);
        btn_5 = view.findViewById(R.id.btn_rtu_status_id_change_5);
        btn_6 = view.findViewById(R.id.btn_rtu_status_id_change_6);
        btn_7 = view.findViewById(R.id.btn_rtu_status_id_change_7);
        btn_8 = view.findViewById(R.id.btn_rtu_status_id_change_8);
        btn_9 = view.findViewById(R.id.btn_rtu_status_id_change_9);
        btn_0 = view.findViewById(R.id.btn_rtu_status_id_change_0);

        btn_cancel = view.findViewById(R.id.btn_rtu_status_id_change_cancel);
        btn_clear = view.findViewById(R.id.btn_rtu_status_id_change_clear);
        btn_confirm = view.findViewById(R.id.btn_rtu_status_id_change_confirm);

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
            tv_4.setText("");
            tv_5.setText("");
            tv_6.setText("");
            tv_7.setText("");
                }
        );
        btn_confirm.setOnClickListener(v -> {
            if (tv_7.getText().toString().equals("")) {
                Toast.makeText(mRTUContext, "7자리를 채워주세요", Toast.LENGTH_SHORT).show();
            } else {
                rtu_id = tv_1.getText().toString() + tv_2.getText().toString() + tv_3.getText().toString() + tv_4.getText().toString()
                        + tv_5.getText().toString() + tv_6.getText().toString() + tv_7.getText().toString();
                String data = DATA_SIGN_START + DATA_TYPE_MUCMD + DATA_SIGN_COMMA +
                        DATA_NUM_1 + DATA_SIGN_COMMA +
                        rtu_id + DATA_SIGN_COMMA +
                        lantern_id + DATA_SIGN_CHECKSUM +
                        DATA_NUM_1 + DATA_NUM_1 +
                        DATA_SIGN_CR + DATA_SIGN_LF;
                send(data);
                Toast.makeText(mRTUContext, "명령어를 보냈습니다.", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }

    void btn_Click(String selected) {

        TextView tv_current;
        TextView[] textViews = {tv_1, tv_2, tv_3, tv_4, tv_5, tv_6, tv_7};

        for (int i = 0; i < textViews.length; i++) {
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
        wm = (WindowManager) mRTUContext.getSystemService(Context.WINDOW_SERVICE);
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
