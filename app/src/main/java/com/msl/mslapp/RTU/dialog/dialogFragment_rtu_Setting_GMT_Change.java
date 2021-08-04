package com.msl.mslapp.RTU.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
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
import static com.msl.mslapp.RTUMainActivity.DATA_NUM_0;
import static com.msl.mslapp.RTUMainActivity.DATA_NUM_1;
import static com.msl.mslapp.RTUMainActivity.DATA_NUM_10;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_CHECKSUM;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_COMMA;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_CR;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_LF;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_START;
import static com.msl.mslapp.RTUMainActivity.DATA_TYPE_MUCMD;
import static com.msl.mslapp.RTUMainActivity.mRTUContext;

public class dialogFragment_rtu_Setting_GMT_Change extends DialogFragment {

    // 로그 이름 용
    public static final String TAG = "Msl-RTU-Setting-Dialog-GMT_Change";

    Button btn_On, btn_Off;

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
        Log.d(TAG, "dialogFragment_rtu_Setting_GMT_Change onCreateView");

        view = inflater.inflate(R.layout.rtu_fragment_setting_dialog_gmt_change, null);

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

        TextView name = view.findViewById(R.id.tv_rtu_setting_dialog_gmt_name);
        name.setText("GMT");
    }

    void btn_Setting() {
        btn_On = view.findViewById(R.id.btn_rtu_setting_dialog_gmt_On);
        btn_Off = view.findViewById(R.id.btn_rtu_setting_dialog_gmt_Off);
        btn_On.setOnClickListener(v -> {

            if(tLanguage.equals("ko")){
                Toast.makeText(mContext, "0으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
            }else if(tLanguage.equals("en")){
                Toast.makeText(mContext, "changed to 0", Toast.LENGTH_SHORT).show();
            }

            String data = DATA_SIGN_START + DATA_TYPE_MUCMD + DATA_SIGN_COMMA +
                    DATA_NUM_10 + DATA_SIGN_COMMA +
                    DATA_NUM_1 +  DATA_SIGN_CHECKSUM +
                    DATA_NUM_1 + DATA_NUM_1 +
                    DATA_SIGN_CR + DATA_SIGN_LF;
            if(from.equals("ble")) {
                ((BleMainActivity) getActivity()).BlewriteData("<"+data);
            }else{
                send(data);
            }
            dismiss();
        });

        btn_Off.setOnClickListener(v -> {
            if(tLanguage.equals("ko")){
                Toast.makeText(mContext, "+9(한국 시간)으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
            }else if(tLanguage.equals("en")){
                Toast.makeText(mContext, "changed to 9", Toast.LENGTH_SHORT).show();
            }
            String data = DATA_SIGN_START + DATA_TYPE_MUCMD + DATA_SIGN_COMMA +
                    DATA_NUM_10 + DATA_SIGN_COMMA +
                    DATA_NUM_0 +  DATA_SIGN_CHECKSUM +
                    DATA_NUM_1 + DATA_NUM_1 +
                    DATA_SIGN_CR + DATA_SIGN_LF;
            if(from.equals("ble")) {
                ((BleMainActivity) getActivity()).BlewriteData("<"+data);
            }else{
                send(data);
            }
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
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        final String x = String.valueOf(Math.round((size.x * 0.8)));
        final String y = String.valueOf(Math.round((size.y * 0.25)));
        int dialogWidth = Integer.parseInt(x);
        int dialogHeight = Integer.parseInt(y);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        // 창크기 지정
    }


}
