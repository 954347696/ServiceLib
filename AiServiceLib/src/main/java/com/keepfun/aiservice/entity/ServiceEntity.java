package com.keepfun.aiservice.entity;

import com.keepfun.aiservice.gson.annotations.SerializedName;
import com.keepfun.blankj.util.StringUtils;

import java.io.Serializable;

/**
 * @author yang
 * @description
 * @date 2020/11/7 2:32 PM
 */
public class ServiceEntity implements Serializable {
    /**
     * : 主键 ,
     */
    private long id;
    /**
     * : 商户id ,
     */
    private long shopId;
    /**
     * 会话id
     */
    private String groupId;
    /**
     * 客户等待人数, -1 不需要等待
     */
    private int waitNo;
    /**
     * 账号类型: 1-官网、2-电服、3-社群，4-主播客服 ,
     */
    private int accountType;
    /**
     * 头像 ,
     */
    @SerializedName("headUrl")
    private String avatarUrl;
    /**
     * 对话类型 -1 ai,0人工服务， 1VIP专线，2视频通话，3语音通话，4不接待 ,
     */
    private int conversationType;
    /**
     * 客服名 ,
     */
    private String name;
    /**
     * 客服英文名 ,
     */
    private String nameEn;
    /**
     * 欢迎语
     */
    private String welcome;

    /**
     * 当前会话未读消息数
     */
    private int unread;

    /**
     * 会话是否结束 0：已结束 1：进行中 2:排队中
     */
    private int isFinish;

    private boolean isTop = false;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public int getWaitNo() {
        return waitNo;
    }

    public void setWaitNo(int waitNo) {
        this.waitNo = waitNo;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getConversationType() {
        return conversationType;
    }

    public void setConversationType(int conversationType) {
        this.conversationType = conversationType;
    }

    public String getGroupId() {
        if (StringUtils.isEmpty(groupId)) {
            return "-1";
        }
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getAllName() {
        String name = getName();
        if (!StringUtils.isEmpty(getNameEn())) {
            name += "/" + getNameEn();
        }
        return name;
    }

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    @Override
    public String toString() {
        return "ServiceEntity{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", waitNo=" + waitNo +
                ", accountType=" + accountType +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", conversationType=" + conversationType +
                ", name='" + name + '\'' +
                ", nameEn='" + nameEn + '\'' +
                '}';
    }
}
