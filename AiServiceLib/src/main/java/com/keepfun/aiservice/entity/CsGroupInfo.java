package com.keepfun.aiservice.entity;

import java.io.Serializable;

/**
 * @author yang
 * @description
 * @date 2020/12/21 2:43 PM
 */
public class CsGroupInfo implements Serializable {
    /**
     * 群会话id ,
     */
    private long groupId;
    /**
     * 群头像 ,
     */
    private String groupImgUrl;
    /**
     * 群名称 ,
     */
    private String groupName;
    /**
     * 群id ,
     */
    private long id;
    /**
     * 是否置顶: 0-否; 1-是 ,
     */
    private int isTop;
    /**
     * 最后一条消息对象 ,
     */
    private Message messageVo;
    /**
     * 机器人主键 ,
     */
    private long robotId;
    /**
     * 机器人名称 ,
     */
    private String robotName;
    /**
     * 商户id
     */
    private Long shopId;

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getGroupImgUrl() {
        return groupImgUrl;
    }

    public void setGroupImgUrl(String groupImgUrl) {
        this.groupImgUrl = groupImgUrl;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public Message getMessageVo() {
        return messageVo;
    }

    public void setMessageVo(Message messageVo) {
        this.messageVo = messageVo;
    }

    public Long getRobotId() {
        return robotId;
    }

    public void setRobotId(Long robotId) {
        this.robotId = robotId;
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    @Override
    public String toString() {
        return "CsGroupInfo{" +
                "groupId=" + groupId +
                ", groupImgUrl='" + groupImgUrl + '\'' +
                ", groupName='" + groupName + '\'' +
                ", id=" + id +
                ", isTop=" + isTop +
                ", messageVo='" + messageVo + '\'' +
                ", robotId=" + robotId +
                ", robotName='" + robotName + '\'' +
                ", shopId=" + shopId +
                '}';
    }
}
