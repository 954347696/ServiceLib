package com.keepfun.aiservice;

import android.app.Application;

import com.keepfun.aiservice.entity.ServiceUser;

/**
 * @author yang
 * @description
 * @date 2020/10/22 9:37 AM
 */
public class ServiceSdk {
    public static volatile ServiceSdk mInstance;

    private ServiceManager mEventManager;

    public static ServiceSdk getInstance() {
        if (mInstance == null) {
            synchronized (ServiceSdk.class) {
                if (mInstance == null) {
                    mInstance = new ServiceSdk(new ServiceManager());
                }
            }
        }
        return mInstance;
    }


    public ServiceSdk(ServiceManager ylEventManager) {
        this.mEventManager = ylEventManager;
    }

    public void initialize(Application application, String appKey, String appSecret, String privateKey) {
        mEventManager.initialize(application, appKey, appSecret, privateKey);
    }

    public void setUserInfo(ServiceUser serviceUser) {
        mEventManager.setUserInfo(serviceUser);
    }

    public void startService(String sourcePage) {
        mEventManager.startService(sourcePage);
    }

    public void startServiceList(String sourcePage) {
        mEventManager.setServiceHomeShow(true);
        mEventManager.startServiceList(sourcePage);
    }

    public void startServiceListOnly(String sourcePage) {
        mEventManager.setServiceHomeShow(false);
        mEventManager.startServiceList(sourcePage);
    }

    public boolean checkInitDataAvailable() {
        return mEventManager.checkInitDataAvailable();
    }

    public boolean checkAccessTokenAvailable() {
        return mEventManager.checkAccessTokenAvailable();
    }

    public void onRelease() {
        mEventManager.onRelease();
    }
}
