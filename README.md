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

***
 `fatal: unable to access 'https://github.com/lmy8848/MyTestDemo.git/': OpenSSL SSL_read: Connection was reset, errno 10054`
 ***
###解决方案 
####有时候可以推送上来，有时候不行 :snowman:
***
 `git config --global --unset http.proxy`
***

***
 `git config --global --unset https.proxy` 
***

```
Android系统广播大全

Intent.ACTION_AIRPLANE_MODE_CHANGED;
//关闭或打开飞行模式时的广播

Intent.ACTION_BATTERY_CHANGED;
//充电状态，或者电池的电量发生变化
//电池的充电状态、电荷级别改变，不能通过组建声明接收这个广播，只有通过Context.registerReceiver()注册

Intent.ACTION_BATTERY_LOW;
//表示电池电量低

Intent.ACTION_BATTERY_OKAY;
//表示电池电量充足，即从电池电量低变化到饱满时会发出广播

Intent.ACTION_BOOT_COMPLETED;
//在系统启动完成后，这个动作被广播一次（只有一次）。

Intent.ACTION_CAMERA_BUTTON;
//按下照相时的拍照按键(硬件按键)时发出的广播

Intent.ACTION_CLOSE_SYSTEM_DIALOGS;
//当屏幕超时进行锁屏时,当用户按下电源按钮,长按或短按(不管有没跳出话框)，进行锁屏时,android系统都会广播此Action消息

Intent.ACTION_CONFIGURATION_CHANGED;
//设备当前设置被改变时发出的广播(包括的改变:界面语言，设备方向，等，请参考Configuration.java)

Intent.ACTION_DATE_CHANGED;
//设备日期发生改变时会发出此广播

Intent.ACTION_DEVICE_STORAGE_LOW;
//设备内存不足时发出的广播,此广播只能由系统使用，其它APP不可用？

Intent.ACTION_DEVICE_STORAGE_OK;
//设备内存从不足到充足时发出的广播,此广播只能由系统使用，其它APP不可用？

Intent.ACTION_DOCK_EVENT;
//
//发出此广播的地方frameworks\base\services\java\com\android\server\DockObserver.java

Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE;
移动APP完成之后，发出的广播(移动是指:APP2SD)

Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE;
//正在移动APP时，发出的广播(移动是指:APP2SD)

Intent.ACTION_GTALK_SERVICE_CONNECTED;
//Gtalk已建立连接时发出的广播

Intent.ACTION_GTALK_SERVICE_DISCONNECTED;
//Gtalk已断开连接时发出的广播

Intent.ACTION_HEADSET_PLUG;
//在耳机口上插入耳机时发出的广播

Intent.ACTION_INPUT_METHOD_CHANGED;
//改变输入法时发出的广播

Intent.ACTION_LOCALE_CHANGED;
//设备当前区域设置已更改时发出的广播

Intent.ACTION_MANAGE_PACKAGE_STORAGE;
//

Intent.ACTION_MEDIA_BAD_REMOVAL;
//未正确移除SD卡(正确移除SD卡的方法:设置–SD卡和设备内存–卸载SD卡)，但已把SD卡取出来时发出的广播
//广播：扩展介质（扩展卡）已经从 SD 卡插槽拔出，但是挂载点 (mount point) 还没解除 (unmount)

Intent.ACTION_MEDIA_BUTTON;
//按下”Media Button” 按键时发出的广播,假如有”Media Button” 按键的话(硬件按键)

Intent.ACTION_MEDIA_CHECKING;
//插入外部储存装置，比如SD卡时，系统会检验SD卡，此时发出的广播?

Intent.ACTION_MEDIA_EJECT;
//已拔掉外部大容量储存设备发出的广播（比如SD卡，或移动硬盘）,不管有没有正确卸载都会发出此广播?
//广播：用户想要移除扩展介质（拔掉扩展卡）。

Intent.ACTION_MEDIA_MOUNTED;
//插入SD卡并且已正确安装（识别）时发出的广播
//广播：扩展介质被插入，而且已经被挂载。

Intent.ACTION_MEDIA_NOFS;
//

Intent.ACTION_MEDIA_REMOVED;
//外部储存设备已被移除，不管有没正确卸载,都会发出此广播？
// 广播：扩展介质被移除。

Intent.ACTION_MEDIA_SCANNER_FINISHED;
//广播：已经扫描完介质的一个目录

Intent.ACTION_MEDIA_SCANNER_SCAN_FILE;
//

Intent.ACTION_MEDIA_SCANNER_STARTED;
//广播：开始扫描介质的一个目录

Intent.ACTION_MEDIA_SHARED;
// 广播：扩展介质的挂载被解除 (unmount)，因为它已经作为 USB 大容量存储被共享。

Intent.ACTION_MEDIA_UNMOUNTABLE;
//

Intent.ACTION_MEDIA_UNMOUNTED
// 广播：扩展介质存在，但是还没有被挂载 (mount)。

Intent.ACTION_NEW_OUTGOING_CALL;
//

Intent.ACTION_PACKAGE_ADDED;
//成功的安装APK之后
//广播：设备上新安装了一个应用程序包。
//一个新应用包已经安装在设备上，数据包括包名（最新安装的包程序不能接收到这个广播）

Intent.ACTION_PACKAGE_CHANGED;
//一个已存在的应用程序包已经改变，包括包名

Intent.ACTION_PACKAGE_DATA_CLEARED;
//清除一个应用程序的数据时发出的广播(在设置－－应用管理－－选中某个应用，之后点清除数据时?)
//用户已经清除一个包的数据，包括包名（清除包程序不能接收到这个广播）

Intent.ACTION_PACKAGE_INSTALL;
//触发一个下载并且完成安装时发出的广播，比如在电子市场里下载应用？
//

Intent.ACTION_PACKAGE_REMOVED;
//成功的删除某个APK之后发出的广播
//一个已存在的应用程序包已经从设备上移除，包括包名（正在被安装的包程序不能接收到这个广播）

Intent.ACTION_PACKAGE_REPLACED;
//替换一个现有的安装包时发出的广播（不管现在安装的APP比之前的新还是旧，都会发出此广播？）

Intent.ACTION_PACKAGE_RESTARTED;
//用户重新开始一个包，包的所有进程将被杀死，所有与其联系的运行时间状态应该被移除，包括包名（重新开始包程序不能接收到这个广播）

Intent.ACTION_POWER_CONNECTED;
//插上外部电源时发出的广播

Intent.ACTION_POWER_DISCONNECTED;
//已断开外部电源连接时发出的广播

Intent.ACTION_PROVIDER_CHANGED;
//

Intent.ACTION_REBOOT;
//重启设备时的广播

Intent.ACTION_SCREEN_OFF;
//屏幕被关闭之后的广播

Intent.ACTION_SCREEN_ON;
//屏幕被打开之后的广播

Intent.ACTION_SHUTDOWN;
//关闭系统时发出的广播

Intent.ACTION_TIMEZONE_CHANGED;
//时区发生改变时发出的广播

Intent.ACTION_TIME_CHANGED;
//时间被设置时发出的广播

Intent.ACTION_TIME_TICK;
//广播：当前时间已经变化（正常的时间流逝）。
//当前时间改变，每分钟都发送，不能通过组件声明来接收，只有通过Context.registerReceiver()方法来注册

Intent.ACTION_UID_REMOVED;
//一个用户ID已经从系统中移除发出的广播
//

Intent.ACTION_UMS_CONNECTED;
//设备已进入USB大容量储存状态时发出的广播？

Intent.ACTION_UMS_DISCONNECTED;
//设备已从USB大容量储存状态转为正常状态时发出的广播？

Intent.ACTION_USER_PRESENT;
//

Intent.ACTION_WALLPAPER_CHANGED;
//设备墙纸已改变时发出的广播
maven阿里云maven仓库
//        maven { url "https://maven.google.com"}
         maven { url 'http://maven.aliyun.com/nexus/content/groups/public/'}
         maven { url 'http://maven.aliyun.com/nexus/content/repositories/jcenter'}
         maven { url 'http://maven.aliyun.com/nexus/content/repositories/google'}
         maven { url 'http://maven.aliyun.com/nexus/content/repositories/gradle-plugin'}

```
 
---