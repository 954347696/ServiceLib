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
import com.keepfun.blankj.constant.CacheConstants;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public final class CacheDoubleUtils implements CacheConstants {
    private static final Map<String, CacheDoubleUtils> CACHE_MAP = new HashMap();
    private final CacheMemoryUtils mCacheMemoryUtils;
    private final CacheDiskUtils mCacheDiskUtils;

    public static CacheDoubleUtils getInstance() {
        return getInstance(CacheMemoryUtils.getInstance(), CacheDiskUtils.getInstance());
    }

    public static CacheDoubleUtils getInstance(@NonNull CacheMemoryUtils cacheMemoryUtils, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (cacheMemoryUtils == null) {
            throw new NullPointerException("Argument 'cacheMemoryUtils' of type CacheMemoryUtils (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            String cacheKey = cacheDiskUtils.toString() + "_" + cacheMemoryUtils.toString();
            CacheDoubleUtils cache = (CacheDoubleUtils)CACHE_MAP.get(cacheKey);
            if (cache == null) {
                Class var4 = CacheDoubleUtils.class;
                synchronized(CacheDoubleUtils.class) {
                    cache = (CacheDoubleUtils)CACHE_MAP.get(cacheKey);
                    if (cache == null) {
                        cache = new CacheDoubleUtils(cacheMemoryUtils, cacheDiskUtils);
                        CACHE_MAP.put(cacheKey, cache);
                    }
                }
            }

            return cache;
        }
    }

    private CacheDoubleUtils(CacheMemoryUtils cacheMemoryUtils, CacheDiskUtils cacheUtils) {
        this.mCacheMemoryUtils = cacheMemoryUtils;
        this.mCacheDiskUtils = cacheUtils;
    }

    public void put(@NonNull String key, byte[] value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.put(key, (byte[])value, -1);
        }
    }

    public void put(@NonNull String key, byte[] value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.mCacheMemoryUtils.put(key, value, saveTime);
            this.mCacheDiskUtils.put(key, value, saveTime);
        }
    }

    public byte[] getBytes(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return this.getBytes(key, (byte[])null);
        }
    }

    public byte[] getBytes(@NonNull String key, byte[] defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            byte[] obj = (byte[])this.mCacheMemoryUtils.get(key);
            return obj != null ? obj : this.mCacheDiskUtils.getBytes(key, defaultValue);
        }
    }

    public void put(@NonNull String key, String value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.put(key, (String)value, -1);
        }
    }

    public void put(@NonNull String key, String value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.mCacheMemoryUtils.put(key, value, saveTime);
            this.mCacheDiskUtils.put(key, value, saveTime);
        }
    }

    public String getString(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return this.getString(key, (String)null);
        }
    }

    public String getString(@NonNull String key, String defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            String obj = (String)this.mCacheMemoryUtils.get(key);
            return obj != null ? obj : this.mCacheDiskUtils.getString(key, defaultValue);
        }
    }

    public void put(@NonNull String key, JSONObject value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.put(key, (JSONObject)value, -1);
        }
    }

    public void put(@NonNull String key, JSONObject value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.mCacheMemoryUtils.put(key, value, saveTime);
            this.mCacheDiskUtils.put(key, value, saveTime);
        }
    }

    public JSONObject getJSONObject(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return this.getJSONObject(key, (JSONObject)null);
        }
    }

    public JSONObject getJSONObject(@NonNull String key, JSONObject defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            JSONObject obj = (JSONObject)this.mCacheMemoryUtils.get(key);
            return obj != null ? obj : this.mCacheDiskUtils.getJSONObject(key, defaultValue);
        }
    }

    public void put(@NonNull String key, JSONArray value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.put(key, (JSONArray)value, -1);
        }
    }

    public void put(@NonNull String key, JSONArray value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.mCacheMemoryUtils.put(key, value, saveTime);
            this.mCacheDiskUtils.put(key, value, saveTime);
        }
    }

    public JSONArray getJSONArray(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return this.getJSONArray(key, (JSONArray)null);
        }
    }

    public JSONArray getJSONArray(@NonNull String key, JSONArray defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            JSONArray obj = (JSONArray)this.mCacheMemoryUtils.get(key);
            return obj != null ? obj : this.mCacheDiskUtils.getJSONArray(key, defaultValue);
        }
    }

    public void put(@NonNull String key, Bitmap value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.put(key, (Bitmap)value, -1);
        }
    }

    public void put(@NonNull String key, Bitmap value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.mCacheMemoryUtils.put(key, value, saveTime);
            this.mCacheDiskUtils.put(key, value, saveTime);
        }
    }

    public Bitmap getBitmap(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return this.getBitmap(key, (Bitmap)null);
        }
    }

    public Bitmap getBitmap(@NonNull String key, Bitmap defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            Bitmap obj = (Bitmap)this.mCacheMemoryUtils.get(key);
            return obj != null ? obj : this.mCacheDiskUtils.getBitmap(key, defaultValue);
        }
    }

    public void put(@NonNull String key, Drawable value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.put(key, (Drawable)value, -1);
        }
    }

    public void put(@NonNull String key, Drawable value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.mCacheMemoryUtils.put(key, value, saveTime);
            this.mCacheDiskUtils.put(key, value, saveTime);
        }
    }

    public Drawable getDrawable(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return this.getDrawable(key, (Drawable)null);
        }
    }

    public Drawable getDrawable(@NonNull String key, Drawable defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            Drawable obj = (Drawable)this.mCacheMemoryUtils.get(key);
            return obj != null ? obj : this.mCacheDiskUtils.getDrawable(key, defaultValue);
        }
    }

    public void put(@NonNull String key, Parcelable value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.put(key, (Parcelable)value, -1);
        }
    }

    public void put(@NonNull String key, Parcelable value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.mCacheMemoryUtils.put(key, value, saveTime);
            this.mCacheDiskUtils.put(key, value, saveTime);
        }
    }

    public <T> T getParcelable(@NonNull String key, @NonNull Creator<T> creator) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (creator == null) {
            throw new NullPointerException("Argument 'creator' of type Parcelable.Creator<T> (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return (T) this.getParcelable(key, creator, (T)null);
        }
    }

    public <T> T getParcelable(@NonNull String key, @NonNull Creator<T> creator, T defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (creator == null) {
            throw new NullPointerException("Argument 'creator' of type Parcelable.Creator<T> (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            T value = this.mCacheMemoryUtils.get(key);
            return value != null ? value : this.mCacheDiskUtils.getParcelable(key, creator, defaultValue);
        }
    }

    public void put(@NonNull String key, Serializable value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.put(key, (Serializable)value, -1);
        }
    }

    public void put(@NonNull String key, Serializable value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.mCacheMemoryUtils.put(key, value, saveTime);
            this.mCacheDiskUtils.put(key, value, saveTime);
        }
    }

    public Object getSerializable(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return this.getSerializable(key, (Object)null);
        }
    }

    public Object getSerializable(@NonNull String key, Object defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            Object obj = this.mCacheMemoryUtils.get(key);
            return obj != null ? obj : this.mCacheDiskUtils.getSerializable(key, defaultValue);
        }
    }

    public long getCacheDiskSize() {
        return this.mCacheDiskUtils.getCacheSize();
    }

    public int getCacheDiskCount() {
        return this.mCacheDiskUtils.getCacheCount();
    }

    public int getCacheMemoryCount() {
        return this.mCacheMemoryUtils.getCacheCount();
    }

    public void remove(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.mCacheMemoryUtils.remove(key);
            this.mCacheDiskUtils.remove(key);
        }
    }

    public void clear() {
        this.mCacheMemoryUtils.clear();
        this.mCacheDiskUtils.clear();
    }
}
