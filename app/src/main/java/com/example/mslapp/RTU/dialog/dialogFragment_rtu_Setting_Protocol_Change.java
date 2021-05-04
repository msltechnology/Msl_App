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
import static com.example.mslapp.RTUMainActivity.DATA_NUM_0;
import static com.example.mslapp.RTUMainActivity.DATA_NUM_1;
import static com.example.mslapp.RTUMainActivity.DATA_NUM_4;
import static com.example.mslapp.RTUMainActivity.DATA_SIGN_CHECKSUM;
import static com.example.mslapp.RTUMainActivity.DATA_SIGN_COMMA;
import static com.example.mslapp.RTUMainActivity.DATA_SIGN_CR;
import static com.example.mslapp.RTUMainActivity.DATA_SIGN_LF;
import static com.example.mslapp.RTUMainActivity.DATA_SIGN_START;
import static com.example.mslapp.RTUMainActivity.DATA_TYPE_MUCMD;
import static com.example.mslapp.RTUMainActivity.mRTUContext;

public class dialogFragment_rtu_Setting_Protocol_Change extends DialogFragment {

    // 로그 이름 용
    public static final String TAG = "Msl-RTU-Setting-Dialog-Protocol_Change";

    Button btn_On, btn_Off;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "dialogFragment_rtu_Setting_Protocol_Change onCreateView");

        view = inflater.inflate(R.layout.rtu_fragment_setting_dialog_protocol_change, null);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        text_setting();
        btn_Setting();

        return view;
    }

    void text_setting() {

        TextView name = view.findViewById(R.id.tv_rtu_setting_dialog_protocol_name);
        name.setText("Protocol");
    }

    void btn_Setting() {
        btn_On = view.findViewById(R.id.btn_rtu_setting_dialog_protocol_On);
        btn_Off = view.findViewById(R.id.btn_rtu_setting_dialog_protocol_Off);
        btn_On.setOnClickListener(v -> {
            Toast.makeText(mRTUContext, getString(R.string.RTU_protocol_private), Toast.LENGTH_SHORT).show();
            String data = DATA_SIGN_START + DATA_TYPE_MUCMD + DATA_SIGN_COMMA +
                    DATA_NUM_4 + DATA_SIGN_COMMA +
                    DATA_NUM_1 +  DATA_SIGN_CHECKSUM +
                    DATA_NUM_1 + DATA_NUM_1 +
                    DATA_SIGN_CR + DATA_SIGN_LF;
            send(data);
            dismiss();
        });

        btn_Off.setOnClickListener(v -> {
            Toast.makeText(mRTUContext, getString(R.string.RTU_protocol_standard), Toast.LENGTH_SHORT).show();
            String data = DATA_SIGN_START + DATA_TYPE_MUCMD + DATA_SIGN_COMMA +
                    DATA_NUM_4 + DATA_SIGN_COMMA +
                    DATA_NUM_0 +  DATA_SIGN_CHECKSUM +
                    DATA_NUM_1 + DATA_NUM_1 +
                    DATA_SIGN_CR + DATA_SIGN_LF;
            send(data);
            dismiss();
        });
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

        final String x = String.valueOf(Math.round((size.x * 0.8)));
        final String y = String.valueOf(Math.round((size.y * 0.3)));
        int dialogWidth = Integer.parseInt(x);
        int dialogHeight = Integer.parseInt(y);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        // 창크기 지정
    }


}
