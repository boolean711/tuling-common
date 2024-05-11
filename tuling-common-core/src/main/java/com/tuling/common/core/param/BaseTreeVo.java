package com.tuling.common.core.param;

import lombok.Data;


import java.util.*;
import java.util.stream.Collectors;


@Data
public abstract class  BaseTreeVo implements TreeVoCreator<BaseTreeVo> {


    /**
     * 节点ID
     */
    private Long id;


    /**
     * 子节点
     */
    private List<? extends BaseTreeVo> children = new ArrayList<>();



    @Override
    public BaseTreeVo create(TreeNode node) {
        this.id=node.getId();
        setCustomField(node);
        this.children = node.getChildren().stream().map(this::create).collect(Collectors.toList());
        return this;
    }

    protected abstract void setCustomField(TreeNode node);
}
