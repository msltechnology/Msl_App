package com.msl.mslapp.RTU.fragment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;

import java.util.List;

public class adapter_RTU_Tab extends FragmentStateAdapter {

    /*Fragment[] fragments = new Fragment[3];
    String[] pageTitles = new String[]{"Status", "Setting","Lantern"};*/
    Fragment[] fragments = new Fragment[2];
    String[] pageTitles = new String[]{"Status", "Setting"};

    public int mCount;

    public adapter_RTU_Tab(Fragment fa, int count){
        super(fa);
        mCount = count;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if(index==0) return new fragment_RTU_Status();
        else return new fragment_RTU_Setting();
        /*
        if(index==0) return new fragment_RTU_Status();
        else if(index==1)return new fragment_RTU_Setting();
        else return new fragment_RTU_Lantern();*/
    }
/*
    @Override
    public int getItemCount() {
        return 3;
    }*/
    @Override
    public int getItemCount() {
        return 2;
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
