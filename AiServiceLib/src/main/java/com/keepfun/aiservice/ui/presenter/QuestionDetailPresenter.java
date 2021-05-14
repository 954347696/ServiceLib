package com.keepfun.aiservice.ui.presenter;

import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.entity.AppCommQsRelationBean;
import com.keepfun.aiservice.entity.PageBean;
import com.keepfun.aiservice.entity.Question;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.act.QuestionDetailActivity;
import com.keepfun.base.PanPresenter;

import java.util.HashMap;

/**
 * @author yang
 * @description
 * @date 2020/8/28 3:14 PM
 */
public class QuestionDetailPresenter extends PanPresenter<QuestionDetailActivity> {


    public void getData(int pageIndex, long questionId, boolean isPull) {
        HashMap<String, Object> params = new HashMap<>();
        AppCommQsRelationBean param = new AppCommQsRelationBean();
        param.setQuestionId(questionId);
        params.put(Arguments.PAGE, pageIndex);
        params.put(Arguments.SIZE, 20);
        params.put(Arguments.PARAM, param);
        OkHttpUtils.postJson(ApiDomain.RELATION_QUESTION_LIST, params, new GsonResponseHandler<PageBean<Question>>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                getV().getDataFailed("");
            }

            @Override
            public void onSuccess(PageBean<Question> pageBean) {
                getV().getDataSuccess(pageBean, isPull);
            }
        });
    }

    public void questionHelpFeedback(Question question, boolean isSolved) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(Arguments.IS_SOLVED, isSolved ? 1 : 0);
        params.put(Arguments.QUESTION_ID, question.getId());
        OkHttpUtils.postJson(ApiDomain.QUESTION_HELP_FEEDBACK, params, new GsonResponseHandler<Boolean>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(Boolean response) {
                getV().resolveReportSuccess(isSolved);
            }
        });
    }
}
