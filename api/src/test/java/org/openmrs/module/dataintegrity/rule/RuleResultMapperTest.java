package org.openmrs.module.dataintegrity.rule;

import org.openmrs.module.dataintegrity.db.DataintegrityRule;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.mockito.MockitoAnnotations.initMocks;

public class RuleResultMapperTest {

    RuleResultMapper resultMapper;
    List<RuleResult> inputresult;
    Map<DataintegrityRule, RuleDefn> rulesMap;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        inputresult = new ArrayList<>();
        resultMapper = new RuleResultMapper();
        rulesMap = new HashMap<>();
    }

    @Test
    public void shouldNotThrowExceptionForEmptyRulesList() throws Exception {

        List diResults = null;
        for(Entry<DataintegrityRule, RuleDefn> ruleEntry : rulesMap.entrySet())
            diResults = resultMapper.getDataintegrityResults(ruleEntry, inputresult);

        Assert.isNull(diResults);
    }

    @Test
    public void shouldReturnEmptyListForEmptyRuleMapValue() throws Exception {

        DataintegrityRule rule = new DataintegrityRule();
        rule.setId(Integer.valueOf("1001"));
        rule.setRuleName("TestRule");

        rulesMap.put(rule, null);

        List diResults = null;
        for(Entry<DataintegrityRule, RuleDefn> ruleEntry : rulesMap.entrySet())
            diResults = resultMapper.getDataintegrityResults(ruleEntry, inputresult);

        Assert.notNull(diResults);
        Assert.isTrue(diResults.isEmpty());
    }

}