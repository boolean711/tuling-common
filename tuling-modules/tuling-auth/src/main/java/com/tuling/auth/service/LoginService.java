package com.tuling.auth.service;


import com.tuling.auth.domain.UpdatePasswordDto;
import com.tuling.auth.domain.UserLoginDto;

public interface LoginService {

    String loginByPassword(UserLoginDto loginDto);

    void updatePassword(UpdatePasswordDto dto);


}
