package com.keepfun.aiservice.entity;

import java.io.Serializable;

/**
 * @author yang
 * @description
 * @date 2020/8/28 2:51 PM
 */
public class Question implements Serializable {
    /**
     * 分类
     */
    private String categoryCode;
    /**
     * 内容
     */
    private String content;
    /**
     * id
     */
    private long id;
    /**
     * 标题
     */
    private String title;

    public Question() {
    }

    public Question(String content, String title) {
        this.content = content;
        this.title = title;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Question{" +
                "categoryCode='" + categoryCode + '\'' +
                ", content='" + content + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
