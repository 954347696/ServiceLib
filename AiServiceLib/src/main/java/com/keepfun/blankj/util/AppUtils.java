package com.keepfun.blankj.util;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Process;
import android.util.Log;
import androidx.annotation.NonNull;
import com.keepfun.blankj.util.ShellUtils.CommandResult;
import com.keepfun.blankj.util.Utils.OnAppStatusChangedListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class AppUtils {
    private AppUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void registerAppStatusChangedListener(@NonNull OnAppStatusChangedListener listener) {
        if (listener == null) {
            throw new NullPointerException("Argument 'listener' of type Utils.OnAppStatusChangedListener (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            UtilsBridge.addOnAppStatusChangedListener(listener);
        }
    }

    public static void unregisterAppStatusChangedListener(@NonNull OnAppStatusChangedListener listener) {
        if (listener == null) {
            throw new NullPointerException("Argument 'listener' of type Utils.OnAppStatusChangedListener (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            UtilsBridge.removeOnAppStatusChangedListener(listener);
        }
    }

    public static void installApp(String filePath) {
        installApp(UtilsBridge.getFileByPath(filePath));
    }

    public static void installApp(File file) {
        Intent installAppIntent = UtilsBridge.getInstallAppIntent(file);
        if (installAppIntent != null) {
            Utils.getApp().startActivity(installAppIntent);
        }
    }

    public static void installApp(Uri uri) {
        Intent installAppIntent = UtilsBridge.getInstallAppIntent(uri);
        if (installAppIntent != null) {
            Utils.getApp().startActivity(installAppIntent);
        }
    }

    public static void uninstallApp(String packageName) {
        if (!UtilsBridge.isSpace(packageName)) {
            Utils.getApp().startActivity(UtilsBridge.getUninstallAppIntent(packageName));
        }
    }

    public static boolean isAppInstalled(String pkgName) {
        if (UtilsBridge.isSpace(pkgName)) {
            return false;
        } else {
            PackageManager pm = Utils.getApp().getPackageManager();

            try {
                return pm.getApplicationInfo(pkgName, 0).enabled;
            } catch (NameNotFoundException var3) {
                return false;
            }
        }
    }

    public static boolean isAppRoot() {
        CommandResult result = UtilsBridge.execCmd("echo root", true);
        return result.result == 0;
    }

    public static boolean isAppDebug() {
        return isAppDebug(Utils.getApp().getPackageName());
    }

    public static boolean isAppDebug(String packageName) {
        if (UtilsBridge.isSpace(packageName)) {
            return false;
        } else {
            ApplicationInfo ai = Utils.getApp().getApplicationInfo();
            return ai != null && (ai.flags & 2) != 0;
        }
    }

    public static boolean isAppSystem() {
        return isAppSystem(Utils.getApp().getPackageName());
    }

    public static boolean isAppSystem(String packageName) {
        if (UtilsBridge.isSpace(packageName)) {
            return false;
        } else {
            try {
                PackageManager pm = Utils.getApp().getPackageManager();
                ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
                return ai != null && (ai.flags & 1) != 0;
            } catch (NameNotFoundException var3) {
                var3.printStackTrace();
                return false;
            }
        }
    }

    public static boolean isAppForeground() {
        ActivityManager am = (ActivityManager)Utils.getApp().getSystemService("activity");
        if (am == null) {
            return false;
        } else {
            List<RunningAppProcessInfo> info = am.getRunningAppProcesses();
            if (info != null && info.size() != 0) {
                Iterator var2 = info.iterator();

                RunningAppProcessInfo aInfo;
                do {
                    if (!var2.hasNext()) {
                        return false;
                    }

                    aInfo = (RunningAppProcessInfo)var2.next();
                } while(aInfo.importance != 100 || !aInfo.processName.equals(Utils.getApp().getPackageName()));

                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean isAppForeground(@NonNull String pkgName) {
        if (pkgName == null) {
            throw new NullPointerException("Argument 'pkgName' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return !UtilsBridge.isSpace(pkgName) && pkgName.equals(UtilsBridge.getForegroundProcessName());
        }
    }

    public static boolean isAppRunning(String pkgName) {
        if (UtilsBridge.isSpace(pkgName)) {
            return false;
        } else {
            ApplicationInfo ai = Utils.getApp().getApplicationInfo();
            int uid = ai.uid;
            ActivityManager am = (ActivityManager)Utils.getApp().getSystemService("activity");
            if (am != null) {
                List<RunningTaskInfo> taskInfo = am.getRunningTasks(2147483647);
                if (taskInfo != null && taskInfo.size() > 0) {
                    Iterator var5 = taskInfo.iterator();

                    while(var5.hasNext()) {
                        RunningTaskInfo aInfo = (RunningTaskInfo)var5.next();
                        if (aInfo.baseActivity != null && pkgName.equals(aInfo.baseActivity.getPackageName())) {
                            return true;
                        }
                    }
                }

                List<RunningServiceInfo> serviceInfo = am.getRunningServices(2147483647);
                if (serviceInfo != null && serviceInfo.size() > 0) {
                    Iterator var9 = serviceInfo.iterator();

                    while(var9.hasNext()) {
                        RunningServiceInfo aInfo = (RunningServiceInfo)var9.next();
                        if (uid == aInfo.uid) {
                            return true;
                        }
                    }
                }
            }

            return false;
        }
    }

    public static void launchApp(String packageName) {
        if (!UtilsBridge.isSpace(packageName)) {
            Intent launchAppIntent = UtilsBridge.getLaunchAppIntent(packageName);
            if (launchAppIntent == null) {
                Log.e("AppUtils", "Didn't exist launcher activity.");
            } else {
                Utils.getApp().startActivity(launchAppIntent);
            }
        }
    }

    public static void relaunchApp() {
        relaunchApp(false);
    }

    public static void relaunchApp(boolean isKillProcess) {
        Intent intent = UtilsBridge.getLaunchAppIntent(Utils.getApp().getPackageName());
        if (intent == null) {
            Log.e("AppUtils", "Didn't exist launcher activity.");
        } else {
            intent.addFlags(335577088);
            Utils.getApp().startActivity(intent);
            if (isKillProcess) {
                Process.killProcess(Process.myPid());
                System.exit(0);
            }
        }
    }

    public static void launchAppDetailsSettings() {
        launchAppDetailsSettings(Utils.getApp().getPackageName());
    }

    public static void launchAppDetailsSettings(String pkgName) {
        if (!UtilsBridge.isSpace(pkgName)) {
            Intent intent = UtilsBridge.getLaunchAppDetailsSettingsIntent(pkgName, true);
            if (UtilsBridge.isIntentAvailable(intent)) {
                Utils.getApp().startActivity(intent);
            }
        }
    }

    public static void launchAppDetailsSettings(Activity activity, int requestCode) {
        launchAppDetailsSettings(activity, requestCode, Utils.getApp().getPackageName());
    }

    public static void launchAppDetailsSettings(Activity activity, int requestCode, String pkgName) {
        if (activity != null && !UtilsBridge.isSpace(pkgName)) {
            Intent intent = UtilsBridge.getLaunchAppDetailsSettingsIntent(pkgName, false);
            if (UtilsBridge.isIntentAvailable(intent)) {
                activity.startActivityForResult(intent, requestCode);
            }
        }
    }

    public static void exitApp() {
        UtilsBridge.finishAllActivities();
        System.exit(0);
    }

    public static Drawable getAppIcon() {
        return getAppIcon(Utils.getApp().getPackageName());
    }

    public static Drawable getAppIcon(String packageName) {
        if (UtilsBridge.isSpace(packageName)) {
            return null;
        } else {
            try {
                PackageManager pm = Utils.getApp().getPackageManager();
                PackageInfo pi = pm.getPackageInfo(packageName, 0);
                return pi == null ? null : pi.applicationInfo.loadIcon(pm);
            } catch (NameNotFoundException var3) {
                var3.printStackTrace();
                return null;
            }
        }
    }

    public static int getAppIconId() {
        return getAppIconId(Utils.getApp().getPackageName());
    }

    public static int getAppIconId(String packageName) {
        if (UtilsBridge.isSpace(packageName)) {
            return 0;
        } else {
            try {
                PackageManager pm = Utils.getApp().getPackageManager();
                PackageInfo pi = pm.getPackageInfo(packageName, 0);
                return pi == null ? 0 : pi.applicationInfo.icon;
            } catch (NameNotFoundException var3) {
                var3.printStackTrace();
                return 0;
            }
        }
    }

    public static String getAppPackageName() {
        return Utils.getApp().getPackageName();
    }

    public static String getAppName() {
        return getAppName(Utils.getApp().getPackageName());
    }

    public static String getAppName(String packageName) {
        if (UtilsBridge.isSpace(packageName)) {
            return "";
        } else {
            try {
                PackageManager pm = Utils.getApp().getPackageManager();
                PackageInfo pi = pm.getPackageInfo(packageName, 0);
                return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
            } catch (NameNotFoundException var3) {
                var3.printStackTrace();
                return "";
            }
        }
    }

    public static String getAppPath() {
        return getAppPath(Utils.getApp().getPackageName());
    }

    public static String getAppPath(String packageName) {
        if (UtilsBridge.isSpace(packageName)) {
            return "";
        } else {
            try {
                PackageManager pm = Utils.getApp().getPackageManager();
                PackageInfo pi = pm.getPackageInfo(packageName, 0);
                return pi == null ? null : pi.applicationInfo.sourceDir;
            } catch (NameNotFoundException var3) {
                var3.printStackTrace();
                return "";
            }
        }
    }

    public static String getAppVersionName() {
        return getAppVersionName(Utils.getApp().getPackageName());
    }

    public static String getAppVersionName(String packageName) {
        if (UtilsBridge.isSpace(packageName)) {
            return "";
        } else {
            try {
                PackageManager pm = Utils.getApp().getPackageManager();
                PackageInfo pi = pm.getPackageInfo(packageName, 0);
                return pi == null ? null : pi.versionName;
            } catch (NameNotFoundException var3) {
                var3.printStackTrace();
                return "";
            }
        }
    }

    public static int getAppVersionCode() {
        return getAppVersionCode(Utils.getApp().getPackageName());
    }

    public static int getAppVersionCode(String packageName) {
        if (UtilsBridge.isSpace(packageName)) {
            return -1;
        } else {
            try {
                PackageManager pm = Utils.getApp().getPackageManager();
                PackageInfo pi = pm.getPackageInfo(packageName, 0);
                return pi == null ? -1 : pi.versionCode;
            } catch (NameNotFoundException var3) {
                var3.printStackTrace();
                return -1;
            }
        }
    }

    public static Signature[] getAppSignature() {
        return getAppSignature(Utils.getApp().getPackageName());
    }

    public static Signature[] getAppSignature(String packageName) {
        if (UtilsBridge.isSpace(packageName)) {
            return null;
        } else {
            try {
                PackageManager pm = Utils.getApp().getPackageManager();
                PackageInfo pi = pm.getPackageInfo(packageName, 64);
                return pi == null ? null : pi.signatures;
            } catch (NameNotFoundException var3) {
                var3.printStackTrace();
                return null;
            }
        }
    }

    public static String getAppSignatureSHA1() {
        return getAppSignatureSHA1(Utils.getApp().getPackageName());
    }

    public static String getAppSignatureSHA1(String packageName) {
        return getAppSignatureHash(packageName, "SHA1");
    }

    public static String getAppSignatureSHA256() {
        return getAppSignatureSHA256(Utils.getApp().getPackageName());
    }

    public static String getAppSignatureSHA256(String packageName) {
        return getAppSignatureHash(packageName, "SHA256");
    }

    public static String getAppSignatureMD5() {
        return getAppSignatureMD5(Utils.getApp().getPackageName());
    }

    public static String getAppSignatureMD5(String packageName) {
        return getAppSignatureHash(packageName, "MD5");
    }

    public static int getAppUid() {
        return getAppUid(Utils.getApp().getPackageName());
    }

    public static int getAppUid(String pkgName) {
        try {
            return Utils.getApp().getPackageManager().getApplicationInfo(pkgName, 0).uid;
        } catch (Exception var2) {
            var2.printStackTrace();
            return -1;
        }
    }

    private static String getAppSignatureHash(String packageName, String algorithm) {
        if (UtilsBridge.isSpace(packageName)) {
            return "";
        } else {
            Signature[] signature = getAppSignature(packageName);
            return signature != null && signature.length > 0 ? UtilsBridge.bytes2HexString(UtilsBridge.hashTemplate(signature[0].toByteArray(), algorithm)).replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0") : "";
        }
    }

    public static AppUtils.AppInfo getAppInfo() {
        return getAppInfo(Utils.getApp().getPackageName());
    }

    public static AppUtils.AppInfo getAppInfo(String packageName) {
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            return pm == null ? null : getBean(pm, pm.getPackageInfo(packageName, 0));
        } catch (NameNotFoundException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static List<AppUtils.AppInfo> getAppsInfo() {
        List<AppUtils.AppInfo> list = new ArrayList();
        PackageManager pm = Utils.getApp().getPackageManager();
        if (pm == null) {
            return list;
        } else {
            List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
            Iterator var3 = installedPackages.iterator();

            while(var3.hasNext()) {
                PackageInfo pi = (PackageInfo)var3.next();
                AppUtils.AppInfo ai = getBean(pm, pi);
                if (ai != null) {
                    list.add(ai);
                }
            }

            return list;
        }
    }

    public static AppUtils.AppInfo getApkInfo(File apkFile) {
        return apkFile != null && apkFile.isFile() && apkFile.exists() ? getApkInfo(apkFile.getAbsolutePath()) : null;
    }

    public static AppUtils.AppInfo getApkInfo(String apkFilePath) {
        if (UtilsBridge.isSpace(apkFilePath)) {
            return null;
        } else {
            PackageManager pm = Utils.getApp().getPackageManager();
            if (pm == null) {
                return null;
            } else {
                PackageInfo pi = pm.getPackageArchiveInfo(apkFilePath, 0);
                if (pi == null) {
                    return null;
                } else {
                    ApplicationInfo appInfo = pi.applicationInfo;
                    appInfo.sourceDir = apkFilePath;
                    appInfo.publicSourceDir = apkFilePath;
                    return getBean(pm, pi);
                }
            }
        }
    }

    private static AppUtils.AppInfo getBean(PackageManager pm, PackageInfo pi) {
        if (pi == null) {
            return null;
        } else {
            ApplicationInfo ai = pi.applicationInfo;
            String packageName = pi.packageName;
            String name = ai.loadLabel(pm).toString();
            Drawable icon = ai.loadIcon(pm);
            String packagePath = ai.sourceDir;
            String versionName = pi.versionName;
            int versionCode = pi.versionCode;
            boolean isSystem = (1 & ai.flags) != 0;
            return new AppUtils.AppInfo(packageName, name, icon, packagePath, versionName, versionCode, isSystem);
        }
    }

    public static class AppInfo {
        private String packageName;
        private String name;
        private Drawable icon;
        private String packagePath;
        private String versionName;
        private int versionCode;
        private boolean isSystem;

        public Drawable getIcon() {
            return this.icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public boolean isSystem() {
            return this.isSystem;
        }

        public void setSystem(boolean isSystem) {
            this.isSystem = isSystem;
        }

        public String getPackageName() {
            return this.packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPackagePath() {
            return this.packagePath;
        }

        public void setPackagePath(String packagePath) {
            this.packagePath = packagePath;
        }

        public int getVersionCode() {
            return this.versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return this.versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public AppInfo(String packageName, String name, Drawable icon, String packagePath, String versionName, int versionCode, boolean isSystem) {
            this.setName(name);
            this.setIcon(icon);
            this.setPackageName(packageName);
            this.setPackagePath(packagePath);
            this.setVersionName(versionName);
            this.setVersionCode(versionCode);
            this.setSystem(isSystem);
        }

        public String toString() {
            return "{\n    pkg name: " + this.getPackageName() + "\n    app icon: " + this.getIcon() + "\n    app name: " + this.getName() + "\n    app path: " + this.getPackagePath() + "\n    app v name: " + this.getVersionName() + "\n    app v code: " + this.getVersionCode() + "\n    is system: " + this.isSystem() + "\n}";
        }
    }
}
