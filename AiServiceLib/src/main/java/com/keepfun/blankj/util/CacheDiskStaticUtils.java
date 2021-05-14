package com.keepfun.blankj.util;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import androidx.annotation.NonNull;
import java.io.Serializable;
import org.json.JSONArray;
import org.json.JSONObject;

public final class CacheDiskStaticUtils {
    private static CacheDiskUtils sDefaultCacheDiskUtils;

    public CacheDiskStaticUtils() {
    }

    public static void setDefaultCacheDiskUtils(CacheDiskUtils cacheDiskUtils) {
        sDefaultCacheDiskUtils = cacheDiskUtils;
    }

    public static void put(@NonNull String key, byte[] value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            put(key, value, getDefaultCacheDiskUtils());
        }
    }

    public static void put(@NonNull String key, byte[] value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            put(key, value, saveTime, getDefaultCacheDiskUtils());
        }
    }

    public static byte[] getBytes(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return getBytes(key, getDefaultCacheDiskUtils());
        }
    }

    public static byte[] getBytes(@NonNull String key, byte[] defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return getBytes(key, defaultValue, getDefaultCacheDiskUtils());
        }
    }

    public static void put(@NonNull String key, String value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            put(key, value, getDefaultCacheDiskUtils());
        }
    }

    public static void put(@NonNull String key, String value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            put(key, value, saveTime, getDefaultCacheDiskUtils());
        }
    }

    public static String getString(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return getString(key, getDefaultCacheDiskUtils());
        }
    }

    public static String getString(@NonNull String key, String defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return getString(key, defaultValue, getDefaultCacheDiskUtils());
        }
    }

    public static void put(@NonNull String key, JSONObject value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            put(key, value, getDefaultCacheDiskUtils());
        }
    }

    public static void put(@NonNull String key, JSONObject value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            put(key, value, saveTime, getDefaultCacheDiskUtils());
        }
    }

    public static JSONObject getJSONObject(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return getJSONObject(key, getDefaultCacheDiskUtils());
        }
    }

    public static JSONObject getJSONObject(@NonNull String key, JSONObject defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return getJSONObject(key, defaultValue, getDefaultCacheDiskUtils());
        }
    }

    public static void put(@NonNull String key, JSONArray value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            put(key, value, getDefaultCacheDiskUtils());
        }
    }

    public static void put(@NonNull String key, JSONArray value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            put(key, value, saveTime, getDefaultCacheDiskUtils());
        }
    }

    public static JSONArray getJSONArray(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return getJSONArray(key, getDefaultCacheDiskUtils());
        }
    }

    public static JSONArray getJSONArray(@NonNull String key, JSONArray defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return getJSONArray(key, defaultValue, getDefaultCacheDiskUtils());
        }
    }

    public static void put(@NonNull String key, Bitmap value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            put(key, value, getDefaultCacheDiskUtils());
        }
    }

    public static void put(@NonNull String key, Bitmap value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            put(key, value, saveTime, getDefaultCacheDiskUtils());
        }
    }

    public static Bitmap getBitmap(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return getBitmap(key, getDefaultCacheDiskUtils());
        }
    }

    public static Bitmap getBitmap(@NonNull String key, Bitmap defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return getBitmap(key, defaultValue, getDefaultCacheDiskUtils());
        }
    }

    public static void put(@NonNull String key, Drawable value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            put(key, value, getDefaultCacheDiskUtils());
        }
    }

    public static void put(@NonNull String key, Drawable value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            put(key, value, saveTime, getDefaultCacheDiskUtils());
        }
    }

    public static Drawable getDrawable(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return getDrawable(key, getDefaultCacheDiskUtils());
        }
    }

    public static Drawable getDrawable(@NonNull String key, Drawable defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return getDrawable(key, defaultValue, getDefaultCacheDiskUtils());
        }
    }

    public static void put(@NonNull String key, Parcelable value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            put(key, value, getDefaultCacheDiskUtils());
        }
    }

    public static void put(@NonNull String key, Parcelable value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            put(key, value, saveTime, getDefaultCacheDiskUtils());
        }
    }

    public static <T> T getParcelable(@NonNull String key, @NonNull Creator<T> creator) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (creator == null) {
            throw new NullPointerException("Argument 'creator' of type Parcelable.Creator<T> (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return getParcelable(key, creator, getDefaultCacheDiskUtils());
        }
    }

    public static <T> T getParcelable(@NonNull String key, @NonNull Creator<T> creator, T defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (creator == null) {
            throw new NullPointerException("Argument 'creator' of type Parcelable.Creator<T> (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return getParcelable(key, creator, defaultValue, getDefaultCacheDiskUtils());
        }
    }

    public static void put(@NonNull String key, Serializable value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            put(key, value, getDefaultCacheDiskUtils());
        }
    }

    public static void put(@NonNull String key, Serializable value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            put(key, value, saveTime, getDefaultCacheDiskUtils());
        }
    }

    public static Object getSerializable(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return getSerializable(key, getDefaultCacheDiskUtils());
        }
    }

    public static Object getSerializable(@NonNull String key, Object defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return getSerializable(key, defaultValue, getDefaultCacheDiskUtils());
        }
    }

    public static long getCacheSize() {
        return getCacheSize(getDefaultCacheDiskUtils());
    }

    public static int getCacheCount() {
        return getCacheCount(getDefaultCacheDiskUtils());
    }

    public static boolean remove(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return remove(key, getDefaultCacheDiskUtils());
        }
    }

    public static boolean clear() {
        return clear(getDefaultCacheDiskUtils());
    }

    public static void put(@NonNull String key, byte[] value, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            cacheDiskUtils.put(key, value);
        }
    }

    public static void put(@NonNull String key, byte[] value, int saveTime, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            cacheDiskUtils.put(key, value, saveTime);
        }
    }

    public static byte[] getBytes(@NonNull String key, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheDiskUtils.getBytes(key);
        }
    }

    public static byte[] getBytes(@NonNull String key, byte[] defaultValue, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheDiskUtils.getBytes(key, defaultValue);
        }
    }

    public static void put(@NonNull String key, String value, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            cacheDiskUtils.put(key, value);
        }
    }

    public static void put(@NonNull String key, String value, int saveTime, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            cacheDiskUtils.put(key, value, saveTime);
        }
    }

    public static String getString(@NonNull String key, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheDiskUtils.getString(key);
        }
    }

    public static String getString(@NonNull String key, String defaultValue, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheDiskUtils.getString(key, defaultValue);
        }
    }

    public static void put(@NonNull String key, JSONObject value, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            cacheDiskUtils.put(key, value);
        }
    }

    public static void put(@NonNull String key, JSONObject value, int saveTime, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            cacheDiskUtils.put(key, value, saveTime);
        }
    }

    public static JSONObject getJSONObject(@NonNull String key, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheDiskUtils.getJSONObject(key);
        }
    }

    public static JSONObject getJSONObject(@NonNull String key, JSONObject defaultValue, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheDiskUtils.getJSONObject(key, defaultValue);
        }
    }

    public static void put(@NonNull String key, JSONArray value, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            cacheDiskUtils.put(key, value);
        }
    }

    public static void put(@NonNull String key, JSONArray value, int saveTime, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            cacheDiskUtils.put(key, value, saveTime);
        }
    }

    public static JSONArray getJSONArray(@NonNull String key, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheDiskUtils.getJSONArray(key);
        }
    }

    public static JSONArray getJSONArray(@NonNull String key, JSONArray defaultValue, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheDiskUtils.getJSONArray(key, defaultValue);
        }
    }

    public static void put(@NonNull String key, Bitmap value, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            cacheDiskUtils.put(key, value);
        }
    }

    public static void put(@NonNull String key, Bitmap value, int saveTime, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            cacheDiskUtils.put(key, value, saveTime);
        }
    }

    public static Bitmap getBitmap(@NonNull String key, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheDiskUtils.getBitmap(key);
        }
    }

    public static Bitmap getBitmap(@NonNull String key, Bitmap defaultValue, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheDiskUtils.getBitmap(key, defaultValue);
        }
    }

    public static void put(@NonNull String key, Drawable value, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            cacheDiskUtils.put(key, value);
        }
    }

    public static void put(@NonNull String key, Drawable value, int saveTime, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            cacheDiskUtils.put(key, value, saveTime);
        }
    }

    public static Drawable getDrawable(@NonNull String key, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheDiskUtils.getDrawable(key);
        }
    }

    public static Drawable getDrawable(@NonNull String key, Drawable defaultValue, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheDiskUtils.getDrawable(key, defaultValue);
        }
    }

    public static void put(@NonNull String key, Parcelable value, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            cacheDiskUtils.put(key, value);
        }
    }

    public static void put(@NonNull String key, Parcelable value, int saveTime, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            cacheDiskUtils.put(key, value, saveTime);
        }
    }

    public static <T> T getParcelable(@NonNull String key, @NonNull Creator<T> creator, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (creator == null) {
            throw new NullPointerException("Argument 'creator' of type Parcelable.Creator<T> (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheDiskUtils.getParcelable(key, creator);
        }
    }

    public static <T> T getParcelable(@NonNull String key, @NonNull Creator<T> creator, T defaultValue, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (creator == null) {
            throw new NullPointerException("Argument 'creator' of type Parcelable.Creator<T> (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheDiskUtils.getParcelable(key, creator, defaultValue);
        }
    }

    public static void put(@NonNull String key, Serializable value, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            cacheDiskUtils.put(key, value);
        }
    }

    public static void put(@NonNull String key, Serializable value, int saveTime, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            cacheDiskUtils.put(key, value, saveTime);
        }
    }

    public static Object getSerializable(@NonNull String key, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheDiskUtils.getSerializable(key);
        }
    }

    public static Object getSerializable(@NonNull String key, Object defaultValue, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheDiskUtils.getSerializable(key, defaultValue);
        }
    }

    public static long getCacheSize(@NonNull CacheDiskUtils cacheDiskUtils) {
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheDiskUtils.getCacheSize();
        }
    }

    public static int getCacheCount(@NonNull CacheDiskUtils cacheDiskUtils) {
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheDiskUtils.getCacheCount();
        }
    }

    public static boolean remove(@NonNull String key, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheDiskUtils.remove(key);
        }
    }

    public static boolean clear(@NonNull CacheDiskUtils cacheDiskUtils) {
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheDiskUtils.clear();
        }
    }

    private static CacheDiskUtils getDefaultCacheDiskUtils() {
        return sDefaultCacheDiskUtils != null ? sDefaultCacheDiskUtils : CacheDiskUtils.getInstance();
    }
}
