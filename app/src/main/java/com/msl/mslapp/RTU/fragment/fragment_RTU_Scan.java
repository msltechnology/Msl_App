package com.msl.mslapp.RTU.fragment;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.msl.mslapp.R;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.util.ArrayList;
import java.util.Locale;

public class fragment_RTU_Scan extends ListFragment {

    static class ListItem {
        UsbDevice device;
        int port;
        UsbSerialDriver driver;

        ListItem(UsbDevice device, int port, UsbSerialDriver driver) {
            this.device = device;
            this.port = port;
            this.driver = driver;
        }
    }

    TextView testTv;

    private final ArrayList<ListItem> listItems = new ArrayList<>();
    private ArrayAdapter<ListItem> listAdapter;
    private int baudRate = 115200;
    private boolean withIoManager = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // 리스트 정의
        listAdapter = new ArrayAdapter<ListItem>(getActivity(), 0, listItems) {
            @NonNull
            @Override
            public View getView(int position, View view, @NonNull ViewGroup parent) {
                ListItem item = listItems.get(position);
                if (view == null)
                    view = getActivity().getLayoutInflater().inflate(R.layout.rtu_fragment_device_list_item, parent, false);
                TextView text1 = view.findViewById(R.id.text1);
                TextView text2 = view.findViewById(R.id.text2);
                if (item.driver == null)
                    text1.setText("<no driver>");
                else if (item.driver.getPorts().size() == 1)
                    text1.setText(item.driver.getClass().getSimpleName().replace("SerialDriver", ""));
                else
                    text1.setText(item.driver.getClass().getSimpleName().replace("SerialDriver", "") + ", Port " + item.port);
                text2.setText(String.format(Locale.US, "Vendor %04X, Product %04X", item.device.getVendorId(), item.device.getProductId()));
                return view;
            }
        };
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(null);
        View header = getActivity().getLayoutInflater().inflate(R.layout.rtu_fragment_device_list_header, null, false);
        getListView().addHeaderView(header, null, false);

        // 해당 2줄이 내용안보이게하고 텍스트만 보이게 함.
        setEmptyText("연결된 장치가 없습니다\n<no USB devices found>");
        ((TextView) getListView().getEmptyView()).setTextSize(18);

        testTv = header.findViewById(R.id.testTv);

        setListAdapter(listAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    // USB 연결 후 해당 어플 선택 시 해당 값 실행. USB 가 연결 되 있는 상태라면 해당 데이터 받아서 리스트로 보여줌.
    void refresh() {
        UsbManager usbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        UsbSerialProber usbDefaultProber = UsbSerialProber.getDefaultProber();
        UsbSerialProber usbCustomProber = CustomProber.getCustomProber();
        listItems.clear();

        testTv.setVisibility(View.VISIBLE);
        for (UsbDevice device : usbManager.getDeviceList().values()) {
            UsbSerialDriver driver = usbDefaultProber.probeDevice(device);
            if (driver == null) {
                driver = usbCustomProber.probeDevice(device);
            }
            if (driver != null) {
                Log.d("test", driver.getPorts().size() + "갯수");
                if (driver.getPorts().size() == 1) {
                    UsbSerialDriver finalDriver = driver;

                    testTv.setVisibility(View.GONE);

                    ListItem item = new ListItem(device, 0, finalDriver);

                    Bundle args = new Bundle();
                    args.putInt("device", item.device.getDeviceId());
                    args.putInt("port", item.port);
                    args.putInt("baud", baudRate);
                    Fragment fragment = new fragment_RTU_Function();
                    fragment.setArguments(args);
                    getFragmentManager().beginTransaction().replace(R.id.rtu_Fragment_Space, fragment, "terminal").commit();

                } else {
                    testTv.setVisibility(View.GONE);
                    for (int port = 0; port < driver.getPorts().size(); port++)
                        listItems.add(new ListItem(device, port, driver));
                }
            } else {
                testTv.setVisibility(View.GONE);
                listItems.add(new ListItem(device, 0, null));
            }
        }
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ListItem item = listItems.get(position - 1);
        if (item.driver == null) {
            Toast.makeText(getActivity(), "no driver", Toast.LENGTH_SHORT).show();
        } else {
            // 선택한 리스트의 데이터를 bundle에 넣어서 넘겨줌.
            Bundle args = new Bundle();
            args.putInt("device", item.device.getDeviceId());
            args.putInt("port", item.port);
            args.putInt("baud", baudRate);
            Fragment fragment = new fragment_RTU_Function();
            fragment.setArguments(args);
            //addToBackStack(null) 하므로서 현재 프래그먼트 데이터를 뒤로 하여 나중에 그대로 사용할 수 있다.
            getFragmentManager().beginTransaction().replace(R.id.rtu_Fragment_Space, fragment, "terminal").addToBackStack(null).commit();
        }
    }

}
