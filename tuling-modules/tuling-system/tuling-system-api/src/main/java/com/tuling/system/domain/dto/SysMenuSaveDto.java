package com.tuling.system.domain.dto;


import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import com.tuling.common.core.param.BaseSaveDto;
import lombok.Data;

@Data
public class SysMenuSaveDto extends BaseSaveDto {

    private String menuName;
    private Long parentId;
    private Integer orderNum;
    private String path;
    private String component;
    private String queryParam;
    private Integer isFrame;
    private Integer isCache;
    private String menuType;
    private String visible;
    private String status;
    private String perms;
    private String icon;
    private String remark;


}
