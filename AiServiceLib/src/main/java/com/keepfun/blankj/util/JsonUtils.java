package com.keepfun.blankj.util;

import android.text.TextUtils;
import android.util.Log;

import com.keepfun.aiservice.gson.EGson;
import com.keepfun.aiservice.gson.GsonBuilder;
import com.keepfun.aiservice.gson.JsonArray;
import com.keepfun.aiservice.gson.JsonElement;
import com.keepfun.aiservice.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2019/01/07
 *     desc  : utils about json
 * </pre>
 */
public final class JsonUtils {

    private static final byte TYPE_BOOLEAN     = 0x00;
    private static final byte TYPE_INT         = 0x01;
    private static final byte TYPE_LONG        = 0x02;
    private static final byte TYPE_DOUBLE      = 0x03;
    private static final byte TYPE_STRING      = 0x04;
    private static final byte TYPE_JSON_OBJECT = 0x05;
    private static final byte TYPE_JSON_ARRAY  = 0x06;

    private JsonUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean getBoolean(final JSONObject jsonObject,
                                     final String key) {
        return getBoolean(jsonObject, key, false);
    }

    public static boolean getBoolean(final JSONObject jsonObject,
                                     final String key,
                                     final boolean defaultValue) {
        return getValueByType(jsonObject, key, defaultValue, TYPE_BOOLEAN);
    }

    public static boolean getBoolean(final String json,
                                     final String key) {
        return getBoolean(json, key, false);
    }

    public static boolean getBoolean(final String json,
                                     final String key,
                                     final boolean defaultValue) {
        return getValueByType(json, key, defaultValue, TYPE_BOOLEAN);
    }

    public static int getInt(final JSONObject jsonObject,
                             final String key) {
        return getInt(jsonObject, key, -1);
    }

    public static int getInt(final JSONObject jsonObject,
                             final String key,
                             final int defaultValue) {
        return getValueByType(jsonObject, key, defaultValue, TYPE_INT);
    }

    public static int getInt(final String json,
                             final String key) {
        return getInt(json, key, -1);
    }

    public static int getInt(final String json,
                             final String key,
                             final int defaultValue) {
        return getValueByType(json, key, defaultValue, TYPE_INT);
    }

    public static long getLong(final JSONObject jsonObject,
                               final String key) {
        return getLong(jsonObject, key, -1);
    }

    public static long getLong(final JSONObject jsonObject,
                               final String key,
                               final long defaultValue) {
        return getValueByType(jsonObject, key, defaultValue, TYPE_LONG);
    }

    public static long getLong(final String json,
                               final String key) {
        return getLong(json, key, -1);
    }

    public static long getLong(final String json,
                               final String key,
                               final long defaultValue) {
        return getValueByType(json, key, defaultValue, TYPE_LONG);
    }

    public static double getDouble(final JSONObject jsonObject,
                                   final String key) {
        return getDouble(jsonObject, key, -1);
    }

    public static double getDouble(final JSONObject jsonObject,
                                   final String key,
                                   final double defaultValue) {
        return getValueByType(jsonObject, key, defaultValue, TYPE_DOUBLE);
    }

    public static double getDouble(final String json,
                                   final String key) {
        return getDouble(json, key, -1);
    }

    public static double getDouble(final String json,
                                   final String key,
                                   final double defaultValue) {
        return getValueByType(json, key, defaultValue, TYPE_DOUBLE);
    }

    public static String getString(final JSONObject jsonObject,
                                   final String key) {
        return getString(jsonObject, key, "");
    }

    public static String getString(final JSONObject jsonObject,
                                   final String key,
                                   final String defaultValue) {
        return getValueByType(jsonObject, key, defaultValue, TYPE_STRING);
    }

    public static String getString(final String json,
                                   final String key) {
        return getString(json, key, "");
    }

    public static String getString(final String json,
                                   final String key,
                                   final String defaultValue) {
        return getValueByType(json, key, defaultValue, TYPE_STRING);
    }

    public static JSONObject getJSONObject(final JSONObject jsonObject,
                                           final String key,
                                           final JSONObject defaultValue) {
        return getValueByType(jsonObject, key, defaultValue, TYPE_JSON_OBJECT);
    }

