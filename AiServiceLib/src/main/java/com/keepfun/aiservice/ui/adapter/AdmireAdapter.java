package com.keepfun.aiservice.ui.adapter;


import com.keepfun.adapter.base.BaseQuickAdapter;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.entity.FeedbackLabelBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/8/28 2:50 PM
 */
public class AdmireAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public AdmireAdapter(@Nullable List<String> data) {
        super(R.layout.service_item_admire, data);
    }

    private OnLabelSelectedListener mOnLabelSelectedListener;
    private int selectPosition = -1;

    public String getSelectLabel() {
        if (selectPosition != -1) {
            return getItem(selectPosition);
        } else {
            return null;
        }
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public void setOnLabelSelectedListener(OnLabelSelectedListener mOnLabelSelectedListener) {
        this.mOnLabelSelectedListener = mOnLabelSelectedListener;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, String label) {
        if (label == null) {
            return;
        }
        helper.setText(R.id.tv_label, label);
        helper.getView(R.id.tv_label).setSelected(selectPosition == getItemPosition(label));
        helper.getView(R.id.tv_label).setOnClickListener(v -> {
            selectPosition = getItemPosition(label);
            notifyDataSetChanged();
            if (mOnLabelSelectedListener != null) {
                mOnLabelSelectedListener.onLabelSelected(label);
            }
        });
    }

    public interface OnLabelSelectedListener {
        void onLabelSelected(String label);
    }
}
