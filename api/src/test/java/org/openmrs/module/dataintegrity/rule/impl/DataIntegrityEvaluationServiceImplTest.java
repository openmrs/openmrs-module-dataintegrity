package org.openmrs.module.dataintegrity.rule.impl;

import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;
import org.openmrs.module.dataintegrity.DataIntegrityResult;
import org.openmrs.module.dataintegrity.DataIntegrityRule;
import org.openmrs.module.dataintegrity.rule.RuleDefinition;
import org.openmrs.module.dataintegrity.rule.RuleDefinitionLoader;
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

public class DataIntegrityEvaluationServiceImplTest {
    @Mock
    private DataIntegrityDAO dataIntegrityDAO;
    @Mock
    private RuleDefinitionLoader ruleDefinitionLoader;
    @Mock
    private RuleResultMapper ruleResultMapper;
    @InjectMocks
    private DataIntegrityEvaluationServiceImpl evaluationService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }


    @Test
    public void shouldFireRulesForMultipleResults() throws Exception {
        List<DataIntegrityRule> rules = new ArrayList<>();
        List<DataIntegrityResult> finalResults = new ArrayList<>();
        finalResults.add(new DataIntegrityResult());
        Map<DataIntegrityRule, RuleDefinition> rulesWithDefn = new HashMap<>();

        DataIntegrityRule rule = new DataIntegrityRule();

        RuleDefinition defn = new RuleDefinition() {
            @Override
            public List<RuleResult> evaluate() {
                return new ArrayList<>();
            }
            @Override
            public DataIntegrityRule getRule(){return null;}
        };
        rulesWithDefn.put(new DataIntegrityRule(), defn);
        rulesWithDefn.put(new DataIntegrityRule(), defn);
        Mockito.when(dataIntegrityDAO.getRules()).thenReturn(rules);

        Mockito.when(ruleDefinitionLoader
                .getRuleDefinitions(any(List.class)))
                .thenReturn(rulesWithDefn);

        Mockito.when(ruleResultMapper
                .getDataIntegrityResults(any(Map.Entry.class), any(List.class)))
                .thenReturn(finalResults);

        evaluationService.fireRules();

        verify(dataIntegrityDAO).clearAllResults();

        ArgumentCaptor<List> argument
                = ArgumentCaptor.forClass(List.class);
        verify(dataIntegrityDAO).saveResults(argument.capture());

        assertEquals(2, argument.getValue().size());
    }

    @Test
    public void shouldFireRules() throws Exception {
        List<DataIntegrityRule> rules = new ArrayList<>();
        List<DataIntegrityResult> finalResults = new ArrayList<>();

        Map<DataIntegrityRule, RuleDefinition> rulesWithDefn = new HashMap<>();

        DataIntegrityRule rule = new DataIntegrityRule();
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

        RuleDefinition defn = new RuleDefinition() {
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
            @Override
            public DataIntegrityRule getRule() {return null;}
        };
        rulesWithDefn.put(rule, defn);
        Map.Entry<DataIntegrityRule, RuleDefinition> ruleAndDefn = rulesWithDefn.entrySet().iterator().next();

        evaluationService.fireRules();

        Mockito.when(dataIntegrityDAO.getRules()).thenReturn(rules);
        Mockito.when(ruleDefinitionLoader.getRuleDefinitions(rules)).thenReturn(rulesWithDefn);
        Mockito.when(ruleResultMapper.getDataIntegrityResults(ruleAndDefn, results)).thenReturn(finalResults);

        verify(dataIntegrityDAO).clearAllResults();
        verify(dataIntegrityDAO).saveResults(finalResults);

    }

}