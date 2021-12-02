package com.qj315.QiJiu315.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.mytestdemo.R;
import com.qj315.QiJiu315.bean.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private TextInputEditText mUserName;
    private TextInputEditText mUserPassword;
    private TextInputEditText mUserPassword2;
    private Button mCreateYes;
    private TextView mCreateNo;
    private CoordinatorLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Bmob.initialize(RegisterActivity.this, "08f5717e435ccb57bd2b266c62b30563");
        setTitle("注册");
        Objects.requireNonNull(getSupportActionBar()).hide();
        initView();

        mCreateYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = Objects.requireNonNull(mUserName.getText()).toString();
                String password1 = Objects.requireNonNull(mUserPassword.getText()).toString();
                String password2 = Objects.requireNonNull(mUserPassword2.getText()).toString();
                if (username.equals("") || password1.equals("") || password2.equals("")) {
                    Toast.makeText(RegisterActivity.this, "输入不能为空！", Toast.LENGTH_SHORT).show();
                } else if (!password1.equals(password2)) {
                    Toast.makeText(RegisterActivity.this, "两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
                } else if (username.length() < 8 || username.length() > 16) {
                    Toast.makeText(RegisterActivity.this, "用户名必须为大于8位小于16的字符！", Toast.LENGTH_SHORT).show();
                } else if (password1.length() < 9 || password1.length() > 18) {
                    Toast.makeText(RegisterActivity.this, "密码为9-18为字符！", Toast.LENGTH_SHORT).show();
                } else if (Repeat(username)){
                    Log.i("TAG", "onClick: "+"用户名重复");
                }
                else {
                    signUp(coordinatorLayout, username, password1);
                }

            }
        });

        mCreateNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean Repeat(String username){
        final boolean[] repeat = new boolean[1];
        BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("username", username);
        categoryBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        Snackbar.make(coordinatorLayout, "已有用户使用此名字！" + object.size(), Snackbar.LENGTH_LONG).show();
                        repeat[0] =true;
                    }
                    repeat[0]=false;
                } else {
                    Log.e("BMOB", e.toString());
                    Snackbar.make(coordinatorLayout, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
        return repeat[0];
    }

    private void signUp(final View view, String unm, String upwd) {
        final User user = new User();
        user.setUsername(unm);
        user.setPassword(upwd);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    Snackbar.make(view, "注册成功", Snackbar.LENGTH_LONG).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
                } else {
                    Snackbar.make(view, "尚未失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    public void initView() {
        mUserName = findViewById(R.id.user_name);
        mUserPassword = findViewById(R.id.user_password);
        mUserPassword2 = findViewById(R.id.user_password2);
        mCreateYes = findViewById(R.id.create_yes);
        mCreateNo = findViewById(R.id.create_no);
        coordinatorLayout = findViewById(R.id.container);
    }
}