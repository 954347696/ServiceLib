package com.keepfun.adapter.base.provider

import com.keepfun.adapter.base.BaseNodeAdapter
import com.keepfun.adapter.base.entity.node.BaseNode

abstract class BaseNodeProvider : BaseItemProvider<BaseNode>() {

    override fun getAdapter(): BaseNodeAdapter? {
        return super.getAdapter() as? BaseNodeAdapter
    }

}