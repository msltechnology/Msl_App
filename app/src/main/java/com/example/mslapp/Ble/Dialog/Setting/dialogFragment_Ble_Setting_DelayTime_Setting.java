package com.example.mslapp.Ble.Dialog.Setting;

import android.app.Activity;
import android.content.Intent;
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

import com.example.mslapp.Ble.fragment.fragment_Ble_Setting;
import com.example.mslapp.R;

import static com.example.mslapp.BleMainActivity.mBleContext;

public class dialogFragment_Ble_Setting_DelayTime_Setting extends DialogFragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Setting-Dialog-DelayTime";

    private static final int Ble_Setting_dialog_ID = 3;

    Button btn_hundred_up, btn_ten_up, btn_one_up, btn_hundred_down, btn_ten_down, btn_one_down, btn_ce, btn_ok;
    TextView tv_hundred, tv_ten, tv_one;

    View view;

    public dialogFragment_Ble_Setting_DelayTime_Setting() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "dialogFragment_Ble_Setting_DelayTime_Setting onCreateView");

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경 투명화
        getDialog().setCanceledOnTouchOutside(true);

        view = inflater.inflate(R.layout.ble_fragment_setting_dialog_delaytime_setting, null);

        tv_hundred = view.findViewById(R.id.tv_dialog_delaytime_hundred);
        tv_ten = view.findViewById(R.id.tv_dialog_delaytime_ten);
        tv_one = view.findViewById(R.id.tv_dialog_delaytime_one);

        btnSetting();

        return view;
    }

    void btnSetting() {
        btn_hundred_up = view.findViewById(R.id.btn_hundred_up);
        btn_hundred_down = view.findViewById(R.id.btn_hundred_down);
        btn_ten_up = view.findViewById(R.id.btn_ten_up);
        btn_ten_down = view.findViewById(R.id.btn_ten_down);
        btn_one_up = view.findViewById(R.id.btn_one_up);
        btn_one_down = view.findViewById(R.id.btn_one_down);
        btn_ce = view.findViewById(R.id.btn_dialog_delaytime_cancel);
        btn_ok = view.findViewById(R.id.btn_dialog_delaytime_OK);

        btn_hundred_up.setOnClickListener(v -> {
            int hundred = Integer.parseInt(tv_hundred.getText().toString());
            if (hundred < 9) {
                hundred += 1;
                tv_hundred.setText(Integer.toString(hundred));
            }
        });
        btn_hundred_down.setOnClickListener(v -> {
            int hundred = Integer.parseInt(tv_hundred.getText().toString());
            if (hundred > 0) {
                hundred -= 1;
                tv_hundred.setText(Integer.toString(hundred));
            }
        });
        btn_ten_up.setOnClickListener(v -> {
            int ten = Integer.parseInt(tv_ten.getText().toString());
            if (ten < 9) {
                ten += 1;
                tv_ten.setText(Integer.toString(ten));
            }
        });
        btn_ten_down.setOnClickListener(v -> {
            int ten = Integer.parseInt(tv_ten.getText().toString());
            if (ten > 0) {
                ten -= 1;
                tv_ten.setText(Integer.toString(ten));
            }
        });
        btn_one_up.setOnClickListener(v -> {
            int one = Integer.parseInt(tv_one.getText().toString());
            if (one < 9) {
                one += 1;
                tv_one.setText(Integer.toString(one));
            }
        });
        btn_one_down.setOnClickListener(v -> {
            int one = Integer.parseInt(tv_one.getText().toString());
            if (one > 0) {
                one -= 1;
                tv_one.setText(Integer.toString(one));
            }
        });
        btn_ce.setOnClickListener(v -> dismiss());
        btn_ok.setOnClickListener(v -> {

            if (getParentFragment() == null) {
                Log.d(TAG, "getParentFragment Null");
                dismiss();
                return;
            }

            String delayTimeData = tv_hundred.getText().toString() + tv_ten.getText().toString() + tv_one.getText().toString();

            Intent intent = fragment_Ble_Setting.newIntent(delayTimeData);
            getParentFragment().onActivityResult(Ble_Setting_dialog_ID, Activity.RESULT_OK, intent);

            dismiss();
            Log.d(TAG, "btn_ok Click");
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
        Display display = mBleContext.getDisplay();
        Point size = new Point();
        display.getSize(size);

        final String x = String.valueOf(Math.round((size.x * 0.8)));
        final String y = String.valueOf(Math.round((size.y * 0.5)));
        int dialogWidth = Integer.parseInt(x);
        int dialogHeight = Integer.parseInt(y);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        // 창크기 지정

    }
}