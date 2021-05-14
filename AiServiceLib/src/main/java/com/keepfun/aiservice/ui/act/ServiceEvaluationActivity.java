package com.keepfun.aiservice.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.entity.EvaluateBean;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.entity.ServiceEntity;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.ui.presenter.ServiceEvaluationPresenter;
import com.keepfun.aiservice.ui.view.ratingbar.AndRatingBar;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.blankj.util.ToastUtils;

/**
 * @author yang
 * @description 客服评价
 * @date 2020/9/3 8:22 PM
 */
public class ServiceEvaluationActivity extends PanActivity<ServiceEvaluationPresenter> implements View.OnClickListener {

    ImageView iv_service_avatar;
    TextView tv_service_nickname;
    LinearLayout layout_qs1_yes;
    LinearLayout layout_qs1_no;
    LinearLayout layout_qs2_yes;
    LinearLayout layout_qs2_no;
    TextView tv_qs3_selection_1;
    TextView tv_qs3_selection_2;
    TextView tv_qs3_selection_3;
    EditText et_info_desc;
    TextView tv_desc_count;
    AndRatingBar rb_score;

    private int ratingResult = 0;
    private boolean isQs1Yes = true;
    private boolean isQs2Yes = true;
    private int qs3Result = 3;
    private Message message;
    private EvaluateBean evaluateBean;
    private int position;

    @Override
    public ServiceEvaluationPresenter newP() {
        return new ServiceEvaluationPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.service_activity_service_evaluation;
    }

    @Override
    public void bindUI(View rootView) {
        iv_service_avatar = findViewById(R.id.iv_service_avatar);
        tv_service_nickname = findViewById(R.id.tv_service_nickname);
        layout_qs1_yes = findViewById(R.id.layout_qs1_yes);
        layout_qs1_no = findViewById(R.id.layout_qs1_no);
        layout_qs2_yes = findViewById(R.id.layout_qs2_yes);
        layout_qs2_no = findViewById(R.id.layout_qs2_no);
        tv_qs3_selection_1 = findViewById(R.id.tv_qs3_selection_1);
        tv_qs3_selection_2 = findViewById(R.id.tv_qs3_selection_2);
        tv_qs3_selection_3 = findViewById(R.id.tv_qs3_selection_3);
        et_info_desc = findViewById(R.id.et_info_desc);
        tv_desc_count = findViewById(R.id.tv_desc_count);
        rb_score = findViewById(R.id.rb_score);
    }

    @Override
    public void bindEvent() {
        findViewById(R.id.tv_commit).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.layout_qs1_yes).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.layout_qs1_no).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.layout_qs2_yes).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.layout_qs2_no).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.tv_qs3_selection_1).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.tv_qs3_selection_2).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.tv_qs3_selection_3).setOnClickListener(new CheckClickListener(this));

        rb_score.setOnRatingChangeListener((ratingBar, rating) -> {
            ratingResult = (int) rating;
        });
        et_info_desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_desc_count.setText(s.length() + " / 500");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            message = (Message) bundle.getSerializable(Arguments.DATA);
            position = bundle.getInt(Arguments.POSITION);
        }
        Glide.with(this).load(message.getFromUserAvatar()).placeholder(R.mipmap.service_bg_chat_default).into(iv_service_avatar);
        tv_service_nickname.setText(message.getFromUserName());

        rb_score.setRating(ratingResult);
        selectQs1Result(isQs1Yes);
        selectQs2Result(isQs2Yes);
        selectQs3Result(qs3Result);

        getSessionById(message.getGroupId());
    }

    private void getSessionById(String groupId) {
        OkHttpUtils.postJson(ApiDomain.GET_SESSION_BY_ID + "?groupId=" + groupId, new GsonResponseHandler<ServiceEntity>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(ServiceEntity serviceEntity) {
                if (serviceEntity != null) {
                    Glide.with(getContext()).load(serviceEntity.getAvatarUrl()).placeholder(R.mipmap.service_bg_chat_default).into(iv_service_avatar);
                    tv_service_nickname.setText(serviceEntity.getAllName());
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_commit) {
            commit();
        } else if (id == R.id.layout_qs1_yes) {
            selectQs1Result(true);
        } else if (id == R.id.layout_qs1_no) {
            selectQs1Result(false);
        } else if (id == R.id.layout_qs2_yes) {
            selectQs2Result(true);
        } else if (id == R.id.layout_qs2_no) {
            selectQs2Result(false);
        } else if (id == R.id.tv_qs3_selection_1) {
            selectQs3Result(1);
        } else if (id == R.id.tv_qs3_selection_2) {
            selectQs3Result(2);
        } else if (id == R.id.tv_qs3_selection_3) {
            selectQs3Result(3);
        }
    }

    private void selectQs1Result(boolean isQs1Yes) {
        this.isQs1Yes = isQs1Yes;
        layout_qs1_yes.setSelected(isQs1Yes);
        layout_qs1_no.setSelected(!isQs1Yes);
    }

    private void selectQs2Result(boolean isQs2Yes) {
        this.isQs2Yes = isQs2Yes;
        layout_qs2_yes.setSelected(isQs2Yes);
        layout_qs2_no.setSelected(!isQs2Yes);
    }

    private void selectQs3Result(int selection) {
        this.qs3Result = selection;
        tv_qs3_selection_1.setSelected(selection == 1);
        tv_qs3_selection_2.setSelected(selection == 2);
        tv_qs3_selection_3.setSelected(selection == 3);
    }

    private void commit() {
        if (ratingResult < 1) {
            ToastUtils.showShort("请选择评价分数");
            return;
        }
        if (evaluateBean == null) {
            evaluateBean = new EvaluateBean();
        }
        evaluateBean.setSessionId(message.getGroupId());
        evaluateBean.setFriendly(isQs2Yes ? 1 : 0);
        evaluateBean.setRecommend(qs3Result - 1);
        evaluateBean.setResolve(isQs1Yes ? 1 : 0);
        String evaluate = et_info_desc.getText().toString().trim();
        if (StringUtils.isEmpty(evaluate)) {
            ToastUtils.showShort("反馈内容不能为空");
            return;
        }
        evaluateBean.setEvaluate(evaluate);
        evaluateBean.setScore(ratingResult);
        getP().commitEvaluationInfo(evaluateBean);

    }

    public void evaluationSuccess() {
        showToast("反馈成功");
        Intent intent = new Intent();
        intent.putExtra(Arguments.POSITION, position);
        setResult(RESULT_OK, intent);
        finish();
    }

}
