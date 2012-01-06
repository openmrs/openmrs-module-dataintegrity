package org.openmrs.module.dataintegrity.executors;

import org.openmrs.api.APIException;
import org.openmrs.module.dataintegrity.DataIntegrityConstants;
import org.openmrs.module.dataintegrity.IntegrityCheck;
import org.openmrs.module.dataintegrity.QueryResults;
import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;
import org.openmrs.util.OpenmrsUtil;

/**
 * this executor simply counts the total rows returned and fails based on the
 * specified operator and directive
 */
public class CountCheckExecutor implements ICheckExecutor {
	private IntegrityCheck check;
	private DataIntegrityDAO dao;
	private QueryResults failedRecords;

	/**
	 * basic constructor to set private DAO
	 * 
	 * @param dao
	 */
	public CountCheckExecutor(DataIntegrityDAO dao) {
		this.dao = dao;
	}

	/**
	 * @see ICheckExecutor#initializeExecutor(IntegrityCheck, String)
	 */
	public void initializeExecutor(IntegrityCheck check) {
		this.check = check;
		this.failedRecords = new QueryResults();
	}

	/**
	 * @see ICheckExecutor#executeCheck()
	 */
	public void executeCheck() throws Exception {
		// get the check code
		String checkCode = this.check.getCheckCode();

		// run the query
		failedRecords = dao.getQueryResults(checkCode);
	}

	/**
	 * get the failed records
	 */
	public QueryResults getFailedRecords() {
		return this.failedRecords;
	}

	/**
	 * get the result of the check
	 */
	public boolean getCheckResult() {
		// find the numeric fail directive
 		int failureThreshold = 0;
		try {
			failureThreshold = Integer.valueOf(this.check.getFailureThreshold());
		} catch (NumberFormatException e) {
			throw new APIException("failure threshold must be an integer: '"
					+ check.getFailureThreshold() + "'", e);
		}

		return compare(failureThreshold, failedRecords.size(), check.getFailureOperator());
	}
	
	public boolean compare(Integer threshold, Integer count, String operator) {
		// execute the check logic
		if (OpenmrsUtil.nullSafeEquals(operator,
				DataIntegrityConstants.FAILURE_OPERATOR_LESS_THAN))
			return !(count < threshold);
		
		if (OpenmrsUtil.nullSafeEquals(operator,
				DataIntegrityConstants.FAILURE_OPERATOR_GREATER_THAN))
			return !(count > threshold);
		
		if (OpenmrsUtil.nullSafeEquals(operator,
				DataIntegrityConstants.FAILURE_OPERATOR_EQUALS))
			return !(count == threshold);
		
		if (OpenmrsUtil.nullSafeEquals(operator,
				DataIntegrityConstants.FAILURE_OPERATOR_NOT_EQUALS))
			return !(count != threshold);
		
		throw new APIException("Specified failure operator is unsupported");
	}

}
