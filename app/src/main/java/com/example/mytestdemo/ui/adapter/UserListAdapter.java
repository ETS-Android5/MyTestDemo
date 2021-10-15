package com.example.mytestdemo.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mytestdemo.R;
import com.example.mytestdemo.bean.User;

import java.util.List;

public class UserListAdapter extends BaseAdapter {

    private Context context;
    private List<User> userList;

    public UserListAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = userList.get(position);
        convertView= LayoutInflater.from(context).inflate(R.layout.item_data,null);
        TextView title=convertView.findViewById(R.id.title_tv);
        TextView msg=convertView.findViewById(R.id.msg_data);
        TextView feeduser=convertView.findViewById(R.id.phone);
        TextView feedbackdate=convertView.findViewById(R.id.date);
        msg.setTypeface(Typeface.create(msg.getTypeface(), Typeface.NORMAL), Typeface.BOLD);
        feeduser.setTypeface(Typeface.create(feeduser.getTypeface(), Typeface.NORMAL), Typeface.BOLD);
        feedbackdate.setTypeface(Typeface.create(feedbackdate.getTypeface(), Typeface.NORMAL), Typeface.BOLD);
        title.setVisibility(View.GONE);
        feeduser.setVisibility(View.GONE);
        msg.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        msg.setText(user.getUsername());
        feedbackdate.setText(user.getCreatedAt());
        return convertView;
    }
}
