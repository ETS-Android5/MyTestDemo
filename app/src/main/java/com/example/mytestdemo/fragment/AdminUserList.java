package com.example.mytestdemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mytestdemo.R;
import com.example.mytestdemo.dialog.UserListView;


public class AdminUserList extends Fragment {
    View view;
    private Button mAddAdministrator;
    private ListView mUserListview;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.admin_user,container,false);
        ListView user_list=view.findViewById(R.id.user_listview);
        initView();
        mAddAdministrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserListView list=new UserListView(requireActivity());
                list.setCancelable(true);
                list.show();
            }
        });


        return view;
    }

    private void initView() {
        mAddAdministrator = view.findViewById(R.id.add_administrator);
        mUserListview = view.findViewById(R.id.user_listview);
    }
}
