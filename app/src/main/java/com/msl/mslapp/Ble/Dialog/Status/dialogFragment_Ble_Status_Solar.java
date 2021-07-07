package com.msl.mslapp.Ble.Dialog.Status;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.msl.mslapp.Ble.fragment.Function.fragment_Ble_Status;
import com.msl.mslapp.R;

import static com.msl.mslapp.BleMainActivity.BlewriteData;
import static com.msl.mslapp.BleMainActivity.DATA_REQUEST_SLC;
import static com.msl.mslapp.BleMainActivity.DATA_REQUEST_SLV;
import static com.msl.mslapp.BleMainActivity.mBleContext;

public class dialogFragment_Ble_Status_Solar extends DialogFragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Status-Dialog-Solar";

    Button btn_call_status_v, btn_call_status_a;
    public static TextView tv_ble_status_sol_value1, tv_ble_status_sol_value2, tv_ble_status_sol_value3, tv_ble_status_sol_value4, tv_ble_status_sol_value5, tv_ble_status_sol_value6,
            tv_ble_status_sol_value1_a, tv_ble_status_sol_value2_a, tv_ble_status_sol_value3_a, tv_ble_status_sol_value4_a, tv_ble_status_sol_value5_a, tv_ble_status_sol_value6_a;
    public static ImageView iv_ble_status_sol1, iv_ble_status_sol2, iv_ble_status_sol3, iv_ble_status_sol4, iv_ble_status_sol5, iv_ble_status_sol6;

    fragment_Ble_Status fragment_ble_status;

    View view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "dialogFragment_Ble_Status_Solar onCreateView");

        view = inflater.inflate(R.layout.ble_fragment_status_dialog_solar, null);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경 투명화
        getDialog().setCanceledOnTouchOutside(true);

        fragment_ble_status = (fragment_Ble_Status) getParentFragment();

        textSetting();
        btnSetting();
        imageViewSetting();
        // 최초 1회 데이터 요청하기
        BlewriteData(DATA_REQUEST_SLV);

        return view;
    }

    void textSetting(){
        tv_ble_status_sol_value1 = view.findViewById(R.id.tv_ble_fragment_status_dialog_Solar_sol_value1);
        tv_ble_status_sol_value2 = view.findViewById(R.id.tv_ble_fragment_status_dialog_Solar_sol_value2);
        tv_ble_status_sol_value3 = view.findViewById(R.id.tv_ble_fragment_status_dialog_Solar_sol_value3);
        tv_ble_status_sol_value4 = view.findViewById(R.id.tv_ble_fragment_status_dialog_Solar_sol_value4);
        tv_ble_status_sol_value5 = view.findViewById(R.id.tv_ble_fragment_status_dialog_Solar_sol_value5);
        tv_ble_status_sol_value6 = view.findViewById(R.id.tv_ble_fragment_status_dialog_Solar_sol_value6);
        tv_ble_status_sol_value1_a = view.findViewById(R.id.tv_ble_fragment_status_dialog_Solar_sol_value1_a);
        tv_ble_status_sol_value2_a = view.findViewById(R.id.tv_ble_fragment_status_dialog_Solar_sol_value2_a);
        tv_ble_status_sol_value3_a = view.findViewById(R.id.tv_ble_fragment_status_dialog_Solar_sol_value3_a);
        tv_ble_status_sol_value4_a = view.findViewById(R.id.tv_ble_fragment_status_dialog_Solar_sol_value4_a);
        tv_ble_status_sol_value5_a = view.findViewById(R.id.tv_ble_fragment_status_dialog_Solar_sol_value5_a);
        tv_ble_status_sol_value6_a = view.findViewById(R.id.tv_ble_fragment_status_dialog_Solar_sol_value6_a);
    }

    void imageViewSetting(){
        iv_ble_status_sol1 = view.findViewById(R.id.iv_ble_fragment_status_dialog_Solar_sol1);
        iv_ble_status_sol2 = view.findViewById(R.id.iv_ble_fragment_status_dialog_Solar_sol2);
        iv_ble_status_sol3 = view.findViewById(R.id.iv_ble_fragment_status_dialog_Solar_sol3);
        iv_ble_status_sol4 = view.findViewById(R.id.iv_ble_fragment_status_dialog_Solar_sol4);
        iv_ble_status_sol5 = view.findViewById(R.id.iv_ble_fragment_status_dialog_Solar_sol5);
        iv_ble_status_sol6 = view.findViewById(R.id.iv_ble_fragment_status_dialog_Solar_sol6);
    }

    void btnSetting() {
        btn_call_status_v = view.findViewById(R.id.btn_dialogFragment_Ble_Status_Solar_callStatus_v);
        btn_call_status_v.setOnClickListener(v -> BlewriteData(DATA_REQUEST_SLV));
        btn_call_status_a = view.findViewById(R.id.btn_dialogFragment_Ble_Status_Solar_callStatus_a);
        btn_call_status_a.setOnClickListener(v -> BlewriteData(DATA_REQUEST_SLC));
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

        final String x = String.valueOf(Math.round((size.x * 0.8)));
        final String y = String.valueOf(Math.round((size.y * 0.8)));
        int dialogWidth = Integer.parseInt(x);
        int dialogHeight = Integer.parseInt(y);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        // 창크기 지정

    }
}