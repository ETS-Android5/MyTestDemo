package com.qj315.QiJiu315;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mytestdemo.R;
import com.qj315.QiJiu315.bean.update;
import com.qj315.QiJiu315.ui.activity.AddDataActivity;
import com.qj315.QiJiu315.ui.activity.LoginActivity;
import com.qj315.QiJiu315.ui.activity.PhoneVerifyActivity;
import com.qj315.QiJiu315.ui.dialog.DialogUpdateV;
import com.qj315.QiJiu315.ui.dialog.Feedback;
import com.qj315.QiJiu315.ui.dialog.UpdateTest;
import com.qj315.QiJiu315.utils.APKVersionCodeUtils;
import com.qj315.QiJiu315.utils.PlayerMusic;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class MainActivity extends AppCompatActivity {
    private final String mVersion_name = "app-release.apk";
    String name;
    //    LinearLayout fragment;
    private long exitTime = 0;
    private String code1, url, text;
    private boolean mIsCancel;
    private AlertDialog mDownloadDialog;
    private String mSavePath;
    private ProgressBar mProgressBar;
    private int code;
    private int mProgress;
    @SuppressLint("HandlerLeak")
    private final Handler mUpdateProgressHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // 设置进度条
                    mProgressBar.setProgress(mProgress);
                    break;
                case 2:
                    // 隐藏当前下载对话框
                    mDownloadDialog.dismiss();
                    // 安装 APK 文件
                    installAPK();
            }
        }
    };
    //    private RadioButton home,alllist,personal_center;
    private FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = BmobUser.getCurrentUser().getUsername();
        button = findViewById(R.id.fab);
        new Thread(() -> {
            while (true) {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(() -> {

                    if (PlayerMusic.IsPlayed()) {
                        button.setVisibility(View.VISIBLE);
                    } else {
                        button.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerMusic.Stop();
            }
        });
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_admins, R.id.navigation_person_center,R.id.navigation_chat)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        navView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull @NotNull MenuItem item) {

                Toast.makeText(MainActivity.this, "当前页已在" + item.getTitle().toString(), Toast.LENGTH_SHORT).show();
            }
        });


//        initView();
////        Bmob.resetDomain("https://files.lumingyuan6868.xyz");
//        Bmob.initialize(MainActivity.this, "08f5717e435ccb57bd2b266c62b30563");
//        setView1();
//      Intent userdata =getIntent();
//         name = userdata.getStringExtra("username");
//         querySingleData();
//
//        alllist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setView3();
//                setTitle("管理员列表");
//            }
//        });
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setView1();
//                setTitle("主页");
//            }
//        });
//        personal_center.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setView4();
//                setTitle("个人中心");
//            }
//        });

    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            //彻底关闭整个APP
            int currentVersion = android.os.Build.VERSION.SDK_INT;
            if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                System.exit(0);
            } else {// android2.1
                ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                am.restartPackage(getPackageName());
            }
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
//                Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
                break;
            case R.id.help:
                Toast.makeText(this, "帮助", Toast.LENGTH_SHORT).show();
                Feedback feedback = new Feedback(MainActivity.this);
                feedback.setCancelable(true);
                feedback.show();
                break;
            case R.id.add:
                Toast.makeText(this, "添加信息", Toast.LENGTH_SHORT).show();
//                initView();
//                setView2();
                startActivity(new Intent(this, AddDataActivity.class));
