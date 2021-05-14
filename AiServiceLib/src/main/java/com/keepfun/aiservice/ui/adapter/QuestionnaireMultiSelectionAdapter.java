package com.keepfun.aiservice.ui.adapter;

import android.widget.ImageView;

import com.keepfun.adapter.base.BaseQuickAdapter;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.entity.AnswerBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/9/1 3:30 PM
 */
public class QuestionnaireMultiSelectionAdapter extends BaseQuickAdapter<AnswerBean, BaseViewHolder> {

    public QuestionnaireMultiSelectionAdapter(List<AnswerBean> data) {
        super(R.layout.service_item_multi_selection, data);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder helper, AnswerBean answerBean) {

        helper.setText(R.id.tv_selection, answerBean.getAnswer());
        ImageView iv_selection = helper.getView(R.id.iv_selection);
        iv_selection.setSelected(answerBean.isSelected());
        iv_selection.setOnClickListener(v -> {
            answerBean.setSelected(!answerBean.isSelected());
            iv_selection.setSelected(answerBean.isSelected());
        });
    }
}
