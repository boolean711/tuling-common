package com.tuling.auth.service;


import com.tuling.auth.domain.dto.UpdatePasswordDto;
import com.tuling.auth.domain.dto.UserLoginDto;
import com.tuling.auth.domain.vo.UserLoginVo;

public interface LoginService {

    UserLoginVo loginByPassword(UserLoginDto loginDto);

    void updatePassword(UpdatePasswordDto dto);


}
