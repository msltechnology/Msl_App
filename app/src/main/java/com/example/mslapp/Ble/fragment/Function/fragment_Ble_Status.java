package com.example.mslapp.Ble.fragment.Function;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Battery;
import com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Solar;
import com.example.mslapp.BleMainActivity;
import com.example.mslapp.R;

import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Battery.iv_ble_status_bat1;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Battery.iv_ble_status_bat2;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Battery.iv_ble_status_bat3;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Battery.iv_ble_status_bat4;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Battery.iv_ble_status_bat5;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Battery.iv_ble_status_bat6;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Battery.tv_ble_status_bat_value1;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Battery.tv_ble_status_bat_value2;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Battery.tv_ble_status_bat_value3;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Battery.tv_ble_status_bat_value4;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Battery.tv_ble_status_bat_value5;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Battery.tv_ble_status_bat_value6;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Solar.iv_ble_status_sol1;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Solar.iv_ble_status_sol2;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Solar.iv_ble_status_sol3;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Solar.iv_ble_status_sol4;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Solar.iv_ble_status_sol5;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Solar.iv_ble_status_sol6;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Solar.tv_ble_status_sol_value1;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Solar.tv_ble_status_sol_value1_a;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Solar.tv_ble_status_sol_value2;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Solar.tv_ble_status_sol_value2_a;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Solar.tv_ble_status_sol_value3;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Solar.tv_ble_status_sol_value3_a;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Solar.tv_ble_status_sol_value4;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Solar.tv_ble_status_sol_value4_a;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Solar.tv_ble_status_sol_value5;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Solar.tv_ble_status_sol_value5_a;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Solar.tv_ble_status_sol_value6;
import static com.example.mslapp.Ble.Dialog.Status.dialogFragment_Ble_Status_Solar.tv_ble_status_sol_value6_a;
import static com.example.mslapp.BleMainActivity.DATA_REQUEST_STATUS;
import static com.example.mslapp.BleMainActivity.DATA_TYPE_BTV;
import static com.example.mslapp.BleMainActivity.DATA_TYPE_LISTS;
import static com.example.mslapp.BleMainActivity.DATA_TYPE_S;
import static com.example.mslapp.BleMainActivity.adminApp;
import static com.example.mslapp.BleMainActivity.mBleContext;

