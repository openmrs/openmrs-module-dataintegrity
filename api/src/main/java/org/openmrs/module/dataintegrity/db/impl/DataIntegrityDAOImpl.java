package org.openmrs.module.dataintegrity.db.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.dataintegrity.DataIntegrityResult;
import org.openmrs.module.dataintegrity.DataIntegrityRule;
import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;

public class DataIntegrityDAOImpl implements DataIntegrityDAO {
    
    protected final Log log = LogFactory.getLog(getClass());
    
    private DbSessionFactory sessionFactory;

    @Override
    public List<DataIntegrityRule> getRules() {
        return getSession().createCriteria(DataIntegrityRule.class).list();
    }
    
    @Override
    public List<DataIntegrityResult> getResults() {
        return getSession().createCriteria(DataIntegrityResult.class).list();
    }
    
    @Override
    public List<DataIntegrityResult> getResultsForRule(DataIntegrityRule dataIntegrityRule) {
        Criteria criteria = getSession().createCriteria(DataIntegrityResult.class);
        // filter by the rule
        criteria.add(Restrictions.eq("rule", dataIntegrityRule));
        return criteria.list();
    }
	
	@Override
	public DataIntegrityRule getRule(Integer id) {
		return (DataIntegrityRule) getSession().get(DataIntegrityRule.class, id);
	}
	
	@Override
	public DataIntegrityRule getRuleByUuid(String uuid) {
		Query q = getSession().createQuery("from DataIntegrityRule f where f.uuid = :uuid");
		return (DataIntegrityRule) q.setString("uuid", uuid).uniqueResult();
	}
	
	@Override
    public void saveResults(List<DataIntegrityResult> results) {
		DbSession session = getSession();
		for(DataIntegrityResult result: results) {
			session.save(result);
		}
    }
    
    @Override
    public DataIntegrityRule saveRule(DataIntegrityRule rule) {
	    DataIntegrityRule existing = getRuleByUuid(rule.getUuid());
	    if (existing != null) {
		    log.debug("DataIntergrityRule with uuid " + rule.getUuid() + " exists with ruleId " + existing.getId());
		    rule.setId(existing.getId());
		    Context.evictFromSession(existing);
	    }
	    getSession().saveOrUpdate(rule);
	    return rule;
    }
    
    @Override
    public void clearAllResults(){
	    log.debug("Clearing the results for all the rules");
	    getSession().createSQLQuery("DELETE from dataintegrity_result").executeUpdate();
    }
    
    @Override
    public void clearResultsForRule(DataIntegrityRule rule) {
        log.debug("Clearing results for rule '" + rule.getRuleName() + "' with uuid " + rule.getUuid());
	    getSession().createSQLQuery("delete from dataintegrity_result where rule_id = " + rule.getId()).executeUpdate();
                
    }
    
    public void setSessionFactory(DbSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public DbSessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public DbSession getSession() {return sessionFactory.getCurrentSession();}
}
