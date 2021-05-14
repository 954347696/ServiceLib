package com.keepfun.aiservice.entity;


import com.keepfun.blankj.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/9/4 5:25 PM
 */
public class FeedbackDetail implements Serializable {
    /**
     * 客服答复内容 ,
     */

    private String answer;
    /**
     * 客服答复时间 ,
     */

    private long answerTime;
    /**
     * 用户账号 ,
     */

    private String appUserName;
    /**
     * 二次反馈 ,
     */

    private FeedbackDetail children;
    /**
     * 提交时间 ,
     */

    private long createTime;
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
     * 反馈类型名称
     */
    private String feedbackTypeName;
    /**
     * 0:未解决,1已解决 ,
     */

    private int finish;
    /**
     * 账号分组 ,
     */

    private String groupName;
    /**
     * 主键id ,
     */

    private long id;
    /**
     * 反馈图片字符串 ,
     */

    private List<FeedbackFile> files;
    /**
     * 父节点 ,
     */

    private long parentId;
    /**
     * 客服账号 ,
     */

    private String shopUserName;
    /**
     * 1:初次反馈,2再次反馈
     */

    private int type;
    /**
     * 应用Id ,
     */

    private long appId;
    /**
     * 用户表id ,
     */

    private long appUserId;
    /**
     * 客服id ,
     */

    private long csStraffId;
    /**
     * 商户id ,
     */

    private long shopId;
    /**
     * 状态(0:禁用,1:启用) ,
     */

    private int status;
    /**
     * 用户标识
     */

    private String userCode;

    public String getAnswer() {
        if (StringUtils.isEmpty(answer)) {
            return "暂无客服回复，请耐心等待客服处理";
        }
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

    public String getAppUserName() {
        return appUserName;
    }

    public void setAppUserName(String appUserName) {
        this.appUserName = appUserName;
    }

    public FeedbackDetail getChildren() {
        return children;
    }

    public void setChildren(FeedbackDetail children) {
        this.children = children;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
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

    public long getFeedbackTypeId() {
        return feedbackTypeId;
    }

    public void setFeedbackTypeId(long feedbackTypeId) {
        this.feedbackTypeId = feedbackTypeId;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<FeedbackFile> getFiles() {
        if (files == null) {
            files = new ArrayList<>();
        }
        return files;
    }

    public void setFiles(List<FeedbackFile> files) {
        this.files = files;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getShopUserName() {
        return shopUserName;
    }

    public void setShopUserName(String shopUserName) {
        this.shopUserName = shopUserName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public long getCsStraffId() {
        return csStraffId;
    }

    public void setCsStraffId(long csStraffId) {
        this.csStraffId = csStraffId;
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

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public boolean hasReply() {
        return answerTime > createTime;
    }

}
