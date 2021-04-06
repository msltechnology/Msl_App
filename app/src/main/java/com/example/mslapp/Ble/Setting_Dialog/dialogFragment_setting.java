package com.example.mslapp.Ble.Setting_Dialog;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mslapp.Ble.fragment.fragment_Ble_Beginning;
import com.example.mslapp.Ble.fragment.fragment_Ble_Scan;
import com.example.mslapp.Ble.fragment.fragment_Ble_Status;
import com.example.mslapp.BleMainActivity;
import com.example.mslapp.R;

import static com.example.mslapp.BleMainActivity.mBleContext;

public class dialogFragment_setting extends DialogFragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Setting-Dialog";

    Button btn_Second, btn_FL, btn_Cancel, btn_Confirm;
    TextView tv_Selected_FL;
    LinearLayout ll_Search;

    View view;

    private EditText mEditText;

    public dialogFragment_setting() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "dialogFragment_setting onCreateView");

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경 투명화
        getDialog().setCanceledOnTouchOutside(true);

        view = inflater.inflate(R.layout.ble_fragment_setting_dialog, null);

        btnSetting();

        Log.d(TAG, "fragment 작업");
        FragmentManager fm = this.getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_dialog_setting_fragmentSpace, new fragment_Ble_Setting_Dialog_Listview());
        ft.commit();


        return view;
    }

    void btnSetting(){
        btn_Second = view.findViewById(R.id.btn_dialog_setting_second);
        btn_FL = view.findViewById(R.id.btn_dialog_setting_FL);
        ll_Search = view.findViewById(R.id.ll_dialog_setting_search);

        btn_Second.setOnClickListener(v -> fragmentChange("dialog_setting_second"));
        btn_FL.setOnClickListener(v -> fragmentChange("dialog_setting_FL"));
        ll_Search.setOnClickListener(v -> fragmentChange("dialog_setting_Listview"));
    }


    // fragment 변화
    public void fragmentChange(String callFragment) {
        Fragment fr;

        switch (callFragment) {
            case "dialog_setting_second":
                fr = new fragment_Ble_Setting_Dialog_Second();
                break;
            case "dialog_setting_FL":
                fr = new fragment_Ble_Setting_Dialog_FL();
                break;
            default:
                fr = new fragment_Ble_Setting_Dialog_Listview();
                break;
        }

        try {
            FragmentManager fm = this.getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fl_dialog_setting_fragmentSpace, fr);
            fragmentTransaction.commit();
        } catch (Exception e) {
            Log.e(TAG, "fragmentChange 간 문제 발생" + e.toString());
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
        Display display = mBleContext.getDisplay();
        Point size = new Point();
        display.getSize(size);

        final String x = String.valueOf(Math.round((size.x * 0.9)));
        final String y = String.valueOf(Math.round((size.y * 0.8)));
        int dialogWidth =Integer.parseInt(x);
        int dialogHeight = Integer.parseInt(y);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        // 창크기 지정

    }
}