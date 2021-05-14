package com.keepfun.aiservice.ui.act;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.entity.PageBean;
import com.keepfun.aiservice.entity.Question;
import com.keepfun.aiservice.entity.ServiceEntity;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.adapter.RelativeQuestionAdapter;
import com.keepfun.aiservice.ui.divider.HorizontalDividerItemDecoration;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.ui.presenter.QuestionDetailPresenter;
import com.keepfun.aiservice.ui.view.DrawableTextView;
import com.keepfun.aiservice.utils.HtmlImageGetter;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.SizeUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * @author yang
 * @description
 * @date 2020/8/31 1:35 PM
 */
public class QuestionDetailActivity extends PanActivity<QuestionDetailPresenter> implements View.OnClickListener {

    private View layoutTip;
    private TextView tv_unread_count;
    TextView tv_question_title;
    TextView tv_question_content;
    DrawableTextView tv_dealed;
    DrawableTextView tv_undeal;
    RecyclerView recyclerView;

    private Question question;
    private List<Question> dataList;
    private RelativeQuestionAdapter mAdapter;
    private boolean isLoadingMore = false;
    private int pageIndex = 1;

    private ServiceEntity mServiceEntity;

    @Override
    public QuestionDetailPresenter newP() {
        return new QuestionDetailPresenter();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.service_question_detail;
    }

    @Override
    public void bindUI(View rootView) {
        layoutTip = findViewById(R.id.layoutTip);
        tv_unread_count = findViewById(R.id.tv_unread_count);
        tv_question_title = findViewById(R.id.tv_question_title);
        tv_question_content = findViewById(R.id.tv_question_content);
        tv_dealed = findViewById(R.id.tv_dealed);
        tv_undeal = findViewById(R.id.tv_undeal);
        recyclerView = findViewById(R.id.recyclerView);
    }

    @Override
    public void bindEvent() {
        tv_dealed.setOnClickListener(new CheckClickListener(this));
        tv_undeal.setOnClickListener(new CheckClickListener(this));
        layoutTip.setOnClickListener(new CheckClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Arguments.DATA, mServiceEntity.getConversationType());
                bundle.putSerializable(Arguments.DATA1, mServiceEntity);
                ActivityUtils.startActivity(bundle, ServiceOnlineActivity.class);
            }
        }));
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            question = (Question) bundle.getSerializable(Arguments.DATA);
        }
        if (question == null) {
//            throw new UnsupportedOperationException("question can't be null");
        }

        tv_question_title.setText(question.getTitle());
        tv_question_content.setText(Html.fromHtml(question.getContent(), new HtmlImageGetter(getContext(), tv_question_content), null));

        initRecyclerView();
        mAdapter.getLoadMoreModule().setPreLoadNumber(2);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            if (!isLoadingMore) {
                requestData(false);
            }
        });
        getIngSession();
        requestData(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (layoutTip != null) {
            getIngSession();
        }
    }

    public void getIngSession() {
        OkHttpUtils.postJson(ApiDomain.GET_ING_SESSION + "?sourceType=0", new GsonResponseHandler<ServiceEntity>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
            }

            @Override
            public void onSuccess(ServiceEntity serviceEntity) {
                getIngSessionSuccess(serviceEntity);
            }
        });
    }

    public void getIngSessionSuccess(ServiceEntity entity) {
        if (entity == null) {
            return;
        }
        this.mServiceEntity = entity;
        layoutTip.setVisibility(entity.getUnread() > 0 ? View.VISIBLE : View.GONE);
        tv_unread_count.setText(String.valueOf(entity.getUnread()));
    }


    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration decor = new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.color_EE)
                .build();
        recyclerView.addItemDecoration(decor);

        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        mAdapter = new RelativeQuestionAdapter(dataList);
        View emptyView = LayoutInflater.from(this).inflate(R.layout.service_view_empty, null);
        mAdapter.setEmptyView(emptyView);
        recyclerView.setAdapter(mAdapter);
    }

    private void requestData(boolean isPullDown) {
        if (isPullDown) {
            pageIndex = 1;
        } else {
            pageIndex++;
        }
        getP().getData(pageIndex, question.getId(), isPullDown);
    }


    public void getDataSuccess(PageBean<Question> pageBean, boolean isPullDown) {
        stopRefreshLoad();

        if (isPullDown) {
            dataList.clear();
        }
        if (pageBean == null || pageBean.getDatas().isEmpty()) {
            mAdapter.getLoadMoreModule().loadMoreEnd(true);
            if (isPullDown) {
                mAdapter.notifyDataSetChanged();
            }
        } else {
            dataList.addAll(pageBean.getDatas());
            mAdapter.notifyDataSetChanged();
            if (pageBean.getPage() == pageBean.getPageNum()) {
                mAdapter.getLoadMoreModule().loadMoreEnd(true);
            }
        }
    }

    public void getDataFailed(String message) {
        stopRefreshLoad();
        mAdapter.notifyDataSetChanged();
    }

    private void stopRefreshLoad() {
        if (isLoadingMore) {
            isLoadingMore = false;
        }
        mAdapter.getLoadMoreModule().loadMoreComplete();
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_dealed) {
            getP().questionHelpFeedback(question, true);
        } else if (id == R.id.tv_undeal) {
            getP().questionHelpFeedback(question, false);
        }
    }

    public void resolveReportSuccess(boolean isSolved) {
        if (isSolved) {
            tv_dealed.setSelected(true);
            tv_dealed.setClickable(false);
            tv_dealed.setDrawable(DrawableTextView.LEFT, R.mipmap.service_ic_dealed_selected, SizeUtils.dp2px(15), SizeUtils.dp2px(15));
            tv_undeal.setSelected(false);
            tv_undeal.setClickable(true);
            tv_undeal.setDrawable(DrawableTextView.LEFT, R.mipmap.service_ic_no_deal_selected, SizeUtils.dp2px(15), SizeUtils.dp2px(15));
        } else {
            tv_dealed.setSelected(false);
            tv_dealed.setClickable(true);
            tv_dealed.setDrawable(DrawableTextView.LEFT, R.mipmap.service_ic_dealed_selected, SizeUtils.dp2px(15), SizeUtils.dp2px(15));
            tv_undeal.setSelected(true);
            tv_undeal.setClickable(false);
            tv_undeal.setDrawable(DrawableTextView.LEFT, R.mipmap.service_ic_no_deal_selected, SizeUtils.dp2px(15), SizeUtils.dp2px(15));
        }
    }

    @Subscribe
    public void receiveMessage(Message message) {
        if (mServiceEntity == null || !mServiceEntity.getGroupId().equals(message.getGroupId())) {
            return;
        }
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_SESSION_END) {
            mServiceEntity.setUnread(0);
        } else {
            mServiceEntity.setUnread(mServiceEntity.getUnread() + 1);
        }
        getIngSessionSuccess(mServiceEntity);
    }

}