//                overridePendingTransition(R.anim.activity_open,0);
                break;
            case R.id.sms_for_phone:
                Toast.makeText(this, "验证码测试！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, PhoneVerifyActivity.class);
                intent.putExtra("username", name);
                startActivity(intent);
                break;
//            case R.id.update_msg:
//                if (name.equals("QJ315")) {
//                    UpdateTest updateTest = new UpdateTest(MainActivity.this);
//                    updateTest.create();
//                    updateTest.show();
//                } else {
//                    Toast.makeText(this, "系统默认管理员，您不是有效管理！", Toast.LENGTH_SHORT).show();
//                }
//                break;
            case R.id.update_dialog:
                querySingleData();
                break;
//            case R.id.logon:
//                Handler handler = new Handler();
//                handler.postDelayed(() -> {
//                    if (PlayerMusic.IsPlayed()) {
//                        PlayerMusic.Reset();
//                    }
//                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                    BmobUser.logOut();
//                    finish();
//                }, 1000);
//                break;
//            case R.id.update_v:
//                if (name.equals("QJ315")) {
//                    DialogUpdateV updateV = new DialogUpdateV(MainActivity.this);
//                    updateV.setCancelable(true);
//                    updateV.show();
//                } else {
//                    Toast.makeText(this, "系统默认管理员，您不是有效管理！", Toast.LENGTH_SHORT).show();
//                }
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void querySingleData() {
        BmobQuery<update> bmobQuery = new BmobQuery<update>();
        bmobQuery.getObject("hgLn333N", new QueryListener<update>() {
            @Override
            public void done(update object, BmobException e) {
                if (e == null) {

                    url = object.getAddress();
                    code1 = object.getCode();
                    text = object.getText();
                    System.out.println("APK更新地址：" + url);
                    System.out.println("版本号：" + code1);
                    System.out.println("更新内容" + text);
                    check();
                } else {
                    Log.i("bmob图片", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    public void check() {
        code = APKVersionCodeUtils.getVersionCode(this);
        int i = Integer.parseInt(this.code1);
        if (i > code) {
            showDialog();
        } else {
            Toast.makeText(this, "软件已是最新版本！", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.lostandfound128)//设置标题的图片
                .setTitle("检查到新版本")//设置对话框的标题
                .setMessage(text)//设置对话框的内容
                //设置对话框的按钮
                .setNegativeButton("取消", (dialog12, which) -> {
                    Toast.makeText(MainActivity.this, "取消更新", Toast.LENGTH_SHORT).show();
                    dialog12.dismiss();
                })
                .setPositiveButton("更新", (dialog1, which) -> {
                    Toast.makeText(MainActivity.this, "点击了确定的按钮", Toast.LENGTH_SHORT).show();
//                        postUrl(url);
                    mIsCancel = false;
                    //展示对话框
                    showDownloadDialog(url);
                    dialog1.dismiss();
                }).create();
        dialog.show();
    }


    /*
     * 显示正在下载对话框
     */
    protected void showDownloadDialog(String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("下载中");
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_progress, null);
        mProgressBar = (ProgressBar) view.findViewById(R.id.id_progress);
        builder.setView(view);

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 隐藏当前对话框
                dialog.dismiss();
                // 设置下载状态为取消
                mIsCancel = true;
            }
        });

        mDownloadDialog = builder.create();
        mDownloadDialog.setCancelable(false);
        mDownloadDialog.show();

        // 下载文件
        downloadAPK();
    }


    private void downloadAPK() {
        new Thread(() -> {

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                        String sdPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
//                      文件保存路径
//                        mSavePath = sdPath + "SITdownload";
                mSavePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                File dir = new File(mSavePath);
//                        if (!dir.exists()){
//                            dir.mkdirs();
//                        }
                // Log.e("TAG", "run: "+ MainActivity.this.url);
                // 下载文件
                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) new URL(MainActivity.this.url).openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("TAG", "run: " + 111);
                }
                conn.setDoInput(true);
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                try {
                    conn.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("TAG", "run: " + 222);
                }

                InputStream is = null;
                try {
                    is = conn.getInputStream();

                    int length = conn.getContentLength();
                    Log.e("TAG", "run: " + length);

                    File apkFile = new File(dir, mVersion_name);
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    Log.e("TAG", "run: " + apkFile.getAbsolutePath());
                    int count = 0;
                    byte[] buffer = new byte[1024];
                    while (!mIsCancel) {
                        int numread = is.read(buffer);
                        count += numread;
                        // 计算进度条的当前位置
                        mProgress = (int) (((float) count / length) * 100);
                        Log.e("mProgress", "run: " + mProgress);

//                            Toast.makeText(MainActivity.this, ""+mProgress, Toast.LENGTH_SHORT).show();
                        // 更新进度条
                        mUpdateProgressHandler.sendEmptyMessage(1);

                        // 下载完成
                        if (numread < 0) {
                            mUpdateProgressHandler.sendEmptyMessage(2);
                            break;
                        }
                        fos.write(buffer, 0, numread);
                    }
                    fos.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 接收消息
     */


    /*
     * 下载到本地后执行安装
     */
    protected void installAPK() {
        File apkFile = new File(mSavePath, mVersion_name);
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", apkFile);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        getApplicationContext().startActivity(intent);

    }


    private void initView() {
//         fragment = (LinearLayout) findViewById(R.id.fragment_list);
//         home=findViewById(R.id.home_rb);
//         alllist=findViewById(R.id.all_admin_list);
//         personal_center=findViewById(R.id.PersonalCenterFragment);

    }
//    private void setmProgress(){
////        AlertDialog.Builder builderpg=new AlertDialog.Builder(MainActivity.this);
////        @SuppressLint("InflateParams") View viewpg=LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_rau_progressbar,null);
////        builderpg.setView(viewpg);
////        ProgressBar progressBar=viewpg.findViewById(R.id.id_progress_ra);
////        builderpg.setCancelable(false);
////        builderpg.create();
////        builderpg.show();
//
//        SetProgressBar setProgressBar=new SetProgressBar(MainActivity.this);
//        setProgressBar.show();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                setProgressBar.dismiss();
//            }
//        },1000);
//    }
//
//    @Override
//    protected void onRestart() {
//        switch (getTitle().toString()){
//            case "管理员列表":
//                setView3();
//                break;
//            case "主页":
//                setView1();
//                break;
//            case "个人中心":
//                setView4();
//                break;
//        }
//        super.onRestart();
//
//    }
}
