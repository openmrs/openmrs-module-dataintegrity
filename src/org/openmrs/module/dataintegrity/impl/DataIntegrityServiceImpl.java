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

package org.openmrs.module.dataintegrity.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openmrs.api.APIException;
import org.openmrs.module.dataintegrity.DataIntegrityConstants;
import org.openmrs.module.dataintegrity.DataIntegrityService;
import org.openmrs.module.dataintegrity.IntegrityCheck;
import org.openmrs.module.dataintegrity.IntegrityCheckResults;
import org.openmrs.module.dataintegrity.QueryResults;
import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;
import org.openmrs.module.dataintegrity.executors.BooleanCheckExecutor;
import org.openmrs.module.dataintegrity.executors.CountCheckExecutor;
import org.openmrs.module.dataintegrity.executors.ICheckExecutor;
import org.openmrs.module.dataintegrity.executors.NumberCheckExecutor;
import org.openmrs.module.dataintegrity.executors.StringCheckExecutor;
import org.springframework.util.StringUtils;

/**
 * Implementation of DataIntegrityService
 * 
 * @see org.openmrs.module.dataintegrity.DataIntegrityService
 */
public class DataIntegrityServiceImpl implements DataIntegrityService {

	/**
	 * dao for use with this service implementation
	 */
	private DataIntegrityDAO dao;

	/**
	 * cache of executors
	 */
	private Map<String, ICheckExecutor> executors = null;
	
	/**
	 * @see org.openmrs.module.dataintegrity.DataIntegrityService#setDataIntegrityDAO(DataIntegrityDAO)
	 */
	public void setDataIntegrityDAO(DataIntegrityDAO dao) {
		this.dao = dao;
	}

	/**
	 * @see org.openmrs.module.dataintegrity.DataIntegrityService#getDataIntegrityDAO()
	 */
	public DataIntegrityDAO getDataIntegrityDAO() {
		return this.dao;
	}

	/**
	 * @see org.openmrs.module.dataintegrity.DataIntegrityService#getAllIntegrityChecks()
	 */
	public List<IntegrityCheck> getAllIntegrityChecks() throws APIException {
		return this.dao.getAllIntegrityChecks();
	}
	
	/**
	 * @see org.openmrs.module.dataintegrity.DataIntegrityService#getIntegrityCheck(Integer)
	 */
	public IntegrityCheck getIntegrityCheck(Integer checkId)
			throws APIException {
		return this.dao.getIntegrityCheck(checkId);
	}

	/**
	 * @see org.openmrs.module.dataintegrity.DataIntegrityService#saveIntegrityCheck(IntegrityCheck)
	 */
	public IntegrityCheck saveIntegrityCheck(IntegrityCheck integrityCheck)
			throws APIException {
		return this.dao.saveIntegrityCheck(integrityCheck);
	}

	/**
	 * @see org.openmrs.module.dataintegrity.DataIntegrityService#deleteIntegrityCheck(IntegrityCheck)
	 */
	public void deleteIntegrityCheck(IntegrityCheck integrityCheck) {
		this.dao.deleteIntegrityCheck(integrityCheck);
	}

	/**
	 * @see org.openmrs.module.dataintegrity.DataIntegrityService#runIntegrityCheck(IntegrityCheck, String)
	 */
	public IntegrityCheckResults runIntegrityCheck(IntegrityCheck integrityCheck, String parameterValues)
			throws Exception {
		// do nothing if the check is null
		if (integrityCheck == null)
			return null;

		// pick the executor
		ICheckExecutor executor = getExecutors().get(
				integrityCheck.getResultType());
		if (executor == null)
			throw new APIException(
					"An executor was expected for integrity check type '"
							+ integrityCheck.getResultType()
							+ "' but none was found.");
		
		executor.initializeExecutor(integrityCheck, parameterValues);

		IntegrityCheckResults results = new IntegrityCheckResults(integrityCheck);

		// time the query
		Long startTime = System.currentTimeMillis();
		executor.executeCheck();
		results.setDuration(System.currentTimeMillis() - startTime);
		
		QueryResults failedRecords = executor.getFailedRecords();
		results.setCheckPassed(executor.getCheckResult());
		results.setFailedRecordCount(failedRecords.size());

		// get the repair results if it failed
		if (!results.isCheckPassed() && StringUtils.hasText(integrityCheck.getRepairCode())) {
			// time the repair query
			startTime = System.currentTimeMillis();
			failedRecords = dao.getQueryResults(integrityCheck.getRepairCode());
			results.setDuration(results.getDuration() + System.currentTimeMillis() - startTime);
		} 
		
		results.setFailedRecords(failedRecords);

		// save (or update) the results
		return this.saveResults(results);
	}

	/**
	 * @see org.openmrs.module.dataintegrity.DataIntegrityService#repairIntegrityCheckViaScript(IntegrityCheck)
	 **/
	public void repairIntegrityCheckViaScript(IntegrityCheck integrityCheck)
			throws Exception {
		this.dao.repairDataIntegrityCheckViaScript(integrityCheck);
	}

	/**
	 * @see DataIntegrityService#saveResults(IntegrityCheckResults)
	 */
	public IntegrityCheckResults saveResults(IntegrityCheckResults results)
			throws APIException {
		results.setDateOccurred(new Date());
		return dao.saveResults(results);
	}

	/**
	 * @see DataIntegrityService#getResults(Integer)
	 */
	public IntegrityCheckResults getResults(Integer resultsId)
			throws APIException {
		return dao.getResults(resultsId);
	}

	/**
	 * @see DataIntegrityService#deleteResults(IntegrityCheckResults)
	 */
	public void deleteResults(IntegrityCheckResults results) {
		dao.deleteResults(results);
	}

	/**
	 * @see DataIntegrityService#getResultsForIntegrityCheck(IntegrityCheck)
	 */
	public IntegrityCheckResults getResultsForIntegrityCheck(
			IntegrityCheck check) {
		return dao.getResultsForIntegrityCheck(check);
	}

	/**
	 * builds a cache of executors used for processing integrity checks
	 * 
	 * @return the cached map of executors
	 */
	private Map<String, ICheckExecutor> getExecutors() {
		if (executors == null) {
			executors = new HashMap<String, ICheckExecutor>();
			executors.put(DataIntegrityConstants.RESULT_TYPE_BOOLEAN,
					new BooleanCheckExecutor(this.getDataIntegrityDAO()));
			executors.put(DataIntegrityConstants.RESULT_TYPE_COUNT,
					new CountCheckExecutor(this.getDataIntegrityDAO()));
			executors.put(DataIntegrityConstants.RESULT_TYPE_NUMBER,
					new NumberCheckExecutor(this.getDataIntegrityDAO()));
			executors.put(DataIntegrityConstants.RESULT_TYPE_STRING,
					new StringCheckExecutor(this.getDataIntegrityDAO()));
		}
		return executors;
	}

}
