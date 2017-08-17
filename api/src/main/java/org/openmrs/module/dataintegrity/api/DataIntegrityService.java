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
	 * Delete the specified rule from the database
	 *
	 * @param dataIntegrityRule the rule to delete
	 */
	public void deleteRule(DataIntegrityRule dataIntegrityRule);
	
	/**
	 * Retrieves the data integrity rule with the specified id
	 *
	 * @param id
	 * @return The rule with the specified id
	 */
	public DataIntegrityRule getRule(Integer id);
	
	/**
	 * Retrieves the data integrity rule with the specified uuid
	 *
	 * @param uuid
	 * @return The rule with the specified uuid
	 */
	public DataIntegrityRule getRuleByUuid(String uuid);

	/**
	 * Retrieves all the data integrity rules belong to specified category
	 *
	 * @param category specified rule category
	 * @return List of data integrity rules
	 */
	public List<DataIntegrityRule> getRuleByCategory(String category);
	
	/**
	 * Retrieves all rules in the system
	 *
	 * @return a list of all rules in the system
	 * @should return all rules in the system
	 */
	public List<DataIntegrityRule> getAllRules();

	/**
	 * Retrieves the data integrity rule with the specified uuid
	 *
	 * @param uuid
	 * @return The rule with the specified uuid
	 */
	public DataIntegrityResult getResultByUuid(String uuid);
	
	/**
	 * Retrieves all the results in the system
	 *
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

	/**
	 * Retrieves all the results for specified patient
	 *
	 * @param patientUuid
	 * @return the results for specified patient
	 */
	public List<DataIntegrityResult> getResultsByPatientUuid(String patientUuid);

	/**
	 * Retrieves all the results belong to specified patientProgram
	 *
	 * @param patientProgramId
	 * @return the results for specified patientProgram
	 */
	public List<DataIntegrityResult> getResultsByPatientProgram(int patientProgramId);

}
