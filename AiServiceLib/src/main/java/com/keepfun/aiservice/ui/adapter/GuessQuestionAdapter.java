package com.keepfun.aiservice.ui.adapter;

import android.os.Bundle;

import com.keepfun.adapter.base.BaseQuickAdapter;
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
public class GuessQuestionAdapter extends BaseQuickAdapter<Question, BaseViewHolder> {

    public GuessQuestionAdapter(@Nullable List<Question> data) {
        super(R.layout.service_item_relation_question, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, Question question) {
        helper.setText(R.id.tv_title, question.getTitle());

        helper.itemView.setOnClickListener(new CheckClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Arguments.DATA, question);
            ActivityUtils.startActivity(bundle, QuestionDetailActivity.class);
        }));
    }
}
