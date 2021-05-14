package com.keepfun.aiservice.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.AnswerBean;
import com.keepfun.aiservice.entity.CodeImgVo;
import com.keepfun.aiservice.entity.QsPaperUploadBean;
import com.keepfun.aiservice.entity.QsUploadBean;
import com.keepfun.aiservice.entity.Questionnaire;
import com.keepfun.aiservice.entity.QuestionnaireBean;
import com.keepfun.aiservice.ui.adapter.QuestionnaireAdapter;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.ui.presenter.QuestionnairePresenter;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.ImageUtils;
import com.keepfun.blankj.util.KeyboardUtils;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.chatlib.Base64;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yang
 * @description 询前问卷
 * @date 2020/9/1 2:25 PM
 */
public class QuestionnaireActivity extends PanActivity<QuestionnairePresenter> implements View.OnClickListener {

    NestedScrollView scroll_view;
    RecyclerView recyclerView;
    private View layout_imageCode;
    private EditText et_image_code;
    private ImageView iv_image_code;
    private TextView tv_code_change;

    private List<QuestionnaireBean> data;
    private QuestionnaireAdapter mAdapter;

    private Questionnaire mQuestionnaire;
    private TextView tv_empty;
    private int mChatType = YLConstant.ChatType.CHAT_TYPE_VIP;
    private boolean needCode = false;
    private String imageKey;

    @Override
    public QuestionnairePresenter newP() {
        return new QuestionnairePresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.service_activity_questionnaire;
    }

    @Override
    public void bindUI(View rootView) {
        scroll_view = findViewById(R.id.scroll_view);
        recyclerView = findViewById(R.id.recyclerView);
        layout_imageCode = findViewById(R.id.layout_imageCode);
        et_image_code = findViewById(R.id.et_image_code);
        iv_image_code = findViewById(R.id.iv_image_code);
        tv_code_change = findViewById(R.id.tv_code_change);
    }

    @Override
    public void bindEvent() {
        findViewById(R.id.tv_commit).setOnClickListener(new CheckClickListener(this));
        tv_code_change.setOnClickListener(new CheckClickListener(this));

        KeyboardUtils.registerSoftInputChangedListener(this, height -> {
//            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) scroll_view.getLayoutParams();
//            lp.bottomMargin = height;
//            scroll_view.setLayoutParams(lp);
//            scrollToBottom();
        });
    }

    /**
     * 滚动到底部（不带动画）
     */
    private void scrollToBottom() {
        LinearLayoutManager ll = (LinearLayoutManager) recyclerView.getLayoutManager();
        ll.scrollToPositionWithOffset(getBottomDataPosition(), 0);
    }

    private int getBottomDataPosition() {
        return mAdapter.getHeaderLayoutCount() + mAdapter.getData().size() - 1;
    }


    @Override
    public void initData() {
        KeyboardUtils.fixAndroidBug5497(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mChatType = bundle.getInt(Arguments.DATA);
            needCode = bundle.getBoolean(Arguments.DATA1);
        }
        layout_imageCode.setVisibility(needCode ? View.VISIBLE : View.GONE);
        initRecyclerView();

        getP().getQuestionnaire();

        if (needCode) {
            getP().getImageCode();
        }
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (data == null) {
            data = new ArrayList<>();
        }

        mAdapter = new QuestionnaireAdapter(data);
        View emptyView = LayoutInflater.from(this).inflate(R.layout.service_view_empty, null);
        tv_empty = emptyView.findViewById(R.id.tv_empty);
        mAdapter.setEmptyView(emptyView);
        recyclerView.setAdapter(mAdapter);
    }

    public void getDataSuccess(Questionnaire result) {
        this.mQuestionnaire = result;
        if (result != null) {
            mAdapter.addData(result.getSubjectVoList());
        }
    }

    public void getDataFailed(String appMsg) {
        tv_empty.setText(appMsg);
    }

    public void getImageCodeSuccess(CodeImgVo codeImgVo) {
        LogUtils.e("getImageCodeSuccess codeImgVo : " + codeImgVo);
        this.imageKey = codeImgVo.getImgKey();
        iv_image_code.setImageBitmap(ImageUtils.bytes2Bitmap(Base64.base64ToByteArray(codeImgVo.getCodeImg())));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_commit) {
            commit();
        } else if (v.getId() == R.id.tv_code_change) {
            getP().getImageCode();
        }
    }

    private void commit() {
        if (mQuestionnaire == null) {
            showToast("问卷获取失败，不能提交");
            return;
        }
        QsPaperUploadBean qsPaperUploadBean = new QsPaperUploadBean();
        qsPaperUploadBean.setId(mQuestionnaire.getId());
        if (needCode) {
            String imageCode = et_image_code.getText().toString().trim();
            if (StringUtils.isEmpty(imageCode)) {
                showToast("请输入图形验证码");
                return;
            }
            qsPaperUploadBean.setImgKey(imageKey);
            qsPaperUploadBean.setImgCode(imageCode);
        }
        for (QuestionnaireBean questionnaireBean : mQuestionnaire.getSubjectVoList()) {
            QsUploadBean qsUploadBean = new QsUploadBean();
            qsUploadBean.setType(questionnaireBean.getType());
            qsUploadBean.setId(questionnaireBean.getId());
            if (questionnaireBean.getType() != 2) {
                List<AnswerBean> answerBeans = new ArrayList<>();
                for (AnswerBean answerBean : questionnaireBean.getAnswerVoList()) {
                    if (answerBean.isSelected()) {
                        answerBeans.add(answerBean);
                    }
                }
                if (answerBeans.size() == 0) {
                    showToast("您还有问题未作答");
                    return;
                }
                qsUploadBean.setAnswerBoList(answerBeans);
                qsPaperUploadBean.getSubjectBoList().add(qsUploadBean);
            } else {
                if (questionnaireBean.getAnswerVoList().size() == 0) {
                    showToast("您还有问题未作答");
                    return;
                }
                qsUploadBean.setAnswerBoList(questionnaireBean.getAnswerVoList());
                qsPaperUploadBean.getSubjectBoList().add(qsUploadBean);
            }
        }
        getP().commitQuestionnaire(qsPaperUploadBean);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//
//            if (isShouldHideKeyboard(v, ev)) {
//                KeyboardUtils.hideSoftInput(this);
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    // Return whether touch the view.
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationOnScreen(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getRawX() > left && event.getRawX() < right
                    && event.getRawY() > top && event.getRawY() < bottom);
        }
        return false;
    }  // Return whether touch the view.



    public void commitSuccess() {
        showToast("提交成功");
        Intent intent = new Intent();
        intent.putExtra(Arguments.DATA, mChatType);
        setResult(RESULT_OK, intent);
        finish();
    }
}
