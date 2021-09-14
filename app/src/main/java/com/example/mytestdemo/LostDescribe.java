package com.example.mytestdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mytestdemo.fragment.FragmentList;

public class LostDescribe extends AppCompatActivity {

    private TextView mTitleDescribe;
    private TextView mMsgDataDescribe;
    private TextView mDateDescribe;
    private TextView mPhoneDescribe;
    private Button callupp, gotophone;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_describe);

        initView();
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String describe = intent.getStringExtra("describe");
        String date = intent.getStringExtra("date");
        String phone = intent.getStringExtra("phone");
        mTitleDescribe.setText(title);
        mMsgDataDescribe.setText(describe);
        mMsgDataDescribe.setMovementMethod(ScrollingMovementMethod.getInstance());//文字滚动
        mDateDescribe.setText(date);
        mPhoneDescribe.setText("TEL:" + phone);
        callupp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(LostDescribe.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LostDescribe.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri data = Uri.parse("tel:" + phone);
                        intent.setData(data);
                        startActivity(intent);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                        Toast.makeText(LostDescribe.this, "异常错误:" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    finish();
                    }
                }
            }
        });
        gotophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        mTitleDescribe = findViewById(R.id.title_describe);
        mMsgDataDescribe = findViewById(R.id.msg_data_describe);
        mDateDescribe = findViewById(R.id.date_describe);
        mPhoneDescribe = findViewById(R.id.phone_describe);
        callupp = findViewById(R.id.call_up);
        gotophone = findViewById(R.id.goto_phone);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("返回").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ("返回".contentEquals(item.getTitle())){
            finish();
            startActivity(new Intent(LostDescribe.this, MainFragment.class));
        }
        return super.onOptionsItemSelected(item);
    }
}