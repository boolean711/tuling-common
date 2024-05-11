package com.tuling.system.domain.vo;


import com.tuling.common.core.param.BaseTreeVo;
import com.tuling.common.core.param.TreeNode;
import lombok.Data;



@Data
public class TreeMenuVo extends BaseTreeVo {
    private static final long serialVersionUID = 1L;


    @Override
    public BaseTreeVo create(TreeNode node) {
       return super.create(node);
    }

    @Override
    protected void setCustomField(TreeNode node) {

    }
}