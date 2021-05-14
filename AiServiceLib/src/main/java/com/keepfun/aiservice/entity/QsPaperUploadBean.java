package com.keepfun.aiservice.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yang
 * @description 问卷model
 * @date 2020/9/1 4:41 PM
 */
public class QsPaperUploadBean {
    /**
     * 问卷id
     */
    private long id;

    /**
     * 图形验证码 ,
     */
    private String imgCode;
    /**
     * 图形验证码
     */
    private String imgKey;
    /**
     * 问卷标题
     */
    private String title;
    /**
     * 题目列表
     */
    private List<QsUploadBean> subjectBoList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgCode() {
        return imgCode;
    }

    public void setImgCode(String imgCode) {
        this.imgCode = imgCode;
    }

    public String getImgKey() {
        return imgKey;
    }

    public void setImgKey(String imgKey) {
        this.imgKey = imgKey;
    }

    public List<QsUploadBean> getSubjectBoList() {
        if (subjectBoList == null) {
            subjectBoList = new ArrayList<>();
        }
        return subjectBoList;
    }

    public void setSubjectBoList(List<QsUploadBean> subjectVoList) {
        this.subjectBoList = subjectVoList;
    }

    @Override
    public String toString() {
        return "QsPaperUploadBean{" +
                "id=" + id +
                ", imgCode='" + imgCode + '\'' +
                ", imgKey='" + imgKey + '\'' +
                ", title='" + title + '\'' +
                ", subjectBoList=" + subjectBoList +
                '}';
    }
}
