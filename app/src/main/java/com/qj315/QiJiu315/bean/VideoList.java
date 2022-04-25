package com.qj315.QiJiu315.bean;

import cn.bmob.v3.BmobObject;

public class VideoList extends BmobObject {
    private String name;//视频名称
    private String urls;//视频地址

    public String getUrls() {
        return urls;
    }

    public void setUrl(String urls) {
        this.urls = urls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
