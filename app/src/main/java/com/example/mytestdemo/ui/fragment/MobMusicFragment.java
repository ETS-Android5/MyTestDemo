package com.example.mytestdemo.ui.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mytestdemo.R;
import com.example.mytestdemo.bean.Song;
import com.example.mytestdemo.ui.adapter.AddGetMusic;

import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class MobMusicFragment extends Fragment {
    View view;

    private ListView mMusicGetMusic;
    private List<Song> songList;
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bmob_music, container, false);
        initView();
        updateDate();
        mMusicGetMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String path = songList.get(position).path;
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

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        return view;
    }

    private void initView() {
        mMusicGetMusic = view.findViewById(R.id.music_get_music);
        mediaPlayer = new MediaPlayer();
    }

    public void updateDate() {
        BmobQuery<Song> query = new BmobQuery<>();
        query.order("-createdAt");
        query.findObjects(new FindListener<Song>() {
            @Override
            public void done(List<Song> list, BmobException e) {

                if (e == null) {
                    songList = list;
                    mMusicGetMusic.setAdapter(new AddGetMusic(requireActivity(), list));
                    mMusicGetMusic.deferNotifyDataSetChanged();

                } else {
                    Toast.makeText(getContext(), "数据加载失败！", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}