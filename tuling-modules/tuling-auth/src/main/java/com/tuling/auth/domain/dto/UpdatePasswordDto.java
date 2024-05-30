package com.tuling.auth.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class UpdatePasswordDto implements Serializable {


    private static final long serialVersionUID = -5409505685065713967L;
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;

    @NotBlank
    private String sessionId;
}
