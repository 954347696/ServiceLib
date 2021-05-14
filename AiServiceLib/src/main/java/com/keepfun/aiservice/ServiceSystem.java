package com.keepfun.aiservice;

import android.app.Application;

import com.keepfun.aiservice.entity.ServiceUser;
import com.keepfun.blankj.util.DeviceUtils;

/**
 * @author yang
 * @description
 * @date 2020/10/20 5:46 PM
 */
public class ServiceSystem {

    public static void initialize(Application application, String appKey, String appSecret, String privateKey) {
        ServiceSdk.getInstance().initialize(application, appKey, appSecret, privateKey);
    }

    public static void setUserInfo(ServiceUser serviceUser) {
        ServiceSdk.getInstance().setUserInfo(serviceUser);
    }

    public static void setGuest() {
        ServiceSdk.getInstance().setUserInfo(new ServiceUser(DeviceUtils.getAndroidID()));
    }

    public static void startService() {
        ServiceSdk.getInstance().startService(null);
    }

    public static void startService(String sourcePage) {
        ServiceSdk.getInstance().startService(sourcePage);
    }

    public static void startServiceList() {
        ServiceSdk.getInstance().startServiceList(null);
    }

    public static void startServiceList(String sourcePage) {
        ServiceSdk.getInstance().startServiceList(sourcePage);
    }
    public static void startServiceListOnly() {
        ServiceSdk.getInstance().startServiceListOnly(null);
    }

    public static void startServiceListOnly(String sourcePage) {
        ServiceSdk.getInstance().startServiceListOnly(sourcePage);
    }

    public void onRelease() {
        ServiceSdk.getInstance().onRelease();
    }

}
