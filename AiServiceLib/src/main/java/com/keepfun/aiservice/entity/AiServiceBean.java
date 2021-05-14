package com.keepfun.aiservice.entity;

import com.keepfun.aiservice.constants.YLConstant;

/**
 * @author yang
 * @description
 * @date 2020/9/22 1:54 PM
 */
public class AiServiceBean {
    private long id;
    private String code;
    private long groupId;
    private long shopId;
    private String name;
    private String welcome;
    private String notIdentified;
    private int status;
    private long createTime;
    private String deft;
    private String url;
    private int frequency;
    private int openStatus;
    private int displayStatus;

    public ServiceEntity getService() {
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setId(getId());
        serviceEntity.setShopId(getShopId());
        serviceEntity.setWelcome(getWelcome());
        serviceEntity.setAvatarUrl(getUrl());
        serviceEntity.setName(getName());
        serviceEntity.setConversationType(YLConstant.ChatType.CHAT_TYPE_AI);
        return serviceEntity;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public String getNotIdentified() {
        return notIdentified;
    }

    public void setNotIdentified(String notIdentified) {
        this.notIdentified = notIdentified;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDeft() {
        return deft;
    }

    public void setDeft(String deft) {
        this.deft = deft;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(int openStatus) {
        this.openStatus = openStatus;
    }

    public int getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(int displayStatus) {
        this.displayStatus = displayStatus;
    }
}
