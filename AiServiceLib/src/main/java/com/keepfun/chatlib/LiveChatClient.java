package com.keepfun.chatlib;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.keepfun.chatlib.transports.ChatWebSocketTransport;

/**
 * 直播使用聊天client<p>
 *
 * @author zixuefei
 * @since 2020/7/21 16:27
 */
public class LiveChatClient implements ChatWebSocketTransport.Listener {
    private static final String TAG = LiveChatClient.class.getSimpleName();
    // Closed flag.
    private boolean mClosed = false;
    // Transport.
    @NonNull
    private final ChatWebSocketClient mTransport;
    // Listener.
    @NonNull
    private final ChatWebSocketTransport.Listener mListener;
    // Connected flag.
    private boolean mConnected;
    private Handler handler = new Handler(Looper.getMainLooper());

    public LiveChatClient(@NonNull String url, @NonNull ChatWebSocketTransport.Listener listener) {
        mTransport = new ChatWebSocketClient(url);
        mListener = listener;
        connectTransport();
    }

    public boolean isClosed() {
        return mClosed;
    }

    public boolean isConnected() {
        return mConnected;
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

        // Emit 'close' event.
        mListener.onClose();
    }

    public void sendMessage(String jsonStr) {
        if (isClosed()) {
            return;
        }
        ChatLogger.d(TAG, "----send msg:" + jsonStr);
        mTransport.sendMessage(jsonStr);
    }

    /**
     * socket开始连接
     */
    private void connectTransport() {
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
        handler.post(() -> {
            if (mListener != null) {
                mListener.onMessage(message);
            }
        });
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
