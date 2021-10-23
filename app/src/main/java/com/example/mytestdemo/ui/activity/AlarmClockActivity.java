package com.example.mytestdemo.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.util.Calendar;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mytestdemo.R;
import com.example.mytestdemo.receiver.AlarmClockReceiver;


public class AlarmClockActivity extends AppCompatActivity implements View.OnClickListener {

    private AlarmManager am;
    private PendingIntent pi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);

        Intent intentmedia=getIntent();
        String media = intentmedia.getStringExtra("media");
        Log.i("TAG", "onCreate:*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-- ");
        if (media!=null){
        if (media.equals("mediaPlayer")){
        AlertDialog builder = new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("起床了您设置默认八点二十的闹钟，已经到了该起床了，Get up！！！")
                .setPositiveButton("好的明白", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlarmClockReceiver.mediaPlayer.stop();
                        dialog.dismiss();
                    }
                }).create();
        builder.show();
        }
        }
        am = (AlarmManager) getSystemService(ALARM_SERVICE);

        //实例化Intent
        Intent intent = new Intent(AlarmClockActivity.this, AlarmClockReceiver.class);

        AlarmClockReceiver receiver = new AlarmClockReceiver();

        //设置Intent action属性
        intent.setAction("android.intent.action.ACTION_TIME_TICK");
        intent.putExtra("msg", "这是闹钟");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction("android.intent.action.ACTION_TIME_TICK");
        registerReceiver(receiver, intentFilter);

        //实例化PendingIntent
        pi = PendingIntent.getBroadcast(AlarmClockActivity.this, 0, intent, 0);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_alarm1:
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTimeInMillis(System.currentTimeMillis());//获取当前系统时间
                calendar1.set(Calendar.SECOND, 10);
                calendar1.add(Calendar.SECOND, 10);//设置10秒的间隔
//                am.setExact(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), pi);
                am.set(AlarmManager.RTC, calendar1.getTimeInMillis(), pi);//10秒后启动闹钟
                Log.i("TAG", "onClick:10秒间隔 ");
                break;
            case R.id.btn_alarm2:
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTimeInMillis(System.currentTimeMillis());//获取当前系统时间
                am.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), 1000 * 60, pi);//每60秒启动一次闹钟
                Log.i("TAG", "onClick:一分钟秒间隔重复 ");
                break;
            case R.id.btn_alarm3:
                Log.i("TAG", "onClick:自定义 ");
                Calendar calendar3 = Calendar.getInstance();
                //设置一个23:30的闹钟
                calendar3.set(Calendar.HOUR_OF_DAY, 24);
                calendar3.set(Calendar.MINUTE, 0);
                calendar3.set(Calendar.SECOND, 0);
                calendar3.set(Calendar.MILLISECOND, 0);
                am.set(AlarmManager.RTC, calendar3.getTimeInMillis(), pi);

                break;
            default:
                break;
        }
    }
}