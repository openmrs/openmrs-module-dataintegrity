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

package org.openmrs.module.dataintegrity.db;

import java.util.List;

import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.Program;
import org.openmrs.module.dataintegrity.DataIntegrityResult;
import org.openmrs.module.dataintegrity.DataIntegrityRule;

public interface DataIntegrityDAO {
	
	/**
	 * Retrieves all rules in the system
	 *
	 * @return a list of all rule definitions in the system
	 * @should return all rule definitions
	 */
	public List<DataIntegrityRule> getRules();
	
	/**
	 * Retrieves all the results in the system
	 *
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

	/**
	 * Retrieves all the results for specified patient
	 *
	 * @param patient
	 * @return the results for specified patient
	 */
	public List<DataIntegrityResult> getResultsByPatient(Patient patient);

	/**
	 * Retrieves all the results for specified patientProgram
	 *
	 * @param patientProgram
	 * @return the results for specified patientProgram
	 */
	public List<DataIntegrityResult> getResultsByPatientProgram(PatientProgram patientProgram);
	
	public DataIntegrityRule getRule(Integer id);
	
	public DataIntegrityRule getRuleByUuid(String uuid);

	/**
	 * Retrieves all the data integrity rules belong to specified category
	 *
	 * @param category specified rule category
	 * @return List of data integrity rules
	 */
	public List<DataIntegrityRule> getRuleByCategory(String category);

	public DataIntegrityResult getResultByUuid(String uuid);
	
	public void saveResults(List<DataIntegrityResult> results);
	
	public DataIntegrityRule saveRule(DataIntegrityRule rule);

	/**
	 * Deletes a particular rule
	 *
	 * @param rule
	 */
	void deleteRule(DataIntegrityRule rule);
	
	/**
	 * Clear all the results
	 */
	public void clearAllResults();
	
	/**
	 * Clear results for the specified fule
	 *
	 * @param rule The rule whose results are to be cleared
	 */
	public void clearResultsForRule(DataIntegrityRule rule);
}
