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
import java.util.List;

import org.junit.Test;
import org.openmrs.module.dataintegrity.DataIntegrityRule;

public class RuleDefinitionLoaderTest {
	
	public void setup() {
		initMocks(this);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void ensureThrowsExecptionForEmptyRuleDefinition() {
		
		RuleDefinitionLoader loader = new RuleDefinitionLoader();
		List<DataIntegrityRule> dataIntegrityRules = new ArrayList<>();
		dataIntegrityRules.add(new DataIntegrityRule());
		loader.getRuleDefinitions(dataIntegrityRules);
	}
	
}
