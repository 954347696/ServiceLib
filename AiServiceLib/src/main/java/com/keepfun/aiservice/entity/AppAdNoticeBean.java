package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/9/11 1:50 PM
 */
public class AppAdNoticeBean {
    /**
     * 内容
     */
    private String content;
    /**
     * 创建时间
     */
    private long createTime;
    /**
     * 结束时间
     */
    private long endTime;
    /**
     * id
     */
    private long id;
    /**
     * 跳转链接
     */
    private String linkUrl;
    /**
     * 图片地址
     */
    private String picUrl;
    /**
     * 开始时间
     */
    private long startTime;
    /**
     * 标题
     */
    private String title;
    /**
     * 图片地址
     */
    private String type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AppAdNoticeBean{" +
                "content='" + content + '\'' +
                ", createTime=" + createTime +
                ", endTime=" + endTime +
                ", id=" + id +
                ", linkUrl='" + linkUrl + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", startTime=" + startTime +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
