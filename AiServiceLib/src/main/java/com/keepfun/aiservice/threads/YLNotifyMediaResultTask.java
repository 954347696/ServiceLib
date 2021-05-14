package com.keepfun.aiservice.threads;

import android.text.TextUtils;

import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.YlAuthData;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.network.HTTPServer;
import com.keepfun.aiservice.network.HttpAdvancedRequest;
import com.keepfun.aiservice.utils.SignUtils;
import com.keepfun.blankj.util.LogUtils;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class YLNotifyMediaResultTask implements Callable<Boolean> {

    private Boolean authData = null;

    private String groupId;
    /**
     * 通话类型: 1-音频; 2-视频
     */
    private int callType;
    /**
     * 操作类型(0-接通，1-拒绝，2-挂断)
     */
    private int operateType;

    public YLNotifyMediaResultTask(String groupId, int callType, int operateType) {
        this.groupId = groupId;
        this.callType = callType;
        this.operateType = operateType;
    }

    @Override
    public Boolean call() {
        //开始上传数据
        HTTPServer.getInstance().doGetRequest(new HttpAdvancedRequest<Boolean>() {
            @Override
            public String getUrl() {
                String url = YLConstant.BASE_URL + ApiDomain.NOTIFY_MEDIA_RESULT + "?groupId=" + groupId + "&callType=" + callType + "&operateType="+operateType;
                return url;
            }

            @Override
            public AdvancedResponseHandler<Boolean> getAdvancedResponseHandler() {
                return new AdvancedResponseHandler<Boolean>() {
                    @Override
                    public void onRequestSuccess(Boolean response) {
                        if (response == null) {
                            LogUtils.e("AppKey认证失败 , 返回数据出错");
                            authData = false;
                        } else {
                            LogUtils.e("AppKey认证通过");
                            authData = response;
                        }
                    }

                    @Override
                    public void onRequestFailure(int statusCode, String response) {
                        LogUtils.e("AppKey认证失败" + (TextUtils.isEmpty(response) ? ",请联系相关技术人员" : " ERROR=" + response));
                        authData = false;
                    }
                };
            }
        }, false);
        return authData;
    }
}
