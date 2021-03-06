package com.qj315.QiJiu315.ui.fragment;

import android.content.Intent;
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
import com.qj315.QiJiu315.bean.Song;
import com.qj315.QiJiu315.ui.activity.PlayMusicDataActivity;
import com.qj315.QiJiu315.ui.adapter.AddGetMusic;
import com.qj315.QiJiu315.utils.PlayerMusic;

import java.io.File;
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
//                String path = songList.get(position).path;
                PlayerMusic.setUrllist(songList);
                Log.i("TAG", "onItemClick: "+ PlayerMusic.IsPlayer());
                if (!PlayerMusic.IsPlayer()){
//                    PlayerMusic.player(path);
                    PlayerMusic.ListPlayMusic(position);
                    Intent intent =new Intent(requireActivity(), PlayMusicDataActivity.class);
                    requireActivity().startActivity(intent);
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
        //???????????????????????????????????????????????????????????????????????????context.getApplicationContext().getCacheDir()+"/bmob/"
        File saveFile = new File(Environment.getExternalStorageDirectory(), file.getFilename());
        Log.i("TAG", "downloadFile: "+Environment.getExternalStorageDirectory());
        file.download(saveFile, new DownloadFileListener() {

            @Override
            public void onStart() {
                Toast.makeText(getContext(), "??????????????????", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void done(String savePath,BmobException e) {
                if(e==null){
                    Toast.makeText(getContext(), "????????????,????????????:"+savePath, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "???????????????"+e.getErrorCode()+","+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                Log.i("TAG","???????????????"+value+","+newworkSpeed);
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
                    Toast.makeText(getContext(), "?????????????????????", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}