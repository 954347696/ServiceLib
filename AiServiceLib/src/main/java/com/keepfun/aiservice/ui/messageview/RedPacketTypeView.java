package com.keepfun.aiservice.ui.messageview;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.RedPacketConstants;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.entity.ReceiveRedPacket;
import com.keepfun.aiservice.entity.RedDrawData;
import com.keepfun.aiservice.entity.RedPacketStatusData;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.act.RedPacketsActivity;
import com.keepfun.aiservice.ui.dialog.OpenRedPacketsDialog;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.utils.JsonUtil;
import com.keepfun.blankj.util.ActivityUtils;

import java.util.HashMap;
import java.util.concurrent.FutureTask;

/**
 * @author yang
 * @description
 * @date 2021/4/20 10:25 AM
 */
public class RedPacketTypeView extends BaseMsgView {
    @Override
    public void setContent(BaseViewHolder helper, Message message) {
        helper.itemView.setOnClickListener(new CheckClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo
                checkPacket(helper.itemView.getContext(), message);
            }
        }));
    }

    private synchronized void checkPacket(Context context, Message message) {
        ReceiveRedPacket receiveRedPacket = JsonUtil.decode(message.getContent(), ReceiveRedPacket.class);

        OkHttpUtils.postJson(ApiDomain.REDPACKET_CHECK + "?redId=" + receiveRedPacket.getId(), new GsonResponseHandler<RedPacketStatusData>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(RedPacketStatusData redPacketStatusData) {
                if (redPacketStatusData != null) {
                    if (redPacketStatusData.getCurDrawStatus() == 1) {
                        showRedPacketsPop(context,message, redPacketStatusData.getRedId(), redPacketStatusData.getRedSendUserVo().getNickname(), redPacketStatusData.getRedSendUserVo().getUserId(), redPacketStatusData.getRedSendUserVo().getAvatar(),
                                RedPacketConstants.RED_PACKET_STATUS_HAS_OBTAIN, redPacketStatusData.getCurDrawAmount());
                    } else {
                        showRedPacketsPop(context,message, redPacketStatusData.getRedId(), redPacketStatusData.getRedSendUserVo().getNickname(), redPacketStatusData.getRedSendUserVo().getUserId(), redPacketStatusData.getRedSendUserVo().getAvatar(),
                                redPacketStatusData.getRedStatus(), redPacketStatusData.getCurDrawAmount());
                    }
                }
            }
        });
    }

    /**
     * redStatus红包状态(0-正常未领完 1-已领完 2-已过期)
     */
    private synchronized void showRedPacketsPop(Context context,Message message, long redId, String sendName, long sendUserId, String userAvatar, int redStatus, float amount) {
        OpenRedPacketsDialog.Builder builder = new OpenRedPacketsDialog.Builder(context, redStatus)
                .setName(sendName)
                .setImage(userAvatar);

        builder.setAmount(amount);
        builder.setFullScreen(true);
        builder.setOpenButton((DialogInterface dialog, int which) -> {
            if (redStatus == RedPacketConstants.RED_PACKET_STATUS_NORMAL) {
                int sourceType = message.getLiveId() == null ? 1 : (message.getLiveType() == 1 ? 2 : 3);
                HashMap<String, Object> params = new HashMap<>();
                params.put("redId", redId);
                params.put("token", GlobalDataHelper.getInstance().getImToken());
                params.put("imGroupId", message.getGroupId());
                params.put("sourceId", sourceType == 1 ? message.getGroupId() : String.valueOf(message.getLiveId()));
                params.put("sourceType", sourceType);
                OkHttpUtils.postJson(ApiDomain.REDPACKET_OPEN, params, new GsonResponseHandler<RedDrawData>() {
                    @Override
                    public void onFailure(String statusCode, String error_msg) {

                    }

                    @Override
                    public void onSuccess(RedDrawData redDrawData) {
                        if (redDrawData != null) {
                            int nStatus = RedPacketConstants.RED_PACKET_STATUS_HAS_OBTAIN;
                            if (redDrawData.getCurDrawStatus() == RedPacketConstants.RED_PACKET_OBTAIN_DONE) {
                                nStatus = RedPacketConstants.RED_PACKET_STATUS_HAS_OBTAIN;
                            } else if (redDrawData.getIsAllDraw().equals("1")) {
                                nStatus = RedPacketConstants.RED_PACKET_STATUS_DONE;
                            }

                            showRedPacketsPop(context,message, redId, redDrawData.getRedSendUserVo().getNickname(),
                                    redDrawData.getRedSendUserVo().getUserId(), redDrawData.getRedSendUserVo().getAvatar(),
                                    nStatus, redDrawData.getCurDrawAmount());
                        }
                    }
                });
            } else {
                HashMap<String, Object> params = new HashMap<>();
                params.put("size", 20);
                params.put("page", 1);
                HashMap<String, Object> param = new HashMap<>();
                param.put("redId", redId);
                params.put("param", param);
                OkHttpUtils.postJson(ApiDomain.REDPACKET_DETAIL, params, new GsonResponseHandler<RedDrawData>() {
                    @Override
                    public void onFailure(String statusCode, String error_msg) {
                    }

                    @Override
                    public void onSuccess(RedDrawData redDrawData) {
                        if (redDrawData != null) {
                            Bundle data = new Bundle();
                            data.putLong("redId", redId);
                            ActivityUtils.startActivity(data, RedPacketsActivity.class);
                        }
                    }
                });
            }
        });

        OpenRedPacketsDialog dialog = builder.create();
        dialog.show();
    }

}
