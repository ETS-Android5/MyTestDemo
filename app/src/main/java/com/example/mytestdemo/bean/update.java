package com.example.mytestdemo.bean;


import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class update extends BmobObject {

    BmobFile APK;
    String text;
    String Code;
    String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BmobFile getAPK() {
        return APK;
    }

    public void setAPK(BmobFile APK) {
        this.APK = APK;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }


    public String getapkUrl() {
        return APK.getFileUrl();
    }


}