package com.keepfun.aiservice.ui.presenter;

import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.entity.FeedbackDetail;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.act.FeedbackDetailActivity;
import com.keepfun.base.PanPresenter;
import com.keepfun.blankj.util.NetworkUtils;

/**
 * @author yang
 * @description
 * @date 2020/9/4 5:22 PM
 */
public class FeedbackDetailPresenter extends PanPresenter<FeedbackDetailActivity> {

    public void getFeedbackDetail(long id) {
        OkHttpUtils.get(ApiDomain.FEEDBACK_DETAIL + id, new GsonResponseHandler<FeedbackDetail>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(FeedbackDetail feedbackDetail) {
                getV().getDetailSuccess(feedbackDetail);
            }
        });
    }

    public void feedbackSolved(long feedbackId) {
        if (!NetworkUtils.isConnected()) {
            getV().showToast("网络不可用");
            return;
        }
        OkHttpUtils.postJson(ApiDomain.FEEDBACK_FINISH + "?id=" + feedbackId, new GsonResponseHandler<Boolean>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(Boolean response) {
                getV().feedbackSolved();
            }
        });
    }
}
