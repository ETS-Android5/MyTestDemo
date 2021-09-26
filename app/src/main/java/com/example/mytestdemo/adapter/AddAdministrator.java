package com.example.mytestdemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.mytestdemo.userlist.UserList;

import java.util.List;

public class AddAdministrator extends BaseAdapter {
    Context context;
    List<UserList> adduserlists;

    public AddAdministrator(Context context, List<UserList> adduserlists) {
        this.context = context;
        this.adduserlists = adduserlists;
    }

    @Override
    public int getCount() {

        return adduserlists.size();
    }

    @Override
    public Object getItem(int position) {
        return adduserlists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
