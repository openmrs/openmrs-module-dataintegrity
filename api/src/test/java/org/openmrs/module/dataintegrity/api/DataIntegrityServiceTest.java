package org.openmrs.module.dataintegrity.api;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.dataintegrity.DataIntegrityResult;
import org.openmrs.module.dataintegrity.DataIntegrityRule;
import org.openmrs.module.dataintegrity.api.DataIntegrityService;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests methods in the {@link DataIntegrityService}
 *
 * @since 4.1
 */
public class DataIntegrityServiceTest extends BaseModuleContextSensitiveTest {
	
	protected static final String DATAINTEGRITY_RULES_XML = "org/openmrs/module/dataintegrity/include/DataIntegrityServiceTest-rules.xml";
	
	@Autowired
	private DataIntegrityService dataIntegrityService;
	
	@Before
	public void before() {
		
	}
	
	@Test
	@Verifies(value = "should get all rules", method = "getAllRules()")
	public void getAllRules_shouldGetAllRules() throws Exception {
		executeDataSet(DATAINTEGRITY_RULES_XML);
		List<DataIntegrityRule> rules = dataIntegrityService.getAllRules();
		assertEquals(3, rules.size());
	}
	
	@Test
	@Verifies(value = "should get all results", method = "getAllResults()")
	public void getAllRules_shouldGetAllResults() throws Exception {
		executeDataSet(DATAINTEGRITY_RULES_XML);
		List<DataIntegrityResult> results = dataIntegrityService.getAllResults();
		assertEquals(2, results.size());
	}
	
	@Test
	@Verifies(value = "should save a new rule", method="saveRule(DataIntegrityRule)")
	public void saveRule_shouldSaveANewRule(){
		DataIntegrityRule rule = new DataIntegrityRule();
		rule.setHandlerClassname("org.openmrs.module.NewRule");
		rule.setRuleName("New Rule");
		rule.setRuleCategory("patient");
		rule.setHandlerConfig("java");
		
		dataIntegrityService.saveRule(rule);
		assertNotNull(rule.getId());
		assertNotNull(rule.getUuid());
	}
	
	@Test
	@Verifies(value = "should update existing rule with same uuid", method="saveRule(DataIntegrityRule)")
	public void saveRule_shouldUpdateExistingRuleWithSameUUID(){
		DataIntegrityRule rule = new DataIntegrityRule();
		rule.setHandlerClassname("org.openmrs.module.AnExistingRule");
		rule.setRuleName("Existing Rule");
		rule.setRuleCategory("program");
		rule.setHandlerConfig("groovy");
		rule.setUuid("097fe53a-36b4-475c-95ea-a429c9512014");
		
		dataIntegrityService.saveRule(rule);
		
		// now load the rule
		DataIntegrityRule existing = dataIntegrityService.getRuleByUuid(rule.getUuid());
		
		assertEquals(1, existing.getRuleId());
		assertEquals(rule.getRuleId(), existing.getRuleId());
		assertEquals(rule.getHandlerClassname(), existing.getHandlerClassname());
		assertEquals(rule.getHandlerConfig(), existing.getHandlerConfig());
		assertEquals(rule.getRuleCategory(), existing.getRuleCategory());
		assertEquals(rule.getRuleName(), existing.getRuleName());
	}
	
	@Test
	@Verifies(value = "should update existing rule with same uuid even with results", method="saveRule(DataIntegrityRule)")
	public void saveRule_shouldUpdateExistingRuleWithSameUUIDEvenWithResults(){
		DataIntegrityRule rule = new DataIntegrityRule();
		rule.setHandlerClassname("org.openmrs.module.AnExistingRuleWithResults");
		rule.setRuleName("Existing Rule With Results");
		rule.setRuleCategory("program");
		rule.setHandlerConfig("groovy");
		rule.setUuid("72330639-0a68-47bb-b5c8-ac8350658866");
		
		dataIntegrityService.saveRule(rule);
		
		// now load the rule
		DataIntegrityRule existing = dataIntegrityService.getRuleByUuid(rule.getUuid());
		
		assertEquals(3, existing.getRuleId());
		assertEquals(rule.getRuleId(), existing.getRuleId());
		assertEquals(rule.getHandlerClassname(), existing.getHandlerClassname());
		assertEquals(rule.getHandlerConfig(), existing.getHandlerConfig());
		assertEquals(rule.getRuleCategory(), existing.getRuleCategory());
		assertEquals(rule.getRuleName(), existing.getRuleName());
	}
	
	
}
