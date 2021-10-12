package com.example.mytestdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.mytestdemo.fragment.AddListData;

public class AddDataMainActivity extends AppCompatActivity {

    private LinearLayout mAddDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data_main);
        getSupportActionBar().hide();
        initView();
        setView();

    }

    private void setView() {
        getSupportFragmentManager().beginTransaction().replace(R.id.add_data_list,new AddListData()).commit();

    }
    public void initView() {
        mAddDataList=(LinearLayout) findViewById(R.id.add_data_list);
    }
}