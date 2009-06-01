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

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.dataintegrity.DataIntegrityCheckTemplate;
import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;

public class HibernateDataIntegrityDAO implements DataIntegrityDAO {

	private SessionFactory sessionFactory;
	
	public List<DataIntegrityCheckTemplate> getAllDataIntegrityCheckTemplates()
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DataIntegrityCheckTemplate.class, "template");
		return (List<DataIntegrityCheckTemplate>)criteria.list();
	}

	public DataIntegrityCheckTemplate getDataIntegrityCheckTemplate(Integer templateId)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DataIntegrityCheckTemplate.class, "template")
         .add(Expression.eq("template.integrityCheckId", templateId));
		List<DataIntegrityCheckTemplate> template = new ArrayList<DataIntegrityCheckTemplate> (criteria.list());
		return template.get(0);
	}

	public void saveDataIntegrityCheckTemplate(DataIntegrityCheckTemplate dataIntegrityTemplate)
			throws DAOException {
		sessionFactory.getCurrentSession().saveOrUpdate(dataIntegrityTemplate);
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void deleteDataIntegrityCheckTemplate(Integer templateId)
			throws DAOException {
		sessionFactory.getCurrentSession().delete(templateId);
		
	}

}
