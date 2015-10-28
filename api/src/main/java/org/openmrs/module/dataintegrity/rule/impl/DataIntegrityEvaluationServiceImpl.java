package org.openmrs.module.dataintegrity.rule.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;
import org.openmrs.module.dataintegrity.DataIntegrityResult;
import org.openmrs.module.dataintegrity.DataIntegrityRule;
import org.openmrs.module.dataintegrity.rule.DataIntegrityEvaluationService;
import org.openmrs.module.dataintegrity.rule.RuleDefinition;
import org.openmrs.module.dataintegrity.rule.RuleDefinitionLoader;
import org.openmrs.module.dataintegrity.rule.RuleResult;
import org.openmrs.module.dataintegrity.rule.RuleResultMapper;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class DataIntegrityEvaluationServiceImpl implements DataIntegrityEvaluationService {
    private Log log = LogFactory.getLog(DataIntegrityEvaluationServiceImpl.class);

    private DataIntegrityDAO dataIntegrityDao;

    private RuleDefinitionLoader ruleDefinitionLoader;

	private RuleResultMapper ruleResultMapper;

    public DataIntegrityEvaluationServiceImpl(DataIntegrityDAO dataIntegrityDao,
                                              RuleDefinitionLoader ruleDefinitionLoader, RuleResultMapper ruleResultMapper){
        this.dataIntegrityDao = dataIntegrityDao;
        this.ruleDefinitionLoader = ruleDefinitionLoader;
	    this.ruleResultMapper = ruleResultMapper;
    }

    @Override
    @Transactional(readOnly=false)
    public void fireRules(){
        List<DataIntegrityResult> results = new ArrayList<>();

        dataIntegrityDao.clearAllResults();

        Map<DataIntegrityRule, RuleDefinition> rulesWithDefinitions = loadRuleDefinitions();

        for (Entry<DataIntegrityRule, RuleDefinition> ruleWithDefn : rulesWithDefinitions.entrySet()) {
            try {

                List<RuleResult> ruleResults = ruleWithDefn.getValue().evaluate();
                results.addAll(ruleResultMapper.getDataIntegrityResults(ruleWithDefn, ruleResults));

            }catch (Exception e){
                log.error(MessageFormat.format(
                        "ERROR executing DataIntegrity Rule : {0} with following Exception - {1}{2}",
                            ruleWithDefn.getKey().getHandlerClassname(), e.toString(), getStackTrace(e)));
            }
        }
        dataIntegrityDao.saveResults(results);
    }
    
    @Override
    @Transactional(readOnly=false)
    public void fireRule(String uuid) {
        List<DataIntegrityResult> results = new ArrayList<>();
        
        // clear the results for the rule
        DataIntegrityRule rule = dataIntegrityDao.getRuleByUuid(uuid);
        log.debug("Clearing results for rule '" + rule.getRuleName() + "' with uuid " + uuid);
        dataIntegrityDao.clearResultsForRule(rule);
        
        List<DataIntegrityRule> ruleList = new ArrayList<DataIntegrityRule>();
        ruleList.add(rule);
    
        Map<DataIntegrityRule, RuleDefinition> rulesWithDefinitions = ruleDefinitionLoader.getRuleDefinitions(ruleList);
    
        for (Entry<DataIntegrityRule, RuleDefinition> ruleWithDefinition : rulesWithDefinitions.entrySet()) {
            try {
            
                List<RuleResult> ruleResults = ruleWithDefinition.getValue().evaluate();
                results.addAll(ruleResultMapper.getDataIntegrityResults(ruleWithDefinition, ruleResults));
            
            }catch (Exception e){
                log.error(MessageFormat.format(
                        "ERROR executing DataIntegrity Rule : {0} with following Exception - {1}{2}",
                        ruleWithDefinition.getKey().getHandlerClassname(), e.toString(), getStackTrace(e)));
            }
        }
        dataIntegrityDao.saveResults(results);
    }
    
    private Map<DataIntegrityRule, RuleDefinition> loadRuleDefinitions() {
        List<DataIntegrityRule> diRules = dataIntegrityDao.getRules();
        return ruleDefinitionLoader.getRuleDefinitions(diRules);
    }
}
