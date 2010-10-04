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
import org.openmrs.module.dataintegrity.DataIntegrityCheckResultTemplate;
import org.openmrs.module.dataintegrity.DataIntegrityCheckTemplate;
import org.openmrs.module.dataintegrity.DataIntegrityConstants;
import org.openmrs.module.dataintegrity.DataIntegrityService;
import org.openmrs.module.dataintegrity.QueryResults;
import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;
import org.openmrs.module.dataintegrity.executors.BooleanCheckExecutor;
import org.openmrs.module.dataintegrity.executors.CountCheckExecutor;
import org.openmrs.module.dataintegrity.executors.ICheckExecutor;
import org.openmrs.module.dataintegrity.executors.NumberCheckExecutor;
import org.openmrs.module.dataintegrity.executors.StringCheckExecutor;

public class DataIntegrityServiceImpl implements DataIntegrityService {

	private DataIntegrityDAO dao;

	private Map<String, ICheckExecutor> executors = null;

	public List<DataIntegrityCheckTemplate> getAllDataIntegrityCheckTemplates()
			throws APIException {
		return this.dao.getAllDataIntegrityCheckTemplates();
	}

	public DataIntegrityCheckTemplate getDataIntegrityCheckTemplate(
			Integer templateId) throws APIException {
		return this.dao.getDataIntegrityCheckTemplate(templateId);
	}

	public void saveDataIntegrityCheckTemplate(
			DataIntegrityCheckTemplate dataIntegrityTemplate)
			throws APIException {
		this.dao.saveDataIntegrityCheckTemplate(dataIntegrityTemplate);
	}

	public void setDataIntegrityDAO(DataIntegrityDAO dao) {
		this.dao = dao;
	}

	public DataIntegrityDAO getDataIntegrityDAO() {
		return this.dao;
	}

	public void deleteDataIntegrityCheckTemplate(
			DataIntegrityCheckTemplate template) {
		this.dao.deleteDataIntegrityCheckTemplate(template);
	}

	public DataIntegrityCheckResultTemplate runIntegrityCheck(
			DataIntegrityCheckTemplate template, String parameterValues)
			throws Exception {
		ICheckExecutor executor = getExecutors().get(template.getResultType());
		executor.initializeExecutor(template, parameterValues);

		DataIntegrityCheckResultTemplate resultTemplate = new DataIntegrityCheckResultTemplate(
				template);

		Long startTime = System.currentTimeMillis();
		executor.executeCheck();
        resultTemplate.setDuration(System.currentTimeMillis() - startTime);

		QueryResults failedRecords = new QueryResults(executor.getFailedRecords());
		resultTemplate.setFailedRecords(failedRecords);
		resultTemplate.setCheckPassed(executor.getCheckResult());
		resultTemplate.setFailedRecordCount(failedRecords.size());
		
		// save (or update) the results
		this.saveResults(resultTemplate);
		
		return resultTemplate;
	}

	public void repairDataIntegrityCheckViaScript(
			DataIntegrityCheckTemplate template) throws Exception {
		this.dao.repairDataIntegrityCheckViaScript(template);
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

	/**
	 * @see DataIntegrityService#saveResults(DataIntegrityCheckResultTemplate)
	 */
	public DataIntegrityCheckResultTemplate saveResults(DataIntegrityCheckResultTemplate results)
			throws APIException {
		results.setDateOccurred(new Date());
		return dao.saveResults(results);
	}

	/**
	 * @see DataIntegrityService#getResults(Integer)
	 */
	public DataIntegrityCheckResultTemplate getResults(Integer resultsId)
			throws APIException {
		return dao.getResults(resultsId);
	}

	/**
	 * @see DataIntegrityService#deleteResults(DataIntegrityCheckResultTemplate)
	 */
	public void deleteResults(DataIntegrityCheckResultTemplate results) {
		dao.deleteResults(results);
	}

	/**
	 * @see DataIntegrityService#getResultsForCheck(DataIntegrityCheckTemplate)
	 */
	public DataIntegrityCheckResultTemplate getResultsForCheck(
			DataIntegrityCheckTemplate check) {
		return dao.getResultsForCheck(check);
	}
	
}
