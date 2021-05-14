package com.keepfun.aiservice.ui.presenter;


import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.entity.QuestionType;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.act.CommonQuestionActivity;
import com.keepfun.base.PanPresenter;

import java.util.List;


/**
 * @author yang
 * @description
 * @date 2020/9/3 9:42 AM
 */
public class QuestionActivityPresenter extends PanPresenter<CommonQuestionActivity> {

    public void getTabLabels() {
        OkHttpUtils.get(ApiDomain.QUESTION_TYPE_LIST, new GsonResponseHandler<List<QuestionType>>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(List<QuestionType> questionTypes) {
                getV().getLabelsSuccess(questionTypes);
            }
        });
    }
}
