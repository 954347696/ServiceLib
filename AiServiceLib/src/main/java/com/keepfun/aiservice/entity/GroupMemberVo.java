package com.keepfun.aiservice.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/12/18 7:32 PM
 */
public class GroupMemberVo {
    private GroupMember vo;

    private List<GroupMember> vos;

    private List<GroupMember> appVos;

    public GroupMember getVo() {
        return vo;
    }

    public void setVo(GroupMember vo) {
        this.vo = vo;
    }

    public List<GroupMember> getCsUsers() {
        List<GroupMember> csUsers = new ArrayList<>();
        csUsers.add(getVo());
        csUsers.addAll(getVos());
        return csUsers;
    }

    public List<GroupMember> getVos() {
        if (vos == null) {
            vos = new ArrayList<>();
        }
        return vos;
    }

    public void setVos(List<GroupMember> vos) {
        this.vos = vos;
    }

    public List<GroupMember> getAppVos() {
        if (appVos == null) {
            appVos = new ArrayList<>();
        }
        return appVos;
    }

    public void setAppVos(List<GroupMember> appVos) {
        this.appVos = appVos;
    }

    @Override
    public String toString() {
        return "GroupMemberVo{" +
                "vo=" + vo +
                ", vos=" + vos +
                '}';
    }
}
