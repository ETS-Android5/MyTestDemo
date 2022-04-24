package com.qj315.QiJiu315.bean;

import cn.bmob.v3.BmobObject;

public class VideoList extends BmobObject {
    private String url;//视频地址
    private String name;//视频名称

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
