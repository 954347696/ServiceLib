package com.keepfun.aiservice.utils.base64;

/**
 * @author yang
 * @description
 * @date 2020/9/1 8:31 PM
 */
public interface BinaryDecoder extends Decoder {
    byte[] decode(byte[] var1) throws DecoderException;
}
