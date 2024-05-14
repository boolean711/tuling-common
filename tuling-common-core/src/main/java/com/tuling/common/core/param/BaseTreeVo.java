package com.tuling.common.core.param;

import com.tuling.common.core.exception.ServiceException;
import lombok.Data;
import java.util.*;



@Data
public abstract class BaseTreeVo {


    private Long id;
    private List<BaseTreeVo> children = new ArrayList<>();

    public static <T extends BaseTreeVo> T createInstance(Class<T> prototypeClass, TreeNode treeNode) {
        try {
            T t = prototypeClass.newInstance();
            t.setId(treeNode.getId());
            t.setCustomField(treeNode);
            List<? extends TreeNode> children = treeNode.getChildren();

            if (children != null && children.size() > 0) {
                for (TreeNode child : children) {
                    t.getChildren().add(createInstance(prototypeClass, child));
                }
            }
            return t;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ServiceException("prototypeClass:" + prototypeClass.getName() + "需要无参构造函数");
        }


    }


    public abstract void setCustomField(TreeNode node);
}
