package com.tuling.system.domain.vo;


import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import com.tuling.common.core.param.BaseVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data

public class SysMenuVo extends BaseVo {

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

    private Date createTime;


    private List<SysMenuVo> children =new ArrayList<>();


}
