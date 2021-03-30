package com.example.mslapp.Ble.blelistview;

public class BleScanListView {
    //private Drawable iconDrawable ;
    private String bleUserdataStr ;
    private String bleNameStr ;
    private String bleAddressStr ;
    private String bleSignStr ;

    /*public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }*/
    public void setBleUserdata(String userdata) {
        bleUserdataStr = userdata ;
    }
    public void setBleName(String name) {
        bleNameStr = name ;
    }
    public void setBleAddress(String address) {
        bleAddressStr = address ;
    }
    public void setBleSign(String sign) {
        bleSignStr = sign ;
    }

    /*public Drawable getIcon() {
        return this.iconDrawable ;0.


    }*/
    public String getBleUserdata() {
        return this.bleUserdataStr ;
    }
    public String getBleName() {
        return this.bleNameStr ;
    }
    public String getBleAddress() {
        return this.bleAddressStr ;
    }
    public String getBleSign() {
        return this.bleSignStr ;
    }
}
