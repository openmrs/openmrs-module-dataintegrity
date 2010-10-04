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
import org.openmrs.module.dataintegrity.DataIntegrityCheckResultTemplate;
import org.openmrs.module.dataintegrity.DataIntegrityCheckTemplate;
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
	 * @see DataIntegrityDAO#getAllDataIntegrityCheckTemplates()
	 */
	@SuppressWarnings("unchecked")
	public List<DataIntegrityCheckTemplate> getAllDataIntegrityCheckTemplates()
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				DataIntegrityCheckTemplate.class, "template");
		return (List<DataIntegrityCheckTemplate>) criteria.list();
	}

	/**
	 * @see DataIntegrityDAO#getDataIntegrityCheckTemplate(Integer)
	 */
	public DataIntegrityCheckTemplate getDataIntegrityCheckTemplate(
			Integer templateId) throws DAOException {
		return (DataIntegrityCheckTemplate) sessionFactory.getCurrentSession()
				.get(DataIntegrityCheckTemplate.class, templateId);
	}

	/**
	 * @see DataIntegrityDAO#saveDataIntegrityCheckTemplate(DataIntegrityCheckTemplate)
	 */
	public void saveDataIntegrityCheckTemplate(
			DataIntegrityCheckTemplate dataIntegrityTemplate)
			throws DAOException {
		sessionFactory.getCurrentSession().saveOrUpdate(dataIntegrityTemplate);
	}

	/**
	 * @see DataIntegrityDAO#deleteDataIntegrityCheckTemplate(DataIntegrityCheckTemplate)
	 */
	public void deleteDataIntegrityCheckTemplate(
			DataIntegrityCheckTemplate template) throws DAOException {
		sessionFactory.getCurrentSession().delete(template);

	}

	/**
	 * @see DataIntegrityDAO#repairDataIntegrityCheckViaScript(DataIntegrityCheckTemplate)
	 */
	public void repairDataIntegrityCheckViaScript(
			DataIntegrityCheckTemplate template) throws DAOException {
		if (template == null)
			return;
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(
				template.getRepairDirective());
		query.executeUpdate();
	}

	/**
	 * @see DataIntegrityDAO#saveResults(DataIntegrityCheckResultTemplate)
	 */
	public DataIntegrityCheckResultTemplate saveResults(
			DataIntegrityCheckResultTemplate results) {
		Criteria crit = sessionFactory
				.getCurrentSession()
				.createCriteria(DataIntegrityCheckResultTemplate.class)
				.add(Restrictions.eq("integrityCheck",
						results.getIntegrityCheck()));
		
		// update the object that already exists (if it does)
		Object found = crit.uniqueResult();
		if (found != null) {
			DataIntegrityCheckResultTemplate old = (DataIntegrityCheckResultTemplate) found;
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
	public DataIntegrityCheckResultTemplate getResults(Integer resultsId) {
		return (DataIntegrityCheckResultTemplate) sessionFactory
				.getCurrentSession().get(
						DataIntegrityCheckResultTemplate.class, resultsId);
	}

	/**
	 * @see DataIntegrityDAO#deleteResults(DataIntegrityCheckResultTemplate)
	 */
	public void deleteResults(DataIntegrityCheckResultTemplate results) {
		sessionFactory.getCurrentSession().delete(results);
	}

	/**
	 * @see DataIntegrityDAO#getResultsForCheck(DataIntegrityCheckTemplate)
	 */
	public DataIntegrityCheckResultTemplate getResultsForCheck(
			DataIntegrityCheckTemplate check) {
		if (check == null)
			return null;
		Criteria crit = sessionFactory
			.getCurrentSession()
			.createCriteria(DataIntegrityCheckResultTemplate.class)
			.add(Restrictions.eq("integrityCheck", check));
		Object result = crit.uniqueResult();
		if (result == null)
			return null;
		return (DataIntegrityCheckResultTemplate) result;
	}
	
}
