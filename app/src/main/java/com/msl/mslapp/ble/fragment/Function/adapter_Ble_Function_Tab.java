package com.msl.mslapp.ble.fragment.Function;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;

import java.util.List;

import static com.msl.mslapp.BleMainActivity.adminApp;

public class adapter_Ble_Function_Tab extends FragmentStateAdapter {

    public int mCount;

    public adapter_Ble_Function_Tab(Fragment fa, int count){
        super(fa);
        mCount = count;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);


        /*// 관리자용 앱이면 RTU 도 보이게
        if(adminApp){
            if(index==0) return new fragment_Ble_Status();
            else if(index==1) return new fragment_Ble_Setting();
            else if(index==2) return new fragment_Ble_Test();
            else return new fragment_Ble_RTU();

        }else{
            if(index==0) return new fragment_Ble_Status();
            else if(index==1) return new fragment_Ble_Setting();
            else return new fragment_Ble_Test();
        }*/
        if(index==0) return new fragment_Ble_Status();
        else if(index==1) return new fragment_Ble_Setting();
        else if(index==2) return new fragment_Ble_Test();
        else return new fragment_Ble_RTU();

    }
    @Override
    public int getItemCount() {
        /*if(adminApp){
            return 4;
        }
        return 3;*/
        return 4;
    }

    public int getRealPosition(int position) { return position % mCount; }

    @Override
    public void onBindViewHolder(@NonNull FragmentViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        //holder.t

    }
}
