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

package org.openmrs.module.dataintegrity.db;

import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.dataintegrity.IntegrityCheck;
import org.openmrs.module.dataintegrity.IntegrityCheckResult;
import org.openmrs.module.dataintegrity.IntegrityCheckResults;
import org.openmrs.module.dataintegrity.QueryResults;

public interface DataIntegrityDAO {

	/**
	 * set the session factory
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory);

	/**
	 * get the session factory
	 * 
	 * @return the session factory
	 */
	public SessionFactory getSessionFactory();

	/**
	 * save an integrity check to the database and return the persisted instance
	 * 
	 * @param integrityCheck
	 * @return persisted instance of the integrity check
	 * @throws DAOException
	 */
	public IntegrityCheck saveIntegrityCheck(IntegrityCheck integrityCheck)
			throws DAOException;

	/**
	 * get an integrity check based on provided checkId
	 * 
	 * @param checkId
	 * @return matching integrity check
	 * @throws DAOException
	 */
	public IntegrityCheck getIntegrityCheck(Integer checkId)
			throws DAOException;

	/**
	 * get all integrity checks from the database
	 * 
	 * @return a list of all integrity checks
	 * @throws DAOException
	 */
	public List<IntegrityCheck> getAllIntegrityChecks() throws DAOException;

	/**
	 * delete an integrity check
	 * 
	 * @param integrityCheck
	 * @throws DAOException
	 */
	public void deleteIntegrityCheck(IntegrityCheck integrityCheck)
			throws DAOException;

	/**
	 * save the results of an integrity check to the database
	 * 
	 * @param results
	 * @return persisted instance of the results
	 */
	public IntegrityCheckResults saveResults(IntegrityCheckResults results);

	/**
	 * get the results of an integrity check based on the resultsId
	 * 
	 * @param resultsId
	 * @return matching integrity check results
	 */
	public IntegrityCheckResults getResults(Integer resultsId);

	/**
	 * delete integrity check results from the database
	 * 
	 * @param results
	 */
	public void deleteResults(IntegrityCheckResults results);

	/**
	 * get results based on an integrity check id
	 * 
	 * @param integrityCheck
	 * @return matching integrity check results
	 */
	public IntegrityCheckResults getResultsForIntegrityCheck(
			IntegrityCheck integrityCheck);

	/**
	 * get the results of a query (columns and data)
	 * 
	 * @param sql
	 * @return QueryResults object containing columns and data
	 * @throws DAOException
	 */
	public QueryResults getQueryResults(String sql) throws DAOException;

	/**
	 * get the results of a query (columns and data)
	 *
	 * @param sql
	 * @return QueryResults object containing columns and data
	 * @throws DAOException
	 */
	public QueryResults getQueryResults(String sql, Integer limit) throws DAOException;

	public IntegrityCheckResult findResultForIntegrityCheckByUid(IntegrityCheck integrityCheck, String uid);
}
