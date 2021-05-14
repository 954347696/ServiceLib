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

public class KeepfunGetSmsTask implements Callable<Boolean> {

    private Boolean authData = null;
    private String phone = null;
    private String countryId = null;

    public KeepfunGetSmsTask(String phone, String countryId) {
        this.phone = phone;
        this.countryId = countryId;
    }

    @Override
    public Boolean call() {
        //开始上传数据
        HTTPServer.getInstance().doRequest(new HttpAdvancedRequest() {
            @Override
            public String getUrl() {
                return Constant.loginUrl + LoginApiDomain.GET_SMS_CODE;
            }

            @Override
            public HashMap<String, Object> getParams() {
                HashMap<String, Object> params = new HashMap<>();
                params.put("useType", 2);
                params.put("phone", phone);
                params.put("countryId", countryId);
                return params;
            }

            @Override
            public HashMap<String, Object> getHeader() {
                HashMap<String, Object> params = new HashMap<>();
                params.put("appCode", "keepfun_sportlive");
                params.put("Content-Type", "application/json;charset=utf-8");
                params.put("terminalType", "1");
                return params;
            }

            @Override
            public AdvancedResponseHandler getAdvancedResponseHandler() {
                return new AdvancedResponseHandler() {
                    @Override
                    public void onRequestSuccess(Object response) {
                        LogUtils.e("AppKey认证通过");
                        authData = true;
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
