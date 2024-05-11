package com.tuling.common.core.param;

@FunctionalInterface
public interface TreeVoCreator <T extends BaseTreeVo> {

    T create(TreeNode node);
}
