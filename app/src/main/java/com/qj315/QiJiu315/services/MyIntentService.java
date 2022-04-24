package com.qj315.QiJiu315.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.qj315.QiJiu315.mysql.MySQL_Utils;
import com.qj315.QiJiu315.receiver.AlarmClockReceiver;

import java.io.IOException;
import java.net.Socket;

/**
 **********     Github https://github.com/lmy8848
 **********     @author 麒玖网络QJ315 (NJQ-PRO)
 **********     @address     NJQ-PC
 **********     @time 2021/10/22 23:14
 */
public class MyIntentService extends Service {


    public static boolean isAlive = false;
    public static String test="标识";
    public static Socket socket;
    private AlarmClockReceiver receiver;
    private boolean isRegistered = false;
    private Intent intent1;
    private PendingIntent p;
    private AlarmManager alarm;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        super.onCreate();


        if (!IsConnect()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    connect();
                    MySQL_Utils mySQLUtils=new MySQL_Utils();
                    mySQLUtils.ConnectSQL();
                }
            }).start();
        }


        Calendar cal = Calendar.getInstance();
        intent1 = new Intent(this, MyIntentService.class);
        intent1.setAction("android.intent.action.TIME_TICK");

        cal.setTimeInMillis(System.currentTimeMillis());//获取当前系统时间
        //= PendingIntent.getService(this, 0, intent1, 0);
        p = PendingIntent.getService(this, 0, intent1, 0);
        alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // 每分钟启动一次。这个时间值视详细情况而定
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 60 * 1000, p);
        isAlive = true;
        Log.e("TAG", "onCreate: 活了");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(@Nullable @org.jetbrains.annotations.Nullable Intent intent, int flags, int startId) {

        if (!isRegistered) {
            receiver = new AlarmClockReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_TIME_TICK);
            intentFilter.addAction("android.intent.action.service.start");
            registerReceiver(receiver, intentFilter);
            Log.i("TAG", "onStartCommand:注册执行 ");
            isRegistered = true;
        }
        flags = START_STICKY;
        Log.i("TAG", "onStartCommand: 开始了");
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("TAG", "onDestroy: 死了");
        isAlive = false;
        Intent intent = new Intent();
        intent.setAction("android.intent.action.service.start");
        sendBroadcast(intent);
        unregisterReceiver(receiver);

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onTrimMemory(int level) {
//        onCreate();

        //保持运行的方法
    }

    @Override
    public void onStart(Intent intent, int startId) {

        super.onStart(intent, startId);
    }

/**
 **********     Github https://github.com/lmy8848
 **********     @author 麒玖网络QJ315 (NJQ-PRO)
 **********     @address     NJQ-PC
 **********     @time 2021/10/22 23:14
 */
    public void connect() {
        try {
            socket = new Socket("39.105.77.85", 5000);
            socket.setSoTimeout(10000);
            test="连接成功";

        } catch (IOException e) {
            test="连接失败";
            Log.i("TAG", "connect:连接失败 "+e.getMessage());
            e.printStackTrace();
        }

        if (socket != null) {
            if (socket.isConnected()){
                Log.i("TAG", "connect:连接成功！ ");
            }
            else {
                Log.i("TAG", "connect: 二次验证------连接失败！");
            }
        } else {
            Log.i("TAG", "connect:二次验证 异常为空！");
        }
    }

    public static boolean IsConnect() {
        if (socket != null) {
            Log.i("TAG", "不为空的IsConnect: "+socket.isConnected()+test);
            return socket.isConnected();
        } else {
            Log.i("TAG", "为空IsConnect: "+false+test);
            return false;
        }
    }
}
