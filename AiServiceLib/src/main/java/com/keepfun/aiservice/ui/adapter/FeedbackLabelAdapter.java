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
public class FeedbackLabelAdapter extends BaseQuickAdapter<FeedbackLabelBean, BaseViewHolder> {

    public FeedbackLabelAdapter(@Nullable List<FeedbackLabelBean> data) {
        super(R.layout.service_item_feedback_label, data);
    }

    private int selectPosition = -1;

    public FeedbackLabelBean getSelectLabel() {
        if (selectPosition != -1) {
            return getItem(selectPosition);
        } else {
            return null;
        }
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, FeedbackLabelBean label) {
        if (label == null) {
            return;
        }
        helper.setText(R.id.tv_label, label.getName());
        helper.getView(R.id.tv_label).setSelected(selectPosition == getItemPosition(label));
        helper.getView(R.id.tv_label).setOnClickListener(v -> {
            selectPosition = getItemPosition(label);
            notifyDataSetChanged();
        });
    }
}
