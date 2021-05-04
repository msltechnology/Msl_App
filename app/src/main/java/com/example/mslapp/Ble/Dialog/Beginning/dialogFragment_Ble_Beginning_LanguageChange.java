package com.example.mslapp.Ble.Dialog.Beginning;

import android.content.res.Configuration;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mslapp.Ble.fragment.fragment_Ble_Beginning;
import com.example.mslapp.R;

import java.util.Locale;

import static com.example.mslapp.BleMainActivity.mBleContext;

public class dialogFragment_Ble_Beginning_LanguageChange extends DialogFragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Beginning-Dialog-LanguageChange";

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "dialogFragment_Ble_Beginning_LanguageChange onCreateView");

        view = inflater.inflate(R.layout.ble_fragment_beginning_dialog_languagechange, null);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView im_kor = view.findViewById(R.id.language_kor);
        ImageView im_eng = view.findViewById(R.id.language_eng);

        im_kor.setOnClickListener(v -> languageChange("ko"));
        im_eng.setOnClickListener(v -> languageChange("en"));

        return view;
    }

    void languageChange(String language){
        fragment_Ble_Beginning.setLocale(language);
        fragment_Ble_Beginning.ble_Beginning_reset();
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
        final String y = String.valueOf(Math.round((size.y * 0.3)));
        int dialogWidth = Integer.parseInt(x);
        int dialogHeight = Integer.parseInt(y);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        // 창크기 지정
    }


}
