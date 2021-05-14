package com.keepfun.banner.indicator;

import android.view.View;

import androidx.annotation.NonNull;

import com.keepfun.banner.config.IndicatorConfig;
import com.keepfun.banner.listener.OnPageChangeListener;

public interface Indicator extends OnPageChangeListener {
    @NonNull
    View getIndicatorView();

    IndicatorConfig getIndicatorConfig();

    void onPageChanged(int count, int currentPosition);

}