    public static JSONObject getJSONObject(final String json,
                                           final String key,
                                           final JSONObject defaultValue) {
        return getValueByType(json, key, defaultValue, TYPE_JSON_OBJECT);
    }

    public static JSONArray getJSONArray(final JSONObject jsonObject,
                                         final String key,
                                         final JSONArray defaultValue) {
        return getValueByType(jsonObject, key, defaultValue, TYPE_JSON_ARRAY);
    }

    public static JSONArray getJSONArray(final String json,
                                         final String key,
                                         final JSONArray defaultValue) {
        return getValueByType(json, key, defaultValue, TYPE_JSON_ARRAY);
    }

    private static <T> T getValueByType(final JSONObject jsonObject,
                                        final String key,
                                        final T defaultValue,
                                        final byte type) {
        if (jsonObject == null || key == null || key.length() == 0) {
            return defaultValue;
        }
        try {
            Object ret;
            if (type == TYPE_BOOLEAN) {
                ret = jsonObject.getBoolean(key);
            } else if (type == TYPE_INT) {
                ret = jsonObject.getInt(key);
            } else if (type == TYPE_LONG) {
                ret = jsonObject.getLong(key);
            } else if (type == TYPE_DOUBLE) {
                ret = jsonObject.getDouble(key);
            } else if (type == TYPE_STRING) {
                ret = jsonObject.getString(key);
            } else if (type == TYPE_JSON_OBJECT) {
                ret = jsonObject.getJSONObject(key);
            } else if (type == TYPE_JSON_ARRAY) {
                ret = jsonObject.getJSONArray(key);
            } else {
                return defaultValue;
            }
            //noinspection unchecked
            return (T) ret;
        } catch (JSONException e) {
            Log.e("JsonUtils", "getValueByType: ", e);
            return defaultValue;
        }
    }

    private static <T> T getValueByType(final String json,
                                        final String key,
                                        final T defaultValue,
                                        final byte type) {
        if (json == null || json.length() == 0
                || key == null || key.length() == 0) {
            return defaultValue;
        }
        try {
            return getValueByType(new JSONObject(json), key, defaultValue, type);
        } catch (JSONException e) {
            Log.e("JsonUtils", "getValueByType: ", e);
            return defaultValue;
        }
    }

    public static String encode(Object json) {
        return new EGson().toJson(json);
    }

    public static String encode(Object json, boolean isPretty) {
        return (isPretty ? new GsonBuilder().setPrettyPrinting().create() : new EGson()).toJson(json);
    }

    public static Object decode(String json) {
        return new JsonParser().parse(json);
    }

    public static <T> T decode(String json, Type typeOfT) {
        return new EGson().fromJson(json, typeOfT);
    }

    public static <T> T decode(String json, Class<T> classOfT) {
        return new EGson().fromJson(json, classOfT);
    }

    public static boolean isJsonFormat(String json) {
        return !TextUtils.isEmpty(json) && (json.startsWith("{") || json.startsWith("[")
                && (json.endsWith("]") || json.endsWith("}")));
    }


    public static String formatJson(final String json) {
        return formatJson(json, 4);
    }

    public static String formatJson(final String json, final int indentSpaces) {
        try {
            for (int i = 0, len = json.length(); i < len; i++) {
                char c = json.charAt(i);
                if (c == '{') {
                    return new JSONObject(json).toString(indentSpaces);
                } else if (c == '[') {
                    return new JSONArray(json).toString(indentSpaces);
                } else if (!Character.isWhitespace(c)) {
                    return json;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        ArrayList<T> arrayList = new ArrayList<>();

        JsonElement jsonElement = new JsonParser().parse(json);
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            if (jsonArray != null && jsonArray.size() > 0) {
                for (JsonElement item : jsonArray) {
                    if (item != null) {
                        arrayList.add(new EGson().fromJson(item, clazz));
                    }
                }
            }
        }
        return arrayList;
    }


    public static ArrayList<String> jsonToStringList(String jsonStringList) {
        ArrayList<String> arrayList = new ArrayList<>();
        JsonElement jsonElement = new JsonParser().parse(jsonStringList);
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            if (jsonArray != null && jsonArray.size() > 0) {
                for (JsonElement item : jsonArray) {
                    if (item != null) {
                        arrayList.add(item.getAsString());
                    }
                }
            }
        }
        return arrayList;
    }
}
