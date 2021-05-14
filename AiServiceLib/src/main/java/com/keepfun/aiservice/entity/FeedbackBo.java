package com.keepfun.aiservice.entity;

import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/9/4 7:31 PM
 */
public class FeedbackBo {
    /**
     * 反馈信息 ,
     */
    private String feedback;
    /**
     * 反馈类型 ,
     */
    private String feedbackType;
    /**
     * 反馈类型 ,
     */
    private long feedbackTypeId;
    /**
     * 0:未解决,1已解决(未解决时候传输) ,
     */
    private int finish;
    /**
     * 反馈图片字符串
     */
    private String imgStr;

    private List<FeedbackFile> files;
    /**
     * 父节点(列表中feedbackBo的id)(未解决时候传输)
     */
    private long parentId = 0;

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public long getFeedbackTypeId() {
        return feedbackTypeId;
    }

    public void setFeedbackTypeId(long feedbackTypeId) {
        this.feedbackTypeId = feedbackTypeId;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public String getImgStr() {
        return imgStr;
    }

    public void setImgStr(String imgStr) {
        this.imgStr = imgStr;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public List<FeedbackFile> getFiles() {
        return files;
    }

    public void setFiles(List<FeedbackFile> files) {
        this.files = files;
    }
}
