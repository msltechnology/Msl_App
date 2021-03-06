package com.msl.mslapp.ble.Dialog.setting;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.msl.mslapp.R;
import com.msl.mslapp.ble.BleViewModel;
import com.msl.mslapp.databinding.BleFragmentSettingDialogModeselectBinding;

import static com.msl.mslapp.BleMainActivity.BlewriteData;
import static com.msl.mslapp.BleMainActivity.mBleContext;
import static com.msl.mslapp.Public.StringList.GPS_SET_OFF;
import static com.msl.mslapp.Public.StringList.GPS_SET_ON;

public class dialogFragment_ble_Setting_ModeSelect extends DialogFragment {

    // 로그 이름 용
    public static final String TAG = "Msl-RTU-Setting-Dialog-ModeSelect";

    Button btn_Remote, btn_Rotate;

    View view;

    Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    BleFragmentSettingDialogModeselectBinding bleFragmentSettingDialogModeselectBinding;
    BleViewModel bleViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "dialogFragment_ble_Setting_GPS_Change onCreateView");

        bleFragmentSettingDialogModeselectBinding = DataBindingUtil.inflate(inflater,R.layout.ble_fragment_setting_dialog_modeselect,container, false);
        view = bleFragmentSettingDialogModeselectBinding.getRoot();


        //view = inflater.inflate(R.layout.ble_fragment_setting_dialog_gps_change, null);

        bleViewModel = new ViewModelProvider(requireActivity()).get(BleViewModel.class);

        bleFragmentSettingDialogModeselectBinding.setBleViewModel(bleViewModel);
        bleFragmentSettingDialogModeselectBinding.setLifecycleOwner(getViewLifecycleOwner());

        mContext = mBleContext;

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btn_Setting();

        return view;
    }

    void btn_Setting() {
        btn_Remote = view.findViewById(R.id.btn_mode_remote);
        btn_Rotate = view.findViewById(R.id.btn_mode_rotate);
        btn_Remote.setOnClickListener(v -> {
            bleViewModel.setRemoteMode();
            Toast.makeText(mContext, "Remote Mode Selected", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        btn_Rotate.setOnClickListener(v -> {
            bleViewModel.setRotateMode();
            Toast.makeText(mContext, "RotateSwitch Mode Selected", Toast.LENGTH_SHORT).show();
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
        final String y = String.valueOf(Math.round((size.y * 0.2)));
        int dialogWidth = Integer.parseInt(x);
        int dialogHeight = Integer.parseInt(y);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        // 창크기 지정
    }


}
