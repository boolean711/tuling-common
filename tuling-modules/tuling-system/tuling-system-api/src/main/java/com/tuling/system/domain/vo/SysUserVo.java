package com.tuling.system.domain.vo;

import com.tuling.common.core.param.BaseVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SysUserVo extends BaseVo {

    private String name;
    private String password;
    private String phoneNum;
    private String avatarUrl;
    private Date lastLoginTime;


    private String username;
    private String lastLoginIp;


    private List<String> permissionList;


    private SysTenantVo tenantVo;




    private String version;


}
