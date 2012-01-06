/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.dataintegrity;

import org.openmrs.BaseOpenmrsObject;

/**
 *
 * @author jkeiper
 */
public class IntegrityCheckColumn extends BaseOpenmrsObject {
	private Integer columnId;
	private Boolean showInResults;
	private Boolean usedInUid;
	private String name;
	private String displayName;
	private IntegrityCheck integrityCheck;

	public Integer getId() {
		return this.getColumnId();
	}

	public void setId(Integer id) {
		this.setColumnId(id);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Integer getColumnId() {
		return columnId;
	}

	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getShowInResults() {
		return showInResults;
	}

	public void setShowInResults(Boolean show) {
		this.showInResults = show;
	}

	public Boolean getUsedInUid() {
		return usedInUid;
	}

	public void setUsedInUid(Boolean usedInUid) {
		this.usedInUid = usedInUid;
	}

	public IntegrityCheck getIntegrityCheck() {
		return integrityCheck;
	}

	public void setIntegrityCheck(IntegrityCheck integrityCheck) {
		this.integrityCheck = integrityCheck;
	}

}
