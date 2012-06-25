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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.util.StringUtils;

/**
 * a data integrity check template
 * 
 * @TODO describe this better
 */
public class IntegrityCheck extends BaseOpenmrsMetadata {

	private Integer integrityCheckId;
	private String checkLanguage;
	private String checkCode;
	private String failureType;
	private String failureThreshold;
	private String failureOperator;
	private String totalLanguage;
	private String totalCode;
	private String resultsLanguage;
	private String resultsCode;
	private String resultsUniqueIdentifier;
	private Set<IntegrityCheckColumn> resultsColumns;
	private Set<IntegrityCheckRun> integrityCheckRuns;
	private Set<IntegrityCheckResult> integrityCheckResults;

	private final Log log = LogFactory.getLog(this.getClass());

	@Override
	public Integer getId() {
		return this.getIntegrityCheckId();
	}

	@Override
	public void setId(Integer id) {
		this.setIntegrityCheckId(id);
	}

	public Integer getIntegrityCheckId() {
		return this.integrityCheckId;
	}

	public void setIntegrityCheckId(Integer id) {
		this.integrityCheckId = id;
	}

	public String getCheckLanguage() {
		return checkLanguage;
	}

	public void setCheckLanguage(String checkLanguage) {
		this.checkLanguage = checkLanguage;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public String getTotalCode() {
		return totalCode;
	}

	public void setTotalCode(String totalCode) {
		this.totalCode = totalCode;
	}

	public String getTotalLanguage() {
		return totalLanguage;
	}

	public void setTotalLanguage(String totalLanguage) {
		this.totalLanguage = totalLanguage;
	}

	public String getFailureType() {
		return failureType;
	}

	public void setFailureType(String failureType) {
		this.failureType = failureType;
	}

	public String getFailureThreshold() {
		return failureThreshold;
	}

	public void setFailureThreshold(String failureThreshold) {
		this.failureThreshold = failureThreshold;
	}

	public String getFailureOperator() {
		return failureOperator;
	}

	public void setFailureOperator(String failureOperator) {
		this.failureOperator = failureOperator;
	}

	public String getResultsCode() {
		return resultsCode;
	}

	public void setResultsCode(String resultsCode) {
		this.resultsCode = resultsCode;
	}

	public String getResultsLanguage() {
		return resultsLanguage;
	}

	public void setResultsLanguage(String resultsLanguage) {
		this.resultsLanguage = resultsLanguage;
	}

	public String getResultsUniqueIdentifier() {
		return resultsUniqueIdentifier;
	}

	public void setResultsUniqueIdentifier(String resultsUniqueIdentifier) {
		this.resultsUniqueIdentifier = resultsUniqueIdentifier;
	}

	public void addResultsColumn(IntegrityCheckColumn col) {
		col.setIntegrityCheck(this);
		this.getResultsColumns().add(col);
	}

	public Set<IntegrityCheckColumn> getResultsColumns() {
		if (resultsColumns == null) {
			resultsColumns = new LinkedHashSet<IntegrityCheckColumn>();
		}
		return resultsColumns;
	}

	public void setResultsColumns(Set<IntegrityCheckColumn> resultsColumns) {
		this.resultsColumns = resultsColumns;
	}

	public Set<IntegrityCheckRun> getIntegrityCheckRuns() {
		if (integrityCheckRuns == null) {
			integrityCheckRuns = new LinkedHashSet<IntegrityCheckRun>();
		}
		return integrityCheckRuns;
	}

	public void setIntegrityCheckRuns(Set<IntegrityCheckRun> integrityCheckRuns) {
		this.integrityCheckRuns = integrityCheckRuns;
	}

	public void addRun(IntegrityCheckRun run) {
		run.setIntegrityCheck(this);
		this.getIntegrityCheckRuns().add(run);
	}

	public Set<IntegrityCheckResult> getIntegrityCheckResults() {
		if (integrityCheckResults == null) {
			integrityCheckResults = new LinkedHashSet<IntegrityCheckResult>();
		}
		return integrityCheckResults;
	}

	public void setIntegrityCheckResults(Set<IntegrityCheckResult> integrityCheckResults) {
		this.integrityCheckResults = integrityCheckResults;
	}

	/**
	 * add or replace a result based on whether another with the same id exists
	 * 
	 * @param result the result to be added or replaced
	 */
	public void addOrReplaceResult(IntegrityCheckResult result) {
		if (result == null) {
			return;
		}

		boolean found = false;
		IntegrityCheckResult toBeDeleted = null;

		if (result.getIntegrityCheckResultId() != null) {
			Iterator<IntegrityCheckResult> iter = this.getIntegrityCheckResults().iterator();
			while (!found && iter.hasNext()) {
				toBeDeleted = iter.next();
				if (OpenmrsUtil.nullSafeEquals(toBeDeleted.getIntegrityCheckResultId(), result.getIntegrityCheckResultId())) {
					found = true;
				}
			}
			if (found) {
				if (DataIntegrityConstants.RESULT_STATUS_IGNORED.equals(toBeDeleted.getStatus()))
					result.setStatus(DataIntegrityConstants.RESULT_STATUS_IGNORED);
				this.getIntegrityCheckResults().remove(toBeDeleted);
			}
		}

		this.getIntegrityCheckResults().add(result);
	}
	
	/**
	 * update columns, add new ones and remove columns that are no longer 
	 * referenced. assumes that "name" of a column must be unique.
	 * 
	 * @param columns new list of columns to be used
	 */
	public void updateColumns(List<IntegrityCheckColumn> columns) {
		// build a map of existing columns
		Map<String, IntegrityCheckColumn> colMap = new HashMap<String, IntegrityCheckColumn>();
		for (IntegrityCheckColumn column : this.getResultsColumns())
			colMap.put(column.getName(), column);

		// loop through new columns
		for (IntegrityCheckColumn newColumn : columns) {
			// use the uuid as an indicator of newness
			String newName = newColumn.getName();
			if (StringUtils.hasText(newName)) {
				// the column must be referencing something that already exists
				IntegrityCheckColumn existing = colMap.get(newName);
				if (existing != null) {
					// update the existing column
					existing.updateWith(newColumn);
					// remove it from the map
					colMap.remove(newName);
				} else {
					// this uuid was not found, so it must be new
					this.addResultsColumn(newColumn);
				}
			} else {
				// a blank uuid means the column is new
				this.addResultsColumn(newColumn);
			}
		}
		
		// clean up stragglers
		this.getResultsColumns().removeAll(colMap.values());
	}
	
	/**
	 * provides an exact copy of this integrity check, minus runs and results.
	 * 
	 * @param includeUuids
	 * @return 
	 */
	public IntegrityCheck clone(Boolean includeUuids){
		IntegrityCheck check = new IntegrityCheck();
		check.setUuid(includeUuids ? this.getUuid() : null);
		check.setName(this.getName());
		check.setDescription(this.getDescription());
		check.setCheckLanguage(this.getCheckLanguage());
		check.setCheckCode(this.getCheckCode());
		check.setFailureType(this.getFailureType());
		check.setFailureOperator(this.getFailureOperator());
		check.setFailureThreshold(this.getFailureThreshold());
		check.setResultsLanguage(this.getResultsLanguage());
		check.setResultsCode(this.getResultsCode());
		check.setResultsUniqueIdentifier(this.getResultsUniqueIdentifier());
		check.setTotalLanguage(this.getTotalLanguage());
		check.setTotalCode(this.getTotalCode());

		for (IntegrityCheckColumn column: this.getResultsColumns())
			check.addResultsColumn(column.clone(includeUuids));
		
		return check;
	}

	/**
	 * helper method for Metadata Sharing Module.
	 */
	public Object writeReplace(){
		return this.clone(true);
	}
}
