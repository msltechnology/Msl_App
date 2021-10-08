package com.msl.mslapp.ble;

import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

public class BleBindingAdapter {

    @BindingAdapter({"imgRes"})
    public static void imgload(ImageView imageView, int resid) {
        imageView.setImageResource(resid);
    }

    @BindingAdapter({"btnRes"})
    public static void btnload(Button btn, int resid) {
        btn.setBackgroundResource(resid);
    }
}