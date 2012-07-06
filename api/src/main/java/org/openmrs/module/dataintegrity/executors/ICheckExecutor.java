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
