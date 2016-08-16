package org.openmrs.module.dataintegrity.rule;

import org.openmrs.module.dataintegrity.DataIntegrityException;
import org.openmrs.module.dataintegrity.db.DataintegrityResult;
import org.openmrs.module.dataintegrity.db.DataintegrityRule;
import org.openmrs.PatientProgram;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

@Component
public class RuleResultMapper {

    public List<DataintegrityResult> getDataintegrityResults(Entry<DataintegrityRule, RuleDefn> ruleWithDefn,
                                                             List<RuleResult> ruleResults) {
        List<DataintegrityResult> results = new ArrayList<>();
        for (RuleResult result : ruleResults) {
            DataintegrityResult diResult = new DataintegrityResult();

            if(result.getEntity() instanceof PatientProgram){
                diResult.setPatientProgram((PatientProgram) result.getEntity());
            } else {
                throw new DataIntegrityException("The entity ["+result.getEntity().toString()+"] is not supported yet!!");
            }

            diResult.setRule(ruleWithDefn.getKey());
            diResult.setNotes(result.getNotes());
            diResult.setActionUrl(result.getActionUrl());

            results.add(diResult);
        }
        return results;
    }
}
