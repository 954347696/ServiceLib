package com.keepfun.blankj.constant;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.annotation.SuppressLint;
import android.os.Build.VERSION;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SuppressLint({"InlinedApi"})
public final class PermissionConstants {
    public static final String CALENDAR = "CALENDAR";
    public static final String CAMERA = "CAMERA";
    public static final String CONTACTS = "CONTACTS";
    public static final String LOCATION = "LOCATION";
    public static final String MICROPHONE = "MICROPHONE";
    public static final String PHONE = "PHONE";
    public static final String SENSORS = "SENSORS";
    public static final String SMS = "SMS";
    public static final String STORAGE = "STORAGE";
    private static final String[] GROUP_CALENDAR = new String[]{"android.permission.READ_CALENDAR", "android.permission.WRITE_CALENDAR"};
    private static final String[] GROUP_CAMERA = new String[]{"android.permission.CAMERA"};
    private static final String[] GROUP_CONTACTS = new String[]{"android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS", "android.permission.GET_ACCOUNTS"};
    private static final String[] GROUP_LOCATION = new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"};
    private static final String[] GROUP_MICROPHONE = new String[]{"android.permission.RECORD_AUDIO"};
    private static final String[] GROUP_PHONE = new String[]{"android.permission.READ_PHONE_STATE", "android.permission.READ_PHONE_NUMBERS", "android.permission.CALL_PHONE", "android.permission.READ_CALL_LOG", "android.permission.WRITE_CALL_LOG", "com.android.voicemail.permission.ADD_VOICEMAIL", "android.permission.USE_SIP", "android.permission.PROCESS_OUTGOING_CALLS", "android.permission.ANSWER_PHONE_CALLS"};
    private static final String[] GROUP_PHONE_BELOW_O = new String[]{"android.permission.READ_PHONE_STATE", "android.permission.READ_PHONE_NUMBERS", "android.permission.CALL_PHONE", "android.permission.READ_CALL_LOG", "android.permission.WRITE_CALL_LOG", "com.android.voicemail.permission.ADD_VOICEMAIL", "android.permission.USE_SIP", "android.permission.PROCESS_OUTGOING_CALLS"};
    private static final String[] GROUP_SENSORS = new String[]{"android.permission.BODY_SENSORS"};
    private static final String[] GROUP_SMS = new String[]{"android.permission.SEND_SMS", "android.permission.RECEIVE_SMS", "android.permission.READ_SMS", "android.permission.RECEIVE_WAP_PUSH", "android.permission.RECEIVE_MMS"};
    private static final String[] GROUP_STORAGE = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};

    public PermissionConstants() {
    }

    public static String[] getPermissions(String permission) {
        if (permission == null) {
            return new String[0];
        } else {
            byte var2 = -1;
            switch(permission.hashCode()) {
                case -1611296843:
                    if (permission.equals("LOCATION")) {
                        var2 = 3;
                    }
                    break;
                case -1596608551:
                    if (permission.equals("SENSORS")) {
                        var2 = 6;
                    }
                    break;
                case -1166291365:
                    if (permission.equals("STORAGE")) {
                        var2 = 8;
                    }
                    break;
                case 82233:
                    if (permission.equals("SMS")) {
                        var2 = 7;
                    }
                    break;
                case 76105038:
                    if (permission.equals("PHONE")) {
                        var2 = 5;
                    }
                    break;
                case 215175251:
                    if (permission.equals("CONTACTS")) {
                        var2 = 2;
                    }
                    break;
                case 604302142:
                    if (permission.equals("CALENDAR")) {
                        var2 = 0;
                    }
                    break;
                case 1856013610:
                    if (permission.equals("MICROPHONE")) {
                        var2 = 4;
                    }
                    break;
                case 1980544805:
                    if (permission.equals("CAMERA")) {
                        var2 = 1;
                    }
            }

            switch(var2) {
                case 0:
                    return GROUP_CALENDAR;
                case 1:
                    return GROUP_CAMERA;
                case 2:
                    return GROUP_CONTACTS;
                case 3:
                    return GROUP_LOCATION;
                case 4:
                    return GROUP_MICROPHONE;
                case 5:
                    if (VERSION.SDK_INT < 26) {
                        return GROUP_PHONE_BELOW_O;
                    }

                    return GROUP_PHONE;
                case 6:
                    return GROUP_SENSORS;
                case 7:
                    return GROUP_SMS;
                case 8:
                    return GROUP_STORAGE;
                default:
                    return new String[]{permission};
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Permission {
    }
}
