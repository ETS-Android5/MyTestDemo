package com.example.mytestdemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mytestdemo.R;
import com.google.android.material.snackbar.Snackbar;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class UpdatePasswordActivity extends AppCompatActivity {

    private EditText mUserP1;
    private EditText mUserP2;
    private Button mOkUpdate;

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        setTitle("修改密码");
        initView();

        mOkUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String up1 = mUserP1.getText().toString();
                String up2 = mUserP2.getText().toString();
                if (!up1.equals(up2)) {
                    //TODO 此处替换为你的旧密码和新密码
                    BmobUser.updateCurrentUserPassword(up1, up2, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Snackbar.make(view, "密码修改成功，请重新登录！", Snackbar.LENGTH_LONG).show();
//                            Toast.makeText(UpdatePasswordActivity.this, "成功", Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        BmobUser.logOut();
                                        String username = BmobUser.getCurrentUser().getUsername();
                                        startActivity(new Intent(UpdatePasswordActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                }, 2000);
                            } else {
                                Snackbar.make(view, "修改失败！" + e.getMessage(), Snackbar.LENGTH_LONG).show();
//                            Toast.makeText(UpdatePasswordActivity.this, "查询失败："+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else if (up2.isEmpty()||up1.isEmpty()){
                    Toast.makeText(UpdatePasswordActivity.this, "原始密码和新密码输入不能为空！", Toast.LENGTH_SHORT).show();
                }
                else {
                    Snackbar.make(view, "原始密码不能与新密码重复！", Snackbar.LENGTH_LONG).show();
                }
            }
        });


//        BmobUser.logOut();
    }

    private void initView() {

        mUserP1 = findViewById(R.id.user_p1);
        mUserP2 = findViewById(R.id.user_p2);
        mOkUpdate = findViewById(R.id.ok_update);
        view = findViewById(R.id.sncackbar_update);
    }
}