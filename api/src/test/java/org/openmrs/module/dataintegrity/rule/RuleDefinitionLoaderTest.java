package org.openmrs.module.dataintegrity.rule;

import org.openmrs.module.dataintegrity.DataIntegrityRule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

public class RuleDefinitionLoaderTest {

	public void setup(){
		initMocks(this);
	}

	@Test(expected = IllegalArgumentException.class)
	public void ensureThrowsExecptionForEmptyRuleDefinition(){

		RuleDefinitionLoader loader = new RuleDefinitionLoader();
		List<DataIntegrityRule> dataIntegrityRules = new ArrayList<>();
		dataIntegrityRules.add(new DataIntegrityRule());
		loader.getRuleDefinitions(dataIntegrityRules);
	}

}
