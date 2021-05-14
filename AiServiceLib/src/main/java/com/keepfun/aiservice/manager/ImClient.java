package com.keepfun.aiservice.manager;

import com.keepfun.aiservice.ServiceManager;
import com.keepfun.aiservice.entity.event.ImReConnectEvent;
import com.keepfun.blankj.constant.TimeConstants;
import com.keepfun.blankj.util.JsonUtils;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.aiservice.constants.ChatConst;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.ImUserInfo;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.event.CreateSessionMessage;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.chatlib.StompLiveChatClient;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;


/**
 * @author yang
 * @description
 * @date 2020/9/5 2:48 PM
 */
public class ImClient {
    private static volatile ImClient mImClient;
    private StompLiveChatClient chatClient;
    private long lastMessageTime;
    private long lastSession;

    private ImClient() {
    }

    public static ImClient getInstance() {
        if (mImClient == null) {
            synchronized (ImClient.class) {
                if (mImClient == null) {
                    mImClient = new ImClient();
                }
            }
        }
        return mImClient;
    }

    public void initChatClient(ImUserInfo userInfoBean, String token) {
        if (chatClient != null) {
            chatClient.disconnect();
        }
        LogUtils.e("initChatClient token : " + token);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Access-token", token);
        if (userInfoBean != null) {
            headers.put("id", String.valueOf(userInfoBean.getId()));
            headers.put("userName", userInfoBean.getNickName());
            headers.put("appKey", userInfoBean.getAppKey());
            headers.put("type", String.valueOf(userInfoBean.getType()));
            headers.put("sourceDevice", String.valueOf(userInfoBean.getDevice()));
            headers.put("ip", userInfoBean.getLoginIp());
            headers.put("roomId", String.valueOf(userInfoBean.getRoomId()));
        }
        headers.put("stompCommand", "CONNECT");
        chatClient = new StompLiveChatClient(ChatConst.CHAT_SERVER, ChatConst.CHAT_RECEIVE_TOPIC, ChatConst.CHAT_SEND_TOPIC, headers);
        chatClient.setLiveChatListener(chatListener);

    }

    private StompLiveChatClient.LiveChatListener chatListener = new StompLiveChatClient.LiveChatListener() {

        @Override
        public void onMessage(String message) {
            ImUserInfo imUserInfo = GlobalDataHelper.getInstance().getImUserInfo();
            if (imUserInfo != null) {
                Message message1 = JsonUtils.decode(message, Message.class);
                message1.setMyUserId(imUserInfo.getId());
                if (message1.getType() == YLConstant.MessageType.MESSAGE_TYPE_SESSION_END) {
                    GlobalDataHelper.getInstance().setData(ChatConst.SESSION_ID, -1L);
                    sendMsg(message1);
                }
//                else if (message1.getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_START
//                        || message1.getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_START
//                        || message1.getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_END
//                        || message1.getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_END
//                ) {
//                    sendMsg(message1);
//                }
                else {
                    message1.setCreateTime(System.currentTimeMillis());
                    if (message1.getType() == YLConstant.MessageType.MESSAGE_TYPE_JOIN ||
                            message1.getType() == YLConstant.MessageType.MESSAGE_TYPE_SERVICE_IN) {
                        GlobalDataHelper.getInstance().setData(ChatConst.SESSION_ID, Long.valueOf(message1.getGroupId()));
                    }
                }
                LogUtils.e("stompLiveChatClient receive msg  :  " + message + " " + message1.getType());
                EventBus.getDefault().post(message1);
            }
        }

        @Override
        public void onError(String message) {
            LogUtils.e("---on error---:" + message);
        }

        @Override
        public void onConnectSuccess() {
            LogUtils.e("-----onConnectSuccess-----");
            EventBus.getDefault().post(new ImReConnectEvent());
        }
    };

    /**
     * 发送聊天消息
     */
    public void sendMsg(int contentType, String msg) {
        sendMsg(YLConstant.MessageType.MESSAGE_TYPE_CHAT, contentType, msg);
    }

