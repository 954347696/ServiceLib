package com.keepfun.aiservice.gson;

import com.keepfun.aiservice.gson.internal.LinkedTreeMap;
import com.keepfun.aiservice.gson.internal.bind.ObjectTypeAdapter;
import com.keepfun.aiservice.gson.reflect.TypeToken;
import com.keepfun.aiservice.gson.stream.JsonReader;
import com.keepfun.aiservice.gson.stream.JsonToken;
import com.keepfun.aiservice.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yang
 * @description
 * @date 2020/12/29 10:55 AM
 */

public final class MapTypeAdapter extends TypeAdapter<Object> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(EGson gson, TypeToken<T> type) {
            if (type.getRawType() == Object.class) {
                return (TypeAdapter<T>) new MapTypeAdapter(gson);
            }
            return null;
        }
    };

    private final EGson gson;

    private MapTypeAdapter(EGson gson) {
        this.gson = gson;
    }

    @Override
    public Object read(JsonReader in) throws IOException {
        JsonToken token = in.peek();
        //判断字符串的实际类型
        switch (token) {
            case BEGIN_ARRAY:
                List<Object> list = new ArrayList<>();
                in.beginArray();
                while (in.hasNext()) {
                    list.add(read(in));
                }
                in.endArray();
                return list;

            case BEGIN_OBJECT:
                Map<String, Object> map = new LinkedTreeMap<>();
                in.beginObject();
                while (in.hasNext()) {
                    map.put(in.nextName(), read(in));
                }
                in.endObject();
                return map;
            case STRING:
                return in.nextString();
            case NUMBER:
                String s = in.nextString();
                if (s.contains(".")) {
                    return Double.valueOf(s);
                } else {
                    try {
                        return Integer.valueOf(s);
                    } catch (Exception e) {
                        return Long.valueOf(s);
                    }
                }
            case BOOLEAN:
                return in.nextBoolean();
            case NULL:
                in.nextNull();
                return null;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void write(JsonWriter out, Object value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        //noinspection unchecked
        TypeAdapter<Object> typeAdapter = (TypeAdapter<Object>) gson.getAdapter(value.getClass());
        if (typeAdapter instanceof ObjectTypeAdapter) {
            out.beginObject();
            out.endObject();
            return;
        }
        typeAdapter.write(out, value);
    }
}