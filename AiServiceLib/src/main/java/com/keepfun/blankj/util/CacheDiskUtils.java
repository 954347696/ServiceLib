package com.keepfun.blankj.util;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

import androidx.annotation.NonNull;

import com.keepfun.blankj.constant.CacheConstants;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONArray;
import org.json.JSONObject;

public final class CacheDiskUtils implements CacheConstants {
    private static final long DEFAULT_MAX_SIZE = 9223372036854775807L;
    private static final int DEFAULT_MAX_COUNT = 2147483647;
    private static final String CACHE_PREFIX = "cdu_";
    private static final String TYPE_BYTE = "by_";
    private static final String TYPE_STRING = "st_";
    private static final String TYPE_JSON_OBJECT = "jo_";
    private static final String TYPE_JSON_ARRAY = "ja_";
    private static final String TYPE_BITMAP = "bi_";
    private static final String TYPE_DRAWABLE = "dr_";
    private static final String TYPE_PARCELABLE = "pa_";
    private static final String TYPE_SERIALIZABLE = "se_";
    private static final Map<String, CacheDiskUtils> CACHE_MAP = new HashMap();
    private final String mCacheKey;
    private final File mCacheDir;
    private final long mMaxSize;
    private final int mMaxCount;
    private CacheDiskUtils.DiskCacheManager mDiskCacheManager;

    public static CacheDiskUtils getInstance() {
        return getInstance("", 9223372036854775807L, 2147483647);
    }

    public static CacheDiskUtils getInstance(String cacheName) {
        return getInstance(cacheName, 9223372036854775807L, 2147483647);
    }

    public static CacheDiskUtils getInstance(long maxSize, int maxCount) {
        return getInstance("", maxSize, maxCount);
    }

    public static CacheDiskUtils getInstance(String cacheName, long maxSize, int maxCount) {
        if (UtilsBridge.isSpace(cacheName)) {
            cacheName = "cacheUtils";
        }

        File file = new File(Utils.getApp().getCacheDir(), cacheName);
        return getInstance(file, maxSize, maxCount);
    }

    public static CacheDiskUtils getInstance(@NonNull File cacheDir) {
        if (cacheDir == null) {
            throw new NullPointerException("Argument 'cacheDir' of type File (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return getInstance(cacheDir, 9223372036854775807L, 2147483647);
        }
    }

    public static CacheDiskUtils getInstance(@NonNull File cacheDir, long maxSize, int maxCount) {
        if (cacheDir == null) {
            throw new NullPointerException("Argument 'cacheDir' of type File (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            String cacheKey = cacheDir.getAbsoluteFile() + "_" + maxSize + "_" + maxCount;
            CacheDiskUtils cache = (CacheDiskUtils) CACHE_MAP.get(cacheKey);
            if (cache == null) {
                Class var6 = CacheDiskUtils.class;
                synchronized (CacheDiskUtils.class) {
                    cache = (CacheDiskUtils) CACHE_MAP.get(cacheKey);
                    if (cache == null) {
                        cache = new CacheDiskUtils(cacheKey, cacheDir, maxSize, maxCount);
                        CACHE_MAP.put(cacheKey, cache);
                    }
                }
            }

            return cache;
        }
    }

    private CacheDiskUtils(String cacheKey, File cacheDir, long maxSize, int maxCount) {
        this.mCacheKey = cacheKey;
        this.mCacheDir = cacheDir;
        this.mMaxSize = maxSize;
        this.mMaxCount = maxCount;
    }

    private CacheDiskUtils.DiskCacheManager getDiskCacheManager() {
        if (this.mCacheDir.exists()) {
            if (this.mDiskCacheManager == null) {
                this.mDiskCacheManager = new CacheDiskUtils.DiskCacheManager(this.mCacheDir, this.mMaxSize, this.mMaxCount);
            }
        } else if (this.mCacheDir.mkdirs()) {
            this.mDiskCacheManager = new CacheDiskUtils.DiskCacheManager(this.mCacheDir, this.mMaxSize, this.mMaxCount);
        } else {
            Log.e("CacheDiskUtils", "can't make dirs in " + this.mCacheDir.getAbsolutePath());
        }

        return this.mDiskCacheManager;
    }

    @Override
    public String toString() {
        return this.mCacheKey + "@" + Integer.toHexString(this.hashCode());
    }

    public void put(@NonNull String key, byte[] value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.put(key, (byte[]) value, -1);
        }
    }

    public void put(@NonNull String key, byte[] value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.realPutBytes("by_" + key, value, saveTime);
        }
    }

