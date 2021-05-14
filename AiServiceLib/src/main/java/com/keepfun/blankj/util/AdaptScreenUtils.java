package com.keepfun.blankj.util;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class AdaptScreenUtils {
    private static List<Field> sMetricsFields;

    private AdaptScreenUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static Resources adaptWidth(Resources resources, int designWidth) {
        float newXdpi = (float)resources.getDisplayMetrics().widthPixels * 72.0F / (float)designWidth;
        applyDisplayMetrics(resources, newXdpi);
        return resources;
    }

    public static Resources adaptHeight(Resources resources, int designHeight) {
        return adaptHeight(resources, designHeight, false);
    }

    public static Resources adaptHeight(Resources resources, int designHeight, boolean includeNavBar) {
        float screenHeight = (float)(resources.getDisplayMetrics().heightPixels + (includeNavBar ? getNavBarHeight(resources) : 0)) * 72.0F;
        float newXdpi = screenHeight / (float)designHeight;
        applyDisplayMetrics(resources, newXdpi);
        return resources;
    }

    private static int getNavBarHeight(Resources resources) {
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resourceId != 0 ? resources.getDimensionPixelSize(resourceId) : 0;
    }

    public static Resources closeAdapt(Resources resources) {
        float newXdpi = Resources.getSystem().getDisplayMetrics().density * 72.0F;
        applyDisplayMetrics(resources, newXdpi);
        return resources;
    }

    public static int pt2Px(float ptValue) {
        DisplayMetrics metrics = Utils.getApp().getResources().getDisplayMetrics();
        return (int)((double)(ptValue * metrics.xdpi / 72.0F) + 0.5D);
    }

    public static int px2Pt(float pxValue) {
        DisplayMetrics metrics = Utils.getApp().getResources().getDisplayMetrics();
        return (int)((double)(pxValue * 72.0F / metrics.xdpi) + 0.5D);
    }

    private static void applyDisplayMetrics(Resources resources, float newXdpi) {
        resources.getDisplayMetrics().xdpi = newXdpi;
        Utils.getApp().getResources().getDisplayMetrics().xdpi = newXdpi;
        applyOtherDisplayMetrics(resources, newXdpi);
    }

    static Runnable getPreLoadRunnable() {
        return new Runnable() {
            public void run() {
                AdaptScreenUtils.preLoad();
            }
        };
    }

    private static void preLoad() {
        applyDisplayMetrics(Resources.getSystem(), Resources.getSystem().getDisplayMetrics().xdpi);
    }

    private static void applyOtherDisplayMetrics(Resources resources, float newXdpi) {
        if (sMetricsFields == null) {
            sMetricsFields = new ArrayList();
            Class resCls = resources.getClass();

            for(Field[] declaredFields = resCls.getDeclaredFields(); declaredFields != null && declaredFields.length > 0; declaredFields = resCls.getDeclaredFields()) {
                Field[] var4 = declaredFields;
                int var5 = declaredFields.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    Field field = var4[var6];
                    if (field.getType().isAssignableFrom(DisplayMetrics.class)) {
                        field.setAccessible(true);
                        DisplayMetrics tmpDm = getMetricsFromField(resources, field);
                        if (tmpDm != null) {
                            sMetricsFields.add(field);
                            tmpDm.xdpi = newXdpi;
                        }
                    }
                }

                resCls = resCls.getSuperclass();
                if (resCls == null) {
                    break;
                }
            }
        } else {
            applyMetricsFields(resources, newXdpi);
        }

    }

    private static void applyMetricsFields(Resources resources, float newXdpi) {
        Iterator var2 = sMetricsFields.iterator();

        while(var2.hasNext()) {
            Field metricsField = (Field)var2.next();

            try {
                DisplayMetrics dm = (DisplayMetrics)metricsField.get(resources);
                if (dm != null) {
                    dm.xdpi = newXdpi;
                }
            } catch (Exception var5) {
                Log.e("AdaptScreenUtils", "applyMetricsFields: " + var5);
            }
        }

    }

    private static DisplayMetrics getMetricsFromField(Resources resources, Field field) {
        try {
            return (DisplayMetrics)field.get(resources);
        } catch (Exception var3) {
            return null;
        }
    }
}
