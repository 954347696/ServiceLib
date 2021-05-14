package com.keepfun.aiservice.ui.presenter;

import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.entity.FeedbackBo;
import com.keepfun.aiservice.entity.FeedbackFile;
import com.keepfun.aiservice.entity.FeedbackLabelBean;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.act.FeedbackEditActivity;
import com.keepfun.aiservice.utils.JsonUtil;
import com.keepfun.base.PanPresenter;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.NetworkUtils;
import com.keepfun.easyphotos.models.album.entity.Photo;

import java.util.List;


/**
 * @author yang
 * @description
 * @date 2020/8/28 3:14 PM
 */
public class FeedbackEditPresenter extends PanPresenter<FeedbackEditActivity> {


    public void getLabels() {
        OkHttpUtils.postJson(ApiDomain.FEEDBACK_LABELS, new GsonResponseHandler<List<FeedbackLabelBean>>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(List<FeedbackLabelBean> labels) {
                getV().getLabelsSuccess(labels);
            }
        });
    }

    public void uploadImage(Photo photo) {
        if (!NetworkUtils.isConnected()) {
            getV().showToast("没有可用的网络");
            return;
        }
        getV().showUploadDialog();
        LogUtils.e("uploadImage start : " + System.currentTimeMillis());
        OkHttpUtils.upload(getV(), ApiDomain.UPLOAD_FILE, photo.path, new GsonResponseHandler<String>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                getV().showToast("可能由于网络问题上传失败，请稍后重试或者尝试减小上传大小");
            }

            @Override
            public void onSuccess(String imageUrl) {
                getV().uploadSuccess(new FeedbackFile(photo.type.contains("video") ? 1 : 0, imageUrl, photo.duration));
            }
        });
    }

    public void commitFeedback(long label, String info, long parentId, List<FeedbackFile> pics) {
        FeedbackBo feedbackBo = new FeedbackBo();
        feedbackBo.setFeedbackTypeId(label);
        feedbackBo.setFeedback(info);
        if (parentId != -1) {
            feedbackBo.setParentId(parentId);
        }
        feedbackBo.setFiles(pics);
        feedbackBo.setFinish(0);

        OkHttpUtils.postJson(getV(), ApiDomain.FEEDBACK_SAVE, JsonUtil.encode(feedbackBo), new GsonResponseHandler<Boolean>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(Boolean response) {
                getV().feedbackSuccess();
            }
        });

    }
}
