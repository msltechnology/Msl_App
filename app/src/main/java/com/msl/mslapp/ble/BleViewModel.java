package com.msl.mslapp.ble;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.msl.mslapp.Public.StringList;
import com.msl.mslapp.R;

import java.util.ArrayList;
import java.util.List;

import static com.msl.mslapp.BleMainActivity.BlewriteData;
import static com.msl.mslapp.BleMainActivity.mBleContext;
import static com.msl.mslapp.Public.StringList.DATA_REQUEST_STATUS;

public class BleViewModel extends ViewModel {

    String TAG = "BleViewModel";

    Handler handler = new Handler(Looper.getMainLooper());

    /////////////////////////////////////////////////////////////
    // 명령용
    public void settingFL(String FL){
        setBleFL(FL);
        String sendData = StringList.DATA_SIGN_START + StringList.DATA_TYPE_LICMD + StringList.DATA_SIGN_COMMA
                + StringList.DATA_TYPE_S + StringList.DATA_SIGN_COMMA
                + FL + StringList.DATA_SIGN_COMMA
                + StringList.DATA_ID_255 + StringList.DATA_SIGN_CHECKSUM;
        BlewriteData(sendData);

        handler.postDelayed(() -> BlewriteData(DATA_REQUEST_STATUS), 200);
        Log.d(TAG,"settingFL : " + FL);
    }


    public void settingID(String ID){
        setBleID(ID);
        String sendData = StringList.DATA_SIGN_START + StringList.DATA_TYPE_LICMD + StringList.DATA_SIGN_COMMA
                + StringList.DATA_TYPE_S + StringList.DATA_SIGN_COMMA
                + StringList.DATA_TYPE_SID + StringList.DATA_SIGN_COMMA
                + ID + StringList.DATA_SIGN_CHECKSUM;
        BlewriteData(sendData);

        handler.postDelayed(() -> BlewriteData(DATA_REQUEST_STATUS), 200);
        Log.d(TAG,"settingID : " + ID);
    }



    public void settingDelay(String Delay){

        String[] dataArr5 = Delay.split("");

        List<String> list = new ArrayList<String>();

        for(String s : dataArr5) {
            if(s != null && s.length() > 0) {
                list.add(s);
            }
        }
        String result = list.get(0) +list.get(1) + "." + list.get(2) + list.get(3) + " " + mBleContext.getString(R.string.Second_Sec);


        setBleDelayTime(result);
        String sendData = StringList.DATA_SIGN_START + StringList.DATA_TYPE_LICMD + StringList.DATA_SIGN_COMMA
                + StringList.DATA_TYPE_S + StringList.DATA_SIGN_COMMA
                + StringList.DATA_TYPE_DEL + StringList.DATA_SIGN_COMMA
                + Delay + StringList.DATA_SIGN_CHECKSUM;
        BlewriteData(sendData);


        handler.postDelayed(() -> BlewriteData(DATA_REQUEST_STATUS), 200);
        Log.d(TAG,"settingDelay : " + result);
    }



    public MutableLiveData<String> getBleCDS() {
        return BleCDS;
    }

    public void setBleCDS(String bleCDS) {
        BleCDS.setValue(bleCDS);
    }

    public MutableLiveData<String> getBleInputV() {
        return BleInputV;
    }

    public void setBleInputV(String bleInputV) {
        BleInputV.setValue(bleInputV);
    }

    public MutableLiveData<String> getBleOutputV() {
        return BleOutputV;
    }

    public void setBleOutputV(String bleOutputV) {
        BleOutputV.setValue(bleOutputV);
    }

    public MutableLiveData<String> getBleLanternStatus() {
        return BleLanternStatus;
    }

    public void setBleLanternStatus(String bleLanternStatus) {
        BleLanternStatus.setValue(bleLanternStatus);
    }

    public MutableLiveData<String> getBleBatteryV() {
        return BleBatteryV;
    }

    public void setBleBatteryV(String bleBatteryV) {
        BleBatteryV.setValue(bleBatteryV);
    }

    public MutableLiveData<String> getBleOutputA() {
        return BleOutputA;
    }

    public void setBleOutputA(String bleOutputA) {
        BleOutputA.setValue(bleOutputA);
    }

    public MutableLiveData<String> getBleChargingA() {
        return BleChargingA;
    }

