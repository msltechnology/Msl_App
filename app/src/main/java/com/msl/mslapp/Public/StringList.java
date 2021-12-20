package com.msl.mslapp.Public;

import static com.msl.mslapp.RTUMainActivity.DATA_NUM_1;
import static com.msl.mslapp.RTUMainActivity.DATA_NUM_6;

public class StringList {
    //region 블루투스 데이터 용
    //데이터 기호
    public static final String DATA_SIGN_START = "$";
    public static final String DATA_SIGN_CHECKSUM = "*";
    public static final String DATA_SIGN_COMMA = ",";
    public static final String DATA_SIGN_CR = "\r"; //<CR>
    public static final String DATA_SIGN_LF = "\n"; //<LF>
    //데이터 타입
    public static final String DATA_TYPE_LICMD = "LICMD"; // 명령 커맨드
    public static final String DATA_TYPE_MUCMD = "MUCMD"; // 명령 커맨드
    public static final String DATA_TYPE_LISTS = "LISTS"; // 상태 정보
    public static final String DATA_TYPE_LISET = "LISET"; // 설정 정보
    public static final String DATA_TYPE_RTU_READ = "[ ConfMsg]"; // RTU 데이터
    public static final String DATA_TYPE_RTU_MODEM_READ = "[ModemMsg]"; // RTU 모뎀 데이터
    public static final String DATA_TYPE_PS = "PS"; //패스워드
    public static final String DATA_TYPE_S = "S"; //설정
    public static final String DATA_TYPE_I = "I"; //설정
    public static final String DATA_TYPE_R = "R"; //Request
    public static final String DATA_TYPE_A = "A"; //
    public static final String DATA_TYPE_W = "W"; //
    public static final String DATA_TYPE_Y = "Y"; //
    public static final String DATA_TYPE_X = "X"; //
    public static final String DATA_TYPE_Z = "Z"; //
    public static final String DATA_TYPE_SID = "SID"; //ID 설정
    public static final String DATA_TYPE_RMC = "RMC"; //리모컨 모드
    public static final String DATA_TYPE_DIP = "DIP"; //DIP SW 모드
    public static final String DATA_TYPE_RST = "RST"; //공장 초기화
    public static final String DATA_TYPE_BTV = "BTV"; //배터리 데이터 확인
    public static final String DATA_TYPE_SLV = "SLV"; //솔라전압 데이터 확인
    public static final String DATA_TYPE_SLC = "SLC"; //태양광 전류 테이터 확인
    public static final String DATA_TYPE_SNB = "SNB"; //시리얼넘버 확인
    public static final String DATA_TYPE_GP1 = "GP1"; //낮동안 GPS 할성화
    public static final String DATA_TYPE_GP0 = "GP0"; //저녁동안에만 활성화
    public static final String DATA_TYPE_DEL = "DEL"; //저녁동안에만 활성화
    public static final String DATA_TYPE_ADMIN = "ZFVVS"; //저녁동안에만 활성화
    public static final String DATA_TYPE_0 = "0";
    public static final String DATA_TYPE_1 = "1"; //상태요청
    public static final String DATA_TYPE_2 = "2"; //강제점등
    public static final String DATA_TYPE_3 = "3"; //강제소등
    public static final String DATA_TYPE_4 = "4"; //리셋
    public static final String DATA_TYPE_5 = "5"; //부동광
    public static final String DATA_TYPE_6 = "6";
    public static final String DATA_TYPE_13 = "13"; // Lowpower 모드
    public static final String DATA_TYPE_14 = "14"; // RTU 통신 시작
    public static final String DATA_TYPE_3dot8 = "3.8"; // Lowpower startpoint
    public static final String DATA_TYPE_4dot0 = "4.0"; // Lowpower endpoint



