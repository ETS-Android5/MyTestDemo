package com.example.mytestdemo.fragment;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mytestdemo.R;
import com.example.mytestdemo.lost.Lost;

import java.io.File;
import java.util.Objects;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;


public class AddListData extends Fragment {
    View view;
    private EditText mTitleTv;
    private EditText mPhoneNumber;
    private EditText mMessageData;
    private ImageButton mReleaseBtn;
    private RadioButton goback;
    private ImageView imG;
    private Button addImg;
    private String photourl;
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.addlist, container, false);
        initView();
        photourl = null;
        setHasOptionsMenu(true);
        requireActivity().setTitle("添加信息");
        Bmob.initialize(getContext(), "08f5717e435ccb57bd2b266c62b30563");

        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission
                        .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                } else {
                    //执行启动相册的方法
                    openAlbum();
                }
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish();
            }
        });
        mReleaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String describe = mMessageData.getText().toString();
                String phone = mPhoneNumber.getText().toString();
                String title = mTitleTv.getText().toString();
                if (describe.isEmpty() || phone.isEmpty() || title.isEmpty()) {
                    Toast.makeText(requireActivity(), "必填项不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    Lost lost = new Lost();
                    lost.setDescribe(describe);
                    lost.setPhone(phone);
                    lost.setTitle(title);
                    if (photourl != null) {
                        lost.setDate(photourl);
                    }
                    lost.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
//                                FragmentManager fm = requireActivity().getSupportFragmentManager();
//                                Fragment fragment = new FragmentList();
//                                fm.beginTransaction().replace(R.id.fragment_list, fragment).commit();
                                requireActivity().finish();
                                Toast.makeText(requireActivity(), "失物信息添加成功!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireActivity(), "添加失败:" + e.getMessage() + "|", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });
        return view;

    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Intent data = result.getData();
            if (data != null) {
                ClipData clipData = Objects.requireNonNull(data).getClipData();
                if (clipData == null || clipData.getItemCount() == 0) {

                    Toast.makeText(getContext(), "未选择任何图片", Toast.LENGTH_SHORT).show();
                } else {
                    handImage(clipData.getItemAt(0).getUri());
                }
            }else {
                Log.i("TAG", "onCreate: data为空 ");
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if ("返回".contentEquals(item.getTitle())) {
//            FragmentManager fm = requireActivity().getSupportFragmentManager();
//            Fragment fragment = new FragmentList();
//            fm.beginTransaction().replace(R.id.fragment_list, fragment).commit();
            requireActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();//清除原有菜单选项
        menu.add("返回").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

    }


    private void initView() {
        mTitleTv = view.findViewById(R.id.title_tv);
        mPhoneNumber = view.findViewById(R.id.phone_number);
        mMessageData = view.findViewById(R.id.message_data);
        mReleaseBtn = view.findViewById(R.id.release_btn);
        addImg = view.findViewById(R.id.add_img);
        goback = view.findViewById(R.id.go_rb_back);
        imG = view.findViewById(R.id.Img);
        mMessageData.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);//多行模式
        mMessageData.setSingleLine(false);//是否单行模式
        mMessageData.setHorizontallyScrolling(false);//是否水平滚动
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                openAlbum();
            else Toast.makeText(getContext(), "你拒绝了", Toast.LENGTH_SHORT).show();
        }
    }

    //启动相册的方法
    private void openAlbum() {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType("image/*");
//        startActivityForResult(intent,1);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        //intent.setAction(Intent.ACTION_GET_CONTENT)  //实现相册多选 该方法获得的uri在转化为真实文件路径时Android 4.4以上版本会有问题
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);  //直接打开系统相册  不设置会有选择相册一步（例：系统相册、QQ浏览器相册）
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
        intentActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }


    //*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 2) {
//            //判断安卓版本
//            if (resultCode == Activity.RESULT_OK && data != null) {
//                handImage(data);
//            }
//        }
//    }

    //安卓版本大于4.4的处理方法
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handImage(Uri uri) {
        String path = null;
//        assert uri != null;
//        Log.e("TAG", "handImage: " + uri.getScheme());
        //根据不同的uri进行不同的解析
        assert uri != null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            path = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        }
        //展示图片
        displayImage(path);
    }


    //安卓小于4.4的处理方法
    private void handImageLow(Intent data) {
        Uri uri = data.getData();
        String path = getImagePath(uri, null);
        displayImage(path);
    }

    //content类型的uri获取图片路径的方法
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = requireActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    //根据路径展示图片的方法
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            imageView.setImageBitmap(bitmap);
            imG.setImageBitmap(bitmap);
            BmobFile bmobFile = new BmobFile(new File(imagePath));
            bmobFile.uploadblock(new UploadFileListener() {

                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
//                        Toast.makeText(getContext(), "上传文件成功:"+bmobFile.getFileUrl(), Toast.LENGTH_SHORT).show();

                        Log.i("Bmob", "done: " + bmobFile.getFileUrl());
                        photourl = bmobFile.getFileUrl();
                    } else {
                        Toast.makeText(getContext(), "上传失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onProgress(Integer value) {
                    // 返回的上传进度（百分比）
                }
            });
            Toast.makeText(requireContext(), "" + bitmap, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "fail to set image", Toast.LENGTH_SHORT).show();
        }
    }
}
