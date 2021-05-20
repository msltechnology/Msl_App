package com.example.mslapp.Ble.fragment.Function;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mslapp.BleMainActivity;
import com.example.mslapp.R;
import com.example.mslapp.RTU.dialog.dialogFragment_rtu_Setting_GMT_Change;
import com.example.mslapp.RTU.dialog.dialogFragment_rtu_Setting_Modem_Change;
import com.example.mslapp.RTU.dialog.dialogFragment_rtu_Setting_Protocol_Change;

import static com.example.mslapp.BleMainActivity.logData_Ble;
import static com.example.mslapp.BleMainActivity.mBleContext;
import static com.example.mslapp.RTUMainActivity.DATA_NUM_1;
import static com.example.mslapp.RTUMainActivity.DATA_NUM_6;
import static com.example.mslapp.RTUMainActivity.DATA_NUM_7;
import static com.example.mslapp.RTUMainActivity.DATA_SIGN_CHECKSUM;
import static com.example.mslapp.RTUMainActivity.DATA_SIGN_COMMA;
import static com.example.mslapp.RTUMainActivity.DATA_SIGN_CR;
import static com.example.mslapp.RTUMainActivity.DATA_SIGN_LF;
import static com.example.mslapp.RTUMainActivity.DATA_SIGN_START;
import static com.example.mslapp.RTUMainActivity.DATA_TYPE_MUCMD;
import static com.example.mslapp.RTUMainActivity.STATUS_CALL;

public class fragment_Ble_RTU_Setting extends Fragment {

    // 로그 이름 용

    public static final String TAG = "Msl-Ble-RTU-Setting";

    String TotalReadData = "";

    TextView tv_modem_power, tv_GMT, tv_protocol, tv_TCP, tv_Modem_Num;
    Button btn_rtu_setting_send, btn_rtu_setting_reset, btn_rtu_setting_status, btn_modem_power, btn_GMT, btn_protocol, btn_TCP, btn_Modem_Num;

    String call_TCP = "AT$$TCP_STATE??" +
            DATA_SIGN_CR + DATA_SIGN_LF;

    String call_Modem_Num = "AT$$MDN" +
            DATA_SIGN_CR + DATA_SIGN_LF;

    View view;

