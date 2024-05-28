package com.tuling.log.domain.dto;


import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import com.tuling.common.core.param.BaseSaveDto;
import lombok.Data;

import java.util.Date;

@Data
public class SysOperationLogSaveDto extends BaseSaveDto {


    private String methodName;
    private String operator;
    private Date operationTime;
    private String operationParams;
    private String operationResult;
    private String operationIp;


}
