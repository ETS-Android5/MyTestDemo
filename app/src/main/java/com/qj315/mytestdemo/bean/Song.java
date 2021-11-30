package com.qj315.mytestdemo.bean;


import cn.bmob.v3.BmobObject;

public class Song extends BmobObject {

    public String song;//歌曲名
    public String singer;//歌手
    public String path;//歌曲地址
    public long size;//歌曲所占空间大小
    public int duration;//歌曲时间长度

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}