    public void setBleChargingA(String bleChargingA) {
        BleChargingA.setValue(bleChargingA);
    }

    public MutableLiveData<String> getBleSolarV() {
        return BleSolarV;
    }

    public void setBleSolarV(String bleSolarV) {
        BleSolarV.setValue(bleSolarV);
    }

    public MutableLiveData<String> getBleBatteryPer() {
        return BleBatteryPer;
    }

    public void setBleBatteryPer(String bleBatteryPer) {
        BleBatteryPer.setValue(bleBatteryPer);
    }

    public MutableLiveData<String> getBleGPSLongitude() {
        return BleGPSLongitude;
    }

    public void setBleGPSLongitude(String bleGPSLongitude) {
        BleGPSLongitude.setValue(bleGPSLongitude);
    }

    public MutableLiveData<String> getBleGPSLatitude() {
        return BleGPSLatitude;
    }

    public void setBleGPSLatitude(String bleGPSLatitude) {
        BleGPSLatitude.setValue(bleGPSLatitude);
    }

    public MutableLiveData<String> getBleDate() {
        return BleDate;
    }

    public void setBleDate(String bleDate) {
        BleDate.setValue(bleDate);
    }

    public MutableLiveData<String> getBleTime() {
        return BleTime;
    }

    public void setBleTime(String bleTime) {
        BleTime.setValue(bleTime);
    }

    public MutableLiveData<String> getBleTemperature() {
        return BleTemperature;
    }

    public void setBleTemperature(String bleTemperature) {
        BleTemperature.setValue(bleTemperature);
    }

    public MutableLiveData<String> getBleBattery_1() {
        return BleBattery_1;
    }

    public void setBleBattery_1(String bleBattery_1) {
        BleBattery_1.setValue(bleBattery_1);
    }

    public MutableLiveData<String> getBleBattery_2() {
        return BleBattery_2;
    }

    public void setBleBattery_2(String bleBattery_2) {
        BleBattery_2.setValue(bleBattery_2);
    }

    public MutableLiveData<String> getBleBattery_3() {
        return BleBattery_3;
    }

    public void setBleBattery_3(String bleBattery_3) {
        BleBattery_3.setValue(bleBattery_3);
    }

    public MutableLiveData<String> getBleBattery_4() {
        return BleBattery_4;
    }

    public void setBleBattery_4(String bleBattery_4) {
        BleBattery_4.setValue(bleBattery_4);
    }

    public MutableLiveData<String> getBleBattery_5() {
        return BleBattery_5;
    }

    public void setBleBattery_5(String bleBattery_5) {
        BleBattery_5.setValue(bleBattery_5);
    }

    public MutableLiveData<String> getBleBattery_6() {
        return BleBattery_6;
    }

    public void setBleBattery_6(String bleBattery_6) {
        BleBattery_6.setValue(bleBattery_6);
    }

    public MutableLiveData<String> getBleSolarV_1() {
        return BleSolarV_1;
    }

    public void setBleSolarV_1(String bleSolarV_1) {
        BleSolarV_1.setValue(bleSolarV_1);
    }

    public MutableLiveData<String> getBleSolarV_2() {
        return BleSolarV_2;
    }

    public void setBleSolarV_2(String bleSolarV_2) {
        BleSolarV_2.setValue(bleSolarV_2);
    }

    public MutableLiveData<String> getBleSolarV_3() {
        return BleSolarV_3;
    }

    public void setBleSolarV_3(String bleSolarV_3) {
        BleSolarV_3.setValue(bleSolarV_3);
    }

    public MutableLiveData<String> getBleSolarV_4() {
        return BleSolarV_4;
    }

    public void setBleSolarV_4(String bleSolarV_4) {
        BleSolarV_4.setValue(bleSolarV_4);
    }

    public MutableLiveData<String> getBleSolarV_5() {
        return BleSolarV_5;
    }

    public void setBleSolarV_5(String bleSolarV_5) {
        BleSolarV_5.setValue(bleSolarV_5);
    }

    public MutableLiveData<String> getBleSolarV_6() {
        return BleSolarV_6;
    }

    public void setBleSolarV_6(String bleSolarV_6) {
        BleSolarV_6.setValue(bleSolarV_6);
    }

    public MutableLiveData<String> getBleSolarA_1() {
        return BleSolarA_1;
    }

