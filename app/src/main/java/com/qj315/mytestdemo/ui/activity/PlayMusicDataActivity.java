package com.qj315.mytestdemo.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.qj315.mytestdemo.MainActivity;
import com.example.mytestdemo.R;

public class PlayMusicDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music_data);
        setTitle("播放器示例待开发中……");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("返回").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ("返回".contentEquals(item.getTitle())){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}