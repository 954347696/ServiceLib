//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.keepfun.immersion;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class ImmersionFragment extends Fragment implements ImmersionOwner {
    private ImmersionProxy mImmersionProxy = new ImmersionProxy(this);

    public ImmersionFragment() {
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.mImmersionProxy.setUserVisibleHint(isVisibleToUser);
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mImmersionProxy.onCreate(savedInstanceState);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mImmersionProxy.onActivityCreated(savedInstanceState);
    }

    public void onResume() {
        super.onResume();
        this.mImmersionProxy.onResume();
    }

    public void onPause() {
        super.onPause();
        this.mImmersionProxy.onPause();
    }

    public void onDestroy() {
        super.onDestroy();
        this.mImmersionProxy.onDestroy();
    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.mImmersionProxy.onHiddenChanged(hidden);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mImmersionProxy.onConfigurationChanged(newConfig);
    }

    public void onLazyBeforeView() {
    }

    public void onLazyAfterView() {
    }

    public void onVisible() {
    }

    public void onInvisible() {
    }

    public boolean immersionBarEnabled() {
        return true;
    }
}
