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

public class DataIntegrityCheckTemplate {
	private int integrityCheckId;
	private String integrityCheckName;
	private String integrityCheckSql;
	private int integrityCheckBaseForFailure;
	private double integrityCheckScore;
	
	public int getIntegrityCheckId() {
		return integrityCheckId;
	}
	public void setIntegrityCheckId(int integrityCheckId) {
		this.integrityCheckId = integrityCheckId;
	}
	public String getIntegrityCheckName() {
		return integrityCheckName;
	}
	public void setIntegrityCheckName(String integrityCheckName) {
		this.integrityCheckName = integrityCheckName;
	}
	public String getIntegrityCheckSql() {
		return integrityCheckSql;
	}
	public void setIntegrityCheckSql(String integrityCheckSql) {
		this.integrityCheckSql = integrityCheckSql;
	}
	public int getIntegrityCheckBaseForFailure() {
		return integrityCheckBaseForFailure;
	}
	public void setIntegrityCheckBaseForFailure(int integrityCheckBaseForFailure) {
		this.integrityCheckBaseForFailure = integrityCheckBaseForFailure;
	}
	public double getIntegrityCheckScore() {
		return integrityCheckScore;
	}
	public void setIntegrityCheckScore(double integrityScore) {
		this.integrityCheckScore = integrityScore;
	}
}
