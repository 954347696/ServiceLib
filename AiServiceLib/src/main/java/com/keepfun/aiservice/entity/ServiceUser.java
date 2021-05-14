package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/9/3 3:31 PM
 */
public class ServiceUser {

    public ServiceUser() {
    }

    public ServiceUser(String userUid) {
        this.userUid = userUid;
    }

    /**
     * 用户头像 ,
     */
    private String avatar;
    /**
     * 创建时间
     * example 2020-09-03T01:40:09.487Z
     */
    private String createTime;
    /**
     * 手机号 ,
     */
    private String mobile;
    /**
     * 用户昵称 ,
     */
    private String nickname;
    /**
     * 等级code ,
     */
    private String rankCode;
    /**
     * 会员等级 ,
     */
    private String rankName;
    /**
     * 用户备注 ,
     */
    private String remark;
    /**
     * 来源 ,
     */
    private String source;
    /**
     * 状态(0:禁用,1:启用) ,
     */
    private int status = 1;
    /**
     * 更新时间
     * example 2020-09-03T01:40:09.487Z
     */
    private String updateTime;
    /**
     * 用户标识 ,
     */
    private String userUid;
    /**
     * 用户账户
     */
    private String username;

    /**
     * VIP专线服务：0-无 1-有 ,
     */
    private int withVipLine;
    /**
     * VIP视频服务：0-无 1-有 ,
     */
    private int withVipVideo;
    /**
     * 语音客服服务：0-无 1-有
     */
    private int withVoiceCs;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRankCode() {
        return rankCode;
    }

    public void setRankCode(String rankCode) {
        this.rankCode = rankCode;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getWithVipLine() {
        return withVipLine;
    }

    public void setWithVipLine(int withVipLine) {
        this.withVipLine = withVipLine;
    }

    public int getWithVipVideo() {
        return withVipVideo;
    }

    public void setWithVipVideo(int withVipVideo) {
        this.withVipVideo = withVipVideo;
    }

    public int getWithVoiceCs() {
        return withVoiceCs;
    }

    public void setWithVoiceCs(int withVoiceCs) {
        this.withVoiceCs = withVoiceCs;
    }

    @Override
    public String toString() {
        return "ServiceUser{" +
                "avatar='" + avatar + '\'' +
                ", createTime=" + createTime +
                ", mobile='" + mobile + '\'' +
                ", nickname='" + nickname + '\'' +
                ", rankCode='" + rankCode + '\'' +
                ", rankName='" + rankName + '\'' +
                ", remark='" + remark + '\'' +
                ", source='" + source + '\'' +
                ", status=" + status +
                ", updateTime=" + updateTime +
                ", userUid='" + userUid + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
