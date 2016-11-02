package org.openmrs.module.dataintegrity.rule;

import org.openmrs.module.dataintegrity.db.DataintegrityRule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

public class RuleDefnLoaderTest {

	public void setup(){
		initMocks(this);
	}

	@Test(expected = IllegalArgumentException.class)
	public void ensureThrowsExecptionForEmptyRuleDefinition(){

		RuleDefnLoader loader = new RuleDefnLoader();
		List<DataintegrityRule> dataintegrityRules = new ArrayList<>();
		dataintegrityRules.add(new DataintegrityRule());
		loader.getRuleDefns(dataintegrityRules);
	}

}
