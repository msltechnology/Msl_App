package com.msl.mslapp.RTU.fragment;

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

import com.msl.mslapp.R;
import com.msl.mslapp.RTU.dialog.dialogFragment_rtu_Setting_GMT_Change;
import com.msl.mslapp.RTU.dialog.dialogFragment_rtu_Setting_Modem_Change;
import com.msl.mslapp.RTU.dialog.dialogFragment_rtu_Setting_Protocol_Change;
import com.msl.mslapp.RTUMainActivity;

import static com.msl.mslapp.Public.StringList.DATA_REQUEST_STATUS;
import static com.msl.mslapp.RTU.fragment.fragment_RTU_Function.send;
import static com.msl.mslapp.RTUMainActivity.DATA_NUM_1;
import static com.msl.mslapp.RTUMainActivity.DATA_NUM_2;
import static com.msl.mslapp.RTUMainActivity.DATA_NUM_6;
import static com.msl.mslapp.RTUMainActivity.DATA_NUM_7;
import static com.msl.mslapp.RTUMainActivity.DATA_NUM_99;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_CHECKSUM;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_COMMA;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_CR;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_LF;
import static com.msl.mslapp.RTUMainActivity.DATA_SIGN_START;
import static com.msl.mslapp.RTUMainActivity.DATA_TYPE_MUCMD;
import static com.msl.mslapp.RTUMainActivity.STATUS_CALL;
import static com.msl.mslapp.RTUMainActivity.logData_RTU;
import static com.msl.mslapp.RTUMainActivity.mRTUMain;