    public void sendMsg(int messageType, int contentType, String msg) {
        sendMsg(messageType, contentType, msg, 1);
    }

    public synchronized void sendMsg(int messageType, int contentType, String msg, long duration) {
        ImUserInfo imUserInfo = GlobalDataHelper.getInstance().getImUserInfo();
        if (imUserInfo == null) {
            LogUtils.e("----chat client is null-----");
            return;
        }
        if (chatClient == null || imUserInfo == null) {
//            String imToken = GlobalDataHelper.getInstance().getImToken();
//            initChatClient(imUserInfo, imToken);
            new ServiceManager().connectIm();
            return;
        }


        Message message = createMessage(imUserInfo, contentType, msg, messageType);
        message.setCreateTime(System.currentTimeMillis());
        message.setMyUserId(imUserInfo.getId());
        Integer waitingNo = (Integer) GlobalDataHelper.getInstance().getData(ChatConst.CHAT_WAITING_NO);
        if (waitingNo != null) {
            message.setWaitNo(waitingNo);
        }
        message.setTimeDuration(duration);
        Long sessionId = (Long) GlobalDataHelper.getInstance().getData(ChatConst.SESSION_ID);
        if (sessionId == null || sessionId == -1 || (lastSession == sessionId.longValue() && (System.currentTimeMillis() - lastMessageTime > 4 * TimeConstants.MIN))) {
            lastMessageTime = System.currentTimeMillis();
            EventBus.getDefault().post(new CreateSessionMessage(message));
        } else {
            lastMessageTime = System.currentTimeMillis();
            lastSession = sessionId;
            message.setGroupId(String.valueOf(sessionId));
//            EventBus.getDefault().post(message);
            chatClient.sendMsg(JsonUtils.encode(message));
        }
    }

    public synchronized void sendMsg(Message message) {
        ImUserInfo imUserInfo = GlobalDataHelper.getInstance().getImUserInfo();
        if (imUserInfo == null) {
            LogUtils.e("----chat client is null-----");
            return;
        }
        if (chatClient == null || imUserInfo == null) {
//            String imToken = GlobalDataHelper.getInstance().getImToken();
//            initChatClient(imUserInfo, imToken);
            new ServiceManager().connectIm();
            return;
        }

        message.setFromUserId(imUserInfo.getId());
        chatClient.sendMsg(JsonUtils.encode(message));
    }

    public void setMessageRead(Message originMessage) {
        ImUserInfo mImUserInfo = GlobalDataHelper.getInstance().getImUserInfo();
        if (originMessage.getId() > 0) {
            Message message =new Message();
            message.setType(42);
            message.setAppKey(mImUserInfo.getAppKey());
            message.setFromUserName(mImUserInfo.getNickName());
            message.setFromUserAvatar(mImUserInfo.getHeadPortrait());
            message.setFromUserType(mImUserInfo.getType());
            message.setFromUserId(mImUserInfo.getId());
            message.setLastReadMessageId(String.valueOf(originMessage.getId()));
            message.setContent("1");
            message.setGroupId(originMessage.getGroupId());
            message.setContentType(1);
            LogUtils.e("lastMessage : " + message);
            mImClient.sendMsg(message);
        }
    }


    public Message createMessage(ImUserInfo imUserInfo, int contentType, String msg, int type) {
        Message liveChatMessage = new Message();
        liveChatMessage.setAppKey(imUserInfo.getAppKey());
        liveChatMessage.setContent(msg);
        liveChatMessage.setContentType(contentType);
        liveChatMessage.setType(type);
        liveChatMessage.setFromUserName(imUserInfo.getNickName());
        liveChatMessage.setFromUserAvatar(imUserInfo.getHeadPortrait());
        liveChatMessage.setFromUserType(imUserInfo.getType());
        liveChatMessage.setFromUserId(imUserInfo.getId());
        return liveChatMessage;
    }

    public void close() {
        if (chatClient != null) {
            chatClient.disconnect();
        }
    }
}
