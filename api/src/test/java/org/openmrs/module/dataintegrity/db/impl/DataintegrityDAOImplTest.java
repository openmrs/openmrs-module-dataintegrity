package org.openmrs.module.dataintegrity.db.impl;

import org.openmrs.module.dataintegrity.db.DataintegrityResult;
import org.openmrs.module.dataintegrity.db.DataintegrityRule;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class DataintegrityDAOImplTest{

    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private org.hibernate.classic.Session session;
    @Mock
    private Transaction transaction;
    @Mock
    private SQLQuery sqlQuery;
    @Mock
    private Criteria criteria;
    @InjectMocks
    DataintegrityDAOImpl dataintegrityDAO;


    public DataintegrityDAOImplTest() {
        initMocks(this);
    }

    @Test
    public void shouldGetRules() throws Exception {
        when(sessionFactory.getCurrentSession()).thenReturn(this.session);
        when(session.createCriteria(DataintegrityRule.class)).thenReturn(this.criteria);

        this.dataintegrityDAO.getRules();
        verify(criteria).list();
    }

    @Test
    public void shouldClearAllResultsByExecutingQuery() throws Exception {

        when(this.sessionFactory.openSession()).thenReturn(this.session);
        when(this.session.beginTransaction()).thenReturn(this.transaction);
        when(this.session.createSQLQuery("delete from dataintegrity_result where result_id > 0"))
                .thenReturn(this.sqlQuery);

        this.dataintegrityDAO.clearAllResults();
        verify(sqlQuery).executeUpdate();
        verify(transaction).commit();
        verify(session).flush();
        verify(session).close();
    }

    @Test
    public void shouldSaveResults() throws Exception {

        List<DataintegrityResult> results = new ArrayList<>();
        DataintegrityResult result = new DataintegrityResult();

        results.add(result);

        when(this.sessionFactory.openSession()).thenReturn(this.session);
        when(this.session.beginTransaction()).thenReturn(this.transaction);

        this.dataintegrityDAO.saveResults(results);
        verify(session).save(result);
        verify(transaction).commit();
        verify(session).flush();
        verify(session).close();
    }
}