package com.keepfun.aiservice.constants;

/**
 * @author yang
 * @description
 * @date 2020/9/5 3:25 PM
 */
public interface ChatConst {
    /**
     * 域名后必须加入/websocket，否则无法连接
     */
    String CHAT_SERVER = YLConstant.chatUrl + "/weim/websocket/";
    String CHAT_RECEIVE_TOPIC = "/user/exchange/topic_1.chat/message";
    String CHAT_SEND_TOPIC = "/exchange/topic_1.chat";

    /**
     * 聊天token
     */
    String CHAT_TOKEN = "chatToken";
    /**
     * 会话id
     */
    String SESSION_ID = "sessionId";
    /**
     * 会话类型 0机器人 1人工
     */
    String SESSION_TYPE = "sessionType";
    /**
     * 聊天类型
     */
    String CHAT_TYPE = "chatType";
    String CHAT_WAITING = "chatWaiting";
    String CHAT_WAITING_NO = "chatWaiting_no";
    String SERVICE_NAME = "serviceName";

}
