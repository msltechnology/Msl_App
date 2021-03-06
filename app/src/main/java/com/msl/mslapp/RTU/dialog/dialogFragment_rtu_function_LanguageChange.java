package com.msl.mslapp.RTU.dialog;

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
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.msl.mslapp.R;
import com.msl.mslapp.RTU.fragment.fragment_RTU_Function;

import static com.msl.mslapp.RTUMainActivity.mRTUContext;

public class dialogFragment_rtu_function_LanguageChange extends DialogFragment {

    // 로그 이름 용
    public static final String TAG = "Msl-RTU-Function-Dialog-LanguageChange";

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "dialogFragment_rtu_function_LanguageChange onCreateView");

        view = inflater.inflate(R.layout.ble_fragment_beginning_dialog_languagechange, null);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LinearLayout im_kor = view.findViewById(R.id.language_kor);
        LinearLayout im_eng = view.findViewById(R.id.language_eng);

        im_kor.setOnClickListener(v -> languageChange("ko"));
        im_eng.setOnClickListener(v -> languageChange("en"));

        return view;
    }

    void languageChange(String language){
        fragment_RTU_Function.setLocale(language);
        fragment_RTU_Function.rtu_Beginning_reset();
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
        Display display = mRTUContext.getDisplay();

        Point size = new Point();
        display.getSize(size);

        final String x = String.valueOf(Math.round((size.x * 0.85)));
        final String y = String.valueOf(Math.round((size.y * 0.2)));
        int dialogWidth = Integer.parseInt(x);
        int dialogHeight = Integer.parseInt(y);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        // 창크기 지정
    }


}
