//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.keepfun.immersion;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class SimpleImmersionFragment extends Fragment implements SimpleImmersionOwner {
    private SimpleImmersionProxy mSimpleImmersionProxy = new SimpleImmersionProxy(this);

    public SimpleImmersionFragment() {
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.mSimpleImmersionProxy.setUserVisibleHint(isVisibleToUser);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mSimpleImmersionProxy.onActivityCreated(savedInstanceState);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mSimpleImmersionProxy.onDestroy();
    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.mSimpleImmersionProxy.onHiddenChanged(hidden);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mSimpleImmersionProxy.onConfigurationChanged(newConfig);
    }

    public boolean immersionBarEnabled() {
        return true;
    }
}