public class fragment_RTU_Setting extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-RTU-Setting";

    String TotalReadData = "";

    TextView tv_modem_power, tv_GMT, tv_protocol, tv_Modem_Num, tv_Network, tv_Socket;
    Button btn_rtu_setting_send, btn_rtu_setting_reset, btn_rtu_setting_status, btn_modem_power, btn_GMT, btn_protocol, btn_Modem_Num;

    String call_TCP = "AT$$TCP_STATE??" +
            DATA_SIGN_CR + DATA_SIGN_LF;

    String call_Modem_Num = "AT$$MDN" +
            DATA_SIGN_CR + DATA_SIGN_LF;

    String call_Lantern_Info = DATA_REQUEST_STATUS + "4C" +
            DATA_SIGN_CR + DATA_SIGN_LF;

    String RTU_Debug_On = DATA_SIGN_START + DATA_TYPE_MUCMD + DATA_SIGN_COMMA +
            DATA_NUM_99 + DATA_SIGN_COMMA +
            DATA_NUM_1 + DATA_SIGN_CHECKSUM +
            DATA_NUM_1 + DATA_NUM_1 +
            DATA_SIGN_CR + DATA_SIGN_LF;

    String RTU_Debug_Off = DATA_SIGN_START + DATA_TYPE_MUCMD + DATA_SIGN_COMMA +
            DATA_NUM_99 + DATA_SIGN_COMMA +
            DATA_NUM_2 + DATA_SIGN_CHECKSUM +
            DATA_NUM_1 + DATA_NUM_1 +
            DATA_SIGN_CR + DATA_SIGN_LF;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "fragment_RTU_Setting onCreateView" + this.getTag());

        view = inflater.inflate(R.layout.rtu_fragment_setting, null);

        tv_Setting();
        btn_Setting();

        return view;
    }

    void tv_Setting() {
        tv_modem_power = view.findViewById(R.id.tv_modem_power);
        tv_GMT = view.findViewById(R.id.tv_GMT);
        tv_protocol = view.findViewById(R.id.tv_protocol);
        tv_Modem_Num = view.findViewById(R.id.tv_Modem_Num);
        tv_Network = view.findViewById(R.id.tv_Network);
        tv_Socket = view.findViewById(R.id.tv_Socket);

    }

    void btn_Setting() {
        btn_rtu_setting_send = view.findViewById(R.id.btn_rtu_setting_send);
        btn_rtu_setting_status = view.findViewById(R.id.btn_rtu_setting_status);
        btn_rtu_setting_reset = view.findViewById(R.id.btn_rtu_setting_reset);

        btn_modem_power = view.findViewById(R.id.btn_modem_power);
        btn_GMT = view.findViewById(R.id.btn_GMT);
        btn_protocol = view.findViewById(R.id.btn_protocol);
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

        FragmentManager fm = this.getChildFragmentManager();

        btn_modem_power.setOnClickListener(new RTUMainActivity.OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                // 설정값 조회하여 rtu 및 lantern id 값 받아야함.
                send(STATUS_CALL);
                dialogFragment_rtu_Setting_Modem_Change customDialog_Modem_Change = new dialogFragment_rtu_Setting_Modem_Change();
                customDialog_Modem_Change.show(fm, "dialogFragment_rtu_Setting_Modem_Change");
            }
        });

        btn_GMT.setOnClickListener(new RTUMainActivity.OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                // 설정값 조회하여 rtu 및 lantern id 값 받아야함.
                send(STATUS_CALL);
                dialogFragment_rtu_Setting_GMT_Change customDialog_GMT_Change = new dialogFragment_rtu_Setting_GMT_Change();
                customDialog_GMT_Change.show(fm, "dialogFragment_rtu_Setting_GMT_Change");
            }
        });

        btn_protocol.setOnClickListener(new RTUMainActivity.OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                // 설정값 조회하여 rtu 및 lantern id 값 받아야함.
                send(STATUS_CALL);
                dialogFragment_rtu_Setting_Protocol_Change customDialog_Protocol_Change = new dialogFragment_rtu_Setting_Protocol_Change();
                customDialog_Protocol_Change.show(fm, "dialogFragment_rtu_Setting_Protocol_Change");
            }
        });

        btn_Modem_Num.setOnClickListener(v -> {
            send(call_Modem_Num);
        });
    }

    Handler mHandler = new Handler();


    public void readData(String data) {
        Log.d(TAG, "fragment_RTU_Setting readData 들어옴 : " + data);
        TotalReadData += data;

        Log.d(TAG, "fragment_RTU_Setting TotalReadData : " + TotalReadData);

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
                    logData_RTU("readData Error! - TotalReadData", "error");
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
                    logData_RTU(readData, "read");
                }

                Log.d(TAG, "fragment_RTU_Setting readData : " + readData);

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
                        tv_GMT.setText("0(UTC)");
                    }
                } else if (readData.contains("Modem Power")) { //모뎀 전원 상태
                    readData = readData.replace("Modem Power:", "");
                    readData = readData.trim();
                    if (readData.equals("0")) {
                        tv_modem_power.setText("OFF");
                    } else if (readData.equals("1")) {
                        tv_modem_power.setText("ON");
                    }
                    Toast.makeText(mRTUMain, "Data Receive Success!", Toast.LENGTH_SHORT).show();
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

                Log.d(TAG, "fragment_RTU_Setting readData : " + readData);

                try {
                    TotalReadData = TotalReadData.substring(lfIndex + 1);
                } catch (Exception e) {
                    logData_RTU("readData Error! - TotalReadData", "error");
                    TotalReadData = "";
                    Log.e(TAG, "fragment_RTU_Setting readData Error : " + e.toString());
                }

                readData = readData.replace("[ModemMsg] ", "");

                if (readData.contains("$$MODEM_STATE: ")) { // Modem 상태 확인
                    logData_RTU(readData, "read");
                    readData = readData.replace("$$MODEM_STATE: ", "");
                    readData = readData.trim();
                    String[] readDataArr = readData.split(",");

                    String textNetworkData = "";

                    switch (readDataArr[3]){
                        case "-1":
                            textNetworkData = "확인 중";
                            break;
                        case "0":
                            textNetworkData = "홈 사업자에 등록";
                            break;
                        case "1":
                            textNetworkData = "등록되지 않음.(탐색 취소)";
                            break;
                        case "2":
                            textNetworkData = "등록되지 않음.(탐색 중)";
                            break;
                        case "3":
                            textNetworkData = "등록/가입해지";
                            break;
                        case "5":
                            textNetworkData = "서비스 불가능 지역";
                            break;
                        case "101":
                            textNetworkData = "개통 필요함";
                            break;
                        case "102":
                            textNetworkData = "인증 실패";
                            break;
                        case "103":
                            textNetworkData = "기기인증 실패";
                            break;
                        case "104":
                            textNetworkData = "위치 등록 실패";
                            break;
                        case "107":
                            textNetworkData = "네트워크 등록 실패";
                            break;
                    }

                    tv_Network.setText(textNetworkData);
                    Log.d(TAG, "tv_Network setText : " + textNetworkData);

                    String textSocketData = "";

                    switch (readDataArr[4]){
                        case "0":
                            textSocketData = "Off";
                            break;
                        case "1":
                            textSocketData = "Closed";
                            break;
                        case "2":
                            textSocketData = "Opened";
                            break;
                        case "3":
                            textSocketData = "Opening";
                            break;
                        case "10":
                            textSocketData = "Socket Error";
                            break;
                        case "11":
                            textSocketData = "Socket Off";
                            break;
                        case "12":
                            textSocketData = "Socket UDP Ready";
                            break;
                        case "13":
                            textSocketData = "Socket Tcp Binded";
                            break;
                        case "14":
                            textSocketData = "Socket Tcp Connecting";
                            break;
                        case "15":
                            textSocketData = "Socket Tcp Ready";
                            break;
                        case "16":
                            textSocketData = "Socket Tcp Disconnecting";
                            break;
                    }

                    tv_Socket.setText(textSocketData);


                } else if (readData.contains("Phone Number")) { //GMT 설정 상태
                    logData_RTU(readData, "read");
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