    public void setBleSolarA_1(String bleSolarA_1) {
        BleSolarA_1.setValue(bleSolarA_1);
    }

    public MutableLiveData<String> getBleSolarA_2() {
        return BleSolarA_2;
    }

    public void setBleSolarA_2(String bleSolarA_2) {
        BleSolarA_2.setValue(bleSolarA_2);
    }

    public MutableLiveData<String> getBleSolarA_3() {
        return BleSolarA_3;
    }

    public void setBleSolarA_3(String bleSolarA_3) {
        BleSolarA_3.setValue(bleSolarA_3);
    }

    public MutableLiveData<String> getBleSolarA_4() {
        return BleSolarA_4;
    }

    public void setBleSolarA_4(String bleSolarA_4) {
        BleSolarA_4.setValue(bleSolarA_4);
    }

    public MutableLiveData<String> getBleSolarA_5() {
        return BleSolarA_5;
    }

    public void setBleSolarA_5(String bleSolarA_5) {
        BleSolarA_5.setValue(bleSolarA_5);
    }

    public MutableLiveData<String> getBleSolarA_6() {
        return BleSolarA_6;
    }

    public void setBleSolarA_6(String bleSolarA_6) {
        BleSolarA_6.setValue(bleSolarA_6);
    }

    public MutableLiveData<String> getBleFirmVer() {
        return BleFirmVer;
    }

    public void setBleFirmVer(String bleFirmVer) {
        BleFirmVer.setValue(bleFirmVer);
    }

    public MutableLiveData<String> getBleRTUVer() {
        return BleRTUVer;
    }

    public void setBleRTUVer(String bleRTUVer) {
        BleRTUVer.setValue(bleRTUVer);
    }

    public MutableLiveData<String> getBleGPSSpeed() {
        return BleGPSSpeed;
    }

    public void setBleGPSSpeed(String bleGPSSpeed) {
        BleGPSSpeed.setValue(bleGPSSpeed);
    }

    public MutableLiveData<String> getBleGPSAlways() {

        return BleGPSAlways;
    }

    public void setBleGPSAlways(String bleGPSAlways) {
        BleGPSAlways.setValue(bleGPSAlways);
    }

    public MutableLiveData<String> getBleDelayTime() {

        return BleDelayTime;
    }

    public void setBleDelayTime(String bleDelayTime) {
        BleDelayTime.setValue(bleDelayTime);
    }

    public MutableLiveData<String> getBleID() {
        return BleID;
    }

    public void setBleID(String bleID) {
        BleID.setValue(bleID);
    }

    public MutableLiveData<String> getBleFL() {
        return BleFL;
    }

    public void setBleFL(String bleFL) {
        BleFL.setValue(bleFL);
    }


    public MutableLiveData<Integer> getBleBatteryV_iv() {
        return BleBatteryV_iv;
    }

    public void setBleBatteryV_iv(String readData) {

        double data = Double.parseDouble(readData);
        if (data > 75) {
            BleBatteryV_iv.setValue(R.drawable.battery_100);
        } else if (data > 50) {
            BleBatteryV_iv.setValue(R.drawable.battery_75);
        } else if (data > 25) {
            BleBatteryV_iv.setValue(R.drawable.battery_50);
        } else {
            BleBatteryV_iv.setValue(R.drawable.battery_25);
        }
    }


    public MutableLiveData<Integer> getBleBattery_iv_1() {
        return BleBattery_iv_1;
    }

    public void setBleBattery_iv_1(String readData) {
        double data = Double.parseDouble(readData);
        if (data > 4) {
            BleBattery_iv_1.setValue(R.drawable.green_battery);
        } else if (data > 3.8) {
            BleBattery_iv_1.setValue(R.drawable.yellow_battery);
        } else if (data > 3.6) {
            BleBattery_iv_1.setValue(R.drawable.brown_battery);
        } else {
            BleBattery_iv_1.setValue(R.drawable.red_battery);
        }
    }

    public MutableLiveData<Integer> getBleBattery_iv_2() {
        return BleBattery_iv_2;
    }

    public void setBleBattery_iv_2(String readData) {
        double data = Double.parseDouble(readData);
        if (data > 4) {
            BleBattery_iv_2.setValue(R.drawable.green_battery);
        } else if (data > 3.8) {
            BleBattery_iv_2.setValue(R.drawable.yellow_battery);
        } else if (data > 3.6) {
            BleBattery_iv_2.setValue(R.drawable.brown_battery);
        } else {
            BleBattery_iv_2.setValue(R.drawable.red_battery);
        }
    }

