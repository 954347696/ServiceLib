package com.keepfun.aiservice.ui.act;

import android.os.Bundle;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.entity.RedDrawData;
import com.keepfun.aiservice.entity.ServiceBean;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.adapter.RedPacketsItemAdapter;
import com.keepfun.aiservice.ui.adapter.RefreshAdapter;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.ui.view.roundedimageview.RoundedImageView;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.CollectionUtils;
import com.keepfun.blankj.util.LogUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class RedPacketsActivity extends PanActivity implements View.OnClickListener {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView roomAdminRecycler;
    ImageView backImageView;
    RoundedImageView userImageView;
    TextView userName;
    TextView redPacketsRemain;
    boolean isSender = false;
    private ArrayList<RedDrawData.DrawRecordPageVoBean.RedDrawBean> redPacketsDataArrayList;
    private RedPacketsItemAdapter redPacketsItemAdapter;
    private boolean isLoadingMore = false;
    private int pageIndex = 1;
    private long redId = 18;

    @Override
    public int getLayoutId() {
        return R.layout.activity_redpackets;
    }

    @Override
    public void bindUI(View rootView) {
        swipeRefreshLayout = findViewById(R.id.red_packet_swipe);
        roomAdminRecycler = findViewById(R.id.redpackets_recycler);
        backImageView = findViewById(R.id.back_image);
        userImageView = findViewById(R.id.user_ImageView);
        userName = findViewById(R.id.user_name);
        redPacketsRemain = findViewById(R.id.redpackets_remain);
    }

    @Override
    public void bindEvent() {
        findViewById(R.id.back_image).setOnClickListener(new CheckClickListener(this));
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            redId = bundle.getLong("redId");
        }
        swipeRefreshLayout.setColorSchemeResources(R.color.color_480ed9, R.color.color_981de7);
        redPacketsDataArrayList = new ArrayList<>();
        redPacketsItemAdapter = new RedPacketsItemAdapter(getContext(), redPacketsDataArrayList);
        roomAdminRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        roomAdminRecycler.setAdapter(redPacketsItemAdapter);

        roomAdminRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing() && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (redPacketsItemAdapter.getItemCount() > 0 && !recyclerView.canScrollVertically(LayoutDirection.RTL) && !isLoadingMore) {
                        redPacketsItemAdapter.changeMoreStatus(RefreshAdapter.LOADING_MORE);
                        isLoadingMore = true;
                        roomAdminRecycler.postDelayed(() -> {
                            lookRedPackets(false);
                        }, 1500);
                    }
                }
            }
        });


        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (isLoadingMore) {
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
            lookRedPackets(true);
        });

        lookRedPackets(true);
    }


    /**
     * 查看红包领取信息
     */
    public synchronized void lookRedPackets(boolean isPullDown) {
        if (isPullDown) {
            pageIndex = 1;
        }
        LogUtils.d(TAG, "---look red packet:" + redId);

        HashMap<String, Object> params = new HashMap<>();
        params.put("size", 20);
        params.put("page", pageIndex);
        HashMap<String, Object> param = new HashMap<>();
        param.put("redId", redId);
        params.put("param", param);
        OkHttpUtils.postJson(ApiDomain.REDPACKET_DETAIL, params, new GsonResponseHandler<RedDrawData>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                stopRefreshLoad();
                showEmpty();
                redPacketsItemAdapter.changeMoreStatus(RefreshAdapter.INIT_NO_DATA);
            }

            @Override
            public void onSuccess(RedDrawData redDrawData) {
                stopRefreshLoad();
                if (redDrawData != null) {
                    updateUI(redDrawData);
                    RedDrawData.DrawRecordPageVoBean drawRecordPageVoBean = redDrawData.getDrawRecordPageVo();
                    if (drawRecordPageVoBean != null) {
                        List<RedDrawData.DrawRecordPageVoBean.RedDrawBean> redDrawBeans = drawRecordPageVoBean.getDatas();
                        if (!CollectionUtils.isEmpty(redDrawBeans)) {
                            if (isPullDown) {
                                redPacketsDataArrayList.clear();
                                redPacketsDataArrayList.addAll(redDrawBeans);
                                redPacketsItemAdapter.notifyDataSetChanged();
                            } else {
                                int oldPosition = redPacketsDataArrayList.size();
                                redPacketsDataArrayList.addAll(redDrawBeans);
                                redPacketsItemAdapter.notifyItemRangeChanged(oldPosition, redDrawBeans.size());
                            }
                            pageIndex++;
                            redPacketsItemAdapter.changeMoreStatus(RefreshAdapter.PULL_UP_LOAD_MORE);
                        } else {
                            if (isPullDown) {
                                redPacketsDataArrayList.clear();
                            }
                            redPacketsItemAdapter.notifyDataSetChanged();
                            redPacketsItemAdapter.changeMoreStatus(RefreshAdapter.INIT_NO_DATA);
                        }
                    }
                } else {
                    showEmpty();
                    redPacketsItemAdapter.changeMoreStatus(RefreshAdapter.INIT_NO_DATA);
                }
            }
        });
    }

    private void updateUI(RedDrawData redDrawData) {
        Log.d(TAG, "updateUI: 1");

        if (redDrawData == null) {
            return;
        }
        RedDrawData.RedSendUserVoBean redSendUserVoBean = redDrawData.getRedSendUserVo();
        if (redSendUserVoBean != null) {
            userName.setText(redSendUserVoBean.getNickname() + "的红包");
            Glide.with(getContext()).asDrawable().load(redSendUserVoBean.getAvatar())
                    .placeholder(R.mipmap.service_bg_chat_default)
                    .into(userImageView);
        }

        String temp = String.format("领取 %d/%d个", redDrawData.getHasDrawTotalNumber(), redDrawData.getTotalNumber());
        Log.d(TAG, "updateUI: " + temp);
        redPacketsRemain.setText(temp);
    }

    private void stopRefreshLoad() {
        Log.d(TAG, "stopRefreshLoad: ");
        if (isLoadingMore) {
            isLoadingMore = false;
        }
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void showEmpty() {
        if (redPacketsItemAdapter != null) {
            if (redPacketsItemAdapter.getItemCount() == 0) {
                redPacketsItemAdapter.changeMoreStatus(RefreshAdapter.EMPTY_DATA);
            }
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.back_image) {
            finish();
        }
    }
}
