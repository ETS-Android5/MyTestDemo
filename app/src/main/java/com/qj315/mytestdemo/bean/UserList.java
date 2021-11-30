package com.qj315.mytestdemo.bean;

import cn.bmob.v3.BmobObject;

public class UserList extends BmobObject {
    private String feedbackmsg;
    private String feedbackuser;

    public String getFeedbackmsg() {
        return feedbackmsg;
    }

    public void setFeedbackmsg(String feedbackmsg) {
        this.feedbackmsg = feedbackmsg;
    }

    public String getFeedbackuser() {
        return feedbackuser;
    }

    public void setFeedbackuser(String feedbackuser) {
        this.feedbackuser = feedbackuser;
    }
}
