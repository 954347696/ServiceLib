package com.keepfun.aiservice.network;

import android.os.HandlerThread;
import android.text.TextUtils;

import com.keepfun.blankj.util.LogUtils;
import com.keepfun.aiservice.entity.HttpResponse;
import com.keepfun.aiservice.threads.YLPoolExecutor;

import java.net.ProtocolException;

/**
 * 网络请求分发、执行类
 */
public class HTTPServer {

    //是否为测试版本
    private static final boolean DEBUG = true;
    //    private Handler mCallHandler;
    private static final int MSG_REQUEST = 0;

    private HandlerThread mRequestHandlerThread = null;

    private static volatile HTTPServer instance;

    public static HTTPServer getInstance() {
        if (instance == null) {
            synchronized (HTTPServer.class) {
                if (instance == null) {
                    instance = new HTTPServer();
                }
            }
        }
        return instance;
    }

    private HTTPServer() {
    }

    public void doRequest(final HttpBasicRequest request) {
        YLPoolExecutor.getInstance().execute(() -> executeRequestInExecutor(BaseConnection.HTTP_REQ_METHOD_POST, false, false, request));
    }

    public void doRequest(final HttpBasicRequest request, boolean newThread) {
        executeRequestInExecutor(BaseConnection.HTTP_REQ_METHOD_POST, false, false, request);
    }

    public void doFormRequest(final HttpBasicRequest request) {
        YLPoolExecutor.getInstance().execute(() -> executeRequestInExecutor(BaseConnection.HTTP_REQ_METHOD_POST, true, false, request));
    }

    public void doFormRequest(final HttpBasicRequest request, boolean newThread) {
        executeRequestInExecutor(BaseConnection.HTTP_REQ_METHOD_POST, true, false, request);
    }

    public void doGetRequest(final HttpBasicRequest request) {
        YLPoolExecutor.getInstance().execute(() -> executeRequestInExecutor(BaseConnection.HTTP_REQ_METHOD_GET, false, false, request));
    }

    public void doGetRequest(final HttpBasicRequest request, boolean newThread) {
        executeRequestInExecutor(BaseConnection.HTTP_REQ_METHOD_GET, false, false, request);
    }

    public void uploadFile(final HttpBasicRequest request, boolean newThread) {
        executeRequestInExecutor(BaseConnection.HTTP_REQ_METHOD_POST, false, true, request);
    }


    private synchronized void executeRequestInExecutor(String method, boolean isForm, boolean isUplooad, HttpBasicRequest request) {

        if (TextUtils.isEmpty(request.getUrl()) || TextUtils.isEmpty(request.getUrl())) {
            LogUtils.e("上传地址URL为NULL");
            request.getResponseHandler().onResponse(-1, "上传地址URL为NULL");
            return;
        }
        request.setRequestTime(System.currentTimeMillis() / 1000);
        String url = request.getUrl();
        LogUtils.i("requestUrl : " + url);
        BaseConnection connection;
        if (url.startsWith("https:") || url.startsWith("HTTPS:")) {
            connection = new HTTPSConnection(url);
        } else {
            connection = new HTTPConnection(url);
        }
        try {
            connection.getURLConnection().setRequestMethod(method);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        HttpResponse responseData;
        if (isUplooad) {
            responseData = connection.uploadFile(request);
        } else {
            responseData = connection.doRequest(isForm, request);
        }
        LogUtils.e("请求url : " + request.getUrl());
        //response不是200 直接返回错误信息
        request.getResponseHandler().onResponse(responseData.getHttpCode(), responseData.getResult());
    }
}