package org.openmrs.module.dataintegrity.db;

import java.util.List;

import org.openmrs.module.dataintegrity.DataIntegrityResult;
import org.openmrs.module.dataintegrity.DataIntegrityRule;

public interface DataIntegrityDAO {
	
	/**
	 * Retrieves all rules in the system
	 
	 * @return a list of all rule definitions in the system
	 * @should return all rule definitions
	 */
	public List<DataIntegrityRule> getRules();
	/**
	 * Retrieves all the results in the system
	 * @return return all the results
	 */
	public List<DataIntegrityResult> getResults();
	/**
	 * Retrieves all the results for the specified rule
	 *
	 * @param dataIntegrityRule
	 * @return the results for the specified rule definitions
	 */
	public List<DataIntegrityResult> getResultsForRule(DataIntegrityRule dataIntegrityRule);
	
	public DataIntegrityRule getRule(Integer id);
	public DataIntegrityRule getRuleByUuid(String uuid);
	
	public void saveResults(List<DataIntegrityResult> results);
	public DataIntegrityRule saveRule(DataIntegrityRule rule);
	
	/**
	 * Clear all the results
	 */
	public void clearAllResults();
	
	/**
	 * Clear results for the specified fule
	 * @param rule The rule whose results are to be cleared
	 */
	public void clearResultsForRule(DataIntegrityRule rule);
}
