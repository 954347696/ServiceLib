package com.keepfun.blankj.util;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import androidx.annotation.NonNull;
import androidx.collection.LruCache;

import com.keepfun.blankj.constant.CacheConstants;

import java.util.HashMap;
import java.util.Map;

public final class CacheMemoryUtils implements CacheConstants {
    private static final int DEFAULT_MAX_COUNT = 256;
    private static final Map<String, CacheMemoryUtils> CACHE_MAP = new HashMap();
    private final String mCacheKey;
    private final LruCache<String, CacheMemoryUtils.CacheValue> mMemoryCache;

    public static CacheMemoryUtils getInstance() {
        return getInstance(256);
    }

    public static CacheMemoryUtils getInstance(int maxCount) {
        return getInstance(String.valueOf(maxCount), maxCount);
    }

    public static CacheMemoryUtils getInstance(String cacheKey, int maxCount) {
        CacheMemoryUtils cache = (CacheMemoryUtils) CACHE_MAP.get(cacheKey);
        if (cache == null) {
            Class var3 = CacheMemoryUtils.class;
            synchronized (CacheMemoryUtils.class) {
                cache = (CacheMemoryUtils) CACHE_MAP.get(cacheKey);
                if (cache == null) {
                    cache = new CacheMemoryUtils(cacheKey, new LruCache(maxCount));
                    CACHE_MAP.put(cacheKey, cache);
                }
            }
        }

        return cache;
    }

    private CacheMemoryUtils(String cacheKey, LruCache<String, CacheMemoryUtils.CacheValue> memoryCache) {
        this.mCacheKey = cacheKey;
        this.mMemoryCache = memoryCache;
    }

    @Override
    public String toString() {
        return this.mCacheKey + "@" + Integer.toHexString(this.hashCode());
    }

    public void put(@NonNull String key, Object value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.put(key, value, -1);
        }
    }

    public void put(@NonNull String key, Object value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (value != null) {
            long dueTime = saveTime < 0 ? -1L : System.currentTimeMillis() + (long) (saveTime * 1000);
            this.mMemoryCache.put(key, new CacheMemoryUtils.CacheValue(dueTime, value));
        }
    }

    public <T> T get(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return (T) this.get(key, (Object) null);
        }
    }

    public <T> T get(@NonNull String key, T defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            CacheMemoryUtils.CacheValue val = (CacheMemoryUtils.CacheValue) this.mMemoryCache.get(key);
            if (val == null) {
                return defaultValue;
            } else if (val.dueTime != -1L && val.dueTime < System.currentTimeMillis()) {
                this.mMemoryCache.remove(key);
                return defaultValue;
            } else {
                return (T) val.value;
            }
        }
    }

    public int getCacheCount() {
        return this.mMemoryCache.size();
    }

    public Object remove(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            CacheMemoryUtils.CacheValue remove = (CacheMemoryUtils.CacheValue) this.mMemoryCache.remove(key);
            return remove == null ? null : remove.value;
        }
    }

    public void clear() {
        this.mMemoryCache.evictAll();
    }

    private static final class CacheValue {
        long dueTime;
        Object value;

        CacheValue(long dueTime, Object value) {
            this.dueTime = dueTime;
            this.value = value;
        }
    }
}
