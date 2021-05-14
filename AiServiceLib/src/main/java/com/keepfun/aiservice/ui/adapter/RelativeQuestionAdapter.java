package com.keepfun.aiservice.ui.adapter;

import android.os.Bundle;
import android.view.View;


import com.keepfun.adapter.base.BaseQuickAdapter;
import com.keepfun.adapter.base.module.LoadMoreModule;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.entity.Question;
import com.keepfun.aiservice.ui.act.QuestionDetailActivity;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.blankj.util.ActivityUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/8/28 2:50 PM
 */
public class RelativeQuestionAdapter extends BaseQuickAdapter<Question, BaseViewHolder> implements LoadMoreModule {

    public RelativeQuestionAdapter(@Nullable List<Question> data) {
        super(R.layout.service_item_relation_question, data);
    }

    private String keyWords;

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, Question question) {
        if (question == null) {
            return;
        }

        helper.setText(R.id.tv_title, question.getTitle());

        helper.itemView.setOnClickListener(new CheckClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Arguments.DATA, question);
            ActivityUtils.startActivity(bundle, QuestionDetailActivity.class);
        }));
    }
}
