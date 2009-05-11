package org.openmrs.module.dataintegrity.db.hibernate;

import java.util.List;

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
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DataIntegrityTemplate.class, "template")
         .add(Expression.eq("template.dataIntegrityId", templateId));
		List<DataIntegrityTemplate> template = criteria.list();
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
