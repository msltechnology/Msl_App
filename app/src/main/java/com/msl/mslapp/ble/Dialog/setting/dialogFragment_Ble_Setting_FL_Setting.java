package com.msl.mslapp.ble.Dialog.setting;

import android.app.Activity;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.msl.mslapp.ble.fragment.Function.fragment_Ble_Setting;
import com.msl.mslapp.R;

import static com.msl.mslapp.BleMainActivity.mBleContext;

public class dialogFragment_Ble_Setting_FL_Setting extends DialogFragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Setting-Dialog-FL";

    private static final int Ble_Setting_dialog_FL = 1;


    Button btn_Second, btn_FL;
    TextView tv_Cancel, tv_Confirm;
    TextView tv_Selected_FL;

    public static String select_Sec = "-1";
    public static String select_FL = "-1";


    View view;

    private EditText mEditText;

    public dialogFragment_Ble_Setting_FL_Setting() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "dialogFragment_Ble_Setting_FL_Setting onCreateView");

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Log.d(TAG, "dialogFragment_Ble_Setting_FL_Setting 0");
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경 투명화
        getDialog().setCanceledOnTouchOutside(true);

        // 배경 모서리 곡선 이용 시 해당 값 넣어야 곡선으로 나옴(배경이 안보임)
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Log.d(TAG, "dialogFragment_Ble_Setting_FL_Setting 0_1");
        view = inflater.inflate(R.layout.ble_fragment_setting_dialog_fl_setting, null);
        Log.d(TAG, "dialogFragment_Ble_Setting_FL_Setting 1");
        btnSetting();

        Log.d(TAG, "dialogFragment_Ble_Setting_FL_Setting 3");
        Log.d(TAG, "fragment 작업");
        FragmentManager fm = this.getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_dialog_setting_fragmentSpace, new fragment_Ble_Setting_Dialog_Listview());
        ft.commit();

        Log.d(TAG, "dialogFragment_Ble_Setting_FL_Setting 4");
        tv_Selected_FL = view.findViewById(R.id.tv_dialog_setting_selectFL);

        return view;
    }

    void btnSetting() {
        btn_Second = view.findViewById(R.id.btn_dialog_setting_second);
        btn_FL = view.findViewById(R.id.btn_dialog_setting_FL);
        tv_Cancel = view.findViewById(R.id.tv_dialog_setting_cancel);
        tv_Confirm = view.findViewById(R.id.tv_dialog_setting_confirm);


        Log.d(TAG, "dialogFragment_Ble_Setting_FL_Setting 2_1");
        btn_Second.setOnClickListener(v -> fragmentChange("dialog_setting_second"));
        Log.d(TAG, "dialogFragment_Ble_Setting_FL_Setting 2_2");
        btn_FL.setOnClickListener(v -> fragmentChange("dialog_setting_FL"));
        Log.d(TAG, "dialogFragment_Ble_Setting_FL_Setting 2_3");
        tv_Cancel.setOnClickListener(v -> dismiss());
        tv_Confirm.setOnClickListener(v -> {
            if( getParentFragment() == null ) {
                Log.d(TAG, "getParentFragment Null");
                dismiss();
                return;
            }

            Intent intent = fragment_Ble_Setting.newIntent(tv_Selected_FL.getText().toString());
            getParentFragment().onActivityResult(Ble_Setting_dialog_FL, Activity.RESULT_OK, intent);

            dismiss();
            Log.d(TAG, "tv_Confirm Click");
        });
    }


    // fragment 변화
    public void fragmentChange(String callFragment) {
        Fragment fr;

        switch (callFragment) {
            case "dialog_setting_second":
                if (currentFragment() == 1) {
                    fr = new fragment_Ble_Setting_Dialog_Listview();
                    break;
                }
                fr = new fragment_Ble_Setting_Dialog_Second();
                break;
            case "dialog_setting_FL":
                if (currentFragment() == 2) {
                    fr = new fragment_Ble_Setting_Dialog_Listview();
                    break;
                }
                fr = new fragment_Ble_Setting_Dialog_FL();
                break;
            default:
                if (currentFragment() == 0)
                    return;
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

    public void btnSetText(int i, String num) {
        if (i == 1) {
            select_Sec = num;
            btn_Second.setText(num);
        } else if (i == 2) {
            select_FL = num;
            btn_FL.setText(num);
        }

        fragmentChange("dialog_setting_Listview");
    }

    public void btnSelectFL(String FL_Code) {
        tv_Selected_FL.setText(FL_Code);
    }
    int currentFragment() {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.fl_dialog_setting_fragmentSpace);
        if (fragment instanceof fragment_Ble_Setting_Dialog_FL) {
            Log.e(TAG, "fragmentChange 중 fragment_Ble_Setting_Dialog_FL 임!");
            return 2;
        } else if (fragment instanceof fragment_Ble_Setting_Dialog_Second) {
            Log.e(TAG, "fragmentChange 중 fragment_Ble_Setting_Dialog_Second 임!");
            return 1;
        } else if (fragment instanceof fragment_Ble_Setting_Dialog_Listview) {
            Log.e(TAG, "fragmentChange 중 fragment_Ble_Setting_Dialog_Listview 임!");
            return 0;
        }

        return 0;
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

        final String x = String.valueOf(Math.round((size.x * 0.95)));
        final String y = String.valueOf(Math.round((size.y * 0.9)));
        int dialogWidth = Integer.parseInt(x);
        int dialogHeight = Integer.parseInt(y);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        // 창크기 지정
    }

    @Override
    public void onDetach() {
        select_FL = "-1";
        select_Sec = "-1";

        super.onDetach();
    }

    // 다른 곳에서 해당 다이어로그를 호출 시 newinstance를 사용해서 데이터를 dialog에 보낼 수 있음.
    // 보내는 곳 : DialogFragment newFragment = MyDialogFragment.newInstance(num); 이후 show 호출
    // 받을 때 : onCreate에 getArguments().getInt("num"); 사용
    public static dialogFragment_Ble_Setting_FL_Setting newInstance(int num) {
        dialogFragment_Ble_Setting_FL_Setting f = new dialogFragment_Ble_Setting_FL_Setting();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {

        super.onDismiss(dialog);
    }
}