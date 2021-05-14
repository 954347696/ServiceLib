package com.keepfun.aiservice.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.keepfun.blankj.util.SizeUtils;
import com.keepfun.magicindicator.buildins.ArgbEvaluatorHolder;
import com.keepfun.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

/**
 * 账户中心tab View<p>
 *
 * @author zixuefei
 * @since 2020/7/2 17:46
 */
public class QuestionTabView extends LinearLayout implements IPagerTitleView {
    private TextView tabText;
    protected int mSelectedColor;
    protected int mNormalColor;
    protected int mSelectedSize = 16;
    protected int mNormalSize = 14;

    public QuestionTabView(Context context) {
        this(context, null);
    }

    public QuestionTabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        tabText = new TextView(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tabText.setLayoutParams(params);
        tabText.setPadding(SizeUtils.dp2px(10), 0, SizeUtils.dp2px(10), 0);
        tabText.setTextSize(14);
        tabText.setGravity(Gravity.CENTER);
        addView(tabText);
        setGravity(Gravity.CENTER);
    }

    public void setTabText(String text) {
        tabText.setText(text);
        invalidate();
    }


    @Override
    public void onSelected(int index, int totalCount) {
        tabText.setTextColor(mSelectedColor);
        tabText.setTextSize(mSelectedSize);
        tabText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        tabText.setTextColor(mNormalColor);
        tabText.setTextSize(mNormalSize);
        tabText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        int color = ArgbEvaluatorHolder.eval(leavePercent, mSelectedColor, mNormalColor);
        tabText.setTextColor(color);
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        int color = ArgbEvaluatorHolder.eval(enterPercent, mNormalColor, mSelectedColor);
        tabText.setTextColor(color);
    }

    public void setSelectedColor(int selectedColor) {
        mSelectedColor = selectedColor;
    }

    public void setNormalColor(int normalColor) {
        mNormalColor = normalColor;
    }

    public void setSelectedSize(int selectedSize) {
        this.mSelectedSize = selectedSize;
    }

    public void setNormalSize(int normalSize) {
        this.mNormalSize = normalSize;
    }

}
