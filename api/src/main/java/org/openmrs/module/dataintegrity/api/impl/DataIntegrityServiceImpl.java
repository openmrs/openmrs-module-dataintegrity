package org.openmrs.module.dataintegrity.api.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.dataintegrity.api.DataIntegrityService;
import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;
import org.openmrs.module.dataintegrity.DataIntegrityResult;
import org.openmrs.module.dataintegrity.DataIntegrityRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Standard implementation of the DataIntegrityService
 */

public class DataIntegrityServiceImpl extends BaseOpenmrsService implements DataIntegrityService {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private DataIntegrityDAO dataIntegrityDAO;
	
	/**
	 * Sets the DAO
	 *
	 * @param dataIntegrityDAO
	 */
	public void setDataIntegrityDAO(DataIntegrityDAO dataIntegrityDAO) {
		this.dataIntegrityDAO = dataIntegrityDAO;
	}
	
	@Override
	@Transactional(readOnly=false)
	public DataIntegrityRule saveRule(DataIntegrityRule rule) {
		return dataIntegrityDAO.saveRule(rule);
	}
	
	@Override
	@Transactional(readOnly=true)
	public DataIntegrityRule getRule(Integer id) {
		return dataIntegrityDAO.getRule(id);
	}
	
	@Override
	@Transactional(readOnly=true)
	public DataIntegrityRule getRuleByUuid(String uuid) {
		return dataIntegrityDAO.getRuleByUuid(uuid);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<DataIntegrityRule> getAllRules() {
		return dataIntegrityDAO.getRules();
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public List<DataIntegrityResult> getAllResults() {
		return dataIntegrityDAO.getResults();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<DataIntegrityResult> getResultsForRule(DataIntegrityRule dataIntegrityRule) {
		return dataIntegrityDAO.getResultsForRule(dataIntegrityRule);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<DataIntegrityResult> getResultsForRuleByUuid(String uuid) {
		return dataIntegrityDAO.getResultsForRule(dataIntegrityDAO.getRuleByUuid(uuid));
	}
}
