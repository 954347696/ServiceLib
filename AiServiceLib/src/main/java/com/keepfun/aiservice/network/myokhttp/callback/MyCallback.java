package com.keepfun.aiservice.network.myokhttp.callback;

import com.keepfun.aiservice.network.myokhttp.MyOkHttp;
import com.keepfun.aiservice.network.myokhttp.response.IResponseHandler;
import com.keepfun.blankj.util.LogUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by tsy on 16/9/18.
 */
public class MyCallback implements Callback {

    private IResponseHandler mResponseHandler;

    public MyCallback(IResponseHandler responseHandler) {
        mResponseHandler = responseHandler;
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        LogUtils.e("onFailure", e);

        MyOkHttp.mHandler.post(() -> mResponseHandler.onFailure(0, e.toString()));
    }

    @Override
    public void onResponse(Call call, final Response response) {
        if(response.isSuccessful()) {
            mResponseHandler.onSuccess(response);
        } else {
            LogUtils.e("onResponse fail status=" + response.code());

            MyOkHttp.mHandler.post(() -> mResponseHandler.onFailure(response.code(), "fail status=" + response.code()));
        }
    }
}
