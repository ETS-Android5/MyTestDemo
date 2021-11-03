package com.example.mytestdemo.ui.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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
import com.example.mytestdemo.utils.PlayerMusic;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
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
                String path = songList.get(position).path;
                Log.i("TAG", "onItemClick: "+ PlayerMusic.IsPlayer());
                if (!PlayerMusic.IsPlayer()){
                    PlayerMusic.player(path);
                }


            }
        });
        mMusicGetMusic.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Song song = songList.get(position);
                BmobFile bmobFile=new BmobFile(song.song,"",song.path);
                downloadFile(bmobFile);

                return true;
            }
        });


        return view;
    }

    private void initView() {
        mMusicGetMusic = view.findViewById(R.id.music_get_music);
        mediaPlayer = new MediaPlayer();
    }

    private void downloadFile(BmobFile file){
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
        File saveFile = new File(Environment.getExternalStorageDirectory(), file.getFilename());
        Log.i("TAG", "downloadFile: "+Environment.getExternalStorageDirectory());
        file.download(saveFile, new DownloadFileListener() {

            @Override
            public void onStart() {
                Toast.makeText(getContext(), "开始下载……", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void done(String savePath,BmobException e) {
                if(e==null){
                    Toast.makeText(getContext(), "下载成功,保存路径:"+savePath, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "下载失败："+e.getErrorCode()+","+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                Log.i("TAG","下载进度："+value+","+newworkSpeed);
            }

        });
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