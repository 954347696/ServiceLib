package com.keepfun.aiservice.event;

import com.keepfun.aiservice.entity.Message;

/**
 * @author yang
 * @description
 * @date 2020/10/23 4:39 PM
 */
public class CreateSessionMessage {

    private Message message;

    public CreateSessionMessage(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
