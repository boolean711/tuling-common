package com.tuling.log.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_operation_log")
public class SysOperationLog extends BaseEntity {


    private String methodName;
    private String operator;
    private Date operationTime;
    private String operationParams;
    private String operationResult;
    private String operationIp;


}
