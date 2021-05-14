package com.keepfun.aiservice.entity;

import com.keepfun.adapter.base.entity.MultiItemEntity;
import com.keepfun.adapter.base.entity.node.BaseNode;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/12/8 2:54 PM
 */
public class ServiceListSubItem extends BaseNode {


    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return null;
    }
}
