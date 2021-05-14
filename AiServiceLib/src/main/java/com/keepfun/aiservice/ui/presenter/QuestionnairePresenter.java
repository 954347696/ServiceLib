package com.keepfun.aiservice.ui.presenter;

import android.text.TextUtils;

import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.entity.CodeImgVo;
import com.keepfun.aiservice.entity.QsPaperUploadBean;
import com.keepfun.aiservice.entity.Questionnaire;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.act.QuestionnaireActivity;
import com.keepfun.aiservice.utils.JsonUtil;
import com.keepfun.base.PanPresenter;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.NetworkUtils;

/**
 * @author yang
 * @description
 * @date 2020/9/1 4:43 PM
 */
public class QuestionnairePresenter extends PanPresenter<QuestionnaireActivity> {


    public void getQuestionnaire() {
        OkHttpUtils.get(ApiDomain.QUESTION_PAPER_CONTENT, new GsonResponseHandler<Questionnaire>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                LogUtils.e("问卷获取失败");
            }

            @Override
            public void onSuccess(Questionnaire questionnaire) {
                getV().getDataSuccess(questionnaire);
            }
        });
    }

    public void getImageCode() {
        OkHttpUtils.get(ApiDomain.GET_IMAGE_CODE, new GsonResponseHandler<CodeImgVo>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(CodeImgVo codeImgVo) {
                getV().getImageCodeSuccess(codeImgVo);
            }
        });
    }


    public void commitQuestionnaire(QsPaperUploadBean questionnaire) {
        LogUtils.e("commitQuestionnaire questionnaire : " + questionnaire);
        if (!NetworkUtils.isConnected()) {
            getV().showToast("没有有效的网络，请稍候重试");
            return;
        }
        OkHttpUtils.postJson(ApiDomain.QUESTION_PAPER_COMMIT, JsonUtil.encode(questionnaire), new GsonResponseHandler<Boolean>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(Boolean response) {
                getV().commitSuccess();
            }
        });
    }
}
