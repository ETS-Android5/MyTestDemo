package com.example.mytestdemo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;

import com.example.mytestdemo.R;
import com.example.mytestdemo.lost.Lost;

import java.util.List;

public class AddAdapter extends BaseAdapter {
    Context context;
    List<Lost> losts;

    public AddAdapter(Context context, List<Lost> losts) {
        this.context = context;
        this.losts = losts;
    }

    @Override
    public int getCount() {
        return losts.size();
    }

    @Override
    public Object getItem(int position) {
        return losts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Lost lost= losts.get(position);
        String lostTitle = lost.getTitle();
        String lostDescribe = lost.getDescribe();
        String lostCreatedAt = lost.getCreatedAt();
        String lostPhone = lost.getPhone();
        convertView= LayoutInflater.from(context).inflate(R.layout.addadapter,null);
        TextView title=convertView.findViewById(R.id.title_tv);
        TextView describe=convertView.findViewById(R.id.msg_data);
        TextView date=convertView.findViewById(R.id.date);
        TextView phone=convertView.findViewById(R.id.phone);
        title.setText(lostTitle);
        describe.setText(lostDescribe);
        describe.setMaxLines(2);
        date.setText(lostCreatedAt);
        phone.setText("TEL:"+lostPhone);

        return convertView;
    }
}
