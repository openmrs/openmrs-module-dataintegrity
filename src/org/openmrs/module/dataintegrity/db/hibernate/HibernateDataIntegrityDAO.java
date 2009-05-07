package org.openmrs.module.dataintegrity.db.hibernate;

import java.util.Vector;

import org.hibernate.SessionFactory;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.dataintegrity.DataIntegrityTemplate;
import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;

public class HibernateDataIntegrityDAO implements DataIntegrityDAO {
	
	private SessionFactory sessionFactory;

	public DataIntegrityTemplate getDataIntegrityTemplate(Integer templateId)
			throws DAOException {
		Vector<DataIntegrityTemplate> colors = new Vector<DataIntegrityTemplate>(sessionFactory.getCurrentSession().createQuery("from Colour colour where colour.id = 1").list());
	}

	public void saveDataIntegrityTemplate(
			DataIntegrityTemplate dataIntegrityTemplate) throws DAOException {
		// TODO Auto-generated method stub
		
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		
	}

}
