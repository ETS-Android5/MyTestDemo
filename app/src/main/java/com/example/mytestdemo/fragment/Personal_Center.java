package com.example.mytestdemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mytestdemo.R;

import org.jetbrains.annotations.NotNull;

public class Personal_Center extends Fragment {

    View view;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.personal_center,container,false);
        return view;
    }
}
