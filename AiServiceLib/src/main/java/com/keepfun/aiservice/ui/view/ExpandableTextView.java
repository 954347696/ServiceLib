
/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright 2014 Manabu Shimobe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keepfun.aiservice.ui.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;


import com.keepfun.aiservice.R;
import com.keepfun.aiservice.manager.RichTextHelper;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.blankj.util.SizeUtils;

import skin.support.widget.SkinCompatLinearLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static skin.support.widget.SkinCompatHelper.INVALID_ID;


/**
 * @author yang
 * @description 可伸展textview
 * @date 2020/9/8 7:58 PM
 */
public class ExpandableTextView extends SkinCompatLinearLayout implements View.OnClickListener {

    /* 默认最高行数 */
    private static final int MAX_COLLAPSED_LINES = 5;

    /* 默认动画执行时间 */
    private static final int DEFAULT_ANIM_DURATION = 200;

    /*内容textview*/
    protected TextView mTvContent;

    /*展开收起textview*/
    protected TextView mTvExpandCollapse;

    /*是否有重新绘制*/
    private boolean mRelayout;

    /*默认收起*/
    private boolean mCollapsed = true;

    /*展开图片*/
    private Drawable mExpandDrawable;
    /*收起图片*/
    private Drawable mCollapseDrawable;
    /*动画执行时间*/
    private int mAnimationDuration;
    /*是否正在执行动画*/
    private boolean mAnimating;
    /* 展开收起状态回调 */
    private OnExpandStateChangeListener mListener;
    /* listview等列表情况下保存每个item的收起/展开状态 */
    private SparseBooleanArray mCollapsedStatus;
    /* 列表位置 */
    private int mPosition;

    /*设置内容最大行数，超过隐藏*/
    private int mMaxCollapsedLines;

    /*这个linerlayout容器的高度*/
    private int mCollapsedHeight;

    /*内容tv真实高度（含padding）*/
    private int mTextHeightWithMaxLines;

    /*内容tvMarginTopAmndBottom高度*/
    private int mMarginBetweenTxtAndBottom;

    private int contentTextAppearance = INVALID_ID;
    /*内容颜色*/
    private int contentTextColor;
    /*收起展开颜色*/
    private int collapseExpandTextColor;
    /*内容字体大小*/
    private float contentTextSize;
    /*收起展字体大小*/
    private float collapseExpandTextSize;
    /*收起文字*/
    private String textCollapse;
    /*展开文字*/
    private String textExpand;

    /*收起展开位置，默认左边*/
    private int grarity;

    /*收起展开图标位置，默认在右边*/
    private int drawableGrarity;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    @Override
    public void setOrientation(int orientation) {
        if (LinearLayout.HORIZONTAL == orientation) {
            throw new IllegalArgumentException("ExpandableTextView only supports Vertical Orientation.");
        }
        super.setOrientation(orientation);
    }

