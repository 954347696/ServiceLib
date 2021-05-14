package com.keepfun.aiservice.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import java.util.List;

public class UIUtils {
    /**
     * 屏幕宽度和指定比率计算位置
     *
     * @param ratio 比率
     */
    public static int getPointFromScreenWidthRatio(Context context, float ratio) {
        return (int) (getScreenWidth(context) * ratio);
    }

    /**
     * 判断坐标点是否在屏幕左边
     *
     * @param x 点的x坐标
     */
    public static boolean isScreenLeft(Context context, int x) {
        int halfScreenWidth = getScreenWidth(context) / 2;
        return x <= halfScreenWidth;
    }

    public static int getPointFromScreenHeightRatio(Context context, float ratio) {
        return (int) (getScreenHeight(context) * ratio);
    }


    public static int dp2px(@NonNull Context ctx, int dp) {
        DisplayMetrics displayMetrics = getDisplayMetrics(ctx);
        return (int) (displayMetrics.density * dp + 0.5f);
    }

    public static int px2dp(@NonNull Context ctx, int px) {
        DisplayMetrics displayMetrics = getDisplayMetrics(ctx);
        return (int) (px / displayMetrics.density);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * 在Application onCreate() onConfigurationChanged() 或Activity 的onCreate()中配置
     * xml中长度单位用pt
     *
     * @param ctx
     * @param designWidth 设计图上的宽度
     */
    public static void configPT(@NonNull Context ctx, float designWidth) {
        DisplayMetrics displayMetrics = getDisplayMetrics(ctx);
        displayMetrics.xdpi = displayMetrics.widthPixels / designWidth * 72f;
    }

    public static int pt2px(@NonNull Context ctx, float pt) {
        return (int) (getDisplayMetrics(ctx).xdpi * pt / 72f + 0.5f);
    }

    public static float px2pt(@NonNull Context ctx, int px) {
        return px * 72f / getDisplayMetrics(ctx).xdpi;
    }

    public static boolean hasNavigationBar(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            WindowManager wm = (WindowManager) (ctx.getSystemService(Context.WINDOW_SERVICE));
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(ctx).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !menu && !back;
        }
    }

    /**
     * 判断某个activity是否在前台显示
     */
    public static boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }

    /**
     * 判断某个界面是否在前台,返回true，为显示,否则不是
     */
    public static boolean isForeground(Activity context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }
}
