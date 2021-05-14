package com.keepfun.base.impl;

import androidx.lifecycle.LifecycleObserver;

/**
 * @author yang
 * @description
 * @date 2020/8/24 3:22 PM
 */
public interface IPresenter<V> extends LifecycleObserver {
    /**
     * bind this presenter to the view
     *
     * @param view
     */
    void attachV(V view);

    /**
     * unbind the presenter
     */
    void detachV();

    /**
     * check if has attached
     *
     * @return
     */
    boolean hasV();

}
