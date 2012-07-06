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
import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.dataintegrity.IntegrityCheck;
import org.openmrs.module.dataintegrity.IntegrityCheckResult;
import org.openmrs.module.dataintegrity.IntegrityCheckResults;
import org.openmrs.module.dataintegrity.IntegrityCheckRun;
import org.openmrs.module.dataintegrity.QueryResults;
import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class HibernateDataIntegrityDAO implements DataIntegrityDAO {

	/**
	 * the session factory to use in this DAO
	 */
	private SessionFactory sessionFactory;

	/**
	 * @see DataIntegrityDAO#setSessionFactory(SessionFactory)
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * @see DataIntegrityDAO#getSessionFactory()
	 */
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	/**
	 * @see DataIntegrityDAO#getAllIntegrityChecks()
	 */
	@SuppressWarnings("unchecked")
	public List<IntegrityCheck> getAllIntegrityChecks() throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(IntegrityCheck.class);
		// criteria.add(Restrictions.eq("voided", false));
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

	/**
	 * @see DataIntegrityDAO#getQueryResults(String)
	 */
	public QueryResults getQueryResults(String sql) throws DAOException {
        return getQueryResults(sql, null);
	}

    /**
	 * @see DataIntegrityDAO#getQueryResults(String, Integer)
	 */
	public QueryResults getQueryResults(String sql, Integer maxRows) throws DAOException {
        DataSource ds = new SingleConnectionDataSource(sessionFactory.openStatelessSession().connection(), true);
        JdbcTemplate jdbc = new JdbcTemplate(ds);
        if (maxRows != null)
            jdbc.setMaxRows(maxRows);
        QueryResults results = (QueryResults) jdbc.query(sql, new QueryResultsExtractor());

        return results;
	}

	public IntegrityCheckResult findResultForIntegrityCheckByUid(IntegrityCheck integrityCheck, String uid) {
		if (integrityCheck == null || uid == null)
			return null;
		Criteria crit = sessionFactory.getCurrentSession()
				.createCriteria(IntegrityCheckResult.class)
				.add(Restrictions.eq("integrityCheck", integrityCheck))
				.add(Restrictions.eq("uniqueIdentifier", uid));
		return (IntegrityCheckResult) crit.uniqueResult();
	}

	public IntegrityCheckRun getMostRecentRunForCheck(IntegrityCheck check) {
		Criteria crit = sessionFactory.getCurrentSession()
				.createCriteria(IntegrityCheckRun.class)
				.add(Restrictions.eq("integrityCheck", check))
				.addOrder(Order.desc("dateCreated"))
				.setMaxResults(1);
		return (IntegrityCheckRun) crit.uniqueResult();
	}
}
