package com.example.mytestdemo.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mytestdemo.MainActivity;
import com.example.mytestdemo.R;
import com.example.mytestdemo.bean.User;
import com.example.mytestdemo.bean.Bean;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
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

public class LoginActivity extends AppCompatActivity {

    TextInputEditText username, password;
    Button login;
    TextView address, wz, tvzc;
    CheckBox savePwd;
    SharedPreferences sp;
    Boolean ischecked;
    private String in;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(LoginActivity.this, "08f5717e435ccb57bd2b266c62b30563");
        setTitle("登录");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //没有权限则申请权限
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        init();//*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
        View v = findViewById(R.id.snackbar_bar);
        v.getBackground().setAlpha(180);
        boolean checked = sp.getBoolean("checked", false);
        if (checked) {
            savePwd.setChecked(true);
            username.setText(sp.getString("username", ""));
            password.setText(sp.getString("password", ""));
            ischecked = true;
        } else {
            savePwd.setChecked(false);
            ischecked = false;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL("https://ip.cn/api/index?ip=&type=0");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                InputStreamReader inputStreamReader = null;
                try {
                    assert url != null;
                    inputStreamReader = new InputStreamReader(url.openStream());

                    Thread.sleep(2000);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
                if (inputStreamReader != null) {
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();

                    while (true) {

                        try {
                            if ((in = bufferedReader.readLine()) != null) {
                                // System.out.println(in);
                                stringBuilder.append(in);
                                Gson gson = new Gson();
                                Bean bean = gson.fromJson(stringBuilder.toString(), Bean.class);
                                address.setText(bean.getIp());
                                wz.setText(bean.getAddress());
                            } else {
                                in = bufferedReader.readLine();
                                bufferedReader.close();

                                inputStreamReader.close();
                                break;
                            }
                        } catch (IOException e) {
                            Toast.makeText(LoginActivity.this, "子线程异常" + e.getMessage() + "应用强制退出！", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                    // System.out.println(stringBuilder.toString());
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(LoginActivity.this, "IP网络异常", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        }).start();

        savePwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ischecked = isChecked;
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unm = username.getText().toString();
                String upwd = password.getText().toString();
                if (unm.equals("") || upwd.equals("")) {
                    Toast.makeText(LoginActivity.this, "输入不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    login(coordinatorLayout, unm, upwd);
                    if (ischecked) {
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("checked", ischecked);
                        editor.putString("username", unm);
                        editor.putString("password", upwd);
                        editor.apply();
                    } else {
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("checked", ischecked);
                        editor.apply();
                    }

                }
            }
        });

        tvzc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    private void login(final View view, String unm, String upwd) {
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

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("username", user.getUsername());
                            startActivity(intent);

                            finish();//*-*-*-*-*-*-*-*-*-*-*-
                        }
                    }, 1000);
                } else {
                    Snackbar.make(view, "登录失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void init() {
        username = findViewById(R.id.username_edt);
        password = findViewById(R.id.password_edt);
        address = findViewById(R.id.address);
        wz = findViewById(R.id.WZ);
        login = findViewById(R.id.button_login);
        tvzc = findViewById(R.id.tv_zc);
        savePwd = findViewById(R.id.save_pwd);
        coordinatorLayout = findViewById(R.id.container_login);
        sp = getSharedPreferences("savedata", MODE_PRIVATE);
    }

}