    private void realPutBytes(String key, byte[] value, int saveTime) {
        if (value != null) {
            CacheDiskUtils.DiskCacheManager diskCacheManager = this.getDiskCacheManager();
            if (diskCacheManager != null) {
                if (saveTime >= 0) {
                    value = CacheDiskUtils.DiskCacheHelper.newByteArrayWithTime(saveTime, value);
                }

                File file = diskCacheManager.getFileBeforePut(key);
                UtilsBridge.writeFileFromBytes(file, value);
                diskCacheManager.updateModify(file);
                diskCacheManager.put(file);
            }
        }
    }

    public byte[] getBytes(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return this.getBytes(key, (byte[]) null);
        }
    }

    public byte[] getBytes(@NonNull String key, byte[] defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return this.realGetBytes("by_" + key, defaultValue);
        }
    }

    private byte[] realGetBytes(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return this.realGetBytes(key, (byte[]) null);
        }
    }

    private byte[] realGetBytes(@NonNull String key, byte[] defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            CacheDiskUtils.DiskCacheManager diskCacheManager = this.getDiskCacheManager();
            if (diskCacheManager == null) {
                return defaultValue;
            } else {
                File file = diskCacheManager.getFileIfExists(key);
                if (file == null) {
                    return defaultValue;
                } else {
                    byte[] data = UtilsBridge.readFile2Bytes(file);
                    if (CacheDiskUtils.DiskCacheHelper.isDue(data)) {
                        diskCacheManager.removeByKey(key);
                        return defaultValue;
                    } else {
                        diskCacheManager.updateModify(file);
                        return CacheDiskUtils.DiskCacheHelper.getDataWithoutDueTime(data);
                    }
                }
            }
        }
    }

    public void put(@NonNull String key, String value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.put(key, (String) value, -1);
        }
    }

    public void put(@NonNull String key, String value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.realPutBytes("st_" + key, UtilsBridge.string2Bytes(value), saveTime);
        }
    }

    public String getString(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return this.getString(key, (String) null);
        }
    }

    public String getString(@NonNull String key, String defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            byte[] bytes = this.realGetBytes("st_" + key);
            return bytes == null ? defaultValue : UtilsBridge.bytes2String(bytes);
        }
    }

    public void put(@NonNull String key, JSONObject value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.put(key, (JSONObject) value, -1);
        }
    }

    public void put(@NonNull String key, JSONObject value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.realPutBytes("jo_" + key, UtilsBridge.jsonObject2Bytes(value), saveTime);
        }
    }

    public JSONObject getJSONObject(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return this.getJSONObject(key, (JSONObject) null);
        }
    }

    public JSONObject getJSONObject(@NonNull String key, JSONObject defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            byte[] bytes = this.realGetBytes("jo_" + key);
            return bytes == null ? defaultValue : UtilsBridge.bytes2JSONObject(bytes);
        }
    }

    public void put(@NonNull String key, JSONArray value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.put(key, (JSONArray) value, -1);
        }
    }

    public void put(@NonNull String key, JSONArray value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.realPutBytes("ja_" + key, UtilsBridge.jsonArray2Bytes(value), saveTime);
        }
    }

    public JSONArray getJSONArray(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return this.getJSONArray(key, (JSONArray) null);
        }
    }

    public JSONArray getJSONArray(@NonNull String key, JSONArray defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            byte[] bytes = this.realGetBytes("ja_" + key);
            return bytes == null ? defaultValue : UtilsBridge.bytes2JSONArray(bytes);
        }
    }

    public void put(@NonNull String key, Bitmap value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.put(key, (Bitmap) value, -1);
        }
    }

    public void put(@NonNull String key, Bitmap value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.realPutBytes("bi_" + key, UtilsBridge.bitmap2Bytes(value), saveTime);
        }
    }

    public Bitmap getBitmap(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return this.getBitmap(key, (Bitmap) null);
        }
    }

    public Bitmap getBitmap(@NonNull String key, Bitmap defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            byte[] bytes = this.realGetBytes("bi_" + key);
            return bytes == null ? defaultValue : UtilsBridge.bytes2Bitmap(bytes);
        }
    }

    public void put(@NonNull String key, Drawable value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.put(key, (Drawable) value, -1);
        }
    }

    public void put(@NonNull String key, Drawable value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.realPutBytes("dr_" + key, UtilsBridge.drawable2Bytes(value), saveTime);
        }
    }

    public Drawable getDrawable(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return this.getDrawable(key, (Drawable) null);
        }
    }

    public Drawable getDrawable(@NonNull String key, Drawable defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            byte[] bytes = this.realGetBytes("dr_" + key);
            return bytes == null ? defaultValue : UtilsBridge.bytes2Drawable(bytes);
        }
    }

    public void put(@NonNull String key, Parcelable value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.put(key, (Parcelable) value, -1);
        }
    }

    public void put(@NonNull String key, Parcelable value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.realPutBytes("pa_" + key, UtilsBridge.parcelable2Bytes(value), saveTime);
        }
    }

    public <T> T getParcelable(@NonNull String key, @NonNull Creator<T> creator) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (creator == null) {
            throw new NullPointerException("Argument 'creator' of type Parcelable.Creator<T> (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return this.getParcelable(key, creator, (T) null);
        }
    }

    public <T> T getParcelable(@NonNull String key, @NonNull Creator<T> creator, T defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (creator == null) {
            throw new NullPointerException("Argument 'creator' of type Parcelable.Creator<T> (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            byte[] bytes = this.realGetBytes("pa_" + key);
            return bytes == null ? defaultValue : UtilsBridge.bytes2Parcelable(bytes, creator);
        }
    }

    public void put(@NonNull String key, Serializable value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.put(key, (Serializable) value, -1);
        }
    }

    public void put(@NonNull String key, Serializable value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            this.realPutBytes("se_" + key, UtilsBridge.serializable2Bytes(value), saveTime);
        }
    }

    public Object getSerializable(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return this.getSerializable(key, (Object) null);
        }
    }

    public Object getSerializable(@NonNull String key, Object defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            byte[] bytes = this.realGetBytes("se_" + key);
            return bytes == null ? defaultValue : UtilsBridge.bytes2Object(bytes);
        }
    }

    public long getCacheSize() {
        CacheDiskUtils.DiskCacheManager diskCacheManager = this.getDiskCacheManager();
        return diskCacheManager == null ? 0L : diskCacheManager.getCacheSize();
    }

    public int getCacheCount() {
        CacheDiskUtils.DiskCacheManager diskCacheManager = this.getDiskCacheManager();
        return diskCacheManager == null ? 0 : diskCacheManager.getCacheCount();
    }

    public boolean remove(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            CacheDiskUtils.DiskCacheManager diskCacheManager = this.getDiskCacheManager();
            if (diskCacheManager == null) {
                return true;
            } else {
                return diskCacheManager.removeByKey("by_" + key) && diskCacheManager.removeByKey("st_" + key) && diskCacheManager.removeByKey("jo_" + key) && diskCacheManager.removeByKey("ja_" + key) && diskCacheManager.removeByKey("bi_" + key) && diskCacheManager.removeByKey("dr_" + key) && diskCacheManager.removeByKey("pa_" + key) && diskCacheManager.removeByKey("se_" + key);
            }
        }
    }

    public boolean clear() {
        CacheDiskUtils.DiskCacheManager diskCacheManager = this.getDiskCacheManager();
        return diskCacheManager == null ? true : diskCacheManager.clear();
    }

    private static final class DiskCacheHelper {
        static final int TIME_INFO_LEN = 14;

        private DiskCacheHelper() {
        }

        private static byte[] newByteArrayWithTime(int second, byte[] data) {
            byte[] time = createDueTime(second).getBytes();
            byte[] content = new byte[time.length + data.length];
            System.arraycopy(time, 0, content, 0, time.length);
            System.arraycopy(data, 0, content, time.length, data.length);
            return content;
        }

        private static String createDueTime(int seconds) {
            return String.format(Locale.getDefault(), "_$%010d$_", System.currentTimeMillis() / 1000L + (long) seconds);
        }

        private static boolean isDue(byte[] data) {
            long millis = getDueTime(data);
            return millis != -1L && System.currentTimeMillis() > millis;
        }

        private static long getDueTime(byte[] data) {
            if (hasTimeInfo(data)) {
                String millis = new String(copyOfRange(data, 2, 12));

                try {
                    return Long.parseLong(millis) * 1000L;
                } catch (NumberFormatException var3) {
                    return -1L;
                }
            } else {
                return -1L;
            }
        }

        private static byte[] getDataWithoutDueTime(byte[] data) {
            return hasTimeInfo(data) ? copyOfRange(data, 14, data.length) : data;
        }

        private static byte[] copyOfRange(byte[] original, int from, int to) {
            int newLength = to - from;
            if (newLength < 0) {
                throw new IllegalArgumentException(from + " > " + to);
            } else {
                byte[] copy = new byte[newLength];
                System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
                return copy;
            }
        }

        private static boolean hasTimeInfo(byte[] data) {
            return data != null && data.length >= 14 && data[0] == 95 && data[1] == 36 && data[12] == 36 && data[13] == 95;
        }
    }

    private static final class DiskCacheManager {
        private final AtomicLong cacheSize;
        private final AtomicInteger cacheCount;
        private final long sizeLimit;
        private final int countLimit;
        private final Map<File, Long> lastUsageDates;
        private final File cacheDir;
        private final Thread mThread;

        private DiskCacheManager(final File cacheDir, long sizeLimit, int countLimit) {
            this.lastUsageDates = Collections.synchronizedMap(new HashMap());
            this.cacheDir = cacheDir;
            this.sizeLimit = sizeLimit;
            this.countLimit = countLimit;
            this.cacheSize = new AtomicLong();
            this.cacheCount = new AtomicInteger();
            this.mThread = new Thread(new Runnable() {
                public void run() {
                    int size = 0;
                    int count = 0;
                    File[] cachedFiles = cacheDir.listFiles(new FilenameFilter() {
                        public boolean accept(File dir, String name) {
                            return name.startsWith("cdu_");
                        }
                    });
                    if (cachedFiles != null) {
                        File[] var4 = cachedFiles;
                        int var5 = cachedFiles.length;

                        for (int var6 = 0; var6 < var5; ++var6) {
                            File cachedFile = var4[var6];
                            size = (int) ((long) size + cachedFile.length());
                            ++count;
                            DiskCacheManager.this.lastUsageDates.put(cachedFile, cachedFile.lastModified());
                        }

                        DiskCacheManager.this.cacheSize.getAndAdd((long) size);
                        DiskCacheManager.this.cacheCount.getAndAdd(count);
                    }

                }
            });
            this.mThread.start();
        }

        private long getCacheSize() {
            this.wait2InitOk();
            return this.cacheSize.get();
        }

        private int getCacheCount() {
            this.wait2InitOk();
            return this.cacheCount.get();
        }

        private File getFileBeforePut(String key) {
            this.wait2InitOk();
            File file = new File(this.cacheDir, this.getCacheNameByKey(key));
            if (file.exists()) {
                this.cacheCount.addAndGet(-1);
                this.cacheSize.addAndGet(-file.length());
            }

            return file;
        }

        private void wait2InitOk() {
            try {
                this.mThread.join();
            } catch (InterruptedException var2) {
                var2.printStackTrace();
            }

        }

        private File getFileIfExists(String key) {
            File file = new File(this.cacheDir, this.getCacheNameByKey(key));
            return !file.exists() ? null : file;
        }

        private String getCacheNameByKey(String key) {
            return "cdu_" + key.substring(0, 3) + key.substring(3).hashCode();
        }

        private void put(File file) {
            this.cacheCount.addAndGet(1);
            this.cacheSize.addAndGet(file.length());

            while (this.cacheCount.get() > this.countLimit || this.cacheSize.get() > this.sizeLimit) {
                this.cacheSize.addAndGet(-this.removeOldest());
                this.cacheCount.addAndGet(-1);
            }

        }

        private void updateModify(File file) {
            Long millis = System.currentTimeMillis();
            file.setLastModified(millis);
            this.lastUsageDates.put(file, millis);
        }

        private boolean removeByKey(String key) {
            File file = this.getFileIfExists(key);
            if (file == null) {
                return true;
            } else if (!file.delete()) {
                return false;
            } else {
                this.cacheSize.addAndGet(-file.length());
                this.cacheCount.addAndGet(-1);
                this.lastUsageDates.remove(file);
                return true;
            }
        }

        private boolean clear() {
            File[] files = this.cacheDir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.startsWith("cdu_");
                }
            });
            if (files != null && files.length > 0) {
                boolean flag = true;
                File[] var3 = files;
                int var4 = files.length;

                for (int var5 = 0; var5 < var4; ++var5) {
                    File file = var3[var5];
                    if (!file.delete()) {
                        flag = false;
                    } else {
                        this.cacheSize.addAndGet(-file.length());
                        this.cacheCount.addAndGet(-1);
                        this.lastUsageDates.remove(file);
                    }
                }

                if (flag) {
                    this.lastUsageDates.clear();
                    this.cacheSize.set(0L);
                    this.cacheCount.set(0);
                }

                return flag;
            } else {
                return true;
            }
        }

        private long removeOldest() {
            if (this.lastUsageDates.isEmpty()) {
                return 0L;
            } else {
                Long oldestUsage = 9223372036854775807L;
                File oldestFile = null;
                Set<Entry<File, Long>> entries = this.lastUsageDates.entrySet();
                synchronized (this.lastUsageDates) {
                    Iterator var5 = entries.iterator();

                    while (var5.hasNext()) {
                        Entry<File, Long> entry = (Entry) var5.next();
                        Long lastValueUsage = (Long) entry.getValue();
                        if (lastValueUsage < oldestUsage) {
                            oldestUsage = lastValueUsage;
                            oldestFile = (File) entry.getKey();
                        }
                    }
                }

                if (oldestFile == null) {
                    return 0L;
                } else {
                    long fileSize = oldestFile.length();
                    if (oldestFile.delete()) {
                        this.lastUsageDates.remove(oldestFile);
                        return fileSize;
                    } else {
                        return 0L;
                    }
                }
            }
        }
    }
}
