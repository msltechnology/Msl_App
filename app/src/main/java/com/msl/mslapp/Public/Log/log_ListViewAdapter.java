package com.msl.mslapp.Public.Log;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msl.mslapp.R;

import java.util.ArrayList;

import static com.msl.mslapp.BleMainActivity.mBleMain;

public class log_ListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<navigation_Log_ListView> log_ListViewList = new ArrayList<>();
    private LinearLayout ll_navigation_log;

    private int nListCnt = 0;

    log_ListViewAdapter log_listViewAdapter;


    navigation_Log_ListView item = new navigation_Log_ListView();

    String dataType_read = "Read : ";
    String dataType_write = "Write : ";
    String dataType_error = "Error : ";

    // ListViewAdapter의 생성자
    public log_ListViewAdapter() {
        log_listViewAdapter = this;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return nListCnt;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.navigation_log_listview, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView tv_log_currentTime = convertView.findViewById(R.id.tv_navigation_log_currentTime);
        TextView tv_log_Data = convertView.findViewById(R.id.tv_navigation_log_Data);
        ll_navigation_log = convertView.findViewById(R.id.ll_navigation_log);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        navigation_Log_ListView listViewItem = log_ListViewList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        tv_log_currentTime.setText(listViewItem.getLogCurrentTime());
        switch (listViewItem.getLogBackColor()) {
            case "read":
                tv_log_Data.setText(dataType_read + listViewItem.getLogData());
                ll_navigation_log.setBackgroundColor(context.getResources().getColor(R.color.navigation_log_read_back));
                break;
            case "write":
                tv_log_Data.setText(dataType_write + listViewItem.getLogData());
                ll_navigation_log.setBackgroundColor(context.getResources().getColor(R.color.navigation_log_write_back));
                break;
            case "error":
                tv_log_Data.setText(dataType_error + listViewItem.getLogData());
                ll_navigation_log.setBackgroundColor(context.getResources().getColor(R.color.navigation_log_error_back));
                break;
            default:
                tv_log_Data.setText(listViewItem.getLogData());
                ll_navigation_log.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        return convertView;
    }


    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return log_ListViewList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    // Activity 에서 notifyDataSetChanged 를 안하고 adapter 에서 실행. 에러가 떠서. 매번 size 를 측정하여 에러나는걸 줄임.
    public void addItem(String currentTime, String Data) {
        // 너무 많으면 초기화
        if(nListCnt >= 300){
            log_ListViewList = new ArrayList<>();
        }

        // 메인 activity 를 통해서 ui를 변경해야 에러가 안뜸.
        mBleMain.runOnUiThread(() -> {
            item = new navigation_Log_ListView();

            item.setLogCurrentTime(currentTime);
            item.setLogData(Data);

            log_ListViewList.add(item);
            nListCnt = log_ListViewList.size();
            log_listViewAdapter.notifyDataSetChanged();
        });


/*
        item = new navigation_Log_ListView();

        item.setLogCurrentTime(currentTime);
        item.setLogData(Data);

        log_ListViewList.add(item);
        nListCnt = log_ListViewList.size();
        this.notifyDataSetChanged();*/
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String currentTime, String Data, String Color) {
        if(nListCnt >= 300){
            log_ListViewList = new ArrayList<>();
        }

        mBleMain.runOnUiThread(() -> {
            item = new navigation_Log_ListView();

            item.setLogCurrentTime(currentTime);
            item.setLogData(Data);
            item.setLogBackColor(Color);

            log_ListViewList.add(item);
            nListCnt = log_ListViewList.size();
            log_listViewAdapter.notifyDataSetChanged();
        });

        /*item = new navigation_Log_ListView();

        item.setLogCurrentTime(currentTime);
        item.setLogData(Data);
        item.setLogBackColor(Color);

        log_ListViewList.add(item);
        nListCnt = log_ListViewList.size();
        this.notifyDataSetChanged();*/
    }
}