    public MutableLiveData<Integer> getBleBattery_iv_3() {
        return BleBattery_iv_3;
    }

    public void setBleBattery_iv_3(String readData) {
        double data = Double.parseDouble(readData);
        if (data > 4) {
            BleBattery_iv_3.setValue(R.drawable.green_battery);
        } else if (data > 3.8) {
            BleBattery_iv_3.setValue(R.drawable.yellow_battery);
        } else if (data > 3.6) {
            BleBattery_iv_3.setValue(R.drawable.brown_battery);
        } else {
            BleBattery_iv_3.setValue(R.drawable.red_battery);
        }
    }

    public MutableLiveData<Integer> getBleBattery_iv_4() {
        return BleBattery_iv_4;
    }

    public void setBleBattery_iv_4(String readData) {
        double data = Double.parseDouble(readData);
        if (data > 4) {
            BleBattery_iv_4.setValue(R.drawable.green_battery);
        } else if (data > 3.8) {
            BleBattery_iv_4.setValue(R.drawable.yellow_battery);
        } else if (data > 3.6) {
            BleBattery_iv_4.setValue(R.drawable.brown_battery);
        } else {
            BleBattery_iv_4.setValue(R.drawable.red_battery);
        }
    }

    public MutableLiveData<Integer> getBleBattery_iv_5() {
        return BleBattery_iv_5;
    }

    public void setBleBattery_iv_5(String readData) {
        double data = Double.parseDouble(readData);
        if (data > 4) {
            BleBattery_iv_5.setValue(R.drawable.green_battery);
        } else if (data > 3.8) {
            BleBattery_iv_5.setValue(R.drawable.yellow_battery);
        } else if (data > 3.6) {
            BleBattery_iv_5.setValue(R.drawable.brown_battery);
        } else {
            BleBattery_iv_5.setValue(R.drawable.red_battery);
        }
    }

    public MutableLiveData<Integer> getBleBattery_iv_6() {
        return BleBattery_iv_6;
    }

    public void setBleBattery_iv_6(String readData) {
        double data = Double.parseDouble(readData);
        if (data > 4) {
            BleBattery_iv_6.setValue(R.drawable.green_battery);
        } else if (data > 3.8) {
            BleBattery_iv_6.setValue(R.drawable.yellow_battery);
        } else if (data > 3.6) {
            BleBattery_iv_6.setValue(R.drawable.brown_battery);
        } else {
            BleBattery_iv_6.setValue(R.drawable.red_battery);
        }
    }

    public MutableLiveData<Integer> getBleSolarV_iv_1() {
        return BleSolarV_iv_1;
    }

    public void setBleSolarV_iv_1(String readData) {
        double data = Double.parseDouble(readData);
        if (data > 4) {
            BleSolarV_iv_1.setValue(R.drawable.green_battery);
        } else if (data > 3.8) {
            BleSolarV_iv_1.setValue(R.drawable.yellow_battery);
        } else if (data > 3.6) {
            BleSolarV_iv_1.setValue(R.drawable.brown_battery);
        } else {
            BleSolarV_iv_1.setValue(R.drawable.red_battery);
        }
    }

    public MutableLiveData<Integer> getBleSolarV_iv_2() {
        return BleSolarV_iv_2;
    }

    public void setBleSolarV_iv_2(String readData) {
        double data = Double.parseDouble(readData);
        if (data > 4) {
            BleSolarV_iv_2.setValue(R.drawable.green_battery);
        } else if (data > 3.8) {
            BleSolarV_iv_2.setValue(R.drawable.yellow_battery);
        } else if (data > 3.6) {
            BleSolarV_iv_2.setValue(R.drawable.brown_battery);
        } else {
            BleSolarV_iv_2.setValue(R.drawable.red_battery);
        }
    }

    public MutableLiveData<Integer> getBleSolarV_iv_3() {
        return BleSolarV_iv_3;
    }

