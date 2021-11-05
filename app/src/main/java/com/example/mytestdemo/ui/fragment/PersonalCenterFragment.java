package com.example.mytestdemo.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.mytestdemo.R;
import com.example.mytestdemo.bean.User;
import com.example.mytestdemo.bean.update;
import com.example.mytestdemo.ui.activity.AlarmClockActivity;
import com.example.mytestdemo.ui.activity.UpdatePasswordActivity;
import com.example.mytestdemo.ui.dialog.SetProgressBar;
import com.example.mytestdemo.utils.PlayerMusic;

import java.io.File;
import java.util.List;
import java.util.Objects;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * *********     Github https://github.com/lmy8848
 * *********     @author 麒玖网络QJ315 (NJQ-PRO)
 * *********     @address     NJQ-PC
 * *********     @time 2021/10/29 21:10
 */
public class PersonalCenterFragment extends Fragment {

    View view;
    //    public static final int CAMERA = 1025;// 拍照的请求码
//    public static final int ALBUM = 1026;// 选择图片的请求码
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;
    private AlertDialog mDownloadDialog;
    private int s1;
    private ProgressBar mProgressBar;
    private ImageView mAvatar;
    private TextView mUserNameTv;
    private Button stopmusic, updateok, alarmclock;
    private MediaPlayer mediaPlayer;
    private String s;
    private String objectId;
    private String facesurl;
    private String facesuser;
    private SetProgressBar progressBar;
    //    private ListView feedback;
    private boolean isAvatarLoaded = false;
    private boolean isMusicLoaded = false;

    public PersonalCenterFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        initView();
        progressBar.show();
//        UserList();
        Intent username = requireActivity().getIntent();
        View users = view.findViewById(R.id.user_data);
        users.getBackground().setAlpha(148);
        mUserNameTv.setText("用户名:" + username.getStringExtra("username"));

        alarmclock.setOnClickListener(v -> startActivity(new Intent(requireActivity(), AlarmClockActivity.class)));

        mAvatar.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission
                    .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            } else {
                //执行启动相册的方法
                openAlbum();
            }
