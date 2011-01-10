package org.openmrs.module.dataintegrity.executors;

import org.openmrs.api.APIException;
import org.openmrs.module.dataintegrity.DataIntegrityConstants;
import org.openmrs.module.dataintegrity.IntegrityCheck;
import org.openmrs.module.dataintegrity.IntegrityCheckUtil;
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
	private String parameterValues;
	private QueryResults failedRecords;
	private boolean checkPassed;

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
	public void initializeExecutor(IntegrityCheck check, String parameterValues) {
		this.check = check;
		this.parameterValues = parameterValues;
		this.failedRecords = new QueryResults();
		this.checkPassed = true;
	}

	/**
	 * @see ICheckExecutor#executeCheck()
	 */
	public void executeCheck() throws Exception {
		// get the check code
		String checkCode = IntegrityCheckUtil.getModifiedCheckCode(
				this.check.getCheckCode(), this.check.getRepairParameters(),
				this.parameterValues);

		// run the query
		failedRecords = dao.getQueryResults(checkCode);

		// find the numeric fail directive
		int failDirective = 0;
		try {
			failDirective = Integer.valueOf(this.check.getFailDirective());
		} catch (NumberFormatException e) {
			throw new APIException("fail directive must be an integer: '"
					+ check.getFailDirective() + "'", e);
		}

		// execute the check logic
		if (OpenmrsUtil.nullSafeEquals(check.getFailDirectiveOperator(),
				DataIntegrityConstants.FAILURE_OPERATOR_LESS_THAN)) {
			if (failedRecords.size() < failDirective) {
				checkPassed = false;
			}
		} else if (OpenmrsUtil.nullSafeEquals(check.getFailDirectiveOperator(),
				DataIntegrityConstants.FAILURE_OPERATOR_GREATER_THAN)) {
			if (failedRecords.size() > failDirective) {
				checkPassed = false;
			}
		} else if (OpenmrsUtil.nullSafeEquals(check.getFailDirectiveOperator(),
				DataIntegrityConstants.FAILURE_OPERATOR_EQUALS)) {
			if (failedRecords.size() == failDirective) {
				checkPassed = false;
			}
		} else if (OpenmrsUtil.nullSafeEquals(check.getFailDirectiveOperator(),
				DataIntegrityConstants.FAILURE_OPERATOR_NOT_EQUALS)) {
			if (failedRecords.size() != failDirective) {
				checkPassed = false;
			}
		} else {
			throw new APIException("Specified failure operator is unsupported");
		}
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
		return checkPassed;
	}
}
