package com.keepfun.aiservice.ui.presenter;

import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.entity.EvaluateBean;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.act.ServiceEvaluationActivity;
import com.keepfun.aiservice.utils.JsonUtil;
import com.keepfun.base.PanPresenter;
import com.keepfun.blankj.util.ToastUtils;


/**
 * @author yang
 * @description
 * @date 2020/9/4 2:14 PM
 */
public class ServiceEvaluationPresenter extends PanPresenter<ServiceEvaluationActivity> {

    public void commitEvaluationInfo(EvaluateBean evaluateBean) {
        OkHttpUtils.postJson(ApiDomain.EVALUATE_SAVE, JsonUtil.encode(evaluateBean), new GsonResponseHandler<Boolean>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(Boolean response) {
                getV().evaluationSuccess();
            }
        });
    }
}