    public void setBleSolarV_iv_3(String readData) {
        double data = Double.parseDouble(readData);
        if (data > 4) {
            BleSolarV_iv_3.setValue(R.drawable.green_battery);
        } else if (data > 3.8) {
            BleSolarV_iv_3.setValue(R.drawable.yellow_battery);
        } else if (data > 3.6) {
            BleSolarV_iv_3.setValue(R.drawable.brown_battery);
        } else {
            BleSolarV_iv_3.setValue(R.drawable.red_battery);
        }
    }

    public MutableLiveData<Integer> getBleSolarV_iv_4() {
        return BleSolarV_iv_4;
    }

    public void setBleSolarV_iv_4(String readData) {
        double data = Double.parseDouble(readData);
        if (data > 4) {
            BleSolarV_iv_4.setValue(R.drawable.green_battery);
        } else if (data > 3.8) {
            BleSolarV_iv_4.setValue(R.drawable.yellow_battery);
        } else if (data > 3.6) {
            BleSolarV_iv_4.setValue(R.drawable.brown_battery);
        } else {
            BleSolarV_iv_4.setValue(R.drawable.red_battery);
        }
    }

    public MutableLiveData<Integer> getBleSolarV_iv_5() {
        return BleSolarV_iv_5;
    }

    public void setBleSolarV_iv_5(String readData) {
        double data = Double.parseDouble(readData);
        if (data > 4) {
            BleSolarV_iv_5.setValue(R.drawable.green_battery);
        } else if (data > 3.8) {
            BleSolarV_iv_5.setValue(R.drawable.yellow_battery);
        } else if (data > 3.6) {
            BleSolarV_iv_5.setValue(R.drawable.brown_battery);
        } else {
            BleSolarV_iv_5.setValue(R.drawable.red_battery);
        }
    }

    public MutableLiveData<Integer> getBleSolarV_iv_6() {
        return BleSolarV_iv_6;
    }

    public void setBleSolarV_iv_6(String readData) {
        double data = Double.parseDouble(readData);
        if (data > 4) {
            BleSolarV_iv_6.setValue(R.drawable.green_battery);
        } else if (data > 3.8) {
            BleSolarV_iv_6.setValue(R.drawable.yellow_battery);
        } else if (data > 3.6) {
            BleSolarV_iv_6.setValue(R.drawable.brown_battery);
        } else {
            BleSolarV_iv_6.setValue(R.drawable.red_battery);
        }
    }

    public MutableLiveData<Integer> getBleGPSAlways_Iv_On() {
        return BleGPSAlways_Iv_On;
    }

    public void setBleGPSAlways_Iv_On(int bleGPSAlways_Iv_On) {
        if (bleGPSAlways_Iv_On == 1) {
            BleGPSAlways_Iv_On.setValue(R.drawable.custom_ble_setting_gps_on_clicked);
            setBleGPSAlways_Iv_On_Text(Color.WHITE);
        } else {
            BleGPSAlways_Iv_On.setValue(R.drawable.custom_ble_setting_gps_on);
            setBleGPSAlways_Iv_On_Text(Color.BLACK);
        }
    }

    public MutableLiveData<Integer> getBleGPSAlways_Iv_Off() {
        return BleGPSAlways_Iv_Off;
    }

    public void setBleGPSAlways_Iv_Off(int bleGPSAlways_Iv_Off) {
        if (bleGPSAlways_Iv_Off == 1) {
            BleGPSAlways_Iv_Off.setValue(R.drawable.custom_ble_setting_gps_off);
            setBleGPSAlways_Iv_Off_Text(Color.BLACK);
        } else {
            BleGPSAlways_Iv_Off.setValue(R.drawable.custom_ble_setting_gps_off_clicked);
            setBleGPSAlways_Iv_Off_Text(Color.WHITE);
        }
    }

    public MutableLiveData<Integer> getBleGPSAlways_Iv_On_Text() {
        return BleGPSAlways_Iv_On_Text;
    }

    public void setBleGPSAlways_Iv_On_Text(int bleGPSAlways_Iv_On_Text) {
        BleGPSAlways_Iv_On_Text.setValue(bleGPSAlways_Iv_On_Text);
    }

    public MutableLiveData<Integer> getBleGPSAlways_Iv_Off_Text() {
        return BleGPSAlways_Iv_Off_Text;
    }

    public void setBleGPSAlways_Iv_Off_Text(int bleGPSAlways_Iv_Off_Text) {
        BleGPSAlways_Iv_Off_Text.setValue(bleGPSAlways_Iv_Off_Text);
    }

