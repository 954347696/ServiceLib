package com.keepfun.login.net;

import android.text.TextUtils;

import com.keepfun.blankj.util.LogUtils;
import com.keepfun.aiservice.network.HTTPServer;
import com.keepfun.aiservice.network.HttpAdvancedRequest;
import com.keepfun.blankj.util.ToastUtils;
import com.keepfun.login.Constant;
import com.keepfun.login.LoginApiDomain;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class KeepfunLoginTask implements Callable<String> {

    private String authData = null;
    private String password = null;
    private String phone = null;
    private String countryId = null;

    public KeepfunLoginTask(String phone, String password, String countryId) {
        this.phone = phone;
        this.password = password;
        this.countryId = countryId;
    }

    @Override
    public String call() {
        //开始上传数据
        HTTPServer.getInstance().doRequest(new HttpAdvancedRequest<String>() {
            @Override
            public String getUrl() {
                return Constant.loginUrl + LoginApiDomain.LOGIN_PWD;
            }

            @Override
            public HashMap<String, Object> getParams() {
                HashMap<String, Object> params = new HashMap<>();
                params.put("password", password);
                params.put("phone", phone);
                params.put("countryId", countryId);
                return params;
            }

            @Override
            public HashMap<String, Object> getHeader() {
                HashMap<String, Object> params = new HashMap<>();
                params.put("appCode", "keepfun_sportlive");
                params.put("terminalType", "1");
                return params;
            }

            @Override
            public AdvancedResponseHandler<String> getAdvancedResponseHandler() {
                return new AdvancedResponseHandler<String>() {
                    @Override
                    public void onRequestSuccess(String response) {
                        if (response == null || TextUtils.isEmpty(response)) {
                            LogUtils.e("AppKey认证失败 , 返回数据出错");
                        } else {
                            LogUtils.e("AppKey认证通过");
                            authData = response;
                        }
                    }

                    @Override
                    public void onRequestFailure(int statusCode, String response) {
                        LogUtils.e("AppKey认证失败" + (TextUtils.isEmpty(response) ? ",请联系相关技术人员" : " ERROR=" + response));
                    }
                };
            }
        }, false);
        return authData;
    }
}
