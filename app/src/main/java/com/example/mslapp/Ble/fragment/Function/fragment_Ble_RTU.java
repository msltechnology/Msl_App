package com.example.mslapp.Ble.fragment.Function;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mslapp.BleMainActivity;
import com.example.mslapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class fragment_Ble_RTU extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-RTU";

    private FragmentActivity myContext;
    View view;
    // bluetooth 탭 (기능)
    TabLayout tabLayout_ble;
    // Viewpager
    private ViewPager2 viewPager;
    adapter_Ble_RTU_Tab adapter_ble_rtu_tab;

    // 자식 프래그먼트
    fragment_Ble_RTU_Status fragment_ble_rtu_status;
    fragment_Ble_RTU_Setting fragment_ble_rtu_setting;

    // 하위 프래그먼트에게 데이터 주는 용도
    FragmentManager fragmentManager;

    // Tablayout title 이름
    String[] tavTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "fragment_Ble_RTU onCreateView");
        view = inflater.inflate(R.layout.ble_fragment_function, null);

        fragmentManager = this.getChildFragmentManager();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tavTitle = new String[]{"Status", "Setting"};

        tabLayout_ble = view.findViewById(R.id.tab_bluetooth);

        viewPager = view.findViewById(R.id.bluetoothViewpageSpace);

        Log.d(TAG, "Viewpage 작업");
        adapter_ble_rtu_tab = new adapter_Ble_RTU_Tab(this,2);
        viewPager.setAdapter(adapter_ble_rtu_tab);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());

        new TabLayoutMediator(tabLayout_ble, viewPager,
                (tab, position) -> tab.setText(tavTitle[position])
        ).attach();


    }

    public void readData(String data){

        Log.d(TAG, "fragment_Ble_Function readData! : " + data);

        switch (viewPager.getCurrentItem()){
            case 0:
                Log.d(TAG, "currentPage : Status");
                fragment_ble_rtu_status = (fragment_Ble_RTU_Status) fragmentManager.findFragmentByTag("f" + viewPager.getCurrentItem());
                try {
                    fragment_ble_rtu_status.readData(data);
                }catch (Exception e){
                    Log.e(TAG, "fragment_Ble_RTU readData Error - Status : " + e.toString());
                };
                break;
            case 1:
                Log.d(TAG, "currentPage : Setting");
                fragment_ble_rtu_setting = (fragment_Ble_RTU_Setting) fragmentManager.findFragmentByTag("f" + viewPager.getCurrentItem());
                try {
                    fragment_ble_rtu_setting.readData(data);
                }catch (Exception e){
                    Log.e(TAG, "fragment_Ble_RTU readData Error - Setting : " + e.toString());
                };
                break;
        }
    }


    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }


    public class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "fragment_Ble_Function - onDestroy");

        ((BleMainActivity)getActivity()).disconnectGattServer("fragment_Ble_Function - onDestroy");
    }


}
