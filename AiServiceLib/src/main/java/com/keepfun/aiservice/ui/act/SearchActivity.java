package com.keepfun.aiservice.ui.act;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.keepfun.adapter.base.BaseQuickAdapter;
import com.keepfun.adapter.base.listener.OnItemClickListener;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.ui.adapter.HistoryAdapter;
import com.keepfun.aiservice.ui.divider.HorizontalDividerItemDecoration;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.ui.presenter.SearchPresenter;
import com.keepfun.aiservice.utils.SpManager;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.SizeUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.blankj.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author yang
 * @description
 * @date 2020/8/27 7:30 PM
 */
public class SearchActivity extends PanActivity<SearchPresenter> implements View.OnClickListener {

    EditText et_search;
    TextView tv_search;
    ImageView iv_delete;
    RecyclerView rv_history;

    private HistoryAdapter mHistoryAdapter;
    private List<String> mHistoryList;

    @Override
    public SearchPresenter newP() {
        return new SearchPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.service_activity_search;
    }

    @Override
    public void bindUI(View rootView) {
        et_search = findViewById(R.id.et_search);
        tv_search = findViewById(R.id.tv_search);
        iv_delete = findViewById(R.id.iv_delete);
        rv_history = findViewById(R.id.rv_history);
    }

    @Override
    public void bindEvent() {
        findViewById(R.id.iv_back).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.iv_delete).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.tv_search).setOnClickListener(new CheckClickListener(this));
    }

    @Override
    public void initData() {
        initRecyclerView();
        mHistoryAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                String item = (String) adapter.getItem(position);
                et_search.setText(item);
                search();
            }
        });
        getP().getHistory();
    }

    private void initRecyclerView() {
        rv_history.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration decor = new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.color_EE)
                .size(SizeUtils.dp2px(0.5f))
                .margin(SizeUtils.dp2px(15))
                .showLastDivider()
                .build();
        rv_history.addItemDecoration(decor);

        if (mHistoryList == null) {
            mHistoryList = new ArrayList<>();
        }
        mHistoryAdapter = new HistoryAdapter(mHistoryList);
        View emptyView = LayoutInflater.from(this).inflate(R.layout.service_view_empty, null);
        TextView tv_empty = emptyView.findViewById(R.id.tv_empty);
        tv_empty.setText(R.string.no_search_logs);
        mHistoryAdapter.setEmptyView(emptyView);
        rv_history.setAdapter(mHistoryAdapter);
    }

    public void setHistoryData(List<String> history) {
        iv_delete.setVisibility(history.isEmpty() ? View.GONE : View.VISIBLE);
        mHistoryList.clear();
        mHistoryList.addAll(history);
        mHistoryAdapter.notifyDataSetChanged();

    }


    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back) {
            onBackPressed();
        } else if (id == R.id.iv_delete) {
            SpManager.getHistory().clear();
            getP().getHistory();
        } else if (id == R.id.tv_search) {
            search();
        }
    }

    private void search() {
        String searchStr = et_search.getText().toString().trim();
        if (StringUtils.isEmpty(searchStr)) {
            ToastUtils.showShort("搜索内容不能为空");
            return;
        }
        SpManager.saveHistory(searchStr);
        getP().getHistory();
        Bundle bundle = new Bundle();
        bundle.putString(Arguments.KEYWORDS, searchStr);
        ActivityUtils.startActivity(bundle, CommonQuestionActivity.class);
    }
}
