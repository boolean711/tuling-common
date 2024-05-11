package com.tuling.system.domain.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import lombok.Data;

@TableName("sys_permission")
@Data
public class SysPermission extends BaseEntity {


    private String permissionName;
    private String describeInfo;

    @TableField(exist = false)
    private Long tenantId;


    private String permissionCode;

}
