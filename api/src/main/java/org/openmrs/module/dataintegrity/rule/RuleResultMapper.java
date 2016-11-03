/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 *
 */

package org.openmrs.module.dataintegrity.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.module.dataintegrity.DataIntegrityException;
import org.openmrs.module.dataintegrity.DataIntegrityResult;
import org.openmrs.module.dataintegrity.DataIntegrityRule;
import org.springframework.stereotype.Component;

@Component
public class RuleResultMapper {
	
	public List<DataIntegrityResult> getDataIntegrityResults(Entry<DataIntegrityRule, RuleDefinition> ruleWithDefn,
	                                                         List<RuleResult> ruleResults) {
		List<DataIntegrityResult> results = new ArrayList<>();
		for (RuleResult result : ruleResults) {
			DataIntegrityResult diResult = new DataIntegrityResult();
			diResult.setRule(ruleWithDefn.getKey());
			diResult.setNotes(result.getNotes());
			diResult.setActionUrl(result.getActionUrl());
			setEntity(result, diResult);
			
			results.add(diResult);
		}
		return results;
	}
	
	private void setEntity(RuleResult result, DataIntegrityResult diResult) {
		if (result.getEntity() instanceof PatientProgram) {
			diResult.setPatientProgram((PatientProgram) result.getEntity());
		} else if (result.getEntity() instanceof Patient) {
			diResult.setPatient((Patient) result.getEntity());
		} else {
			throw new DataIntegrityException("The entity [" + result.getEntity().toString() + "] is not supported yet!!");
		}
	}
}
