package org.openmrs.module.dataintegrity.rule.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.dataintegrity.db.DataintegrityDAO;
import org.openmrs.module.dataintegrity.db.DataintegrityResult;
import org.openmrs.module.dataintegrity.db.DataintegrityRule;
import org.openmrs.module.dataintegrity.rule.DataintegrityEvaluationService;
import org.openmrs.module.dataintegrity.rule.RuleDefn;
import org.openmrs.module.dataintegrity.rule.RuleDefnLoader;
import org.openmrs.module.dataintegrity.rule.RuleResult;
import org.openmrs.module.dataintegrity.rule.RuleResultMapper;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class DataintegrityEvaluationServiceImpl implements DataintegrityEvaluationService {
    private static Log log = LogFactory.getLog(DataintegrityEvaluationServiceImpl.class);

    private DataintegrityDAO dataintegrityDao;

    private RuleDefnLoader ruleDefnLoader;

	private RuleResultMapper ruleResultMapper;

    public DataintegrityEvaluationServiceImpl(DataintegrityDAO dataintegrityDao,
                                              RuleDefnLoader ruleDefnLoader, RuleResultMapper ruleResultMapper){
        this.dataintegrityDao = dataintegrityDao;
        this.ruleDefnLoader = ruleDefnLoader;
	    this.ruleResultMapper = ruleResultMapper;
    }

    @Override
    public void fireRules(){
        List<DataintegrityResult> results = new ArrayList<>();

        dataintegrityDao.clearAllResults();

        Map<DataintegrityRule, RuleDefn> rulesWithDefns = loadRuleDefns();

        for (Entry<DataintegrityRule, RuleDefn> ruleWithDefn : rulesWithDefns.entrySet()) {
            try {

                List<RuleResult> ruleResults = ruleWithDefn.getValue().evaluate();
                results.addAll(ruleResultMapper.getDataintegrityResults(ruleWithDefn, ruleResults));

            }catch (Exception e){
                log.error(MessageFormat.format(
                        "ERROR executing DataIntegrity Rule : {0} with follwoing Exception - {1}{2}",
                            ruleWithDefn.getKey().getHandlerClassname(), e.toString(), getStackTrace(e)));
            }
        }
        dataintegrityDao.saveResults(results);
    }
    private Map<DataintegrityRule, RuleDefn> loadRuleDefns() {
        List<DataintegrityRule> diRules = dataintegrityDao.getRules();
        return ruleDefnLoader.getRuleDefns(diRules);
    }
}
