package com.keepfun.login.entity;


/**
 * @author yang
 */
public class UserBasicInfoBean {
    /**
     * 头像
     */
    private String avatar;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 用户简介
     */
    private String userIntro;
    /**
     * 用户等级
     */
    private int userLevel;
    /**
     * 用户编号
     */
    private String userNo;
    /**
     * 操作类型(1-设置房管 2-取消房管)
     */
    private int type = 2;

    private long roomId;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserIntro() {
        return userIntro;
    }

    public void setUserIntro(String userIntro) {
        this.userIntro = userIntro;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "UserBasicInfoBean{" +
                "avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phone='" + phone + '\'' +
                ", userIntro='" + userIntro + '\'' +
                ", userLevel='" + userLevel + '\'' +
                ", userNo='" + userNo + '\'' +
                '}';
    }
}
