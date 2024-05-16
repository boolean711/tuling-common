package com.tuling.system.event;

import org.springframework.context.ApplicationEvent;

public class AfterSaveTenantEvent extends ApplicationEvent {


    private Long tenantId;


    public AfterSaveTenantEvent(Object source, Long tenantId) {
        super(source);
        this.tenantId = tenantId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
