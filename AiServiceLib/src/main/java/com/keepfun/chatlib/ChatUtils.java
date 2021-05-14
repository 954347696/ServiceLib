package com.keepfun.chatlib;

import java.util.Random;

public class ChatUtils {

  /**
   * Generates a random positive integer.
   *
   * @return a random positive integer
   */
  public static long generateRandomNumber() {
    return (new Random()).nextInt(10000000);
  }
}
