package com.tuling.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.domain.dto.SysCodeRuleSaveDto;
import com.tuling.system.domain.entity.SysCodeRule;
import com.tuling.system.domain.vo.SysCodeRuleVo;
import com.tuling.system.mapper.SysCodeRuleMapper;
import com.tuling.system.service.SysCodeRuleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class SysCodeRuleServiceImpl extends CrudBaseServiceImpl<SysCodeRule, SysCodeRuleVo, SysCodeRuleSaveDto, SysCodeRuleMapper> implements SysCodeRuleService {

    private static final Lock lock = new ReentrantLock();


    @Override
    @Transactional
    public String generateCode(String rule) {
        try {
            lock.tryLock();
            String res;
            LambdaQueryWrapper<SysCodeRule> lwq = new LambdaQueryWrapper<>();

            lwq.eq(SysCodeRule::getRulesCode, rule);


            SysCodeRule codeRule = this.getOne(lwq);
            Integer currentValue = codeRule.getCurrentValue();
            Integer nowVal = currentValue == 0 ? codeRule.getInitValue() : currentValue;
            String format = String.format("%0" + codeRule.getItemLength() + "d", nowVal);

            res = codeRule.getPrefix() + format;

            codeRule.setCurrentValue(++currentValue);
            this.saveOrUpdate(codeRule);
            return res;

        } finally {
            lock.unlock();
        }
    }
    @Override
    @Transactional
    public List<String> generateCodes(String rule, int numberOfCodes) {
        List<String> codes = new ArrayList<>();
        try {
            lock.tryLock();
            LambdaQueryWrapper<SysCodeRule> lwq = new LambdaQueryWrapper<>();
            lwq.eq(SysCodeRule::getRulesCode, rule);

            SysCodeRule codeRule = this.getOne(lwq);
            Integer currentValue = codeRule.getCurrentValue();
            Integer nowVal = currentValue == 0 ? codeRule.getInitValue() : currentValue;

            for (int i = 0; i < numberOfCodes; i++) {
                String format = String.format("%0" + codeRule.getItemLength() + "d", nowVal);
                String code = codeRule.getPrefix() + format;
                codes.add(code);
                nowVal++;
            }

            codeRule.setCurrentValue(nowVal);
            this.saveOrUpdate(codeRule);

        } finally {
            lock.unlock();
        }
        return codes;
    }
}
