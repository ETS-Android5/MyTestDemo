package com.qj315.QiJiu315.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.mytestdemo.R;
import com.qj315.QiJiu315.MainActivity;
import com.qj315.QiJiu315.ui.dialog.DialogUpdateV;
import com.qj315.QiJiu315.ui.dialog.UpdateTest;
import com.qj315.QiJiu315.utils.PlayerMusic;

import cn.bmob.v3.BmobUser;

public class SettingActivity extends AppCompatActivity {

    RadioButton btn_back;
    private Button mLoginAgain;
    private Button mUpdateG;
    private Button mUpdateVersion;
    private Button mInspect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().hide();
        initView();


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLoginAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    if (PlayerMusic.IsPlayed()) {
                        PlayerMusic.Reset();
                    }
                    startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                    BmobUser.logOut();
                    finish();
                }, 1000);
            }
        });
        mUpdateG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BmobUser.getCurrentUser().getUsername().equals("QJ315")) {
                    UpdateTest updateTest = new UpdateTest(SettingActivity.this);
                    updateTest.create();
                    updateTest.show();
                } else {
                    Toast.makeText(SettingActivity.this, "系统默认管理员，您不是有效管理！", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mUpdateVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BmobUser.getCurrentUser().getUsername().equals("QJ315")) {
                    DialogUpdateV updateV = new DialogUpdateV(SettingActivity.this);
                    updateV.setCancelable(true);
                    updateV.show();
                } else {
                    Toast.makeText(SettingActivity.this, "系统默认管理员，您不是有效管理！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mInspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    private void initView(){
        btn_back=findViewById(R.id.setting_back);
        mLoginAgain = findViewById(R.id.login_again);
        mUpdateG = findViewById(R.id.update_G);
        mUpdateVersion = findViewById(R.id.update_version);
       mInspect = findViewById(R.id.Inspect);
    }
}