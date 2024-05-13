package com.tuling.system.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import lombok.Data;

import java.util.Date;

@TableName("sys_user")
@Data
public class SysUser extends BaseEntity {


    private String name;
    private String password;
    private String phoneNum;
    private String avatarUrl;
    private Date lastLoginTime;

    private String email;
    private String status;


    private String username;
    private String lastLoginIp;


}
