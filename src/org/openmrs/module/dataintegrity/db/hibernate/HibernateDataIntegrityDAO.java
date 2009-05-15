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
import org.openmrs.module.dataintegrity.DataIntegrityTemplate;
import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;

public class HibernateDataIntegrityDAO implements DataIntegrityDAO {

	private SessionFactory sessionFactory;
	
	public List<DataIntegrityTemplate> getAllDataIntegrityTemplates()
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DataIntegrityTemplate.class, "template");
		return (List<DataIntegrityTemplate>)criteria.list();
	}

	public DataIntegrityTemplate getDataIntegrityTemplate(Integer templateId)
			throws DAOException {
//		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DataIntegrityTemplate.class, "template")
//         .add(Expression.eq("template.dataIntegrityId", templateId));
//		List<DataIntegrityTemplate> template = new ArrayList<DataIntegrityTemplate> (criteria.list());
		
		List<DataIntegrityTemplate> template = new ArrayList<DataIntegrityTemplate> (sessionFactory.getCurrentSession().createQuery("from DataIntegrityTemplate").list());
		return template.get(0);
	}

	public void saveDataIntegrityTemplate(DataIntegrityTemplate idcardsTemplate)
			throws DAOException {
		sessionFactory.getCurrentSession().saveOrUpdate(idcardsTemplate);
		
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
