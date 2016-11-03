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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

/**
 * Test cases for the validator for the DataIntegrityRule
 */
public class DataIntegrityRuleValidatorTest extends BaseModuleContextSensitiveTest {
	
	@Autowired
	public DataIntegrityRuleValidator dataIntegrityRuleValidator;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	/**
	 * @verifies fail if the data integrity rule is null
	 * @see DataIntegrityRuleValidator#validate(Object, Errors)
	 */
	@Test
	public void validate_shouldFailIfTheDataIntegrityRuleObjectIsNull() throws Exception {
		Errors errors = new BindException(new DataIntegrityRule(), "dataintegrityrule");
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage(
				"The parameter obj should not be null and must be of type" + DataIntegrityRule.class);
		dataIntegrityRuleValidator.validate(null, errors);
	}
	
	/**
	 * @verifies fail if the rule name, handler config, rule category, handler classname  is null
	 * @see DataIntegrityRuleValidator#validate(Object, Errors)
	 */
	@Test
	public void validate_shouldFailIfRequiredFieldsAreNull() throws Exception {
		DataIntegrityRule rule = new DataIntegrityRule();
		Errors errors = new BindException(rule, "dataintegrityrule");
		dataIntegrityRuleValidator.validate(rule, errors);
		assertTrue(errors.hasFieldErrors("ruleName"));
		assertEquals("dataintegrityrule.error.rulename.required", errors.getFieldError("ruleName").getCode());
		assertTrue(errors.hasFieldErrors("handlerConfig"));
		assertEquals("dataintegrityrule.error.handlerconfig.required", errors.getFieldError("handlerConfig").getCode());
		assertTrue(errors.hasFieldErrors("handlerClassname"));
		assertEquals("dataintegrityrule.error.handlerclassname.required",
				errors.getFieldError("handlerClassname").getCode());
		assertTrue(errors.hasFieldErrors("ruleCategory"));
		assertEquals("dataintegrityrule.error.rulecategory.required", errors.getFieldError("ruleCategory").getCode());
	}
	
	/**
	 * @verifies fail if the rule category is not patient or program
	 * @see DataIntegrityRuleValidator#validate(Object, Errors)
	 */
	@Test
	public void validate_shouldFailIfRuleCategoryIsNotPatientOrProgram() throws Exception {
		DataIntegrityRule rule = new DataIntegrityRule();
		rule.setRuleCategory("obs");
		rule.setHandlerConfig("java");
		rule.setHandlerClassname(getClass().getName());
		rule.setRuleName("Invalid Rule Category");
		
		Errors errors = new BindException(rule, "dataintegrityrule");
		dataIntegrityRuleValidator.validate(rule, errors);
		assertTrue(errors.hasFieldErrors("ruleCategory"));
		assertEquals("dataintegrityrule.error.rulecategory.invalidvalue", errors.getFieldError("ruleCategory").getCode());
	}
	
	/**
	 * @verifies fail if the rule category is not patient or program
	 * @see DataIntegrityRuleValidator#validate(Object, Errors)
	 */
	@Test
	public void validate_shouldFailIfHandlerConfigIsNotJavaOrGroovy() throws Exception {
		DataIntegrityRule rule = new DataIntegrityRule();
		rule.setRuleCategory("patient");
		rule.setHandlerConfig("sql");
		rule.setHandlerClassname(getClass().getName());
		rule.setRuleName("Invalid Handler Config");
		
		Errors errors = new BindException(rule, "dataintegrityrule");
		dataIntegrityRuleValidator.validate(rule, errors);
		assertTrue(errors.hasFieldErrors("handlerConfig"));
		assertEquals("dataintegrityrule.error.handlerconfig.invalidvalue", errors.getFieldError("handlerConfig").getCode());
	}
	
}
