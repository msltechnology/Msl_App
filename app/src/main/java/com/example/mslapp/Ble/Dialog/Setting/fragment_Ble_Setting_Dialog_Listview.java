package com.example.mslapp.Ble.Dialog.Setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mslapp.R;

import static com.example.mslapp.Ble.Dialog.Setting.dialogFragment_Ble_Setting_FL_Setting.select_FL;
import static com.example.mslapp.Ble.Dialog.Setting.dialogFragment_Ble_Setting_FL_Setting.select_Sec;

public class fragment_Ble_Setting_Dialog_Listview extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Setting-Dialog-ListView";

    Context settingDialogContext;

    TableLayout tableLayout;
    private TableRow tableRowData = null;
    private TableRow tableRowBlank = null;
    TextView tv_FL_Data;
    Button btn_FL_Data;

    String[][] FL_list_String;

    dialogFragment_Ble_Setting_FL_Setting dialogFragment_Ble_Setting_FL_Setting_fragment;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "fragment_Ble_Setting_Dialog_ListView onCreateView");

        view = inflater.inflate(R.layout.ble_fragment_setting_dialog_fl_setting_listview, null);

        settingDialogContext = this.getContext();

        tableLayout = view.findViewById(R.id.tl_ble_fragment_setting_dialog);

        dialogFragment_Ble_Setting_FL_Setting_fragment = (dialogFragment_Ble_Setting_FL_Setting) getParentFragment();

        tableTitleSetting();


        return view;
    }

    @SuppressLint("ResourceAsColor")
    void tableTitleSetting() {

        switch (select_FL) {
            case "-1":
                FL_list_String = FL_List.FL_list_Default;
                break;
            case "1":
                FL_list_String = FL_List.FL_list_1;
                break;
            case "2":
                FL_list_String = FL_List.FL_list_2;
                break;
            case "3":
                FL_list_String = FL_List.FL_list_3;
                break;
            case "4":
                FL_list_String = FL_List.FL_list_4;
                break;
            case "5":
                FL_list_String = FL_List.FL_list_5;
                break;
            case "6":
                FL_list_String = FL_List.FL_list_6;
                break;
            case "2_1":
                FL_list_String = FL_List.FL_list_2_1;
                break;
            case "ISO":
                FL_list_String = FL_List.FL_list_ISO;
                break;
            case "LFL":
                FL_list_String = FL_List.FL_list_LFL;
                break;
            case "FFL":
                FL_list_String = FL_List.FL_list_FFL;
                break;
            case "MO":
                FL_list_String = FL_List.FL_list_MO;
                break;
            case "OC":
                FL_list_String = FL_List.FL_list_OC;
                break;
            case "Q":
                FL_list_String = FL_List.FL_list_Q;
                break;
            case "VQ":
                FL_list_String = FL_List.FL_list_VQ;
                break;
            case "AI":
                FL_list_String = FL_List.FL_list_AI;
                break;
            default:

                break;
        }

        Log.d(TAG, "select_sec : " + select_Sec + " and select_FL : " + select_FL);


        for (int i = 0; i < FL_list_String.length; i++) {
            // 부를때 마다 초기화
            tableRowData = new TableRow(getActivity());
            tableRowData.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < 24; j++) {
                // 초기값일 경우 대표적 섬광표 보이게
                if(select_Sec.equals("-1") && select_FL.equals("-1")){
                    listSetting(i,j);
                }else if(select_Sec.equals("-1") || select_FL.equals("-1")){
                    return;
                }
                // 해당 초 값을 가진 값만 나오게 / 만약에 포값만 변경했을 시 해당 초 값을 가진 모든 표 보임.
                else if (select_Sec.equals((FL_list_String[i][2]).replace(" S", ""))) {
                    listSetting(i,j);
                }
            }

            if (tableRowData.getVirtualChildCount() != 0) {
                tableRowBlank = new TableRow(getActivity());
                tableRowBlank.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                tv_FL_Data = new TextView(getActivity());
                tv_FL_Data.setLayoutParams(new TableRow.LayoutParams(
                        (int) TypedValue.applyDimension
                                (TypedValue.COMPLEX_UNIT_DIP, 54, getResources().getDisplayMetrics()),
                        (int) TypedValue.applyDimension
                                (TypedValue.COMPLEX_UNIT_DIP, 2
                                        , getResources().getDisplayMetrics())));

                tableRowBlank.addView(tv_FL_Data);
                tableLayout.addView(tableRowBlank);

                tableLayout.addView(tableRowData);
            }
        }
    }


    public void btnOnClick(String FL_code) {
        dialogFragment_Ble_Setting_FL_Setting_fragment.btnSelectFL(FL_code);
    }

    public void listSetting(int i, int j){
        if (j == 0) {
            String FL_code = FL_list_String[i][j];

            btn_FL_Data = new Button(getActivity());

            btn_FL_Data.setTextSize(12);
            btn_FL_Data.setTextColor(0xFF000000);
            btn_FL_Data.setText(FL_code);
            btn_FL_Data.setGravity(Gravity.CENTER);
            btn_FL_Data.setOnClickListener(view -> btnOnClick(FL_code));
            btn_FL_Data.setLayoutParams(new TableRow.LayoutParams(
                    (int) TypedValue.applyDimension
                            (TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension
                            (TypedValue.COMPLEX_UNIT_DIP, 52
                                    , getResources().getDisplayMetrics())));
            if(!(FL_list_String[i][22]).equals("") || !(FL_list_String[i][23]).equals("")){
                btn_FL_Data.setBackgroundColor(ContextCompat.getColor(settingDialogContext, R.color.dialog_select_special));
            }else{
                btn_FL_Data.setBackgroundColor(ContextCompat.getColor(settingDialogContext, R.color.dialog_select));
            }
            tableRowData.addView(btn_FL_Data);
        }

        tv_FL_Data = new TextView(getActivity());
        tv_FL_Data.setTextSize(12);
        tv_FL_Data.setTextColor(0xFF000000);
        tv_FL_Data.setText(FL_list_String[i][j]);
        tv_FL_Data.setGravity(Gravity.CENTER);
        if(j != 23){
            tv_FL_Data.setLayoutParams(new TableRow.LayoutParams(
                    (int) TypedValue.applyDimension
                            (TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension
                            (TypedValue.COMPLEX_UNIT_DIP, 52
                                    , getResources().getDisplayMetrics())));
        }else{
            tv_FL_Data.setLayoutParams(new TableRow.LayoutParams(
                    (int) TypedValue.applyDimension
                            (TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension
                            (TypedValue.COMPLEX_UNIT_DIP, 52
                                    , getResources().getDisplayMetrics())));
        }

        if (j == 0) {
            tv_FL_Data.setBackgroundColor(ContextCompat.getColor(settingDialogContext, R.color.dialog_Num));
        } else if (j == 1) {
            tv_FL_Data.setBackgroundColor(ContextCompat.getColor(settingDialogContext, R.color.dialog_FL));
        } else if (j == 2) {
            tv_FL_Data.setBackgroundColor(ContextCompat.getColor(settingDialogContext, R.color.dialog_Sec));
        } else if (j == 21) {
            tv_FL_Data.setBackgroundColor(ContextCompat.getColor(settingDialogContext, R.color.dialog_Dip_SW));
        } else if (j == 22) {
            tv_FL_Data.setBackgroundColor(ContextCompat.getColor(settingDialogContext, R.color.dialog_division));
        } else if (j == 23) {
            tv_FL_Data.setBackgroundColor(ContextCompat.getColor(settingDialogContext, R.color.dialog_remarks));
        } else if ((j % 2) == 0) {
            tv_FL_Data.setBackgroundColor(ContextCompat.getColor(settingDialogContext, R.color.dialog_light_Off));
        } else if ((j % 2) == 1) {
            tv_FL_Data.setBackgroundColor(ContextCompat.getColor(settingDialogContext, R.color.dialog_light_On));
        }

        tableRowData.addView(tv_FL_Data);
    }
}
