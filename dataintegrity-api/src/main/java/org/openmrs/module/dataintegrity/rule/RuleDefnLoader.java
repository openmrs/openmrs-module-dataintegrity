package org.openmrs.module.dataintegrity.rule;

import org.openmrs.module.dataintegrity.DataIntegrityException;
import org.openmrs.module.dataintegrity.db.DataintegrityRule;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

@Component
public class RuleDefnLoader {

    public Map<DataintegrityRule, RuleDefn> getRuleDefns(List<DataintegrityRule> dataintegrityRules) {
        Map<DataintegrityRule, RuleDefn> rulesWithDefn = new HashMap<>();

        for (DataintegrityRule rule : dataintegrityRules){

            if(!"java".equals(rule.getHandlerConfig())){
                throw new IllegalArgumentException("The rule type ["+ rule.getHandlerConfig()+"] is not supported");
            }

            RuleDefn ruleDefn = getJavaRuleInstance(rule.getHandlerClassname());

            if(ruleDefn != null) {
                rulesWithDefn.put(rule, ruleDefn);
            }
        }
        return rulesWithDefn;
    }

    private RuleDefn getJavaRuleInstance(String ruleCode) {
        RuleDefn ruleDefn = null;
        try {
            Class<?> ruleClass = Context.loadClass(ruleCode);
            Object o = ruleClass.newInstance();
            ruleDefn = (RuleDefn) o;
        } catch (Exception e) {
            throw new DataIntegrityException("Failed to load java rule" + getStackTrace(e));
        }
        return ruleDefn;
    }
}
