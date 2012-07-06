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
import org.openmrs.module.dataintegrity.IntegrityCheckRun;
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
	 * @param integrityCheck the integrity check to be saved
	 * @return persisted instance of the integrity check
	 * @throws DAOException
	 */
	public IntegrityCheck saveIntegrityCheck(IntegrityCheck integrityCheck)
			throws DAOException;

	/**
	 * get an integrity check based on provided checkId
	 * 
	 * @param checkId the id of the desired integrity check
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
	 * @param integrityCheck the integrity check to be deleted
	 * @throws DAOException
	 */
	public void deleteIntegrityCheck(IntegrityCheck integrityCheck)
			throws DAOException;

	/**
	 * get the results of a query (columns and data)
	 * 
	 * @param sql the SQL form of this query
	 * @return QueryResults object containing columns and data
	 * @throws DAOException
	 */
	public QueryResults getQueryResults(String sql) throws DAOException;

	/**
	 * get the results of a query (columns and data)
	 *
	 * @param sql the SQL form of this query
	 * @param limit the maximum number of results to return
	 * @return QueryResults object containing columns and data
	 * @throws DAOException
	 */
	public QueryResults getQueryResults(String sql, Integer limit) throws DAOException;

	/**
	 * find a result for a given integrity check by its UID
	 * 
	 * @param integrityCheck the integrity check related to the desired result
	 * @param uid the unique identifier string for the desired result
	 * @return the persisted result object
	 */
	public IntegrityCheckResult findResultForIntegrityCheckByUid(IntegrityCheck integrityCheck, String uid);

	/**
	 * get the most recent run for a given integrity check
	 * 
	 * @param check the check whose most recent run is desired
	 * @return the most recent persisted integrity check run
	 */
	public IntegrityCheckRun getMostRecentRunForCheck(IntegrityCheck check);
}
