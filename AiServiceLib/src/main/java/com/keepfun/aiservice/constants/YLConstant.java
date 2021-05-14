package com.keepfun.aiservice.constants;

import com.keepfun.blankj.constant.TimeConstants;
import com.keepfun.blankj.util.Utils;

import skin.support.utils.SkinFileUtils;

public class YLConstant {
    public static final int HISTORY_MAX_COUNT = 5;
    /**
     * 接口地址
     */
//    public static String BASE_URL = "http://47.103.38.151:29005";
//    public static String BASE_URL = "http://47.103.97.102:29005";
//    public static String BASE_URL = "https://www.keepfun.cn/ics-im";

//    public static String BASE_URL = "https://ics.keepfun.cn/apis";
//    public static String chatUrl = "https://im.keepfun.cn/apis/message";

//    public static String chatUrl = "https://dbms.keepfun.cn:8091";
//    public static String BASE_URL = "https://www.keepfun.cn:29006";
    public static String BASE_URL = "https://www.keepfun.cn/ics-im";
    public static String chatUrl = "https://www.keepfun.cn/im-msg";
    public static String MEDIA_HOST = "47.103.38.151";

    public static AppMode appMode = AppMode.Test;

    public enum AppMode {
        Test, Overseas, Overseas_Test
    }

    static {
        if (appMode == AppMode.Test) {
            BASE_URL = "https://www.keepfun.cn/ics-im";
            chatUrl = "https://www.keepfun.cn/im-msg";
            MEDIA_HOST = "47.103.38.151:4443";
        } else if(appMode == AppMode.Overseas_Test){
            BASE_URL = "https://icstest.aicspro.com/apis";
            chatUrl = "https://imtest.aicspro.com/apis/message";
            MEDIA_HOST = "mediatest.aicspro.com:4443";
        } else if(appMode == AppMode.Overseas){
            BASE_URL = "https://ics.aicspro.com/apis";
            chatUrl = "https://im.aicspro.com/apis/message";
            MEDIA_HOST = "video.aicspro.com";
        }
    }

//    public static String BASE_URL = "https://icstest.aicspro.com/apis";
//    public static String chatUrl = "https://imtest.aicspro.com/apis/message";
//    public static String MEDIA_HOST = "mediatest.aicspro.com";
    /**
     * 会话最大延续时间
     */
    public static final int SESSION_DISCONNECT_DELAY_TIME = 4 * TimeConstants.MIN;
    /**
     * 皮肤下载地址
     */
    public final static String SKIN_PATH = SkinFileUtils.getSkinDir(Utils.getApp());

    public interface ChatType {
        int CHAT_TYPE_AI = -1;
        int CHAT_TYPE_COMMON = 0;
        int CHAT_TYPE_VIP = 1;
        int CHAT_TYPE_VIDEO = 2;
        int CHAT_TYPE_VOICE = 3;
    }

