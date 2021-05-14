package com.keepfun.aiservice.entity;

import com.keepfun.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yang
 * @description 询前问卷问题
 * @date 2020/9/1 3:18 PM
 */
public class QuestionnaireBean implements MultiItemEntity {
    /**
     * 题目id
     */
    private long id;
    /**
     * 题目
     */
    private String question;

    private List<AnswerBean> answerVoList;
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

    public List<AnswerBean> getAnswerVoList() {
        return answerVoList == null ? new ArrayList<>() : answerVoList;
    }

    public void setAnswerVoList(List<AnswerBean> answerVoList) {
        this.answerVoList = answerVoList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public int getItemType() {
        return type;
    }

    @Override
    public String toString() {
        return "QuestionnaireBean{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", answerVoList=" + answerVoList +
                ", type=" + type +
                '}';
    }
}
