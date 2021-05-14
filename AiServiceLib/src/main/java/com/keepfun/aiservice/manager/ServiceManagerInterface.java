package com.keepfun.aiservice.manager;

import android.app.Application;

import java.util.HashMap;

/**
 * @author yang
 * @description
 * @date 2020/10/22 9:49 AM
 */
public interface ServiceManagerInterface {
    void initialize(Application application, String appKey, String appSecret, String privateKey);


    void onRelease();
}