    Bundle args = new Bundle();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "fragment_Ble_RTU_Setting onCreateView" + this.getTag());

        view = inflater.inflate(R.layout.rtu_fragment_setting, null);

        args.putString("from", "ble");

        tv_Setting();
        btn_Setting();

        return view;
    }


    void send(String massage){
        ((BleMainActivity) getActivity()).BlewriteData("<"+massage);
    }

    Handler handler = new Handler();

    void tv_Setting() {
        tv_modem_power = view.findViewById(R.id.tv_modem_power);
        tv_GMT = view.findViewById(R.id.tv_GMT);
        tv_protocol = view.findViewById(R.id.tv_protocol);
        tv_TCP = view.findViewById(R.id.tv_TCP);
        tv_Modem_Num = view.findViewById(R.id.tv_Modem_Num);

    }

    void btn_Setting() {
        btn_rtu_setting_send = view.findViewById(R.id.btn_rtu_setting_send);
        btn_rtu_setting_status = view.findViewById(R.id.btn_rtu_setting_status);
        btn_rtu_setting_reset = view.findViewById(R.id.btn_rtu_setting_reset);

        btn_modem_power = view.findViewById(R.id.btn_modem_power);
        btn_GMT = view.findViewById(R.id.btn_GMT);
        btn_protocol = view.findViewById(R.id.btn_protocol);
        btn_TCP = view.findViewById(R.id.btn_TCP);
        btn_Modem_Num = view.findViewById(R.id.btn_Modem_Num);

        btn_rtu_setting_status.setOnClickListener(v -> {
            send(STATUS_CALL);
        });
        btn_rtu_setting_send.setOnClickListener(v -> {
            String data = DATA_SIGN_START + DATA_TYPE_MUCMD + DATA_SIGN_COMMA +
                    DATA_NUM_7 + DATA_SIGN_CHECKSUM +
                    DATA_NUM_1 + DATA_NUM_1 +
                    DATA_SIGN_CR + DATA_SIGN_LF;
            send(data);
        });
        btn_rtu_setting_reset.setOnClickListener(v -> {
            String data = DATA_SIGN_START + DATA_TYPE_MUCMD + DATA_SIGN_COMMA +
                    DATA_NUM_6 + DATA_SIGN_CHECKSUM +
                    DATA_NUM_1 + DATA_NUM_1 +
                    DATA_SIGN_CR + DATA_SIGN_LF;
            send(data);
        });


        btn_modem_power.setOnClickListener(v -> {
            send(STATUS_CALL);
            FragmentManager fm = this.getChildFragmentManager();
            dialogFragment_rtu_Setting_Modem_Change customDialog_Modem_Change = new dialogFragment_rtu_Setting_Modem_Change();
            customDialog_Modem_Change.setArguments(args);
            customDialog_Modem_Change.show(fm, "dialogFragment_ble_rtu_Setting_Modem_Change");
        });

        btn_GMT.setOnClickListener(v -> {
            send(STATUS_CALL);
            FragmentManager fm = this.getChildFragmentManager();
            dialogFragment_rtu_Setting_GMT_Change customDialog_GMT_Change = new dialogFragment_rtu_Setting_GMT_Change();
            customDialog_GMT_Change.setArguments(args);
            customDialog_GMT_Change.show(fm, "dialogFragment_ble_rtu_Setting_GMT_Change");
        });

        btn_protocol.setOnClickListener(v -> {
            send(STATUS_CALL);
            FragmentManager fm = this.getChildFragmentManager();
            dialogFragment_rtu_Setting_Protocol_Change customDialog_Protocol_Change = new dialogFragment_rtu_Setting_Protocol_Change();
            customDialog_Protocol_Change.setArguments(args);
            customDialog_Protocol_Change.show(fm, "dialogFragment_ble_rtu_Setting_Protocol_Change");
        });

        btn_TCP.setOnClickListener(v -> {
            send(call_TCP);
        });

        btn_Modem_Num.setOnClickListener(v -> {
            send(call_Modem_Num);
        });
    }


    public void readData(String data) {
        Log.d(TAG, "fragment_Ble_RTU_Setting readData 들어옴 : " + data);
        TotalReadData += data;

        Log.d(TAG, "fragment_Ble_RTU_Setting TotalReadData : " + TotalReadData);

        int configIndex = 0;
        int lfIndex = 0;

        if (TotalReadData.contains("[ ConfMsg]") && TotalReadData.contains("\n")) {

            configIndex = TotalReadData.indexOf("[ ConfMsg]");
            lfIndex = TotalReadData.indexOf("\n", configIndex);
            while (configIndex < lfIndex) {

                String readData = TotalReadData.substring(configIndex, lfIndex);

                try {
                    TotalReadData = TotalReadData.substring(lfIndex + 1);
                } catch (Exception e) {
                    logData_Ble("readData Error! - TotalReadData", "error");
                    TotalReadData = "";
                    Log.e(TAG, "fragment_RTU_Setting readData Error : " + e.toString());
                }

                if (readData.contains("Low Power") || readData.contains("DebugMode") || readData.contains("Phone Number") || readData.contains("Reset Time")) {
                    configIndex = TotalReadData.indexOf("[ ConfMsg]");
                    lfIndex = TotalReadData.indexOf("\n", configIndex);
                    if (configIndex < 0 | lfIndex < 0) {
                        break;
                    }
                } else {
                    logData_Ble(readData, "read");
                }

                Log.d(TAG, "fragment_Ble_RTU_Setting readData : " + readData);

                readData = readData.replace("[ ConfMsg] ", "");

                if (readData.contains("Use 0x51")) { //프로토콜 변경 여부 상태
                    readData = readData.replace("Use 0x51:", "");
                    readData = readData.trim();
                    if (readData.equals("0")) {
                        tv_protocol.setText(getString(R.string.RTU_protocol_standard));
                    } else if (readData.equals("1")) {
                        tv_protocol.setText(getString(R.string.RTU_protocol_private));
                    }
                } else if (readData.contains("GMT Time")) { //GMT 설정 상태
                    readData = readData.replace("GMT Time:", "");
                    readData = readData.trim();
                    if (readData.equals("0")) {
                        tv_GMT.setText("+9(KOR)");
                    } else if (readData.equals("1")) {
                        tv_GMT.setText("0(ENG)");
                    }
                } else if (readData.contains("Modem Power")) { //모뎀 전원 상태
                    readData = readData.replace("Modem Power:", "");
                    readData = readData.trim();
                    if (readData.equals("0")) {
                        tv_modem_power.setText("OFF");
                    } else if (readData.equals("1")) {
                        tv_modem_power.setText("ON");
                    }
                    Toast.makeText(mBleContext, "Data Receive Success!", Toast.LENGTH_SHORT).show();
                } else if (readData.contains("Reset Time")) { //리셋 주기
                    readData = readData.replace("Reset Time:", "");
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
                // Status
                else if (readData.contains("Verion")) {
                    readData = readData.replace("Verion:", ""); //RTU 자체에 오타 있음 (version = Verion).
                } else if (readData.contains("Version")) {
                    readData = readData.replace("Version:", ""); //RTU 자체에 오타 있음 (version = Verion).
                } else if (readData.contains("DevID")) { //RTU ID
                    readData = readData.replace("DevID:", "");
                } else if (readData.contains("LanternID")) { //등명기 ID
                    readData = readData.replace("LanternID:", "");
                } else if (readData.contains("Status Interval")) { //상태 전송주기
                    readData = readData.replace("Status Interval:", "");
                } else if (readData.contains("Server #01")) {
                    readData = readData.replace("Server #01 ", "");
                } else if (readData.contains("Server #02")) {
                    readData = readData.replace("Server #02 ", "");
                }


                configIndex = TotalReadData.indexOf("[ ConfMsg]");
                lfIndex = TotalReadData.indexOf("\n", configIndex);
                if (configIndex < 0 | lfIndex < 0) {
                    break;
                }

            }
        } else if (TotalReadData.contains("[ModemMsg]") && TotalReadData.contains("\n")) {
            configIndex = TotalReadData.indexOf("[ModemMsg]");
            lfIndex = TotalReadData.indexOf("\n", configIndex);
            while (configIndex < lfIndex) {

                String readData = TotalReadData.substring(configIndex, lfIndex);

                Log.d(TAG, "fragment_Ble_RTU_Setting readData : " + readData);

                try {
                    TotalReadData = TotalReadData.substring(lfIndex + 1);
                } catch (Exception e) {
                    logData_Ble("readData Error! - TotalReadData", "error");
                    TotalReadData = "";
                    Log.e(TAG, "fragment_Ble_RTU_Setting readData Error : " + e.toString());
                }

                readData = readData.replace("[ModemMsg] ", "");

                if (readData.contains("$$TCP_STATE: ")) { // TCP 상태 확인
                    logData_Ble(readData, "read");
                    readData = readData.replace("$$TCP_STATE: ", "");
                    readData = readData.trim();
                    String[] readDataArr = readData.split(",");

                    if (readDataArr[0].equals("2")) {
                        tv_TCP.setText("Opened");
                    } else if (readDataArr[0].equals("1")) {
                        tv_TCP.setText("Closed");
                    } else if (readDataArr[0].equals("0")) {
                        tv_TCP.setText("NET OFF");
                    }
                } else if (readData.contains("Phone Number")) { //GMT 설정 상태
                    logData_Ble(readData, "read");
                    readData = readData.replace("Phone Number: ", "");
                    readData = readData.trim();

                    tv_Modem_Num.setText(readData);
                }

                configIndex = TotalReadData.indexOf("[ModemMsg]");
                lfIndex = TotalReadData.indexOf("\n", configIndex);
                if (configIndex < 0 | lfIndex < 0) {
                    break;
                }
            }
        }
    }
}
