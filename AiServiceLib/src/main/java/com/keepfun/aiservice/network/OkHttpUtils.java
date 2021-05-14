package com.keepfun.aiservice.network;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.network.myokhttp.DownloadMgr;
import com.keepfun.aiservice.network.myokhttp.MyOkHttp;
import com.keepfun.aiservice.network.myokhttp.response.IResponseHandler;
import com.keepfun.aiservice.utils.JsonUtil;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.blankj.util.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author yang
 * @description
 * @date 2021/4/21 3:21 PM
 */
public class OkHttpUtils {
    private static MyOkHttp mMyOkHttp;
    private static DownloadMgr mDownloadMgr;

    public static void postJson(String url, IResponseHandler handler) {
        postJson(OkHttpUtils.class.getSimpleName(), url, new HashMap<>(), handler);
    }

    public static void postJson(String url, HashMap<String, Object> params, IResponseHandler handler) {
        postJson(OkHttpUtils.class.getSimpleName(), url, params, handler);
    }

    public static void postJson(Object tag, String url, HashMap<String, Object> params, IResponseHandler handler) {
        postJson(tag, url, params, getHeaders(), handler);
    }

    public static void postJson(Object tag, String url, HashMap<String, Object> params, HashMap<String, String> headers, IResponseHandler handler) {
        postJson(tag, url, JsonUtil.encode(params), headers, handler);
    }


    public static void postJson(String url, String params, IResponseHandler handler) {
        postJson(OkHttpUtils.class.getSimpleName(), url, params, handler);
    }

    public static void postJson(Object tag, String url, String params, IResponseHandler handler) {
        postJson(tag, url, params, getHeaders(), handler);
    }

    public static void postJson(Object tag, String url, String params, HashMap<String, String> headers, IResponseHandler handler) {
        if (!url.startsWith("http")) {
            url = YLConstant.BASE_URL + url;
        }
        OkHttpUtils.getClient().post()
                .url(url)
                .jsonParams(params)
                .tag(tag)
                .headers(headers)
                .enqueue(handler);
    }

    public static void get(String url, IResponseHandler handler) {
        get(OkHttpUtils.class.getSimpleName(), url, null, handler);
    }

    public static void get(String url, HashMap<String, String> params, IResponseHandler handler) {
        get(OkHttpUtils.class.getSimpleName(), url, params, handler);
    }

    public static void get(Object tag, String url, HashMap<String, String> params, IResponseHandler handler) {
        get(tag, url, params, getHeaders(), handler);
    }

    public static void get(Object tag, String url, HashMap<String, String> params, HashMap<String, String> headers, IResponseHandler handler) {
        if (!url.startsWith("http")) {
            url = YLConstant.BASE_URL + url;
        }
        OkHttpUtils.getClient().get()
                .url(url)
                .headers(headers)
                .tag(tag)
                .params(params)
                .enqueue(handler);
    }

    public static void upload(String url, String path, IResponseHandler handler) {
        upload(OkHttpUtils.class.getSimpleName(), url, path, handler);
    }

    public static void upload(Object tag, String url, String path, IResponseHandler handler) {
        upload(tag, url, path, getHeaders(), handler);
    }

    public static void upload(Object tag, String url, String path, HashMap<String, String> headers, IResponseHandler handler) {
        if (!url.startsWith("http")) {
            url = YLConstant.BASE_URL + url;
        }
        File file = new File(path);
        if (!file.exists()) {
            handler.onFailure(-1, "文件不存在");
            return;
        }
        OkHttpUtils.getClient().upload()
                .url(url)
                .headers(headers)
                .addFile("file", file)
                .tag(tag)
                .enqueue(handler);
    }

    public static MyOkHttp getClient() {
        if (mMyOkHttp == null) {
            synchronized (OkHttpUtils.class) {
                if (mMyOkHttp == null) {
                    mMyOkHttp = getHttpClient();
                }
            }
        }
        return mMyOkHttp;
    }

    private static MyOkHttp getHttpClient() {
        //持久化存储cookie
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Utils.getApp()));

        //log拦截器
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //自定义OkHttp
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)       //设置开启cookie
                .addInterceptor(logging)            //设置开启log
                .build();
        mMyOkHttp = new MyOkHttp(okHttpClient);
        return mMyOkHttp;
    }

    public static DownloadMgr getDownloadMgr() {
        if (mDownloadMgr == null) {
            synchronized (OkHttpUtils.class) {
                if (mDownloadMgr == null) {
                    mDownloadMgr = getDownloader();
                }
            }
        }
        return mDownloadMgr;
    }

    private static DownloadMgr getDownloader() {
        mDownloadMgr = (DownloadMgr) new DownloadMgr.Builder()
                .myOkHttp(mMyOkHttp)
                .maxDownloadIngNum(5)       //设置最大同时下载数量（不设置默认5）
                .saveProgressBytes(50 * 1204)   //设置每50kb触发一次saveProgress保存进度 （不能在onProgress每次都保存 过于频繁） 不设置默认50kb
                .build();
        return mDownloadMgr;
    }

    public static HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        String token = GlobalDataHelper.getInstance().getAccessToken();
        if (!StringUtils.isEmpty(token)) {
            headers.put(Arguments.TOKEN, token);
            headers.put(Arguments.ACCESS_TOKEN, token);
        }
        String uUid = GlobalDataHelper.getInstance().getuUid();
        if (!StringUtils.isEmpty(uUid)) {
            headers.put(Arguments.UUID, uUid);
        }
        headers.put(Arguments.TERMINAL, String.valueOf(1));
        headers.put("imToken", GlobalDataHelper.getInstance().getImToken());
        LogUtils.e("setHeaders  : " + headers);
        return headers;
    }

}
