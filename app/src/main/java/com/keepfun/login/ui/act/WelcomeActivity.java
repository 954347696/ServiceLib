package com.keepfun.login.ui.act;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.login.R;

/**
 * @author yang
 * @description
 * @date 2021/3/18 4:58 PM
 */
public class WelcomeActivity extends PanActivity {

    @Override
    public int getLayoutId() {
        return R.layout.service_activity_splash;
    }

    @Override
    public String[] getPermissions() {
        return new String[]{Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    @Override
    public void bindUI(View rootView) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!isTaskRoot()) {
            finish();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isTaskRoot()) {
            finish();
            return;
        }
    }

    @Override
    public void initData() {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogUtils.e("startActivity");
                    ActivityUtils.startActivity(PwdLoginActivity.class);
                    finish();
                }
            }, 1000);
    }

    @Override
    public void bindEvent() {

    }
}
