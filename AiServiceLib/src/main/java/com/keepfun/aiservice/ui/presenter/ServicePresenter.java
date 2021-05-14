package com.keepfun.aiservice.ui.presenter;

import androidx.annotation.Nullable;

import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.AiServiceBean;
import com.keepfun.aiservice.entity.QsPaperSetBean;
import com.keepfun.aiservice.entity.Question;
import com.keepfun.aiservice.entity.ServiceBean;
import com.keepfun.aiservice.entity.ServiceEntity;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.fragment.ServiceHomeFragment;
import com.keepfun.aiservice.ui.impl.QsPaperSetCheckListener;
import com.keepfun.base.PanPresenter;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.NetworkUtils;
import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/9/1 7:59 PM
 */
public class ServicePresenter extends PanPresenter<ServiceHomeFragment> {

    public synchronized void getGuessQuestions() {
        OkHttpUtils.get(ApiDomain.GUESS_ASK_LIST, new GsonResponseHandler<List<Question>>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(List<Question> data) {
                if(data!=null){
                    getV().getQuestionSuccess(data);
                }
            }
        });
    }

    /**
     * 智能机器人
     *
     * @param chatType -1 AI,0人工服务， 1VIP专线，2视频通话，3语音通话
     */
    public synchronized void getService(int chatType) {
        if (chatType == YLConstant.ChatType.CHAT_TYPE_AI) {
            OkHttpUtils.postJson(ApiDomain.ACHIEVE_AI_SERVICE, new GsonResponseHandler<AiServiceBean>() {
                @Override
                public void onFailure(String statusCode, String error_msg) {
                    //获取机器人客服不存在，获取人工客服
                    getService(YLConstant.ChatType.CHAT_TYPE_COMMON);
                }

                @Override
                public void onSuccess(AiServiceBean aiServiceBean) {
                    getV().checkServiceExist(chatType);
                }
            });
        } else {
            String url = ApiDomain.ACHIEVE_SERVICE + "?" + Arguments.TYPE + "=" + chatType;
            OkHttpUtils.postJson(url, new GsonResponseHandler<ServiceBean>() {
                @Override
                public void onFailure(String statusCode, String error_msg) {
                    getV().checkServiceNoExist();
                }

                @Override
                public void onSuccess(ServiceBean response) {
                    getV().checkServiceExist(chatType);
                }
            });
        }
    }


    public void getHistorySession(int chatType) {
        LogUtils.e("getServiceResult  : ");
        if (!getV().canGo) {
            getV().dismissLoading();
            return;
        }
        getV().canGo = false;
        String url = ApiDomain.HISTORY_SESSION + "?type=" + chatType;
        OkHttpUtils.postJson(url, new GsonResponseHandler<ServiceEntity>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                getV().getServiceResult(chatType, null);
            }

            @Override
            public void onSuccess(ServiceEntity serviceEntity) {
                if (serviceEntity != null) {
                    getV().getServiceResult(chatType, serviceEntity);
                } else {
                    getV().getServiceResult(chatType, null);
                }
            }
        });
    }

    public void getIngSession() {
        OkHttpUtils.postJson(ApiDomain.GET_ING_SESSION + "?sourceType=0", new GsonResponseHandler<ServiceEntity>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(ServiceEntity serviceEntity) {
                getV().getIngSessionSuccess(serviceEntity);
            }
        });
    }


    public synchronized void checkQuestionnaireSwitch(int chatType, @Nullable QsPaperSetCheckListener listener) {
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
                listener.qsPaperSetCheck(qsPaperSetBean);
            }
        });

    }

}
