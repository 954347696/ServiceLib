package com.keepfun.aiservice.utils.base64;

/**
 * @author yang
 * @description
 * @date 2020/9/1 8:32 PM
 */
public class EncoderException extends Exception {
    private static final long serialVersionUID = 1L;

    public EncoderException() {
    }

    public EncoderException(String message) {
        super(message);
    }

    public EncoderException(String message, Throwable cause) {
        super(message, cause);
    }

    public EncoderException(Throwable cause) {
        super(cause);
    }
}
