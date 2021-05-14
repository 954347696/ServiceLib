package com.keepfun.aiservice.manager;

import android.content.Context;

import com.keepfun.aiservice.ui.messageview.ActivityTypeView;
import com.keepfun.aiservice.ui.messageview.BaseMsgView;
import com.keepfun.aiservice.ui.messageview.DefaultTypeView;
import com.keepfun.aiservice.ui.messageview.MediaTypeView;
import com.keepfun.aiservice.ui.messageview.PicView;
import com.keepfun.aiservice.ui.messageview.RedPacketTypeView;
import com.keepfun.aiservice.ui.messageview.SessionEndView;
import com.keepfun.aiservice.ui.messageview.SystemInfoView;
import com.keepfun.aiservice.ui.messageview.TipInfoView;
import com.keepfun.aiservice.ui.messageview.Transfer2AiView;
import com.keepfun.aiservice.ui.messageview.VideoTypeView;
import com.keepfun.aiservice.ui.messageview.VoiceTypeView;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * @author yang
 * @description
 * @date 2021/4/19 4:32 PM
 */
public class ChatKit {

    public interface MessageItemType {
        int MESSAGE_ITEM_TYPE_DEFAULT = 1;
        int MESSAGE_ITEM_TYPE_TIP = 2;
        int MESSAGE_ITEM_TYPE_END = 3;
        int MESSAGE_ITEM_TYPE_PIC = 4;
        int MESSAGE_ITEM_TYPE_VOICE = 5;
        int MESSAGE_ITEM_TYPE_SYSTEM = 6;
        int MESSAGE_ITEM_TYPE_MEDIA = 7;
        int MESSAGE_ITEM_TYPE_AI_2_ARTIFICIAL = 8;
        int MESSAGE_ITEM_TYPE_ACTIVITY = 9;
        int MESSAGE_ITEM_TYPE_VIDEO = 10;
        int MESSAGE_ITEM_TYPE_RED_PACKET = 11;
    }

    /**
     * 消息类与消息UI展示类对应表
     */
    private static SoftReference<HashMap<Integer, Class<? extends BaseMsgView>>> msgViewMap = new SoftReference<>(new HashMap<>());

    /**
     * 注册消息展示界面类。
     *
     * @param msgType 消息类型
     * @param msgView 对应的界面展示类
     */
    public static void registerMessageView(Integer msgType, Class<? extends BaseMsgView> msgView) {
        msgViewMap.get().put(msgType, msgView);
    }

    /**
     * 获取注册消息对应的UI展示类。
     *
     * @param msgType 注册的消息类型
     * @return 对应的UI展示类
     */
    public static Class<? extends BaseMsgView> getRegisterMessageView(Integer msgType) {
        return msgViewMap.get().get(msgType);
    }

    /**
     * 初始化方法，在整个应用程序全局只需要调用一次，建议在Application 继承类中调用。
     * <p/>
     * <strong>注意：</strong>其余方法都需要在这之后调用。
     *
     * @param context Application类的Context
     */
    public static void init(Context context) {
        //todo 链接im

        registerMessageView(MessageItemType.MESSAGE_ITEM_TYPE_SYSTEM, SystemInfoView.class);

        registerMessageView(MessageItemType.MESSAGE_ITEM_TYPE_TIP, TipInfoView.class);

        registerMessageView(MessageItemType.MESSAGE_ITEM_TYPE_END, SessionEndView.class);

        registerMessageView(MessageItemType.MESSAGE_ITEM_TYPE_PIC, PicView.class);

        registerMessageView(MessageItemType.MESSAGE_ITEM_TYPE_VIDEO, VideoTypeView.class);

        registerMessageView(MessageItemType.MESSAGE_ITEM_TYPE_MEDIA, MediaTypeView.class);

        registerMessageView(MessageItemType.MESSAGE_ITEM_TYPE_AI_2_ARTIFICIAL, Transfer2AiView.class);

        registerMessageView(MessageItemType.MESSAGE_ITEM_TYPE_VOICE, VoiceTypeView.class);

        registerMessageView(MessageItemType.MESSAGE_ITEM_TYPE_ACTIVITY, ActivityTypeView.class);

        registerMessageView(MessageItemType.MESSAGE_ITEM_TYPE_DEFAULT, DefaultTypeView.class);

        registerMessageView(MessageItemType.MESSAGE_ITEM_TYPE_RED_PACKET, RedPacketTypeView.class);

    }

}
