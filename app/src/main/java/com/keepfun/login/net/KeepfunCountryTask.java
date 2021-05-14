package com.keepfun.login.net;

import android.text.TextUtils;

import com.keepfun.aiservice.network.HTTPServer;
import com.keepfun.aiservice.network.HttpAdvancedRequest;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.ToastUtils;
import com.keepfun.login.Constant;
import com.keepfun.login.LoginApiDomain;
import com.keepfun.login.entity.GlCountryEntity;
import com.keepfun.login.entity.UserBasicInfoBean;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

public class KeepfunCountryTask implements Callable<List<GlCountryEntity>> {

    private List<GlCountryEntity> authData = null;

    @Override
    public List<GlCountryEntity> call() {
        //开始上传数据
        HTTPServer.getInstance().doGetRequest(new HttpAdvancedRequest<List<GlCountryEntity>>() {
            @Override
            public String getUrl() {
                return Constant.loginUrl + LoginApiDomain.GET_COUNTRY_LIST;
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
            public boolean isArray() {
                return true;
            }

            @Override
            public Class getReflectClass() {
                return GlCountryEntity.class;
            }

            @Override
            public AdvancedResponseHandler<List<GlCountryEntity>> getAdvancedResponseHandler() {
                return new AdvancedResponseHandler<List<GlCountryEntity>>() {
                    @Override
                    public void onRequestSuccess(List<GlCountryEntity> response) {
                        if (response == null) {
                            LogUtils.e("获取国家列表失败 , 返回数据出错");
                        } else {
                            LogUtils.e("获取国家列表通过");
                            authData = response;
                        }
                    }

                    @Override
                    public void onRequestFailure(int statusCode, String response) {
                        LogUtils.e("获取国家列表失败" + (TextUtils.isEmpty(response) ? ",请联系相关技术人员" : " ERROR=" + response));
                        ToastUtils.showShort(response);
                    }
                };
            }
        }, false);
        return authData;
    }
}
