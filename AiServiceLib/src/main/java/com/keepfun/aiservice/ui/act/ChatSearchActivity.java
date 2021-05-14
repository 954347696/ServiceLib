package com.keepfun.aiservice.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.entity.PageBean;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.adapter.ChatHistoryAdapter;
import com.keepfun.aiservice.ui.divider.HorizontalDividerItemDecoration;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.KeyboardUtils;
import com.keepfun.blankj.util.SizeUtils;
import com.keepfun.blankj.util.StringUtils;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * @author yang
 * @description
 * @date 2020/12/14 7:52 PM
 */
public class ChatSearchActivity extends PanActivity implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.service_activity_chat_search;
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    private static final int REQUEST_CALENDAR = 0x001;

    private EditText et_keywords;
    private ImageView iv_clear;
    private RecyclerView rv_chat_history;

    private String groupId;
    private int type = 1;//1:单聊：2 群聊
    private ChatHistoryAdapter mAdapter;
    private List<Message> mData;
    private boolean isLoadingMore = false;
    private int pageIndex = 1;

    private String date;

    @Override
    public void bindUI(View rootView) {
        et_keywords = findViewById(R.id.et_keywords);
        iv_clear = findViewById(R.id.iv_clear);
        rv_chat_history = findViewById(R.id.rv_chat_history);
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            groupId = bundle.getString(Arguments.DATA);
            type = bundle.getInt(Arguments.DATA1);
        }

        rv_chat_history.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration decoration = new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.color_EE)
                .size(SizeUtils.dp2px(1f))
                .margin(SizeUtils.dp2px(14))
                .build();
        rv_chat_history.addItemDecoration(decoration);

        if (mData == null) {
            mData = new ArrayList<>();
        }
        mAdapter = new ChatHistoryAdapter(mData);
        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.service_view_empty, null);
        TextView tv_empty = emptyView.findViewById(R.id.tv_empty);
        tv_empty.setText("暂无数据");
        mAdapter.setEmptyView(emptyView);
        mAdapter.getLoadMoreModule().setPreLoadNumber(2);
        rv_chat_history.setAdapter(mAdapter);

        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatSearchActivity activity = (ChatSearchActivity) getContext();
                KeyboardUtils.hideSoftInput(activity);
            }
        });
    }

    @Override
    public void bindEvent() {
        iv_clear.setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.tv_cancel).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.tv_select_date).setOnClickListener(new CheckClickListener(this));
        et_keywords.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = et_keywords.getText().toString().trim();
                iv_clear.setVisibility(StringUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
            }
        });
        et_keywords.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                KeyboardUtils.hideSoftInput(this);
                requestData(true);
            }
            return false;
        });
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            if (!isLoadingMore) {
                requestData(false);
            }
        });
    }

    private synchronized void requestData(boolean isPullDown) {
        mAdapter.getLoadMoreModule().setEnableLoadMore(true);
        if (isPullDown) {
            pageIndex = 1;
        } else {
            pageIndex++;
        }
        isLoadingMore = true;
        mAdapter.getLoadMoreModule().setEnableLoadMore(true);

        HashMap<String, Object> params = new HashMap<>();
        params.put(Arguments.PAGE, pageIndex);
        params.put(Arguments.SIZE, 20);
        HashMap<String, Object> param = new HashMap<>();

        GsonResponseHandler<PageBean<Message>> gsonResponseHandler = new GsonResponseHandler<PageBean<Message>>() {
            @Override
            public void onSuccess(PageBean<Message> pageBean) {
                getDataSuccess(pageBean, isPullDown);
            }

            @Override
            public void onFailure(String statusCode, String error_msg) {
                getDataFailed("");
            }
        };

        if (type == 2) {
            String content = et_keywords.getText().toString().trim();
            if (!StringUtils.isEmpty(content)) {
                param.put(Arguments.KEYWORD, content);
                mAdapter.setKeywords(content);
            }
            if (!StringUtils.isEmpty(date)) {
                param.put(Arguments.DATE, date);
            }
            param.put(Arguments.ID, groupId);
            param.put("imToken", GlobalDataHelper.getInstance().getImToken());
            params.put(Arguments.PARAM, param);
            OkHttpUtils.postJson(this, ApiDomain.GROUP_MESSAGE_LIST, params, gsonResponseHandler);
        } else {
            String content = et_keywords.getText().toString().trim();
            if (!StringUtils.isEmpty(content)) {
                param.put("searchWords", mAdapter.getKeywords());
                mAdapter.setKeywords(content);
            }
            if (!StringUtils.isEmpty(date)) {
                param.put("searchStartTime", date);
                param.put("searchEndTime", date);
            }
            if (GlobalDataHelper.getInstance().getImUserInfo() != null) {
                param.put("userId", GlobalDataHelper.getInstance().getImUserInfo().getId());
            }
            param.put("csUserId", groupId);
            params.put(Arguments.PARAM, param);
            OkHttpUtils.postJson(this, ApiDomain.ONLINE_HISTORY, params, gsonResponseHandler);
        }
    }


    public void getDataSuccess(PageBean<Message> pageBean, boolean isPullDown) {
        stopRefreshLoad();

        if (isPullDown) {
            mData.clear();
        }
        if (pageBean == null || pageBean.getDatas().isEmpty()) {
            mAdapter.getLoadMoreModule().loadMoreEnd(true);
            if (isPullDown) {
                mAdapter.notifyDataSetChanged();
            }
        } else {
            List<Message> list = new ArrayList<>();
            for (Message message : pageBean.getDatas()) {
                if (message.isInHistory() && message.getContentType() == 1) {
                    list.add(0, message);
                }
            }
            mData.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void getDataFailed(String message) {
        stopRefreshLoad();
        mAdapter.notifyDataSetChanged();
        mAdapter.getLoadMoreModule().loadMoreEnd(true);
    }

    private void stopRefreshLoad() {
        if (isLoadingMore) {
            isLoadingMore = false;
        }
        mAdapter.getLoadMoreModule().loadMoreComplete();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_clear) {
            et_keywords.setText("");
        } else if (id == R.id.tv_cancel) {
            et_keywords.setText("");
            mAdapter.setKeywords("");
            date = null;
            mData.clear();
            mAdapter.notifyDataSetChanged();
        } else if (id == R.id.tv_select_date) {
            ActivityUtils.startActivityForResult(this, CalendarActivity.class, REQUEST_CALENDAR);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK && data == null) {
            return;
        }
        if (requestCode == REQUEST_CALENDAR) {
            date = data.getStringExtra(Arguments.DATA);
            requestData(true);
        }
    }

    @Subscribe
    public void receiveMsg(Message message) {
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_SESSION_CANCEL) {
            if (groupId.equals(message.getGroupId())) {
                finish();
            }
        }
    }

}
