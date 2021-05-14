package com.keepfun.login;

import android.app.Application;

import com.keepfun.blankj.util.LogUtils;
import com.keepfun.aiservice.ServiceSystem;

/**
 * @author yang
 * @description
 * @date 2020/10/22 10:24 AM
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceSystem.initialize(this, Constant.appKey, Constant.appSecret, Constant.privateKey);


//        Thread.setDefaultUncaughtExceptionHandler(new UnCatchExceptionHandler());
    }


    //异常退出的时候,自动重启
    class UnCatchExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            LogUtils.e("UnCatchExceptionHandler : " + ex.getMessage());
            ex.printStackTrace();
//            Intent intent = new Intent(TankBangApplication.this, SplashActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            TankBangApplication.this.startActivity(intent);
//            Process.killProcess(android.os.Process.myPid());
//            System.exit(1);
        }
    }
}
