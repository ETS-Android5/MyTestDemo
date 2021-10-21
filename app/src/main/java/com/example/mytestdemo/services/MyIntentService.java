package com.example.mytestdemo.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.mytestdemo.receiver.AlarmClockReceiver;
import com.example.mytestdemo.ui.activity.AlarmClockActivity;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends Service {


    private AlarmClockReceiver receiver;
    public static boolean isAlive  = false;
    private  boolean isRegistered  = false;
    private Intent intent1;
    private PendingIntent p;
    private AlarmManager alarm;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        super.onCreate();
        Calendar cal = Calendar.getInstance();
        intent1 = new Intent(this,MyIntentService.class);
        intent1.setAction("android.intent.action.TIME_TICK");

        cal.setTimeInMillis(System.currentTimeMillis());//获取当前系统时间
         //= PendingIntent.getService(this, 0, intent1, 0);
        p = PendingIntent.getService(this, 0, intent1, 0);
        alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        // 每分钟启动一次。这个时间值视详细情况而定
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 60*1000, p);
        isAlive = true;
        Log.e("TAG", "onCreate: 活了");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(@Nullable @org.jetbrains.annotations.Nullable Intent intent, int flags, int startId) {

        if (!isRegistered){
            receiver = new AlarmClockReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_TIME_TICK);
            intentFilter.addAction("android.intent.action.service.start");
            registerReceiver(receiver,intentFilter);
            Log.i("TAG", "onStartCommand:注册执行 ");
            isRegistered= true;
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
        onCreate();

        //保持运行的方法
    }

    @Override
    public void onStart(Intent intent, int startId) {

        super.onStart(intent, startId);
    }
}