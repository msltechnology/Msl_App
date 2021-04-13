package com.example.mslapp.Ble.Setting_Dialog;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mslapp.R;

import static com.example.mslapp.BleMainActivity.mBleContext;

public class dialogFragment_Ble_Setting_FL_Setting extends DialogFragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Setting-Dialog-FL";

    Button btn_Second, btn_FL, btn_Cancel, btn_Confirm;
    TextView tv_Selected_FL;
    LinearLayout ll_Search;

    public static String select_Sec = "0";
    public static String select_FL = "0";


    View view;

    private EditText mEditText;

    public dialogFragment_Ble_Setting_FL_Setting() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "dialogFragment_Ble_Setting_FL_Setting onCreateView");

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경 투명화
        getDialog().setCanceledOnTouchOutside(true);

        view = inflater.inflate(R.layout.ble_fragment_setting_dialog_fl_setting, null);

        btnSetting();

        Log.d(TAG, "fragment 작업");
        FragmentManager fm = this.getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_dialog_setting_fragmentSpace, new fragment_Ble_Setting_Dialog_Listview());
        ft.commit();

        tv_Selected_FL = view.findViewById(R.id.tv_dialog_setting_selectFL);


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
                if(currentFragment() == 1){
                    fr = new fragment_Ble_Setting_Dialog_Listview();
                    break;
                }
                fr = new fragment_Ble_Setting_Dialog_Second();
                break;
            case "dialog_setting_FL":
                if(currentFragment() == 2) {
                    fr = new fragment_Ble_Setting_Dialog_Listview();
                    break;
                }
                fr = new fragment_Ble_Setting_Dialog_FL();
                break;
            default:
                if(currentFragment() == 0)
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

    public void btnSetText(int i, String num){
        if(i == 1){
            select_Sec = num;
            btn_Second.setText(num);
        }else if(i == 2){
            select_FL = num;
            btn_FL.setText(num);
        }

        fragmentChange("dialog_setting_Listview");
    }

    public void btnSelectFL(String FL_Code){
        tv_Selected_FL.setText(FL_Code);
    }

    int currentFragment(){
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.fl_dialog_setting_fragmentSpace);
        if(fragment instanceof fragment_Ble_Setting_Dialog_FL){
            Log.e(TAG, "fragmentChange 중 fragment_Ble_Setting_Dialog_FL 임!");
            return 2;
        }else if(fragment instanceof  fragment_Ble_Setting_Dialog_Second){
            Log.e(TAG, "fragmentChange 중 fragment_Ble_Setting_Dialog_Second 임!");
            return 1;
        }else if (fragment instanceof  fragment_Ble_Setting_Dialog_Listview){
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
        int dialogWidth =Integer.parseInt(x);
        int dialogHeight = Integer.parseInt(y);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        // 창크기 지정
    }

    @Override
    public void onDetach() {
        select_FL = "0";
        select_Sec = "0";

        super.onDetach();
    }
}