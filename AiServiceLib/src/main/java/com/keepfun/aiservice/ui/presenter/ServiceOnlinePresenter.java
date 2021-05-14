package com.keepfun.aiservice.ui.presenter;

import androidx.annotation.Nullable;

import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.AiServiceBean;
import com.keepfun.aiservice.entity.CreateSessionResp;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.entity.PageBean;
import com.keepfun.aiservice.entity.QsPaperSetBean;
import com.keepfun.aiservice.entity.ServiceBean;
import com.keepfun.aiservice.entity.ServiceEntity;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.network.myokhttp.response.IResponseHandler;
import com.keepfun.aiservice.ui.act.ServiceOnlineActivity;
import com.keepfun.aiservice.ui.impl.QsPaperSetCheckListener;
import com.keepfun.base.PanPresenter;
import com.keepfun.blankj.util.NetworkUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.easyphotos.models.album.entity.Photo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author yang
 * @description
 * @date 2020/9/2 5:11 PM
 */
public class ServiceOnlinePresenter extends PanPresenter<ServiceOnlineActivity> {

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

    public void checkServiceExist(int chatType, IResponseHandler handler){
        String url = ApiDomain.ACHIEVE_SERVICE + "?" + Arguments.TYPE + "=" + chatType;
        OkHttpUtils.postJson(url, handler);
    }


    /**
     * 智能机器人
     *
     * @param chatType -1 AI,0人工服务， 1VIP专线，2视频通话，3语音通话
     */
    public synchronized void getService(int chatType, boolean isBotToHuman, @Nullable Message message, int mediaType) {
        if (chatType == YLConstant.ChatType.CHAT_TYPE_AI) {
            OkHttpUtils.postJson(ApiDomain.ACHIEVE_AI_SERVICE, new GsonResponseHandler<AiServiceBean>() {
                @Override
                public void onFailure(String statusCode, String error_msg) {
                    //获取机器人客服不存在，获取人工客服
                    getService(YLConstant.ChatType.CHAT_TYPE_COMMON, isBotToHuman, message, mediaType);
                }

                @Override
                public void onSuccess(AiServiceBean aiServiceBean) {
                    createSession(chatType, aiServiceBean.getService(), message, isBotToHuman, mediaType);
                }
            });
        } else {
            String url = ApiDomain.ACHIEVE_SERVICE + "?" + Arguments.TYPE + "=" + chatType;
            if (isBotToHuman) {
                url = url + "&isBotToHuman=1";
            }
            OkHttpUtils.postJson(url, new GsonResponseHandler<ServiceBean>() {
                @Override
                public void onFailure(String statusCode, String error_msg) {
                    getV().createSessionSuccess(null, message, mediaType, true);
                }

                @Override
                public void onSuccess(ServiceBean serviceBean) {
                    createSession(chatType, serviceBean.getService(), message, isBotToHuman, mediaType);
                }
            });
        }
    }

    /**
     * @param serviceEntity
     * @param message
     */
    public void createSession(int chatType, ServiceEntity serviceEntity, @Nullable Message message, boolean isBotToHuman, int mediaType) {
        String url = ApiDomain.CREATE_SESSION + "?" + Arguments.SERVICE_ID + "=" + serviceEntity.getId()
                + "&" + Arguments.SHOP_ID + "=" + serviceEntity.getShopId() + "&" + Arguments.WAIT_NO + "=" + serviceEntity.getWaitNo()
                + "&" + Arguments.SERVICE_TYPE + "=" + (serviceEntity.getConversationType() == YLConstant.ChatType.CHAT_TYPE_AI ? 0 : 1);
        if (chatType != -1) {
            url += "&type=" + chatType;
        }
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
                if (serviceEntity.getConversationType() != YLConstant.ChatType.CHAT_TYPE_AI && isBotToHuman) {
                    reportAi2Service(result.getGroupId());
                }
            }
        });
    }

    private void reportAi2Service(String sessionId) {
        OkHttpUtils.postJson(ApiDomain.EVALUATE_MANPOWER + "?sessionId=" + sessionId, new GsonResponseHandler<Object>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(Object response) {

            }
        });
    }

    public synchronized void getHistory(int pageIndex, boolean isPull) {
        HashMap<String, Object> params = new HashMap<>();
        HashMap<String, Object> param = new HashMap<>();
        if (GlobalDataHelper.getInstance().getImUserInfo() != null) {
            param.put("userId", GlobalDataHelper.getInstance().getImUserInfo().getId());
        }
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

    public void checkQuestionnaireSwitch(int chatType, @Nullable QsPaperSetCheckListener listener) {
        if (!NetworkUtils.isConnected()) {
            getV().showToast("网络不可用，请稍候重试");
            return;
        }
        OkHttpUtils.get(ApiDomain.QUESTION_PAPER_CHECK + "?type=" + chatType, new GsonResponseHandler<QsPaperSetBean>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(QsPaperSetBean qsPaperSetBean) {
                if (qsPaperSetBean != null) {
                    listener.qsPaperSetCheck(qsPaperSetBean);
                }
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
