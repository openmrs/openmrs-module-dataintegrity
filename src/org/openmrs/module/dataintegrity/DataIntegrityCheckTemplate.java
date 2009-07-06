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
	private String integrityCheckType;
	private String integrityCheckCode;
	private String integrityCheckResultType;
	private String integrityCheckFailDirective;
	private String integrityCheckFailDirectiveOperator;
	private String integrityCheckRepairType;
	private String integrityCheckRepairDirective;
	private String integrityCheckParameters;
	
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
	public String getIntegrityCheckType() {
		return integrityCheckType;
	}
	public void setIntegrityCheckType(String integrityCheckType) {
		this.integrityCheckType = integrityCheckType;
	}
	public String getIntegrityCheckCode() {
		return integrityCheckCode;
	}
	public void setIntegrityCheckCode(String integrityCheckCode) {
		this.integrityCheckCode = integrityCheckCode;
	}
	public String getIntegrityCheckResultType() {
		return integrityCheckResultType;
	}
	public void setIntegrityCheckResultType(String integrityCheckResultType) {
		this.integrityCheckResultType = integrityCheckResultType;
	}
	public String getIntegrityCheckFailDirective() {
		return integrityCheckFailDirective;
	}
	public void setIntegrityCheckFailDirective(String integrityCheckFailDirective) {
		this.integrityCheckFailDirective = integrityCheckFailDirective;
	}
	public String getIntegrityCheckFailDirectiveOperator() {
		return integrityCheckFailDirectiveOperator;
	}
	public void setIntegrityCheckFailDirectiveOperator(
			String integrityCheckFailDirectiveOperator) {
		this.integrityCheckFailDirectiveOperator = integrityCheckFailDirectiveOperator;
	}
	public String getIntegrityCheckRepairType() {
		return integrityCheckRepairType;
	}
	public void setIntegrityCheckRepairType(String integrityCheckRepairType) {
		this.integrityCheckRepairType = integrityCheckRepairType;
	}
	public String getIntegrityCheckRepairDirective() {
		return integrityCheckRepairDirective;
	}
	public void setIntegrityCheckRepairDirective(
			String integrityCheckRepairDirective) {
		this.integrityCheckRepairDirective = integrityCheckRepairDirective;
	}
	public String getIntegrityCheckParameters() {
		return integrityCheckParameters;
	}
	public void setIntegrityCheckParameters(String integrityCheckParameters) {
		this.integrityCheckParameters = integrityCheckParameters;
	}
}
