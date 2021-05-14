package com.keepfun.aiservice.ui.presenter;


import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.entity.FeedbackBean;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.act.FeedbackActivity;
import com.keepfun.base.PanPresenter;

import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/8/28 3:14 PM
 */
public class FeedbackPresenter extends PanPresenter<FeedbackActivity> {


    public synchronized void getData(int pageIndex, boolean isPull) {
        OkHttpUtils.postJson(ApiDomain.FEEDBACK_LIST, new GsonResponseHandler<List<FeedbackBean>>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                getV().getDataFailed("");
            }

            @Override
            public void onSuccess(List<FeedbackBean> feedbackBeans) {
                if (feedbackBeans != null) {
                    getV().getDataSuccess(feedbackBeans, isPull);
                } else {
                    getV().getDataFailed("");
                }
            }
        });
    }
}
