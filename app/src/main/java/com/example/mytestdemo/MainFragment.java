package com.example.mytestdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.mytestdemo.fragment.AddListData;
import com.example.mytestdemo.fragment.FragmentList;

public class MainFragment extends AppCompatActivity {
    FrameLayout fragment;
    private long exitTime = 0;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);
        initView();
        setView1();
      Intent userdata =getIntent();
         name = userdata.getStringExtra("username");

    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            //彻底关闭整个APP
            int currentVersion = android.os.Build.VERSION.SDK_INT;
            if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                System.exit(0);
            } else {// android2.1
                ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                am.restartPackage(getPackageName());
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
                break;
            case R.id.help:
                Toast.makeText(this, "帮助", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add:
                Toast.makeText(this, "添加信息", Toast.LENGTH_SHORT).show();
                initView();
                setView2();
                break;
            case R.id.sms_for_phone:
                Toast.makeText(this, "验证码测试！", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainFragment.this,MainActivity.class);
                intent.putExtra("username",name);
                startActivity(intent);
                break;
            case R.id.logon:
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainFragment.this,Login.class));
                        finish();
                    }
                },1000);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void setView1() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main,new FragmentList()).commit();

    }

    private void setView2() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main,new AddListData()).commit();

    }

    private void  initView(){
         fragment = (FrameLayout) findViewById(R.id.fragment_list);
    }
}