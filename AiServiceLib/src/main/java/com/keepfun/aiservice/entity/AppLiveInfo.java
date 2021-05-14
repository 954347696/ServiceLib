package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/12/17 7:31 PM
 */
public class AppLiveInfo {
    /**
     * 主播id ,
     */
    private long anchorId;
    /**
     * 主播名 ,
     */
    private String anchorName;
    private String anchorImg;
    private String imId;
    /**
     * 关播时间 ,
     */
    private long closeTime;
    /**
     * 关播人员主键 ,
     */
    private String closeUser;
    /**
     * 关播人员名称 ,
     */
    private String closeUserName;
    /**
     * 关播人员类型 1 主播自己 2 后台人员 ,
     */
    private int closeUserType;
    /**
     * 创建人 ,
     */
    private String createBy;
    /**
     * 是否删除 0 未删除 1 删除 ,
     */
    private int delFlag;
    /**
     * 结束时间
     */
    private long finishTime;
    /**
     * 主键 ,
     */
    private long id;
    /**
     * 封面 ,
     */
    private String liveCover;
    /**
     * 直播间id ,
     */
    private long liveNo;
    /**
     * 直播状态 10 未开始 20 进行中 25  暂停 30 已结束 ,
     */
    private int liveStatus;
    /**
     * 开播时间 ,
     */
    private long openTime;
    /**
     * 暂停时间
     */
    private long pauseTime;
    /**
     * 房间id ,
     */
    private long roomId;
    /**
     * 标题 ,
     */
    private String title;
    /**
     * 被打赏 ,
     */
    private float toGift;
    /**
     * 观看人数 ,
     */
    private int watchPeople;
    /**
     * 最高人数
     */
    private int watchPeopleMax;

    public long getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(long anchorId) {
        this.anchorId = anchorId;
    }

    public String getImId() {
        return imId;
    }

    public void setImId(String imId) {
        this.imId = imId;
    }

    public String getAnchorName() {
        return anchorName;
    }

    public void setAnchorName(String anchorName) {
        this.anchorName = anchorName;
    }

    public String getAnchorImg() {
        return anchorImg;
    }

    public void setAnchorImg(String anchorImg) {
        this.anchorImg = anchorImg;
    }

    public long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(long closeTime) {
        this.closeTime = closeTime;
    }

    public String getCloseUser() {
        return closeUser;
    }

    public void setCloseUser(String closeUser) {
        this.closeUser = closeUser;
    }

    public String getCloseUserName() {
        return closeUserName;
    }

    public void setCloseUserName(String closeUserName) {
        this.closeUserName = closeUserName;
    }

    public int getCloseUserType() {
        return closeUserType;
    }

    public void setCloseUserType(int closeUserType) {
        this.closeUserType = closeUserType;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLiveCover() {
        return liveCover;
    }

    public void setLiveCover(String liveCover) {
        this.liveCover = liveCover;
    }

    public long getLiveNo() {
        return liveNo;
    }

    public void setLiveNo(long liveNo) {
        this.liveNo = liveNo;
    }

    public int getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(int liveStatus) {
        this.liveStatus = liveStatus;
    }

    public long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(long openTime) {
        this.openTime = openTime;
    }

    public long getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(long pauseTime) {
        this.pauseTime = pauseTime;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getToGift() {
        return toGift;
    }

    public void setToGift(float toGift) {
        this.toGift = toGift;
    }

    public int getWatchPeople() {
        return watchPeople;
    }

    public void setWatchPeople(int watchPeople) {
        this.watchPeople = watchPeople;
    }

    public int getWatchPeopleMax() {
        return watchPeopleMax;
    }

    public void setWatchPeopleMax(int watchPeopleMax) {
        this.watchPeopleMax = watchPeopleMax;
    }

    @Override
    public String toString() {
        return "AppLiveInfo{" +
                "anchorId=" + anchorId +
                ", anchorName='" + anchorName + '\'' +
                ", closeTime=" + closeTime +
                ", closeUser='" + closeUser + '\'' +
                ", closeUserName='" + closeUserName + '\'' +
                ", closeUserType=" + closeUserType +
                ", createBy='" + createBy + '\'' +
                ", delFlag=" + delFlag +
                ", id=" + id +
                ", liveCover='" + liveCover + '\'' +
                ", liveNo=" + liveNo +
                ", liveStatus='" + liveStatus + '\'' +
                ", openTime=" + openTime +
                ", roomId=" + roomId +
                ", title='" + title + '\'' +
                ", toGift=" + toGift +
                ", watchPeople=" + watchPeople +
                ", watchPeopleMax=" + watchPeopleMax +
                '}';
    }
}
