
package com.msl.mslapp.ble.fragment.Function;

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

import com.msl.mslapp.BleMainActivity;
import com.msl.mslapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import static com.msl.mslapp.BleMainActivity.logData_Ble;
import static com.msl.mslapp.Public.StringList.DATA_TYPE_LISET;
import static com.msl.mslapp.Public.StringList.DATA_TYPE_PS;
import static com.msl.mslapp.BleMainActivity.adminApp;
import static com.msl.mslapp.BleMainActivity.navigation_GPS_Visible;
import static com.msl.mslapp.BleMainActivity.navigation_icon_Change;

public class fragment_Ble_Function extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Function";

    private FragmentActivity myContext;
    View view;
    // bluetooth 탭 (기능)
    TabLayout tabLayout_ble;
    // Viewpager
    private ViewPager2 viewPager;
    adapter_Ble_Function_Tab adapter_ble_Function_tab;

    // 자식 프래그먼트
    fragment_Ble_Status fragment_ble_status;
    fragment_Ble_Setting fragment_ble_setting;
    fragment_Ble_Test fragment_ble_test;
    fragment_Ble_RTU fragment_ble_RTU;

    // 하위 프래그먼트에게 데이터 주는 용도
    FragmentManager fragmentManager;
    public static boolean gps_status = true;

    // Tablayout title 이름
    String[] tavTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "fragment_Ble_Function onCreateView");
        view = inflater.inflate(R.layout.ble_fragment_function, null);


        navigation_GPS_Visible(true);

        fragmentManager = this.getChildFragmentManager();

        navigation_icon_Change("function");

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        tabLayout_ble = view.findViewById(R.id.tab_bluetooth);

        viewPager = view.findViewById(R.id.bluetoothViewpageSpace);

        Log.d(TAG, "Viewpage 작업");

        // admin 버전만 블루투스로 RTU 통신 사용하였으나 일반에도 적용하도록하여 주석
        /*// RTU가 있는버젼
        //tavTitle = new String[]{"Status", "Setting", "Test", "RTU"};
        if (adminApp) {
            tavTitle = new String[]{"Status", "Setting", "Test", "RTU"};

            adapter_ble_Function_tab = new adapter_Ble_Function_Tab(this, 4);
            viewPager.setAdapter(adapter_ble_Function_tab);
            viewPager.setCurrentItem(0);
            viewPager.setOffscreenPageLimit(3);
        } else {
            tavTitle = new String[]{"Status", "Setting", "Test"};

            adapter_ble_Function_tab = new adapter_Ble_Function_Tab(this, 3);
            viewPager.setAdapter(adapter_ble_Function_tab);
            viewPager.setCurrentItem(0);
            viewPager.setOffscreenPageLimit(2);
        }*/

        tavTitle = new String[]{"Status", "Setting", "Test", "RTU"};

        adapter_ble_Function_tab = new adapter_Ble_Function_Tab(this, 4);
        viewPager.setAdapter(adapter_ble_Function_tab);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);

        viewPager.setPageTransformer(new ZoomOutPageTransformer());

        // RTU가 있는버젼
        /*Log.d(TAG, "Viewpage 작업");
        adapter_ble_Function_tab = new adapter_Ble_Function_Tab(this, 4);
        viewPager.setAdapter(adapter_ble_Function_tab);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());*/

        new TabLayoutMediator(tabLayout_ble, viewPager,
                (tab, position) -> tab.setText(tavTitle[position])
        ).attach();


    }

    public void readData(String data) {

        Log.d(TAG, "fragment_Ble_Function readData! : " + data);

        if (data.contains(DATA_TYPE_PS)) {

            if (data.length() > 4) {
                String PsData = data.substring(0, 3);
                if (PsData.equals("$PS")) {
                    logData_Ble("Fun - read PS");
                    // 사용도중 연결이 이상한지 몰라도 갑자기 비밀번호 요청할 때가 있는데 그럴경우 대비해서 비밀번호 자동으로 보낼려 했는데
                    // 3마일 복호화 암호때문에 이상해져서 일단 주석, 3마일 펌웨어 업데이트 완료되면 다시 사용헤도 될듯 - 07-09
                    //BlewriteData("$PS,A," + readPassword + "*");
                }
                //return;
            }
        }

        String[] data_arr = data.split(",");
        if (data_arr[0].contains(DATA_TYPE_LISET)) {

            // GPS Setting
            if (Integer.parseInt(data_arr[4]) == 0) {
                gps_status = false;
            } else if (Integer.parseInt(data_arr[4]) == 1) {
                gps_status = true;
            }

        }


        switch (viewPager.getCurrentItem()) {
            case 0:
                Log.d(TAG, "currentPage : Status");
                fragment_ble_status = (fragment_Ble_Status) fragmentManager.findFragmentByTag("f" + viewPager.getCurrentItem());
                try {
                    fragment_ble_status.readData(data);
                } catch (Exception e) {
                    Log.e(TAG, "fragment_Ble_Function readData Error - Status : " + e.toString());
                }
                ;
                break;
            case 1:
                Log.d(TAG, "currentPage : Setting");
                fragment_ble_setting = (fragment_Ble_Setting) fragmentManager.findFragmentByTag("f" + viewPager.getCurrentItem());
                try {
                    fragment_ble_setting.readData(data);
                } catch (Exception e) {
                    Log.e(TAG, "fragment_Ble_Function readData Error - Setting : " + e.toString());
                }
                ;
                break;
            case 2:
                Log.d(TAG, "currentPage : Test");
                fragment_ble_test = (fragment_Ble_Test) fragmentManager.findFragmentByTag("f" + viewPager.getCurrentItem());
                try {
                    fragment_ble_test.readData(data);
                } catch (Exception e) {
                    Log.e(TAG, "fragment_Ble_Function readData Error - Test: " + e.toString());
                }
                ;
                break;
            case 3:
                Log.d(TAG, "currentPage : RTU");
                fragment_ble_RTU = (fragment_Ble_RTU) fragmentManager.findFragmentByTag("f" + viewPager.getCurrentItem());
                try {
                    fragment_ble_RTU.readData(data);
                } catch (Exception e) {
                    Log.e(TAG, "fragment_Ble_Function readData Error - RTU: " + e.toString());
                }
                ;
                break;
        }
    }


    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
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

        ((BleMainActivity) getActivity()).disconnectGattServer("fragment_Ble_Function - onDestroy");
        navigation_GPS_Visible(false);
    }


}
