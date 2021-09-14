package com.example.mytestdemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mytestdemo.R;


public class AdminUserList extends Fragment {
    View view;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.admin_user,container,false);
        ListView user_list=view.findViewById(R.id.user_listview);



        return view;
    }

}
