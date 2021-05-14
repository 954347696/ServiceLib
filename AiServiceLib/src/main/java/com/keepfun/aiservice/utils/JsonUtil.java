package com.keepfun.aiservice.utils;

import com.keepfun.aiservice.gson.EGson;
import com.keepfun.aiservice.gson.GsonBuilder;
import com.keepfun.aiservice.gson.JsonParser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * JsonUtil
 */
public class JsonUtil {

    /**
     * parseObject
     *
     * @param jsonStr
     * @param entityClass
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String jsonStr, Class<T> entityClass) {
        T ret = null;
        try {
            ret = new EGson().fromJson(jsonStr, entityClass);
        } catch (Exception e) {
            e.printStackTrace();

        }

        return ret;
    }

    public static <T> T parseObject(String jsonStr, Type type) {
        T ret = null;
        try {
            ret = new EGson().fromJson(jsonStr, type);
        } catch (Exception e) {
            e.printStackTrace();

        }

        return ret;
    }

    public static <T> List<T> parseArrayList(String jsonStr, Class<T> entityClass) {
        List<T> ret = null;
        try {
            ret = new EGson().fromJson(jsonStr, new ListOfSomething<T>(entityClass));
        } catch (Exception e) {
            e.printStackTrace();

        }

        return ret;
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
        try {
            return new EGson().fromJson(json, typeOfT);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T decode(String json, Class<T> classOfT) {
        try {
            return new EGson().fromJson(json, classOfT);
        } catch (Exception e) {
            return null;
        }
    }


    public static String toJSONString(Object obj) {
        String ret = null;

        try {
            ret = new EGson().toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }


    static class ListOfSomething<X> implements ParameterizedType {
        private Class<?> wrapped;

        public ListOfSomething(Class<X> wrapped) {
            this.wrapped = wrapped;
        }

        public Type[] getActualTypeArguments() {
            return new Type[]{wrapped};
        }

        public Type getRawType() {
            return List.class;
        }

        public Type getOwnerType() {
            return null;
        }
    }
}
