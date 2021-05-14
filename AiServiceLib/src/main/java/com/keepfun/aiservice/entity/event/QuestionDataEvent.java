package com.keepfun.aiservice.entity.event;

/**
 * @author yang
 * @description
 * @date 2021/1/7 5:13 PM
 */
public class QuestionDataEvent {
    public String title;
    public boolean hasData=false;

    public QuestionDataEvent(String title, boolean hasData) {
        this.title = title;
        this.hasData = hasData;
    }
}
