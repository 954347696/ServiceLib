package com.keepfun.chatlib;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import org.json.JSONObject;

import io.reactivex.Observable;


@SuppressWarnings({"unused", "WeakerAccess"})
public class ChatClient extends ChatPeer {

  private static final String TAG = "ChatClient";

  interface RequestGenerator {
    void request(JSONObject req);
  }

  public ChatClient(@NonNull ChatWebSocketClient transport, @NonNull Listener listener) {
    super(transport, listener);
  }

  public Observable<String> request(String method) {
    return request(method, new JSONObject());
  }

  public Observable<String> request(String method, @NonNull RequestGenerator generator) {
    JSONObject req = new JSONObject();
    generator.request(req);
    return request(method, req);
  }

  private Observable<String> request(String method, @NonNull JSONObject data) {
    ChatLogger.d(TAG, "request(), method: " + method);
    return Observable.create(
        emitter ->
            request(
                method,
                data,
                new ClientRequestHandler() {
                  @Override
                  public void resolve(String data) {
                    if (!emitter.isDisposed()) {
                      emitter.onNext(data);
                    }
                  }

                  @Override
                  public void reject(long error, String errorReason) {
                    if (!emitter.isDisposed()) {
                      emitter.onError(new ChatException(error, errorReason));
                    }
                  }
                }));
  }

  @WorkerThread
  public String syncRequest(String method) throws ChatException {
    return syncRequest(method, new JSONObject());
  }

  @WorkerThread
  public String syncRequest(String method, @NonNull RequestGenerator generator)
      throws ChatException {
    JSONObject req = new JSONObject();
    generator.request(req);
    return syncRequest(method, req);
  }

  @WorkerThread
  private String syncRequest(String method, @NonNull JSONObject data) throws ChatException {
    ChatLogger.d(TAG, "syncRequest(), method: " + method);

    try {
      return request(method, data).blockingFirst();
    } catch (Throwable throwable) {
      throw new ChatException(-1, throwable.getMessage());
    }
  }
}
