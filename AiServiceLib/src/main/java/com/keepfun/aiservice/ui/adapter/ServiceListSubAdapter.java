package com.keepfun.aiservice.ui.adapter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.keepfun.adapter.base.BaseQuickAdapter;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.entity.AppCsStaffVo;
import com.keepfun.aiservice.entity.ServiceEntity;
import com.keepfun.aiservice.entity.WelcomeInfo;
import com.keepfun.aiservice.manager.ImClient;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.act.ChatGroupActivity;
import com.keepfun.aiservice.ui.act.OnDutyActivity;
import com.keepfun.aiservice.ui.act.ServiceLiveActivity;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.TimeUtils;
import com.keepfun.blankj.util.ToastUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/12/9 9:49 AM
 */
public class ServiceListSubAdapter extends BaseQuickAdapter<AppCsStaffVo, BaseViewHolder> {

    private int type;

    public ServiceListSubAdapter(@Nullable List<AppCsStaffVo> data) {
        super(R.layout.service_item_service_list_sub, data);
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, AppCsStaffVo item) {
        ImageView iv_avatar = holder.findView(R.id.iv_avatar);
        if (type == 3) {
            Glide.with(getContext()).load(item.getGroupImgUrl()).placeholder(R.mipmap.service_bg_chat_default).apply(new RequestOptions().circleCrop()).into(iv_avatar);
            holder.setGone(R.id.iv_status, true);

            holder.setText(R.id.tv_name, item.getGroupName());
            holder.setGone(R.id.tv_live_end, true);
            holder.setGone(R.id.layout_live, true);
            if (item.getMessageVo() != null) {
                holder.setVisible(R.id.layout_content, true);
                holder.setGone(R.id.tv_time, false);
                holder.setText(R.id.tv_time, TimeUtils.getFriendlyTimeSpanByNow(item.getMessageVo().getCreateTime()));
                holder.setText(R.id.tv_content, item.getMessageVo().getFromUserName() + ":" + item.getMessageVo().getShortContent());
                if (item.getMessageVo().getUnRead() > 0) {
                    holder.setVisible(R.id.tv_unread_count, true);
                    holder.setText(R.id.tv_unread_count, String.valueOf(item.getMessageVo().getUnRead()));
                } else {
                    holder.setVisible(R.id.tv_unread_count, false);
                }
            } else {
                holder.setVisible(R.id.layout_content, false);
                holder.setGone(R.id.tv_time, true);
            }
        } else if (type == 4) {
            Glide.with(getContext()).load(item.getLiveCover()).placeholder(R.mipmap.service_bg_chat_default).apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(8))).into(iv_avatar);
            holder.setGone(R.id.iv_status, true);

            holder.setText(R.id.tv_name, item.getTitle());
            holder.setGone(R.id.tv_live_end, item.getLiveStatus() != 30);
            holder.setGone(R.id.tv_time, true);

            holder.setGone(R.id.layout_live, false);
            holder.setGone(R.id.layout_content, true);
            holder.setText(R.id.tv_liveId, "ID:" + item.getLiveNo());
            holder.setText(R.id.tv_watch_count, item.getWatchNumMax() + "/" + item.getWatchingNum());
        } else {
            Glide.with(getContext()).load(item.getAvatarUrl()).placeholder(R.mipmap.service_bg_chat_default).apply(new RequestOptions().circleCrop()).into(iv_avatar);
            holder.setGone(R.id.iv_status, false);
            holder.setImageResource(R.id.iv_status, item.getOnlineStatus() == 0 ? R.mipmap.service_ic_status_offline : (item.getOnlineStatus() == 1 ? R.mipmap.service_ic_status_online : R.mipmap.service_ic_status_busy));
            holder.setText(R.id.tv_name, item.getService().getName());
            holder.setGone(R.id.tv_live_end, true);
            holder.setGone(R.id.layout_live, true);
            if (item.getMessageVo() != null) {
                holder.setGone(R.id.layout_content, false);
                holder.setGone(R.id.tv_time, false);
                holder.setText(R.id.tv_time, TimeUtils.getFriendlyTimeSpanByNow(item.getMessageVo().getCreateTime()));
                holder.setText(R.id.tv_content, item.getMessageVo().getShortContent());
                if (item.getMessageVo().getUnRead() > 0) {
                    holder.setVisible(R.id.tv_unread_count, true);
                    holder.setText(R.id.tv_unread_count, String.valueOf(item.getMessageVo().getUnRead()));
                } else {
                    holder.setVisible(R.id.tv_unread_count, false);
                }
            } else {
                holder.setGone(R.id.layout_content, true);
                holder.setGone(R.id.tv_time, true);
            }
        }

        holder.itemView.setOnClickListener(new CheckClickListener(v -> {
            if (type == 4) {
                checkWatchStatus(item.getLiveId());
            } else if (type == 3) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Arguments.DATA, item.getGroupInfo());
                ActivityUtils.startActivity(bundle, ChatGroupActivity.class);
            } else {
                if (item.getMessageVo() != null) {
                    ImClient.getInstance().setMessageRead(item.getMessageVo());
                }
                if (item.getOnlineStatus() == 1) {
                    getHistorySession(type, item.getService());
                } else if (item.getOnlineStatus() == 2) {
                    ToastUtils.showShort("客服忙碌中，请选择其他客服或稍后咨询");
                } else if (item.getOnlineStatus() == 0) {
                    ToastUtils.showShort("客服不在线，请选择其他客服或稍后咨询");
                }
            }
        }));
    }

    private void getHistorySession(int type, ServiceEntity serviceEntity) {
        String url = ApiDomain.HISTORY_SESSION + "?serviceId=" + serviceEntity.getId();
        OkHttpUtils.postJson(url, new GsonResponseHandler<ServiceEntity>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                startService(type, serviceEntity);
            }

            @Override
            public void onSuccess(ServiceEntity serviceEntity1) {
                if (serviceEntity1 != null) {
                    serviceEntity1.setTop(serviceEntity.isTop());
                    startService(type, serviceEntity1);
                } else {
                    startService(type, serviceEntity);
                }
            }
        });

    }

    private void checkWatchStatus(Long liveId) {
        OkHttpUtils.get(ApiDomain.CHECK_WATCH_STATUS + "?liveId=" + liveId, new GsonResponseHandler<WelcomeInfo>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(WelcomeInfo result) {
                Bundle bundle = new Bundle();
                bundle.putLong(Arguments.DATA, liveId);
                ActivityUtils.startActivity(bundle, ServiceLiveActivity.class);
            }
        });

    }

    private void startService(int type, ServiceEntity serviceEntity) {
        Bundle bundle = new Bundle();
        serviceEntity.setAccountType(type == 1 ? 2 : 1);
        bundle.putSerializable(Arguments.DATA1, serviceEntity);
        ActivityUtils.startActivity(bundle, OnDutyActivity.class);
    }
}
