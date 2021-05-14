package com.keepfun.login.net;

import android.text.TextUtils;

import com.keepfun.aiservice.network.HTTPServer;
import com.keepfun.aiservice.network.HttpAdvancedRequest;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.ToastUtils;
import com.keepfun.login.Constant;
import com.keepfun.login.LoginApiDomain;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class KeepfunSmsCodeTask implements Callable<String> {

    private String authData = null;
    private String phone = null;
    private String smsCode = null;
    private String countryId = null;

    public KeepfunSmsCodeTask(String phone, String smsCode, String countryId) {
        this.phone = phone;
        this.smsCode = smsCode;
        this.countryId = countryId;
    }

    @Override
    public String call() {
        //开始上传数据
        HTTPServer.getInstance().doRequest(new HttpAdvancedRequest<String>() {
            @Override
            public String getUrl() {
                return Constant.loginUrl + LoginApiDomain.USER_LOGIN_CODE;
            }

            @Override
            public HashMap<String, Object> getParams() {
                HashMap<String, Object> params = new HashMap<>();
                params.put("smsCode", smsCode);
                params.put("phone", phone);
                params.put("terminalTyp", 1);
                params.put("countryId", countryId);
                return params;
            }

            @Override
            public HashMap<String, Object> getHeader() {
                HashMap<String, Object> params = new HashMap<>();
                params.put("appCode", "keepfun_sportlive");
                params.put("terminalType", "1");
                params.put("Content-Type", "application/json;charset=utf-8");
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
                        ToastUtils.showShort(response);
                    }
                };
            }
        }, false);
        return authData;
    }
}
