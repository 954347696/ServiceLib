package com.keepfun.aiservice.utils;

import android.content.res.AssetFileDescriptor;

import com.keepfun.blankj.util.AppUtils;
import com.keepfun.blankj.util.Utils;

import java.io.IOException;

/**
 * @author yang
 * @description
 * @date 2021/2/22 2:42 PM
 */
public class RingUtils {
    private static MusicPlayer musicPlayer;

    public static void startRing() {
        if (musicPlayer == null) {
            musicPlayer = new MusicPlayer();
        }
        try {
            AssetFileDescriptor fd = Utils.getApp().getAssets().openFd("ring.mp3");
            musicPlayer.prepare(fd);
            musicPlayer.setMusicPlayerListener(new MusicPlayer.MusicPlayerListener() {
                @Override
                public void onPrepared(int duration) {

                }

                @Override
                public void onCompletion() {
                    musicPlayer.reset();
                    musicPlayer.prepare(fd);
                }

                @Override
                public void onError(int what) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void release() {
        if (musicPlayer != null) {
            musicPlayer.release();
        }
    }
}
