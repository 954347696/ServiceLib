package com.keepfun.chatlib.transports;

import com.keepfun.chatlib.ChatMessage;

public abstract class ChatWebSocketTransport {

  public interface Listener {

    void onOpen();

    /** Connection could not be established in the first place. */
    void onFail();

    /** @param message {@link ChatMessage} */
    void onMessage(ChatMessage message);

    /** A previously established connection was lost unexpected. */
    void onDisconnected();

    void onClose();
  }

  // WebSocket URL.
  protected String mUrl;

  public ChatWebSocketTransport(String url) {
    this.mUrl = url;
  }

  public abstract void connect(Listener listener);

  public abstract String sendMessage(String message);

  public abstract void close();

  public abstract boolean isClosed();
}
