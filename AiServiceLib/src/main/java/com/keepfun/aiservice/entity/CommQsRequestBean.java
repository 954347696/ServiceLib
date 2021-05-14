package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/8/28 5:16 PM
 */
public class CommQsRequestBean {

    /**
     * 分类
     */
    private String categoryCode;
    /**
     * 内容
     */
    private String content;
    /**
     * 标题或内容关键字
     */
    private String keywords;
    /**
     * 标题
     */
    private String title;

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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
