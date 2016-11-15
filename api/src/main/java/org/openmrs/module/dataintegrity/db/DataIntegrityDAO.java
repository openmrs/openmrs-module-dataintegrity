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
	 *
	 * @param rule The rule whose results are to be cleared
	 */
	public void clearResultsForRule(DataIntegrityRule rule);
}
