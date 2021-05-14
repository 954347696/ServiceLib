//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.keepfun.immersion;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SimpleImmersionProxy {
    private Fragment mFragment;
    private SimpleImmersionOwner mSimpleImmersionOwner;
    private boolean mIsActivityCreated;

    public SimpleImmersionProxy(Fragment fragment) {
        this.mFragment = fragment;
        if (fragment instanceof SimpleImmersionOwner) {
            this.mSimpleImmersionOwner = (SimpleImmersionOwner)fragment;
        } else {
            throw new IllegalArgumentException("Fragment请实现SimpleImmersionOwner接口");
        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.setImmersionBar();
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        this.mIsActivityCreated = true;
        this.setImmersionBar();
    }

    public void onDestroy() {
        if (this.mFragment != null && this.mFragment.getActivity() != null && this.mSimpleImmersionOwner.immersionBarEnabled()) {
            ImmersionBar.with(this.mFragment).destroy();
        }

        this.mFragment = null;
        this.mSimpleImmersionOwner = null;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        this.setImmersionBar();
    }

    public void onHiddenChanged(boolean hidden) {
        if (this.mFragment != null) {
            this.mFragment.setUserVisibleHint(!hidden);
        }

    }

    public boolean isUserVisibleHint() {
        return this.mFragment != null ? this.mFragment.getUserVisibleHint() : false;
    }

    private void setImmersionBar() {
        if (this.mFragment != null && this.mIsActivityCreated && this.mFragment.getUserVisibleHint() && this.mSimpleImmersionOwner.immersionBarEnabled()) {
            this.mSimpleImmersionOwner.initImmersionBar();
        }

    }
}
