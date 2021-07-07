package com.msl.mslapp.Ble.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.msl.mslapp.BleMainActivity;
import com.msl.mslapp.R;

import static com.msl.mslapp.BleMainActivity.ADMIN_PASSWORD;
import static com.msl.mslapp.BleMainActivity.CDS_LAMP_OFF_READY;
import static com.msl.mslapp.BleMainActivity.CDS_LAMP_OFF_SETTING;
import static com.msl.mslapp.BleMainActivity.CDS_LAMP_ON_READY;
import static com.msl.mslapp.BleMainActivity.CDS_LAMP_ON_SETTING;

public class fragment_CDS_Setting extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-CDS";

    View view;
    private Activity activity;

    TextView readData;

    // 로딩바
    ProgressDialog dialog;

    // 일단 이대로 두고 직접 사용한다음 수정할 예정.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");
        view = inflater.inflate(R.layout.ble_fragment_cds, null);

        ((CDS_Setting_Listener) activity).onCreateViewFragment_CDS_Setting();

        dialog = new ProgressDialog(view.getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading");
        dialog.show();

        Button onPrepareBtn = view.findViewById(R.id.on_prepare);
        onPrepareBtn.setOnClickListener(v -> ((BleMainActivity)getActivity()).BlewriteData(CDS_LAMP_ON_READY));
        Button onSetBtn = view.findViewById(R.id.on_set);
        onSetBtn.setOnClickListener(v -> ((BleMainActivity)getActivity()).BlewriteData(CDS_LAMP_ON_SETTING));
        Button offPrepareBtn = view.findViewById(R.id.off_prepare);
        offPrepareBtn.setOnClickListener(v -> ((BleMainActivity)getActivity()).BlewriteData(CDS_LAMP_OFF_READY));
        Button offSetBtn = view.findViewById(R.id.off_set);
        offSetBtn.setOnClickListener(v -> ((BleMainActivity)getActivity()).BlewriteData(CDS_LAMP_OFF_SETTING));

        readData = view.findViewById(R.id.readData_cds);

        return view;
    }
    public void readData(String data){

        if(data.contains("$PS,R,")){
            dialog.dismiss();
            ((BleMainActivity)getActivity()).BlewriteData(ADMIN_PASSWORD);
        }
        /*if(data.contains("$PS,A,")){
            dialog.dismiss();
        }*/

        readData.setText(data);

        Log.d(TAG, "readData! : " + data);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof CDS_Setting_Listener) {
            //selecetBleListener = (Ble_Status_Listener) context;
            this.activity = (Activity) context;
        } else {
            throw new RuntimeException((context.toString() + " must implement SelectBleListener"));
        }
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        //CdsFlag = false;
        dialog.dismiss();
        ((CDS_Setting_Listener) activity).onDetachFragment_CDS_Setting();
        super.onDetach();
    }

    public interface CDS_Setting_Listener {
        void onCreateViewFragment_CDS_Setting();

        void onDetachFragment_CDS_Setting();
    }
}
