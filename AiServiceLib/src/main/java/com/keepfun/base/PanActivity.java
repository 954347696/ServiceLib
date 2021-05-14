package com.keepfun.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.SkinAppCompatDelegateImpl;

import com.keepfun.aiservice.ui.act.ServiceMediaActivity;
import com.keepfun.aiservice.ui.dialog.ProgressLoadingDialog;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.ClickUtils;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.PermissionUtils;
import com.keepfun.blankj.util.ToastUtils;
import com.keepfun.base.impl.IPresenter;
import com.keepfun.base.impl.IView;
import com.keepfun.immersion.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/8/24 3:34 PM
 */
public abstract class PanActivity<P extends IPresenter> extends AppCompatActivity implements IView<P> {
    public String TAG = this.getClass().getSimpleName();

    private P p;
    public ImmersionBar mImmersionBar;
    private ProgressLoadingDialog progressLoadingDialog;

    public boolean isInitImmersionBar() {
        return true;
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    public P newP() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            bindUI(null);
        }
        if (isInitImmersionBar()) {
            mImmersionBar = ImmersionBar.with(this);
            mImmersionBar.statusBarDarkFont(true).init();
        }
        initDataWithCheck();
    }

    public void initDataWithCheck() {
        if (getPermissions().length == 0) {
            initData();
            bindEvent();
            return;
        }
        if (PermissionUtils.isGranted(getPermissions())) {
            initData();
            bindEvent();
        } else {
            LogUtils.e("initDataWithCheck permission request : " + this.getClass().getSimpleName());
            PermissionUtils.permission(getPermissions()).callback(new PermissionUtils.FullCallback() {
                @Override
                public void onGranted(@NonNull List<String> granted) {
                    initData();
                    bindEvent();
                }

                @Override
                public void onDenied(@NonNull List<String> deniedForever, @NonNull List<String> denied) {
                    ToastUtils.showShort("请允许获取权限");
                }
            }).request();
        }
    }

    @Override
    public String[] getPermissions() {
        return new String[0];
    }


    protected P getP() {
        if (p == null) {
            p = newP();
        }
        if (p != null) {
            if (!p.hasV()) {
                p.attachV(this);
            }
        }
        return p;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //音视频页面存在 并且未开启小屏
        if (ActivityUtils.isActivityExistsInStack(ServiceMediaActivity.class) &&
                ServiceMediaActivity.getServiceMediaActivity() != null &&
                ActivityUtils.isActivityAlive(ServiceMediaActivity.getServiceMediaActivity()) &&
                !ServiceMediaActivity.getServiceMediaActivity().ismIsOpen() &&
                !(ActivityUtils.getTopActivity() instanceof ServiceMediaActivity)) {
            ActivityUtils.startActivity(ServiceMediaActivity.class);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        if (getP() != null) {
            getP().detachV();
        }
        p = null;
        // 必须调用该方法，防止内存泄漏
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
    }

    @NonNull
    @Override
    public AppCompatDelegate getDelegate() {
        return SkinAppCompatDelegateImpl.get(this, this);
    }

    public void showToast(String appMsg) {
        Toast toast = Toast.makeText(this, appMsg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void showLoading() {
        if (progressLoadingDialog == null) {
            progressLoadingDialog = new ProgressLoadingDialog(this);
        }
        if (!progressLoadingDialog.isShowing()) {
            progressLoadingDialog.show();
        }
    }

    public void dismissLoading() {
        if (progressLoadingDialog != null && progressLoadingDialog.isShowing()) {
            progressLoadingDialog.dismiss();
        }
    }
}
