package com.msl.mslapp.ble.Dialog.setting;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.msl.mslapp.ble.fragment.Function.fragment_Ble_Setting;
import com.msl.mslapp.R;

import static com.msl.mslapp.BleMainActivity.mBleContext;

public class dialogFragment_Ble_Setting_DelayTime_Setting extends DialogFragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Setting-Dialog-DelayTime";

    private static final int Ble_Setting_dialog_DelayTime = 3;

    TextView btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0, btn_ok;
    TextView tv_hundred, tv_ten, tv_one;
    LinearLayout ll_delaytime_tiem, btn_ce;
    ImageView iv_sigh;

    boolean delay_sigh = true;

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

        // 배경 모서리 곡선 이용 시 해당 값 넣어야 곡선으로 나옴(배경이 안보임)
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        view = inflater.inflate(R.layout.ble_fragment_setting_dialog_delaytime_setting, null);

        ll_delaytime_tiem = view.findViewById(R.id.ll_delaytime_tiem);
        iv_sigh = view.findViewById(R.id.ll_dialog_delaytime_sigh);
        iv_sigh.setImageResource(R.drawable.dialog_delaytime_sigh_plus);
        tv_hundred = view.findViewById(R.id.tv_dialog_delaytime_hundred);
        tv_ten = view.findViewById(R.id.tv_dialog_delaytime_ten);
        tv_one = view.findViewById(R.id.tv_dialog_delaytime_one);


        // 셋팅된 값을 가져와서 그대로 해당값으로 설정하는데 그것보다 초기화된 모습으로 새로 입력하는게 나아보여서 바꿈.
        /*delay_time = delay_time.trim();

        String[] dataArr5 = delay_time.split("");

        List<String> list = new ArrayList<String>();

        for (String s : dataArr5) {
            if (s != null && s.length() > 0) {
                list.add(s);
            }
        }

        if (list.get(0).contains("+")) {
            delay_sigh = true;
            iv_sigh.setImageResource(R.drawable.dialog_delaytime_sigh_plus);
        } else if (list.get(0).contains("-")) {
            delay_sigh = false;
            iv_sigh.setImageResource(R.drawable.dialog_delaytime_sigh_minus);
        }
        tv_hundred.setText(list.get(1));
        tv_ten.setText(list.get(2));
        tv_one.setText(list.get(3));*/


        iv_sigh.setImageResource(R.drawable.dialog_delaytime_sigh_plus);
        tv_hundred.setText("-");
        tv_hundred.setTextColor(ContextCompat.getColor(mBleContext, R.color.blank_value));
        tv_ten.setText("-");
        tv_ten.setTextColor(ContextCompat.getColor(mBleContext, R.color.blank_value));
        tv_one.setText("-");
        tv_one.setTextColor(ContextCompat.getColor(mBleContext, R.color.blank_value));

        btnSetting();

        return view;
    }

    void btnSetting() {
        btn_1 = view.findViewById(R.id.btn_dialog_setting_ID_1);
        btn_2 = view.findViewById(R.id.btn_dialog_setting_ID_2);
        btn_3 = view.findViewById(R.id.btn_dialog_setting_ID_3);
        btn_4 = view.findViewById(R.id.btn_dialog_setting_ID_4);
        btn_5 = view.findViewById(R.id.btn_dialog_setting_ID_5);
        btn_6 = view.findViewById(R.id.btn_dialog_setting_ID_6);
        btn_7 = view.findViewById(R.id.btn_dialog_setting_ID_7);
        btn_8 = view.findViewById(R.id.btn_dialog_setting_ID_8);
        btn_9 = view.findViewById(R.id.btn_dialog_setting_ID_9);
        btn_0 = view.findViewById(R.id.btn_dialog_setting_ID_0);
        btn_ce = view.findViewById(R.id.btn_dialog_setting_ID_cancel);
        btn_ok = view.findViewById(R.id.btn_dialog_setting_ID_OK);


        btn_1.setOnClickListener(v -> btnSelectID("1"));
        btn_2.setOnClickListener(v -> btnSelectID("2"));
        btn_3.setOnClickListener(v -> btnSelectID("3"));
        btn_4.setOnClickListener(v -> btnSelectID("4"));
        btn_5.setOnClickListener(v -> btnSelectID("5"));
        btn_6.setOnClickListener(v -> btnSelectID("6"));
        btn_7.setOnClickListener(v -> btnSelectID("7"));
        btn_8.setOnClickListener(v -> btnSelectID("8"));
        btn_9.setOnClickListener(v -> btnSelectID("9"));
        btn_0.setOnClickListener(v -> btnSelectID("0"));
        btn_ce.setOnClickListener(v -> btnDeleteID());
        btn_ok.setOnClickListener(v -> {

            String hundred_value = "0";
            String ten_value = "0";
            String one_value = "0";

            if(!tv_hundred.getText().equals("-")){
                hundred_value = tv_hundred.getText().toString();
            }
            if(!tv_ten.getText().equals("-")){
                ten_value = tv_ten.getText().toString();
            }
            if(!tv_one.getText().equals("-")){
                one_value = tv_one.getText().toString();
            }

            String value = hundred_value + ten_value + one_value;

            if (getParentFragment() == null) {
                Log.d(TAG, "getParentFragment Null");
                dismiss();
                return;
            }
            String delayTimeData = "";
            if (delay_sigh) {
                delayTimeData = "+" + value;
            } else {
                delayTimeData = "-" + value;
            }

            try {
                Intent intent = fragment_Ble_Setting.newIntent(delayTimeData);
                getParentFragment().onActivityResult(Ble_Setting_dialog_DelayTime, Activity.RESULT_OK, intent);


                dismiss();
                Log.d(TAG, "btn_ok Click");
            } catch (Exception e) {
                Toast.makeText(mBleContext, "3자리 값을 다 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        iv_sigh.setOnClickListener(v -> {
            if (delay_sigh) {
                delay_sigh = false;
                iv_sigh.setImageResource(R.drawable.dialog_delaytime_sigh_minus);
            } else {
                delay_sigh = true;
                iv_sigh.setImageResource(R.drawable.dialog_delaytime_sigh_plus);
            }
            timeColorOff();
        });

    }

    void btnSelectID(String num) {
        timeColorOn();
        if (tv_hundred.getText().equals("-")) {
            tv_hundred.setText(num);
            tv_ten.setTextColor(ContextCompat.getColor(mBleContext, R.color.blank_value));
        } else if (tv_ten.getText().equals("-")) {
            tv_ten.setText(num);
            tv_one.setTextColor(ContextCompat.getColor(mBleContext, R.color.blank_value));
        } else if (tv_one.getText().equals("-")) {
            tv_one.setText(num);
        }
    }

    void btnDeleteID() {
        timeColorOn();
        if (!tv_one.getText().equals("-")) {
            tv_one.setText("-");
            tv_one.setTextColor(ContextCompat.getColor(mBleContext, R.color.blank_value));
        } else if (!tv_ten.getText().equals("-")) {
            tv_ten.setText("-");
            tv_ten.setTextColor(ContextCompat.getColor(mBleContext, R.color.blank_value));
        } else if (!tv_hundred.getText().equals("-")) {
            tv_hundred.setText("-");
            tv_hundred.setTextColor(ContextCompat.getColor(mBleContext, R.color.blank_value));
        }

        if (tv_hundred.getText().equals("-")) {
            tv_hundred.setTextColor(ContextCompat.getColor(mBleContext, R.color.blank_value));
        }

    }

    void timeColorOn() {
        tv_one.setTextColor(ContextCompat.getColor(mBleContext, R.color.MSL_Blue));
        tv_ten.setTextColor(ContextCompat.getColor(mBleContext, R.color.MSL_Blue));
        tv_hundred.setTextColor(ContextCompat.getColor(mBleContext, R.color.MSL_Blue));
        ll_delaytime_tiem.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_delaytime_tv_background));
    }

    void timeColorOff() {
        if(tv_one.getText().equals("-"))
            tv_one.setTextColor(ContextCompat.getColor(mBleContext, R.color.blank_value));
        if(tv_ten.getText().equals("-"))
            tv_ten.setTextColor(ContextCompat.getColor(mBleContext, R.color.blank_value));
        if(tv_hundred.getText().equals("-"))
            tv_hundred.setTextColor(ContextCompat.getColor(mBleContext, R.color.blank_value));
        ll_delaytime_tiem.setBackgroundColor(ContextCompat.getColor(mBleContext, R.color.bleFunctionBack));
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

        final String x = String.valueOf(Math.round((size.x * 0.85)));
        final String y = String.valueOf(Math.round((size.y * 0.6)));
        int dialogWidth = Integer.parseInt(x);
        int dialogHeight = Integer.parseInt(y);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        // 창크기 지정

    }
}