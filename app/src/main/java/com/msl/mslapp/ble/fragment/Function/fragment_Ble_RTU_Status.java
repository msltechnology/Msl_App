package com.msl.mslapp.ble.fragment.Function;

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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.msl.mslapp.BleMainActivity;
import com.msl.mslapp.R;
import com.msl.mslapp.RTU.dialog.dialogFragment_rtu_Status_Lantern_ID_Change;
import com.msl.mslapp.RTU.dialog.dialogFragment_rtu_Status_RTU_ID_Change;
import com.msl.mslapp.RTU.dialog.dialogFragment_rtu_Status_Send_Cycle_Change;
import com.msl.mslapp.RTU.dialog.dialogFragment_rtu_Status_Server_1_Change;
import com.msl.mslapp.RTU.dialog.dialogFragment_rtu_Status_Server_2_Change;
import com.msl.mslapp.RTUMainActivity;

import static com.msl.mslapp.BleMainActivity.logData_Ble;
import static com.msl.mslapp.RTUMainActivity.DATA_NUM_1;
import static com.msl.mslapp.RTUMainActivity.DATA_NUM_7;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_CHECKSUM;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_COMMA;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_CR;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_LF;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_START;
import static com.msl.mslapp.RTUMainActivity.DATA_TYPE_MUCMD;
import static com.msl.mslapp.RTUMainActivity.STATUS_CALL;
import static com.msl.mslapp.RTUMainActivity.logData_RTU;
import static com.msl.mslapp.RTUMainActivity.mRTUMain;

public class fragment_Ble_RTU_Status extends Fragment {

    // 로그 이름 용
    public final String TAG = "Msl-Ble-RTU-Status";

    String TotalReadData = "";

    TextView tv_readData_version, tv_readData_RTU_ID, tv_readData_Lantern_ID, tv_readData_status_Interval,
            tv_readData_reset_Interval_1, tv_readData_reset_Interval_2, tv_readData_reset_Interval_3,
            tv_readData_server1_ip, tv_readData_server1_port, tv_readData_server2_ip, tv_readData_server2_port;

    LinearLayout ll_rtu_fragment_status_lantern_ID;

    public static String BLE_rtu_id = "1910000";
    public static String BLE_lantern_id = "255";
    public static String BLE_Server_1 = "000.000.000.000";
    public static String BLE_Server_Port_1 = "00000";
    public static String BLE_Server_2 = "000.000.000.000";
    public static String BLE_Server_Port_2 = "00000";

    View view;

