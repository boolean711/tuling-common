package com.tuling.common.satoken.param;

import java.util.List;

public interface LoginUserDetails {
    Long getId();

    List<String> getAuthorities();

    String getPassword();

    String getUsername();

    String getNickName();

    boolean isAccountNonExpired();

    boolean isAccountNonLocked();

    boolean isCredentialsNonExpired();

    boolean isEnabled();

    boolean isAdmin();

    Long getTenantId();


    List<Long> getRoleIdList();


}
