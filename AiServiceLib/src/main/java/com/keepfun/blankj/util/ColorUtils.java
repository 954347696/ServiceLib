//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.keepfun.blankj.util;

import android.graphics.Color;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public final class ColorUtils {
    private ColorUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static int getColor(@ColorRes int id) {
        return ContextCompat.getColor(Utils.getApp(), id);
    }

    public static int setAlphaComponent(@ColorInt int color, @IntRange(from = 0L,to = 255L) int alpha) {
        return color & 16777215 | alpha << 24;
    }

    public static int setAlphaComponent(@ColorInt int color, @FloatRange(from = 0.0D,to = 1.0D) float alpha) {
        return color & 16777215 | (int)(alpha * 255.0F + 0.5F) << 24;
    }

    public static int setRedComponent(@ColorInt int color, @IntRange(from = 0L,to = 255L) int red) {
        return color & -16711681 | red << 16;
    }

    public static int setRedComponent(@ColorInt int color, @FloatRange(from = 0.0D,to = 1.0D) float red) {
        return color & -16711681 | (int)(red * 255.0F + 0.5F) << 16;
    }

    public static int setGreenComponent(@ColorInt int color, @IntRange(from = 0L,to = 255L) int green) {
        return color & -65281 | green << 8;
    }

    public static int setGreenComponent(@ColorInt int color, @FloatRange(from = 0.0D,to = 1.0D) float green) {
        return color & -65281 | (int)(green * 255.0F + 0.5F) << 8;
    }

    public static int setBlueComponent(@ColorInt int color, @IntRange(from = 0L,to = 255L) int blue) {
        return color & -256 | blue;
    }

    public static int setBlueComponent(@ColorInt int color, @FloatRange(from = 0.0D,to = 1.0D) float blue) {
        return color & -256 | (int)(blue * 255.0F + 0.5F);
    }

    public static int string2Int(@NonNull String colorString) {
        if (colorString == null) {
            throw new NullPointerException("Argument 'colorString' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return Color.parseColor(colorString);
        }
    }

    public static String int2RgbString(@ColorInt int colorInt) {
        colorInt &= 16777215;

        String color;
        for(color = Integer.toHexString(colorInt); color.length() < 6; color = "0" + color) {
        }

        return "#" + color;
    }

    public static String int2ArgbString(@ColorInt int colorInt) {
        String color;
        for(color = Integer.toHexString(colorInt); color.length() < 6; color = "0" + color) {
        }

        while(color.length() < 8) {
            color = "f" + color;
        }

        return "#" + color;
    }

    public static int getRandomColor() {
        return getRandomColor(true);
    }

    public static int getRandomColor(boolean supportAlpha) {
        int high = supportAlpha ? (int)(Math.random() * 256.0D) << 24 : -16777216;
        return high | (int)(Math.random() * 1.6777216E7D);
    }
}
