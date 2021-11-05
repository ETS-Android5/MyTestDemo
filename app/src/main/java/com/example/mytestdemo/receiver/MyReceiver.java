package com.example.mytestdemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    private Message message;

    @Override
    public void onReceive(Context context, Intent intent) {
        //接收MainActivity传过来的数据
//        Toast.makeText(context, intent.getStringExtra("hello"), Toast.LENGTH_SHORT).show();
        Log.i("TAG", "onReceive: "+intent.getStringExtra("hello"));

        //调用Message接口的方法
        message.getMsg("调用");
    }

    public interface Message {
        void getMsg(String str);
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}