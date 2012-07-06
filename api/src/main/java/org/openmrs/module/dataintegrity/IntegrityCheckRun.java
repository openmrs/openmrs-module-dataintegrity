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
import org.openmrs.BaseOpenmrsObject;
import org.openmrs.User;

/**
 *
 * @author jkeiper
 */
public class IntegrityCheckRun extends BaseOpenmrsObject {

	private Integer integrityCheckRunId;
	private Boolean checkPassed;
	private Long duration;
	private Integer totalCount = 0;
	private Integer newCount = 0;
	private Integer voidedCount = 0;
	private Integer ignoredCount = 0;
	private IntegrityCheck integrityCheck;
	private User creator;
	private Date dateCreated;

	public Integer getId() {
		return getIntegrityCheckRunId();
	}

	public void setId(Integer id) {
		this.setIntegrityCheckRunId(id);
	}

	public Boolean getCheckPassed() {
		return checkPassed;
	}

	public void setCheckPassed(Boolean checkPassed) {
		this.checkPassed = checkPassed;
	}

	public Integer getIgnoredCount() {
		return ignoredCount;
	}

	public void setIgnoredCount(Integer ignoredCount) {
		this.ignoredCount = ignoredCount;
	}

	public Integer getNewCount() {
		return newCount;
	}

	public void setNewCount(Integer newCount) {
		this.newCount = newCount;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getVoidedCount() {
		return voidedCount;
	}

	public void setVoidedCount(Integer voidedCount) {
		this.voidedCount = voidedCount;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public IntegrityCheck getIntegrityCheck() {
		return integrityCheck;
	}

	public void setIntegrityCheck(IntegrityCheck integrityCheck) {
		this.integrityCheck = integrityCheck;
	}

	public Integer getIntegrityCheckRunId() {
		return integrityCheckRunId;
	}

	public void setIntegrityCheckRunId(Integer integrityCheckRunId) {
		this.integrityCheckRunId = integrityCheckRunId;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
}
