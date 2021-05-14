package com.keepfun.aiservice.ui.presenter;

import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.entity.CommQsRequestBean;
import com.keepfun.aiservice.entity.PageBean;
import com.keepfun.aiservice.entity.Question;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.fragment.CommonQuestionFragment;
import com.keepfun.base.PanPresenter;

import java.util.HashMap;

/**
 * @author yang
 * @description
 * @date 2020/8/28 3:14 PM
 */
public class QuestionFragmentPresenter extends PanPresenter<CommonQuestionFragment> {


    public synchronized void getData(int pageIndex, String categoryCode, String keyword, boolean isPull) {
        CommQsRequestBean param = new CommQsRequestBean();
        param.setKeywords(keyword);
        param.setCategoryCode(categoryCode);
        HashMap<String, Object> params = new HashMap<>();
        params.put(Arguments.PAGE, pageIndex);
        params.put(Arguments.SIZE, 20);
        params.put(Arguments.PARAM, param);
        OkHttpUtils.postJson(ApiDomain.COMM_QUESTION_LIST, params, new GsonResponseHandler<PageBean<Question>>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                getV().getDataFailed("");
            }

            @Override
            public void onSuccess(PageBean<Question> pageBean) {
                if (pageBean != null) {
                    getV().getDataSuccess(pageBean, isPull);
                } else {
                    getV().getDataFailed("");
                }
            }
        });
    }
}
