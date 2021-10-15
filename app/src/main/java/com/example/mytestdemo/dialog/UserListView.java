package com.example.mytestdemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mytestdemo.R;
import com.example.mytestdemo.User;
import com.example.mytestdemo.adapter.UserListAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class UserListView extends Dialog {
    List<User> usersList;
    private ListView mUserList;

    public UserListView(@NonNull @NotNull Context context) {
        super(context);
        setContentView(R.layout.userlist);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initView();
        new Thread(new Runnable() {
            @Override
            public void run() {

                BmobQuery<User> bmobQuery = new BmobQuery<User>();
                bmobQuery.order("-createdAt");
                bmobQuery.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> object, BmobException e) {
                        if (e == null) {
                            usersList = object;
                            mUserList.setAdapter(new UserListAdapter(getContext(),object));
                            Toast.makeText(getContext(), "" + object.size(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }
                });
            }
        }).start();
        super.onCreate(savedInstanceState);
    }

    public void initView() {
        mUserList = findViewById(R.id.user_list);
    }
}
