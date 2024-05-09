package com.tuling.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


@Data
public class TreeMenuVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 节点ID
     */
    private Long id;

    /**
     * 节点名称
     */
    private String label;

    /**
     * 子节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeMenuVo> children;

    public TreeMenuVo() {

    }


    public TreeMenuVo(SysMenuVo menu) {
        this.id = menu.getId();
        this.label = menu.getMenuName();
        this.children = menu.getChildren().stream().map(TreeMenuVo::new).collect(Collectors.toList());
    }


}
