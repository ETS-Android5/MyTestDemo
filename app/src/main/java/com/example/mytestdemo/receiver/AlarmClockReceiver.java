package com.example.mytestdemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.mytestdemo.services.MyIntentService;

public class AlarmClockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.i("TAG", "onReceive: ");
//        String msg = intent.getStringExtra("msg");
//        Log.i("闹钟内容：TAG", "I can live forever");
        Toast.makeText(context, "程序后台运行中！", Toast.LENGTH_SHORT).show();
        if(Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
            Log.i("闹钟内容：TAG", "系统时间广播");
        }
//        if (Intent.ACTION_PACKAGE_RESTARTED.equals(intent.getAction())){
//            if (!MyIntentService.isAlive){
//                context.startService(new Intent(context, MyIntentService.class));
//            }
//        }
        if ("android.intent.action.service.start".equals(intent.getAction())){
            Log.i("TAG","onReceive:要活了");
            context.startService(new Intent(context, MyIntentService.class));
        }
    }
}