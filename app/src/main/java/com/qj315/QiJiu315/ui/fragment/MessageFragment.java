package com.qj315.QiJiu315.ui.fragment;

import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.mytestdemo.R;
import com.qj315.QiJiu315.bean.UserList;
import com.qj315.QiJiu315.receiver.MyReceiver;
import com.qj315.QiJiu315.ui.adapter.AddAdministrator;
import com.qj315.QiJiu315.ui.dialog.SetProgressBar;
import com.qj315.QiJiu315.ui.dialog.UserListView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;


public class MessageFragment extends Fragment implements MyReceiver.Message {
    View view;
    private Button mAddAdministrator;
    private ListView feedback;
    private List<UserList> feedlist;
    private SetProgressBar progressBar;
    private MyReceiver myReceiver;
    private Boolean getAction=false;
    private TabLayout tab_layout;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);

        initView();
        UserList();
        mAddAdministrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserListView list = new UserListView(requireActivity());
                list.setCancelable(true);
                list.setTitle("用户列表");
                list.show();
            }
        });

        feedback.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                UserList userList = feedlist.get(position);
                String objectId = userList.getObjectId();
                AlertDialog dialog = new AlertDialog.Builder(requireActivity()).setTitle("提示").setMessage("您确定要删除此条留言吗？").setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserList listed = new UserList();
                        listed.setObjectId(objectId);
                        listed.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(getContext(), "删除成功！", Toast.LENGTH_SHORT).show();
                                    UserList();
                                } else {
                                    Toast.makeText(getContext(), "删除失败！" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).create();
                adminUser(dialog);
                return true;
            }
        });


        return view;
    }


    private void initView() {
        mAddAdministrator = view.findViewById(R.id.add_administrator);
        feedback = view.findViewById(R.id.user_listview);
//        tab_layout = view.findViewById(R.id.tab_layout);
        progressBar = new SetProgressBar(requireActivity());
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.nangch.broadcasereceiver.MYRECEIVER");
        requireActivity().registerReceiver(myReceiver, intentFilter);
        myReceiver.setMessage(this);

    }

    public void UserList() {
        BmobQuery<UserList> query = new BmobQuery<>();
        query.order("-createdAt");
        query.findObjects(new FindListener<UserList>() {
            @Override
            public void done(List<UserList> list, BmobException e) {
                if (e == null) {
                    feedlist = list;
                    feedback.setAdapter(new AddAdministrator(getActivity(), list));
                    feedback.deferNotifyDataSetChanged();

                } else {
                    Toast.makeText(getContext(), "数据加载失败！", Toast.LENGTH_SHORT).show();
                }

                progressBar.dismiss();
            }
        });
    }

    public void adminUser(AlertDialog dialog) {
        String username = BmobUser.getCurrentUser().getUsername();
        if (username.contentEquals("QJ315")) {
            dialog.show();
        }

    }

    @Override
    public void onResume() {
         getAction=true;
        progressBar.show();
        super.onResume();
        try {
            UserList();
        } catch (Exception e) {
            e.printStackTrace();
            progressBar.dismiss();
        }
    }

    @Override
    public void onPause() {
        Log.i("TAG", "onPause: 暂停");
        super.onPause();
    }

    @Override
    public void onStart() {
        Log.i("TAG", "onStart: 启动");
        super.onStart();
    }

    @Override
    public void onStop() {
        getAction=false;
        Log.i("TAG", "onStop: 停止");
        super.onStop();
    }

    @Override
    public void getMsg(String str) {
        Log.i("TAG", "getMsg: "+getAction);
        if (getAction) {
//        Toast.makeText(requireActivity(), ""+str, Toast.LENGTH_SHORT).show();
            progressBar.show();
            super.onResume();
            try {
                UserList();
                Log.i("TAG", "getMsg:收到处理数据 ");

            } catch (Exception e) {
                e.printStackTrace();
                progressBar.dismiss();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unregisterReceiver(myReceiver);
    }
}
