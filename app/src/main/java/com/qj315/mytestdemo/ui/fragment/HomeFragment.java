package com.qj315.mytestdemo.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.qj315.mytestdemo.ui.activity.DataDetailsActivity;
import com.example.mytestdemo.R;
import com.qj315.mytestdemo.ui.adapter.AddAdapter;
import com.qj315.mytestdemo.bean.Messages;
import com.qj315.mytestdemo.ui.dialog.SetProgressBar;
import com.qj315.mytestdemo.bean.Lost;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 **********     Github https://github.com/lmy8848
 **********     @author 麒玖网络QJ315 (NJQ-PRO)
 **********     @address     NJQ-PC
 **********     @time 2021/10/29 21:10
 */
public class HomeFragment extends Fragment {
    View view;
    ListView listView;
    List<Lost> losts = new ArrayList<>();
    TextView textViewtop;
    private boolean isLoad = true;
    private SetProgressBar progressBar;
    private long prevTime = 0;
    private long currTime = 0;

    @SuppressLint("SetTextI18n")
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        textViewtop = view.findViewById(R.id.text1);
        requireActivity().setTitle("首页");
//        textViewtop.setMovementMethod(new ScrollingMovementMethod());
        progressBar = new SetProgressBar(requireActivity());
        BmobQuery<Messages> bmobQuery = new BmobQuery<Messages>();
        bmobQuery.getObject("Hnyl4449", new QueryListener<Messages>() {
            @Override
            public void done(Messages object, BmobException e) {
                if (e == null) {
                    textViewtop.setText(object.getNotice());
                } else {
                    Toast.makeText(getActivity(), "初始化公告失败", Toast.LENGTH_SHORT).show();
                    textViewtop.setText("一条评论只能属于某一篇帖子，一篇帖子可以有很多用户对其进行评论，那么帖子和评论之间的关系就是一对多关系，推荐使用pointer类型来表示。");
                }
            }
        });
        listView = view.findViewById(R.id.list_view);
//        listView.getBackground().setAlpha(150);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Lost lost = losts.get(position);

                        Intent intent = new Intent(getActivity(), DataDetailsActivity.class);
                        intent.putExtra("title", lost.getTitle());
                        intent.putExtra("describe", lost.getDescribe());
                        intent.putExtra("date", lost.getCreatedAt());
                        intent.putExtra("phone", lost.getPhone());
                        intent.putExtra("photo", lost.getDate());//图片
                        startActivity(intent);
                    }
                });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Lost lost = losts.get(position);
                String objectId = lost.getObjectId();
                String photos = lost.getDate();


                AlertDialog dialogueDel = new AlertDialog.Builder(getContext())
                        .setTitle("提示")
                        .setMessage("确认删除此条数据吗？")
                        .setNegativeButton("确认删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Lost listed = new Lost();
                                listed.setObjectId(objectId);
                                listed.delete(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(getContext(), "删除成功！", Toast.LENGTH_SHORT).show();
                                            updateDate();
                                        } else {
                                            Toast.makeText(getContext(), "删除失败！" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                BmobFile file = new BmobFile();
                                file.setUrl(photos);//此url是上传文件成功之后通过bmobFile.getUrl()方法获取的。
                                file.delete(new UpdateListener() {

                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            Log.i("del", "done:删除图片成功 ");
                                        } else {
                                            Toast.makeText(getContext(), "图片资源删除失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                            toast("文件删除失败："+e.getErrorCode()+","+e.getMessage());
                                        }
                                    }
                                });
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                adminUser(dialogueDel);
                return true;
            }
        });

        //按照时间降序
        //执行查询，第一个参数为上下文，第二个参数为查找的回调
        return view;
    }

    public void updateDate() {
        BmobQuery<Lost> query = new BmobQuery<>();
        query.order("-createdAt");
        query.findObjects(new FindListener<Lost>() {
            @Override
            public void done(List<Lost> list, BmobException e) {
                if (e == null) {
                    losts = list;
                    listView.setAdapter(new AddAdapter(getActivity(),losts));
                    listView.deferNotifyDataSetChanged();

                } else {
                    Toast.makeText(getContext(), "数据加载失败！", Toast.LENGTH_SHORT).show();
                }
                progressBar.dismiss();
            }
        });
    }

    public void adminUser(AlertDialog dialog) {
        Intent intent = requireActivity().getIntent();
        String username = intent.getStringExtra("username");
        if (username.contentEquals("QJ315")) {
            dialog.show();
        }

    }

    @Override
    public void onResume() {
//        currTime = System.currentTimeMillis();
        super.onResume();
//        if ((currTime - prevTime) >= 1000) {
//            Toast.makeText(getContext(), "load", Toast.LENGTH_SHORT).show();
        progressBar.show();
        try {
            updateDate();
        } catch (Exception e) {
            e.printStackTrace();
            progressBar.dismiss();
        }
//        } else {
//            listView.setAdapter(new AddAdapter(getActivity(), losts));
//            listView.deferNotifyDataSetChanged();
//            progressBar.dismiss();
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        prevTime = currTime;
    }
}
