package com.msl.mslapp.Ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.msl.mslapp.BleMainActivity.CHARACTERISTIC_COMMAND_STRING;
import static com.msl.mslapp.BleMainActivity.CHARACTERISTIC_COMMAND_STRING_4;
import static com.msl.mslapp.BleMainActivity.CHARACTERISTIC_RESPONSE_STRING;
import static com.msl.mslapp.BleMainActivity.CHARACTERISTIC_RESPONSE_STRING_4;
import static com.msl.mslapp.BleMainActivity.SERVICE_STRING;
import static com.msl.mslapp.BleMainActivity.SERVICE_STRING_4;

public class BluetoothUtils {

    public static List<BluetoothGattCharacteristic> findBLECharacteristics(BluetoothGatt gatt){

        ArrayList<BluetoothGattCharacteristic> matchingCharacteristics = new ArrayList();
        List<BluetoothGattService> serviceList = gatt.getServices();
        if(findGattService(serviceList) == null){
            return matchingCharacteristics;
        }
        BluetoothGattService service = findGattService(serviceList);
        List<BluetoothGattCharacteristic> characteristicList = service.getCharacteristics();
        for(BluetoothGattCharacteristic characteristic : characteristicList){
            if(isMatchingCharacteristic(characteristic)){
                matchingCharacteristics.add(characteristic);
            }
        }
        return matchingCharacteristics;
    }

    public static BluetoothGattCharacteristic findCommandCharacteristic(BluetoothGatt gatt){
        return findCharacteristic(gatt, CHARACTERISTIC_COMMAND_STRING);
    }

    public static BluetoothGattCharacteristic findResponseCharacteristic(BluetoothGatt gatt){
        return findCharacteristic(gatt, CHARACTERISTIC_RESPONSE_STRING);

    }

    private static BluetoothGattCharacteristic findCharacteristic(BluetoothGatt gatt, String uuidString){

        List<BluetoothGattService> serviceList = gatt.getServices();
        if(findGattService(serviceList) == null){
            return null;
        }
        BluetoothGattService service = findGattService(serviceList);
        List<BluetoothGattCharacteristic> characteristicList = service.getCharacteristics();
        for(BluetoothGattCharacteristic characteristic : characteristicList){
            if(matchCharacteristic(characteristic, uuidString)){
                return characteristic;
            }
        }
        return null;
    }

    private static Boolean matchCharacteristic(BluetoothGattCharacteristic characteristic, String uuidString){
        if(characteristic == null){
            return false;
        }
        UUID uuid = characteristic.getUuid();
        return matchUUIDs(uuid.toString(), uuidString);
    }

    private static BluetoothGattService findGattService(List<BluetoothGattService> serviceList){
        for(BluetoothGattService service : serviceList){
            String serviceUuidString = service.getUuid().toString();
            if(matchServiceUUIDString(serviceUuidString)){
                return service;
            }
        }
        return null;
    }

    private static Boolean matchServiceUUIDString(String serviceUuidString){
        if(matchUUIDs(serviceUuidString, SERVICE_STRING_4)){
            return matchUUIDs(serviceUuidString,
                    SERVICE_STRING_4);
        }
        return matchUUIDs(serviceUuidString, SERVICE_STRING);
    }

    private static Boolean isMatchingCharacteristic(BluetoothGattCharacteristic characteristic){
        if(characteristic == null){
            return false;
        }
        UUID uuid = characteristic.getUuid();
        return matchCharacteristicUUID(uuid.toString());
    }

    private static Boolean matchCharacteristicUUID(String characteristicUuidString){
        if(matchUUIDs(characteristicUuidString, CHARACTERISTIC_COMMAND_STRING_4, CHARACTERISTIC_RESPONSE_STRING_4)){
            Log.d("matchCharacteristicUUID", "matching 4");
            return matchUUIDs(characteristicUuidString, CHARACTERISTIC_COMMAND_STRING_4, CHARACTERISTIC_RESPONSE_STRING_4);
        }
        Log.d("matchCharacteristicUUID", "matching 5");
        return matchUUIDs(characteristicUuidString, CHARACTERISTIC_COMMAND_STRING, CHARACTERISTIC_RESPONSE_STRING);
    }

    private static Boolean matchUUIDs(String uuidString, String...matches){
        for(String match : matches){
            if(uuidString.equalsIgnoreCase(match)){
                return true;
            }
        }
        return false;
    }

}
