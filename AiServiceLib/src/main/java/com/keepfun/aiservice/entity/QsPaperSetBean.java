package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description 询前问卷设置信息返回
 * @date 2020/9/2 11:48 AM
 */
public class QsPaperSetBean {
    /**
     * 验证码状态(0:不需要,1:需要)
     */
    private int needCode;
    /**
     * 是否需要提交询前问卷：0 不需要 1 需要
     */
    private int needPaper;
    /**
     * 询前问卷开关状态(0:关闭,1:开启)
     */
    private int switchStatus;

    public int getNeedCode() {
        return needCode;
    }

    public void setNeedCode(int needCode) {
        this.needCode = needCode;
    }

    public int getNeedPaper() {
        return needPaper;
    }

    public void setNeedPaper(int needPaper) {
        this.needPaper = needPaper;
    }

    /**
     * 询前问卷开关状态(0:关闭,1:开启)
     */
    public int getSwitchStatus() {
        return switchStatus;
    }

    public void setSwitchStatus(int switchStatus) {
        this.switchStatus = switchStatus;
    }

    @Override
    public String toString() {
        return "QsPaperSetBean{" +
                "needCode=" + needCode +
                ", needPaper=" + needPaper +
                ", switchStatus=" + switchStatus +
                '}';
    }
}
