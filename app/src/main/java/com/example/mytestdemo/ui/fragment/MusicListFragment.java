package com.example.mytestdemo.ui.fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mytestdemo.MainActivity;
import com.example.mytestdemo.R;
import com.example.mytestdemo.bean.Song;
import com.example.mytestdemo.utils.MusicUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MusicListFragment extends Fragment {
    View view;
    private MediaPlayer mediaPlayer;
    private ListView mylist;
    List<Song> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_music_list, container, false);

        initView();

        list = new ArrayList<>();

        list = MusicUtils.getmusic(requireContext());

        MyAdapter myAdapter = new MyAdapter(requireActivity(), list);
        mylist.setAdapter(myAdapter);



        //        给ListView添加点击事件，实现点击哪首音乐就进行播放
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String p = list.get(i).path;//获得歌曲的地址
                play(p);
            }
        });
        return view;
    }

    public void play(String path) {

        try {
            //        重置音频文件，防止多次点击会报错
            mediaPlayer.reset();
//        调用方法传进播放地址
            mediaPlayer.setDataSource(path);
//            异步准备资源，防止卡顿
            mediaPlayer.prepareAsync();
//            调用音频的监听方法，音频准备完毕后响应该方法进行音乐播放
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });

        } catch ( IOException e) {
            e.printStackTrace();
        }


    }
    public void initView(){
        mediaPlayer=new MediaPlayer();
        mylist=view.findViewById(R.id.list_music);

    }

    @Override
    public void onDestroy() {
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }
}
class MyAdapter extends BaseAdapter {

    Context context;
    List<Song> list;

    public MyAdapter(Context mainActivity, List<Song> list) {
        this.context = mainActivity;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Myholder myholder;

        if (view == null) {
            myholder = new Myholder();
            view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.item_data, null);

            myholder.t_position = view.findViewById(R.id.title_tv);
            myholder.t_song = view.findViewById(R.id.msg_data);
            myholder.t_singer = view.findViewById(R.id.date);
            myholder.t_duration = view.findViewById(R.id.phone);

            view.setTag(myholder);

        } else {
            myholder = (Myholder) view.getTag();
        }

        myholder.t_song.setText(list.get(i).song.toString());
        myholder.t_singer.setText(list.get(i).singer.toString());
        String time = MusicUtils.formatTime(list.get(i).duration);

        myholder.t_duration.setText(time);
        myholder.t_position.setText(i + 1 + "");


        return view;
    }


    class Myholder {
        TextView t_position, t_song, t_singer, t_duration;
    }


}


