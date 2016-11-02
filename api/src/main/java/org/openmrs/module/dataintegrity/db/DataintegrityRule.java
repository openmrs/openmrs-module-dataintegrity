package org.openmrs.module.dataintegrity.db;

import org.openmrs.BaseOpenmrsObject;

import java.io.Serializable;

public class DataintegrityRule extends BaseOpenmrsObject implements Serializable {
    private int ruleId;
    private String ruleName;
    private String ruleCategory;
    private String handlerConfig;
    private String handlerClassname;


    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleCategory() {
        return ruleCategory;
    }

    public void setRuleCategory(String ruleCategory) {
        this.ruleCategory = ruleCategory;
    }

    public String getHandlerConfig() {
        return handlerConfig;
    }

    public void setHandlerConfig(String handlerConfig) {
        this.handlerConfig = handlerConfig;
    }

    public String getHandlerClassname() {
        return handlerClassname;
    }

    public void setHandlerClassname(String handlerClassname) {
        this.handlerClassname = handlerClassname;
    }


    @Override
    public Integer getId() {
        return ruleId;
    }

    @Override
    public void setId(Integer integer) {
        ruleId = integer;
    }
}
