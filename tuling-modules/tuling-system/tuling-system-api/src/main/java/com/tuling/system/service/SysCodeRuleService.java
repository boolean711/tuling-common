package com.tuling.system.service;

import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.system.domain.dto.SysCodeRuleSaveDto;
import com.tuling.system.domain.entity.SysCodeRule;
import com.tuling.system.domain.vo.SysCodeRuleVo;

import java.util.List;

public interface SysCodeRuleService extends CrudBaseIService<SysCodeRule, SysCodeRuleVo, SysCodeRuleSaveDto> {

    String generateCode(String rule);


    List<String> generateCodes(String rule, int numberOfCodes);
}
