package com.example.mytestdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginRegister extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private EditText mUserName;
    private EditText mUserPassword;
    private EditText mUserPassword2;
    private Button mCreateYes;
    private TextView mCreateNo;
    private CoordinatorLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        Bmob.initialize(LoginRegister.this, "08f5717e435ccb57bd2b266c62b30563");
        setTitle("注册");
        initView();

        mCreateYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUserName.getText().toString();
                String password1 = mUserPassword.getText().toString();
                String password2 = mUserPassword2.getText().toString();
                if (username.equals("")||password1.equals("")||password2.equals("")){
                    Toast.makeText(LoginRegister.this, "输入不能为空！", Toast.LENGTH_SHORT).show();
                }else if (!password1.equals(password2)){
                    Toast.makeText(LoginRegister.this, "两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
                }else if(username.length()<8||username.length()>16){
                    Toast.makeText(LoginRegister.this, "用户名必须为大于8位小于16的字符！", Toast.LENGTH_SHORT).show();
                }else if (password1.length()<9||password1.length()>18){
                    Toast.makeText(LoginRegister.this, "密码为9-18为字符！", Toast.LENGTH_SHORT).show();
                }
                else {
                signUp(coordinatorLayout,username,password1);
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

    private void signUp(final View view,String unm,String upwd) {
        final User user = new User();
        user.setUsername(unm);
        user.setPassword(upwd);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    Snackbar.make(view, "注册成功", Snackbar.LENGTH_LONG).show();
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },1000);
                }
                else {
                    Snackbar.make(view, "尚未失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    public void initView() {
        mUserName=findViewById(R.id.user_name);
        mUserPassword=findViewById(R.id.user_password);
        mUserPassword2=findViewById(R.id.user_password2);
        mCreateYes=findViewById(R.id.create_yes);
        mCreateNo=findViewById(R.id.create_no);
        coordinatorLayout=findViewById(R.id.container);
    }
}