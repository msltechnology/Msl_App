package com.example.mslapp.RTU.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mslapp.R;
import com.example.mslapp.RTUMainActivity;

import static com.example.mslapp.RTU.fragment.fragment_RTU_Function.send;
import static com.example.mslapp.RTUMainActivity.DATA_CR;
import static com.example.mslapp.RTUMainActivity.DATA_LF;

public class fragment_RTU_Status extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-RTU-Status";

    String TotalReadData = "";

    TextView readData1;
    TextView readData2;
    TextView readData3;
    TextView readData4;
    TextView readData5;
    TextView readData6;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "fragment_RTU_Status onCreateView" + this.getTag());
        View view = inflater.inflate(R.layout.rtu_fragment_status, null);

        readData1 = view.findViewById(R.id.tv_version);
        readData2 = view.findViewById(R.id.tv_DevId);
        readData3 = view.findViewById(R.id.tv_LanternId);
        readData4 = view.findViewById(R.id.tv_Status_Interval);
        readData5 = view.findViewById(R.id.tv_server_1);
        readData6 = view.findViewById(R.id.tv_server_2);


        Button open_btn = view.findViewById(R.id.rtu_open_btn);
        open_btn.setOnClickListener(v -> send("$MUCMD,8,1*11\r\n"));
        Button send_btn = view.findViewById(R.id.rtu_send_btn);
        //send_btn.setOnClickListener(v -> read());

        return view;
    }

    public static void testData(String data) {
        Log.d(TAG, "fragment_RTU_Status testData 들어옴 : " + data);
    }

    public void readData(String data) {
        Log.d(TAG, "fragment_RTU_Status readData 들어옴 : " + data);
        TotalReadData += data;

        //TotalReadData.replace("\n","");

        Log.d(TAG, "fragment_RTU_Status TotalReadData : " + TotalReadData);


        int configIndex = 0;
        int lfIndex = 0;

        if (TotalReadData.contains("[ ConfMsg]") && TotalReadData.contains("\n")) {

            Log.d(TAG, "fragment_RTU_Status Test1");

            configIndex = TotalReadData.indexOf("[ ConfMsg]");
            lfIndex = TotalReadData.indexOf("\n", configIndex);
            Log.d(TAG, "fragment_RTU_Status Test2 : " + configIndex + " / " + lfIndex);
            while (configIndex < lfIndex) {

                String readData = TotalReadData.substring(configIndex, lfIndex);

                Log.d(TAG, "fragment_RTU_Status readData : " + readData);

                try {
                    TotalReadData = TotalReadData.substring(lfIndex + 1);
                } catch (Exception e) {
                    TotalReadData = "";
                    Log.e(TAG, "fragment_RTU_Status readData Error : " + e.toString());
                }

                readData = readData.replace("[ ConfMsg] ", "");

                if (readData.contains("Verion")) {
                    readData = readData.replace("Verion:", ""); //RTU 자체에 오타 있음 (version = Verion).
                    readData1.setText(readData);
                } else if (readData.contains("Version")) {
                    readData = readData.replace("Version:", ""); //RTU 자체에 오타 있음 (version = Verion).
                    readData1.setText(readData);
                } else if (readData.contains("DevID")) { //RTU ID
                    readData = readData.replace("DevID:", "");
                    readData2.setText(readData);
                } else if (readData.contains("LanternID")) { //등명기 ID
                    readData = readData.replace("LanternID:", "");
                    readData3.setText(readData);
                } else if (readData.contains("Status Interval")) { //상태 전송주기
                    readData = readData.replace("Status Interval:", "");
                    readData4.setText(readData);
                } else if (readData.contains("Server #01")) {
                    readData = readData.replace("Server #01 ", "");
                    String[] serverData = readData.split(":");
                    for (int i = 0; i < serverData.length; i++) {
                        Log.d(TAG, "fragment_RTU_Status serverData : " + i + "번째 : " + serverData[i]);
                    }
                    readData5.setText(serverData[0] + " : " + serverData[1]);
                } else if (readData.contains("Server #02")) {
                    readData = readData.replace("Server #02 ", "");
                    String[] serverData = readData.split(":");
                    for (int i = 0; i < serverData.length; i++) {
                        Log.d(TAG, "fragment_RTU_Status serverData2 : " + i + "번째 : " + serverData[i]);
                    }
                    readData6.setText(serverData[0] + " : " + serverData[1]);
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
