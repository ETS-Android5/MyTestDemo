package com.example.mytestdemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mytestdemo.LostDescribe;
import com.example.mytestdemo.R;
import com.example.mytestdemo.adapter.AddAdapter;
import com.example.mytestdemo.lost.Lost;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;


public class FragmentList extends Fragment {
    View view;
    ListView listView;
    List<Lost> losts;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragmentlist,container,false);
        listView=view.findViewById(R.id.list_view);

        BmobQuery<Lost> query = new BmobQuery<>();
        query.order("-createdAt");

        query.findObjects( new FindListener<Lost>() {
            @Override
            public void done(List<Lost> list, BmobException e) {
                if (e==null){
                    losts=list;
                    listView.setAdapter(new AddAdapter(getActivity(),losts));
                }
                else {
                    Toast.makeText(getContext(), "数据加载失败！", Toast.LENGTH_SHORT).show();
                }
            }

//                //将结果显示在列表中
//                LostAdapter.addAll(losts);
        });

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Lost lost=losts.get(position);
                        Intent intent=new Intent(getActivity(), LostDescribe.class);
                        intent.putExtra("title",lost.getTitle());
                        intent.putExtra("describe",lost.getDescribe());
                        intent.putExtra("date",lost.getCreatedAt());
                        intent.putExtra("phone",lost.getPhone());
                        startActivity(intent);
                    }
                });


        //按照时间降序
        //执行查询，第一个参数为上下文，第二个参数为查找的回调
        return view;
    }


}
