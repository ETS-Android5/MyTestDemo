package com.example.mytestdemo.receiver;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.mytestdemo.services.MyIntentService;
import com.example.mytestdemo.ui.activity.AlarmClockActivity;

public class AlarmClockReceiver extends BroadcastReceiver {

    public static MediaPlayer mediaPlayer;

    /*https://files.lumingyuan6868.xyz/2021/10/22/18a6157340d4c0858075eac00fefa6ba.mp3*/
    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.i("TAG", "onReceive: ");
//        String msg = intent.getStringExtra("msg");
//        Log.i("闹钟内容：TAG", "I can live forever");
        Toast.makeText(context, "程序后台运行中,将持续接受消息！", Toast.LENGTH_SHORT).show();
        if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
            Log.i("闹钟内容：TAG", "系统时间广播");
        }
//        if (Intent.ACTION_PACKAGE_RESTARTED.equals(intent.getAction())){
//            if (!MyIntentService.isAlive){
//                context.startService(new Intent(context, MyIntentService.class));
//            }
//        }
        if ("android.intent.action.service.start".equals(intent.getAction())) {
            Log.i("TAG", "onReceive:要活了");
            context.startService(new Intent(context, MyIntentService.class));
        } else if ("android.intent.action.ACTION_TIME_TICK".equals(intent.getAction())) {
            Log.i("TAG", "onReceive: 定时任务");
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource("https://files.lumingyuan6868.xyz/2021/10/22/18a6157340d4c0858075eac00fefa6ba.mp3");
                mediaPlayer.prepare();
                mediaPlayer.start();
                AlertDialog builder = new AlertDialog.Builder(context).setTitle("提示")
                        .setMessage("起床了您设置默认八点二十的闹钟，已经到了该起床了，Get up！！！")
                        .setPositiveButton("好的明白", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mediaPlayer.stop();
                                dialog.dismiss();
                            }
                        }).create();

//                if (builder.getWindow() != null) {
//                    int type;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        type = WindowManager.LayoutParams.TYPE_TOAST;
//                    } else {
//                        type = WindowManager.LayoutParams.TYPE_PHONE;
//                    }
//                    builder.getWindow().setType(type);
//                }
//                builder.show();
                builder.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                builder.show();


            } catch (Exception e) {
                e.printStackTrace();
                Log.i("TAG", "onReceive: ERROR" + e.getMessage());
            }
        }

        Intent intentnews = new Intent(context, AlarmClockActivity.class);
        intent.putExtra("media", "mediaPlayer");
        intentnews.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentnews);
    }

}