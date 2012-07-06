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
	 * @param integrityCheck the integrity check to be saved
	 * @throws APIException
	 */
	public IntegrityCheck saveIntegrityCheck(IntegrityCheck integrityCheck)
			throws APIException;

	/**
	 * returns a single data integrity check
	 * 
	 * @param checkId the identifier of the integrity check to be retrieved
	 * @return the requested integrity check
	 * @throws APIException
	 * @should not return a null object
	 */
	public IntegrityCheck getIntegrityCheck(Integer checkId)
			throws APIException;

	/**
	 * returns all non-retired data integrity checks
	 * 
	 * @return all non-retired data integrity checks
	 * @throws APIException
	 */
	public List<IntegrityCheck> getAllIntegrityChecks() throws APIException;

	/**
	 * deletes a data integrity check
	 * 
	 * @param integrityCheck the integrity check to be deleted
	 */
	public void deleteIntegrityCheck(IntegrityCheck integrityCheck);

	/**
	 * runs a data integrity check
	 * 
	 * @param integrityCheck the integrity check to be run
	 * @throws Exception
	 */
	public IntegrityCheckRun runIntegrityCheck(IntegrityCheck integrityCheck) throws Exception;

    /**
     * runs a query and returns the results
     * 
     * @param code the SQL version of this query
     * @return a result set for the given query
     */
    public QueryResults getQueryResults(String code) throws APIException;
        
    /**
     * runs a query and returns the results, with a limit on the number of rows returned
     *
     * @param code the SQL version of this query
	 * @param limit the maximum number of rows to return
     * @return a result set for the given query
     */
    public QueryResults getQueryResults(String code, Integer limit) throws APIException;

	/**
	 * find a result for a given integrity check by its UID
	 * 
	 * @param integrityCheck the integrity check related to the desired result
	 * @param uid the unique identifier string for the desired result
	 * @return the persisted result object
	 */
	public IntegrityCheckResult findResultForIntegrityCheckByUid(IntegrityCheck integrityCheck, String uid);

	/**
	 * retires the given integrity check
	 * 
	 * @param check the integrity check to retire
	 * @param reason the reason provided for retiring
	 */
	public void retireIntegrityCheck(IntegrityCheck check, String reason);

	/**
	 * un-retires the given integrity check
	 * 
	 * @param check the integrity check to be un-retired
	 */
	public void unretireIntegrityCheck(IntegrityCheck check);

	/**
	 * get the most recent run for a given integrity check
	 * 
	 * @param check the check whose most recent run is desired
	 * @return the most recent persisted integrity check run
	 */
	public List<IntegrityCheckRun> getMostRecentRunsForAllChecks();

}
