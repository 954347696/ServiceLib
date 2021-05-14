package com.keepfun.aiservice.ui.adapter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.keepfun.adapter.base.BaseQuickAdapter;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.AppCsStaffVo;
import com.keepfun.aiservice.entity.GroupMember;
import com.keepfun.aiservice.entity.ServiceEntity;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.act.OnDutyActivity;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.ToastUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/12/14 10:37 AM
 */
public class GroupMemberAdapter2 extends BaseQuickAdapter<GroupMember, BaseViewHolder> {

    public GroupMemberAdapter2(@Nullable List<GroupMember> data) {
        super(R.layout.service_item_group_member2, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, GroupMember item) {
        ImageView iv_avatar = holder.findView(R.id.iv_avatar);
        Glide.with(getContext()).load(item.getImgUrl()).placeholder(R.mipmap.service_bg_chat_default).into(iv_avatar);
        holder.setText(R.id.tv_nickname, item.getName());

        holder.setGone(R.id.tv_master, item.getType() != 0);
        holder.setGone(R.id.tv_manager, item.getType() != 1);

        holder.setGone(R.id.iv_status, item.getType() == 2);
        holder.setImageResource(R.id.iv_status, item.getWorkStatus() == 0 ? R.mipmap.service_ic_status_offline : (item.getWorkStatus() == 1 ? R.mipmap.service_ic_status_online : R.mipmap.service_ic_status_busy));
        holder.itemView.setOnClickListener(new CheckClickListener(v -> {
            if (item.getType() != 2) {
                getCsStaffInfo(item.getId());
            }
        }));
    }

    private void getCsStaffInfo(long csStaffId) {
        OkHttpUtils.get(ApiDomain.GET_CS_STAFF_INFO + "?csUserId=" + csStaffId, new GsonResponseHandler<AppCsStaffVo>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(AppCsStaffVo appCsStaffVo) {
                if (appCsStaffVo.getOnlineStatus() == 1) {
                    getHistorySession(appCsStaffVo.getService());
                } else if (appCsStaffVo.getOnlineStatus() == 2) {
                    ToastUtils.showShort("客服忙碌中，请选择其他客服或稍后咨询");
                } else if (appCsStaffVo.getOnlineStatus() == 0) {
                    ToastUtils.showShort("客服不在线，请选择其他客服或稍后咨询");
                }
            }
        });
    }

    private void getHistorySession(ServiceEntity serviceEntity) {
        String url = ApiDomain.HISTORY_SESSION + "?serviceId=" + serviceEntity.getId();
        OkHttpUtils.postJson(url, new GsonResponseHandler<ServiceEntity>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                startService(serviceEntity);
            }

            @Override
            public void onSuccess(ServiceEntity serviceEntity1) {
                if (serviceEntity1 != null) {
                   startService(serviceEntity1);
                } else {
                    startService(serviceEntity);
                }
            }
        });
    }

    private void startService(ServiceEntity serviceEntity) {
        Bundle bundle = new Bundle();
        serviceEntity.setAccountType(1);
        bundle.putSerializable(Arguments.DATA1, serviceEntity);
        ActivityUtils.startActivity(bundle, OnDutyActivity.class);
    }
}
