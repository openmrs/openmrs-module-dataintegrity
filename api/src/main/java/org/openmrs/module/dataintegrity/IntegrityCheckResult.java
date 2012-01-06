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
import java.util.Map;
import org.openmrs.BaseOpenmrsObject;
import org.openmrs.User;

/**
 *
 * @author jkeiper
 */
public class IntegrityCheckResult extends BaseOpenmrsObject {

	private Integer integrityCheckResultId;
	private String uniqueIdentifier;
	private QueryResult data;
	private Integer status;
	private IntegrityCheck integrityCheck;
	private IntegrityCheckRun firstSeen;
	private IntegrityCheckRun lastSeen;
	private User creator;
	private Date dateCreated;

	public Integer getId() {
		return getIntegrityCheckResultId();
	}

	public void setId(Integer id) {
		this.setIntegrityCheckResultId(id);
	}

	public QueryResult getData() {
		return data;
	}

	public void setData(QueryResult data) {
		this.data = data;
	}

	public Integer getIntegrityCheckResultId() {
		return integrityCheckResultId;
	}

	public void setIntegrityCheckResultId(Integer integrityCheckResultId) {
		this.integrityCheckResultId = integrityCheckResultId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	public void setUniqueIdentifier(String uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}

	public IntegrityCheck getIntegrityCheck() {
		return integrityCheck;
	}

	public void setIntegrityCheck(IntegrityCheck integrityCheck) {
		this.integrityCheck = integrityCheck;
	}

	public IntegrityCheckRun getFirstSeen() {
		return firstSeen;
	}

	public void setFirstSeen(IntegrityCheckRun firstSeen) {
		this.firstSeen = firstSeen;
	}

	public IntegrityCheckRun getLastSeen() {
		return lastSeen;
	}

	public void setLastSeen(IntegrityCheckRun lastSeen) {
		this.lastSeen = lastSeen;
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