//                Intent intent = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                requireActivity().startActivityForResult(intent, ALBUM);
        });
        stopmusic.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                stopmusic.setText("播放");
            } else if (!mediaPlayer.isPlaying()) {
                if (PlayerMusic.IsPlayed()) {
                    PlayerMusic.Stop();
                }
                mediaPlayer.start();
                stopmusic.setText("暂停");
            }
        });

        updateok.setOnClickListener(v -> startActivity(new Intent(requireActivity(), UpdatePasswordActivity.class)));

        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Intent data = result.getData();
            if (data != null) {
                ClipData clipData = Objects.requireNonNull(data).getClipData();
                if (clipData == null || clipData.getItemCount() == 0) {
                    Toast.makeText(requireActivity(), "未选择任何图片", Toast.LENGTH_SHORT).show();
                } else {
                    handImage(clipData.getItemAt(0).getUri());
                }
            } else {
                Log.i("TAG", "onCreate: data为空 ");
            }
        });


        return view;

    }


    @Override
    public void onStart() {
        super.onStart();

    }


    public void initView() {
        mAvatar = view.findViewById(R.id.Avatar);
        mUserNameTv = view.findViewById(R.id.user_name_tv);
        stopmusic = view.findViewById(R.id.stop_music);
        updateok = view.findViewById(R.id.update_password);
        alarmclock = view.findViewById(R.id.alarm_clock);
//        feedback=view.findViewById(R.id.feedback_list);
        mediaPlayer = new MediaPlayer();
        progressBar = new SetProgressBar(requireActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("上传中");
        View view = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_progress, null);
        mProgressBar = view.findViewById(R.id.id_progress);
        builder.setView(view);
        mDownloadDialog = builder.create();
        mDownloadDialog.setCancelable(false);
    }

    //启动相册的方法
    @SuppressLint("IntentReset")
    private void openAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        //intent.setAction(Intent.ACTION_GET_CONTENT)  //实现相册多选 该方法获得的uri在转化为真实文件路径时Android 4.4以上版本会有问题
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);  //直接打开系统相册  不设置会有选择相册一步（例：系统相册、QQ浏览器相册）
        intentActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handImage(Uri uri) {
        String path = null;
        //根据不同的uri进行不同的解析
        assert uri != null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            path = getImagePath(uri);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        }
        //展示图片
        displayImage(path);
    }

    //content类型的uri获取图片路径的方法
    private String getImagePath(Uri uri) {
        String path = null;
        Cursor cursor = requireActivity().getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            imageView.setImageBitmap(bitmap);
            mAvatar.setImageBitmap(bitmap);
            BmobFile bmobFile = new BmobFile(new File(imagePath));
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    mDownloadDialog.show();
                    if (e == null) {
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
//                        Toast.makeText(AddDataActivity.this, "上传文件成功:"+bmobFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                        Log.i("Bmob", "done: " + bmobFile.getFileUrl());
                        facesurl = bmobFile.getFileUrl();

                        User p2 = new User();
                        p2.setFaces(facesurl);
                        p2.update(objectId, new UpdateListener() {

                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Log.i("TAG", "done: " + p2.getCreatedAt());
                                    NetworkRequest();
                                } else {
                                    Toast.makeText(getContext(), "更新头像连接错误！" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
                        if (s1 > 99) {
                            mDownloadDialog.cancel();
                            mDownloadDialog.dismiss();
                            Log.i("TAG", "onProgress: " + s);
                        }
                    } else {
                        Toast.makeText(requireActivity(), "上传失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        mDownloadDialog.dismiss();
                    }

                }

                @Override
                public void onProgress(Integer value) {
                    // 返回的上传进度（百分比）
                    s1 = value;
                    requireActivity().runOnUiThread(() -> mProgressBar.setProgress(s1));


                }
            });
            Toast.makeText(getContext(), "" + bitmap, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "fail to set image", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onResume() {
        if (!isAvatarLoaded && !isMusicLoaded) {
            progressBar.show();
            NetworkRequest();
        }
        super.onResume();
//        UserList();
    }

    @Override
    public void onPause() {
        super.onPause();
        isAvatarLoaded = false;
        isMusicLoaded = false;
    }

    private void NetworkRequest() {
        String uname = BmobUser.getCurrentUser().getUsername();
        BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("username", uname);
        categoryBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    Log.i("TAG", "done: " + uname + object.size());
                    if (object.size() > 0) {
                        User user = object.get(0);
                        objectId = user.getObjectId();
                        facesuser = user.getFaces();
                        try {
                            Glide.with(requireActivity()).load(facesuser).placeholder(android.R.drawable.ic_input_add).apply(new RequestOptions()
                                    .transforms(new CenterCrop(), new RoundedCorners(20)
                                    )).into(mAvatar);
                            BmobFile file = new BmobFile();
                            file.setUrl(facesuser);//此url是上传文件成功之后通过bmobFile.getUrl()方法获取的。
                            file.delete(new UpdateListener() {

                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Log.i("del", "done:删除图片成功 ");
                                    } else {
                                        Toast.makeText(getContext(), "数据库图片资源删除失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            Log.i("TAG", "done: " + object.size());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "头像资源加载失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("BMOB", e.toString() + "||" + e.getMessage());
                }
                isAvatarLoaded = true;
                if (isMusicLoaded) {
                    progressBar.dismiss();
                }
            }
        });


        try {
            BmobQuery<update> bmobimg = new BmobQuery<>();
            bmobimg.getObject("le8pUUUX", new QueryListener<update>() {
                @Override
                public void done(update update, BmobException e) {
                    if (e == null) {
                        try {
                            String url = update.getapkUrl();
                            mediaPlayer.setDataSource(url);
                            mediaPlayer.prepare();
                        } catch (Exception es) {
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
    }

}
