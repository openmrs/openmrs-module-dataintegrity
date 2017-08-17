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

package org.openmrs.module.dataintegrity.api.impl;

import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.PatientProgram;
import org.openmrs.api.db.PatientDAO;
import org.openmrs.api.db.hibernate.HibernatePatientDAO;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.dataintegrity.DataIntegrityResult;
import org.openmrs.module.dataintegrity.DataIntegrityRule;
import org.openmrs.module.dataintegrity.api.DataIntegrityService;
import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * Standard implementation of the DataIntegrityService
 */

public class DataIntegrityServiceImpl extends BaseOpenmrsService implements DataIntegrityService {
	
	protected final Log log = LogFactory.getLog(getClass());

	private DataIntegrityDAO dataIntegrityDAO;
	private PatientDAO patientDAO;
	
	/**
	 * Sets the data integrity DAO
	 *
	 * @param dataIntegrityDAO
	 */
	public void setDataIntegrityDAO(DataIntegrityDAO dataIntegrityDAO) {
		this.dataIntegrityDAO = dataIntegrityDAO;
	}

	/**
	 * Sets the patient DAO
	 *
	 * @param patientDAO
	 */
	public void setPatientDAO(PatientDAO patientDAO) {
		this.patientDAO = patientDAO;
	}


	@Override
	@Transactional(readOnly = false)
	public DataIntegrityRule saveRule(DataIntegrityRule rule) {
		return dataIntegrityDAO.saveRule(rule);
	}

	@Override
	public void deleteRule(DataIntegrityRule dataIntegrityRule) {
		dataIntegrityDAO.deleteRule(dataIntegrityRule);
	}

	@Override
	@Transactional(readOnly = true)
	public DataIntegrityRule getRule(Integer id) {
		return dataIntegrityDAO.getRule(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public DataIntegrityRule getRuleByUuid(String uuid) {
		return dataIntegrityDAO.getRuleByUuid(uuid);
	}

	@Override
	public List<DataIntegrityRule> getRuleByCategory(String category) {
		return dataIntegrityDAO.getRuleByCategory(category);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DataIntegrityRule> getAllRules() {
		return dataIntegrityDAO.getRules();
	}

	@Override
	@Transactional(readOnly = true)
	public DataIntegrityResult getResultByUuid(String uuid) {
		return dataIntegrityDAO.getResultByUuid(uuid);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DataIntegrityResult> getAllResults() {
		return dataIntegrityDAO.getResults();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<DataIntegrityResult> getResultsForRule(DataIntegrityRule dataIntegrityRule) {
		return dataIntegrityDAO.getResultsForRule(dataIntegrityRule);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<DataIntegrityResult> getResultsForRuleByUuid(String uuid) {
		return dataIntegrityDAO.getResultsForRule(dataIntegrityDAO.getRuleByUuid(uuid));
	}

	@Override
	@Transactional(readOnly = true)
	public List<DataIntegrityResult> getResultsByPatientUuid(String patientUuid) {
		return dataIntegrityDAO.getResultsByPatient(patientDAO.getPatientByUuid(patientUuid));
	}

	@Override
	@Transactional(readOnly = true)
	public List<DataIntegrityResult> getResultsByPatientProgram(int patientProgramId) {
		PatientProgram patientProgram = new PatientProgram(patientProgramId);
		return dataIntegrityDAO.getResultsByPatientProgram(patientProgram);
	}
}
