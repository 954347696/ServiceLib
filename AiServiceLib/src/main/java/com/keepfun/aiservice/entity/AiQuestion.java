package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/9/29 5:38 PM
 */
public class AiQuestion {
    private String type;
    private String question;
    private String content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
