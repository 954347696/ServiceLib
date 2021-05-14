package com.keepfun.base;

import com.keepfun.base.impl.IPresenter;
import com.keepfun.base.impl.IView;

import java.lang.ref.WeakReference;


/**
 * @author yang
 * @description base class of Presenter
 * @date 2020/8/24 4:33 PM
 */
public class PanPresenter<V extends IView> implements IPresenter<V> {
    private WeakReference<V> v;

    public PanPresenter() {
    }

    @Override
    public void attachV(V view) {
        v = new WeakReference<>(view);
    }

    @Override
    public void detachV() {
        if (v.get() != null) {
            v.clear();
        }
        v = null;
    }

    public V getV() {
        if (v == null || v.get() == null) {
            throw new IllegalStateException("v can not be null");
        }
        return v.get();
    }


    @Override
    public boolean hasV() {
        return v != null && v.get() != null;
    }
}
