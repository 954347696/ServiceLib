package com.keepfun.aiservice.entity;

import java.io.Serializable;

/**
 * @author yang
 * @description
 * @date 2020/12/16 8:54 PM
 */
public class FeedbackFile implements Serializable {
    /**
     * 时长 ,
     */
    private long time;
    /**
     * type(0-图片，1-视频) ,
     */
    private int type;
    /**
     * 地址
     */
    private String url;

    public FeedbackFile(int type, String url, long time) {
        this.time = time;
        this.type = type;
        this.url = url;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "FeedbackFile{" +
                "time=" + time +
                ", type=" + type +
                ", url='" + url + '\'' +
                '}';
    }
}