    // Dialog 로 보낼 번들
    Bundle args = new Bundle();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "fragment_BLE_RTU_Status onCreateView" + this.getTag());
        view = inflater.inflate(R.layout.rtu_fragment_status, null);

        // 키값 넣기
        args.putString("from", "ble");

        ll_rtu_fragment_status_lantern_ID = view.findViewById(R.id.ll_rtu_fragment_status_lantern_ID);
        ll_rtu_fragment_status_lantern_ID.setVisibility(View.GONE);
        tv_Setting();
        btn_Setting();

        return view;
    }

    void send(String massage) {
        ((BleMainActivity) getActivity()).BlewriteData("<" + massage);
    }

    Handler handler = new Handler();

    void tv_Setting() {
        tv_readData_version = view.findViewById(R.id.tv_version);
        tv_readData_RTU_ID = view.findViewById(R.id.tv_DevId);
        tv_readData_Lantern_ID = view.findViewById(R.id.tv_LanternId);
        tv_readData_status_Interval = view.findViewById(R.id.tv_Status_Interval);
        tv_readData_reset_Interval_1 = view.findViewById(R.id.tv_reset_Interval_1);
        tv_readData_reset_Interval_2 = view.findViewById(R.id.tv_reset_Interval_2);
        tv_readData_reset_Interval_3 = view.findViewById(R.id.tv_reset_Interval_3);
        tv_readData_server1_ip = view.findViewById(R.id.tv_server_1_ip);
        tv_readData_server1_port = view.findViewById(R.id.tv_server_1_port);
        tv_readData_server2_ip = view.findViewById(R.id.tv_server_2_ip);
        tv_readData_server2_port = view.findViewById(R.id.tv_server_2_port);
    }

    void btn_Setting() {
        FragmentManager fm = this.getChildFragmentManager();

        Button btn_rtu_status_call = view.findViewById(R.id.btn_rtu_status);
        Button btn_rtu_status_send = view.findViewById(R.id.btn_rtu_send);

        Button btn_rtu_id_change = view.findViewById(R.id.btn_DevId);
        Button btn_rtu_lantern_change = view.findViewById(R.id.btn_LanternId);
        Button btn_rtu_send_cycle_change = view.findViewById(R.id.btn_Status_Interval);
        //Button btn_rtu_reset_change = view.findViewById(R.id.btn_reset_Interval);
        Button btn_rtu_server1_change = view.findViewById(R.id.btn_server_1);
        Button btn_rtu_server2_change = view.findViewById(R.id.btn_server_2);

        // RTU Activity를 호출한적이 없어도 가능한가?
        btn_rtu_status_call.setOnClickListener(v -> send(STATUS_CALL));
        btn_rtu_status_send.setOnClickListener(v -> {
            String data = DATA_SIGN_START + DATA_TYPE_MUCMD + DATA_SIGN_COMMA +
                    DATA_NUM_7 + DATA_SIGN_CHECKSUM +
                    DATA_NUM_1 + DATA_NUM_1 +
                    DATA_SIGN_CR + DATA_SIGN_LF;
            send(data);
        });


        btn_rtu_id_change.setOnClickListener(new RTUMainActivity.OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                // 설정값 조회하여 rtu 및 lantern id 값 받아야함.
                send(STATUS_CALL);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        args.putString("lantern_id", BLE_lantern_id);
                        dialogFragment_rtu_Status_RTU_ID_Change customDialog_RTU_ID_Change = new dialogFragment_rtu_Status_RTU_ID_Change();
                        customDialog_RTU_ID_Change.setArguments(args);
                        customDialog_RTU_ID_Change.show(fm, "dialogFragment_rtu_Status_RTU_ID_Change");
                    }
                }, 400);
            }
        });
        btn_rtu_lantern_change.setOnClickListener(new RTUMainActivity.OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                // 설정값 조회하여 rtu 및 lantern id 값 받아야함.
                send(STATUS_CALL);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        args.putString("rtu_id", BLE_rtu_id);
                        dialogFragment_rtu_Status_Lantern_ID_Change customDialog_Lantern_ID_Change = new dialogFragment_rtu_Status_Lantern_ID_Change();
                        customDialog_Lantern_ID_Change.setArguments(args);
                        customDialog_Lantern_ID_Change.show(fm, "dialogFragment_rtu_Status_Lantern_ID_Change");
                    }
                }, 400);
            }
        });

        btn_rtu_send_cycle_change.setOnClickListener(new RTUMainActivity.OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                send(STATUS_CALL);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialogFragment_rtu_Status_Send_Cycle_Change customDialog_Send_Cycle_Change = new dialogFragment_rtu_Status_Send_Cycle_Change();
                        customDialog_Send_Cycle_Change.setArguments(args);
                        customDialog_Send_Cycle_Change.show(fm, "dialogFragment_rtu_Status_Send_Cycle_Change");
                    }
                }, 400);
            }
        });
        //btn_rtu_reset_change.setOnClickListener(v ->);
        btn_rtu_server1_change.setOnClickListener(new RTUMainActivity.OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                send(STATUS_CALL);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        args.putString("server2", BLE_Server_2);
                        args.putString("server2port", BLE_Server_Port_2);
                        dialogFragment_rtu_Status_Server_1_Change customDialog_Server_1_Change = new dialogFragment_rtu_Status_Server_1_Change();
                        customDialog_Server_1_Change.setArguments(args);
                        customDialog_Server_1_Change.show(fm, "dialogFragment_rtu_Status_Server_1_Change");
                    }
                }, 400);
            }
        });

        btn_rtu_server2_change.setOnClickListener(new RTUMainActivity.OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                send(STATUS_CALL);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        args.putString("server1", BLE_Server_1);
                        args.putString("server1port", BLE_Server_Port_1);
                        dialogFragment_rtu_Status_Server_2_Change customDialog_Server_2_Change = new dialogFragment_rtu_Status_Server_2_Change();
                        customDialog_Server_2_Change.setArguments(args);
                        customDialog_Server_2_Change.show(fm, "dialogFragment_rtu_Status_Server_2_Change");
                    }
                }, 400);
            }
        });
    }

    public void readData(String data) {
        Log.d(TAG, "fragment_BLE_RTU_Status readData 들어옴 : " + data);
        TotalReadData += data;

        Log.d(TAG, "fragment_BLE_RTU_Status TotalReadData : " + TotalReadData);

        int configIndex = 0;
        int lfIndex = 0;

        if (TotalReadData.contains("[ ConfMsg]") && TotalReadData.contains(">")) {

            configIndex = TotalReadData.indexOf("[ ConfMsg]");
            lfIndex = TotalReadData.indexOf(">", configIndex);
            while (configIndex < lfIndex) {

                String readData = TotalReadData.substring(configIndex, lfIndex);

                try {
                    TotalReadData = TotalReadData.substring(lfIndex + 1);
                } catch (Exception e) {
                    TotalReadData = "";
                    logData_Ble("readData Error! - TotalReadData", "error");
                    Log.e(TAG, "fragment_BLE_RTU_Status readData Error : " + e.toString());
                }

                if (readData.contains("Low Power") || readData.contains("DebugMode") || readData.contains("Phone Number") || readData.contains("Reset Time")) {
                    configIndex = TotalReadData.indexOf("[ ConfMsg]");
                    lfIndex = TotalReadData.indexOf("\n", configIndex);
                    if (configIndex < 0 | lfIndex < 0) {
                        break;
                    }
                } else {

                }


                Log.d(TAG, "fragment_BLE_RTU_Status readData : " + readData);

                //logData_Ble(readData, "read");

                readData = readData.replace("[ ConfMsg] ", "");

                if (readData.contains("Verion")) {
                    readData = readData.replace("Verion:", ""); //RTU 자체에 오타 있음 (version = Verion).
                    tv_readData_version.setText("RTU Version : " + readData);
                } else if (readData.contains("Version")) {
                    readData = readData.replace("Version:", ""); //RTU 자체에 오타 있음 (version = Verion).
                    tv_readData_version.setText(readData);
                } else if (readData.contains("DevID")) { //RTU ID
                    readData = readData.replace("DevID:", "");
                    BLE_rtu_id = readData;
                    tv_readData_RTU_ID.setText(readData);
                } else if (readData.contains("LanternID")) { //등명기 ID
                    readData = readData.replace("LanternID:", "");
                    BLE_lantern_id = readData;
                    tv_readData_Lantern_ID.setText(readData);
                } else if (readData.contains("Status Interval")) { //상태 전송주기
                    readData = readData.replace("Status Interval:", "");
                    tv_readData_status_Interval.setText(readData + " Min");
                } else if (readData.contains("Reset Time")) { //리셋 주기
                    readData = readData.replace("Reset Time:", "");
                    String[] resetData = readData.split(",");
                    try {
                        tv_readData_reset_Interval_1.setText(resetData[0]);
                        tv_readData_reset_Interval_2.setText(resetData[1]);
                        tv_readData_reset_Interval_3.setText(resetData[2]);
                    } catch (Exception e) {
                        logData_Ble("readData Error! - reset_cycle", "error");
                        Log.d(TAG, "readData reset_Interval Error : " + e.toString());
                    }
                } else if (readData.contains("Server #01")) {
                    readData = readData.replace("Server #01 ", "");
                    String[] serverData = readData.split(":");
                    for (int i = 0; i < serverData.length; i++) {
                        Log.d(TAG, "fragment_BLE_RTU_Status serverData : " + i + "번째 : " + serverData[i]);
                    }
                    String serverDataArr[] = serverData[0].split("\\.");

                    BLE_Server_1 = serverDataArr[0] + "," + serverDataArr[1] + "," + serverDataArr[2] + "," + serverDataArr[3];
                    BLE_Server_Port_1 = serverData[1];
                    tv_readData_server1_ip.setText("IP : " + serverData[0]);
                    tv_readData_server1_port.setText("Port : " + serverData[1]);
                } else if (readData.contains("Server #02")) {
                    readData = readData.replace("Server #02 ", "");
                    String[] serverData = readData.split(":");
                    for (int i = 0; i < serverData.length; i++) {
                        Log.d(TAG, "fragment_BLE_RTU_Status serverData2 : " + i + "번째 : " + serverData[i]);
                    }
                    String serverDataArr[] = serverData[0].split("\\.");

                    BLE_Server_2 = serverDataArr[0] + "," + serverDataArr[1] + "," + serverDataArr[2] + "," + serverDataArr[3];
                    BLE_Server_Port_2 = serverData[1];
                    tv_readData_server2_ip.setText("IP : " + serverData[0]);
                    tv_readData_server2_port.setText("Port : " + serverData[1]);
                    Toast.makeText(mRTUMain, "Data Receive Success!", Toast.LENGTH_SHORT).show();
                }
                // Setting 관련
                else if (readData.contains("Use 0x51")) { //프로토콜 변경 여부 상태
                    readData = readData.replace("Use 0x51:", "");

                } else if (readData.contains("GMT Time")) { //GMT 설정 상태
                    readData = readData.replace("GMT Time:", "");

                } else if (readData.contains("Modem Power")) { //모뎀 전원 상태
                    readData = readData.replace("Modem Power:", "");

                } else if (readData.contains("Low Power Mode")) { //Low Power 상태
                    readData = readData.replace("Low Power Mode:", "");

                } else if (readData.contains("Low Power Interval")) { //Low Power 주기
                    readData = readData.replace("Low Power Interval:", "");

                } else if (readData.contains("Low Power Cutoff Voltage")) { //Low Power 모드로 전환되는 V
                    readData = readData.replace("Low Power Cutoff Voltage:", "");

                } else if (readData.contains("Low Power Restore Voltage")) { //Low Power 모드에서 정상모드로 가는 V
                    readData = readData.replace("Low Power Restore Voltage:", "");

                } else if (readData.contains("Phone Number")) { //관리자 전화번호
                    readData = readData.replace("Phone Number:", "");

                } else if (readData.contains("DebugMode")) { //디버그 모드
                    readData = readData.replace("DebugMode:", "");

                }

                configIndex = TotalReadData.indexOf("[ ConfMsg]");
                lfIndex = TotalReadData.indexOf("\n", configIndex);
                if (configIndex < 0 | lfIndex < 0) {
                    break;
                }

            }
        }
    }
}
