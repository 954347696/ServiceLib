package com.keepfun.aiservice.network.myokhttp.response;

import com.keepfun.aiservice.entity.BaseResponse;
import com.keepfun.aiservice.entity.ResponseData;
import com.keepfun.aiservice.entity.event.LoginExitEvent;
import com.keepfun.aiservice.gson.EGson;
import com.keepfun.aiservice.gson.internal.$Gson$Types;
import com.keepfun.aiservice.network.myokhttp.MyOkHttp;
import com.keepfun.aiservice.utils.JsonUtil;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Gson类型的回调接口
 * Created by tsy on 16/8/15.
 */
public abstract class GsonResponseHandler<T> implements IResponseHandler {

    private Type mType;

    public GsonResponseHandler() {
        Type myclass = getClass().getGenericSuperclass();    //反射获取带泛型的class
        if (myclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameter = (ParameterizedType) myclass;      //获取所有泛型
        mType = $Gson$Types.canonicalize(parameter.getActualTypeArguments()[0]);  //将泛型转为type
    }

    private Type getType() {
        return mType;
    }

    @Override
    public final void onSuccess(final Response response) {
        ResponseBody responseBody = response.body();
        String responseBodyStr = "";

        try {
            responseBodyStr = responseBody.string();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e("onResponse fail read response body");
            MyOkHttp.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onFailure(String.valueOf(response.code()), "fail read response body");
                }
            });
            return;
        } finally {
            responseBody.close();
        }
        LogUtils.e("onSuccess response : " + response.request().url() + "\n" + responseBodyStr);

        final String finalResponseBodyStr = responseBodyStr;

        try {
            EGson gson = new EGson();
            ResponseData response1 = gson.fromJson(finalResponseBodyStr, ResponseData.class);
            final T gsonResponse;
            if (response1.isSuccess()) {
                gsonResponse = (T) gson.fromJson(JsonUtil.encode(response1.getResult()), getType());
                MyOkHttp.mHandler.post(() -> onSuccess(gsonResponse));
            } else {
                MyOkHttp.mHandler.post(() -> {
                    if ("S0017".equals(response1.getAppCode())) {
                        EventBus.getDefault().post(new LoginExitEvent());
                    } else if ("S0010".equals(response1.getAppCode())) {
                        EventBus.getDefault().post(new LoginExitEvent());
                    } else if ("S0001".equals(response1.getAppCode())) {
                        EventBus.getDefault().post(new LoginExitEvent());
                    } else if ("S0012".equals(response1.getAppCode())) {
                        EventBus.getDefault().post(new LoginExitEvent());
                    }
//                    if ("F0241".equals(response1.getAppCode())) {
//
//                    }
                    else {
                        ToastUtils.showShort(response1.getAppMsg());
                        onFailure(response1.getAppCode(), response1.getAppMsg());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("onResponse fail parse gson, body=" + finalResponseBodyStr);
            MyOkHttp.mHandler.post(() -> onFailure(String.valueOf(response.code()), "fail parse gson, body=" + finalResponseBodyStr));

        }
    }

    @Override
    public void onFailure(int statusCode, String error_msg) {
        onFailure(String.valueOf(statusCode), error_msg);
    }

    public abstract void onSuccess(T response);

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }
}
