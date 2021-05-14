package com.keepfun.blankj.util;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

public final class BrightnessUtils {
    private BrightnessUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean isAutoBrightnessEnabled() {
        try {
            int mode = System.getInt(Utils.getApp().getContentResolver(), "screen_brightness_mode");
            return mode == 1;
        } catch (SettingNotFoundException var1) {
            var1.printStackTrace();
            return false;
        }
    }

    public static boolean setAutoBrightnessEnabled(boolean enabled) {
        return System.putInt(Utils.getApp().getContentResolver(), "screen_brightness_mode", enabled ? 1 : 0);
    }

    public static int getBrightness() {
        try {
            return System.getInt(Utils.getApp().getContentResolver(), "screen_brightness");
        } catch (SettingNotFoundException var1) {
            var1.printStackTrace();
            return 0;
        }
    }

    public static boolean setBrightness(@IntRange(from = 0L,to = 255L) int brightness) {
        ContentResolver resolver = Utils.getApp().getContentResolver();
        boolean b = System.putInt(resolver, "screen_brightness", brightness);
        resolver.notifyChange(System.getUriFor("screen_brightness"), (ContentObserver)null);
        return b;
    }

    public static void setWindowBrightness(@NonNull Window window, @IntRange(from = 0L,to = 255L) int brightness) {
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            LayoutParams lp = window.getAttributes();
            lp.screenBrightness = (float)brightness / 255.0F;
            window.setAttributes(lp);
        }
    }

    public static int getWindowBrightness(Window window) {
        LayoutParams lp = window.getAttributes();
        float brightness = lp.screenBrightness;
        return brightness < 0.0F ? getBrightness() : (int)(brightness * 255.0F);
    }
}
