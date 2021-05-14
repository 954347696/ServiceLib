package com.keepfun.aiservice.network;


import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.keepfun.aiservice.ServiceManager;
import com.keepfun.aiservice.ServiceSdk;
import com.keepfun.aiservice.entity.event.LoginExitEvent;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.JsonUtils;
import com.keepfun.aiservice.entity.ResponseData;
import com.keepfun.aiservice.utils.JsonUtil;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.HashMap;


/**
 * 网络请求实例类的基类，所有具体的网络请求都是他的实现
 */

public abstract class HttpAdvancedRequest<T> extends HttpBasicRequest {

    public interface AdvancedResponseHandler<T> {
        void onRequestSuccess(T response);

        void onRequestFailure(int statusCode, String response);
    }

    public abstract AdvancedResponseHandler<T> getAdvancedResponseHandler();

    private Class<T> getTClass() {
        try {
            Type type = getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType) type).getActualTypeArguments();
            Class<T> reponseClass = (Class) params[0];
            return reponseClass;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HashMap<String, Object> getParams() {
        return null;
    }

    @Override
    public HashMap<String, Object> getHeader() {
        return null;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public Class getReflectClass() {
        return getTClass();
    }

    @Override
    public Type getReflectType() {
        return null;
    }

    @Override
    public HttpResponseHandler getResponseHandler() {
        return mHttpResponseHandler;
    }

    public HttpResponseHandler mHttpResponseHandler = (statusCode, response) -> {
        LogUtils.e("HttpResponseHandler response : " + response + "     statusCode   : " + statusCode);
        try {
            if (statusCode == HttpURLConnection.HTTP_OK) {
                ResponseData responseData = JsonUtil.parseObject(response, ResponseData.class);
                if (responseData == null) {
                    getAdvancedResponseHandler().onRequestFailure(statusCode, "数据异常");
                } else if (!responseData.isSuccess()) {
                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);


                    getAdvancedResponseHandler().onRequestFailure(statusCode, TextUtils.isEmpty(responseData.getAppMsg()) ? "errorMsg is null" : responseData.getAppMsg());
                    if ("S0017".equals(responseData.getAppCode())) {
                        EventBus.getDefault().post(new LoginExitEvent());
                    } else if ("S0010".equals(responseData.getAppCode())) {
                        EventBus.getDefault().post(new LoginExitEvent());
                    } else if ("S0001".equals(responseData.getAppCode())) {
                        EventBus.getDefault().post(new LoginExitEvent());
                    } else if ("S0012".equals(responseData.getAppCode())) {
                        EventBus.getDefault().post(new LoginExitEvent());
                    }else if ("F0241".equals(responseData.getAppCode())) {

                    }else{
                        ToastUtils.showShort(responseData.getAppMsg());
                    }
                } else {
                    T resultObj;
                    if (isArray()) {
                        resultObj = (T) JsonUtil.parseArrayList(JsonUtils.encode(responseData.getResult()), getReflectClass());
                    } else {
                        if (getReflectType() != null) {
                            resultObj = (T) JsonUtil.parseObject(JsonUtils.encode(responseData.getResult()), getReflectType());
                        } else {
                            resultObj = (T) JsonUtil.parseObject(JsonUtils.encode(responseData.getResult()), getReflectClass());
                        }
                    }
                    getAdvancedResponseHandler().onRequestSuccess(resultObj);
                }
            } else {
                ToastUtils.setGravity(Gravity.CENTER, 0, 0);
                ToastUtils.showShort("可能由于网络延迟或其他问题，暂时无法提供服务，稍后再试");

                getAdvancedResponseHandler().onRequestFailure(statusCode, response);
            }
        } catch (Exception e) {
            getAdvancedResponseHandler().onRequestFailure(-1, e.getMessage());
        }

    };


}
