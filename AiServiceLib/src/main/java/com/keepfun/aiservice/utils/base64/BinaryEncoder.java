package com.keepfun.aiservice.utils.base64;

/**
 * @author yang
 * @description
 * @date 2020/9/1 8:30 PM
 */
public interface BinaryEncoder extends Encoder {
    byte[] encode(byte[] var1) throws EncoderException;
}
