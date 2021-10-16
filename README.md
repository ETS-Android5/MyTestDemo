# MyTestDemo
## 个人博客（wordpress构建）
[麒玖网络（QJ315）](http://www.lumingyuan6868.xyz/)
## Demo示例图
![图片alt](https://files.lumingyuan6868.xyz/2021/10/16/beddfb1b5e904fc1aa384cb5c600034b.jpg)
![图片alt](https://files.lumingyuan6868.xyz/2021/10/16/2d9b38e909994d9089583128a4499bbf.jpg)
![图片alt](https://files.lumingyuan6868.xyz/2021/10/16/9f2bc3165c8642568bc7084b29d5b96c.jpg)

## 项目build.gradle
``` 
plugins {
    id 'com.android.application'
}

android {
    compileSdkVersion 28
    buildToolsVersion "30.0.3"

    defaultConfig {
        applicationId "com.example.mytestdemo"
        minSdkVersion 22
        //noinspection ExpiredTargetSdkVersion
        targetSdkVersion 28
        versionCode 13
        versionName "v2.2.13" +
                ""

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation 'androidx.appcompat:appcompat:1.3.1'
    implementation 'com.google.android.material:material:1.4.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.1'
    implementation 'androidx.coordinatorlayout:coordinatorlayout:1.1.0'
    implementation 'androidx.navigation:navigation-fragment:2.3.5'
    implementation 'androidx.navigation:navigation-ui:2.3.5'
    //noinspection GradlePath

    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
    implementation 'io.github.bmob:android-sdk:3.8.8'
    implementation 'io.reactivex.rxjava2:rxjava:2.2.8'
    implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
    implementation 'com.squareup.okhttp3:okhttp:4.8.1'
    implementation 'com.squareup.okio:okio:2.7.0'
    implementation 'com.google.code.gson:gson:2.8.6'
    implementation 'com.google.code.gson:gson:2.8.6'
    implementation 'com.github.bumptech.glide:glide:4.12.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.12.0'

}
```
## 项目Manifest文件
``` 
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    package="com.example.mytestdemo"
    android:versionCode="2"
    android:versionName="2.1.1">

    <uses-permission android:name="android.permission.INTERNET" /> <!-- 获取GSM（2g）、WCDMA（联通3g）等网络状态的信息 -->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> <!-- 获取wifi网络状态的信息 -->
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" /> <!-- 保持CPU 运转，屏幕和键盘灯有可能是关闭的,用于文件上传和下载 -->
    <uses-permission android:name="android.permission.WAKE_LOCK" /> <!-- 获取sd卡写的权限，用于文件上传和下载 -->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /> <!-- 允许读取手机状态 用于创建BmobInstallation -->
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.CALL_PHONE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />


    <application
        android:allowBackup="true"
        android:icon="@drawable/lostandfound128"
        android:label="@string/app_name"
        android:roundIcon="@drawable/lostandfound128"
        android:supportsRtl="true"
        android:theme="@style/Theme.MyTestDemo"
        android:usesCleartextTraffic="true"
        tools:ignore="UnusedAttribute"
        tools:replace="android:theme">
        <activity android:name=".ui.activity.AddDataActivity"></activity>
        <activity android:name=".ui.activity.UpdatePasswordActivity" />
        <activity
            android:name=".ui.activity.Qj315Page"
            android:theme="@android:style/Theme.Wallpaper.NoTitleBar.Fullscreen">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity
            android:name=".ui.activity.DataDetailsActivity"
            android:exported="true"
            android:label="失物详细信息"
            android:launchMode="singleInstance" />
        <activity
            android:name=".MainActivity"
            android:label="失物招领"
            android:launchMode="singleInstance" />
        <activity
            android:name=".ui.activity.RegisterActivity"
            android:launchMode="singleInstance" />
        <activity android:name=".ui.activity.LoginActivity" />

        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="com.example.mytestdemo.provider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/provider" />
        </provider>
        <provider
            android:name="cn.bmob.v3.util.BmobContentProvider"
            android:authorities="com.example.mytestdemo.BmobContentProvider"
            android:exported="true" />
        <provider
            android:name="cn.bmob.v3.util.BmobContentProvider"
            android:authorities="com.example.mytestdemo.ui.fragment.BmobContentProvider"
            android:exported="true"
            tools:ignore="ExportedContentProvider" />

        <activity
            android:name=".ui.activity.PhoneVerifyActivity"
            android:exported="true"
            android:launchMode="singleInstance" />
    </application>

</manifest>
``` 

**项目提交时遇到的问题**

#### fatal: unable to access 'https://github.com/lmy8848/MyTestDemo.git/': OpenSSL SSL_read: Connection was reset, errno 10054
###解决方案 
####有时候可以推送上来，有时候不行 :snowman:
***
 `git config --global --unset http.proxy` 
 `git config --global --unset https.proxy` 
***
