package com.keepfun.aiservice.entity;

import android.graphics.Color;
import android.text.SpannableStringBuilder;

import com.keepfun.adapter.base.entity.MultiItemEntity;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.ChatConst;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.manager.ImClient;
import com.keepfun.aiservice.utils.JsonUtil;
import com.keepfun.aiservice.utils.TransferUtils;
import com.keepfun.blankj.util.SpanUtils;
import com.keepfun.aiservice.constants.YLConstant;

import java.io.Serializable;

/**
 * @author yang
 * @description
 * @date 2020/10/23 2:21 PM
 */
public class Message implements Serializable, MultiItemEntity {
    /**
     * 已读(如果是单聊表示对方已读，如果是群聊消息表示当前用户已读)
     */
    private boolean alreadyRead;
    /**
     * 应用appKey
     */
    private String appKey;
    /**
     * 是否辅助消息 1:是, 0:否
     */
    private int assist;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 消息内容类型（1：文本消息 、2：图片消息、3：语音消息、4：视频消息 5.文件 6.自定义 7.位置信息)
     */
    private int contentType;
    /**
     * 创建时间
     */
    private long createTime;
    /**
     * 拓展字段
     */
    private Object ext;
    /**
     * 发送者头像
     */
    private String fromUserAvatar;
    /**
     * 发送者
     */
    private String fromUserId;
    /**
     * 发送者
     */
    private String fromUserName;
    /**
     * 发送者 用户身份
     */
    private int fromUserType;
    /**
     * 群id
     */
    private String groupId;
    /**
     * 主键,消息id
     */
    private long id;
    /**
     * 提醒的用户
     */
    private String noticeUsers;
    /**
     * 真实的APP用户No
     */
    private String realUserNo;
    /**
     * 消息签名
     */
    private String signature;
    /**
     * 时长
     */
    private long timeDuration;
    /**
     * 消息类型 1、 聊天消息 2、入群提醒消息 3、消息发送成功确认消息 4、撤销消息（此时content为消息ID）
     * 5、客服接入提醒消息 6、解散群通知消息 7、群信息修改 8、结束会话通知 9、客服转接提醒消息 10、用户暂时离开
     * 11、下线提醒, 12、消息阅读通知, 20、添加好友请求，21、同意添加好友请求，22、删除好友，23、拒绝添加好友请求
     * 26、机器人收消息 27、机器人发消息 28、排队等待人数消息 30、实时监控客户输入 31、辅助消息 34、语音对话开始
     * 35、语音对话结束 36、视频对话开始 37、视频对话结束 38、多次未识别转人工提醒 ,39、视频转语音((byte)47, "发红包"),
     * ((byte)48, "收红包"),
     * ((byte)49, "打赏"),
     * type=51,content={"root":2}, root(1-所有人，2-仅管理发言)
     * type=52, content=0(0-正常，1-禁言中),只有被操作的用户可以收到.
     */
    private int type;
    /**
     * 未读数量
     */
    private int unRead;

    private String myUserId;


    private int waitNo;

    /**
     * 发送时间 ,
     */
    private String createDate;
    /**
     * 临时发送时间 ,
     */
    private String createTimeTemp;
    /**
     * 当前在线人数 ,
     */
    private int curUserNum;
    /**
     * 发送者账号 ,
     */
    private String fromUserAccount;
    /**
     * 临时消息id ,
     */
    private long idTemp;
    /**
     * 是否入群提示 （0：否 1：是） ,
     */
    private String isJoinGroup;
    /**
     * 直播ID ,
     */
    private Long liveId;
    /**
     * "直播类型(1社群,2直播间")
     */
    private Integer liveType;
    /**
     * 按群分组后的消息条数 ,
     */
    private int messageNumber;
    /**
     * sourceUserId ,关联用户表ID,与对应平台的用户主键对应 ,
     */
    private String sourceUserId;
    /**
     * 信息状态（ 1.正常，2.已删除，3.已撤销） ,
     */
    private int status;
    /**
     * 接收者账号 ,
     */
    private String toUserAccount;
    /**
     * 接收者头像 ,
     */
    private String toUserAvatar;
    /**
     * 接收者id ,
     */
    private String toUserId;
    /**
     * 接收者昵称 ,
     */
    private String toUserName;
    /**
     * 接收者 用户身份 ,
     */
    private int toUserType;

    /**
     * 接收者应用openid
     */
    private String toSourceId;
    /**
     * 发送者应用openid
     */
    private String fromSourceId;

    private String lastReadMessageId;


    private SpannableStringBuilder contentStr;

    public boolean isAlreadyRead() {
        return alreadyRead;
    }

    public void setAlreadyRead(boolean alreadyRead) {
        this.alreadyRead = alreadyRead;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public int getAssist() {
        return assist;
    }

    public void setAssist(int assist) {
        this.assist = assist;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShortContent() {
        if (getType() == YLConstant.MessageType.MESSAGE_TYPE_CHAT) {
            if (getContentType() == YLConstant.ContentType.CONTENT_TYPE_PIC) {
                return "[图片]";
            } else if (getContentType() == YLConstant.ContentType.CONTENT_TYPE_VOICE) {
                return "[语音]";
            } else if (getContentType() == YLConstant.ContentType.CONTENT_TYPE_VIDEO) {
                return "[视频]";
            }
        } else if (getType() == YLConstant.MessageType.MESSAGE_TYPE_RECEIVE_RED_PACKET) {
            return "[红包]";
        } else if (getType() == YLConstant.MessageType.MESSAGE_TYPE_REWORD) {
            return "[打赏]";
        } else if (getType() == YLConstant.MessageType.MESSAGE_TYPE_ACTIVITY) {
            ActiveBean activeBean = JsonUtil.decode(getContent(), ActiveBean.class);
            return activeBean.getTitle();
        } else if (getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_END) {
            return "[语音通话]";
        } else if (getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_END) {
            return "[视频通话]";
        }
        return TransferUtils.html2Text(getContent());
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }

    public String getFromUserAvatar() {
        return fromUserAvatar;
    }

    public void setFromUserAvatar(String fromUserAvatar) {
        this.fromUserAvatar = fromUserAvatar;
    }

    public long getFromUserId() {
        try {
            return Long.parseLong(fromUserId);
        } catch (Exception e) {
            return 0;
        }
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = String.valueOf(fromUserId);
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public int getFromUserType() {
        return fromUserType;
    }

    public void setFromUserType(int fromUserType) {
        this.fromUserType = fromUserType;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNoticeUsers() {
        return noticeUsers;
    }

    public void setNoticeUsers(String noticeUsers) {
        this.noticeUsers = noticeUsers;
    }

    public String getRealUserNo() {
        return realUserNo;
    }

    public void setRealUserNo(String realUserNo) {
        this.realUserNo = realUserNo;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public long getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(long timeDuration) {
        this.timeDuration = timeDuration;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUnRead() {
        return unRead;
    }

    public void setUnRead(int unRead) {
        this.unRead = unRead;
    }

    public long getMyUserId() {
        try {
            return Long.parseLong(myUserId);
        } catch (Exception e) {
            return 0;
        }
    }

    public void setMyUserId(long myUserId) {
        this.myUserId = String.valueOf(myUserId);
    }

    public int getWaitNo() {
        return waitNo;
    }

    public void setWaitNo(int waitNo) {
        this.waitNo = waitNo;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateTimeTemp() {
        return createTimeTemp;
    }

    public void setCreateTimeTemp(String createTimeTemp) {
        this.createTimeTemp = createTimeTemp;
    }

    public int getCurUserNum() {
        return curUserNum;
    }

    public void setCurUserNum(int curUserNum) {
        this.curUserNum = curUserNum;
    }

    public String getFromUserAccount() {
        return fromUserAccount;
    }

    public void setFromUserAccount(String fromUserAccount) {
        this.fromUserAccount = fromUserAccount;
    }

    public long getIdTemp() {
        return idTemp;
    }

    public void setIdTemp(long idTemp) {
        this.idTemp = idTemp;
    }

    public String getIsJoinGroup() {
        return isJoinGroup;
    }

    public void setIsJoinGroup(String isJoinGroup) {
        this.isJoinGroup = isJoinGroup;
    }

    public Long getLiveId() {
        return liveId;
    }

    public void setLiveId(Long liveId) {
        this.liveId = liveId;
    }

    public Integer getLiveType() {
        return liveType;
    }

    public void setLiveType(Integer liveType) {
        this.liveType = liveType;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
    }

    public long getSourceUserId() {
        try {
            return Long.parseLong(sourceUserId);
        } catch (Exception e) {
            return 0;
        }
    }

    public void setSourceUserId(long sourceUserId) {
        this.sourceUserId = String.valueOf(sourceUserId);
    }

    public void setSourceUserId(String sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getToUserAccount() {
        return toUserAccount;
    }

    public void setToUserAccount(String toUserAccount) {
        this.toUserAccount = toUserAccount;
    }

    public String getToUserAvatar() {
        return toUserAvatar;
    }

    public void setToUserAvatar(String toUserAvatar) {
        this.toUserAvatar = toUserAvatar;
    }

    public long getToUserId() {
        try {
            return Long.parseLong(toUserId);
        } catch (Exception e) {
            return 0;
        }
    }

    public void setToUserId(long toUserId) {
        this.toUserId = String.valueOf(toUserId);
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public int getToUserType() {
        return toUserType;
    }

    public void setToUserType(int toUserType) {
        this.toUserType = toUserType;
    }

    public String getToSourceId() {
        return toSourceId;
    }

    public void setToSourceId(String toSourceId) {
        this.toSourceId = toSourceId;
    }

    public String getFromSourceId() {
        return fromSourceId;
    }

    public void setFromSourceId(String fromSourceId) {
        this.fromSourceId = fromSourceId;
    }

    public String getLastReadMessageId() {
        return lastReadMessageId;
    }

    public void setLastReadMessageId(String lastReadMessageId) {
        this.lastReadMessageId = lastReadMessageId;
    }

    public SpannableStringBuilder getContentStr() {
        if (contentStr != null) {
            return contentStr;
        }
        SpanUtils spanUtils = new SpanUtils();
        if (getType() == YLConstant.MessageType.MESSAGE_TYPE_SERVICE_TRANS_TIP) {
            spanUtils.append("正在为您转接到").setForegroundColor(Color.parseColor("#888888"))
                    .append(getFromUserName() + "").setForegroundColor(Color.parseColor("#000000"));
        } else if (getType() == YLConstant.MessageType.MESSAGE_TYPE_OPEN_RED_PACKET) {
            ReceiveAmount receiveAmount = JsonUtil.decode(getContent(), ReceiveAmount.class);
            if (receiveAmount != null) {
                spanUtils.appendImage(R.mipmap.service_ic_little_packet)
                        .append(" " + (getFromUserId() == GlobalDataHelper.getInstance().getImUserInfo().getId() ? "你" : getFromUserName()) + "领取了" + receiveAmount.getCsUserName() + "的").setForegroundColor(Color.parseColor("#888888"))
                        .append("红包").setForegroundColor(Color.parseColor("#FF8F00"));
            }
        } else if (getType() == YLConstant.MessageType.MESSAGE_TYPE_REWORD) {
            ReceiveAmount receiveAmount = JsonUtil.decode(getContent(), ReceiveAmount.class);
            spanUtils.appendImage(R.mipmap.service_ic_little_reword)
                    .append(" " + (getFromUserId() == GlobalDataHelper.getInstance().getImUserInfo().getId() ? "你" : getFromUserName()) + "打赏了" + receiveAmount.getCsUserName() + " ").setForegroundColor(Color.parseColor("#888888"))
                    .append(receiveAmount.getAmount() + receiveAmount.getCurrency()).setForegroundColor(Color.parseColor("#FF8F00"));
        } else {
            spanUtils.append(getContent());
        }
        return spanUtils.create();
    }

    public void setContentStr(SpannableStringBuilder contentStr) {
        this.contentStr = contentStr;
    }

    @Override
    public String toString() {
        return "Message{" +
                "alreadyRead=" + alreadyRead +
                ", appKey='" + appKey + '\'' +
                ", assist=" + assist +
                ", content='" + content + '\'' +
                ", contentType=" + contentType +
                ", createTime=" + createTime +
                ", ext=" + ext +
                ", fromUserAvatar='" + fromUserAvatar + '\'' +
                ", fromUserId=" + fromUserId +
                ", fromUserName='" + fromUserName + '\'' +
                ", fromUserType=" + fromUserType +
                ", groupId='" + groupId + '\'' +
                ", id=" + id +
                ", noticeUsers='" + noticeUsers + '\'' +
                ", realUserNo='" + realUserNo + '\'' +
                ", signature='" + signature + '\'' +
                ", timeDuration=" + timeDuration +
                ", type=" + type +
                ", unRead=" + unRead +
                ", myUserId=" + myUserId +
                ", waitNo=" + waitNo +
                ", createDate='" + createDate + '\'' +
                ", createTimeTemp='" + createTimeTemp + '\'' +
                ", curUserNum=" + curUserNum +
                ", fromUserAccount='" + fromUserAccount + '\'' +
                ", idTemp=" + idTemp +
                ", isJoinGroup='" + isJoinGroup + '\'' +
                ", liveId=" + liveId +
                ", liveType=" + liveType +
                ", messageNumber=" + messageNumber +
                ", sourceUserId=" + sourceUserId +
                ", status='" + status + '\'' +
                ", toUserAccount='" + toUserAccount + '\'' +
                ", toUserAvatar='" + toUserAvatar + '\'' +
                ", toUserId=" + toUserId +
                ", toUserName='" + toUserName + '\'' +
                ", toUserType=" + toUserType +
                ", toSourceId='" + toSourceId + '\'' +
                ", fromSourceId='" + fromSourceId + '\'' +
                ", lastReadMessageId='" + lastReadMessageId + '\'' +
                ", contentStr=" + contentStr +
                '}';
    }

    //1、5、9、26、27、34、35、36、37、41
    public boolean isInHistory() {
        return getType() == 1 || getType() == 5 || getType() == 9
                || getType() == 26 || getType() == 27
                || getType() == 35 || getType() == 37 || getType() == 41
                || getType() == 47 || getType() == 48 || getType() == 49;
    }

    public boolean isShow() {
        return (getType() == 1 && (getContentType() == 1 || getContentType() == 2 || getContentType() == 3 || getContentType() == 4 || getContentType() == 5 || getContentType() == 8 || getContentType() == 9))
                || getType() == 5 || getType() == 8 || getType() == 9 || getType() == 26 || getType() == 27 || getType() == 28 || getType() == 32
                || getType() == 35 || getType() == 37 || getType() == 38 || getType() == 41 || getType() == 47 || getType() == 48 || getType() == 49;
    }

    public boolean nextShowTime() {
        return getItemType() != YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_PIC &&
                getItemType() != YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_VOICE &&
                getItemType() != YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_DEFAULT;
    }

    public boolean hideTime() {
        return getItemType() == YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_TIP || getItemType() == YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_END ||
                getItemType() == YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_SYSTEM || getItemType() == YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_AI_2_ARTIFICIAL ||
                getItemType() == YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_ACTIVITY;
    }

    public boolean mediaEnd() {
        return getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_END || getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_END
                || getType() == YLConstant.MessageType.MESSAGE_TYPE_VOICE_START || getType() == YLConstant.MessageType.MESSAGE_TYPE_VIDEO_START;
    }

    @Override
    public int getItemType() {
        if (getStatus() == 3) {
            return YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_DEFAULT;
        }
        switch (getType()) {
            case YLConstant.MessageType.MESSAGE_TYPE_SERVICE_IN:
            case YLConstant.MessageType.MESSAGE_TYPE_SERVICE_TRANS_TIP:
            case YLConstant.MessageType.MESSAGE_TYPE_OPEN_RED_PACKET:
            case YLConstant.MessageType.MESSAGE_TYPE_REWORD:
                return YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_TIP;
            case YLConstant.MessageType.MESSAGE_TYPE_SYSTEM:
                return YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_SYSTEM;
            case YLConstant.MessageType.MESSAGE_TYPE_SESSION_END:
                return YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_END;
            case YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_END:
            case YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_END:
                return YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_MEDIA;
            case YLConstant.MessageType.MESSAGE_TYPE_TRANSFORM_2_ARTIFICIAL:
                return YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_AI_2_ARTIFICIAL;
            case YLConstant.MessageType.MESSAGE_TYPE_ACTIVITY:
                return YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_ACTIVITY;
            case YLConstant.MessageType.MESSAGE_TYPE_RECEIVE_RED_PACKET:
                return YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_RED_PACKET;
            default:
                switch (getContentType()) {
                    case YLConstant.ContentType.CONTENT_TYPE_PIC:
                        return YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_PIC;
                    case YLConstant.ContentType.CONTENT_TYPE_VIDEO:
                        return YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_VIDEO;
                    case YLConstant.ContentType.CONTENT_TYPE_VOICE:
                        return YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_VOICE;
                    default:
                        return YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_DEFAULT;
                }
        }
    }
}
