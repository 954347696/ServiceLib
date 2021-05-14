package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/12/14 10:37 AM
 */
public class GroupMember {
    /**
     * 主键 ,
     */
    private long id;
    /**
     * 头像 ,
     */
    private String imgUrl;
    /**
     * 名称 ,
     */
    private String name;
    /**
     * 身份（0-群主，1-群管理员，2-群成员）
     */
    private int type;

    /**
     * 状态（0-签出 1-在线 2-事忙）
     */
    private int workStatus;

    public GroupMember() {
    }

    public GroupMember(String name) {
        this.name = name;
    }

    public GroupMember(long id, String name, int type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(int workStatus) {
        this.workStatus = workStatus;
    }

    @Override
    public String toString() {
        return "GroupMember{" +
                "id='" + id + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
