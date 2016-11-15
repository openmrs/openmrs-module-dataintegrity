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

package org.openmrs.module.dataintegrity.rule;

import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.dataintegrity.DataIntegrityRule;
import org.springframework.util.Assert;

public class RuleResultMapperTest {
	
	RuleResultMapper resultMapper;
	
	List<RuleResult> inputresult;
	
	Map<DataIntegrityRule, RuleDefinition> rulesMap;
	
	@Before
	public void setUp() throws Exception {
		initMocks(this);
		inputresult = new ArrayList<>();
		resultMapper = new RuleResultMapper();
		rulesMap = new HashMap<>();
	}
	
	@Test
	public void shouldNotThrowExceptionForEmptyRulesList() throws Exception {
		
		List diResults = null;
		for (Entry<DataIntegrityRule, RuleDefinition> ruleEntry : rulesMap.entrySet()) {
			diResults = resultMapper.getDataIntegrityResults(ruleEntry, inputresult);
		}
		
		Assert.isNull(diResults);
	}
	
	@Test
	public void shouldReturnEmptyListForEmptyRuleMapValue() throws Exception {
		
		DataIntegrityRule rule = new DataIntegrityRule();
		rule.setId(Integer.valueOf("1001"));
		rule.setRuleName("TestRule");
		
		rulesMap.put(rule, null);
		
		List diResults = null;
		for (Entry<DataIntegrityRule, RuleDefinition> ruleEntry : rulesMap.entrySet()) {
			diResults = resultMapper.getDataIntegrityResults(ruleEntry, inputresult);
		}
		
		Assert.notNull(diResults);
		Assert.isTrue(diResults.isEmpty());
	}
	
}