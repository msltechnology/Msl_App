package com.msl.mslapp.Ble.fragment.Function;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;

import java.util.List;

import static com.msl.mslapp.BleMainActivity.adminApp;

public class adapter_Ble_Function_Tab extends FragmentStateAdapter {

    Fragment[] fragments = new Fragment[3];
    String[] pageTitles = new String[]{"Status", "Setting", "Test"};

    public int mCount;

    public adapter_Ble_Function_Tab(Fragment fa, int count){
        super(fa);
        mCount = count;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if(adminApp){
            if(index==0) return new fragment_Ble_Status();
            else if(index==1) return new fragment_Ble_Setting();
            else if(index==2) return new fragment_Ble_Test();
            else return new fragment_Ble_RTU();

        }else{
            if(index==0) return new fragment_Ble_Status();
            else if(index==1) return new fragment_Ble_Setting();
            else return new fragment_Ble_Test();
        }

    }

    // RTU 가 있다면.
    /*@NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if(index==0) return new fragment_Ble_Status();
        else if(index==1) return new fragment_Ble_Setting();
        else if(index==2) return new fragment_Ble_Test();
        else return new fragment_Ble_RTU();
    }

    @Override
    public int getItemCount() {
        return 4;
    }*/

    @Override
    public int getItemCount() {
        if(adminApp){
            return 4;
        }

        return 3;
    }

    public int getRealPosition(int position) { return position % mCount; }

    @Override
    public void onBindViewHolder(@NonNull FragmentViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        //holder.t

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTest;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            //tvTest = itemView.findViewById(R.id.)
        }
    }
}
