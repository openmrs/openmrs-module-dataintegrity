package org.openmrs.module.dataintegrity;

import java.util.List;

public interface ICheckExecutor {
	public void initializeExecutor(DataIntegrityCheckTemplate check, String parameterValues);
	public void executeCheck() throws Exception;
	public <T> List<T[]> getFailedRecords();
}
