package com.msl.mslapp.ble.fragment.Function;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.msl.mslapp.ble.BleViewModel;
import com.msl.mslapp.ble.Dialog.setting.dialogFragment_Ble_Setting_DelayTime_Setting;
import com.msl.mslapp.ble.Dialog.setting.dialogFragment_Ble_Setting_FL_Setting;
import com.msl.mslapp.ble.Dialog.setting.dialogFragment_Ble_Setting_ID_Setting;
import com.msl.mslapp.ble.Dialog.setting.dialogFragment_Ble_Setting_Password_Change;
import com.msl.mslapp.Public.StringList;
import com.msl.mslapp.R;
import com.msl.mslapp.databinding.BleFragmentSettingBinding;

import java.util.ArrayList;
import java.util.List;

import static com.msl.mslapp.BleMainActivity.*;
import static com.msl.mslapp.Public.StringList.DATA_REQUEST_STATUS;

public class fragment_Ble_Setting extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Setting";

    TextView selected_FL, selected_ID, tv_delay_time, tv_version_num, tv_version_rtu, tv_version_gps_speed;

    Button btn_status, ban_information, btn_FL_Setting, btn_ID_Setting, btn_Password_Change, btn_GPS_ON, btn_GPS_OFF, btn_delay_time, btn_mode_remote, btn_mode_switch;

    LinearLayout ll_ble_fragment_setting_delay, ll_ble_fragment_setting_version, ll_mode;

    public static String lantern_id = "000",
            delay_time = "+000";

    View view;

    private static final int Ble_Setting_dialog_FL = 1;
    private static final int Ble_Setting_dialog_ID = 2;
    private static final int Ble_Setting_dialog_DelayTime = 3;
    private static final String EXTRA_GREETING_MESSAGE = "message";

    BleFragmentSettingBinding mBinding;
    BleViewModel viewModel ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "fragment_Ble_Setting onCreateView" + this.getTag());

        mBinding = DataBindingUtil.inflate(inflater,R.layout.ble_fragment_setting,container, false);

        view = mBinding.getRoot();

        selected_FL = view.findViewById(R.id.tv_ble_fragment_setting_selected_FL);
        selected_ID = view.findViewById(R.id.tv_ble_fragment_setting_selected_ID);
        tv_delay_time = view.findViewById(R.id.tv_ble_fragment_setting_delay_time);
        tv_version_num = view.findViewById(R.id.tv_ble_fragment_setting_version_num);
        tv_version_rtu = view.findViewById(R.id.tv_ble_fragment_setting_version_rtu);
        tv_version_gps_speed = view.findViewById(R.id.tv_ble_fragment_setting_version_gps_speed);

        btnSetting();

        ll_ble_fragment_setting_delay = view.findViewById(R.id.ll_ble_fragment_setting_delay);
        ll_ble_fragment_setting_version = view.findViewById(R.id.ll_ble_fragment_setting_version);
        ll_mode = view.findViewById(R.id.ll_mode);


        if(adminApp){
            ll_ble_fragment_setting_delay.setVisibility(View.VISIBLE);
            ll_ble_fragment_setting_version.setVisibility(View.VISIBLE);
            ll_mode.setVisibility(View.VISIBLE);
        }
        
        if(delaytimeApp){
            ll_ble_fragment_setting_delay.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(BleViewModel.class);
        mBinding.setBleViewModel(viewModel);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void readData(String data) {
        Log.d(TAG, "fragment_Ble_Setting readData!");

        // 비밀번호 변경 시 확인
        String passwordchangeCheck = "$PS,A," + readPassword;

        if (data.contains(passwordchangeCheck))
            Toast.makeText(mBleContext, "Password Change Success", Toast.LENGTH_SHORT).show();

        if (data.contains("*")) {
            data = data.substring(0, data.indexOf("*"));
        }

        String[] data_arr = data.split(",");


        if (data_arr[0].contains(StringList.DATA_TYPE_LISTS)) {
            if (data_arr[1].equals("S")) {

            } else {

                Log.d(TAG, "readData 데이터 읽기");

                lantern_id = data_arr[1];
                selected_ID.setTextColor(Color.parseColor("#ff0000"));
                selected_ID.setText(data_arr[1]);
                selected_FL.setTextColor(Color.parseColor("#ff0000"));
                selected_FL.setText(data_arr[6]);
            }
        }

        if (data_arr[0].contains(StringList.DATA_TYPE_LICMD)) {
            if (data_arr[1].equals(StringList.DATA_TYPE_S)) {

                switch (data_arr[2]) {
                    case StringList.DATA_TYPE_SID:
                        Toast.makeText(mBleContext, "Set ID : " + data_arr[3].substring(0, 3), Toast.LENGTH_SHORT).show();
                        break;
                    case StringList.DATA_TYPE_GP1:
                        Toast.makeText(mBleContext, "GPS Set : Always", Toast.LENGTH_SHORT).show();
                        break;
                    case StringList.DATA_TYPE_GP0:
                        Toast.makeText(mBleContext, "GPS Set : Only Night", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        if (checkDouble(data_arr[2])) {
                            Toast.makeText(mBleContext, "Set FL : " + data_arr[2].substring(0, 3), Toast.LENGTH_SHORT).show();
                        }
                        break;
                }

            }
        }

        if (data_arr[0].contains(StringList.DATA_TYPE_LISET)) {

            // 펌웨어 버전 값
            tv_version_num.setText(data_arr[1]);

            // RTU 용인지 확인
            if (Integer.parseInt(data_arr[2]) == 0) {
                tv_version_rtu.setText(getString(R.string.Lantern));
            } else if (Integer.parseInt(data_arr[2]) == 1) {
                tv_version_rtu.setText(getString(R.string.RTU));
            }

            // GPS 속도
            if (Integer.parseInt(data_arr[3]) == 0) {
                tv_version_gps_speed.setText("9600");
            } else if (Integer.parseInt(data_arr[3]) == 1) {
                tv_version_gps_speed.setText("4800");
            }

            // GPS Setting
            if (Integer.parseInt(data_arr[4]) == 0) {
                btn_GPS_ON.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_ble_setting_gps_on));
                btn_GPS_OFF.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_ble_setting_gps_off_clicked));
            } else if (Integer.parseInt(data_arr[4]) == 1) {
                btn_GPS_ON.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_ble_setting_gps_on_clicked));
                btn_GPS_OFF.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_ble_setting_gps_off));
            }

            delay_time = data_arr[5];


            String[] dataArr5 = delay_time.split("");

            List<String> list = new ArrayList<>();

            for(String s : dataArr5) {
                if(s != null && s.length() > 0) {
                    list.add(s);
                }
            }
            tv_delay_time.setTextColor(Color.parseColor("#ff0000"));
            tv_delay_time.setText(list.get(0) +list.get(1) + "." + list.get(2) + list.get(3) + " " + getString(R.string.Second_Sec));
        }
    }

    Boolean checkDouble(String data) {
        try {
            Double.parseDouble(data);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    void btnSetting() {
        btn_status = view.findViewById(R.id.btn_status);
        ban_information = view.findViewById(R.id.btn_information);
        btn_FL_Setting = view.findViewById(R.id.btn_FL_Setting);
        btn_ID_Setting = view.findViewById(R.id.btn_ID_Setting);
        btn_Password_Change = view.findViewById(R.id.btn_Password_Change);
        btn_GPS_ON = view.findViewById(R.id.btn_GPS_On);
        btn_GPS_OFF = view.findViewById(R.id.btn_GPS_Off);
        btn_delay_time = view.findViewById(R.id.btn_Delay_Time);
        btn_mode_remote = view.findViewById(R.id.btn_mode_remote);
        btn_mode_switch = view.findViewById(R.id.btn_mode_switch);


        btn_status.setOnClickListener(v -> BlewriteData(DATA_REQUEST_STATUS));
        ban_information.setOnClickListener(v -> BlewriteData(StringList.DATA_REQUEST_INFORMATION));
        btn_FL_Setting.setOnClickListener(
                new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        BlewriteData(DATA_REQUEST_STATUS);
                        showDialogFragment_FL();
                    }
        });
        btn_ID_Setting.setOnClickListener(
                new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        BlewriteData(DATA_REQUEST_STATUS);

                        if (lantern_id.equals("000")) {
                            handler.postDelayed(() -> showDialogFragment_ID(), 400);
                        } else {
                            showDialogFragment_ID();
                        }
                    }

        });
        btn_Password_Change.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                showPasswordChangeDialog();
            }
        });
        btn_GPS_ON.setOnClickListener(v -> {
            BlewriteData(StringList.GPS_SET_ON);
            btn_GPS_ON.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_ble_setting_gps_on_clicked));
            btn_GPS_OFF.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_ble_setting_gps_off));
        });
        btn_GPS_OFF.setOnClickListener(v -> {
            BlewriteData(StringList.GPS_SET_OFF);
            btn_GPS_ON.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_ble_setting_gps_on));
            btn_GPS_OFF.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_ble_setting_gps_off_clicked));
        });
        btn_delay_time.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                BlewriteData(StringList.DATA_REQUEST_INFORMATION);
                handler.postDelayed(() -> showDelayTimeChangeDialog(), 200);
            }
        });
        btn_mode_remote.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                BlewriteData(StringList.DATA_SET_RMC);

                btn_mode_remote.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_ble_setting_gps_on_clicked));
                btn_mode_switch.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_ble_setting_gps_off));
            }
        });
        btn_mode_switch.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                BlewriteData(StringList.DATA_SET_DIP);
                btn_mode_remote.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_ble_setting_gps_on));
                btn_mode_switch.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_ble_setting_gps_off_clicked));
            }
        });
    }

    public void setSelected_FL(String selected_fl) {
        Log.d(TAG, "fragment_Ble_Setting setSelected_FL : " + selected_fl);

        if (selected_fl.equals("")) {
            Toast.makeText(mBleContext, "Set FL blank!", Toast.LENGTH_SHORT).show();
            return;
        }

        String sendData = StringList.DATA_SIGN_START + StringList.DATA_TYPE_LICMD + StringList.DATA_SIGN_COMMA
                + StringList.DATA_TYPE_S + StringList.DATA_SIGN_COMMA
                + selected_fl + StringList.DATA_SIGN_COMMA
                + StringList.DATA_ID_255 + StringList.DATA_SIGN_CHECKSUM;

        BlewriteData(sendData);

        selected_FL.setTextColor(Color.parseColor("#ff0000"));
        selected_FL.setText(selected_fl);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                BlewriteData(DATA_REQUEST_STATUS);
            }
        }, 200);

    }

    public void setSelected_ID(String selected_id) {
        Log.d(TAG, "fragment_Ble_Setting setSelected_ID : " + selected_id);

        String sendData = StringList.DATA_SIGN_START + StringList.DATA_TYPE_LICMD + StringList.DATA_SIGN_COMMA
                + StringList.DATA_TYPE_S + StringList.DATA_SIGN_COMMA
                + StringList.DATA_TYPE_SID + StringList.DATA_SIGN_COMMA
                + selected_id + StringList.DATA_SIGN_CHECKSUM;

        BlewriteData(sendData);

        selected_ID.setTextColor(Color.parseColor("#ff0000"));
        selected_ID.setText(selected_id);
        handler.postDelayed(() -> BlewriteData(DATA_REQUEST_STATUS), 200);
    }

    public void setDelayTime(String selected_delaytime) {
        Log.d(TAG, "fragment_Ble_Setting setSelected_DelayTime : " + selected_delaytime);

        String sendData = StringList.DATA_SIGN_START + StringList.DATA_TYPE_LICMD + StringList.DATA_SIGN_COMMA
                + StringList.DATA_TYPE_S + StringList.DATA_SIGN_COMMA
                + StringList.DATA_TYPE_DEL + StringList.DATA_SIGN_COMMA
                + selected_delaytime + StringList.DATA_SIGN_CHECKSUM;

        BlewriteData(sendData);

        handler.postDelayed(() -> BlewriteData(StringList.DATA_REQUEST_INFORMATION), 200);
    }

    private void showDialogFragment_FL() {
        FragmentManager fm = this.getChildFragmentManager();
        dialogFragment_Ble_Setting_FL_Setting setting_FL_DialogFragment = new dialogFragment_Ble_Setting_FL_Setting();
        setting_FL_DialogFragment.show(fm, "fragment_setting_dialog_FL");
    }

    private void showDialogFragment_ID() {
        FragmentManager fm = this.getChildFragmentManager();
        dialogFragment_Ble_Setting_ID_Setting setting_ID_DialogFragment = new dialogFragment_Ble_Setting_ID_Setting();
        setting_ID_DialogFragment.show(fm, "fragment_setting_dialog_ID");
    }

    private void showPasswordChangeDialog() {
        FragmentManager fm = this.getChildFragmentManager();
        dialogFragment_Ble_Setting_Password_Change setting_PasswordChange_DialogFragment = new dialogFragment_Ble_Setting_Password_Change();
        setting_PasswordChange_DialogFragment.show(fm, "fragment_setting_dialog_Password");
    }

    private void showDelayTimeChangeDialog() {
        FragmentManager fm = this.getChildFragmentManager();
        dialogFragment_Ble_Setting_DelayTime_Setting setting_delayTime_DialogFragment = new dialogFragment_Ble_Setting_DelayTime_Setting();
        setting_delayTime_DialogFragment.show(fm, "dialogFragment_Ble_Setting_DelayTime_Setting");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            Log.d(TAG, "fragment_Ble_Setting onActivityResult RESULT_OK : Fail ");
            return;
        }
        Log.d(TAG, "fragment_Ble_Setting onActivityResult RESULT_OK : Success ");
        if (requestCode == Ble_Setting_dialog_FL) {
            Log.d(TAG, "fragment_Ble_Setting onActivityResult Code : Ble_Setting_dialog_FL ");
            String greeting = data.getStringExtra(EXTRA_GREETING_MESSAGE);
            setSelected_FL(greeting);
        } else if (requestCode == Ble_Setting_dialog_ID) {
            Log.d(TAG, "fragment_Ble_Setting onActivityResult Code : Ble_Setting_dialog_ID ");
            String greeting = data.getStringExtra(EXTRA_GREETING_MESSAGE);
            setSelected_ID(greeting);
        } else if (requestCode == Ble_Setting_dialog_DelayTime) {

            Log.d(TAG, "fragment_Ble_Setting onActivityResult Code : Ble_Setting_dialog_DelayTime ");
            String greeting = data.getStringExtra(EXTRA_GREETING_MESSAGE);
            setDelayTime(greeting);
        }
    }

    public static Intent newIntent(String message) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_GREETING_MESSAGE, message);
        return intent;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    Handler handler = new Handler();

}
