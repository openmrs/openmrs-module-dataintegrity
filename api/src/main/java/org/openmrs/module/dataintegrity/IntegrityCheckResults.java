/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.dataintegrity;

import java.util.Date;

/**
 * @TODO put description here :-/
 */
public class IntegrityCheckResults {
	private Integer id = null;
	private IntegrityCheck integrityCheck = null;
	private Integer failedRecordCount = null;
	private Boolean checkPassed = null;
	private QueryResults failedRecords = null;
	private Date dateOccurred = null;
	private Long duration = null;

	/**
	 * default constructor
	 */
	public IntegrityCheckResults() {
		// pass
	}

	/**
	 * convenience constructor to connect results with the associated integrity
	 * check template
	 */
	public IntegrityCheckResults(IntegrityCheck check) {
		integrityCheck = check;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the integrityCheck
	 */
	public IntegrityCheck getIntegrityCheck() {
		return integrityCheck;
	}

	/**
	 * @param integrityCheck
	 *            the integrityCheck to set
	 */
	public void setIntegrityCheck(IntegrityCheck integrityCheck) {
		this.integrityCheck = integrityCheck;
	}

	/**
	 * @return
	 */
	public Integer getFailedRecordCount() {
		return failedRecordCount;
	}

	/**
	 * @param failedRecordCount
	 */
	public void setFailedRecordCount(Integer failedRecordCount) {
		this.failedRecordCount = failedRecordCount;
	}

	/**
	 * @return
	 */
	public Boolean isCheckPassed() {
		return checkPassed;
	}

	/**
	 * needed for hibernate
	 * 
	 * @return
	 */
	public Boolean getCheckPassed() {
		return checkPassed;
	}

	/**
	 * @param checkPassed
	 */
	public void setCheckPassed(Boolean checkPassed) {
		this.checkPassed = checkPassed;
	}

	/**
	 * @return
	 */
	public QueryResults getFailedRecords() {
		return failedRecords;
	}

	/**
	 * @param failedRecords
	 */
	public void setFailedRecords(QueryResults failedRecords) {
		this.failedRecords = failedRecords;
	}

	/**
	 * @return the dateOccurred
	 */
	public Date getDateOccurred() {
		return dateOccurred;
	}

	/**
	 * @param dateOccurred
	 *            the dateOccurred to set
	 */
	public void setDateOccurred(Date dateOccurred) {
		this.dateOccurred = dateOccurred;
	}

	/**
	 * @return the duration
	 */
	public Long getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(Long duration) {
		this.duration = duration;
	}

	/**
	 * @deprecated see {@link IntegrityCheck#getName()}
	 */
	public String getCheckName() {
		if (this.getIntegrityCheck() != null)
			return this.getIntegrityCheck().getName();
		return null;
	}

}
