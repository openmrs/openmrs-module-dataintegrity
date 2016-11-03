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

package org.openmrs.module.dataintegrity;

import java.io.Serializable;
import java.util.Date;

import org.openmrs.BaseOpenmrsObject;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;

public class DataIntegrityResult extends BaseOpenmrsObject implements Serializable {
	
	private int resultId;
	
	private String actionUrl;
	
	private String notes;
	
	private Patient patient;
	
	private PatientProgram patientProgram;
	
	private DataIntegrityRule rule;
	
	private Date dateCreated;
	
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public DataIntegrityRule getRule() {
		return rule;
	}
	
	public void setRule(DataIntegrityRule rule) {
		this.rule = rule;
	}
	
	public PatientProgram getPatientProgram() {
		return patientProgram;
	}
	
	public void setPatientProgram(PatientProgram patientProgram) {
		this.patientProgram = patientProgram;
	}
	
	public Patient getPatient() {
		return patient;
	}
	
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	public int getResultId() {
		return resultId;
	}
	
	public void setResultId(int resultId) {
		this.resultId = resultId;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@Override
	public Integer getId() {
		return resultId;
	}
	
	@Override
	public void setId(Integer id) {
		resultId = id;
	}
	
	public String getActionUrl() {
		return actionUrl;
	}
	
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	
	/**
	 * The name of the entity for the result
	 *
	 * @return
	 */
	public String getName() {
		if (getPatientProgram() != null) {
			return getPatientProgram().getProgram().getName();
		}
		if (getPatient() != null) {
			return getPatient().getFamilyName() + ", " + getPatient().getMiddleName() + " , " + getPatient().getGivenName();
		}
		return "";
	}
}
