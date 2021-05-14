package com.keepfun.aiservice.ui.view;

import android.app.Activity;
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
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.blankj.util.StringUtils;

import skin.support.content.res.SkinCompatResources;
import skin.support.widget.SkinCompatHelper;
import skin.support.widget.SkinCompatLinearLayout;

import static skin.support.widget.SkinCompatHelper.INVALID_ID;

/**
 * @author yang
 * @description
 * @date 2020/8/27 3:31 PM
 */
public class ServiceTitleView extends SkinCompatLinearLayout {


    public ServiceTitleView(Context context) {
        this(context, null);
    }

    public ServiceTitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ServiceTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    ImageView iv_left;
    TextView tv_left;
    TextView tv_title;
    ImageView iv_right;
    TextView tv_right;

    private Context context;
    private View mView;
    private int ivLeftRes = INVALID_ID;
    private int ivRightRes = INVALID_ID;
    private int colorTitle = INVALID_ID;

    private void init(@Nullable AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.service_commin_title, this, true);
        tv_title = mView.findViewById(R.id.tv_title);
        iv_left = mView.findViewById(R.id.iv_left);
        tv_left = mView.findViewById(R.id.tv_left);
        iv_right = mView.findViewById(R.id.iv_right);
        tv_right = mView.findViewById(R.id.tv_right);
        initAttrs(attrs);
        initListeners();
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        //获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ServiceTitleView);
        if (ta.hasValue(R.styleable.ServiceTitleView_stv_left_img)) {
            ivLeftRes = ta.getResourceId(R.styleable.ServiceTitleView_stv_left_img, INVALID_ID);
        }
        if (ta.hasValue(R.styleable.ServiceTitleView_stv_show_left)) {
            boolean showLeft = ta.getBoolean(R.styleable.ServiceTitleView_stv_show_left, true);
            iv_left.setVisibility(showLeft ? VISIBLE : GONE);
        }
        if (ta.hasValue(R.styleable.ServiceTitleView_stv_right_img)) {
            ivRightRes = ta.getResourceId(R.styleable.ServiceTitleView_stv_right_img, INVALID_ID);
        }
        if (ta.hasValue(R.styleable.ServiceTitleView_stv_left_tv)) {
            String textLeft = ta.getString(R.styleable.ServiceTitleView_stv_left_tv);
            tv_left.setText(textLeft);
        }
        if (ta.hasValue(R.styleable.ServiceTitleView_stv_title)) {
            String textTitle = ta.getString(R.styleable.ServiceTitleView_stv_title);
            tv_title.setText(textTitle);
        }
        if (ta.hasValue(R.styleable.ServiceTitleView_stv_title_color)) {
            colorTitle = ta.getResourceId(R.styleable.ServiceTitleView_stv_title_color, INVALID_ID);
        }
        if (ta.hasValue(R.styleable.ServiceTitleView_stv_right_tv)) {
            String textLeft = ta.getString(R.styleable.ServiceTitleView_stv_right_tv);
            tv_right.setText(textLeft);
        }

        ta.recycle();

        applyImageResource();
    }

    private void applyImageResource() {
        ivLeftRes = SkinCompatHelper.checkResourceId(ivLeftRes);
        if (ivLeftRes != INVALID_ID) {
            Drawable drawable = SkinCompatResources.getDrawable(getContext(), ivLeftRes);
            iv_left.setImageDrawable(drawable);
        }
        ivRightRes = SkinCompatHelper.checkResourceId(ivRightRes);
        if (ivRightRes != INVALID_ID) {
            Drawable drawable = SkinCompatResources.getDrawable(getContext(), ivRightRes);
            iv_right.setImageDrawable(drawable);
        }
        colorTitle = SkinCompatHelper.checkResourceId(colorTitle);
        if (colorTitle != INVALID_ID) {
            int color = SkinCompatResources.getColor(getContext(), colorTitle);
            tv_title.setTextColor(color);
        }
    }

    private void initListeners() {
        if (!StringUtils.isEmpty(tv_left.getText().toString().trim()) || iv_left.getDrawable() != null) {
            mView.findViewById(R.id.layout_left).setOnClickListener(new CheckClickListener(v -> ((Activity) context).finish()));
        }
    }

    public void setRightGone() {
        iv_right.setVisibility(GONE);
    }

    public void setLeftListener(OnClickListener listener) {
        mView.findViewById(R.id.layout_left).setOnClickListener(new CheckClickListener(listener));
    }

    public void setRightListener(OnClickListener listener) {
        mView.findViewById(R.id.layout_right).setOnClickListener(new CheckClickListener(listener));
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    @Override
    public void applySkin() {
        applyImageResource();
    }

}
