package com.tuling.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import lombok.Data;

import java.util.Date;

@TableName("sys_tenant")
@Data
public class SysTenant extends BaseEntity {


    private String name;
    private String phoneNum;
    private String tenantCode;
    private String address;
    private String iconUrl;
    private Integer orderNum;
    private Date validTime;

    @TableField(exist = false)
    private Long tenantId;


    private Integer textMessageQty;



}
