package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/8/31 7:33 PM
 */
public class FeedbackLabelBean {
    /**
     * 创建时间 ,
     */
    private long createTime;
    /**
     * id ,
     */
    private long id;
    /**
     * 反馈类型(名称) ,
     */
    private String name;
    /**
     * 商户id ,
     */
    private long shopId;
    /**
     * 状态(0:禁用,1:正常)
     */
    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }
}
