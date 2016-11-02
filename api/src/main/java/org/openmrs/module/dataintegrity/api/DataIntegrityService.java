package org.openmrs.module.dataintegrity.api;

import java.util.List;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.dataintegrity.DataIntegrityResult;
import org.openmrs.module.dataintegrity.DataIntegrityRule;

/**
 * Defines the services provided by the Data Integrity Module
 */
public interface DataIntegrityService extends OpenmrsService {
	
	/**
	 * Saves the specified rule to the database
	 *
	 * @param dataIntegrityRule the rule to save
	 * @return the rule saved
	 */
	public DataIntegrityRule saveRule(DataIntegrityRule dataIntegrityRule);
	/**
	 * Retrieves the data integrity rule with the specified id
	 * @param id
	 * @return The rule with the specified id
	 */
	public DataIntegrityRule getRule(Integer id);
	/**
	 * Retrieves the data integrity rule with the specified uuid
	 * @param uuid
	 * @return The rule with the specified uuid
	 */
	public DataIntegrityRule getRuleByUuid(String uuid);
	/**
	 * Retrieves all rules in the system
	 
	 * @return a list of all rules in the system
	 * @should return all rules in the system
	 */
	public List<DataIntegrityRule> getAllRules();
	
	/**
	 * Retrieves all the results in the system
	 * @return return all the results
	 * @should return all results
	 */
	public List<DataIntegrityResult> getAllResults();
	
	/**
	 * Retrieves all the results for the specified rule
	 *
	 * @param dataIntegrityRule
	 * @return the results for the specified rule definitions
	 */
	public List<DataIntegrityResult> getResultsForRule(DataIntegrityRule dataIntegrityRule);
	/**
	 * Retrieves all the results for the specified rule
	 *
	 * @param uuid
	 * @return the results for the specified rule definitions
	 */
	public List<DataIntegrityResult> getResultsForRuleByUuid(String uuid);
}
