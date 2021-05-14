package com.keepfun.chatlib;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;

import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.ToastUtils;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

/**
 * STOMP websocket长连接客户端<p>
 *
 * @author zixuefei
 * @since 2020/7/23 16:19
 */
public class StompLiveChatClient {
    //    private final String TAG = StompLiveChatClient.class.getSimpleName();
    private StompClient mStompClient;
    private CompositeDisposable compositeDisposable;
    private String mUrl;
    private LiveChatListener liveChatListener;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String sendPath;
    private String receivePath;
    private boolean isUserLeave;
    private Map<String, String> headers;
    private int connectCounts = 0;

    public StompLiveChatClient(String mUrl, String receivePath, String sendPath, Map<String, String> headers) {
        this.mUrl = mUrl;
        this.sendPath = sendPath;
        this.receivePath = receivePath;
        this.headers = headers;
        RxJavaPlugins.setErrorHandler(throwable -> {
            ToastUtils.showShort(throwable.getMessage());
            LogUtils.e("throwable==", throwable.getMessage());
        });
        try {
            connectStomp(receivePath, headers);
        } catch (Exception e) {
            LogUtils.e("StompLiveChatClient connectStomp error : " + e.getMessage());
        }

    }

    /**
     * 开启长链接
     */
    private void connectStomp(String receivePath, Map<String, String> headers) {
        LogUtils.e("---------start connect-------");
        connectCounts++;
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, mUrl);
        mStompClient.withClientHeartbeat(1000 * 3).withServerHeartbeat(1000 * 3);
        resetSubscriptions();
        Disposable disposeLifecycle = mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            LogUtils.e("Stomp connection opened");
                            if (liveChatListener != null) {
                                liveChatListener.onConnectSuccess();
                            }
                            break;
                        case ERROR:
                            LogUtils.e("Stomp connection error", lifecycleEvent.getException());
                            disconnect();
                            handler.postDelayed(() -> {
                                connectStomp(receivePath, headers);
                            }, 500);
                            break;
                        case CLOSED:
                            LogUtils.e("Stomp connection closed");
                            resetSubscriptions();
                            if (!isUserLeave() && liveChatListener != null) {
                                liveChatListener.onError("connect exception closed");
                            }
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            LogUtils.e("Stomp failed server heartbeat");
                            break;
                        default:
                            break;
                    }
                }, throwable -> {
                    LogUtils.e("连接错误", throwable);
                    if (liveChatListener != null) {
                        liveChatListener.onError("connect error:" + throwable.getMessage());
                    }
                    disconnect();
                    handler.postDelayed(() -> {
                        connectStomp(receivePath, headers);
                    }, 500);
                });

        compositeDisposable.add(disposeLifecycle);
        mStompClient.connect(createHeaders(headers));
        subscribeMsg(receivePath);
    }

    private void subscribeMsg(String receivePath) {
        // Receive greetings
        Disposable dispTopic = mStompClient.topic(receivePath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    try {
                        String result = new String(Base64.base64ToByteArray(topicMessage.getPayload()));
                        LogUtils.e("Received msg:" + result);
                        //添加你的数据逻辑
                        if (liveChatListener != null) {
                            liveChatListener.onMessage(result);
                        }
                    } catch (Exception e) {
                        LogUtils.e("subscribeMsg error : " + e.getMessage());
                    }
                }, throwable -> {
                    LogUtils.e("接收消息错误", throwable);
                    if (liveChatListener != null) {
                        liveChatListener.onError("receive error:" + throwable.getMessage());
                    }
                    if (!mStompClient.isConnected()) {
                        subscribeMsg(receivePath);
                    }
                });
        compositeDisposable.delete(dispTopic);
        compositeDisposable.add(dispTopic);
    }

    private void reconnect() {
        LogUtils.e("mStompClient reconnect");
        try {
            mStompClient.reconnect();
            subscribeMsg(receivePath);
        } catch (Exception e) {
            reconnect();
        }
    }

    @SuppressLint("CheckResult")
    public void sendMsg(String jsonStr) {
        if (!mStompClient.isConnected()) {
            LogUtils.e("------send msg failed client is closed-------");
//            reconnect();
            connectCounts = 0;
            connectStomp(receivePath, headers);
        }
        String encodeStr = Base64.byteArrayToBase64(jsonStr.getBytes());
        LogUtils.e("-----send msg to server----- " + jsonStr);
        mStompClient.send(sendPath, encodeStr)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    LogUtils.e("-----send success----- ");
                }, throwable -> {
                    LogUtils.e("------send failed------", throwable);
                    if (liveChatListener != null) {
                        liveChatListener.onError("send msg error:" + throwable.getMessage());
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void closeChat(String jsonStr) {
        if (mStompClient == null || !mStompClient.isConnected()) {
            LogUtils.e("------closeChat failed client is closed-------");
            mStompClient.reconnect();
            return;
        }
        String encodeStr = Base64.byteArrayToBase64(jsonStr.getBytes());
        LogUtils.e("-----send msg to server----- " + jsonStr);
        mStompClient.send(sendPath, encodeStr)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    LogUtils.e("-----send success----- ");
                    disconnect();
                }, throwable -> {
                    LogUtils.e("------send failed------", throwable);
                    if (liveChatListener != null) {
                        liveChatListener.onError("closeChat error:" + throwable.getMessage());
                    }
                });
    }

    public void setLiveChatListener(LiveChatListener liveChatListener) {
        this.liveChatListener = liveChatListener;
    }

    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }

    public void disconnect() {
        setUserLeave(true);
        if (mStompClient != null) {
            mStompClient.disconnectCompletable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
        }
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    public boolean isUserLeave() {
        return isUserLeave;
    }

    private void setUserLeave(boolean userLeave) {
        isUserLeave = userLeave;
    }

    /**
     * 创建通用服务器header参数
     */
    private ArrayList<StompHeader> createHeaders(Map<String, String> headers) {
        if (headers == null) {
            return null;
        }
        ArrayList<StompHeader> stompHeaders = new ArrayList<>();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            StompHeader stompHeader = new StompHeader(entry.getKey(), entry.getValue());
            stompHeaders.add(stompHeader);
        }
        return stompHeaders;
    }

    public interface LiveChatListener {

        void onMessage(String msg);

        void onError(String reason);

        void onConnectSuccess();
    }

}
