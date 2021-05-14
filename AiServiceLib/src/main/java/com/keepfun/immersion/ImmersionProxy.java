//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.keepfun.immersion;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ImmersionProxy {
    private Fragment mFragment;
    private ImmersionOwner mImmersionOwner;
    private boolean mIsActivityCreated;
    private boolean mIsLazyAfterView;
    private boolean mIsLazyBeforeView;

    public ImmersionProxy(Fragment fragment) {
        this.mFragment = fragment;
        if (fragment instanceof ImmersionOwner) {
            this.mImmersionOwner = (ImmersionOwner)fragment;
        } else {
            throw new IllegalArgumentException("Fragment请实现ImmersionOwner接口");
        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (this.mFragment != null) {
            if (this.mFragment.getUserVisibleHint()) {
                if (!this.mIsLazyBeforeView) {
                    this.mImmersionOwner.onLazyBeforeView();
                    this.mIsLazyBeforeView = true;
                }

                if (this.mIsActivityCreated && this.mFragment.getUserVisibleHint()) {
                    if (this.mImmersionOwner.immersionBarEnabled()) {
                        this.mImmersionOwner.initImmersionBar();
                    }

                    if (!this.mIsLazyAfterView) {
                        this.mImmersionOwner.onLazyAfterView();
                        this.mIsLazyAfterView = true;
                    }

                    this.mImmersionOwner.onVisible();
                }
            } else if (this.mIsActivityCreated) {
                this.mImmersionOwner.onInvisible();
            }
        }

    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (this.mFragment != null && this.mFragment.getUserVisibleHint() && !this.mIsLazyBeforeView) {
            this.mImmersionOwner.onLazyBeforeView();
            this.mIsLazyBeforeView = true;
        }

    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        this.mIsActivityCreated = true;
        if (this.mFragment != null && this.mFragment.getUserVisibleHint()) {
            if (this.mImmersionOwner.immersionBarEnabled()) {
                this.mImmersionOwner.initImmersionBar();
            }

            if (!this.mIsLazyAfterView) {
                this.mImmersionOwner.onLazyAfterView();
                this.mIsLazyAfterView = true;
            }
        }

    }

    public void onResume() {
        if (this.mFragment != null && this.mFragment.getUserVisibleHint()) {
            this.mImmersionOwner.onVisible();
        }

    }

    public void onPause() {
        if (this.mFragment != null) {
            this.mImmersionOwner.onInvisible();
        }

    }

    public void onDestroy() {
        if (this.mFragment != null && this.mFragment.getActivity() != null && this.mImmersionOwner.immersionBarEnabled()) {
            ImmersionBar.with(this.mFragment).destroy();
        }

        this.mFragment = null;
        this.mImmersionOwner = null;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (this.mFragment != null && this.mFragment.getUserVisibleHint()) {
            if (this.mImmersionOwner.immersionBarEnabled()) {
                this.mImmersionOwner.initImmersionBar();
            }

            this.mImmersionOwner.onVisible();
        }

    }

    public void onHiddenChanged(boolean hidden) {
        if (this.mFragment != null) {
            this.mFragment.setUserVisibleHint(!hidden);
        }

    }

    public boolean isUserVisibleHint() {
        return this.mFragment != null ? this.mFragment.getUserVisibleHint() : false;
    }
}
