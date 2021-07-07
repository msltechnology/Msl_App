package com.msl.mslapp.Public.Log;

public class navigation_Log_ListView {

    // 추가할 내용/ 현재시간, 내용 2개로 칸 나누고 중요 내용은 색 변경하기.

    //private Drawable iconDrawable ;
    private String logCurrentTime ;
    private String logData ;
    private String logBackColor = "white" ;

    /*public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }*/
    public void setLogCurrentTime(String userdata) {
        logCurrentTime = userdata;
    }
    public void setLogData(String name) {
        logData = name ;
    }public void setLogBackColor(String color) {
        logBackColor = color ;
    }

    /*public Drawable getIcon() {
        return this.iconDrawable ;0.


    }*/
    public String getLogCurrentTime() {
        return this.logCurrentTime ;
    }
    public String getLogData() {
        return this.logData ;
    }
    public String getLogBackColor() {
        return this.logBackColor ;
    }
}
