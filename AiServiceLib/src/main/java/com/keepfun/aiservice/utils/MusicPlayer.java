package com.keepfun.aiservice.utils;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.text.TextUtils;


import com.keepfun.blankj.util.LogUtils;

import java.io.IOException;

/**
 * Created by zxf on 2018/8/22.
 */
public class MusicPlayer {
    private static final String TAG = MusicPlayer.class.getSimpleName();
    private MediaPlayer mediaPlayer;
    private int duration;
    private MusicPlayerListener musicPlayerListener;
    private boolean isPrepared;
    private String url;
    private int index;

    public interface MusicPlayerListener {
        void onPrepared(int duration);

        void onCompletion();

        void onError(int what);
    }

    public MusicPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    public boolean isLooping() {
        return mediaPlayer.isLooping();
    }

    public void setLooping(boolean looping) {
        mediaPlayer.setLooping(looping);
    }

    public int getDuration() {
        return duration;
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void setMusicPlayerListener(MusicPlayerListener musicPlayerListener) {
        this.musicPlayerListener = musicPlayerListener;
    }

    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            duration = mp.getDuration();
            isPrepared = true;
            LogUtils.i(TAG, "onPrepared:" + duration);
            if (musicPlayerListener != null) {
                musicPlayerListener.onPrepared(duration);
            }
            play();
        }
    };

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            LogUtils.i(TAG, "onCompletion");
            if (musicPlayerListener != null) {
                musicPlayerListener.onCompletion();
            }
        }
    };

    private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            LogUtils.e(TAG, "player failed onError:" + what + " extra:" + extra);
            if (musicPlayerListener != null) {
                musicPlayerListener.onError(what);
            }
            return false;
        }
    };

    public void prepare(AssetFileDescriptor fd) {
        LogUtils.d(TAG, "------prepare------" + url);
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            isPrepared = false;
            mediaPlayer.reset();
            mediaPlayer.setDataSource(fd);
            mediaPlayer.setLooping(false);//是否循环播放
            mediaPlayer.prepareAsync();//网络视频，异步
            mediaPlayer.setOnPreparedListener(onPreparedListener);
            mediaPlayer.setOnCompletionListener(onCompletionListener);
            mediaPlayer.setOnErrorListener(onErrorListener);
        } catch (IOException e) {
            LogUtils.e(TAG, "prepare", e);
            if (musicPlayerListener != null) {
                musicPlayerListener.onError(0);
            }
        }
    }

    public void prepare(String url) {
        LogUtils.d(TAG, "------prepare------" + url);
        this.url = url;
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            isPrepared = false;
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.setLooping(false);//是否循环播放
            mediaPlayer.prepareAsync();//网络视频，异步
            mediaPlayer.setOnPreparedListener(onPreparedListener);
            mediaPlayer.setOnCompletionListener(onCompletionListener);
            mediaPlayer.setOnErrorListener(onErrorListener);
        } catch (IOException e) {
            LogUtils.e(TAG, "prepare", e);
            if (musicPlayerListener != null) {
                musicPlayerListener.onError(0);
            }
        }
    }


    public boolean isSameCurrent(String newUrl) {
        return TextUtils.equals(url, newUrl);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getCurrentPlayUrl() {
        return url;
    }

    public void play() {
        LogUtils.d(TAG, "------play------");
        if (isPrepared && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void pause() {
        LogUtils.d(TAG, "------pause------");
        mediaPlayer.pause();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public void stop() {
        LogUtils.d(TAG, "------stop------");
        isPrepared = false;
        mediaPlayer.stop();
    }

    public void seekTo(int msec) {
        mediaPlayer.seekTo(msec);
    }

    public void reset() {
        LogUtils.d(TAG, "------reset------");
        isPrepared = false;
        mediaPlayer.reset();
    }

    public void release() {
        LogUtils.d(TAG, "------music player release------");
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        isPrepared = false;
    }
}
