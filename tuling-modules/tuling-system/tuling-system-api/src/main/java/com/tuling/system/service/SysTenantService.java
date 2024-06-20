package com.tuling.system.service;


import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.system.domain.dto.SysTenantSaveDto;
import com.tuling.system.domain.entity.SysTenant;
import com.tuling.system.domain.vo.SysTenantVo;


import java.util.Date;
import java.util.List;


public interface SysTenantService extends CrudBaseIService<SysTenant, SysTenantVo, SysTenantSaveDto> {

    List<SysTenantVo> getTenantByUserName(String userName);



    default boolean isTenantValid(Long id) {

        SysTenantVo tenantVo = this.getInfoById(id);
        if (tenantVo != null) {
            if (tenantVo.getIndefinite()){
                return true;
            }
            Date validTime = tenantVo.getValidTime();

            return validTime != null && validTime.after(new Date());

        }
        return false;
    }

    void renew(Integer numMonth,Long id);
}
