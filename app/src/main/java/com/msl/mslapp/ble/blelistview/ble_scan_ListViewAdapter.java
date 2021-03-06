package com.msl.mslapp.ble.blelistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msl.mslapp.R;

import java.util.ArrayList;

public class ble_scan_ListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<BleScanListView> blelistViewList = new ArrayList<>();
    private LinearLayout bleListLl;

    // ListViewAdapter의 생성자
    public ble_scan_ListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return blelistViewList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ble_scan_listview, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView tv_bleUserdata = (TextView) convertView.findViewById(R.id.bleUserdata);
        TextView tv_bleName = (TextView) convertView.findViewById(R.id.bleName);
        TextView tv_bleAddress = (TextView) convertView.findViewById(R.id.bleAddress);
        TextView tv_bleSign = (TextView) convertView.findViewById(R.id.bleSign);
        bleListLl = convertView.findViewById(R.id.bleListLl);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        BleScanListView listViewItem = blelistViewList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        tv_bleUserdata.setText(listViewItem.getBleUserdata());
        tv_bleName.setText(listViewItem.getBleName());
        tv_bleAddress.setText(listViewItem.getBleAddress());
        tv_bleSign.setText(listViewItem.getBleSign());
        // 배경색 변경하여 MSL TECH 이름의 제품 찾기 쉽게
        if (listViewItem.getBleCheck()) {
            bleListLl.setBackgroundColor(context.getResources().getColor(R.color.ble_scan_list_MSL));

        } else {
            bleListLl.setBackgroundColor(context.getResources().getColor(R.color.ble_scan_list));
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
        return blelistViewList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String userdata, String name, String address, String sign) {
        BleScanListView item = new BleScanListView();

        item.setBleUserdata(userdata);
        item.setBleName(name);
        item.setBleAddress(address);
        item.setBleSign(sign);

        blelistViewList.add(item);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String userdata, String name, String address, String sign, boolean check) {
        BleScanListView item = new BleScanListView();

        item.setBleUserdata(userdata);
        item.setBleName(name);
        item.setBleAddress(address);
        item.setBleSign(sign);
        item.setBleCheck(check);

        blelistViewList.add(item);
    }

    // 아이템을 최신 데이터로 갱신
    public void updateItem(int order, String Userdata, String sign) {
        BleScanListView bleScanListView = blelistViewList.get(order);
        BleScanListView item = new BleScanListView();

        item.setBleUserdata(Userdata);
        item.setBleName(bleScanListView.getBleName());
        item.setBleAddress(bleScanListView.getBleAddress());
        item.setBleSign(sign);
        item.setBleCheck(bleScanListView.getBleCheck());

        blelistViewList.set(order, item);
    }
}