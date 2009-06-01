package org.openmrs.module.dataintegrity;

import java.util.Vector;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;


public class HibernateTest {
	@Test
	public void sayHello() {
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Vector<DataIntegrityCheckTemplate> template = new Vector<DataIntegrityCheckTemplate>(session.createQuery("from DataIntegrityTemplate template where template.dataIntegrityId = 1").list());
		System.out.println(template.get(0).getIntegrityCheckName());
		session.getTransaction().commit();
		sessionFactory.close();
	}
}
