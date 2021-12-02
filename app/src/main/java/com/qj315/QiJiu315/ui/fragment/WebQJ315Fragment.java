package com.qj315.QiJiu315.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.mytestdemo.R;

/**
 **********     Github https://github.com/lmy8848
 **********     @author 麒玖网络QJ315 (NJQ-PRO)
 **********     @address     NJQ-PC
 **********     @time 2021/11/20 23:47
 */
public class WebQJ315Fragment extends Fragment {

    View view;
    private WebView mWebQj315;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_web_q_j315, container, false);
        initView();
        mWebQj315.setWebViewClient(new WebViewClient());
        mWebQj315.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebQj315.getSettings().setJavaScriptEnabled(true);
        mWebQj315.getSettings().setDomStorageEnabled(true);
        mWebQj315.loadUrl("http://www.lumingyuan6868.xyz/");
        return view;
    }

    public void initView() {
        mWebQj315=view.findViewById(R.id.web_qj315);
    }
}