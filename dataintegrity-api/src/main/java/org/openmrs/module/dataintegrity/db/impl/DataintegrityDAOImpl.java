package org.openmrs.module.dataintegrity.db.impl;

import org.openmrs.module.dataintegrity.db.DataintegrityDAO;
import org.openmrs.module.dataintegrity.db.DataintegrityResult;
import org.openmrs.module.dataintegrity.db.DataintegrityRule;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import java.util.List;

public class DataintegrityDAOImpl implements DataintegrityDAO {

    private SessionFactory sessionFactory;

    @Override
    public List<DataintegrityRule> getRules() {
        return sessionFactory.getCurrentSession().createCriteria(DataintegrityRule.class).list();
    }

    @Override
    public void saveResults(List<DataintegrityResult> results) {
        for(DataintegrityResult result: results) saveResult(result);
    }

    @Override
    public void clearAllResults(){
        final Session session = sessionFactory.openSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {
                session.createSQLQuery("delete from dataintegrity_result where result_id > 0").executeUpdate();
                transaction.commit();
                session.flush();
            } catch (Exception ex) {
                transaction.rollback();
                throw ex;
            }

        } finally {
            session.close();
        }
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private void saveResult(DataintegrityResult result) {
        final Session session = sessionFactory.openSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {
                session.save(result);
                transaction.commit();
                session.flush();
            } catch (Exception ex) {
                transaction.rollback();
                throw ex;
            }

        } finally {
            session.close();
        }
    }
}
