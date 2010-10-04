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

/**
 * a data integrity check template
 * 
 * @TODO describe this better
 */
public class DataIntegrityCheckTemplate {
	private int id;
	private String name;
	private String checkType;
	private String checkCode;
	private String resultType;
	private String failDirective;
	private String failDirectiveOperator;
	private String repairType;
	private String repairDirective;
	private String repairParameters;
	private DataIntegrityCheckResultTemplate latestResults;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the checkType
	 */
	public String getCheckType() {
		return checkType;
	}

	/**
	 * @param checkType
	 *            the checkType to set
	 */
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	/**
	 * @return the checkCode
	 */
	public String getCheckCode() {
		return checkCode;
	}

	/**
	 * @param checkCode
	 *            the checkCode to set
	 */
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	/**
	 * @return the resultType
	 */
	public String getResultType() {
		return resultType;
	}

	/**
	 * @param resultType
	 *            the resultType to set
	 */
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	/**
	 * @return the failDirective
	 */
	public String getFailDirective() {
		return failDirective;
	}

	/**
	 * @param failDirective
	 *            the failDirective to set
	 */
	public void setFailDirective(String failDirective) {
		this.failDirective = failDirective;
	}

	/**
	 * @return the failDirectiveOperator
	 */
	public String getFailDirectiveOperator() {
		return failDirectiveOperator;
	}

	/**
	 * @param failDirectiveOperator
	 *            the failDirectiveOperator to set
	 */
	public void setFailDirectiveOperator(String failDirectiveOperator) {
		this.failDirectiveOperator = failDirectiveOperator;
	}

	/**
	 * @return the repairType
	 */
	public String getRepairType() {
		return repairType;
	}

	/**
	 * @param repairType
	 *            the repairType to set
	 */
	public void setRepairType(String repairType) {
		this.repairType = repairType;
	}

	/**
	 * @return the repairDirective
	 */
	public String getRepairDirective() {
		return repairDirective;
	}

	/**
	 * @param repairDirective
	 *            the repairDirective to set
	 */
	public void setRepairDirective(String repairDirective) {
		this.repairDirective = repairDirective;
	}

	/**
	 * @return the repairParameters
	 */
	public String getRepairParameters() {
		return repairParameters;
	}

	/**
	 * @param repairParameters
	 *            the repairParameters to set
	 */
	public void setRepairParameters(String repairParameters) {
		this.repairParameters = repairParameters;
	}

	/**
	 * @return the latestResults
	 */
	public DataIntegrityCheckResultTemplate getLatestResults() {
		return latestResults;
	}

	/**
	 * @param latestResults
	 *            the latestResults to set
	 */
	public void setLatestResults(DataIntegrityCheckResultTemplate latestResults) {
		if (latestResults != null)
			latestResults.setIntegrityCheck(this);
		this.latestResults = latestResults;
	}

	// DEPRECATED getters and setters

	/**
	 * @deprecated see {@link #getId()}
	 */
	@Deprecated
	public int getIntegrityCheckId() {
		return this.getId();
	}

	/**
	 * @deprecated see {@link #setId(int)}
	 */
	@Deprecated
	public void setIntegrityCheckId(int id) {
		this.setId(id);
	}

	/**
	 * @deprecated see {@link #getName()}
	 */
	@Deprecated
	public String getIntegrityCheckName() {
		return this.getName();
	}

	/**
	 * @deprecated see {@link #setName(String)}
	 */
	@Deprecated
	public void setIntegrityCheckName(String name) {
		this.setName(name);
	}

	/**
	 * @deprecated see {@link #getCheckType()}
	 */
	@Deprecated
	public String getIntegrityCheckType() {
		return this.getCheckType();
	}

	/**
	 * @deprecated see {@link #setCheckType(String)}
	 */
	@Deprecated
	public void setIntegrityCheckType(String checkType) {
		this.setCheckType(checkType);
	}

	/**
	 * @deprecated see {@link #getCheckCode()}
	 */
	@Deprecated
	public String getIntegrityCheckCode() {
		return this.getCheckCode();
	}

	/**
	 * @deprecated see {@link #setCheckCode(String)}
	 */
	@Deprecated
	public void setIntegrityCheckCode(String checkCode) {
		this.setCheckCode(checkCode);
	}

	/**
	 * @deprecated see {@link #getResultType()}
	 */
	@Deprecated
	public String getIntegrityCheckResultType() {
		return this.getResultType();
	}

	/**
	 * @deprecated see {@link #setResultType(String)}
	 */
	@Deprecated
	public void setIntegrityCheckResultType(String resultType) {
		this.setResultType(resultType);
	}

	/**
	 * @deprecated see {@link #getFailDirective()}
	 */
	@Deprecated
	public String getIntegrityCheckFailDirective() {
		return this.getFailDirective();
	}

	/**
	 * @deprecated see {@link #setFailDirective(String)}
	 */
	@Deprecated
	public void setIntegrityCheckFailDirective(String failDirective) {
		this.setFailDirective(failDirective);
	}

	/**
	 * @deprecated see {@link #getFailDirectiveOperator()}
	 */
	@Deprecated
	public String getIntegrityCheckFailDirectiveOperator() {
		return this.getFailDirectiveOperator();
	}

	/**
	 * @deprecated see {@link #setFailDirectiveOperator(String)}
	 */
	@Deprecated
	public void setIntegrityCheckFailDirectiveOperator(
			String failDirectiveOperator) {
		this.setFailDirectiveOperator(failDirectiveOperator);
	}

	/**
	 * @deprecated see {@link #getRepairType()}
	 */
	@Deprecated
	public String getIntegrityCheckRepairType() {
		return this.getRepairType();
	}

	/**
	 * @deprecated see {@link #setRepairType(String)}
	 */
	@Deprecated
	public void setIntegrityCheckRepairType(String repairType) {
		this.setRepairType(repairType);
	}

	/**
	 * @deprecated see {@link #getRepairDirective()}
	 */
	@Deprecated
	public String getIntegrityCheckRepairDirective() {
		return this.getRepairDirective();
	}

	/**
	 * @deprecated see {@link #setRepairDirective(String)}
	 */
	@Deprecated
	public void setIntegrityCheckRepairDirective(String repairDirective) {
		this.setRepairDirective(repairDirective);
	}

	/**
	 * @deprecated see {@link #getRepairParameters()}
	 */
	@Deprecated
	public String getIntegrityCheckParameters() {
		return this.getRepairParameters();
	}

	/**
	 * @deprecated see {@link #setRepairParameters(String)}
	 */
	@Deprecated
	public void setIntegrityCheckParameters(String repairParameters) {
		this.setRepairParameters(repairParameters);
	}

}
