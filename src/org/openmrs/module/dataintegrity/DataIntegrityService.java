/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.dataintegrity;

import java.util.List;

import org.openmrs.api.APIException;
import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * service for Data Integrity object manipulation and persistence
 */
@Transactional
public interface DataIntegrityService {

	/**
	 * sets the data integrity DAO
	 * 
	 * @param dao
	 */
	public void setDataIntegrityDAO(DataIntegrityDAO dao);

	/**
	 * returns the data integrity DAO
	 * 
	 * @return
	 */
	public DataIntegrityDAO getDataIntegrityDAO();

	/**
	 * saves a single data integrity check
	 * 
	 * @param integrityCheck
	 * @throws APIException
	 */
	public IntegrityCheck saveIntegrityCheck(IntegrityCheck integrityCheck)
			throws APIException;

	/**
	 * returns a single data integrity check
	 * 
	 * @param checkId
	 * @return the data record
	 * @throws APIException
	 * @should not return a null object
	 */
	public IntegrityCheck getIntegrityCheck(Integer checkId)
			throws APIException;

	/**
	 * returns all non-voided data integrity checks
	 * 
	 * @return
	 * @throws APIException
	 */
	public List<IntegrityCheck> getAllIntegrityChecks() throws APIException;

	/**
	 * deletes a data integrity check
	 * 
	 * @param integrityCheck
	 */
	public void deleteIntegrityCheck(IntegrityCheck integrityCheck);

	/**
	 * saves the results of a single data integrity check
	 * 
	 * @param results
	 * @throws APIException
	 * @should not throw an error saving results
	 */
	public IntegrityCheckResults saveResults(IntegrityCheckResults results)
			throws APIException;

	/**
	 * returns the results of a single data integrity check
	 * 
	 * @param resultsId
	 * @return the data record
	 * @throws APIException
	 * @should retrieve results if a check exists
	 * @should return null if results for a check do not exist
	 */
	public IntegrityCheckResults getResults(Integer resultsId)
			throws APIException;

	/**
	 * deletes a set of results
	 * 
	 * @param template
	 */
	public void deleteResults(IntegrityCheckResults results);

	/**
	 * runs a data integrity check
	 * 
	 * @param integrityCheck
	 * @param parameterValues
	 * @return
	 * @throws Exception
	 * @should return results with linked integrity check
	 */
	public IntegrityCheckResults runIntegrityCheck(
			IntegrityCheck integrityCheck, String parameterValues)
			throws Exception;

	/**
	 * repairs data based on a given script
	 * 
	 * @param integrityCheck
	 * @throws Exception
	 */
	public void repairIntegrityCheckViaScript(
			IntegrityCheck integrityCheck) throws Exception;

	/**
	 * returns results for the given integrity check
	 * 
	 * @param integrityCheck
	 * @return
	 */
	public IntegrityCheckResults getResultsForIntegrityCheck(
			IntegrityCheck integrityCheck);
}
