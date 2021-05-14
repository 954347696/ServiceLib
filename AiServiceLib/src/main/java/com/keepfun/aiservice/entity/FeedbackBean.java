package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/8/31 5:31 PM
 */
public class FeedbackBean {
    /**
     * 客服答复内容 ,
     */
    private String answer;
    /**
     * 客服答复时间 ,
     */
    private long answerTime;
    /**
     * 应用Id ,
     */
    private long appId;
    /**
     * 用户表id ,
     */
    private long appUserId;
    /**
     * 提交时间 ,
     */
    private long createTime;
    /**
     * 客服id ,
     */
    private long csStraffId;
    /**
     * 反馈信息 ,
     */
    private String feedback;
    /**
     * 反馈类型 ,
     */
    private String feedbackType;
    /**
     * 反馈类型名称
     */
    private String feedbackTypeName;
    /**
     * 0:未解决,1已解决 ,
     */
    private int finish;
    /**
     *
     */
    private long id;
    /**
     * 反馈图片字符串 ,
     */
    private String imgStr;
    /**
     * 客服id ,
     */
    private long parentId;
    /**
     * 商户id ,
     */
    private long shopId;
    /**
     * 状态(0:禁用,1:启用) ,
     */
    private int status;
    /**
     * 1:初次反馈,2再次反馈 ,
     */
    private int type;
    /**
     * 用户标识
     */
    private String userCode;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public long getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(long answerTime) {
        this.answerTime = answerTime;
    }

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(long appUserId) {
        this.appUserId = appUserId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getCsStraffId() {
        return csStraffId;
    }

    public void setCsStraffId(long csStraffId) {
        this.csStraffId = csStraffId;
    }

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

    public String getFeedbackTypeName() {
        return feedbackTypeName;
    }

    public void setFeedbackTypeName(String feedbackTypeName) {
        this.feedbackTypeName = feedbackTypeName;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public boolean hasReply() {
        return answerTime > createTime;
    }

    @Override
    public String toString() {
        return "FeedbackBean{" +
                "answer='" + answer + '\'' +
                ", answerTime=" + answerTime +
                ", appId=" + appId +
                ", appUserId=" + appUserId +
                ", createTime=" + createTime +
                ", csStraffId=" + csStraffId +
                ", feedback='" + feedback + '\'' +
                ", feedbackType=" + feedbackType +
                ", finish=" + finish +
                ", id=" + id +
                ", imgStr='" + imgStr + '\'' +
                ", parentId=" + parentId +
                ", shopId=" + shopId +
                ", status=" + status +
                ", type=" + type +
                ", userCode='" + userCode + '\'' +
                '}';
    }
}
