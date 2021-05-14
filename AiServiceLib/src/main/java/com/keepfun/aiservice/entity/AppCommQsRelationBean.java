package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/8/31 3:25 PM
 */
public class AppCommQsRelationBean {

    /**
     * 分类
     */
    private int categoryCode;
    /**
     * 标题或内容关键字
     */
    private String keywords;
    /**
     * 问题id
     */
    private long questionId;

    public int getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }


}
