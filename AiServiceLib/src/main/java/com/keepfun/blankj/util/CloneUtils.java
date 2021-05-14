//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.keepfun.blankj.util;

import java.lang.reflect.Type;

public final class CloneUtils {
    private CloneUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static <T> T deepClone(T data, Type type) {
        try {
            return UtilsBridge.fromJson(UtilsBridge.toJson(data), type);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }
}
