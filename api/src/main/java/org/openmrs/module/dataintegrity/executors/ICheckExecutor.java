package org.openmrs.module.dataintegrity.executors;

import org.openmrs.module.dataintegrity.IntegrityCheck;
import org.openmrs.module.dataintegrity.QueryResults;

public interface ICheckExecutor {
	/**
	 * 
	 * Method to initialize the check executing class.
	 * 
	 * @param check: The Integrity Check to be executed
	 */
	public void initializeExecutor(IntegrityCheck check);
	
	/**
	 * 
	 * Executes the check according to the result type
	 * 
	 * @throws Exception
	 */
	public void executeCheck() throws Exception;
	
	/**
	 * 
	 * Returns the records that fail the check.
	 * 
	 * @param <T>: The type of the returned failed records List
	 * @return: The list of failed records
	 */
	public QueryResults getFailedRecords();
	
	/**
	 * 
	 * Returns the result of the check
	 * 
	 * @return whether the check passed or not
	 */
	public boolean getCheckResult();
	
	public boolean compare(Integer threshold, Integer count, String operator);
}
