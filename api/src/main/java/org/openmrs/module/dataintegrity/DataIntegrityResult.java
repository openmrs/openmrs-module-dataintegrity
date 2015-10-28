package org.openmrs.module.dataintegrity;

import org.openmrs.BaseOpenmrsObject;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;

import java.io.Serializable;
import java.util.Date;

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
    public void setId(Integer integer) {
        resultId = integer;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }
    
    /**
     * The name of the entity for the result
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