    MutableLiveData<String> BleID = new MutableLiveData<>("000");
    MutableLiveData<String> BleCDS = new MutableLiveData<>("000");
    MutableLiveData<String> BleFL = new MutableLiveData<>("000");
    MutableLiveData<String> BleInputV = new MutableLiveData<>("000");
    MutableLiveData<String> BleOutputV = new MutableLiveData<>("000");
    MutableLiveData<String> BleLanternStatus = new MutableLiveData<>("000");
    MutableLiveData<String> BleBatteryV = new MutableLiveData<>("000");
    MutableLiveData<Integer> BleBatteryV_iv = new MutableLiveData<>(R.drawable.battery_25);
    MutableLiveData<String> BleOutputA = new MutableLiveData<>("000");
    MutableLiveData<String> BleChargingA = new MutableLiveData<>("000");
    MutableLiveData<String> BleSolarV = new MutableLiveData<>("000");
    MutableLiveData<String> BleBatteryPer = new MutableLiveData<>("000");
    MutableLiveData<String> BleGPSLongitude = new MutableLiveData<>("E 000º00'00.0000");
    MutableLiveData<String> BleGPSLatitude = new MutableLiveData<>("N 00º00'00.0000");
    MutableLiveData<String> BleDate = new MutableLiveData<>("0000Y 00M 00D");
    MutableLiveData<String> BleTime = new MutableLiveData<>("00H 00M 00S");
    MutableLiveData<String> BleTemperature = new MutableLiveData<>("000");

    MutableLiveData<String> BleBattery_1 = new MutableLiveData<>("000");
    MutableLiveData<String> BleBattery_2 = new MutableLiveData<>("000");
    MutableLiveData<String> BleBattery_3 = new MutableLiveData<>("000");
    MutableLiveData<String> BleBattery_4 = new MutableLiveData<>("000");
    MutableLiveData<String> BleBattery_5 = new MutableLiveData<>("000");
    MutableLiveData<String> BleBattery_6 = new MutableLiveData<>("000");

    MutableLiveData<Integer> BleBattery_iv_1 = new MutableLiveData<>(R.drawable.red_battery);
    MutableLiveData<Integer> BleBattery_iv_2 = new MutableLiveData<>(R.drawable.red_battery);
    MutableLiveData<Integer> BleBattery_iv_3 = new MutableLiveData<>(R.drawable.red_battery);
    MutableLiveData<Integer> BleBattery_iv_4 = new MutableLiveData<>(R.drawable.red_battery);
    MutableLiveData<Integer> BleBattery_iv_5 = new MutableLiveData<>(R.drawable.red_battery);
    MutableLiveData<Integer> BleBattery_iv_6 = new MutableLiveData<>(R.drawable.red_battery);

    MutableLiveData<String> BleSolarV_1 = new MutableLiveData<>("000");
    MutableLiveData<String> BleSolarV_2 = new MutableLiveData<>("000");
    MutableLiveData<String> BleSolarV_3 = new MutableLiveData<>("000");
    MutableLiveData<String> BleSolarV_4 = new MutableLiveData<>("000");
    MutableLiveData<String> BleSolarV_5 = new MutableLiveData<>("000");
    MutableLiveData<String> BleSolarV_6 = new MutableLiveData<>("000");
    MutableLiveData<Integer> BleSolarV_iv_1 = new MutableLiveData<>(R.drawable.red_battery);
    MutableLiveData<Integer> BleSolarV_iv_2 = new MutableLiveData<>(R.drawable.red_battery);
    MutableLiveData<Integer> BleSolarV_iv_3 = new MutableLiveData<>(R.drawable.red_battery);
    MutableLiveData<Integer> BleSolarV_iv_4 = new MutableLiveData<>(R.drawable.red_battery);
    MutableLiveData<Integer> BleSolarV_iv_5 = new MutableLiveData<>(R.drawable.red_battery);
    MutableLiveData<Integer> BleSolarV_iv_6 = new MutableLiveData<>(R.drawable.red_battery);
    MutableLiveData<String> BleSolarA_1 = new MutableLiveData<>("000");
    MutableLiveData<String> BleSolarA_2 = new MutableLiveData<>("000");
    MutableLiveData<String> BleSolarA_3 = new MutableLiveData<>("000");
    MutableLiveData<String> BleSolarA_4 = new MutableLiveData<>("000");
    MutableLiveData<String> BleSolarA_5 = new MutableLiveData<>("000");
    MutableLiveData<String> BleSolarA_6 = new MutableLiveData<>("000");

