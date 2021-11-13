package com.example.mytestdemo.utils;

import android.media.MediaPlayer;
import android.util.Log;

import com.example.mytestdemo.bean.Song;

import java.io.IOException;
import java.util.List;

/**
 * *********     Github https://github.com/lmy8848
 * *********     @author 麒玖网络QJ315 (NJQ-PRO)
 * *********     @address     NJQ-PC
 * *********     @time 2021/11/4 18:42
 */
public class PlayerMusic {
    private static MediaPlayer mediaPlayer;
    private static List<Song> urllist;
    private static int position;


    public static void setUrllist(List<Song> urllist) {
        PlayerMusic.urllist = urllist;
    }

    public static void player(String musicpath) {
        mediaPlayer = new MediaPlayer();
        try {
            //        重置音频文件，防止多次点击会报错
            mediaPlayer.reset();
            //        调用方法传进播放地址
            mediaPlayer.setDataSource(musicpath);
            //            异步准备资源，防止卡顿
            mediaPlayer.prepareAsync();
            //            调用音频的监听方法，音频准备完毕后响应该方法进行音乐播放
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();

                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ListPlayMusic(int i) {
        position = i;
        mediaPlayer = new MediaPlayer();
        try {
            //        重置音频文件，防止多次点击会报错
            mediaPlayer.reset();
            //        调用方法传进播放地址
            mediaPlayer.setDataSource(urllist.get(i).path);

            //            异步准备资源，防止卡顿
            mediaPlayer.prepareAsync();
            //            调用音频的监听方法，音频准备完毕后响应该方法进行音乐播放
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {

                    mediaPlayer.start();

                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (position <= urllist.size() - 1) {
                        ListPlayMusic(++position);
                    }else {
                        Stop();
                        Reset();
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Boolean IsPlayer() {
        if (mediaPlayer != null) {

            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.release();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("TAG", "run: " + e.getMessage());
            }

        }
        return false;
    }

    public static void Reset() {
        mediaPlayer.release();
    }

    public static void Stop() {
        mediaPlayer.stop();
    }

    public static Boolean IsPlayed() {
        if (mediaPlayer != null) {

            try {
                if (mediaPlayer.isPlaying()) {
                    return true;
                } else {
                    return false;
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("TAG", "run: " + e.getMessage());
                return false;
            }

        } else {
            return false;
        }

    }

    private long getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }


}
