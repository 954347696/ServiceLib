package com.keepfun.base.impl;

import android.content.Context;
import android.view.View;

import androidx.annotation.UiThread;

/**
 * @author yang
 * @description
 * @date 2020/8/24 3:24 PM
 */
public interface IView<P> {

    /**
     * get a layout resource.  The resource will be
     * inflated, adding all top-level views to the view.
     *
     * @return Resource ID to be inflated
     */
    int getLayoutId();

    /**
     * bind UI ids
     *
     * @param rootView
     */
    void bindUI(View rootView);
    /**
     * do data operations
     *
     */
    @UiThread
    void initData();

    /**
     * set listeners here
     */
    void bindEvent();

    /**
     * select whether use eventBus in this view ,than decision if need to do register and unregister
     *
     * @return
     */
    boolean useEventBus();

    /**
     * get presenter ,if not initialized ,initialized and attach to the view
     *
     * @return presenter
     */
    P newP();

    /**
     * get context
     *
     * @return
     */
    Context getContext();

    /**
     * get the permissions which the view needs
     *
     * @return
     */
    String[] getPermissions();
}