    // 펌웨어 버젼
    MutableLiveData<String> BleFirmVer = new MutableLiveData<>("0.0v");
    // RTU 용 확인
    MutableLiveData<String> BleRTUVer = new MutableLiveData<>("000");
    // GPS 속도
    MutableLiveData<String> BleGPSSpeed = new MutableLiveData<>("0000");
    // GPS 셋팅
    MutableLiveData<String> BleGPSAlways = new MutableLiveData<>("2");

    MutableLiveData<Integer> BleGPSAlways_Iv_On = new MutableLiveData<>(R.drawable.custom_ble_setting_gps_on);
    MutableLiveData<Integer> BleGPSAlways_Iv_Off = new MutableLiveData<>(R.drawable.custom_ble_setting_gps_off);
    MutableLiveData<Integer> BleGPSAlways_Iv_On_Text = new MutableLiveData<>(Color.BLACK);
    MutableLiveData<Integer> BleGPSAlways_Iv_Off_Text = new MutableLiveData<>(Color.BLACK);
    // Delay Time
    MutableLiveData<String> BleDelayTime = new MutableLiveData<>("+000 sec");

    public void reinitializeData() {
        BleID.setValue("000");
        BleCDS.setValue("000");
        BleFL.setValue("000");
        BleInputV.setValue("000");
        BleOutputV.setValue("000");
        BleLanternStatus.setValue("000");
        BleBatteryV.setValue("00");
        BleBatteryV_iv.setValue(R.drawable.battery_25);
        BleOutputA.setValue("000");
        BleChargingA.setValue("000");
        BleSolarV.setValue("000");
        BleBatteryPer.setValue("000");
        BleGPSLongitude.setValue("E 000º00'00.0000");
        BleGPSLatitude.setValue("N 00º00'00.0000");
        BleDate.setValue("0000Y 00M 00D");
        BleTime.setValue("00H 00M 00S");
        BleTemperature.setValue("000");
        BleBattery_1.setValue("000");
        BleBattery_2.setValue("000");
        BleBattery_3.setValue("000");
        BleBattery_4.setValue("000");
        BleBattery_5.setValue("000");
        BleBattery_6.setValue("000");
        BleBattery_iv_1.setValue(R.drawable.red_battery);
        BleBattery_iv_2.setValue(R.drawable.red_battery);
        BleBattery_iv_3.setValue(R.drawable.red_battery);
        BleBattery_iv_4.setValue(R.drawable.red_battery);
        BleBattery_iv_5.setValue(R.drawable.red_battery);
        BleBattery_iv_6.setValue(R.drawable.red_battery);
        BleSolarV_1.setValue("000");
        BleSolarV_2.setValue("000");
        BleSolarV_3.setValue("000");
        BleSolarV_4.setValue("000");
        BleSolarV_5.setValue("000");
        BleSolarV_6.setValue("000");
        BleSolarV_iv_1.setValue(R.drawable.red_battery);
        BleSolarV_iv_2.setValue(R.drawable.red_battery);
        BleSolarV_iv_3.setValue(R.drawable.red_battery);
        BleSolarV_iv_4.setValue(R.drawable.red_battery);
        BleSolarV_iv_5.setValue(R.drawable.red_battery);
        BleSolarV_iv_6.setValue(R.drawable.red_battery);
        BleSolarA_1.setValue("000");
        BleSolarA_2.setValue("000");
        BleSolarA_3.setValue("000");
        BleSolarA_4.setValue("000");
        BleSolarA_5.setValue("000");
        BleSolarA_6.setValue("000");
        BleFirmVer.setValue("0.0v");
        BleRTUVer.setValue("000");
        BleGPSSpeed.setValue("0000");
        BleGPSAlways.setValue("2");
        BleGPSAlways_Iv_On.setValue(R.drawable.custom_ble_setting_gps_on);
        BleGPSAlways_Iv_Off.setValue(R.drawable.custom_ble_setting_gps_off);
        BleGPSAlways_Iv_On_Text.setValue(Color.BLACK);
        BleGPSAlways_Iv_Off_Text.setValue(Color.WHITE);
        BleDelayTime.setValue("+000 sec");
    }


}
