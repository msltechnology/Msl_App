package com.msl.mslapp;

import android.util.Log;
import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static com.msl.mslapp.BleMainActivity.TAG;

public class TestViewModel extends ViewModel {

    private MutableLiveData<String> dataString;

    //data binding시 필요
    public LiveData<String> getDataString()
    {
        if(dataString == null){
            dataString = new MutableLiveData<String>();
        }

        return dataString;
    }

    public void setDataString(String data)
    {
        dataString.setValue(data);
        Log.i("setDataString", data);
        Log.i("DataString 값 : ", dataString.getValue());
    }
}