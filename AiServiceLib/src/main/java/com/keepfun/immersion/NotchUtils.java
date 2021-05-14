//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.keepfun.immersion;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NotchUtils {
    private static final String SYSTEM_PROPERTIES = "android.os.SystemProperties";
    private static final String NOTCH_XIAO_MI = "ro.miui.notch";
    private static final String NOTCH_HUA_WEI = "com.huawei.android.util.HwNotchSizeUtil";
    private static final String NOTCH_VIVO = "android.util.FtFeature";
    private static final String NOTCH_OPPO = "com.oppo.feature.screen.heteromorphism";

    public NotchUtils() {
    }

    public static boolean hasNotchScreen(Activity activity) {
        return activity != null && (hasNotchAtXiaoMi(activity) || hasNotchAtHuaWei(activity) || hasNotchAtOPPO(activity) || hasNotchAtVIVO(activity) || hasNotchAtAndroidP(activity));
    }

    public static boolean hasNotchScreen(View view) {
        return view != null && (hasNotchAtXiaoMi(view.getContext()) || hasNotchAtHuaWei(view.getContext()) || hasNotchAtOPPO(view.getContext()) || hasNotchAtVIVO(view.getContext()) || hasNotchAtAndroidP(view));
    }

    private static boolean hasNotchAtAndroidP(View view) {
        return getDisplayCutout(view) != null;
    }

    private static boolean hasNotchAtAndroidP(Activity activity) {
        return getDisplayCutout(activity) != null;
    }

    private static DisplayCutout getDisplayCutout(Activity activity) {
        if (VERSION.SDK_INT >= 28 && activity != null) {
            Window window = activity.getWindow();
            if (window != null) {
                WindowInsets windowInsets = window.getDecorView().getRootWindowInsets();
                if (windowInsets != null) {
                    return windowInsets.getDisplayCutout();
                }
            }
        }

        return null;
    }

    private static DisplayCutout getDisplayCutout(View view) {
        if (VERSION.SDK_INT >= 28 && view != null) {
            WindowInsets windowInsets = view.getRootWindowInsets();
            if (windowInsets != null) {
                return windowInsets.getDisplayCutout();
            }
        }

        return null;
    }

    private static boolean hasNotchAtXiaoMi(Context context) {
        int result = 0;
        if ("Xiaomi".equals(Build.MANUFACTURER)) {
            try {
                ClassLoader classLoader = context.getClassLoader();
                Class<?> aClass = classLoader.loadClass("android.os.SystemProperties");
                Method method = aClass.getMethod("getInt", String.class, Integer.TYPE);
                result = (Integer)method.invoke(aClass, "ro.miui.notch", 0);
            } catch (NoSuchMethodException var5) {
            } catch (IllegalAccessException var6) {
            } catch (InvocationTargetException var7) {
            } catch (ClassNotFoundException var8) {
            }
        }

        return result == 1;
    }

    private static boolean hasNotchAtHuaWei(Context context) {
        boolean result = false;

        try {
            ClassLoader classLoader = context.getClassLoader();
            Class<?> aClass = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = aClass.getMethod("hasNotchInScreen");
            result = (Boolean)get.invoke(aClass);
        } catch (ClassNotFoundException var5) {
        } catch (NoSuchMethodException var6) {
        } catch (Exception var7) {
        }

        return result;
    }

    private static boolean hasNotchAtVIVO(Context context) {
        boolean result = false;

        try {
            ClassLoader classLoader = context.getClassLoader();
            Class<?> aClass = classLoader.loadClass("android.util.FtFeature");
            Method method = aClass.getMethod("isFeatureSupport", Integer.TYPE);
            result = (Boolean)method.invoke(aClass, 32);
        } catch (ClassNotFoundException var5) {
        } catch (NoSuchMethodException var6) {
        } catch (Exception var7) {
        }

        return result;
    }

    private static boolean hasNotchAtOPPO(Context context) {
        try {
            return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
        } catch (Exception var2) {
            return false;
        }
    }
}
