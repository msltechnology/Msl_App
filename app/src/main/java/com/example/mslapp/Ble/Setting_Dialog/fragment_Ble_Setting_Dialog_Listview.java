package com.example.mslapp.Ble.Setting_Dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mslapp.R;

import static com.example.mslapp.Ble.Setting_Dialog.dialogFragment_Ble_Setting_FL_Setting.select_FL;
import static com.example.mslapp.Ble.Setting_Dialog.dialogFragment_Ble_Setting_FL_Setting.select_Sec;

public class fragment_Ble_Setting_Dialog_Listview extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Setting-Dialog-ListView";

    TableLayout tableLayout;
    private TableRow tableRowData = null;
    TextView tv_FL_Data;

    String[][] FL_list_String;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "fragment_Ble_Setting_Dialog_ListView onCreateView");

        view = inflater.inflate(R.layout.ble_fragment_setting_dialog_fl_setting_listview, null);

        tableLayout = view.findViewById(R.id.tl_ble_fragment_setting_dialog);

        tableTitleSetting();


        return view;
    }

    void tableTitleSetting() {

        switch (select_FL){
            case "1":
                FL_list_String = FL_List.FL_list_1;
                break;
            case "2":
                FL_list_String = FL_List.FL_list_2;
                break;
            default:
                FL_list_String = FL_List.FL_list_1;
                break;
        }


        for(int i = 0; i < FL_list_String.length; i++){
            // 부를때 마다 초기화
            tableRowData = new TableRow(getActivity());
            tableRowData.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
            for(int j = 0; j < 24; j++){

                Log.d(TAG,select_Sec + " , " + (FL_list_String[i][2]).replace("S",""));

                if(select_Sec.equals((FL_list_String[i][2]).replace(" S",""))){
                    tv_FL_Data = new TextView(getActivity());
                    tv_FL_Data.setTextSize(14);
                    tv_FL_Data.setTextColor(0xFF000000);
                    tv_FL_Data.setText(FL_list_String[i][j]);
                    tv_FL_Data.setGravity(Gravity.CENTER);
                    tv_FL_Data.setLayoutParams(new TableRow.LayoutParams(
                            (int) TypedValue.applyDimension
                                    (TypedValue.COMPLEX_UNIT_DIP, 54, getResources().getDisplayMetrics()),
                            (int) TypedValue.applyDimension
                                    (TypedValue.COMPLEX_UNIT_DIP, 36, getResources().getDisplayMetrics())));
                    if(j == 0){
                        tv_FL_Data.setBackgroundColor(0xFF90CAF9);
                    }else if(j == 1){
                        tv_FL_Data.setBackgroundColor(Color.BLUE);
                    }else if(j == 2){
                        tv_FL_Data.setBackgroundColor(Color.YELLOW);
                    }else if(j == 21){
                        tv_FL_Data.setBackgroundColor(Color.MAGENTA);
                    }else if(j == 22){
                        tv_FL_Data.setBackgroundColor(Color.CYAN);
                    }else if(j == 23){
                        tv_FL_Data.setBackgroundColor(Color.DKGRAY);
                    }else if((j%2) == 0){
                        tv_FL_Data.setBackgroundColor(Color.GREEN);
                    }else if((j%2) == 1){
                        tv_FL_Data.setBackgroundColor(Color.RED);
                    }

                    tableRowData.addView(tv_FL_Data);
                }else{

                }
            }
            tableLayout.addView(tableRowData);
        }
    }

}
