package com.keepfun.login.ui.adapter;

import androidx.annotation.Nullable;

import com.keepfun.adapter.base.BaseMultiItemQuickAdapter;
import com.keepfun.adapter.base.BaseQuickAdapter;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.login.R;
import com.keepfun.login.entity.GlCountryEntity;

import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/12/25 10:23 AM
 */
public class SelectCountryAdapter extends BaseMultiItemQuickAdapter<GlCountryEntity, BaseViewHolder> {

    public SelectCountryAdapter(@Nullable List<GlCountryEntity> data) {
        super(data);
        addItemType(GlCountryEntity.TYPE_TITLE, R.layout.item_select_country_title);
        addItemType(GlCountryEntity.TYPE_CONTENT, R.layout.item_select_country);
    }

    private OnCountrySelectedListener onCountrySelectedListener;

    public void setOnCountrySelectedListener(OnCountrySelectedListener onCountrySelectedListener) {
        this.onCountrySelectedListener = onCountrySelectedListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, GlCountryEntity item) {
        switch (helper.getItemViewType()) {
            case GlCountryEntity.TYPE_TITLE:
                helper.setText(R.id.tv_code, item.getCnName());
                break;
            case GlCountryEntity.TYPE_CONTENT:
                helper.setText(R.id.tv_country, item.getCnName());
                helper.setText(R.id.tv_code, "+" + item.getCode());
                break;
        }

        helper.itemView.setOnClickListener(v -> {
            if (onCountrySelectedListener != null && helper.getItemViewType() == GlCountryEntity.TYPE_CONTENT) {
                onCountrySelectedListener.onCountrySelected(item);
            }
        });
    }

    public interface OnCountrySelectedListener {
        void onCountrySelected(GlCountryEntity countryEntity);
    }
}
