package com.tuling.common.core.param;

import java.util.List;

public interface TreeNode {
    Long getId();

    Long getParentId();

    List<? extends TreeNode> getChildren();
}