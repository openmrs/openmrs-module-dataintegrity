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

package org.openmrs.module.dataintegrity.db.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.dataintegrity.IntegrityCheckResults;
import org.openmrs.module.dataintegrity.IntegrityCheck;
import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;

public class HibernateDataIntegrityDAO implements DataIntegrityDAO {

	private SessionFactory sessionFactory;

	/**
	 * sets the session factory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * returns the session factory
	 */
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	/**
	 * @see DataIntegrityDAO#getAllIntegrityChecks()
	 */
	@SuppressWarnings("unchecked")
	public List<IntegrityCheck> getAllIntegrityChecks() throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				IntegrityCheck.class, "template");
		return (List<IntegrityCheck>) criteria.list();
	}

	/**
	 * @see DataIntegrityDAO#getIntegrityCheck(Integer)
	 */
	public IntegrityCheck getIntegrityCheck(Integer checkId)
			throws DAOException {
		return (IntegrityCheck) sessionFactory.getCurrentSession().get(
				IntegrityCheck.class, checkId);
	}

	/**
	 * @see DataIntegrityDAO#saveIntegrityCheck(IntegrityCheck)
	 */
	public IntegrityCheck saveIntegrityCheck(IntegrityCheck integrityCheck)
			throws DAOException {
		sessionFactory.getCurrentSession().saveOrUpdate(integrityCheck);
		return integrityCheck;
	}

	/**
	 * @see DataIntegrityDAO#deleteIntegrityCheck(IntegrityCheck)
	 */
	public void deleteIntegrityCheck(IntegrityCheck integrityCheck)
			throws DAOException {
		sessionFactory.getCurrentSession().delete(integrityCheck);

	}

	/**
	 * @see DataIntegrityDAO#repairDataIntegrityCheckViaScript(IntegrityCheck)
	 */
	public void repairDataIntegrityCheckViaScript(IntegrityCheck integrityCheck)
			throws DAOException {
		if (integrityCheck == null)
			return;
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(
				integrityCheck.getRepairDirective());
		query.executeUpdate();
	}

	/**
	 * @see DataIntegrityDAO#saveResults(IntegrityCheckResults)
	 */
	public IntegrityCheckResults saveResults(IntegrityCheckResults results) {
		Criteria crit = sessionFactory
				.getCurrentSession()
				.createCriteria(IntegrityCheckResults.class)
				.add(Restrictions.eq("integrityCheck",
						results.getIntegrityCheck()));

		// update the object that already exists (if it does)
		Object found = crit.uniqueResult();
		if (found != null) {
			IntegrityCheckResults old = (IntegrityCheckResults) found;
			old.setCheckPassed(results.getCheckPassed());
			old.setDateOccurred(results.getDateOccurred());
			old.setFailedRecordCount(results.getFailedRecordCount());
			old.setFailedRecords(results.getFailedRecords());
			old.setDuration(results.getDuration());
			sessionFactory.getCurrentSession().saveOrUpdate(old);
			return old;
		}

		// otherwise, this is a new one ...
		sessionFactory.getCurrentSession().saveOrUpdate(results);
		return results;
	}

	/**
	 * @see DataIntegrityDAO#getResults(Integer)
	 */
	public IntegrityCheckResults getResults(Integer resultsId) {
		return (IntegrityCheckResults) sessionFactory.getCurrentSession().get(
				IntegrityCheckResults.class, resultsId);
	}

	/**
	 * @see DataIntegrityDAO#deleteResults(IntegrityCheckResults)
	 */
	public void deleteResults(IntegrityCheckResults results) {
		sessionFactory.getCurrentSession().delete(results);
	}

	/**
	 * @see DataIntegrityDAO#getResultsForIntegrityCheck(IntegrityCheck)
	 */
	public IntegrityCheckResults getResultsForIntegrityCheck(
			IntegrityCheck integrityCheck) {
		if (integrityCheck == null)
			return null;
		Criteria crit = sessionFactory.getCurrentSession()
				.createCriteria(IntegrityCheckResults.class)
				.add(Restrictions.eq("integrityCheck", integrityCheck));
		Object result = crit.uniqueResult();
		if (result == null)
			return null;
		return (IntegrityCheckResults) result;
	}

}
