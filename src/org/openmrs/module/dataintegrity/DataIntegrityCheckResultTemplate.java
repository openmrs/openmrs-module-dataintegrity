package org.openmrs.module.dataintegrity;

import java.util.List;

public class DataIntegrityCheckResultTemplate {
	private int failedRecordCount;
	private String checkName;
	private boolean checkPassed;
	private List<Object[]> failedRecords;
	
	public int getFailedRecordCount() {
		return failedRecordCount;
	}
	public void setFailedRecordCount(int failedRecordCount) {
		this.failedRecordCount = failedRecordCount;
	}
	public String getCheckName() {
		return checkName;
	}
	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}
	public boolean isCheckPassed() {
		return checkPassed;
	}
	public void setCheckPassed(boolean checkPassed) {
		this.checkPassed = checkPassed;
	}
	public List<Object[]> getFailedRecords() {
		return failedRecords;
	}
	public void setFailedRecords(List<Object[]> failedRecords) {
		this.failedRecords = failedRecords;
	}

}
