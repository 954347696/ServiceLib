package com.keepfun.aiservice.ui.presenter;

import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.entity.PageBean;
import com.keepfun.aiservice.entity.StatusBean;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.act.ChatGroupActivity;
import com.keepfun.base.PanPresenter;

import java.util.HashMap;

/**
 * @author yang
 * @description
 * @date 2020/12/16 4:52 PM
 */
public class ChatGroupPresenter extends PanPresenter<ChatGroupActivity> {


    public synchronized void getHistory(String groupId, int pageIndex, boolean isPull) {
        HashMap<String, Object> params = new HashMap<>();
        HashMap<String, Object> param = new HashMap<>();
        if (GlobalDataHelper.getInstance().getImUserInfo() != null) {
            param.put("userId", GlobalDataHelper.getInstance().getImUserInfo().getId());
        }
        param.put("imToken", GlobalDataHelper.getInstance().getImToken());
        param.put(Arguments.ID, groupId);
        params.put(Arguments.PAGE, pageIndex);
        params.put(Arguments.SIZE, 20);
        params.put(Arguments.PARAM, param);
        OkHttpUtils.postJson(getV(), ApiDomain.GROUP_MESSAGE_LIST, params, new GsonResponseHandler<PageBean<Message>>() {
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

    public void getSilentStatus(String groupId) {
        OkHttpUtils.get(getV(), ApiDomain.GET_GROUP_SILENT_STATUS + "?" + Arguments.ID + "=" + groupId, null, new GsonResponseHandler<StatusBean>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(StatusBean response) {
                getV().setSilentStatus(response);
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
