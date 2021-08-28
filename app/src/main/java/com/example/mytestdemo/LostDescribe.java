package com.example.mytestdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LostDescribe extends AppCompatActivity {

    private TextView mTitleDescribe;
    private TextView mMsgDataDescribe;
    private TextView mDateDescribe;
    private TextView mPhoneDescribe;
    private Button callupp,gotophone;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_describe);
        initView();
        Intent intent=getIntent();
        String title = intent.getStringExtra("title");
        String describe = intent.getStringExtra("describe");
        String date = intent.getStringExtra("date");
        String phone = intent.getStringExtra("phone");
        mTitleDescribe.setText(title);
        mMsgDataDescribe.setText(describe);
        mMsgDataDescribe.setMovementMethod(ScrollingMovementMethod.getInstance());//文字滚动
        mDateDescribe.setText(date);
        mPhoneDescribe.setText("TEL:"+phone);
        callupp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                startActivity(intent);
                }catch (SecurityException e){
                    e.printStackTrace();
                    Toast.makeText(LostDescribe.this, "异常错误:"+e.getMessage(), Toast.LENGTH_SHORT).show();
//                    finish();
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
        mDateDescribe= findViewById(R.id.date_describe);
        mPhoneDescribe= findViewById(R.id.phone_describe);
        callupp=findViewById(R.id.call_up);
        gotophone=findViewById(R.id.goto_phone);
    }
}