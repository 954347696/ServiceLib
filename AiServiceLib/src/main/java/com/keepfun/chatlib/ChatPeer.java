package com.keepfun.chatlib;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.keepfun.chatlib.transports.ChatWebSocketTransport;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChatPeer implements ChatWebSocketTransport.Listener {

    private static final String TAG = "ChatPeer";

    public interface Listener {

        void onOpen();

        void onFail();

        void onRequest(@NonNull ChatMessage.Request request, @NonNull ServerRequestHandler handler);

        void onNotification(@NonNull ChatMessage.Notification notification);

        void onDisconnected();

        void onClose();
    }

    public interface ServerRequestHandler {

        default void accept() {
            accept(null);
        }

        void accept(String data);

        void reject(long code, String errorReason);
    }

    public interface ClientRequestHandler {

        void resolve(String data);

        void reject(long error, String errorReason);
    }

    class ClientRequestHandlerProxy implements ClientRequestHandler, Runnable {

        long mRequestId;
        String mMethod;
        ClientRequestHandler mClientRequestHandler;

        ClientRequestHandlerProxy(
                long requestId,
                String method,
                long timeoutDelayMillis,
                ClientRequestHandler clientRequestHandler) {
            mRequestId = requestId;
            mMethod = method;
            mClientRequestHandler = clientRequestHandler;
            mTimerCheckHandler.postDelayed(this, timeoutDelayMillis);
        }

        @Override
        public void run() {
            mSends.remove(mRequestId);
            // TODO (HaiyangWu): error code redefine. use http timeout
            if (mClientRequestHandler != null) {
                mClientRequestHandler.reject(408, "request timeout");
            }
        }

        @Override
        public void resolve(String data) {
            ChatLogger.d(TAG, "request() " + mMethod + " success, " + data);
            if (mClientRequestHandler != null) {
                mClientRequestHandler.resolve(data);
            }
        }

        @Override
        public void reject(long error, String errorReason) {
            ChatLogger.w(TAG, "request() " + mMethod + " fail, " + error + ", " + errorReason);
            if (mClientRequestHandler != null) {
                mClientRequestHandler.reject(error, errorReason);
            }
        }

        void close() {
            // stop timeout check.
            mTimerCheckHandler.removeCallbacks(this);
        }
    }

    // Closed flag.
    private boolean mClosed = false;
    // Transport.
    @NonNull
    private final ChatWebSocketTransport mTransport;
    // Listener.
    @NonNull
    private final Listener mListener;
    // Handler for timeout check.
    @NonNull
    private final Handler mTimerCheckHandler;
    // Connected flag.
    private boolean mConnected;
    // Custom data object.
    private JSONObject mData;
    // Map of pending sent request objects indexed by request id.
    @SuppressLint("UseSparseArrays")
    private Map<Long, ClientRequestHandlerProxy> mSends = new HashMap<>();

    public ChatPeer(@NonNull ChatWebSocketTransport transport, @NonNull Listener listener) {
        mTransport = transport;
        mListener = listener;
        mTimerCheckHandler = new Handler(Looper.getMainLooper());
        handleTransport();
    }

    public boolean isClosed() {
        return mClosed;
    }

    public boolean isConnected() {
        return mConnected;
    }

    public JSONObject getData() {
        return mData;
    }

    public void close() {
        if (mClosed) {
            return;
        }

        ChatLogger.d(TAG, "close()");
        mClosed = true;
        mConnected = false;

        // Close Transport.
        mTransport.close();

        // Close every pending sent.
        for (ClientRequestHandlerProxy proxy : mSends.values()) {
            proxy.close();
        }
        mSends.clear();

        // Emit 'close' event.
        mListener.onClose();
    }

    public void request(String method, String data, ClientRequestHandler clientRequestHandler) {
        try {
            request(method, new JSONObject(data), clientRequestHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void request(
            String method, @NonNull JSONObject data, ClientRequestHandler clientRequestHandler) {
        JSONObject request = ChatMessage.createRequest(method, data);
        long requestId = request.optLong("id");
        ChatLogger.d(TAG, String.format("request() [method:%s, data:%s]", method, data.toString()));
        String payload = mTransport.sendMessage(request.toString());

        long timeout = (long) (1500 * (15 + (0.1 * payload.length())));
        mSends.put(
                requestId, new ClientRequestHandlerProxy(requestId, method, timeout, clientRequestHandler));
    }

    public void notify(String method, String data) {
        try {
            notify(method, new JSONObject(data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void notify(String method, JSONObject data) {
        JSONObject notification = ChatMessage.createNotification(method, data);
        ChatLogger.d(TAG, String.format("notify() [method:%s]", method));
        mTransport.sendMessage(notification.toString());
    }

    private void handleTransport() {
        if (mTransport.isClosed()) {
            if (mClosed) {
                return;
            }

            mConnected = false;
            mListener.onClose();
            return;
        }

        mTransport.connect(this);
    }

    private void handleRequest(ChatMessage.Request request) {
        mListener.onRequest(
                request,
                new ServerRequestHandler() {
                    @Override
                    public void accept(String data) {
                        try {
                            JSONObject response;
                            if (TextUtils.isEmpty(data)) {
                                response = ChatMessage.createSuccessResponse(request, new JSONObject());
                            } else {
                                response = ChatMessage.createSuccessResponse(request, new JSONObject(data));
                            }
                            mTransport.sendMessage(response.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void reject(long code, String errorReason) {
                        JSONObject response = ChatMessage.createErrorResponse(request, code, errorReason);
                        try {
                            mTransport.sendMessage(response.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void handleResponse(ChatMessage.Response response) {
        ClientRequestHandlerProxy sent = mSends.remove(response.getId());
        if (sent == null) {
            ChatLogger.e(
                    TAG, "received response does not match any sent request [id:" + response.getId() + "]");
            return;
        }

        sent.close();
        if (response.isOK()) {
            sent.resolve(response.getData().toString());
        } else {
            sent.reject(response.getErrorCode(), response.getErrorReason());
        }
    }

    private void handleNotification(ChatMessage.Notification notification) {
        mListener.onNotification(notification);
    }

    // implement MyWebSocketTransport$Listener
    @Override
    public void onOpen() {
        if (mClosed) {
            return;
        }
        ChatLogger.d(TAG, "onOpen()");
        mConnected = true;
        mListener.onOpen();
    }

    @Override
    public void onFail() {
        if (mClosed) {
            return;
        }
        ChatLogger.e(TAG, "onFail()");
        mConnected = false;
        mListener.onFail();
    }

    @Override
    public void onMessage(ChatMessage message) {
        if (mClosed) {
            return;
        }
        ChatLogger.d(TAG, "onMessage()");
        if (message instanceof ChatMessage.Request) {
            handleRequest((ChatMessage.Request) message);
        } else if (message instanceof ChatMessage.Response) {
            handleResponse((ChatMessage.Response) message);
        } else if (message instanceof ChatMessage.Notification) {
            handleNotification((ChatMessage.Notification) message);
        }
    }

    @Override
    public void onDisconnected() {
        if (mClosed) {
            return;
        }
        ChatLogger.w(TAG, "onDisconnected()");
        mConnected = false;
        mListener.onDisconnected();
    }

    @Override
    public void onClose() {
        if (mClosed) {
            return;
        }
        ChatLogger.w(TAG, "onClose()");
        mClosed = true;
        mConnected = false;
        mListener.onClose();
    }
}
