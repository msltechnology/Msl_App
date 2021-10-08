package com.msl.mslapp.ble.Dialog.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.msl.mslapp.R;

import static com.msl.mslapp.ble.Dialog.setting.dialogFragment_Ble_Setting_FL_Setting.select_FL;
import static com.msl.mslapp.ble.Dialog.setting.dialogFragment_Ble_Setting_FL_Setting.select_Sec;

public class fragment_Ble_Setting_Dialog_Listview extends Fragment {

    // 로그 이름 용
    public static final String TAG = "Msl-Ble-Setting-Dialog-ListView";

    Context settingDialogContext;

    TableLayout tableLayout;
    private TableRow tableRowData = null;
    private TableRow tableRowBlank = null;
    TextView tv_FL_Data;
    ImageButton btn_FL_Data;

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

            // 첫번째(머릿말) 데이터빼고 나머지 자료값에 배경색 지정
            if (tableRowData.getVirtualChildCount() != 0) {
                /*tableRowBlank = new TableRow(getActivity());
                tableRowBlank.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                tv_FL_Data = new TextView(getActivity());
                tv_FL_Data.setLayoutParams(new TableRow.LayoutParams(
                        (int) TypedValue.applyDimension
                                (TypedValue.COMPLEX_UNIT_DIP, 54, getResources().getDisplayMetrics()),
                        (int) TypedValue.applyDimension
                                (TypedValue.COMPLEX_UNIT_DIP, 2
                                        , getResources().getDisplayMetrics())));

                tableRowBlank.addView(tv_FL_Data);
                tableLayout.addView(tableRowBlank);*/

                tableRowData.setBackgroundColor(Color.WHITE);
                //tableRowData.setPadding(0,1,0,1);

                tableLayout.addView(tableRowData);
            }
        }
    }


    public void btnOnClick(String FL_code) {
        dialogFragment_Ble_Setting_FL_Setting_fragment.btnSelectFL(FL_code);
    }

    public void listSetting(int i, int j){
        /*if(j == 0){
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
        }*/

        // 맨처음 이미지 정하기
        if(j == 0){
            String FL_code = FL_list_String[i][j];

            btn_FL_Data = new ImageButton(getActivity());
            btn_FL_Data.setBackgroundColor(Color.parseColor("#Ffffff"));
            btn_FL_Data.setImageResource(R.drawable.ble_fragment_setting_dialog_select);
            btn_FL_Data.setOnClickListener(view -> {
                //btn_FL_Data.setImageResource(R.drawable.ble_fragment_setting_dialog_selected);
                tableLayout.removeAllViews();

                btnOnClick(FL_code);

                for (int k = 0; k < FL_list_String.length; k++) {
                    // 부를때 마다 초기화
                    tableRowData = new TableRow(getActivity());
                    tableRowData.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                    for (int l = 0; l < 24; l++) {
                        // 초기값일 경우 대표적 섬광표 보이게
                        if(select_Sec.equals("-1") && select_FL.equals("-1")){
                            selectListSetting(k,l,FL_code);
                        }else if(select_Sec.equals("-1") || select_FL.equals("-1")){
                            return;
                        }
                        // 해당 초 값을 가진 값만 나오게 / 만약에 포값만 변경했을 시 해당 초 값을 가진 모든 표 보임.
                        else if (select_Sec.equals((FL_list_String[k][2]).replace(" S", ""))) {
                            selectListSetting(k,l,FL_code);
                        }
                    }

                    // 첫번째(머릿말) 데이터빼고 나머지 자료값에 배경색 지정
                    if (tableRowData.getVirtualChildCount() != 0) {
                /*tableRowBlank = new TableRow(getActivity());
                tableRowBlank.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                tv_FL_Data = new TextView(getActivity());
                tv_FL_Data.setLayoutParams(new TableRow.LayoutParams(
                        (int) TypedValue.applyDimension
                                (TypedValue.COMPLEX_UNIT_DIP, 54, getResources().getDisplayMetrics()),
                        (int) TypedValue.applyDimension
                                (TypedValue.COMPLEX_UNIT_DIP, 2
                                        , getResources().getDisplayMetrics())));

                tableRowBlank.addView(tv_FL_Data);
                tableLayout.addView(tableRowBlank);*/

                        tableRowData.setBackgroundColor(Color.WHITE);
                        //tableRowData.setPadding(0,1,0,1);

                        tableLayout.addView(tableRowData);
                    }
                }


            });
            btn_FL_Data.setAdjustViewBounds(true);

            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    (int) TypedValue.applyDimension
                            (TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension
                            (TypedValue.COMPLEX_UNIT_DIP, 40
                                    , getResources().getDisplayMetrics()));

            btn_FL_Data.setLayoutParams(params);

            tableRowData.setGravity(Gravity.CENTER);
            tableRowData.addView(btn_FL_Data);
        }



        // 그 이후 text 데이터 정하기
        tv_FL_Data = new TextView(getActivity());
        tv_FL_Data.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        tv_FL_Data.setTextColor(0xFF000000);
        tv_FL_Data.setText(FL_list_String[i][j]);
        tv_FL_Data.setGravity(Gravity.CENTER);

        // 맨마지막 데이터 칸 넓이 변경
        if(j != 23){
            tv_FL_Data.setLayoutParams(new TableRow.LayoutParams(
                    (int) TypedValue.applyDimension
                            (TypedValue.COMPLEX_UNIT_DIP, 57, getResources().getDisplayMetrics()),
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


        // 첫 텍스트 특정 데이터일 경우 색 변경
        if (j == 0) {
            if(!(FL_list_String[i][22]).equals("") || !(FL_list_String[i][23]).equals("")){
                tv_FL_Data.setTextColor(ContextCompat.getColor(settingDialogContext, R.color.ble_scan_list_MSL));
            }else{
                tv_FL_Data.setTextColor(ContextCompat.getColor(settingDialogContext, R.color.black));
            }
            tableRowData.addView(tv_FL_Data);
            return;
        }

        /*if (j == 0) {
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
        }*/

        tableRowData.addView(tv_FL_Data);
    }



    void selectListSetting(int i, int j, String flCode){



        // 맨처음 이미지 정하기
        if(j == 0){
            String FL_code = FL_list_String[i][j];

            btn_FL_Data = new ImageButton(getActivity());
            btn_FL_Data.setBackgroundColor(Color.parseColor("#Ffffff"));
            if(FL_code.equals(flCode)){
                btn_FL_Data.setImageResource(R.drawable.ble_fragment_setting_dialog_selected);
            }else{
                btn_FL_Data.setImageResource(R.drawable.ble_fragment_setting_dialog_select);
            }
            btn_FL_Data.setOnClickListener(view -> {
                //btn_FL_Data.setImageResource(R.drawable.ble_fragment_setting_dialog_selected);
                tableLayout.removeAllViews();

                btnOnClick(FL_code);

                for (int k = 0; k < FL_list_String.length; k++) {
                    // 부를때 마다 초기화
                    tableRowData = new TableRow(getActivity());
                    tableRowData.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                    for (int l = 0; l < 24; l++) {
                        // 초기값일 경우 대표적 섬광표 보이게
                        if(select_Sec.equals("-1") && select_FL.equals("-1")){
                            selectListSetting(k,l,FL_code);
                        }else if(select_Sec.equals("-1") || select_FL.equals("-1")){
                            return;
                        }
                        // 해당 초 값을 가진 값만 나오게 / 만약에 포값만 변경했을 시 해당 초 값을 가진 모든 표 보임.
                        else if (select_Sec.equals((FL_list_String[k][2]).replace(" S", ""))) {
                            selectListSetting(k,l,FL_code);
                        }
                    }

                    // 첫번째(머릿말) 데이터빼고 나머지 자료값에 배경색 지정
                    if (tableRowData.getVirtualChildCount() != 0) {
                /*tableRowBlank = new TableRow(getActivity());
                tableRowBlank.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                tv_FL_Data = new TextView(getActivity());
                tv_FL_Data.setLayoutParams(new TableRow.LayoutParams(
                        (int) TypedValue.applyDimension
                                (TypedValue.COMPLEX_UNIT_DIP, 54, getResources().getDisplayMetrics()),
                        (int) TypedValue.applyDimension
                                (TypedValue.COMPLEX_UNIT_DIP, 2
                                        , getResources().getDisplayMetrics())));

                tableRowBlank.addView(tv_FL_Data);
                tableLayout.addView(tableRowBlank);*/

                        tableRowData.setBackgroundColor(Color.WHITE);
                        //tableRowData.setPadding(0,1,0,1);

                        tableLayout.addView(tableRowData);
                    }
                }
            });
            btn_FL_Data.setAdjustViewBounds(true);

            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    (int) TypedValue.applyDimension
                            (TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension
                            (TypedValue.COMPLEX_UNIT_DIP, 40
                                    , getResources().getDisplayMetrics()));

            btn_FL_Data.setLayoutParams(params);

            tableRowData.setGravity(Gravity.CENTER);
            tableRowData.addView(btn_FL_Data);
        }



        // 그 이후 text 데이터 정하기
        tv_FL_Data = new TextView(getActivity());
        tv_FL_Data.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        tv_FL_Data.setTextColor(0xFF000000);
        tv_FL_Data.setText(FL_list_String[i][j]);
        tv_FL_Data.setGravity(Gravity.CENTER);

        // 맨마지막 데이터 칸 넓이 변경
        if(j != 23){
            tv_FL_Data.setLayoutParams(new TableRow.LayoutParams(
                    (int) TypedValue.applyDimension
                            (TypedValue.COMPLEX_UNIT_DIP, 57, getResources().getDisplayMetrics()),
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


        // 첫 텍스트 특정 데이터일 경우 색 변경
        if (j == 0) {
            if(!(FL_list_String[i][22]).equals("") || !(FL_list_String[i][23]).equals("")){
                tv_FL_Data.setTextColor(ContextCompat.getColor(settingDialogContext, R.color.ble_scan_list_MSL));
            }else{
                tv_FL_Data.setTextColor(ContextCompat.getColor(settingDialogContext, R.color.black));
            }
            tableRowData.addView(tv_FL_Data);
            return;
        }

        tableRowData.addView(tv_FL_Data);
    }
}
