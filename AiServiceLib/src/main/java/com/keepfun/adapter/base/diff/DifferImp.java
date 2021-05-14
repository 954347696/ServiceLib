package com.keepfun.adapter.base.diff;

import androidx.annotation.NonNull;

import com.keepfun.adapter.base.diff.ListChangeListener;

/**
 * 使用java接口定义方法
 * @param <T>
 */
public interface DifferImp<T> {
    void addListListener(@NonNull ListChangeListener<T> listChangeListener);
}
