package com.keepfun.aiservice.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.keepfun.aiservice.R;

import skin.support.content.res.SkinCompatResources;
import skin.support.widget.SkinCompatHelper;
import skin.support.widget.SkinCompatLinearLayout;

import static skin.support.widget.SkinCompatHelper.INVALID_ID;

/**
 * @author yang
 * @description
 * @date 2020/8/28 9:22 AM
 */
public class HomeTabView extends SkinCompatLinearLayout {

    public HomeTabView(Context context) {
        this(context, null);
    }

    public HomeTabView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs, defStyleAttr);
    }

    ImageView icon;
    TextView text;
    TextView tv_unread_count;

    private Context context;
    private int mHomeImageRes = INVALID_ID;

    private void init(@Nullable AttributeSet attrs, int defStyleRes) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = inflater.inflate(R.layout.service_item_home_tab, this, true);
        initUI(mView);
        initAttrs(attrs, defStyleRes);
    }

    private void initUI(View mView) {
        icon = mView.findViewById(R.id.icon);
        text = mView.findViewById(R.id.text);
        tv_unread_count = mView.findViewById(R.id.tv_unread_count);
    }


    private void initAttrs(AttributeSet attrs, int defStyleRes) {
        if (attrs == null) {
            return;
        }
        //获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HomeTabView);
        if (ta.hasValue(R.styleable.HomeTabView_htv_img)) {
            mHomeImageRes = ta.getResourceId(R.styleable.HomeTabView_htv_img, INVALID_ID);
            Drawable drawable = ta.getDrawable(R.styleable.HomeTabView_htv_img);
            icon.setImageDrawable(drawable);
        }
        if (ta.hasValue(R.styleable.HomeTabView_htv_tv)) {
            String textStr = ta.getString(R.styleable.HomeTabView_htv_tv);
            text.setText(textStr);
        }
        ta.recycle();
        applyImageResource();
    }

    public void setUnReadCount(int count) {
        tv_unread_count.setVisibility(count > 0 ? VISIBLE : GONE);
        tv_unread_count.setText(String.valueOf(count));
    }

    private void applyImageResource() {
        mHomeImageRes = SkinCompatHelper.checkResourceId(mHomeImageRes);
        if (mHomeImageRes != INVALID_ID) {
            Drawable drawable = SkinCompatResources.getDrawable(getContext(), mHomeImageRes);
            icon.setImageDrawable(drawable);
        }
    }

    @Override
    public void applySkin() {
        super.applySkin();
        applyImageResource();
    }


}
