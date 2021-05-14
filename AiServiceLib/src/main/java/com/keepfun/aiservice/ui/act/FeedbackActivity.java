package com.keepfun.aiservice.ui.act;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.keepfun.aiservice.R;
import com.keepfun.aiservice.entity.FeedbackBean;
import com.keepfun.aiservice.ui.adapter.FeedbackAdapter;
import com.keepfun.aiservice.ui.presenter.FeedbackPresenter;
import com.keepfun.aiservice.ui.view.ServiceTitleView;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/8/31 4:26 PM
 */
public class FeedbackActivity extends PanActivity<FeedbackPresenter> {

    ServiceTitleView titleView;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;

    private List<FeedbackBean> dataList;
    private FeedbackAdapter mAdapter;
    private boolean isLoadingMore = false;
    private int pageIndex = 1;

    private static final int REQUEST_FOR_EDIT = 0X100;

    @Override
    public FeedbackPresenter newP() {
        return new FeedbackPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.service_activity_feedback;
    }

    @Override
    public void bindUI(View rootView) {
        titleView = findViewById(R.id.titleView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
    }

    @Override
    public void bindEvent() {
        titleView.setRightListener(v -> ActivityUtils.startActivityForResult(FeedbackActivity.this, FeedbackEditActivity.class, REQUEST_FOR_EDIT));

        swipeRefreshLayout.setOnRefreshListener(() -> {
            LogUtils.d("isRefreshing:" + swipeRefreshLayout.isRefreshing());
            if (isLoadingMore) {
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
            requestData(true);
        });
    }

    @Override
    public void initData() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);

        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        initRecyclerView();

//        mAdapter.getLoadMoreModule().setPreLoadNumber(2);
//        mAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
//            if (!isLoadingMore) {
//                requestData(false);
//            }
//        });
        requestData(true);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new FeedbackAdapter(dataList);
        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.service_view_empty, null);
        TextView tv_empty = emptyView.findViewById(R.id.tv_empty);
        tv_empty.setText("暂无意见反馈");
        mAdapter.setEmptyView(emptyView);
        recyclerView.setAdapter(mAdapter);
    }

    private void requestData(boolean isPullDown) {
        if (isPullDown) {
            pageIndex = 1;
        } else {
            pageIndex++;
        }
        getP().getData(pageIndex, isPullDown);
    }

    public void getDataSuccess(List<FeedbackBean> pageBean, boolean isPullDown) {
        stopRefreshLoad();
        if (isPullDown) {
            dataList.clear();
        }
        if (pageBean == null || pageBean.isEmpty()) {
            if (isPullDown) {
                mAdapter.notifyDataSetChanged();
            }
        } else {
            dataList.addAll(pageBean);
            mAdapter.notifyDataSetChanged();
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
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        mAdapter.getLoadMoreModule().loadMoreComplete();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FOR_EDIT && resultCode == RESULT_OK) {
            requestData(true);
        }
    }
}
