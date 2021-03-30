package com.example.mslapp.Ble.fragment;

import android.app.ProgressDialog;
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

import com.example.mslapp.BleMainActivity;
import com.example.mslapp.R;

import static com.example.mslapp.BleMainActivity.cdsFlag;

public class fragment_CDS_Setting extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-CDS";

    View view;

    TextView readData;

    // 로딩바
    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");
        view = inflater.inflate(R.layout.ble_fragment_cds, null);

        dialog = new ProgressDialog(view.getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading");
        dialog.show();

        Button onPrepareBtn = view.findViewById(R.id.on_prepare);
        onPrepareBtn.setOnClickListener(v -> ((BleMainActivity)getActivity()).BlewriteData("LICMD,W,255"));
        Button onSetBtn = view.findViewById(R.id.on_set);
        onSetBtn.setOnClickListener(v -> ((BleMainActivity)getActivity()).BlewriteData("LICMD,Y,255"));
        Button offPrepareBtn = view.findViewById(R.id.off_prepare);
        offPrepareBtn.setOnClickListener(v -> ((BleMainActivity)getActivity()).BlewriteData("LICMD,X,255"));
        Button offSetBtn = view.findViewById(R.id.off_set);
        offSetBtn.setOnClickListener(v -> ((BleMainActivity)getActivity()).BlewriteData("LICMD,Z,255"));

        readData = view.findViewById(R.id.readData_cds);

        return view;
    }
    public void readData(String data){

        if(data.contains("$PS,R,")){
            dialog.dismiss();
            ((BleMainActivity)getActivity()).BlewriteData("PS,A,ZFVVS");
        }
        /*if(data.contains("$PS,A,")){
            dialog.dismiss();
        }*/

        readData.setText(data);

        Log.d(TAG, "readData! : " + data);
    }

    @Override
    public void onDetach() {
        cdsFlag = false;
        dialog.dismiss();
        super.onDetach();
    }
}
