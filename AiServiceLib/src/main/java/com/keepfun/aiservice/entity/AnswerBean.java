package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/9/1 3:59 PM
 */
public class AnswerBean {
    /**
     * 答案
     */
    private String answer;
    /**
     * 答案id
     */
    private long id;

    private boolean isSelected = false;

    public AnswerBean() {
    }

    public AnswerBean(String answer) {
        this.answer = answer;
    }

    public AnswerBean(String answer, long id) {
        this.answer = answer;
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "AnswerBean{" +
                "answer='" + answer + '\'' +
                ", id=" + id +
                ", isSelected=" + isSelected +
                '}';
    }
}
