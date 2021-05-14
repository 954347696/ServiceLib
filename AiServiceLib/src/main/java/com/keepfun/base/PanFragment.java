package com.keepfun.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.keepfun.aiservice.ui.dialog.ProgressLoadingDialog;
import com.keepfun.blankj.util.ClickUtils;
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
 * @date 2020/8/24 4:35 PM
 */
public abstract class PanFragment<P extends IPresenter> extends Fragment implements IView<P> {

    private P p;
    protected Activity context;
    private View rootView;
    public ImmersionBar mImmersionBar;
    private ProgressLoadingDialog progressLoadingDialog;

    @Override
    public P newP() {
        return null;
    }

    public boolean isInitImmersionBar() {
        return false;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null && getLayoutId() > 0) {
            rootView = inflater.inflate(getLayoutId(), null);
            bindUI(rootView);
        } else {
            ViewGroup viewGroup = (ViewGroup) rootView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(rootView);
            }
        }
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
        if (isInitImmersionBar()) {
            mImmersionBar = ImmersionBar.with(this);
        }
        initDataWithCheck();
        return rootView;
    }

    @SuppressWarnings("TypeParameterUnusedInFormals")
    public final <T extends View> T findViewById(@IdRes int ids){
        return rootView.findViewById(ids);
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
        }else{
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
    public String[] getPermissions() {
        return new String[0];
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.context = (Activity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

    public void showToast(String message) {
        ToastUtils.showShort(message);
    }

    public void showLoading() {
        if (progressLoadingDialog == null) {
            progressLoadingDialog = new ProgressLoadingDialog(getContext());
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
