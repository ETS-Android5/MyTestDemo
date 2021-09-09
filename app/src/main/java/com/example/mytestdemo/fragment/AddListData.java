package com.example.mytestdemo.fragment;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mytestdemo.R;
import com.example.mytestdemo.lost.Lost;

import java.sql.Date;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;


public class AddListData extends Fragment {
    View view;
    private EditText mTitleTv;
    private EditText mPhoneNumber;
    private EditText mMessageData;
    private ImageButton mReleaseBtn;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.addlist, container, false);
        initView();
        setHasOptionsMenu(true);
        requireActivity().setTitle("添加信息");
        Bmob.initialize(getContext(), "08f5717e435ccb57bd2b266c62b30563");

        mReleaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String describe = mMessageData.getText().toString();
                String phone = mPhoneNumber.getText().toString();
                String title = mTitleTv.getText().toString();
                if(describe.isEmpty()||phone.isEmpty()||title.isEmpty()){
                    Toast.makeText(requireActivity(), "必填项不能为空！", Toast.LENGTH_SHORT).show();
                }else {
                Lost lost = new Lost();
                lost.setDescribe(describe);
                lost.setPhone(phone);
                lost.setTitle(title);
                lost.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            FragmentManager fm = requireActivity().getSupportFragmentManager();
                            Fragment fragment = new FragmentList();
                            fm.beginTransaction().replace(R.id.fragment_list, fragment).commit();
                            Toast.makeText(requireActivity(), "失物信息添加成功!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireActivity(), "添加失败:" + e.getMessage() + "|", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                }


            }
        });
        return view;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if ("返回".contentEquals(item.getTitle())) {
            FragmentManager fm = requireActivity().getSupportFragmentManager();
            Fragment fragment = new FragmentList();
            fm.beginTransaction().replace(R.id.fragment_list, fragment).commit();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();//清除原有菜单选项
        menu.add("返回").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

    }


    private void initView() {
        mTitleTv = view.findViewById(R.id.title_tv);
        mPhoneNumber = view.findViewById(R.id.phone_number);
        mMessageData = view.findViewById(R.id.message_data);
        mReleaseBtn = view.findViewById(R.id.release_btn);
        mMessageData.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);//多行模式
        mMessageData.setSingleLine(false);//是否单行模式
        mMessageData.setHorizontallyScrolling(false);//是否水平滚动
    }
}
