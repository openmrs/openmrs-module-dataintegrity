/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 *
 */

package org.openmrs.module.dataintegrity;

import java.io.Serializable;
import java.util.Set;

import org.openmrs.BaseOpenmrsObject;
import org.openmrs.annotation.Independent;

public class DataIntegrityRule extends BaseOpenmrsObject implements Serializable {
	
	private int ruleId;
	
	private String ruleName;
	
	private String ruleCategory;
	
	private String handlerConfig;
	
	private String handlerClassname;
	
	@Independent
	private Set<DataIntegrityResult> results;
	
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
	
	/**
	 * Returns the results for running this rule
	 *
	 * @return
	 */
	public Set<DataIntegrityResult> getResults() {
		return results;
	}
	
	public void setResults(Set<DataIntegrityResult> results) {
		this.results = results;
	}
	
	@Override
	public Integer getId() {
		return getRuleId();
	}
	
	@Override
	public void setId(Integer id) {
		setRuleId(id);
	}
	
	@Override
	public String toString() {
		return getRuleName() + " for " + getRuleCategory() + " with handler " + getHandlerConfig() + " using "
				+ getHandlerClassname() + " with rule_id " + getRuleId() + " and uuid " + getUuid();
	}
	
	public void addResult(DataIntegrityResult result) {
		result.setRule(this);
		getResults().add(result);
	}
}
