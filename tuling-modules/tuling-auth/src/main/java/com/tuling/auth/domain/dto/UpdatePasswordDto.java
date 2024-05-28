package com.tuling.auth.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdatePasswordDto implements Serializable {


    private static final long serialVersionUID = -5409505685065713967L;
    private String oldPassword;

    private String newPassword;
}
