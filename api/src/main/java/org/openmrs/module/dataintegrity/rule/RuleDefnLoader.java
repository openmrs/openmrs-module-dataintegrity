package org.openmrs.module.dataintegrity.rule;

import groovy.lang.GroovyClassLoader;
import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.DataIntegrityException;
import org.openmrs.module.dataintegrity.db.DataintegrityRule;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

@Component
public class RuleDefnLoader {

    public static final String GROOVY_DQ_RULES_PATH = "/dataQualityRules/";

    public Map<DataintegrityRule, RuleDefn> getRuleDefns(List<DataintegrityRule> dataintegrityRules) {
        Map<DataintegrityRule, RuleDefn> rulesWithDefn = new HashMap<>();

        for (DataintegrityRule rule : dataintegrityRules){
            RuleDefn ruleDefn = getRuleInstance(rule);
            if(ruleDefn != null) {
                rulesWithDefn.put(rule, ruleDefn);
            }
        }
        return rulesWithDefn;
    }

    private RuleDefn getRuleInstance(DataintegrityRule rule){

        if("java".equals(rule.getHandlerConfig())){
            return getJavaRuleInstance(rule.getHandlerClassname());
        }else if("groovy".equals(rule.getHandlerConfig())){
            return getGroovyRuleInstance(rule.getHandlerClassname());
        }else{
            throw new IllegalArgumentException("The rule type ["+ rule.getHandlerConfig()+"] is not supported");
        }
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

    private RuleDefn getGroovyRuleInstance(String groovyFileName) {
        RuleDefn ruleDefn = null;

        try {
            Class clazz = new GroovyClassLoader().parseClass(new File(getGroovyClassPath(groovyFileName)));
            ruleDefn = (RuleDefn) clazz.newInstance();
        }
        catch (Exception e) {
            throw new DataIntegrityException("Failed to load groovy rule " + getStackTrace(e));
        }
        return ruleDefn;
    }

    private String getGroovyClassPath(String groovyFileName) {
        return OpenmrsUtil.getApplicationDataDirectory() + GROOVY_DQ_RULES_PATH + groovyFileName ;
    }
}
