package com.msl.mslapp.Ble.Dialog.Setting;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.msl.mslapp.Ble.fragment.Function.fragment_Ble_Setting;
import com.msl.mslapp.R;

import static com.msl.mslapp.Ble.fragment.Function.fragment_Ble_Setting.lantern_id;
import static com.msl.mslapp.BleMainActivity.mBleContext;

public class dialogFragment_Ble_Setting_ID_Setting extends DialogFragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Setting-Dialog-ID";

    private static final int Ble_Setting_dialog_ID = 2;

    TextView btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0, btn_ce, btn_ok;
    TextView tv_Selected_ID1, tv_Selected_ID2, tv_Selected_ID3;

    View view;

    public dialogFragment_Ble_Setting_ID_Setting() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "dialogFragment_Ble_Setting_ID_Setting onCreateView");

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경 투명화
        getDialog().setCanceledOnTouchOutside(true);

        view = inflater.inflate(R.layout.ble_fragment_setting_dialog_id_setting, null);

        tv_Selected_ID1 = view.findViewById(R.id.tv_dialog_setting_ID_1);
        tv_Selected_ID2 = view.findViewById(R.id.tv_dialog_setting_ID_2);
        tv_Selected_ID3 = view.findViewById(R.id.tv_dialog_setting_ID_3);

        /*lantern_id = lantern_id.trim();
        String[] dataArr = lantern_id.split("");

        try {
            tv_Selected_ID1.setText(dataArr[0]);
            tv_Selected_ID2.setText(dataArr[1]);
            tv_Selected_ID3.setText(dataArr[2]);
        }catch(Exception e){
            Log.e(TAG, "e : " + e.toString() + " //// " + dataArr.length);
        }*/

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
            if(tv_Selected_ID3.getText().toString().equals("")){
                Toast.makeText(mBleContext, getString(R.string.btnSelectID_Error1), Toast.LENGTH_SHORT).show();
                return;
            }

            if (getParentFragment() == null) {
                Log.d(TAG, "getParentFragment Null");
                dismiss();
                return;
            }
            String sendData = tv_Selected_ID1.getText().toString() + tv_Selected_ID2.getText().toString() + tv_Selected_ID3.getText().toString();

            Intent intent = fragment_Ble_Setting.newIntent(sendData);
            getParentFragment().onActivityResult(Ble_Setting_dialog_ID, Activity.RESULT_OK, intent);

            dismiss();
            Log.d(TAG, "btn_ok Click");
        });
    }


    void btnSelectID(String num) {
        int intNum = Integer.parseInt(num);
        if (tv_Selected_ID1.getText().equals("")) {
            if (intNum > 2) {
                Toast.makeText(mBleContext, getString(R.string.btnSelectID_Error1), Toast.LENGTH_SHORT).show();
                return;
            }
            tv_Selected_ID1.setText(num);
        } else if (tv_Selected_ID2.getText().equals("")) {
            if (tv_Selected_ID1.getText().toString().equals("2")) {
                if (intNum > 5) {
                    Toast.makeText(mBleContext, getString(R.string.btnSelectID_Error1), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            tv_Selected_ID2.setText(num);
        } else if (tv_Selected_ID3.getText().equals("")) {
            if (tv_Selected_ID1.getText().toString().equals("2") && tv_Selected_ID2.getText().toString().equals("5")) {
                if (intNum > 6) {
                    Toast.makeText(mBleContext, getString(R.string.btnSelectID_Error1), Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (tv_Selected_ID1.getText().toString().equals("0") && tv_Selected_ID2.getText().toString().equals("0")) {
                if (intNum == 0) {
                    Toast.makeText(mBleContext, getString(R.string.btnSelectID_Error1), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            tv_Selected_ID3.setText(num);
        }
    }

    void btnDeleteID() {
        if (!tv_Selected_ID3.getText().equals("")) {
            tv_Selected_ID3.setText("");
        } else if (!tv_Selected_ID2.getText().equals("")) {
            tv_Selected_ID2.setText("");
        } else if (!tv_Selected_ID1.getText().equals("")) {
            tv_Selected_ID1.setText("");
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

        final String x = String.valueOf(Math.round((size.x * 0.8)));
        final String y = String.valueOf(Math.round((size.y * 0.7)));
        int dialogWidth = Integer.parseInt(x);
        int dialogHeight = Integer.parseInt(y);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        // 창크기 지정

    }
}