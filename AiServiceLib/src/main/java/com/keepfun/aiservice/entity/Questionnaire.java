package com.keepfun.aiservice.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yang
 * @description 问卷model
 * @date 2020/9/1 4:41 PM
 */
public class Questionnaire {
    /**
     * 问卷id
     */
    private long id;
    /**
     * 问卷标题
     */
    private String title;
    /**
     * 题目列表
     */
    private List<QuestionnaireBean> subjectVoList;

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

    public List<QuestionnaireBean> getSubjectVoList() {
        return subjectVoList==null?new ArrayList<>():subjectVoList;
    }

    public void setSubjectVoList(List<QuestionnaireBean> subjectVoList) {
        this.subjectVoList = subjectVoList;
    }

    @Override
    public String toString() {
        return "Questionnaire{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subjectVoList=" + subjectVoList +
                '}';
    }
}
