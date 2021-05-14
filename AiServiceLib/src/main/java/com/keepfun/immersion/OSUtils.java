//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.keepfun.immersion;

import android.text.TextUtils;
import java.lang.reflect.Method;

public class OSUtils {
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_EMUI_VERSION_NAME = "ro.build.version.emui";
    private static final String KEY_DISPLAY = "ro.build.display.id";

    public OSUtils() {
    }

    public static boolean isMIUI() {
        String property = getSystemProperty("ro.miui.ui.version.name", "");
        return !TextUtils.isEmpty(property);
    }

    public static boolean isMIUI6Later() {
        String version = getMIUIVersion();
        if (!version.isEmpty()) {
            try {
                int num = Integer.valueOf(version.substring(1));
                return num >= 6;
            } catch (NumberFormatException var3) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String getMIUIVersion() {
        return isMIUI() ? getSystemProperty("ro.miui.ui.version.name", "") : "";
    }

    public static boolean isEMUI() {
        String property = getSystemProperty("ro.build.version.emui", "");
        return !TextUtils.isEmpty(property);
    }

    public static String getEMUIVersion() {
        return isEMUI() ? getSystemProperty("ro.build.version.emui", "") : "";
    }

    public static boolean isEMUI3_1() {
        String property = getEMUIVersion();
        return "EmotionUI 3".equals(property) || property.contains("EmotionUI_3.1");
    }

    public static boolean isEMUI3_0() {
        String property = getEMUIVersion();
        return property.contains("EmotionUI_3.0");
    }

    public static boolean isEMUI3_x() {
        return isEMUI3_0() || isEMUI3_1();
    }

    public static boolean isFlymeOS() {
        return getFlymeOSFlag().toLowerCase().contains("flyme");
    }

    public static boolean isFlymeOS4Later() {
        String version = getFlymeOSVersion();
        if (!version.isEmpty()) {
            try {
                int num;
                if (version.toLowerCase().contains("os")) {
                    num = Integer.valueOf(version.substring(9, 10));
                } else {
                    num = Integer.valueOf(version.substring(6, 7));
                }

                return num >= 4;
            } catch (NumberFormatException var3) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isFlymeOS5() {
        String version = getFlymeOSVersion();
        if (!version.isEmpty()) {
            try {
                int num;
                if (version.toLowerCase().contains("os")) {
                    num = Integer.valueOf(version.substring(9, 10));
                } else {
                    num = Integer.valueOf(version.substring(6, 7));
                }

                return num == 5;
            } catch (NumberFormatException var3) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String getFlymeOSVersion() {
        return isFlymeOS() ? getSystemProperty("ro.build.display.id", "") : "";
    }

    private static String getFlymeOSFlag() {
        return getSystemProperty("ro.build.display.id", "");
    }

    private static String getSystemProperty(String key, String defaultValue) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method method = clz.getMethod("get", String.class, String.class);
            return (String)method.invoke(clz, key, defaultValue);
        } catch (Exception var4) {
            var4.printStackTrace();
            return defaultValue;
        }
    }
}
