package com.qj315.QiJiu315.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.qj315.QiJiu315.MainActivity;
import com.example.mytestdemo.R;
import com.qj315.QiJiu315.bean.Lost;

import java.io.File;
import java.util.Objects;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class AddDataActivity extends AppCompatActivity {

    private LinearLayout mAddDataList;
    private EditText mTitleTv;
    private EditText mPhoneNumber;
    private EditText mMessageData;
    private ImageButton mReleaseBtn;
    private RadioButton goback;
    private ImageView imG;
    private Button addImg;
    private AlertDialog mDownloadDialog;
    private String photourl;
    private ProgressBar mProgressBar;
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;
    private int s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        AddDataActivity.this.setTitle("????????????");
        getSupportActionBar().hide();

        initView();
        photourl = null;
        Bmob.initialize(AddDataActivity.this, "08f5717e435ccb57bd2b266c62b30563");
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddDataActivity.this, Manifest.permission
                        .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddDataActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                } else {
                    //???????????????????????????
                    openAlbum();
                }
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDataActivity.this.finish();
                startActivity(new Intent(AddDataActivity.this, MainActivity.class));
            }
        });
        mReleaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String describe = mMessageData.getText().toString();
                String phone = mPhoneNumber.getText().toString();
                String title = mTitleTv.getText().toString();
                if (describe.isEmpty() || phone.isEmpty() || title.isEmpty()) {
                    Toast.makeText(AddDataActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
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
//                                FragmentManager fm AddDataActivity.this.getSupportFragmentManager();
//                                Fragment fragment = new HomeFragment();
//                                fm.beginTransaction().replace(R.id.fragment_list, fragment).commit();
                                AddDataActivity.this.finish();
                                Toast.makeText(AddDataActivity.this, "????????????????????????!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddDataActivity.this, "????????????:" + e.getMessage() + "|", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });
        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Intent data = result.getData();
            if (data != null) {
                ClipData clipData = Objects.requireNonNull(data).getClipData();
                if (clipData == null || clipData.getItemCount() == 0) {
                    Toast.makeText(AddDataActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                } else {
                    handImage(clipData.getItemAt(0).getUri());
                }
            } else {
                Log.i("TAG", "onCreate: data?????? ");
            }
        });
    }

    public void initView() {
        mTitleTv = findViewById(R.id.title_tv);
        mPhoneNumber = findViewById(R.id.phone_number);
        mMessageData = findViewById(R.id.message_data);
        mReleaseBtn = findViewById(R.id.release_btn);
        addImg = findViewById(R.id.add_img);
        goback = findViewById(R.id.go_rb_back);
        imG = findViewById(R.id.Img);
        mMessageData.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);//????????????
        mMessageData.setSingleLine(false);//??????????????????
        mMessageData.setHorizontallyScrolling(false);//??????????????????
        AlertDialog.Builder builder = new AlertDialog.Builder(AddDataActivity.this);
        builder.setTitle("?????????");
        View view = LayoutInflater.from(AddDataActivity.this).inflate(R.layout.dialog_progress, null);
        mProgressBar = view.findViewById(R.id.id_progress);
        builder.setView(view);
        mDownloadDialog = builder.create();
        mDownloadDialog.setCancelable(false);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if ("??????".contentEquals(item.getTitle())) {
            AddDataActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                openAlbum();
            else Toast.makeText(AddDataActivity.this, "????????????", Toast.LENGTH_SHORT).show();
        }
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

    //??????????????????4.4???????????????
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handImage(Uri uri) {
        String path = null;
        //???????????????uri?????????????????????
        assert uri != null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            path = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        }
        //????????????
        displayImage(path);
    }


    //????????????4.4???????????????
    private void handImageLow(Intent data) {
        Uri uri = data.getData();
        String path = getImagePath(uri, null);
        displayImage(path);
    }

    //content?????????uri???????????????????????????
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = AddDataActivity.this.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    //?????????????????????????????????
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            imageView.setImageBitmap(bitmap);
            imG.setImageBitmap(bitmap);
            BmobFile bmobFile = new BmobFile(new File(imagePath));
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    mDownloadDialog.show();
                    if (e == null) {
                        //bmobFile.getFileUrl()--????????????????????????????????????
//                        Toast.makeText(AddDataActivity.this, "??????????????????:"+bmobFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                        Log.i("Bmob", "done: " + bmobFile.getFileUrl());
                        photourl = bmobFile.getFileUrl();
                        if (s > 99) {
                            mDownloadDialog.cancel();
                            mDownloadDialog.dismiss();
                            Log.i("TAG", "onProgress: " + s);
                        }
                    } else {
                        Toast.makeText(AddDataActivity.this, "????????????:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        mDownloadDialog.dismiss();
                    }

                }

                @Override
                public void onProgress(Integer value) {
                    // ????????????????????????????????????
                    s = value;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(s);
                        }
                    });


                }
            });
            Toast.makeText(AddDataActivity.this, "" + bitmap, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AddDataActivity.this, "fail to set image", Toast.LENGTH_SHORT).show();

        }
    }
}