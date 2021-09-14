package com.example.mytestdemo.userlist;

import cn.bmob.v3.BmobObject;

public class UserList extends BmobObject {
    private String userlist;

    public String getUserlist() {
        return userlist;
    }

    public void setUserlist(String userlist) {
        this.userlist = userlist;
    }
}
