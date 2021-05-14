package com.keepfun.aiservice.threads;

import android.text.TextUtils;

import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.ObjectUtils;
import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.ServiceUser;
import com.keepfun.aiservice.network.HTTPServer;
import com.keepfun.aiservice.network.HttpAdvancedRequest;
import com.keepfun.blankj.util.ToastUtils;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class YLRegInfoTask implements Callable<ServiceUser> {

    private ServiceUser authData = null;
    private ServiceUser params;

    public YLRegInfoTask(ServiceUser user) {
        this.params = user;
    }


    @Override
    public ServiceUser call() {
        //开始上传数据
        HTTPServer.getInstance().doRequest(new HttpAdvancedRequest<ServiceUser>() {
            @Override
            public String getUrl() {
                return YLConstant.BASE_URL + ApiDomain.REGISTER_INFO;
            }

            @Override
            public HashMap<String, Object> getParams() {
                try {
                    return (HashMap<String, Object>) ObjectUtils.getObjectToMap(params);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public AdvancedResponseHandler<ServiceUser> getAdvancedResponseHandler() {
                return new AdvancedResponseHandler<ServiceUser>() {
                    @Override
                    public void onRequestSuccess(ServiceUser response) {
                        if (response != null) {
                            LogUtils.e("用户信息录入通过");
                            authData = response;
                        } else {
                            LogUtils.e("用户信息录入失败 , 返回数据出错");
                        }
                    }

                    @Override
                    public void onRequestFailure(int statusCode, String response) {
                        LogUtils.e("用户信息录入失败" + (TextUtils.isEmpty(response) ? ",请联系相关技术人员" : " ERROR=" + response));
                    }
                };
            }
        }, false);
        return authData;
    }
}