    /**
     * 初始化属性
     *
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        mCollapsedStatus = new SparseBooleanArray();

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.IcsExpandableTextView);
        mMaxCollapsedLines = typedArray.getInt(R.styleable.IcsExpandableTextView_icsMaxCollapsedLines, MAX_COLLAPSED_LINES);
        mAnimationDuration = typedArray.getInt(R.styleable.IcsExpandableTextView_icsAnimDuration, DEFAULT_ANIM_DURATION);
        mExpandDrawable = typedArray.getDrawable(R.styleable.IcsExpandableTextView_icsEpandDrawable);
        mCollapseDrawable = typedArray.getDrawable(R.styleable.IcsExpandableTextView_icsCollapseDrawable);

        textCollapse = typedArray.getString(R.styleable.IcsExpandableTextView_icsTextCollapse);
        textExpand = typedArray.getString(R.styleable.IcsExpandableTextView_icsTextExpand);

        if (mExpandDrawable == null) {
            mExpandDrawable = ContextCompat.getDrawable(getContext(), R.mipmap.service_ic_arrow_up);
        }
        if (mCollapseDrawable == null) {
            mCollapseDrawable = ContextCompat.getDrawable(getContext(), R.mipmap.service_ic_arrow_down);
        }

        if (TextUtils.isEmpty(textCollapse)) {
            textCollapse = getContext().getString(R.string.collapse);
        }
        if (TextUtils.isEmpty(textExpand)) {
            textExpand = getContext().getString(R.string.expand);
        }
        contentTextAppearance = typedArray.getResourceId(R.styleable.IcsExpandableTextView_icsContentTextAppearance, INVALID_ID);
        contentTextColor = typedArray.getColor(R.styleable.IcsExpandableTextView_icsContentTextColor, ContextCompat.getColor(getContext(), R.color.textColor));
        contentTextSize = typedArray.getDimension(R.styleable.IcsExpandableTextView_icsContentTextSize, SizeUtils.sp2px(14));

        collapseExpandTextColor = typedArray.getColor(R.styleable.IcsExpandableTextView_icsCollapseExpandTextColor, ContextCompat.getColor(getContext(), R.color.color_FFA200));
        collapseExpandTextSize = typedArray.getDimension(R.styleable.IcsExpandableTextView_icsCollapseExpandTextSize, SizeUtils.sp2px(14));

        grarity = typedArray.getInt(R.styleable.IcsExpandableTextView_icsCollapseExpandGrarity, Gravity.CENTER_HORIZONTAL);
        drawableGrarity = typedArray.getInt(R.styleable.IcsExpandableTextView_icsDrawableGrarity, Gravity.RIGHT);

        typedArray.recycle();
        // enforces vertical orientation
        setOrientation(LinearLayout.VERTICAL);
        // default visibility is gone
        setVisibility(GONE);

    }

    /**
     * 渲染完成时初始化view
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViews();
    }

    /**
     * 初始化viwe
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void findViews() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.service_item_expand_collapse, this);
        mTvContent =  findViewById(R.id.expandable_text);
        mTvContent.setMovementMethod(LinkMovementMethod.getInstance());
        mTvContent.setOnClickListener(new CheckClickListener(this));
        mTvExpandCollapse = findViewById(R.id.expand_collapse);
        setDrawbleAndText();
        mTvExpandCollapse.setOnClickListener(new CheckClickListener(this));

        mTvContent.setTextColor(contentTextColor);
        mTvContent.getPaint().setTextSize(contentTextSize);

        if (contentTextAppearance != INVALID_ID) {
            mTvContent.setTextAppearance(contentTextAppearance);
        }

        mTvExpandCollapse.setTextColor(collapseExpandTextColor);
        mTvExpandCollapse.getPaint().setTextSize(collapseExpandTextSize);

        //设置收起展开位置：左或者右
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = grarity;
        mTvExpandCollapse.setLayoutParams(lp);
    }

    public TextView getTextView() {
        return mTvContent;
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (mTvExpandCollapse.getVisibility() != View.VISIBLE) {
            return;
        }
        mCollapsed = !mCollapsed;
        //修改收起/展开图标、文字
        setDrawbleAndText();
        //保存位置状态
        if (mCollapsedStatus != null) {
            mCollapsedStatus.put(mPosition, mCollapsed);
        }
        // 执行展开/收起动画
        mAnimating = true;
        ValueAnimator valueAnimator;
        if (mCollapsed) {
//            mTvContent.setMaxLines(mMaxCollapsedLines);
            valueAnimator = ValueAnimator.ofInt(getHeight(), mCollapsedHeight);
        } else {
            valueAnimator = ValueAnimator.ofInt(getHeight(), getHeight() +
                    mTextHeightWithMaxLines - mTvContent.getHeight());
        }
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animatedValue = (int) valueAnimator.getAnimatedValue();
                mTvContent.setMaxHeight(animatedValue - mMarginBetweenTxtAndBottom);
                getLayoutParams().height = animatedValue;
                requestLayout();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // 动画结束后发送结束的信号
                /// clear the animation flag
                mAnimating = false;
                // notify the listener
                if (mListener != null) {
                    mListener.onExpandStateChanged(mTvContent, !mCollapsed);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        valueAnimator.setDuration(mAnimationDuration);
        valueAnimator.start();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 当动画还在执行状态时，拦截事件，不让child处理
        return mAnimating;
    }

    /**
     * 重新测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // If no change, measure and return
        if (!mRelayout || getVisibility() == View.GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        mRelayout = false;

        // Setup with optimistic case
        // i.e. Everything fits. No button needed
        mTvExpandCollapse.setVisibility(View.GONE);
        mTvContent.setMaxLines(Integer.MAX_VALUE);

        // Measure
        getLayoutParams().width = WRAP_CONTENT;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //如果内容真实行数小于等于最大行数，不处理
        if (mTvContent.getLineCount() <= mMaxCollapsedLines) {
            return;
        }
        // 获取内容tv真实高度（含padding）
        mTextHeightWithMaxLines = getRealTextViewHeight(mTvContent);
        getLayoutParams().width = MATCH_PARENT;
        // 如果是收起状态，重新设置最大行数
        if (mCollapsed) {
            mTvContent.setMaxLines(mMaxCollapsedLines);
        }
        mTvExpandCollapse.setVisibility(View.VISIBLE);
        // Re-measure with new setup
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mCollapsed) {
            // Gets the margin between the TextView's bottom and the ViewGroup's bottom
            mTvContent.post(new Runnable() {
                @Override
                public void run() {
                    mMarginBetweenTxtAndBottom = getHeight() - mTvContent.getHeight();
                }
            });
            // 保存这个容器的测量高度
            mCollapsedHeight = getMeasuredHeight();
        }
    }

    /**
     * 获取内容tv真实高度（含padding）
     *
     * @param textView
     * @return
     */
    private static int getRealTextViewHeight(TextView textView) {
        int textHeight = textView.getLayout().getLineTop(textView.getLineCount());
        int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();
        return textHeight + padding;
    }

