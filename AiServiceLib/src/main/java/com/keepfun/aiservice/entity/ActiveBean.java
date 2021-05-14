package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/11/12 8:20 PM
 */
public class ActiveBean {
    /**
     * 80,
     */
    private String id;
    /**
     * 105,
     */
    private String shopId;
    /**
     * "急急急",
     */
    private String title;
    /**
     * "<p>男男女女</p>",
     */
    private String content;
    /**
     * "https://www.teck.com/",
     */
    private String url;
    /**
     * 1604332800000,
     */
    private long startTime;
    /**
     * 1605715200000,
     */
    private long endTime;
    /**
     * 1604977215000,
     */
    private long createTime;
    /**
     * 1
     */
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
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

    @Override
    public String toString() {
        return "ActiveBean{" +
                "id='" + id + '\'' +
                ", shopId='" + shopId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", createTime=" + createTime +
                ", status=" + status +
                '}';
    }
}
