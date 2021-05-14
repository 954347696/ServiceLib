package com.keepfun.aiservice.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

public class TimeCount extends CountDownTimer {
    private TextView btnGetCode;

    public TimeCount(long millisInFuture, long countDownInterval, TextView view) {
        super(millisInFuture, countDownInterval);
        this.btnGetCode = view;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        btnGetCode.setEnabled(false);
        btnGetCode.setClickable(false);
        btnGetCode.setText(millisUntilFinished / 1000 + "s");
    }

    @Override
    public void onFinish() {
        btnGetCode.setText("获取验证码");
        btnGetCode.setEnabled(true);
        btnGetCode.setClickable(true);
    }

}