    //디바이스 ID
    public static final String DATA_ID_255 = "255";
    //$MUCMD,14,1*11<CR><LF> : RTU에서 데이터 보내게함
    public final static String DATA_RTU_BLUETOOTH_SENDING = DATA_SIGN_START
            + DATA_TYPE_MUCMD + DATA_SIGN_COMMA
            + DATA_TYPE_14 + DATA_SIGN_COMMA
            + DATA_TYPE_1 + DATA_SIGN_CHECKSUM
            + DATA_TYPE_1 + DATA_TYPE_1;
    // $LICMD,S,GP1,255* : GPS 낮에도 작동
    public final static String GPS_SET_ON = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_S + DATA_SIGN_COMMA
            + DATA_TYPE_GP1 + DATA_SIGN_COMMA
            + DATA_ID_255 + DATA_SIGN_CHECKSUM;
    // $LICMD,S,GP0,255* : GPS 밤에만 작동
    public final static String GPS_SET_OFF = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_S + DATA_SIGN_COMMA
            + DATA_TYPE_GP0 + DATA_SIGN_COMMA
            + DATA_ID_255 + DATA_SIGN_CHECKSUM;
    // $LICMD,S,SLV,255* : 솔라판 각 전류 확인
    public final static String DATA_REQUEST_SLC = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_S + DATA_SIGN_COMMA
            + DATA_TYPE_SLC + DATA_SIGN_COMMA
            + DATA_ID_255 + DATA_SIGN_CHECKSUM;
    // $LICMD,S,SLV,255* : 솔라판 각 전압 확인
    public final static String DATA_REQUEST_SLV = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_S + DATA_SIGN_COMMA
            + DATA_TYPE_SLV + DATA_SIGN_COMMA
            + DATA_ID_255 + DATA_SIGN_CHECKSUM;
    // $LICMD,S,BTV,255* : 배터리 각 전압 확인
    public final static String DATA_REQUEST_BTV = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_S + DATA_SIGN_COMMA
            + DATA_TYPE_BTV + DATA_SIGN_COMMA
            + DATA_ID_255 + DATA_SIGN_CHECKSUM;
    //$PS,A,ZFVVS* : 관리자 패스워드
    public final static String ADMIN_PASSWORD = DATA_SIGN_START
            + DATA_TYPE_PS + DATA_SIGN_COMMA
            + DATA_TYPE_A + DATA_SIGN_COMMA
            + DATA_TYPE_ADMIN
            + DATA_SIGN_CHECKSUM;
    //$LICMD,Z,255* : 소등 설정
    public final static String CDS_LAMP_OFF_SETTING = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_Z + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$LICMD,X,255* : 소등 준비
    public final static String CDS_LAMP_OFF_READY = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_X + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$LICMD,Y,255* : 점등 설정
    public final static String CDS_LAMP_ON_SETTING = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_Y + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$LICMD,W,255* : 점등 준비
    public final static String CDS_LAMP_ON_READY = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_W + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$LICMD,S,RST,255* : 공장 초기화
    public final static String DATA_SET_RST = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_S + DATA_SIGN_COMMA
            + DATA_TYPE_RST + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$LICMD,S,DIP,255* : DIP SW 모드
    public final static String DATA_SET_DIP = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_S + DATA_SIGN_COMMA
            + DATA_TYPE_DIP + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$LICMD,S,RMC,255* : 리모컨 모드
    public final static String DATA_SET_RMC = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_S + DATA_SIGN_COMMA
            + DATA_TYPE_RMC + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$LICMD,5,255* : 부동광
    public final static String DATA_LAMP_FIXED = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_5 + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$LICMD,4,255* : 리셋
    public final static String DATA_DEVICE_RESET = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_4 + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$LICMD,3,255* : 강제소등
    public final static String DATA_LAMP_OFF = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_3 + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$LICMD,2,255* : 강제점등
    public final static String DATA_LAMP_ON = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_2 + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$LICMD,I* : 정보요청(펌웨어 버전 및 GPS 상태, delay time 값 등)
    public final static String DATA_REQUEST_INFORMATION = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_I
            + DATA_SIGN_CHECKSUM;
    //$LICMD,1,255* : 상태요청
    public final static String DATA_REQUEST_STATUS = DATA_SIGN_START
            + DATA_TYPE_LICMD + DATA_SIGN_COMMA
            + DATA_TYPE_1 + DATA_SIGN_COMMA
            + DATA_ID_255
            + DATA_SIGN_CHECKSUM;
    //$MUCMD,13,1,1,3.8,4.0*11 : lowpower 모드 ( on/off, 주기(시간), on 값, off 값)
    public final static String LOW_MODE_ON = DATA_SIGN_START
            + DATA_TYPE_MUCMD + DATA_SIGN_COMMA
            + DATA_TYPE_13 + DATA_SIGN_COMMA
            + DATA_TYPE_1 + DATA_SIGN_COMMA
            + DATA_TYPE_1 + DATA_SIGN_COMMA
            + DATA_TYPE_3dot8 + DATA_SIGN_COMMA
            + DATA_TYPE_4dot0
            + DATA_SIGN_CHECKSUM
            + DATA_TYPE_1 + DATA_TYPE_1
            + DATA_SIGN_CR + DATA_SIGN_LF;
    ;
    //$MUCMD,13,0,1,3.8,4.0*11 : lowpower 모드
    public final static String LOW_MODE_OFF = DATA_SIGN_START
            + DATA_TYPE_MUCMD + DATA_SIGN_COMMA
            + DATA_TYPE_13 + DATA_SIGN_COMMA
            + DATA_TYPE_0 + DATA_SIGN_COMMA
            + DATA_TYPE_1 + DATA_SIGN_COMMA
            + DATA_TYPE_3dot8 + DATA_SIGN_COMMA
            + DATA_TYPE_4dot0
            + DATA_SIGN_CHECKSUM
            + DATA_TYPE_1 + DATA_TYPE_1
            + DATA_SIGN_CR + DATA_SIGN_LF;
    ;
    // RTU 리셋
    public final static String RTU_RESET = DATA_SIGN_START + DATA_TYPE_MUCMD + DATA_SIGN_COMMA +
            DATA_TYPE_6 + DATA_SIGN_CHECKSUM +
            DATA_NUM_1 + DATA_NUM_1 +
            DATA_SIGN_CR + DATA_SIGN_LF;
    ;
}
