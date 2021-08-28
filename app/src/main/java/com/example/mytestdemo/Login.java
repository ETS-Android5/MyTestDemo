package com.example.mytestdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Login extends AppCompatActivity {

    EditText username,password;
    Button login,btnzc;
    TextView address,wz;
    private String in;
    private CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(Login.this, "08f5717e435ccb57bd2b266c62b30563");
        setTitle("登录");
        init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url=null;
                try {
                    url = new URL("https://ip.cn/api/index?ip=&type=0");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                InputStreamReader inputStreamReader= null;
                try {
                    assert url != null;
                    inputStreamReader = new InputStreamReader(url.openStream());
                    Thread.sleep(3000);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while (true){

                    try {
                        if ((in=bufferedReader.readLine())!=null){
                            // System.out.println(in);
                            stringBuilder.append(in);
                        }
                        else {
                            in=bufferedReader.readLine();
                            bufferedReader.close();

                            inputStreamReader.close();
                            break;
                        }
                    } catch (IOException e) {
                        Toast.makeText(Login.this, "子线程异常"+e.getMessage()+"应用强制退出！", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                // System.out.println(stringBuilder.toString());
                Gson gson = new Gson();
                Bean bean = gson.fromJson(stringBuilder.toString(), Bean.class);
                address.setText(bean.getIp());
                wz.setText(bean.getAddress());


            }
        }).start();
       login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String unm = username.getText().toString();
               String upwd = password.getText().toString();
               if (unm.equals("")||upwd.equals("")){
                   Toast.makeText(Login.this, "输入不能为空！", Toast.LENGTH_SHORT).show();
               }
               else {
                   login(coordinatorLayout,unm,upwd);
               }
           }
       });

       btnzc.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(Login.this,LoginRegister.class));
           }
       });

    }
    private void login(final View view,String unm,String upwd) {
        final User user = new User();
        user.setUsername(unm);
        user.setPassword(upwd);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User bmobUser, BmobException e) {
                if (e == null) {
                    User user = BmobUser.getCurrentUser(User.class);
                    Snackbar.make(view, "登录成功：" + user.getUsername(), Snackbar.LENGTH_LONG).show();

                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent=new Intent(Login.this,MainFragment.class);
                            intent.putExtra("username",user.getUsername());
                            startActivity(intent);

                            finish();//*-*-*-*-*-*-*-*-*-*-*-
                        }
                    },1000);
                } else {
                    Snackbar.make(view, "登录失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
    private void init(){
        username=findViewById(R.id.username_edt);
        password=findViewById(R.id.password_edt);
        address=findViewById(R.id.address);
        wz = findViewById(R.id.WZ);
        login=findViewById(R.id.button_login);
        btnzc=findViewById(R.id.button_zc);
        coordinatorLayout=findViewById(R.id.container_login);
    }
}