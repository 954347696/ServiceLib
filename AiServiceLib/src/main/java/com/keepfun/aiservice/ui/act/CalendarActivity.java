package com.keepfun.aiservice.ui.act;

import android.content.Intent;
import android.view.View;
import android.widget.CalendarView;

import androidx.annotation.NonNull;

import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.TimeUtils;
import com.keepfun.blankj.util.ToastUtils;

import java.text.SimpleDateFormat;


public class CalendarActivity extends PanActivity implements View.OnClickListener {
    private CalendarView calendar;
    private String date;

    @Override
    public int getLayoutId() {
        return R.layout.activity_calendar;
    }

    @Override
    public void bindUI(View rootView) {
        calendar = findViewById(R.id.calendar);
    }

    @Override
    public void initData() {
        date = TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Override
    public void bindEvent() {
        calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            //
            date = year + "-" + (month + 1) + "-" + dayOfMonth;
        });

        findViewById(R.id.tv_sure).setOnClickListener(new CheckClickListener(this));
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_sure) {
            Intent intent = new Intent();
            intent.putExtra(Arguments.DATA, date);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
