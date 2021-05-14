package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/10/21 2:36 PM
 */
public class WelcomeContent {
    /**
     * 内容 ,
     */
    private String content;
    /**
     * 主键id ,
     */
    private long id;
    /**
     * 商户id ,
     */
    private long shopId;
    /**
     * 开关状态(0:关闭,1:开启) ,
     */
    private int status;
    /**
     * 类型(1:系统导语,2:欢迎设置)
     */
    private int type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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
}