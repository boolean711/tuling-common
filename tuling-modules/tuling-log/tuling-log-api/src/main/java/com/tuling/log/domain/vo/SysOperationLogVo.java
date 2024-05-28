package com.tuling.log.domain.vo;



import com.tuling.common.core.param.BaseVo;
import lombok.Data;

import java.util.Date;

@Data
public class SysOperationLogVo extends BaseVo {


    private String methodName;
    private String operator;
    private Date operationTime;
    private String operationParams;
    private String operationResult;
    private String operationIp;


}
