package com.qj315.mytestdemo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mytestdemo.R;
import com.qj315.mytestdemo.services.MyIntentService;

import java.util.Objects;

public class ChatAllActivity extends AppCompatActivity {

    private TextView mChatTitle,connectMsg;
    private ListView mListView;
    private EditText mChatInput;
    private Button mBtnSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_all);
        Objects.requireNonNull(getSupportActionBar()).hide();
        initView();
        Intent intent=getIntent();
        String title = intent.getStringExtra("title");
        mChatTitle.setText(title);
        try {
            if (MyIntentService.IsConnect()){
                connectMsg.setTextColor(Color.GREEN);
                connectMsg.setText("连接服务端成功");
            }else {
                connectMsg.setTextColor(Color.RED);

                connectMsg.setText("未连接，没有拿到变量值");
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            connectMsg.append(e.getMessage());
        }

    }

    private void initView() {
        mChatTitle = findViewById(R.id.chat_title);
        mListView = findViewById(R.id.list_view);
        mChatInput = findViewById(R.id.chat_input);
        mBtnSend = findViewById(R.id.btn_send);
        connectMsg=findViewById(R.id.connect_msg);
    }
}