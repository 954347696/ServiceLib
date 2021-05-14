package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/12/17 8:24 PM
 */
public class ReceiveRedPacket {
    private long id;//红包id
    private long userId;//发红包用户id
    private int redType;//红包类型: 1-普通红包(默认) 2-分享红包
    private int disType;//分配方式: 1-拼手气(默认) 2-平均分配
    private String totalAmount;//红包总金额
    private int totalNumber;//红包总数量
    private String coverNotes;//红包封面备注

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getRedType() {
        return redType;
    }

    public void setRedType(int redType) {
        this.redType = redType;
    }

    public int getDisType() {
        return disType;
    }

    public void setDisType(int disType) {
        this.disType = disType;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public String getCoverNotes() {
        return coverNotes;
    }

    public void setCoverNotes(String coverNotes) {
        this.coverNotes = coverNotes;
    }
}
