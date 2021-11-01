package com.example.mytestdemo.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.mytestdemo.R;
import com.example.mytestdemo.bean.Song;

import java.util.List;

public class AddGetMusic extends BaseAdapter {
    Context context;
    List<Song> losts;

    public AddGetMusic(Context context, List<Song> losts) {
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
        Song lost= losts.get(position);
        String lostTitle = lost.getSong();
        String lostCreatedAt = lost.getSinger();
        String lostPhone = lost.getCreatedAt();
        convertView= LayoutInflater.from(context).inflate(R.layout.item_data,null);
        TextView title=convertView.findViewById(R.id.title_tv);
        TextView describe=convertView.findViewById(R.id.msg_data);
        TextView date=convertView.findViewById(R.id.date);
        TextView phone=convertView.findViewById(R.id.phone);
        title.setText(lostTitle);
        describe.setVisibility(View.GONE);
        date.setText(lostCreatedAt);
        phone.setText(lostPhone);

        return convertView;
    }
}
