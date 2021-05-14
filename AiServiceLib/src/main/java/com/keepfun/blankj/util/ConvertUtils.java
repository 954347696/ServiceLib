//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.keepfun.blankj.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.view.View;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public final class ConvertUtils {
    private static final int BUFFER_SIZE = 8192;
    private static final char[] HEX_DIGITS_UPPER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final char[] HEX_DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private ConvertUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static String int2HexString(int num) {
        return Integer.toHexString(num);
    }

    public static int hexString2Int(String hexString) {
        return Integer.parseInt(hexString, 16);
    }

    public static String bytes2Bits(byte[] bytes) {
        if (bytes != null && bytes.length != 0) {
            StringBuilder sb = new StringBuilder();
            byte[] var2 = bytes;
            int var3 = bytes.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                byte aByte = var2[var4];

                for(int j = 7; j >= 0; --j) {
                    sb.append((char)((aByte >> j & 1) == 0 ? '0' : '1'));
                }
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    public static byte[] bits2Bytes(String bits) {
        int lenMod = bits.length() % 8;
        int byteLen = bits.length() / 8;
        if (lenMod != 0) {
            for(int i = lenMod; i < 8; ++i) {
                bits = "0" + bits;
            }

            ++byteLen;
        }

        byte[] bytes = new byte[byteLen];

        for(int i = 0; i < byteLen; ++i) {
            for(int j = 0; j < 8; ++j) {
                bytes[i] = (byte)(bytes[i] << 1);
                bytes[i] = (byte)(bytes[i] | bits.charAt(i * 8 + j) - 48);
            }
        }

        return bytes;
    }

    public static char[] bytes2Chars(byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            int len = bytes.length;
            if (len <= 0) {
                return null;
            } else {
                char[] chars = new char[len];

                for(int i = 0; i < len; ++i) {
                    chars[i] = (char)(bytes[i] & 255);
                }

                return chars;
            }
        }
    }

    public static byte[] chars2Bytes(char[] chars) {
        if (chars != null && chars.length > 0) {
            int len = chars.length;
            byte[] bytes = new byte[len];

            for(int i = 0; i < len; ++i) {
                bytes[i] = (byte)chars[i];
            }

            return bytes;
        } else {
            return null;
        }
    }

    public static String bytes2HexString(byte[] bytes) {
        return bytes2HexString(bytes, true);
    }

    public static String bytes2HexString(byte[] bytes, boolean isUpperCase) {
        if (bytes == null) {
            return "";
        } else {
            char[] hexDigits = isUpperCase ? HEX_DIGITS_UPPER : HEX_DIGITS_LOWER;
            int len = bytes.length;
            if (len <= 0) {
                return "";
            } else {
                char[] ret = new char[len << 1];
                int i = 0;

                for(int var6 = 0; i < len; ++i) {
                    ret[var6++] = hexDigits[bytes[i] >> 4 & 15];
                    ret[var6++] = hexDigits[bytes[i] & 15];
                }

                return new String(ret);
            }
        }
    }

    public static byte[] hexString2Bytes(String hexString) {
        if (UtilsBridge.isSpace(hexString)) {
            return new byte[0];
        } else {
            int len = hexString.length();
            if (len % 2 != 0) {
                hexString = "0" + hexString;
                ++len;
            }

            char[] hexBytes = hexString.toUpperCase().toCharArray();
            byte[] ret = new byte[len >> 1];

            for(int i = 0; i < len; i += 2) {
                ret[i >> 1] = (byte)(hex2Dec(hexBytes[i]) << 4 | hex2Dec(hexBytes[i + 1]));
            }

            return ret;
        }
    }

    private static int hex2Dec(char hexChar) {
        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - 48;
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 65 + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static String bytes2String(byte[] bytes) {
        return bytes2String(bytes, "");
    }

    public static String bytes2String(byte[] bytes, String charsetName) {
        if (bytes == null) {
            return null;
        } else {
            try {
                return new String(bytes, getSafeCharset(charsetName));
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
                return new String(bytes);
            }
        }
    }

    public static byte[] string2Bytes(String string) {
        return string2Bytes(string, "");
    }

    public static byte[] string2Bytes(String string, String charsetName) {
        if (string == null) {
            return null;
        } else {
            try {
                return string.getBytes(getSafeCharset(charsetName));
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
                return string.getBytes();
            }
        }
    }

    public static JSONObject bytes2JSONObject(byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            try {
                return new JSONObject(new String(bytes));
            } catch (Exception var2) {
                var2.printStackTrace();
                return null;
            }
        }
    }

    public static byte[] jsonObject2Bytes(JSONObject jsonObject) {
        return jsonObject == null ? null : jsonObject.toString().getBytes();
    }

    public static JSONArray bytes2JSONArray(byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            try {
                return new JSONArray(new String(bytes));
            } catch (Exception var2) {
                var2.printStackTrace();
                return null;
            }
        }
    }

    public static byte[] jsonArray2Bytes(JSONArray jsonArray) {
        return jsonArray == null ? null : jsonArray.toString().getBytes();
    }

    public static <T> T bytes2Parcelable(byte[] bytes, Creator<T> creator) {
        if (bytes == null) {
            return null;
        } else {
            Parcel parcel = Parcel.obtain();
            parcel.unmarshall(bytes, 0, bytes.length);
            parcel.setDataPosition(0);
            T result = creator.createFromParcel(parcel);
            parcel.recycle();
            return result;
        }
    }

    public static byte[] parcelable2Bytes(Parcelable parcelable) {
        if (parcelable == null) {
            return null;
        } else {
            Parcel parcel = Parcel.obtain();
            parcelable.writeToParcel(parcel, 0);
            byte[] bytes = parcel.marshall();
            parcel.recycle();
            return bytes;
        }
    }

    public static Object bytes2Object(byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            ObjectInputStream ois = null;

            Object var3;
            try {
                ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
                Object var2 = ois.readObject();
                return var2;
            } catch (Exception var13) {
                var13.printStackTrace();
                var3 = null;
            } finally {
                try {
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException var12) {
                    var12.printStackTrace();
                }

            }

            return var3;
        }
    }

    public static byte[] serializable2Bytes(Serializable serializable) {
        if (serializable == null) {
            return null;
        } else {
            ObjectOutputStream oos = null;

            Object var4;
            try {
                ByteArrayOutputStream baos;
                oos = new ObjectOutputStream(baos = new ByteArrayOutputStream());
                oos.writeObject(serializable);
                byte[] var3 = baos.toByteArray();
                return var3;
            } catch (Exception var14) {
                var14.printStackTrace();
                var4 = null;
            } finally {
                try {
                    if (oos != null) {
                        oos.close();
                    }
                } catch (IOException var13) {
                    var13.printStackTrace();
                }

            }

            return (byte[])var4;
        }
    }

    public static Bitmap bytes2Bitmap(byte[] bytes) {
        return UtilsBridge.bytes2Bitmap(bytes);
    }

    public static byte[] bitmap2Bytes(Bitmap bitmap) {
        return UtilsBridge.bitmap2Bytes(bitmap);
    }

    public static byte[] bitmap2Bytes(Bitmap bitmap, CompressFormat format, int quality) {
        return UtilsBridge.bitmap2Bytes(bitmap, format, quality);
    }

    public static Drawable bytes2Drawable(byte[] bytes) {
        return UtilsBridge.bytes2Drawable(bytes);
    }

    public static byte[] drawable2Bytes(Drawable drawable) {
        return UtilsBridge.drawable2Bytes(drawable);
    }

    public static byte[] drawable2Bytes(Drawable drawable, CompressFormat format, int quality) {
        return UtilsBridge.drawable2Bytes(drawable, format, quality);
    }

    public static long memorySize2Byte(long memorySize, int unit) {
        return memorySize < 0L ? -1L : memorySize * (long)unit;
    }

    public static double byte2MemorySize(long byteSize, int unit) {
        return byteSize < 0L ? -1.0D : (double)byteSize / (double)unit;
    }

    @SuppressLint({"DefaultLocale"})
    public static String byte2FitMemorySize(long byteSize) {
        return byte2FitMemorySize(byteSize, 3);
    }

    @SuppressLint({"DefaultLocale"})
    public static String byte2FitMemorySize(long byteSize, int precision) {
        if (precision < 0) {
            throw new IllegalArgumentException("precision shouldn't be less than zero!");
        } else if (byteSize < 0L) {
            throw new IllegalArgumentException("byteSize shouldn't be less than zero!");
        } else if (byteSize < 1024L) {
            return String.format("%." + precision + "fB", (double)byteSize);
        } else if (byteSize < 1048576L) {
            return String.format("%." + precision + "fKB", (double)byteSize / 1024.0D);
        } else {
            return byteSize < 1073741824L ? String.format("%." + precision + "fMB", (double)byteSize / 1048576.0D) : String.format("%." + precision + "fGB", (double)byteSize / 1.073741824E9D);
        }
    }

    public static long timeSpan2Millis(long timeSpan, int unit) {
        return timeSpan * (long)unit;
    }

    public static long millis2TimeSpan(long millis, int unit) {
        return millis / (long)unit;
    }

    public static String millis2FitTimeSpan(long millis, int precision) {
        return UtilsBridge.millis2FitTimeSpan(millis, precision);
    }

    public static ByteArrayOutputStream input2OutputStream(InputStream is) {
        if (is == null) {
            return null;
        } else {
            Object var2;
            try {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                byte[] b = new byte[8192];

                int len;
                while((len = is.read(b, 0, 8192)) != -1) {
                    os.write(b, 0, len);
                }

                ByteArrayOutputStream var4 = os;
                return var4;
            } catch (IOException var14) {
                var14.printStackTrace();
                var2 = null;
            } finally {
                try {
                    is.close();
                } catch (IOException var13) {
                    var13.printStackTrace();
                }

            }

            return (ByteArrayOutputStream)var2;
        }
    }

    public ByteArrayInputStream output2InputStream(OutputStream out) {
        return out == null ? null : new ByteArrayInputStream(((ByteArrayOutputStream)out).toByteArray());
    }

    public static byte[] inputStream2Bytes(InputStream is) {
        return is == null ? null : input2OutputStream(is).toByteArray();
    }

    public static InputStream bytes2InputStream(byte[] bytes) {
        return bytes != null && bytes.length > 0 ? new ByteArrayInputStream(bytes) : null;
    }

    public static byte[] outputStream2Bytes(OutputStream out) {
        return out == null ? null : ((ByteArrayOutputStream)out).toByteArray();
    }

    public static OutputStream bytes2OutputStream(byte[] bytes) {
        if (bytes != null && bytes.length > 0) {
            ByteArrayOutputStream os = null;

            Object var3;
            try {
                os = new ByteArrayOutputStream();
                os.write(bytes);
                ByteArrayOutputStream var2 = os;
                return var2;
            } catch (IOException var13) {
                var13.printStackTrace();
                var3 = null;
            } finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException var12) {
                    var12.printStackTrace();
                }

            }

            return (OutputStream)var3;
        } else {
            return null;
        }
    }

    public static String inputStream2String(InputStream is, String charsetName) {
        if (is == null) {
            return "";
        } else {
            try {
                ByteArrayOutputStream baos = input2OutputStream(is);
                return baos == null ? "" : baos.toString(getSafeCharset(charsetName));
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
                return "";
            }
        }
    }

    public static InputStream string2InputStream(String string, String charsetName) {
        if (string == null) {
            return null;
        } else {
            try {
                return new ByteArrayInputStream(string.getBytes(getSafeCharset(charsetName)));
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
                return null;
            }
        }
    }

    public static String outputStream2String(OutputStream out, String charsetName) {
        if (out == null) {
            return "";
        } else {
            try {
                return new String(outputStream2Bytes(out), getSafeCharset(charsetName));
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
                return "";
            }
        }
    }

    public static OutputStream string2OutputStream(String string, String charsetName) {
        if (string == null) {
            return null;
        } else {
            try {
                return bytes2OutputStream(string.getBytes(getSafeCharset(charsetName)));
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
                return null;
            }
        }
    }

    public static List<String> inputStream2Lines(InputStream is) {
        return inputStream2Lines(is, "");
    }

    public static List<String> inputStream2Lines(InputStream is, String charsetName) {
        BufferedReader reader = null;

        String line;
        try {
            List<String> list = new ArrayList();
            reader = new BufferedReader(new InputStreamReader(is, getSafeCharset(charsetName)));

            while((line = reader.readLine()) != null) {
                list.add(line);
            }

            return list;
        } catch (IOException var15) {
            var15.printStackTrace();
            line = null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException var14) {
                var14.printStackTrace();
            }

        }

        return null;
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        return UtilsBridge.drawable2Bitmap(drawable);
    }

    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        return UtilsBridge.bitmap2Drawable(bitmap);
    }

    public static Bitmap view2Bitmap(View view) {
        return UtilsBridge.view2Bitmap(view);
    }

    public static int dp2px(float dpValue) {
        return UtilsBridge.dp2px(dpValue);
    }

    public static int px2dp(float pxValue) {
        return UtilsBridge.px2dp(pxValue);
    }

    public static int sp2px(float spValue) {
        return UtilsBridge.sp2px(spValue);
    }

    public static int px2sp(float pxValue) {
        return UtilsBridge.px2sp(pxValue);
    }

    private static String getSafeCharset(String charsetName) {
        String cn = charsetName;
        if (UtilsBridge.isSpace(charsetName) || !Charset.isSupported(charsetName)) {
            cn = "UTF-8";
        }

        return cn;
    }
}
