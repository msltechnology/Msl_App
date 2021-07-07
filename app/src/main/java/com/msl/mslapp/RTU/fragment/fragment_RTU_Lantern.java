package com.msl.mslapp.RTU.fragment;

import android.os.Bundle;
import android.os.Handler;
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

import com.msl.mslapp.R;

import static com.msl.mslapp.BleMainActivity.DATA_REQUEST_STATUS;
import static com.msl.mslapp.RTU.fragment.fragment_RTU_Function.send;
import static com.msl.mslapp.RTUMainActivity.DATA_NUM_1;
import static com.msl.mslapp.RTUMainActivity.DATA_NUM_2;
import static com.msl.mslapp.RTUMainActivity.DATA_NUM_99;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_CHECKSUM;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_COMMA;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_CR;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_LF;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_START;
import static com.msl.mslapp.RTUMainActivity.DATA_TYPE_MUCMD;
import static com.msl.mslapp.RTUMainActivity.logData_RTU;

public class fragment_RTU_Lantern extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-RTU-Lantern";

    String TotalReadData = "";

    TextView tv_rtu_status_id, tv_rtu_status_input_v, tv_rtu_status_output_a, tv_rtu_status_cds, tv_rtu_status_lantern_status, tv_rtu_status_fl,
            tv_rtu_status_solar_v, tv_rtu_status_battery_v, tv_rtu_status_output_v, tv_rtu_status_charge_dischar_a, tv_rtu_status_battery_percent,
            tv_rtu_status_receive_data, tv_rtu_status_receive_time, tv_rtu_status_gps_latitude, tv_rtu_status_gps_longitude;

    Button btn_once_status;

    ImageView iv_ble_status_battery_percent;

    String call_Lantern_Info = DATA_REQUEST_STATUS + "4C" +
            DATA_SIGN_CR + DATA_SIGN_LF;

    String RUT_Debug_On = DATA_SIGN_START + DATA_TYPE_MUCMD + DATA_SIGN_COMMA +
            DATA_NUM_99 + DATA_SIGN_COMMA +
            DATA_NUM_1 + DATA_SIGN_CHECKSUM +
            DATA_NUM_1 + DATA_NUM_1 +
            DATA_SIGN_CR + DATA_SIGN_LF;

    String RUT_Debug_Off = DATA_SIGN_START + DATA_TYPE_MUCMD + DATA_SIGN_COMMA +
            DATA_NUM_99 + DATA_SIGN_COMMA +
            DATA_NUM_2 + DATA_SIGN_CHECKSUM +
            DATA_NUM_1 + DATA_NUM_1 +
            DATA_SIGN_CR + DATA_SIGN_LF;

    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "fragment_RTU_Lantern onCreateView" + this.getTag());

        view = inflater.inflate(R.layout.rtu_fragment_lantern, null);

        tv_Setting();
        btn_Setting();

        iv_ble_status_battery_percent = view.findViewById(R.id.iv_rtu_fragment_lantern_battery_percent);

        return view;
    }

    void tv_Setting() {

        tv_rtu_status_id = view.findViewById(R.id.tv_rtu_fragment_status_id);
        tv_rtu_status_input_v = view.findViewById(R.id.tv_rtu_fragment_status_input_v);
        tv_rtu_status_output_a = view.findViewById(R.id.tv_rtu_fragment_status_output_a);
        tv_rtu_status_cds = view.findViewById(R.id.tv_rtu_fragment_status_cds);
        tv_rtu_status_lantern_status = view.findViewById(R.id.tv_rtu_fragment_status_lantern_status);
        tv_rtu_status_fl = view.findViewById(R.id.tv_rtu_fragment_status_fl);
        tv_rtu_status_solar_v = view.findViewById(R.id.tv_rtu_fragment_status_solar_v);
        tv_rtu_status_battery_v = view.findViewById(R.id.tv_rtu_fragment_status_battery_v);
        tv_rtu_status_output_v = view.findViewById(R.id.tv_rtu_fragment_status_output_v);
        tv_rtu_status_charge_dischar_a = view.findViewById(R.id.tv_rtu_fragment_status_charge_dischar_a);
        tv_rtu_status_battery_percent = view.findViewById(R.id.tv_rtu_fragment_status_battery_percent);
        tv_rtu_status_receive_data = view.findViewById(R.id.tv_rtu_fragment_status_receive_date);
        tv_rtu_status_receive_time = view.findViewById(R.id.tv_rtu_fragment_status_receive_time);
        tv_rtu_status_gps_latitude = view.findViewById(R.id.tv_rtu_fragment_status_gps_latitude);
        tv_rtu_status_gps_longitude = view.findViewById(R.id.tv_rtu_fragment_status_gps_longitude);
    }

    void btn_Setting() {
        btn_once_status = view.findViewById(R.id.btn_rtu_lantern_status);

        btn_once_status.setOnClickListener(v -> {

            send(RUT_Debug_On);

            mHandler.postDelayed(() -> send(call_Lantern_Info), 50);

            mHandler.postDelayed(() -> send(RUT_Debug_Off), 400);
        });

    }

    Handler mHandler = new Handler();


    public void readData(String data) {

        Log.d(TAG, "fragment_RTU_Lantern readData 들어옴 : " + data);
        TotalReadData += data;

        int configIndex = 0;
        int lfIndex = 0;

        if (TotalReadData.contains("$LISTS") && TotalReadData.contains("\n")) {

            configIndex = TotalReadData.indexOf("$LISTS");
            lfIndex = TotalReadData.indexOf("\n", configIndex);
            while (configIndex < lfIndex) {

                String readData = TotalReadData.substring(configIndex, lfIndex);

                Log.d(TAG, "fragment_RTU_Setting readData : " + readData);

                try {
                    TotalReadData = TotalReadData.substring(lfIndex + 1);
                } catch (Exception e) {
                    logData_RTU("readData Error! - TotalReadData", "error");
                    TotalReadData = "";
                    Log.e(TAG, "fragment_RTU_Setting readData Error : " + e.toString());
                }

                logData_RTU(readData, "read");

                readData = readData.substring(0,readData.indexOf("*"));

                String[] data_arr = readData.split(",");

                Log.d(TAG, "data_arr length" + data_arr.length);

                tv_rtu_status_id.setText(data_arr[1]);
                tv_rtu_status_input_v.setText(data_arr[2] + "V");
                tv_rtu_status_output_a.setText(data_arr[3] + "A");
                if (data_arr[4].equals("0")) {
                    tv_rtu_status_cds.setText(getString(R.string.ble_status_cds_0));
                } else {
                    tv_rtu_status_cds.setText(getString(R.string.ble_status_cds_1));
                }
                if (data_arr[5].equals("0")) {
                    tv_rtu_status_lantern_status.setText(getString(R.string.ble_status_lantern_status_0));
                } else {
                    tv_rtu_status_lantern_status.setText(getString(R.string.ble_status_lantern_status_1));
                }
                tv_rtu_status_fl.setText(data_arr[6]);
                tv_rtu_status_solar_v.setText(data_arr[7] + "V");
                tv_rtu_status_battery_v.setText(data_arr[8] + "V");
                tv_rtu_status_output_v.setText(data_arr[9] + "V");
                tv_rtu_status_charge_dischar_a.setText(data_arr[10] + "A");
                tv_rtu_status_battery_percent.setText(data_arr[11] + "%");
                int battery_percent = Integer.parseInt(data_arr[11]);
                if (battery_percent >= 75) {
                    iv_ble_status_battery_percent.setImageResource(R.drawable.battery_100);
                } else if (battery_percent >= 50) {
                    iv_ble_status_battery_percent.setImageResource(R.drawable.battery_75);
                } else if (battery_percent >= 25) {
                    iv_ble_status_battery_percent.setImageResource(R.drawable.battery_50);
                } else {
                    iv_ble_status_battery_percent.setImageResource(R.drawable.battery_25);
                }

                String receiveTime_hour;
                String receiveTime_min = data_arr[13].substring(2, 4);
                String receiveTime_sec = data_arr[13].substring(4);

                String[] receiveData = data_arr[12].split("\\.");
                String receiveData_year = receiveData[0];
                String receiveData_mon = receiveData[1].substring(0, 2);
                String receiveData_day = receiveData[1].substring(2);

                // gmt 으로 인한 9시간 추가해야함. 15시 이후면 다음날로 측정하여 계산
                if (Integer.parseInt(data_arr[13].substring(0, 2)) >= 15) {
                    // 15시간을 뺀 시간
                    receiveTime_hour = (Integer.parseInt(data_arr[13].substring(0, 2)) - 15) + "";
                    // 1일을 더 넣어서 계산
                    int day = Integer.parseInt(receiveData[1].substring(2));
                    switch (receiveData[1].substring(0, 2)) {
                        case "01":
                            if (day != 31) {
                                receiveData_day = (day + 1) + "";
                            } else {
                                receiveData_mon = "02";
                                receiveData_day = "01";
                            }
                            break;
                        case "02":
                            if (day != 28) {
                                receiveData_day = (day + 1) + "";
                            } else {
                                receiveData_mon = "03";
                                receiveData_day = "01";
                            }
                            break;
                        case "03":
                            if (day != 31) {
                                receiveData_day = (day + 1) + "";
                            } else {
                                receiveData_mon = "04";
                                receiveData_day = "01";
                            }
                            break;
                        case "04":
                            if (day != 30) {
                                receiveData_day = (day + 1) + "";
                            } else {
                                receiveData_mon = "05";
                                receiveData_day = "01";
                            }
                            break;
                        case "05":
                            if (day != 31) {
                                receiveData_day = (day + 1) + "";
                            } else {
                                receiveData_mon = "06";
                                receiveData_day = "01";
                            }
                            break;
                        case "06":
                            if (day != 30) {
                                receiveData_day = (day + 1) + "";
                            } else {
                                receiveData_mon = "07";
                                receiveData_day = "01";
                            }
                            break;
                        case "07":
                            if (day != 31) {
                                receiveData_day = (day + 1) + "";
                            } else {
                                receiveData_mon = "08";
                                receiveData_day = "01";
                            }
                            break;
                        case "08":
                            if (day != 31) {
                                receiveData_day = (day + 1) + "";
                            } else {
                                receiveData_mon = "09";
                                receiveData_day = "01";
                            }
                            break;
                        case "09":
                            if (day != 30) {
                                receiveData_day = (day + 1) + "";
                            } else {
                                receiveData_mon = "10";
                                receiveData_day = "01";
                            }
                            break;
                        case "10":
                            if (day != 31) {
                                receiveData_day = (day + 1) + "";
                            } else {
                                receiveData_mon = "11";
                                receiveData_day = "01";
                            }
                            break;
                        case "11":
                            if (day != 30) {
                                receiveData_day = (day + 1) + "";
                            } else {
                                receiveData_mon = "12";
                                receiveData_day = "01";
                            }
                            break;
                        case "12":
                            if (day != 31) {
                                receiveData_day = (day + 1) + "";
                            } else {
                                receiveData_year = (Integer.parseInt(receiveData_year) + 1) + "";
                                receiveData_mon = "01";
                                receiveData_day = "01";
                            }
                            break;
                    }

                } else {
                    receiveTime_hour = (Integer.parseInt(data_arr[13].substring(0, 2)) + 9) + "";
                }

                tv_rtu_status_receive_data.setText(
                        receiveData_year + getString(R.string.year) + " " + receiveData_mon + getString(R.string.month) + " " + receiveData_day + getString(R.string.day));

                tv_rtu_status_receive_time.setText(
                        receiveTime_hour + getString(R.string.hour) + " " + receiveTime_min + getString(R.string.min) + " " + receiveTime_sec + getString(R.string.sec));

                tv_rtu_status_gps_latitude.setText(data_arr[14]);
                String longitude = data_arr[15];
                if (longitude.contains("*")) {
                    longitude = longitude.substring(0, longitude.indexOf("*"));
                }
                tv_rtu_status_gps_longitude.setText(longitude);

                configIndex = TotalReadData.indexOf("$LISTS");
                lfIndex = TotalReadData.indexOf("\n", configIndex);
                if (configIndex < 0 | lfIndex < 0) {
                    break;
                }
            }
        }
    }
}
