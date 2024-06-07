package com.tuling.system.domain.vo;


import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import com.tuling.common.core.param.BaseVo;
import lombok.Data;

@Data
public class SysUpdateLogVo extends BaseVo {


    private String content;
    private String version;
    private Integer orderNum;

}
