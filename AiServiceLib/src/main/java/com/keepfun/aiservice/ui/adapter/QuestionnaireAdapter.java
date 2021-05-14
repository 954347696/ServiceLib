package com.keepfun.aiservice.ui.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.keepfun.adapter.base.BaseMultiItemQuickAdapter;
import com.keepfun.adapter.base.BaseQuickAdapter;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.entity.AnswerBean;
import com.keepfun.aiservice.entity.QuestionnaireBean;
import com.keepfun.aiservice.ui.divider.HorizontalDividerItemDecoration;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.SizeUtils;
import com.keepfun.blankj.util.StringUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/9/1 3:30 PM
 */
public class QuestionnaireAdapter extends BaseMultiItemQuickAdapter<QuestionnaireBean, BaseViewHolder> {

    private static final int TYPE_SINGLE_SELECTION = 0;
    private static final int TYPE_MULTI_SELECTION = 1;
    private static final int TYPE_EDIT_FILL = 2;

    public QuestionnaireAdapter(List<QuestionnaireBean> data) {
        super(data);
        addItemType(TYPE_SINGLE_SELECTION, R.layout.service_layout_single_selection);
        addItemType(TYPE_MULTI_SELECTION, R.layout.service_layout_single_selection);
        addItemType(TYPE_EDIT_FILL, R.layout.service_layout_edit_fill);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder helper, QuestionnaireBean questionnaireBean) {
        helper.setText(R.id.tv_question_title, (getItemPosition(questionnaireBean) + 1) + "." + questionnaireBean.getQuestion());
        switch (helper.getItemViewType()) {
            case TYPE_EDIT_FILL:
                EditText et_question = helper.getView(R.id.et_question);
                TextView tv_question_length = helper.getView(R.id.tv_question_length);
                et_question.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        LogUtils.e("questionnaire edittext focus changed");
                        if(hasFocus){
                            LinearLayoutManager ll = (LinearLayoutManager) getRecyclerView().getLayoutManager();
                            ll.scrollToPositionWithOffset(helper.getAdapterPosition(), 0);
                        }
                    }
                });
//                et_question.removeTextChangedListener();
                et_question.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        LogUtils.e("questionnaire edittext focus changed");
                        LinearLayoutManager ll = (LinearLayoutManager) getRecyclerView().getLayoutManager();
                        ll.scrollToPositionWithOffset(helper.getAdapterPosition(), 0);
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        tv_question_length.setText(s.length() + " / 100");
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String answer = et_question.getText().toString().trim();
                        List<AnswerBean> answerBeans = new ArrayList<>();
                        if (!StringUtils.isEmpty(answer)) {
                            AnswerBean answerBean = new AnswerBean(answer);
                            answerBeans.add(answerBean);
                        }
                        questionnaireBean.setAnswerVoList(answerBeans);
                    }
                });
                break;
            case TYPE_MULTI_SELECTION:
            case TYPE_SINGLE_SELECTION:
                RecyclerView recyclerView = helper.getView(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                RecyclerView.ItemDecoration decor = new HorizontalDividerItemDecoration.Builder(getContext())
                        .colorResId(R.color.color_EE)
                        .size(SizeUtils.dp2px(0.5f))
                        .showLastDivider()
                        .build();
                recyclerView.addItemDecoration(decor);

                List<AnswerBean> data = new ArrayList<>();
                BaseQuickAdapter singleSelectionAdapter;
                if (helper.getItemViewType() == TYPE_SINGLE_SELECTION) {
                    singleSelectionAdapter = new QuestionnaireSingleSelectionAdapter(data);
                } else {
                    singleSelectionAdapter = new QuestionnaireMultiSelectionAdapter(data);
                }
                data.addAll(questionnaireBean.getAnswerVoList());
                recyclerView.setAdapter(singleSelectionAdapter);
            default:
                break;
        }
    }
}
