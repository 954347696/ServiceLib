package com.keepfun.login.net;

import android.text.TextUtils;

import com.keepfun.aiservice.network.HTTPServer;
import com.keepfun.aiservice.network.HttpAdvancedRequest;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.ToastUtils;
import com.keepfun.login.Constant;
import com.keepfun.login.LoginApiDomain;
import com.keepfun.login.entity.UserBasicInfoBean;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class KeepfunUserInfoTask implements Callable<UserBasicInfoBean> {

    private UserBasicInfoBean authData = null;
    private String token;

    public KeepfunUserInfoTask(String token) {
        this.token = token;
    }

    @Override
    public UserBasicInfoBean call() {
        //开始上传数据
        HTTPServer.getInstance().doGetRequest(new HttpAdvancedRequest<UserBasicInfoBean>() {
            @Override
            public String getUrl() {
                return Constant.loginUrl + LoginApiDomain.USER_BASE_INFO;
            }

            @Override
            public HashMap<String, Object> getHeader() {
                HashMap<String, Object> params = new HashMap<>();
                params.put("token", token);
                params.put("appCode", "keepfun_sportlive");
                params.put("terminalType", "1");
                params.put("Content-Type", "application/json;charset=utf-8");
                return params;
            }

            @Override
            public AdvancedResponseHandler<UserBasicInfoBean> getAdvancedResponseHandler() {
                return new AdvancedResponseHandler<UserBasicInfoBean>() {
                    @Override
                    public void onRequestSuccess(UserBasicInfoBean response) {
                        if (response == null) {
                            LogUtils.e("用户信息获取失败 , 返回数据出错");
                        } else {
                            LogUtils.e("用户信息获取通过");
                            authData = response;
                        }
                    }

                    @Override
                    public void onRequestFailure(int statusCode, String response) {
                        LogUtils.e("用户信息获取失败" + (TextUtils.isEmpty(response) ? ",请联系相关技术人员" : " ERROR=" + response));
                        ToastUtils.showShort(response);
                    }
                };
            }
        }, false);
        return authData;
    }
}
