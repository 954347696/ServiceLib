package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/9/10 7:39 PM
 */
public class EvaluateBean {
    /**
     * 会话id ,
     */
    private String sessionId;
    /**
     * 创建时间 ,
     */
    private long createTime;
    /**
     * 评价内容 ,
     */
    private String evaluate;
    /**
     * 是否友善(0:否, 1:是) ,
     */
    private int friendly;
    /**
     *
     */
    private long id;
    /**
     * 是否推荐(0:不会 ,1:有机会, 2:会) ,
     */
    private int recommend;
    /**
     * 解决问题 (0:没有 ,1已解决) ,
     */
    private int resolve;
    /**
     * 分数 ,
     */
    private int score;
    /**
     *
     */
    private int status;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public int getFriendly() {
        return friendly;
    }

    public void setFriendly(int friendly) {
        this.friendly = friendly;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public int getResolve() {
        return resolve;
    }

    public void setResolve(int resolve) {
        this.resolve = resolve;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
