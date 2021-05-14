package com.keepfun.chatlib;

public class ChatException extends Exception {
  private long error;
  private String errorReason;

  public ChatException(long error, String errorReason) {
    this.error = error;
    this.errorReason = errorReason;
  }
}
