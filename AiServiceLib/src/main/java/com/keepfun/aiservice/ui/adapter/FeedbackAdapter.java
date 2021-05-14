package com.keepfun.aiservice.ui.adapter;

import android.os.Bundle;


import com.keepfun.adapter.base.BaseQuickAdapter;
import com.keepfun.adapter.base.module.LoadMoreModule;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.entity.FeedbackBean;
import com.keepfun.aiservice.ui.act.FeedbackDetailActivity;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.TimeUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/8/28 2:50 PM
 */
public class FeedbackAdapter extends BaseQuickAdapter<FeedbackBean, BaseViewHolder> implements LoadMoreModule {

    public FeedbackAdapter(@Nullable List<FeedbackBean> data) {
        super(R.layout.service_item_feedback, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, FeedbackBean feedbackBean) {

        helper.setText(R.id.tv_feedback_type, feedbackBean.getFeedbackTypeName());
        helper.setText(R.id.tv_feedback_info, feedbackBean.getFeedback());
        helper.setText(R.id.tv_time, TimeUtils.millis2String(feedbackBean.getCreateTime(), "yyyy/MM/dd  HH:mm"));
        helper.setGone(R.id.tv_no_reply, feedbackBean.hasReply());
        helper.setGone(R.id.tv_view_reply, !feedbackBean.hasReply());
        helper.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putLong(Arguments.DATA, feedbackBean.getId());
            ActivityUtils.startActivity(bundle, FeedbackDetailActivity.class);
        });
    }
}
