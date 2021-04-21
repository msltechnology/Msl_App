package com.example.mslapp.Ble.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mslapp.BleMainActivity;
import com.example.mslapp.R;

import static com.example.mslapp.BleMainActivity.DATA_REQUEST_STATUS;

public class fragment_Ble_Status extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Status";

    TextView tv_ble_status_id, tv_ble_status_input_v, tv_ble_status_output_a, tv_ble_status_cds, tv_ble_status_lantern_status, tv_ble_status_fl,
            tv_ble_status_solar_v, tv_ble_status_battery_v, tv_ble_status_output_v, tv_ble_status_charge_dischar_a, tv_ble_status_battery_percent,
            tv_ble_status_receive_data, tv_ble_status_receive_time, tv_ble_status_gps_latitude, tv_ble_status_gps_longitude;

    ImageView iv_ble_status_battery_percent;

    View view;


    // 상위Activity 에게 데이터 주는 용도
    //private Ble_Status_Listener selecetBleListener;
    private Activity activity;


    public fragment_Ble_Status() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "fragment_Ble_Status onCreateView" + this.getTag());
        view = inflater.inflate(R.layout.ble_fragment_status, null);


        ((Ble_Status_Listener) activity).onCreateViewFragment_Ble_Status();

        buttonSetting();
        textviewSetting();

        iv_ble_status_battery_percent = view.findViewById(R.id.iv_ble_fragment_status_battery_percent);

        // 최초 1회 정보 요청
        ((BleMainActivity) getActivity()).BlewriteData(DATA_REQUEST_STATUS);

        return view;
    }

    void buttonSetting() {

        Button statusBtn = view.findViewById(R.id.btn_once_status);
        statusBtn.setOnClickListener(v -> ((BleMainActivity) getActivity()).BlewriteData(DATA_REQUEST_STATUS));

        /*Button testBtn1 = view.findViewById(R.id.test_bt1);
        testBtn1.setOnClickListener(v -> ((BleMainActivity) getActivity()).BlewriteData(CDS_LAMP_ON_READY));
        Button testBtn2 = view.findViewById(R.id.test_bt2);
        testBtn2.setOnClickListener(v -> ((BleMainActivity) getActivity()).BlewriteData(CDS_LAMP_ON_SETTING));
        Button testBtn3 = view.findViewById(R.id.test_bt3);
        testBtn3.setOnClickListener(v -> ((BleMainActivity) getActivity()).BlewriteData(CDS_LAMP_OFF_READY));
        Button testBtn4 = view.findViewById(R.id.test_bt4);
        testBtn4.setOnClickListener(v -> ((BleMainActivity) getActivity()).BlewriteData(CDS_LAMP_OFF_SETTING));
        Button testBtn5 = view.findViewById(R.id.test_bt5);
        testBtn5.setOnClickListener(v -> ((BleMainActivity) getActivity()).BlewriteData(DATA_DEVICE_RESET));*/
    }

    void textviewSetting() {

        tv_ble_status_id = view.findViewById(R.id.tv_ble_fragment_status_id);
        tv_ble_status_input_v = view.findViewById(R.id.tv_ble_fragment_status_input_v);
        tv_ble_status_output_a = view.findViewById(R.id.tv_ble_fragment_status_output_a);
        tv_ble_status_cds = view.findViewById(R.id.tv_ble_fragment_status_cds);
        tv_ble_status_lantern_status = view.findViewById(R.id.tv_ble_fragment_status_lantern_status);
        tv_ble_status_fl = view.findViewById(R.id.tv_ble_fragment_status_fl);
        tv_ble_status_solar_v = view.findViewById(R.id.tv_ble_fragment_status_solar_v);
        tv_ble_status_battery_v = view.findViewById(R.id.tv_ble_fragment_status_battery_v);
        tv_ble_status_output_v = view.findViewById(R.id.tv_ble_fragment_status_output_v);
        tv_ble_status_charge_dischar_a = view.findViewById(R.id.tv_ble_fragment_status_charge_dischar_a);
        tv_ble_status_battery_percent = view.findViewById(R.id.tv_ble_fragment_status_battery_percent);
        tv_ble_status_receive_data = view.findViewById(R.id.tv_ble_fragment_status_receive_date);
        tv_ble_status_receive_time = view.findViewById(R.id.tv_ble_fragment_status_receive_time);
        tv_ble_status_gps_latitude = view.findViewById(R.id.tv_ble_fragment_status_gps_latitude);
        tv_ble_status_gps_longitude = view.findViewById(R.id.tv_ble_fragment_status_gps_longitude);

    }


    public void readData(String data) {
        if (data.substring(1, 6).contains("LISTS")) {
            if(data.startsWith("S", 7)){

            }else{
                Log.d(TAG, "readData 데이터 읽기");
                String[] data_arr = data.split(",");
                Log.d(TAG, "data_arr length" + data_arr.length);



                tv_ble_status_id.setText(data_arr[1]);
                tv_ble_status_input_v.setText(data_arr[2]+"V");
                tv_ble_status_output_a.setText(data_arr[3]+"A");
                if(data_arr[4].equals("0")){
                    tv_ble_status_cds.setText(getString(R.string.ble_status_cds_0));
                }else {
                    tv_ble_status_cds.setText(getString(R.string.ble_status_cds_1));
                }
                if(data_arr[5].equals("0")){
                    tv_ble_status_lantern_status.setText(getString(R.string.ble_status_lantern_status_0));
                }else {
                    tv_ble_status_lantern_status.setText(getString(R.string.ble_status_lantern_status_1));
                }
                tv_ble_status_fl.setText(data_arr[6]);
                tv_ble_status_solar_v.setText(data_arr[7]+"V");
                tv_ble_status_battery_v.setText(data_arr[8]+"V");
                tv_ble_status_output_v.setText(data_arr[9]+"V");
                tv_ble_status_charge_dischar_a.setText(data_arr[10]+"A");
                tv_ble_status_battery_percent.setText(data_arr[11]+"%");
                int battery_percent = Integer.parseInt(data_arr[11]);
                if(battery_percent>=75){
                    iv_ble_status_battery_percent.setImageResource(R.drawable.green_battery);
                }else if(battery_percent>=50){
                    iv_ble_status_battery_percent.setImageResource(R.drawable.yellow_battery);
                }else if(battery_percent>=25){
                    iv_ble_status_battery_percent.setImageResource(R.drawable.brown_battery);
                }else {
                    iv_ble_status_battery_percent.setImageResource(R.drawable.red_battery);
                }
                String[] receiveData = data_arr[12].split("\\.");
                Log.d(TAG, "receiveData" + receiveData[0] + " " + receiveData[1]);
                String receiveData_year = receiveData[0];
                String receiveData_mon = receiveData[1].substring(0,2);
                String receiveData_day = receiveData[1].substring(2);
                tv_ble_status_receive_data.setText(
                        receiveData_year + getString(R.string.year) + " " +  receiveData_mon + getString(R.string.month) + " " +  receiveData_day + getString(R.string.day));

                String receiveTime_hour = data_arr[13].substring(0,2);
                String receiveTime_min = data_arr[13].substring(2,4);
                String receiveTime_sec = data_arr[13].substring(4);
                tv_ble_status_receive_time.setText(
                        receiveTime_hour + getString(R.string.hour) + " " +  receiveTime_min + getString(R.string.min) + " " +  receiveTime_sec + getString(R.string.sec));
                tv_ble_status_gps_latitude.setText(data_arr[14]);
                String longitude = data_arr[15];
                if(longitude.contains("*")){
                    longitude = longitude.substring(0,longitude.indexOf("*"));
                }
                tv_ble_status_gps_longitude.setText(longitude);
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
        if (context instanceof Ble_Status_Listener) {
            //selecetBleListener = (Ble_Status_Listener) context;
            this.activity = (Activity) context;
        } else {
            throw new RuntimeException((context.toString() + " must implement SelectBleListener"));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
        ((Ble_Status_Listener) activity).onDetachFragment_Ble_Status();
        //selecetBleListener = null;
    }

    public interface Ble_Status_Listener {
        void onCreateViewFragment_Ble_Status();

        void onDetachFragment_Ble_Status();
    }


}
