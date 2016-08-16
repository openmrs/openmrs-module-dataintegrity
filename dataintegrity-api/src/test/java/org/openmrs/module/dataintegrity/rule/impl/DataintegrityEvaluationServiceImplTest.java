package org.openmrs.module.dataintegrity.rule.impl;

import org.openmrs.module.dataintegrity.db.DataintegrityDAO;
import org.openmrs.module.dataintegrity.db.DataintegrityResult;
import org.openmrs.module.dataintegrity.db.DataintegrityRule;
import org.openmrs.module.dataintegrity.rule.RuleDefn;
import org.openmrs.module.dataintegrity.rule.RuleDefnLoader;
import org.openmrs.module.dataintegrity.rule.RuleResult;
import org.openmrs.module.dataintegrity.rule.RuleResultMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openmrs.PatientProgram;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class DataintegrityEvaluationServiceImplTest {
    @Mock
    private DataintegrityDAO dataintegrityDAO;
    @Mock
    private RuleDefnLoader ruleDefnLoader;
    @Mock
    private RuleResultMapper ruleResultMapper;
    @InjectMocks
    private DataintegrityEvaluationServiceImpl evaluationService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }


    @Test
    public void shouldFireRulesForMultipleResults() throws Exception {
        List<DataintegrityRule> rules = new ArrayList<>();
        List<DataintegrityResult> finalResults = new ArrayList<>();
        finalResults.add(new DataintegrityResult());
        Map<DataintegrityRule, RuleDefn> rulesWithDefn = new HashMap<>();

        DataintegrityRule rule = new DataintegrityRule();

        RuleDefn defn = new RuleDefn() {
            @Override
            public List<RuleResult> evaluate() {
                return new ArrayList<>();
            }
        };
        rulesWithDefn.put(new DataintegrityRule(), defn);
        rulesWithDefn.put(new DataintegrityRule(), defn);
        Mockito.when(dataintegrityDAO.getRules()).thenReturn(rules);

        Mockito.when(ruleDefnLoader
                .getRuleDefns(any(List.class)))
                .thenReturn(rulesWithDefn);

        Mockito.when(ruleResultMapper
                .getDataintegrityResults(any(Map.Entry.class), any(List.class)))
                .thenReturn(finalResults);

        evaluationService.fireRules();

        verify(dataintegrityDAO).clearAllResults();

        ArgumentCaptor<List> argument
                = ArgumentCaptor.forClass(List.class);
        verify(dataintegrityDAO).saveResults(argument.capture());

        assertEquals(2, argument.getValue().size());
    }

    @Test
    public void shouldFireRules() throws Exception {
        List<DataintegrityRule> rules = new ArrayList<>();
        List<DataintegrityResult> finalResults = new ArrayList<>();

        Map<DataintegrityRule, RuleDefn> rulesWithDefn = new HashMap<>();

        DataintegrityRule rule = new DataintegrityRule();
        rule.setId(Integer.valueOf("001"));
        rule.setRuleName("FirstRule");
        rule.setRuleCategory("DI_Rule");
        rule.setHandlerClassname("test.MyJavaClass");
        rule.setHandlerConfig("java");
        rules.add(rule);

        List<RuleResult> results = new ArrayList<>();
        RuleResult ruleResult = new RuleResult();
        ruleResult.setNotes("TestRuleNotes");
        ruleResult.setActionUrl("ActionURL");
        PatientProgram pp = new PatientProgram();
        pp.setId(Integer.valueOf("123"));
        ruleResult.setEntity(pp);
        results.add(ruleResult);

        RuleDefn defn = new RuleDefn() {
            @Override
            public List<RuleResult> evaluate() {
                List<RuleResult> results = new ArrayList<>();
                RuleResult ruleResult = new RuleResult();
                ruleResult.setNotes("TestRuleNotes");
                ruleResult.setActionUrl("ActionURL");
                PatientProgram pp = new PatientProgram();
                pp.setId(Integer.valueOf("123"));
                ruleResult.setEntity(pp);
                results.add(ruleResult);
                return results;
            }
        };
        rulesWithDefn.put(rule, defn);
        Map.Entry<DataintegrityRule, RuleDefn> ruleAndDefn = rulesWithDefn.entrySet().iterator().next();

        evaluationService.fireRules();

        Mockito.when(dataintegrityDAO.getRules()).thenReturn(rules);
        Mockito.when(ruleDefnLoader.getRuleDefns(rules)).thenReturn(rulesWithDefn);
        Mockito.when(ruleResultMapper.getDataintegrityResults(ruleAndDefn, results)).thenReturn(finalResults);

        verify(dataintegrityDAO).clearAllResults();
        verify(dataintegrityDAO).saveResults(finalResults);

    }

}