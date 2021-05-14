package com.keepfun.aiservice.entity;

import com.keepfun.adapter.base.entity.node.BaseNode;

import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/12/8 2:54 PM
 */
public class ServiceListItem {

    private List<AppCsStaffVo> childNode;
    private Boolean isExpanded;
    private int type;

    public ServiceListItem(List<AppCsStaffVo> childNode, int type) {
        this.childNode = childNode;
        this.type = type;
        setExpanded(false);
    }

    public List<AppCsStaffVo> getChildNode() {
        return childNode;
    }

    public void setChildNode(List<AppCsStaffVo> childNode) {
        this.childNode = childNode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        if (type == 1) {
            return "值班客服";
        } else if (type == 2) {
            return "专属客服";
        } else if (type == 3) {
            return "客服群";
        } else {
            return "客服直播间";
        }
    }


    public String getEmptyStr() {
        if (type == 1) {
            return "无在线值班客服";
        } else if (type == 2) {
            return "无在线专属客服";
        } else if (type == 3) {
            return "您没有加入任何客服群";
        } else {
            return "暂时没有进行中的客服直播";
        }
    }

    public Boolean getExpanded() {
        return isExpanded;
    }

    public void setExpanded(Boolean expanded) {
        isExpanded = expanded;
    }
}
