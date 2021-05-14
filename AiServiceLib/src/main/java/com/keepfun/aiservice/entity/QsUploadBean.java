package com.keepfun.aiservice.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yang
 * @description 询前问卷问题
 * @date 2020/9/1 3:18 PM
 */
public class QsUploadBean {
    /**
     * 题目id
     */
    private long id;
    /**
     * 题目
     */
    private String question;

    private List<AnswerBean> answerBoList;
    /**
     * 题型:0-单选 1-多选 2-填写
     */
    private int type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<AnswerBean> getAnswerBoList() {
        return answerBoList == null ? new ArrayList<>() : answerBoList;
    }

    public void setAnswerBoList(List<AnswerBean> answerVoList) {
        this.answerBoList = answerVoList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "QuestionnaireBean{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", answerBoList=" + answerBoList +
                ", type=" + type +
                '}';
    }
}
