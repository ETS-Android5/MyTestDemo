package com.qj315.QiJiu315.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mytestdemo.R;
import com.qj315.QiJiu315.bean.Song;
import com.qj315.QiJiu315.utils.MusicUtils;
import com.qj315.QiJiu315.utils.PlayerMusic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class MusicListFragment extends Fragment {
    View view;
    List<Song> list;
    private ListView mylist;
    private AlertDialog udialog;
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_music_list, container, false);

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

        mylist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String pathing = list.get(position).path;
                Song psong=list.get(position);
                AlertDialog builder = new AlertDialog.Builder(requireActivity()).setTitle("音乐上传")
                        .setMessage("您确认将此条数据上传到云端吗？").setNegativeButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BmobFile bmobFile = new BmobFile(new File(pathing));
                                bmobFile.uploadblock(new UploadFileListener() {

                                    @Override
                                    public void done(BmobException e) {
                                        udialog.show();
                                        dialog.dismiss();
                                        if (e == null) {
                                            Toast.makeText(getContext(), "上传文件成功:" + bmobFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                                            addMusic(psong.song,psong.singer,bmobFile.getFileUrl());
                                            udialog.dismiss();
                                        } else {
                                            Toast.makeText(getContext(), "上传文件失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            udialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onProgress(Integer value) {
                                        mProgressBar.setProgress((int)value);
                                        // 返回的上传进度（百分比）

                                    }
                                });
                            }
                        }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();

                builder.show();
                return true;
            }
        });
        return view;
    }

    public void play(String path) {
        if (!PlayerMusic.IsPlayer()){
            PlayerMusic.player(path);
        }


    }
    public void addMusic(String song,String singer,String path){
        Song addsong=new Song();
        addsong.setSong(song);
        addsong.setSinger(singer);
        addsong.setPath(path);
        addsong.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Toast.makeText(requireActivity(), "添加数据成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(requireActivity(), "创建数据失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void initView() {
        mylist = view.findViewById(R.id.list_music);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("上传中");
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_progress, null);
        mProgressBar = view.findViewById(R.id.id_progress);
        builder.setView(view);
        udialog = builder.create();
        udialog.setCancelable(false);

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

    @SuppressLint("SetTextI18n")
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

        myholder.t_song.setText(list.get(i).song);
        myholder.t_position.setText(list.get(i).singer);
        String time = MusicUtils.formatTime(list.get(i).duration);

        myholder.t_duration.setText(time);
        myholder.t_singer.setText(i + 1 + "");


        return view;
    }


    class Myholder {
        TextView t_position, t_song, t_singer, t_duration;
    }


}


