package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/12/14 8:50 PM
 */
public class HistoryChatMessage {

    /**
     * 内容 ,
     */
    private String content;
    /**
     * 时间 ,
     */
    private String date;
    /**
     * 用户头像 ,
     */
    private String imgUrl;
    /**
     * 用户昵称
     */
    private String userName;

    public HistoryChatMessage() {
    }

    public HistoryChatMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "HistoryChatMessage{" +
                "content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
