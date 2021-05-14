package com.keepfun.aiservice.network;

import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.network.myokhttp.response.JsonResponseHandler;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author yang
 * @description
 * @date 2021/4/21 11:54 AM
 */
public class RxHttpUtils {

    public static Observable<String> postForm(String tag, String url, HashMap<String, String> params) {
        Observable observable = Observable.create((ObservableOnSubscribe<String>) emitter -> {
            OkHttpUtils.getClient()
                    .post()
                    .url(url)
                    .tag(tag)
                    .params(params)
                    .enqueue(new GsonResponseHandler<String>() {
                        @Override
                        public void onSuccess( String response) {
                            emitter.onNext(response);
                            emitter.onComplete();
                        }

                        @Override
                        public void onFailure(String statusCode, String error_msg) {
                            emitter.onError(new Exception(error_msg));
                        }
                    });

        });
        return observable;
    }
}
