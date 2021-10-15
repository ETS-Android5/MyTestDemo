package com.example.mytestdemo.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.mytestdemo.R;
import com.example.mytestdemo.UpdatePassword;
import com.example.mytestdemo.adapter.AddAdapter;
import com.example.mytestdemo.adapter.AddAdministrator;
import com.example.mytestdemo.dialog.SetProgressBar;
import com.example.mytestdemo.lost.Lost;
import com.example.mytestdemo.update.update;
import com.example.mytestdemo.userlist.UserList;

import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class Personal_Center extends Fragment {

    View view;
    private ImageView mAvatar;
    private TextView mUserNameTv;
    private Button stopmusic, updateok;
    private MediaPlayer mediaPlayer;
    private String s;
//    private ListView feedback;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.personal_center, container, false);
        initView();
//        UserList();
        Intent username = requireActivity().getIntent();
        View users = view.findViewById(R.id.user_data);
        users.getBackground().setAlpha(148);
        mUserNameTv.setText("用户名:" + username.getStringExtra("username"));

        BmobQuery<update> bmobQuery = new BmobQuery<update>();
        bmobQuery.getObject("Gb5WEEEK", new QueryListener<update>() {
            @Override
            public void done(update object, BmobException e) {
                if (e == null) {
//                            toast("查询成功");
                    s = object.getapkUrl();
//                    Log.i("APKURL", "done: " + s);
                    Log.i("TAG", "onStart: " + s);
                    Glide.with(requireActivity()).load(s).apply(new RequestOptions()
                            .transforms(new CenterCrop(), new RoundedCorners(20)
                            )).into(mAvatar);

                } else {
//                            toast("查询失败：" + e.getMessage());
                    Toast.makeText(getContext(), "头像资源加载失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        BmobQuery<update> bmobimg = new BmobQuery<update>();
        bmobimg.getObject("le8pUUUX", new QueryListener<update>() {
            @Override
            public void done(update update, BmobException e) {
                if (e == null) {
                    try {
                        String url = update.getapkUrl();
                        mediaPlayer.setDataSource(url);
                        mediaPlayer.prepare();
                    } catch (IOException es) {
                        es.printStackTrace();
                    }

                } else {
                    Toast.makeText(getContext(), "音频资源加载失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        stopmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    stopmusic.setText("开始");
                } else if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    stopmusic.setText("暂停");
                }
            }
        });

        updateok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), UpdatePassword.class));
            }
        });


        return view;

    }

//    public void UserList() {
//        BmobQuery<UserList> query = new BmobQuery<>();
//        query.order("-createdAt");
//        query.findObjects(new FindListener<UserList>() {
//            @Override
//            public void done(List<UserList> list, BmobException e) {
//                if (e == null) {
//                    feedback.setAdapter(new AddAdministrator(getActivity(),list));
//                    feedback.deferNotifyDataSetChanged();
//                } else {
//                    Toast.makeText(getContext(), "数据加载失败！", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    @Override
    public void onStart() {
        super.onStart();


//        requireActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {

//            }
//        });
    }

    public void initView() {
        mAvatar = view.findViewById(R.id.Avatar);
        mUserNameTv = view.findViewById(R.id.user_name_tv);
        stopmusic = view.findViewById(R.id.stop_music);
        updateok = view.findViewById(R.id.update_password);
//        feedback=view.findViewById(R.id.feedback_list);
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public void onResume() {
        SetProgressBar progressBar=new SetProgressBar(requireActivity());
        progressBar.show();
        super.onResume();
//        UserList();
    }
}
