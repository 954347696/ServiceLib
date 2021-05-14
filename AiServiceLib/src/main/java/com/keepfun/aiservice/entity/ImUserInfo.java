package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/9/5 3:29 PM
 */
public class ImUserInfo {
    /**
     * 用户来源 对应的鉴权中心分配的appkey ,
     */
    private String appKey;
    /**
     * 登录设备（1：android 2：ios 3：ipad 4：pc 5：H5） ,
     */
    private int device;
    /**
     * 组id ,
     */
    private long groupId;
    /**
     * photo,用户头像连接 ,
     */
    private String headPortrait;
    /**
     * id ,
     */
    private long id;
    /**
     * 直播ID ,
     */
    private long liveId;
    /**
     * 登录IP ,
     */
    private String loginIp;
    /**
     * nickName，用户昵称，默认为空 ,
     */
    private String nickName;
    /**
     * 手机号 ,
     */
    private String phone;
    /**
     * remark，备注 ,
     */
    private String remark;
    /**
     * 房间ID ,
     */
    private long roomId;
    /**
     * signature，签名格言 ,
     */
    private String signature;
    /**
     * sourceUserId ,关联用户表ID,与对应平台的用户主键对应 ,
     */
    private String sourceUserId;
    /**
     * status，账户状态 1.正常，2.冻结，3.危险 ,
     */
    private int status;
    /**
     * type,用户类型 1：普通用户 2：会员 4：客服系统管理员 5:客服 6：IM系统管理员
     */
    private int type;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public int getDevice() {
        return device;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLiveId() {
        return liveId;
    }

    public void setLiveId(long liveId) {
        this.liveId = liveId;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getRoomId() {
//        return  "e2mlh5q9";
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSourceUserId() {
        return sourceUserId;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ImUserInfo{" +
                "appKey='" + appKey + '\'' +
                ", device=" + device +
                ", groupId=" + groupId +
                ", headPortrait='" + headPortrait + '\'' +
                ", id=" + id +
                ", liveId=" + liveId +
                ", loginIp='" + loginIp + '\'' +
                ", nickName='" + nickName + '\'' +
                ", phone='" + phone + '\'' +
                ", remark='" + remark + '\'' +
                ", roomId=" + roomId +
                ", signature='" + signature + '\'' +
                ", sourceUserId='" + sourceUserId + '\'' +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}
