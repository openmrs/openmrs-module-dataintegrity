package org.openmrs.module.dataintegrity;

import org.openmrs.annotation.Handler;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component("dataIntegrityRuleValidator")
@Handler(supports = DataIntegrityRule.class, order = 50)
public class DataIntegrityRuleValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> aClass) {
		return DataIntegrityRule.class.isAssignableFrom(aClass);
	}
	
	/**
	 * @see Validator#validate(Object, Errors)
	 * @should fail if the data integrity rule object is null
	 * @should fail if the handler classname is null
	 * @should fail if the rule name is null
	 * @should fail if the rule category is null
	 * @should fail if the rule category is not patient or program
	 * @should fail if the handler config is null
	 * @should fail is the handler config is not java or groovy
	 * @should pass for a valid data integrity rule object
	 */
	
	@Override
	public void validate(Object target, Errors errors) {
		if (target == null || !(target instanceof DataIntegrityRule)) {
			throw new IllegalArgumentException("The parameter obj should not be null and must be of type" + getClass());
		}
		
		DataIntegrityRule rule = (DataIntegrityRule) target;
		
		ValidationUtils.rejectIfEmpty(errors, "ruleName", "dataintegrityrule.error.rulename.required");
		ValidationUtils.rejectIfEmpty(errors, "handlerClassname", "dataintegrityrule.error.handlerclassname.required");
		ValidationUtils.rejectIfEmpty(errors, "handlerConfig", "dataintegrityrule.error.handlerconfig.required");
		ValidationUtils.rejectIfEmpty(errors, "ruleCategory", "dataintegrityrule.error.rulecategory.required");
		
		if (rule.getRuleCategory() != "patient" || rule.getRuleCategory() != "program") {
			errors.rejectValue("ruleCategory", "dataintegrityrule.error.rulecategory.invalidvalue");
		}
		
		if (rule.getHandlerConfig() != "java" || rule.getHandlerConfig() != "groovy") {
			errors.rejectValue("handlerConfig", "dataintegrityrule.error.handlerconfig.invalidvalue");
		}
		
		
	}
}
