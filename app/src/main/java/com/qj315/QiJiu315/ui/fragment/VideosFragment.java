package com.qj315.QiJiu315.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.mytestdemo.R;
import com.qj315.QiJiu315.bean.Song;
import com.qj315.QiJiu315.bean.VideoList;
import com.qj315.QiJiu315.ui.adapter.AddGetMusic;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.jzvd.JzvdStd;


public class VideosFragment extends Fragment {

    View view;
    List<VideoList> videoLists;
    private ListView mJieVideos;

    public VideosFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
//    public static VideosFragment newInstance(String param1, String param2) {
//        VideosFragment fragment = new VideosFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.fragment_videos, container, false);
         initView();
        Log.i("TAG", "onCreateView: ");
        try {
         updateDate();
        }catch (Exception e){
            Log.i("TAG", "onCreateView: "+e.getMessage());
        }
        return view;

    }

    public void initView() {
        mJieVideos=view.findViewById(R.id.jie_videos);
    }


    public void updateDate() {
        BmobQuery<VideoList> query = new BmobQuery<>();
        query.order("-createdAt");
        query.findObjects(new FindListener<VideoList>() {
            @Override
            public void done(List<VideoList> list, BmobException e) {

                if (e == null) {
                    videoLists = list;
                    mJieVideos.setAdapter(new VideoAdapter(requireActivity(), list));
//                    mJieVideos.deferNotifyDataSetChanged();

                } else {
                    Toast.makeText(getContext(), "数据加载失败！", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
class VideoAdapter extends BaseAdapter{
    Context context;
    List<VideoList> videoLists;

    public VideoAdapter(Context context, List<VideoList> videoLists) {
        this.context = context;
        this.videoLists = videoLists;
    }

    @Override
    public int getCount() {
        return videoLists.size();
    }

    @Override
    public Object getItem(int position) {
        return videoLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=LayoutInflater.from(context).inflate(R.layout.item_video,null);
        JzvdStd videoViewdata=convertView.findViewById(R.id.item_video);
        VideoList videoList=videoLists.get(position);
        Log.i("TAG", "getView: "+videoList.getUrl());
        videoViewdata.setUp(videoList.getUrl(), "第"+(position+1)+"视频");


        return convertView;
    }
}