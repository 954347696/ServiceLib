package com.keepfun.aiservice.ui.presenter;

import androidx.annotation.Nullable;

import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.CreateSessionResp;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.entity.PageBean;
import com.keepfun.aiservice.entity.ServiceEntity;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.act.OnDutyActivity;
import com.keepfun.base.PanPresenter;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.easyphotos.models.album.entity.Photo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/12/16 4:52 PM
 */
public class OnDutyPresenter extends PanPresenter<OnDutyActivity> {


    public synchronized void getHistory(long serviceId, int pageIndex, boolean isPull) {
        HashMap<String, Object> params = new HashMap<>();
        HashMap<String, Object> param = new HashMap<>();
        if (GlobalDataHelper.getInstance().getImUserInfo() != null) {
            param.put("userId", GlobalDataHelper.getInstance().getImUserInfo().getId());
        }
        param.put("csUserId", serviceId);
        params.put(Arguments.PAGE, pageIndex);
        params.put(Arguments.SIZE, 20);
        params.put(Arguments.PARAM, param);
        OkHttpUtils.postJson(getV(), ApiDomain.ONLINE_HISTORY, params, new GsonResponseHandler<PageBean<Message>>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                getV().getDataFailed("");
            }

            @Override
            public void onSuccess(PageBean<Message> pageBean) {
                getV().getDataSuccess(pageBean, isPull);
            }
        });

    }

    public void getUnReadMessages(String groupId) {
        if (StringUtils.isEmpty(groupId)) {
            return;
        }
        OkHttpUtils.postJson(ApiDomain.GET_UNREAD_MESSAGES + "?groupId=" + groupId, new GsonResponseHandler<List<Message>>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                getV().getUnReadList(new ArrayList<>());
            }

            @Override
            public void onSuccess(List<Message> pageBean) {
                getV().getUnReadList(pageBean);
            }
        });
    }

    /**
     * @param serviceEntity
     * @param message
     */
    public void createSession(ServiceEntity serviceEntity, @Nullable Message message, int mediaType) {
        String url = ApiDomain.CREATE_SESSION + "?" + Arguments.SERVICE_ID + "=" + serviceEntity.getId()
                + "&" + Arguments.SHOP_ID + "=" + serviceEntity.getShopId() + "&" + Arguments.WAIT_NO + "=" + serviceEntity.getWaitNo()
                + "&" + Arguments.SERVICE_TYPE + "=" + (serviceEntity.getConversationType() == YLConstant.ChatType.CHAT_TYPE_AI ? 0 : 1);
        if (!StringUtils.isEmpty(GlobalDataHelper.getInstance().getSourcePage())) {
            url += "&sourcePage=" + GlobalDataHelper.getInstance().getSourcePage();
        }
        OkHttpUtils.postJson(url, new GsonResponseHandler<CreateSessionResp>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                getV().showToast("会话创建失败");
            }

            @Override
            public void onSuccess(CreateSessionResp result) {
                serviceEntity.setGroupId(result.getGroupId());
                serviceEntity.setWaitNo(result.getWaitNo());
                getV().createSessionSuccess(serviceEntity, message, mediaType, true);
            }
        });
    }


    public void uploadImage(Photo photo) {
        OkHttpUtils.upload(getV(), ApiDomain.UPLOAD_FILE, photo.path, new GsonResponseHandler<String>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                getV().showToast("可能由于网络问题上传失败，请稍后重试或者尝试减小上传大小");
            }

            @Override
            public void onSuccess(String result) {
                if (photo.type.contains("video")) {
                    getV().sendMessage(result, YLConstant.ContentType.CONTENT_TYPE_VIDEO, photo.duration);
                } else {
                    getV().sendMessage(result, YLConstant.ContentType.CONTENT_TYPE_PIC);
                }
            }
        });
    }

    public void uploadFile(String path, long duration) {
        OkHttpUtils.upload(getV(), ApiDomain.UPLOAD_FILE, path, new GsonResponseHandler<String>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                getV().showToast("可能由于网络问题上传失败，请稍后重试或者尝试减小上传大小");
            }

            @Override
            public void onSuccess(String result) {
                getV().sendMessage(result, YLConstant.ContentType.CONTENT_TYPE_VOICE, duration);
            }
        });
    }

}
