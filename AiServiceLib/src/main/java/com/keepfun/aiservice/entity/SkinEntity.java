package com.keepfun.aiservice.entity;


import com.keepfun.aiservice.downloader.domain.DownloadInfo;

/**
 * @author yang
 * @description
 * @date 2020/9/15 5:03 PM
 */
public class SkinEntity {
    /**
     * 是否是默认(0:不是,1:是) ,
     */
    private int acquiesce;
    /**
     * 皮肤id ,
     */
    private long code;
    /**
     * 创建时间 ,
     */
    private long createTime;
    /**
     * 主键 ,
     */
    private long id;
    /**
     * 皮肤名称 ,
     */
    private String name;
    /**
     * 备注 ,
     */
    private String remark;
    /**
     * 大小 ,
     */
    private long size;
    /**
     * 装填(0:禁用,1,正常) ,
     */
    private int status;
    /**
     * 皮肤类型(1:安卓,2:ios) ,
     */
    private int type;
    /**
     * 皮肤地址
     */
    private String url;

    private DownloadInfo mDownloadInfo;

    public int getAcquiesce() {
        return acquiesce;
    }

    public void setAcquiesce(int acquiesce) {
        this.acquiesce = acquiesce;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DownloadInfo getDownloadInfo() {
        return mDownloadInfo;
    }

    public void setDownloadInfo(DownloadInfo mDownloadInfo) {
        this.mDownloadInfo = mDownloadInfo;
    }

    @Override
    public String toString() {
        return "SkinEntity{" +
                "acquiesce=" + acquiesce +
                ", code=" + code +
                ", createTime=" + createTime +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                ", size=" + size +
                ", status=" + status +
                ", type=" + type +
                ", url='" + url + '\'' +
                ", mDownloadInfo=" + mDownloadInfo +
                '}';
    }
}
