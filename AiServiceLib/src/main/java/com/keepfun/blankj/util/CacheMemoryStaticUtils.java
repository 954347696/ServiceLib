package com.keepfun.blankj.util;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import androidx.annotation.NonNull;

public final class CacheMemoryStaticUtils {
    private static CacheMemoryUtils sDefaultCacheMemoryUtils;

    public CacheMemoryStaticUtils() {
    }

    public static void setDefaultCacheMemoryUtils(CacheMemoryUtils cacheMemoryUtils) {
        sDefaultCacheMemoryUtils = cacheMemoryUtils;
    }

    public static void put(@NonNull String key, Object value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            put(key, value, getDefaultCacheMemoryUtils());
        }
    }

    public static void put(@NonNull String key, Object value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            put(key, value, saveTime, getDefaultCacheMemoryUtils());
        }
    }

    public static <T> T get(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return get(key, getDefaultCacheMemoryUtils());
        }
    }

    public static <T> T get(@NonNull String key, T defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return get(key, defaultValue, getDefaultCacheMemoryUtils());
        }
    }

    public static int getCacheCount() {
        return getCacheCount(getDefaultCacheMemoryUtils());
    }

    public static Object remove(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return remove(key, getDefaultCacheMemoryUtils());
        }
    }

    public static void clear() {
        clear(getDefaultCacheMemoryUtils());
    }

    public static void put(@NonNull String key, Object value, @NonNull CacheMemoryUtils cacheMemoryUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheMemoryUtils == null) {
            throw new NullPointerException("Argument 'cacheMemoryUtils' of type CacheMemoryUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            cacheMemoryUtils.put(key, value);
        }
    }

    public static void put(@NonNull String key, Object value, int saveTime, @NonNull CacheMemoryUtils cacheMemoryUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheMemoryUtils == null) {
            throw new NullPointerException("Argument 'cacheMemoryUtils' of type CacheMemoryUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            cacheMemoryUtils.put(key, value, saveTime);
        }
    }

    public static <T> T get(@NonNull String key, @NonNull CacheMemoryUtils cacheMemoryUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheMemoryUtils == null) {
            throw new NullPointerException("Argument 'cacheMemoryUtils' of type CacheMemoryUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheMemoryUtils.get(key);
        }
    }

    public static <T> T get(@NonNull String key, T defaultValue, @NonNull CacheMemoryUtils cacheMemoryUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheMemoryUtils == null) {
            throw new NullPointerException("Argument 'cacheMemoryUtils' of type CacheMemoryUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheMemoryUtils.get(key, defaultValue);
        }
    }

    public static int getCacheCount(@NonNull CacheMemoryUtils cacheMemoryUtils) {
        if (cacheMemoryUtils == null) {
            throw new NullPointerException("Argument 'cacheMemoryUtils' of type CacheMemoryUtils (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheMemoryUtils.getCacheCount();
        }
    }

    public static Object remove(@NonNull String key, @NonNull CacheMemoryUtils cacheMemoryUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cacheMemoryUtils == null) {
            throw new NullPointerException("Argument 'cacheMemoryUtils' of type CacheMemoryUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return cacheMemoryUtils.remove(key);
        }
    }

    public static void clear(@NonNull CacheMemoryUtils cacheMemoryUtils) {
        if (cacheMemoryUtils == null) {
            throw new NullPointerException("Argument 'cacheMemoryUtils' of type CacheMemoryUtils (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            cacheMemoryUtils.clear();
        }
    }

    private static CacheMemoryUtils getDefaultCacheMemoryUtils() {
        return sDefaultCacheMemoryUtils != null ? sDefaultCacheMemoryUtils : CacheMemoryUtils.getInstance();
    }
}
