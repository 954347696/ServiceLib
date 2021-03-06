package com.keepfun.base;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


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
 * @date 2020/8/24 5:35 PM
 */
public abstract class PanDialogFragment<P extends IPresenter> extends DialogFragment implements IView<P> {
    private P p;
    protected Activity context;
    public View rootView;
    public ImmersionBar mImmersionBar;

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
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
        if (isInitImmersionBar()) {
            mImmersionBar = ImmersionBar.with(this);
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
            PermissionUtils.permission(getPermissions()).callback(new PermissionUtils.FullCallback() {
                @Override
                public void onGranted(@NonNull List<String> granted) {
                    initData();
                    bindEvent();
                }

                @Override
                public void onDenied(@NonNull List<String> deniedForever, @NonNull List<String> denied) {
                    ToastUtils.showShort("?????????????????????");
                }
            }).request();
        }
    }


    @Override
    public void bindUI(View rootView) {

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
        //api??????23??????????????????
        if (context instanceof Activity) {
            this.context = (Activity) context;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //api??????23??????????????????
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            this.context = activity;
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
        // ??????????????????????????????????????????
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
    }
}
