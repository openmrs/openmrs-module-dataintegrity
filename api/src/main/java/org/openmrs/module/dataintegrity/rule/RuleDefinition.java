package org.openmrs.module.dataintegrity.rule;

import java.util.List;

import org.openmrs.module.dataintegrity.DataIntegrityRule;

public interface RuleDefinition<T>
{
	
	/**
	 * Evaluate the defined rule returning any entities that violate it
	 * @return
	 */
	public List<RuleResult<T>> evaluate();
	
	/**
	 * Return the DataIntegrityRule instance that is used to execute this rule. A null value for this method will not save any metadata for the rule
	 * @return
	 */
	public DataIntegrityRule getRule();
}
