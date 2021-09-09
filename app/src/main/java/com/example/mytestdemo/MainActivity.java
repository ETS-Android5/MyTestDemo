package com.example.mytestdemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Date;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class MainActivity extends AppCompatActivity {
    EditText phonenumber;
    Button gotobtn, exitbtn;
    TextView servertime;
    Long date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phonenumber = findViewById(R.id.phone_number_edt);
        gotobtn = findViewById(R.id.goto_btn);
        exitbtn = findViewById(R.id.exit_btn);
        servertime = findViewById(R.id.server_time);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        if (!username.equals("QJ315")) {
            phonenumber.setFocusable(false);
            phonenumber.setFocusableInTouchMode(false);
            phonenumber.setEnabled(false);
            gotobtn.setText("已停用");
            gotobtn.setEnabled(false);
        }

        setTitle("验证码测试");
        Bmob.initialize(MainActivity.this, "08f5717e435ccb57bd2b266c62b30563");
        Bmob.getServerTime(new QueryListener<Long>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("SetTextI18n")
            @Override
            public void done(Long time, BmobException e) {
                if (e == null) {
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    date = time + 8 * 60 * 60;//服务器时间不稳定 有时差八小时可能是时区问题
                    String times = formatter.format(new Date(date * 1000L));
                    Log.i("bmob", "当前服务器时间为:" + times + "time=" + time);
                    servertime.setText("当前服务器时间为:" + times);
                } else {
                    Log.i("bmob", "获取服务器时间失败:" + e.getMessage());
                    servertime.setText("获取服务器时间失败:" + e.getMessage());
                }
            }

        });
        gotobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = phonenumber.getText().toString();

                if (!s.equals("") && s.length() == 11) {
                    BmobSMS.requestSMSCode(s, "QJ315", new QueryListener<Integer>() {
                        @Override
                        public void done(Integer smsId, BmobException e) {
                            if (e == null) {

                                Toast.makeText(MainActivity.this, "发送验证码成功，短信ID：" + smsId, Toast.LENGTH_LONG).show();
                                phonenumber.setText("");
                            } else {

                                Toast.makeText(MainActivity.this, "发送验证码失败：" + e.getErrorCode() + "-" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "输入不能为空，且为11位数字！", Toast.LENGTH_SHORT).show();
                }


            }
        });

        exitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}