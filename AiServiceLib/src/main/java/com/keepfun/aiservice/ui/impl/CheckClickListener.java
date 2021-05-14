package com.keepfun.aiservice.ui.impl;

import android.view.View;

import com.keepfun.blankj.util.ClickUtils;
import com.keepfun.blankj.util.LogUtils;

/**
 * @author yang
 * @description
 * @date 2020/10/31 3:43 PM
 */
public class CheckClickListener implements View.OnClickListener {

    private View.OnClickListener onClickListener;

    public CheckClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    @Override
    public void onClick(View v) {
        long clickTime=System.currentTimeMillis();
        if (!ClickUtils.checkInvalidClick(v,clickTime)) {
            onClickListener.onClick(v);
        }
    }
}
