package com.keepfun.aiservice.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.entity.PageBean;
import com.keepfun.aiservice.entity.Question;
import com.keepfun.aiservice.entity.event.QuestionDataEvent;
import com.keepfun.aiservice.ui.adapter.CommonQuestionAdapter;
import com.keepfun.aiservice.ui.divider.HorizontalDividerItemDecoration;
import com.keepfun.aiservice.ui.presenter.QuestionFragmentPresenter;
import com.keepfun.base.PanFragment;
import com.keepfun.blankj.util.SizeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/8/28 2:16 PM
 */
public class CommonQuestionFragment extends PanFragment<QuestionFragmentPresenter> {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;

    private List<Question> dataList;
    private CommonQuestionAdapter mAdapter;
    private boolean isLoadingMore = false;
    private int pageIndex = 1;
    private String type;
    private String keywords;

    public static CommonQuestionFragment getInstance(String type, String keywords) {
        CommonQuestionFragment fragment = new CommonQuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Arguments.DATA, type);
        bundle.putString(Arguments.KEYWORDS, keywords);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public QuestionFragmentPresenter newP() {
        return new QuestionFragmentPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.service_layout_single_list;
    }

    @Override
    public void bindUI(View rootView) {
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        recyclerView = rootView.findViewById(R.id.recyclerView);
    }

    @Override
    public void bindEvent() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (isLoadingMore) {
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
            requestData(true);
        });
    }

    @Override
    public void initData() {
        type = getArguments().getString(Arguments.DATA);
        keywords = getArguments().getString(Arguments.KEYWORDS);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);

        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        initRecyclerView();

        mAdapter.getLoadMoreModule().setPreLoadNumber(2);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            if (!isLoadingMore) {
                requestData(false);
            }
        });
        requestData(true);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.ItemDecoration decor = new HorizontalDividerItemDecoration.Builder(getContext())
                .colorResId(R.color.color_EE)
                .size(SizeUtils.dp2px(0.5f))
                .margin(SizeUtils.dp2px(15))
                .showLastDivider()
                .build();
        recyclerView.addItemDecoration(decor);
        mAdapter = new CommonQuestionAdapter(dataList);
        mAdapter.setKeyWords(keywords);
        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.service_view_empty, null);
        TextView tv_empty = emptyView.findViewById(R.id.tv_empty);
        tv_empty.setText("暂无数据");
        mAdapter.setEmptyView(emptyView);
        recyclerView.setAdapter(mAdapter);
    }

    private void requestData(boolean isPullDown) {
        if (isPullDown) {
            pageIndex = 1;
        } else {
            pageIndex++;
        }
        getP().getData(pageIndex, type, keywords, isPullDown);
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
        EventBus.getDefault().post(new QuestionDataEvent(type, mAdapter.getData().size() > 0));
    }

    public void getDataFailed(String message) {
        stopRefreshLoad();
        mAdapter.notifyDataSetChanged();
    }

    private void stopRefreshLoad() {
        if (isLoadingMore) {
            isLoadingMore = false;
        }
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        mAdapter.getLoadMoreModule().loadMoreComplete();
    }

}