    /**
     * 设置收起展开图标位置和文字
     */
    private void setDrawbleAndText() {
        if (Gravity.LEFT == drawableGrarity) {
            mTvExpandCollapse.setCompoundDrawablesWithIntrinsicBounds(mCollapsed ? mCollapseDrawable : mExpandDrawable, null, null, null);
        } else {
            mTvExpandCollapse.setCompoundDrawablesWithIntrinsicBounds(null, null, mCollapsed ? mCollapseDrawable : mExpandDrawable, null);
        }
        mTvExpandCollapse.setText(mCollapsed ? getResources().getString(R.string.expand) : getResources().getString(R.string.collapse));
    }


    /*********暴露给外部调用方法***********/

    /**
     * 设置收起/展开监听
     *
     * @param listener
     */
    public void setOnExpandStateChangeListener(OnExpandStateChangeListener listener) {
        mListener = listener;
    }

    /**
     * 设置内容
     *
     * @param text
     */
    public void setText(CharSequence text) {
        mRelayout = true;
//        mTvContent.setText(text);
        setHtml(String.valueOf(text),mTvContent);
        setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    private void setHtml(String content,TextView textView){
        RichTextHelper.showRich(textView,content);
    }

    /**
     * 设置内容，列表情况下，带有保存位置收起/展开状态
     *
     * @param text
     * @param position
     */
    public void setText(CharSequence text, int position) {
        mPosition = position;
        //获取状态，如无，默认是true:收起
        mCollapsed = mCollapsedStatus.get(position, true);
        clearAnimation();
        //设置收起/展开图标和文字
        setDrawbleAndText();
        mTvExpandCollapse.setText(mCollapsed ? getResources().getString(R.string.expand) : getResources().getString(R.string.collapse));

        setText(text);
        getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        requestLayout();
    }

    /**
     * 获取内容
     *
     * @return
     */
    public CharSequence getText() {
        if (mTvContent == null) {
            return "";
        }
        return mTvContent.getText();
    }

    /**
     * 定义状态改变接口
     */
    public interface OnExpandStateChangeListener {
        /**
         * @param textView   - TextView being expanded/collapsed
         * @param isExpanded - true if the TextView has been expanded
         */
        void onExpandStateChanged(TextView textView, boolean isExpanded);
    }
}