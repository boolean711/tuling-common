package com.tuling.common.core.param;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
public class BaseEntity {


    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @TableLogic
    private Boolean deleteFlag = false;

    @TableField(fill = FieldFill.INSERT)
    private Long createId;
    @TableField(fill = FieldFill.INSERT)
    private String createName;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateId;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateName;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    private Long tenantId;

    @TableField(exist = false)
    private boolean needInsertMetaData=true;

}