    /**
     * 1、 聊天消息 2、入群提醒消息 3、消息发送成功确认消息 4、撤销消息（此时content为消息ID）
     * 5、客服接入提醒消息 6、解散群通知消息 7、群信息修改 8、结束会话通知 9、客服转接提醒消息 10、用户暂时离开
     * 11、下线提醒, 12、消息阅读通知, 20、添加好友请求，21、同意添加好友请求，22、删除好友，23、拒绝添加好友请求
     * 26、机器人收消息 27、机器人发消息 28、排队等待人数消息 30、实时监控客户输入 31、辅助消息 34、语音对话开始
     * 35、语音对话结束 36、视频对话开始 37、视频对话结束 38、多次未识别转人工提醒 ,39、视频转语音,40、等待接入数
     * 41、本日会话数  42、消息已查看,回执消息 58 语音对话接通  59 视频对话接通
     */
    public interface MessageType {
        /**
         * 聊天消息
         */
        int MESSAGE_TYPE_CHAT = 1;
        /**
         * 入群消息
         */
        int MESSAGE_TYPE_JOIN = 2;
        /**
         * 消息发送成功确认消息
         */
        int MESSAGE_TYPE_SEND_CONFIRM = 3;
        /**
         * 撤销消息（此时content为消息ID）
         */
        int MESSAGE_TYPE_ROLLBACK = 4;
        /**
         * 客服接入通知消息
         */
        int MESSAGE_TYPE_SERVICE_IN = 5;
        /**
         * 解散群通知
         */
        int MESSAGE_TYPE_SESSION_CANCEL = 6;
        /**
         * 群信息修改
         */
        int MESSAGE_TYPE_GROUP_INFO_MODIFY = 7;
        /**
         * 结束会话通知
         */
        int MESSAGE_TYPE_SESSION_END = 8;
        /**
         * 客服转接提醒消息
         */
        int MESSAGE_TYPE_SERVICE_TRANS_TIP = 9;
        /**
         * 用户暂时离开
         */
        int MESSAGE_TYPE_USER_LEAVE = 10;
        /**
         * 下线提醒
         */
        int MESSAGE_TYPE_OFFLINE = 11;
        //24、语音对话
        int MESSAGE_TYPE_MUTE = 24;
        /**
         * 25、视频对话
         */
        int MESSAGE_TYPE_VIDEO = 25;
        /**
         * 26、机器人收消息
         */
        int MESSAGE_TYPE_AI_RECEIVE = 26;
        /**
         * 27、机器人发消息
         */
        int MESSAGE_TYPE_AI_SEND = 27;
        /**
         * 28、排队等待人数消息
         */
        int MESSAGE_TYPE_WAIT_CHANGE = 28;
        /**
         * 30、实时监控客户输入
         */
        int MESSAGE_TYPE_MONITOR = 30;
        /**
         * 辅助消息
         */
        int MESSAGE_TYPE_HELP = 31;
        /**
         * 提示消息
         */
        int MESSAGE_TYPE_SYSTEM = 32;
        /**
         * 34、语音对话开始
         */
        int MESSAGE_TYPE_MEDIA_VOICE_START = 34;
        /**
         * 35、语音对话结束
         */
        int MESSAGE_TYPE_MEDIA_VOICE_END = 35;
        /**
         * 36、视频对话开始
         */
        int MESSAGE_TYPE_MEDIA_VIDEO_START = 36;
        /**
         * 37、视频对话结束
         */
        int MESSAGE_TYPE_MEDIA_VIDEO_END = 37;
        /**
         * 38、多次未识别转人工提醒
         */
        int MESSAGE_TYPE_TRANSFORM_2_ARTIFICIAL = 38;
        /**
         * 39、视频转语音
         */
        int MESSAGE_TYPE_VIDEO_2_VOICE = 39;
        /**
         * 心跳
         */
        int MESSAGE_TYPE_HEARTBEAT = 40;
        /**
         * 营销活动
         */
        int MESSAGE_TYPE_ACTIVITY = 41;
        /**
         * 发红包
         */
        int MESSAGE_TYPE_RECEIVE_RED_PACKET = 47;
        /**
         * 打开红包
         */
        int MESSAGE_TYPE_OPEN_RED_PACKET = 48;
        /**
         * 打赏
         */
        int MESSAGE_TYPE_REWORD = 49;

        int MESSAGE_TYPE_SILENT_ALL = 51;
        int MESSAGE_TYPE_SILENT = 52;
        int MESSAGE_TYPE_USER = 53;
        /**
         * 语音对话接通
         */
        int MESSAGE_TYPE_VOICE_START = 58;
        /**
         * 视频对话接通
         */
        int MESSAGE_TYPE_VIDEO_START = 59;

    }

    //消息内容类型（1：文本消息 、2：图片消息、3：语音消息、4：视频消息 5.文件 6.自定义 7.位置信息 ）
    public interface ContentType {
        int CONTENT_TYPE_TEXT = 1;
        int CONTENT_TYPE_PIC = 2;
        int CONTENT_TYPE_VOICE = 3;
        int CONTENT_TYPE_VIDEO = 4;
        int CONTENT_TYPE_FILE = 5;
        int CONTENT_TYPE_CUSTOM = 6;
        int CONTENT_TYPE_LOCATION = 7;
    }

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
}
