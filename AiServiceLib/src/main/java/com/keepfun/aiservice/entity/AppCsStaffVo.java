package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/12/17 9:13 AM
 */
public class AppCsStaffVo {

    private long id;
    /**
     * 账号类型: 1-官网客服、2-电服客服、3-社群客服，4-主播客服 ,
     */
    private int accountType;
    /**
     * 对话类型 -1 ai,0人工服务， 1VIP专线，2视频通话，3语音通话，4不接待 ,
     */
    private int conversationType;

    /**
     * 直播状态 10 未开始 20 进行中 30 已结束 ,
     */
    private int liveStatus;
    /**
     * 客服头像 ,
     */
    private String avatarUrl;

    /**
     * 是否置顶: 0-否; 1-是 ,
     */
    private int isTop;
    /**
     * : 商户id ,
     */
    private long shopId;
    /**
     * 客服中文名 ,
     */
    private String csStaffName;
    /**
     * 客服英文名 ,
     */
    private String csStaffNameEn;
    /**
     * 客服id ,
     */
    private long csUserId;
    /**
     * 在线状态: 0-签出 1-在线 2-事忙
     */
    private int onlineStatus;

    /**
     * : 群id ,
     */
    private Long groupId;
    /**
     * 群头像 ,
     */
    private String groupImgUrl;
    /**
     * 群名称 ,
     */
    private String groupName;

    /**
     * im群组id ,
     */
    private String imGroupId;
    /**
     * 直播间封面 ,
     */
    private String liveCover;
    /**
     * 直播间id ,
     */
    private long liveId;
    /**
     * ID/直播间编号 ,
     */
    private String liveNo;
    /**
     * 房间id ,
     */
    private String roomId;
    /**
     * 直播间标题 ,
     */
    private String title;
    /**
     * 房间人数上限 ,
     */
    private int watchNumMax;
    /**
     * 当前观看人数
     */
    private int watchingNum;

    /**
     * 机器人主键 ,
     */
    private Long robotId;
    /**
     * 机器人名称 ,
     */
    private String robotName;

    /**
     * 最后一条消息对象 ,
     */
    private Message messageVo;

    public ServiceEntity getService() {
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setName(getCsStaffName());
        serviceEntity.setNameEn(getCsStaffNameEn());
        serviceEntity.setId(getCsUserId());
        serviceEntity.setAvatarUrl(getAvatarUrl());
        serviceEntity.setAccountType(getAccountType());
        serviceEntity.setConversationType(getConversationType());
        serviceEntity.setShopId(getShopId());
        serviceEntity.setTop(getIsTop() == 1);
        return serviceEntity;
    }

    public CsGroupInfo getGroupInfo() {
        CsGroupInfo groupInfo = new CsGroupInfo();
        groupInfo.setGroupId(getGroupId());
        groupInfo.setGroupImgUrl(getGroupImgUrl());
        groupInfo.setGroupName(getGroupName());
        groupInfo.setId(getId());
        groupInfo.setIsTop(getIsTop());
        groupInfo.setMessageVo(getMessageVo());
        groupInfo.setRobotId(getRobotId());
        groupInfo.setRobotName(getRobotName());
        groupInfo.setShopId(getShopId());
        return groupInfo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getConversationType() {
        return conversationType;
    }

    public void setConversationType(int conversationType) {
        this.conversationType = conversationType;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCsStaffName() {
        return csStaffName;
    }

    public void setCsStaffName(String csStaffName) {
        this.csStaffName = csStaffName;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public String getCsStaffNameEn() {
        return csStaffNameEn;
    }

    public void setCsStaffNameEn(String csStaffNameEn) {
        this.csStaffNameEn = csStaffNameEn;
    }

    public long getCsUserId() {

        return csUserId;
    }

    public void setCsUserId(long csUserId) {
        this.csUserId = csUserId;
    }

    public int getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(int liveStatus) {
        this.liveStatus = liveStatus;
    }

    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
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

    public String getImGroupId() {
        return imGroupId;
    }

    public void setImGroupId(String imGroupId) {
        this.imGroupId = imGroupId;
    }

    public String getLiveCover() {
        return liveCover;
    }

    public void setLiveCover(String liveCover) {
        this.liveCover = liveCover;
    }

    public long getLiveId() {
        return liveId;
    }

    public void setLiveId(long liveId) {
        this.liveId = liveId;
    }

    public String getLiveNo() {
        return liveNo;
    }

    public void setLiveNo(String liveNo) {
        this.liveNo = liveNo;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWatchNumMax() {
        return watchNumMax;
    }

    public void setWatchNumMax(int watchNumMax) {
        this.watchNumMax = watchNumMax;
    }

    public int getWatchingNum() {
        return watchingNum;
    }

    public void setWatchingNum(int watchingNum) {
        this.watchingNum = watchingNum;
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

    @Override
    public String toString() {
        return "AppCsStaffVo{" +
                "accountType=" + accountType +
                ", conversationType=" + conversationType +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", shopId=" + shopId +
                ", csStaffName='" + csStaffName + '\'' +
                ", csStaffNameEn='" + csStaffNameEn + '\'' +
                ", csUserId='" + csUserId + '\'' +
                ", onlineStatus=" + onlineStatus +
                ", groupId='" + groupId + '\'' +
                ", groupImgUrl='" + groupImgUrl + '\'' +
                ", groupName='" + groupName + '\'' +
                ", imGroupId='" + imGroupId + '\'' +
                ", liveCover='" + liveCover + '\'' +
                ", liveId='" + liveId + '\'' +
                ", liveNo='" + liveNo + '\'' +
                ", roomId='" + roomId + '\'' +
                ", title='" + title + '\'' +
                ", watchNumMax='" + watchNumMax + '\'' +
                ", watchingNum='" + watchingNum + '\'' +
                '}';
    }
}