public class fragment_Ble_Status extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Status";

    private final int MESSAGE_HANDLER_START = 100;
    private final int MESSAGE_HANDLER_WORK = 101;
    private final int MESSAGE_HANDLER_STOP = 102;

    LinearLayout ll_ble_fragment_status_battery_v_detail, ll_ble_fragment_status_solar_v_detail;

    TextView tv_ble_status_id, tv_ble_status_input_v, tv_ble_status_output_a, tv_ble_status_cds, tv_ble_status_lantern_status, tv_ble_status_fl,
            tv_ble_status_solar_v, tv_ble_status_battery_v, tv_ble_status_output_v, tv_ble_status_charge_dischar_a, tv_ble_status_battery_percent,
            tv_ble_status_receive_data, tv_ble_status_receive_time, tv_ble_status_gps_latitude, tv_ble_status_gps_longitude;

    ImageView iv_ble_status_battery_percent;

    Button btn_once_status, btn_cycle_status, btn_battery_detail, btn_solar_detatil;
    Boolean cycleFlag = false;


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

        ll_ble_fragment_status_battery_v_detail = view.findViewById(R.id.ll_ble_fragment_status_battery_v_detail);
        ll_ble_fragment_status_solar_v_detail = view.findViewById(R.id.ll_ble_fragment_status_solar_v_detail);

        buttonSetting();
        textviewSetting();

        iv_ble_status_battery_percent = view.findViewById(R.id.iv_ble_fragment_status_battery_percent);

        // 최초 1회 정보 요청
        ((BleMainActivity) getActivity()).BlewriteData(DATA_REQUEST_STATUS);

        return view;
    }

    void cycle_Status_Clicked() {
        cycleFlag = !cycleFlag;
        if (cycleFlag) {
            btn_cycle_status.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_ble_status_call_clicked_btn));
            cycleHandler.sendEmptyMessage(MESSAGE_HANDLER_START);
        } else {
            btn_cycle_status.setBackground(ContextCompat.getDrawable(mBleContext, R.drawable.custom_ble_status_call_btn));
            cycleHandler.sendEmptyMessage(MESSAGE_HANDLER_STOP);
        }
    }

    void battery_Detail_Clicked() {
        FragmentManager fm = this.getChildFragmentManager();

        dialogFragment_Ble_Status_Battery dialogFragment_ble_status_battery = new dialogFragment_Ble_Status_Battery();
        dialogFragment_ble_status_battery.show(fm, "fragment_status_dialog_bat");
    }

    void solar_Detail_Clicked() {
        FragmentManager fm = this.getChildFragmentManager();
        dialogFragment_Ble_Status_Solar dialogFragment_ble_status_solar = new dialogFragment_Ble_Status_Solar();
        dialogFragment_ble_status_solar.show(fm, "fragment_status_dialog_solar");
    }

    Handler cycleHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MESSAGE_HANDLER_START:
                    ((BleMainActivity) getActivity()).BlewriteData(DATA_REQUEST_STATUS);
                    Log.d(TAG, "handler check MESSAGE_HANDLER_START");
                    this.removeMessages(MESSAGE_HANDLER_WORK);
                    cycleHandler.sendEmptyMessageDelayed(MESSAGE_HANDLER_WORK, 3000);
                    break;
                case MESSAGE_HANDLER_WORK:
                    ((BleMainActivity) getActivity()).BlewriteData(DATA_REQUEST_STATUS);
                    Log.d(TAG, "handler check MESSAGE_HANDLER_WORK");
                    cycleHandler.sendEmptyMessageDelayed(MESSAGE_HANDLER_WORK, 3000);
                    break;
                case MESSAGE_HANDLER_STOP:
                    this.removeMessages(MESSAGE_HANDLER_WORK);
                    break;
            }
        }
    };


    void buttonSetting() {

        btn_once_status = view.findViewById(R.id.btn_once_status);
        btn_once_status.setOnClickListener(v -> ((BleMainActivity) getActivity()).BlewriteData(DATA_REQUEST_STATUS));
        btn_cycle_status = view.findViewById(R.id.btn_cycle_status);
        btn_cycle_status.setOnClickListener(v -> cycle_Status_Clicked());
        btn_battery_detail = view.findViewById(R.id.btn_ble_fragment_status_battery_v_detail);
        btn_battery_detail.setOnClickListener(new BleMainActivity.OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                battery_Detail_Clicked();
            }
        });
        btn_solar_detatil = view.findViewById(R.id.btn_ble_fragment_status_solar_v_detail);
        btn_solar_detatil.setOnClickListener(new BleMainActivity.OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                solar_Detail_Clicked();
            }
        });

        if(!adminApp){
            ll_ble_fragment_status_battery_v_detail.setVisibility(View.GONE);
            ll_ble_fragment_status_solar_v_detail.setVisibility(View.GONE);
        }

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


    @SuppressLint("SetTextI18n")
    public void readData(String data) {
        if (data.substring(1, 6).contains(DATA_TYPE_LISTS)) {
            if (data.startsWith(DATA_TYPE_S, 7)) {
                if (data.substring(9, 12).contains(DATA_TYPE_BTV)) {
                    Log.d(TAG, "readData BTV 들어옴");
                    // 다이얼로그한테 데이터를 보내야함.
                    String[] data_arr = data.split(",");
                    String tv_bat_value6_value = "";
                    if (data_arr[9].contains("*")) {
                        tv_bat_value6_value = data_arr[9].substring(0, data_arr[9].indexOf("*"));
                    }
                    try {
                        tv_ble_status_bat_value1.setText(data_arr[4] + "V");
                        tv_ble_status_bat_value2.setText(data_arr[5] + "V");
                        tv_ble_status_bat_value3.setText(data_arr[6] + "V");
                        tv_ble_status_bat_value4.setText(data_arr[7] + "V");
                        tv_ble_status_bat_value5.setText(data_arr[8] + "V");
                        tv_ble_status_bat_value6.setText(tv_bat_value6_value + "V");
                    } catch (Exception e) {
                        Log.d(TAG, "tv_ble_status_bat_value 설정 간 문제 발생 : " + e);
                    }

                    try {
                        // 배터리 값에 따른 이미지 변경
                        for (int i = 0; i <= 5; i++) {
                            ImageView imageView;
                            switch (i) {
                                case 0:
                                    imageView = iv_ble_status_bat1;
                                    break;
                                case 1:
                                    imageView = iv_ble_status_bat2;
                                    break;
                                case 2:
                                    imageView = iv_ble_status_bat3;
                                    break;
                                case 3:
                                    imageView = iv_ble_status_bat4;
                                    break;
                                case 4:
                                    imageView = iv_ble_status_bat5;
                                    break;
                                default:
                                    imageView = iv_ble_status_bat6;
                                    break;
                            }

                            double bat_value;

                            if (i != 5) {
                                bat_value = Double.parseDouble(data_arr[i + 4]);
                            } else {
                                bat_value = Double.parseDouble(tv_bat_value6_value);
                            }

                            if (bat_value >= 4) {
                                imageView.setBackgroundResource(R.drawable.green_battery);
                            } else if (bat_value >= 3.8) {
                                imageView.setBackgroundResource(R.drawable.yellow_battery);
                            } else if (bat_value >= 3.6) {
                                imageView.setBackgroundResource(R.drawable.brown_battery);
                            } else {
                                imageView.setBackgroundResource(R.drawable.red_battery);
                            }


                        }


                    } catch (Exception e) {
                        Log.d(TAG, "iv_ble_status_bat 설정 간 문제 발생 : " + e);
                    }

                } else if (data.substring(9, 12).contains("SLV")) {
                    Log.d(TAG, "readData SLV 들어옴");
                    // 다이얼로그한테 데이터를 보내야함.
                    String[] data_arr = data.split(",");
                    String tv_sol_value6_value = "";
                    if (data_arr[9].contains("*")) {
                        tv_sol_value6_value = data_arr[9].substring(0, data_arr[9].indexOf("*"));
                    }
                    try {
                        tv_ble_status_sol_value1.setText(data_arr[4] + "V");
                        tv_ble_status_sol_value2.setText(data_arr[5] + "V");
                        tv_ble_status_sol_value3.setText(data_arr[6] + "V");
                        tv_ble_status_sol_value4.setText(data_arr[7] + "V");
                        tv_ble_status_sol_value5.setText(data_arr[8] + "V");
                        tv_ble_status_sol_value6.setText(tv_sol_value6_value + "V");
                    } catch (Exception e) {
                        Log.d(TAG, "tv_ble_status_bat_value 설정 간 문제 발생 : " + e);
                    }

                    try {
                        // 배터리 값에 따른 이미지 변경
                        for (int i = 0; i <= 5; i++) {
                            ImageView imageView;
                            switch (i) {
                                case 0:
                                    imageView = iv_ble_status_sol1;
                                    break;
                                case 1:
                                    imageView = iv_ble_status_sol2;
                                    break;
                                case 2:
                                    imageView = iv_ble_status_sol3;
                                    break;
                                case 3:
                                    imageView = iv_ble_status_sol4;
                                    break;
                                case 4:
                                    imageView = iv_ble_status_sol5;
                                    break;
                                default:
                                    imageView = iv_ble_status_sol6;
                                    break;
                            }

                            double sol_value;

                            if (i != 5) {
                                sol_value = Double.parseDouble(data_arr[i + 4]);
                            } else {
                                sol_value = Double.parseDouble(tv_sol_value6_value);
                            }

                            if (sol_value >= 5) {
                                imageView.setBackgroundResource(R.drawable.green_battery);
                            } else if (sol_value >= 4) {
                                imageView.setBackgroundResource(R.drawable.yellow_battery);
                            } else if (sol_value >= 3) {
                                imageView.setBackgroundResource(R.drawable.brown_battery);
                            } else {
                                imageView.setBackgroundResource(R.drawable.red_battery);
                            }


                        }


                    } catch (Exception e) {
                        Log.d(TAG, "iv_ble_status_bat 설정 간 문제 발생 : " + e);
                    }

                } else if (data.substring(9, 12).contains("SLC")) {
                    Log.d(TAG, "readData SLC 들어옴");
                    // 다이얼로그한테 데이터를 보내야함.
                    String[] data_arr = data.split(",");
                    String tv_sol_value6_value = "";
                    if (data_arr[9].contains("*")) {
                        tv_sol_value6_value = data_arr[9].substring(0, data_arr[9].indexOf("*"));
                    }
                    try {
                        tv_ble_status_sol_value1_a.setText(data_arr[4] + "A");
                        tv_ble_status_sol_value2_a.setText(data_arr[5] + "A");
                        tv_ble_status_sol_value3_a.setText(data_arr[6] + "A");
                        tv_ble_status_sol_value4_a.setText(data_arr[7] + "A");
                        tv_ble_status_sol_value5_a.setText(data_arr[8] + "A");
                        tv_ble_status_sol_value6_a.setText(tv_sol_value6_value + "A");
                    } catch (Exception e) {
                        Log.d(TAG, "tv_ble_status_bat_value 설정 간 문제 발생 : " + e);
                    }
                }

            } else {
                Log.d(TAG, "readData 데이터 읽기");
                String[] data_arr = data.split(",");
                Log.d(TAG, "data_arr length" + data_arr.length);


                tv_ble_status_id.setText(data_arr[1]);
                tv_ble_status_input_v.setText(data_arr[2] + "V");
                tv_ble_status_output_a.setText(data_arr[3] + "A");
                if (data_arr[4].equals("0")) {
                    tv_ble_status_cds.setText(getString(R.string.ble_status_cds_0));
                } else {
                    tv_ble_status_cds.setText(getString(R.string.ble_status_cds_1));
                }
                if (data_arr[5].equals("0")) {
                    tv_ble_status_lantern_status.setText(getString(R.string.ble_status_lantern_status_0));
                } else {
                    tv_ble_status_lantern_status.setText(getString(R.string.ble_status_lantern_status_1));
                }
                tv_ble_status_fl.setText(data_arr[6]);
                tv_ble_status_solar_v.setText(data_arr[7] + "V");
                tv_ble_status_battery_v.setText(data_arr[8] + "V");
                tv_ble_status_output_v.setText(data_arr[9] + "V");
                tv_ble_status_charge_dischar_a.setText(data_arr[10] + "A");
                tv_ble_status_battery_percent.setText(data_arr[11] + "%");
                int battery_percent = Integer.parseInt(data_arr[11]);
                if (battery_percent >= 75) {
                    iv_ble_status_battery_percent.setImageResource(R.drawable.green_battery);
                } else if (battery_percent >= 50) {
                    iv_ble_status_battery_percent.setImageResource(R.drawable.yellow_battery);
                } else if (battery_percent >= 25) {
                    iv_ble_status_battery_percent.setImageResource(R.drawable.brown_battery);
                } else {
                    iv_ble_status_battery_percent.setImageResource(R.drawable.red_battery);
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

                tv_ble_status_receive_data.setText(
                        receiveData_year + getString(R.string.year) + " " + receiveData_mon + getString(R.string.month) + " " + receiveData_day + getString(R.string.day));

                tv_ble_status_receive_time.setText(
                        receiveTime_hour + getString(R.string.hour) + " " + receiveTime_min + getString(R.string.min) + " " + receiveTime_sec + getString(R.string.sec));

                //GPS 좌표 계산
                String[] tempLatitude;
                String lat1, lat2, lat3;
                String[] tempLongitude;
                String lon1, lon2, lon3;

                //위도
                if (Float.valueOf(data_arr[14]) != 0f) {
                    tempLatitude = data_arr[14].split("\\.");
                    if (tempLatitude[0].charAt(0) != '-') {
                        lat1 = tempLatitude[0].substring(0, 2);
                        lat2 = tempLatitude[0].substring(2, 4);
                        lat1 = "N " + lat1;
                    } else {
                        lat1 = tempLatitude[0].substring(1, 3);
                        lat2 = tempLatitude[0].substring(3, 5);
                        lat1 = "S " + lat1;
                    }
                    lat3 = String.valueOf(String.format("%.4f", Double.valueOf("0." + tempLatitude[1]) * 60));

                    tv_ble_status_gps_latitude.setText(lat1 + "° " + lat2 + "\' " + lat3 + "\"");
                } else {
                    tv_ble_status_gps_latitude.setText(data_arr[14].substring(0, 2) + "° " + data_arr[14].substring(2, 4) + "\' " + data_arr[14].substring(5, 7) + "." + data_arr[14].substring(7) + "\"");
                }

                String longitude = data_arr[15];
                if (longitude.contains("*")) {
                    longitude = longitude.substring(0, longitude.indexOf("*"));
                }

                //경도
                if (Float.valueOf(longitude) != 0f) {
                    tempLongitude = longitude.split("\\.");
                    if (tempLongitude[0].charAt(0) != '-') {
                        lon1 = tempLongitude[0].substring(0, 3);
                        lon2 = tempLongitude[0].substring(3, 5);
                        lon1 = "E " + lon1;
                    } else {
                        lon1 = tempLongitude[0].substring(1, 4);
                        lon2 = tempLongitude[0].substring(4, 6);
                        lon1 = "W " + lon1;
                    }
                    lon3 = String.valueOf(String.format("%.4f", Double.valueOf("0." + tempLongitude[1]) * 60));

                    tv_ble_status_gps_longitude.setText(lon1 + "° " + lon2 + "\' " + lon3 + "\"");
                } else {
                    tv_ble_status_gps_longitude.setText(longitude.substring(0, 3) + "° " + longitude.substring(3, 5) + "\' " + longitude.substring(6, 8) + "." + longitude.substring(8, 10) + "\"");
                }





                /*tv_ble_status_gps_latitude.setText(data_arr[14]);
                String longitude = data_arr[15];
                if (longitude.contains("*")) {
                    longitude = longitude.substring(0, longitude.indexOf("*"));
                }
                tv_ble_status_gps_longitude.setText(longitude);*/
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
        cycleHandler.sendEmptyMessage(MESSAGE_HANDLER_STOP);
        ((Ble_Status_Listener) activity).onDetachFragment_Ble_Status();
        //selecetBleListener = null;
    }

    public interface Ble_Status_Listener {
        void onCreateViewFragment_Ble_Status();

        void onDetachFragment_Ble_Status();
    }


}
