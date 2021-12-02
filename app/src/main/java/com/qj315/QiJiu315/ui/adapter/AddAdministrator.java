package com.qj315.QiJiu315.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mytestdemo.R;
import com.qj315.QiJiu315.bean.UserList;

import java.util.List;

public class AddAdministrator extends BaseAdapter {
    Context context;
    List<UserList> feedback;

    public AddAdministrator(Context context, List<UserList> feedback) {
        this.context = context;
        this.feedback = feedback;
    }

    @Override
    public int getCount() {

        return feedback.size();
    }

    @Override
    public Object getItem(int position) {
        return feedback.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserList userList=feedback.get(position);
        convertView= LayoutInflater.from(context).inflate(R.layout.item_data,null);
        TextView title=convertView.findViewById(R.id.title_tv);
        TextView msg=convertView.findViewById(R.id.msg_data);
        TextView feedbackuser=convertView.findViewById(R.id.phone);
        TextView feedbackdate=convertView.findViewById(R.id.date);
        msg.setTypeface(Typeface.create(msg.getTypeface(), Typeface.NORMAL), Typeface.BOLD);
        feedbackuser.setTypeface(Typeface.create(feedbackuser.getTypeface(), Typeface.NORMAL), Typeface.BOLD);
        feedbackdate.setTypeface(Typeface.create(feedbackdate.getTypeface(), Typeface.NORMAL), Typeface.BOLD);
        title.setVisibility(View.GONE);
        msg.setTextColor(Color.rgb(15,247,239));
        msg.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

        msg.setText(userList.getFeedbackmsg());
        feedbackdate.setText("时间:"+userList.getCreatedAt());
        feedbackuser.setText("用户:"+userList.getFeedbackuser());
        return convertView;
    }
}
