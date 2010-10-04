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
	 * @param dataIntegrityTemplate
	 * @throws APIException
	 */
	public void saveDataIntegrityCheckTemplate(
			DataIntegrityCheckTemplate dataIntegrityTemplate)
			throws APIException;

	/**
	 * returns a single data integrity check
	 * 
	 * @param templateId
	 * @return the data record
	 * @throws APIException
	 * @should not return a null object
	 */
	public DataIntegrityCheckTemplate getDataIntegrityCheckTemplate(
			Integer templateId) throws APIException;

	/**
	 * returns all non-voided data integrity checks
	 * 
	 * @return
	 * @throws APIException
	 */
	public List<DataIntegrityCheckTemplate> getAllDataIntegrityCheckTemplates()
			throws APIException;

	/**
	 * deletes a data integrity check
	 * 
	 * @param template
	 */
	public void deleteDataIntegrityCheckTemplate(
			DataIntegrityCheckTemplate template);

	/**
	 * saves the results of a single data integrity check
	 * 
	 * @param results
	 * @throws APIException
	 * @should not throw an error saving results
	 */
	public DataIntegrityCheckResultTemplate saveResults(DataIntegrityCheckResultTemplate results)
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
	public DataIntegrityCheckResultTemplate getResults(Integer resultsId)
			throws APIException;

	/**
	 * deletes a set of results
	 * 
	 * @param template
	 */
	public void deleteResults(DataIntegrityCheckResultTemplate results);

	/**
	 * runs a data integrity check
	 * 
	 * @param template
	 * @param parameterValues
	 * @return
	 * @throws Exception
	 */
	public DataIntegrityCheckResultTemplate runIntegrityCheck(
			DataIntegrityCheckTemplate template, String parameterValues)
			throws Exception;

	/**
	 * repairs data based on a given script
	 * 
	 * @param template
	 * @throws Exception
	 */
	public void repairDataIntegrityCheckViaScript(
			DataIntegrityCheckTemplate template) throws Exception;

	/**
	 * returns results for the given integrity check
	 * 
	 * @param check
	 * @return
	 */
	public DataIntegrityCheckResultTemplate getResultsForCheck(
			DataIntegrityCheckTemplate check);
}
