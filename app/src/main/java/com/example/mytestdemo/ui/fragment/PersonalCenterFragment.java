package com.example.mytestdemo.ui.fragment;

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
import com.example.mytestdemo.ui.activity.UpdatePasswordActivity;
import com.example.mytestdemo.ui.dialog.SetProgressBar;
import com.example.mytestdemo.bean.update;

import java.io.IOException;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class PersonalCenterFragment extends Fragment {

    View view;
    private ImageView mAvatar;
    private TextView mUserNameTv;
    private Button stopmusic, updateok;
    private MediaPlayer mediaPlayer;
    private String s;
    private SetProgressBar progressBar;
    //    private ListView feedback;
    private boolean isAvatarLoaded = false;
    private boolean isMusicLoaded = false;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        initView();
//        UserList();
        Intent username = requireActivity().getIntent();
        View users = view.findViewById(R.id.user_data);
        users.getBackground().setAlpha(148);
        mUserNameTv.setText("用户名:" + username.getStringExtra("username"));
        try {
            BmobQuery<update> bmobQuery = new BmobQuery<update>();
            bmobQuery.getObject("Gb5WEEEK", new QueryListener<update>() {
                @Override
                public void done(update object, BmobException e) {
                    if (e == null) {
//                            toast("查询成功");
                        s = object.getapkUrl();
//                    Log.i("APKURL", "done: " + s);
                        Log.i("TAG", "onStart: " + s);
                        try {
                            Glide.with(requireActivity()).load(s).apply(new RequestOptions()
                                    .transforms(new CenterCrop(), new RoundedCorners(20)
                                    )).into(mAvatar);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    } else {
//                            toast("查询失败：" + e.getMessage());
                        Toast.makeText(getContext(), "头像资源加载失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    isAvatarLoaded = true;
                    if (isMusicLoaded) {
                        progressBar.dismiss();
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
                    isMusicLoaded = true;
                    if (isAvatarLoaded) {
                        progressBar.dismiss();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            progressBar.dismiss();
        }
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
                startActivity(new Intent(requireActivity(), UpdatePasswordActivity.class));
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
        progressBar = new SetProgressBar(requireActivity());
    }

    @Override
    public void onResume() {
        progressBar.show();
        super.onResume();
//        UserList();
    }

    @Override
    public void onPause() {
        super.onPause();
        isAvatarLoaded = false;
        isMusicLoaded = false;
    }
}
