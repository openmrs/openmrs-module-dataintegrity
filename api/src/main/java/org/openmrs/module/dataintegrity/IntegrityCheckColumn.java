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
	private String datatype;
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

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
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

	/**
	 * updates this column with new column data; ignores id, uuid and integritycheck
	 * 
	 * @param newcol the new column to get data from
	 */
	void updateWith(IntegrityCheckColumn newcol) {
		this.setDatatype(newcol.getDatatype());
		this.setDisplayName(newcol.getDisplayName());
		this.setName(newcol.getName());
		this.setShowInResults(newcol.getShowInResults());
		this.setUsedInUid(newcol.getUsedInUid());
	}

}
