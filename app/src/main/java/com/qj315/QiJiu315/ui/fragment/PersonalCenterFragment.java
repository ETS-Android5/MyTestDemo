package com.qj315.QiJiu315.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import com.qj315.QiJiu315.bean.User;
import com.qj315.QiJiu315.bean.update;
import com.qj315.QiJiu315.ui.activity.AlarmClockActivity;
import com.qj315.QiJiu315.ui.activity.SettingActivity;
import com.qj315.QiJiu315.ui.activity.UpdatePasswordActivity;
import com.qj315.QiJiu315.ui.dialog.SetProgressBar;
import com.qj315.QiJiu315.utils.PlayerMusic;

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
 * *********     @author ????????????QJ315 (NJQ-PRO)
 * *********     @address     NJQ-PC
 * *********     @time 2021/10/29 21:10
 */
public class PersonalCenterFragment extends Fragment {

    View view;
    //    public static final int CAMERA = 1025;// ??????????????????
//    public static final int ALBUM = 1026;// ????????????????????????
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;
    private AlertDialog mDownloadDialog;
    private int s1;
    private ProgressBar mProgressBar;
    private ImageView mAvatar;
    private TextView mUserNameTv;
    private Button stopmusic, updateok, alarmclock,setting;
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

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        initView();
        progressBar.show();
//        UserList();
        Intent username = requireActivity().getIntent();
        View users = view.findViewById(R.id.user_data);
        users.setBackground(getResources().getDrawable(R.drawable.user_background));
        users.getBackground().setAlpha(148);
        mUserNameTv.setText("?????????:" + username.getStringExtra("username"));

        alarmclock.setOnClickListener(v -> startActivity(new Intent(requireActivity(), AlarmClockActivity.class)));

        mAvatar.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission
                    .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            } else {
                //???????????????????????????
                openAlbum();
            }
//                Intent intent = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                requireActivity().startActivityForResult(intent, ALBUM);
        });
        stopmusic.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                stopmusic.setText("??????");
            } else if (!mediaPlayer.isPlaying()) {
                if (PlayerMusic.IsPlayed()) {
                    PlayerMusic.Stop();
                }
                mediaPlayer.start();
                stopmusic.setText("??????");
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SettingActivity.class));
            }
        });

        updateok.setOnClickListener(v -> startActivity(new Intent(requireActivity(), UpdatePasswordActivity.class)));

        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Intent data = result.getData();
            if (data != null) {
                ClipData clipData = Objects.requireNonNull(data).getClipData();
                if (clipData == null || clipData.getItemCount() == 0) {
                    Toast.makeText(requireActivity(), "?????????????????????", Toast.LENGTH_SHORT).show();
                } else {
                    handImage(clipData.getItemAt(0).getUri());
                }
            } else {
                Log.i("TAG", "onCreate: data?????? ");
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
        setting=view.findViewById(R.id.system_Setting);
        updateok = view.findViewById(R.id.update_password);
        alarmclock = view.findViewById(R.id.alarm_clock);
//        feedback=view.findViewById(R.id.feedback_list);
        mediaPlayer = new MediaPlayer();
        progressBar = new SetProgressBar(requireActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("?????????");
        View view = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_progress, null);
        mProgressBar = view.findViewById(R.id.id_progress);
        builder.setView(view);
        mDownloadDialog = builder.create();
        mDownloadDialog.setCancelable(false);
    }

    //?????????????????????
    @SuppressLint("IntentReset")
    private void openAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        //intent.setAction(Intent.ACTION_GET_CONTENT)  //?????????????????? ??????????????????uri?????????????????????????????????Android 4.4????????????????????????
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);  //????????????????????????  ?????????????????????????????????????????????????????????QQ??????????????????
        intentActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handImage(Uri uri) {
        String path = null;
        //???????????????uri?????????????????????
        assert uri != null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            path = getImagePath(uri);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        }
        //????????????
        displayImage(path);
    }

    //content?????????uri???????????????????????????
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
            BmobQuery<User> bmobQuery = new BmobQuery<User>();
            bmobQuery.getObject("6b6c11c537", new QueryListener<User>() {
                @Override
                public void done(User object,BmobException e) {
                    if(e==null){
//                        toast("????????????");
                        Log.i("TAG", "done: ????????????????????????");
                        BmobFile file = new BmobFile();
                        file.setUrl(object.getFaces());//???url?????????????????????????????????bmobFile.getUrl()??????????????????
                        file.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Log.i("del", "done:?????????????????? ");
                                } else {
                                    if (isAdded()) {
                                        Toast.makeText(requireActivity(), "?????????????????????????????????" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }else{
                        Log.i("TAG", "done:????????????????????? ");
                    }
                }
            });
            BmobFile bmobFile = new BmobFile(new File(imagePath));
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    mDownloadDialog.show();
                    if (e == null) {
                        //bmobFile.getFileUrl()--????????????????????????????????????
//                        Toast.makeText(AddDataActivity.this, "??????????????????:"+bmobFile.getFileUrl(), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getContext(), "???????????????????????????" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
                        if (s1 > 99) {
                            mDownloadDialog.cancel();
                            mDownloadDialog.dismiss();
                            Log.i("TAG", "onProgress: " + s);
                        }
                    } else {
                        Toast.makeText(requireActivity(), "????????????:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        mDownloadDialog.dismiss();
                    }

                }

                @Override
                public void onProgress(Integer value) {
                    // ????????????????????????????????????
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
                            Log.i("TAG", "done: " + object.size());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "????????????????????????" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "????????????????????????" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
