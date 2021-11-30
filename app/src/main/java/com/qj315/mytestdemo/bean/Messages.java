package com.qj315.mytestdemo.bean;

import cn.bmob.v3.BmobObject;

public class Messages extends BmobObject {
    private String Notice;

    public String getNotice() {
        return Notice;
    }

    public void setNotice(String notice) {
        Notice = notice;
    }
}
