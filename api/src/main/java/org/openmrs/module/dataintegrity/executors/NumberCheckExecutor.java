package org.openmrs.module.dataintegrity.executors;

import org.openmrs.api.APIException;
import org.openmrs.module.dataintegrity.DataIntegrityConstants;
import org.openmrs.module.dataintegrity.IntegrityCheck;
import org.openmrs.module.dataintegrity.IntegrityCheckUtil;
import org.openmrs.module.dataintegrity.QueryResults;
import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;
import org.openmrs.util.OpenmrsUtil;

/**
 * this executor examines the double value of each column and will add the row
 * to the list of failures according to the specified operator and directive
 */
public class NumberCheckExecutor implements ICheckExecutor {
	private IntegrityCheck check;
	private DataIntegrityDAO dao;
	private String parameterValues;
	private QueryResults failedRecords;
	
	/**
	 * basic constructor to set private DAO
	 * 
	 * @param dao
	 */
	public NumberCheckExecutor(DataIntegrityDAO dao) {
		this.dao = dao;
	}
	
	/**
	 * @see ICheckExecutor#initializeExecutor(IntegrityCheck, String)
	 */
	public void initializeExecutor(IntegrityCheck check, String parameterValues) {
		this.check = check;
		this.parameterValues = parameterValues;
		this.failedRecords = new QueryResults();
	}

	/**
	 * return the failed records
	 */
	public QueryResults getFailedRecords() {
		return this.failedRecords;
	}
	
	/**
	 * return the check result
	 */
	public boolean getCheckResult() {
		boolean checkPassed = (failedRecords.size() > 0) ? false : true;
		return checkPassed;
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
		QueryResults resultList = dao.getQueryResults(checkCode);

		// quit early if there are no results
		if (resultList.size() <= 0)
			throw new Exception("Query returned no results");

		// get the column count
		int columnCount = resultList.getColumnCount();
		
		// find the numeric fail directive
		Double failDirective = null;
		try {
			failDirective = Double.valueOf(this.check.getFailDirective());
		} catch (NumberFormatException e) {
			throw new APIException("fail directive must be a double: '"
					+ check.getFailDirective() + "'", e);
		}

		// execute the check logic
		if (OpenmrsUtil.nullSafeEquals(
				check.getFailDirectiveOperator(),
				DataIntegrityConstants.FAILURE_OPERATOR_LESS_THAN)) {
			for (int i=0; i<resultList.size(); i++) {
				if (columnCount > 1) {
					Double[] doubleArray = new Double[columnCount];
					for (int j=0; j<columnCount; j++) {
						Double value = (Double) resultList.get(i)[j];
						if (value < failDirective) {
							for (int k=0; k<columnCount; k++) {
								doubleArray[k] = (Double) resultList.get(i)[k];
							}
							failedRecords.add(doubleArray);
							break;
						}
					}
				} else {
					Object value = resultList.get(i);
					Double doubleValue = (Double)value;
					if (doubleValue < failDirective) {
						Double[] doubleArray = new Double[1];
						doubleArray[0] = doubleValue;
						failedRecords.add(doubleArray);
					}
				}
			}
		} else if (OpenmrsUtil.nullSafeEquals(
				check.getFailDirectiveOperator(),
				DataIntegrityConstants.FAILURE_OPERATOR_GREATER_THAN)) {
			for (int i=0; i<resultList.size(); i++) {
				if (columnCount > 1) {
					Double[] doubleArray = new Double[columnCount];
					for (int j=0; j<columnCount; j++) {
						Double value = (Double) resultList.get(i)[j];
						if (value > failDirective) {
							for (int k=0; k<columnCount; k++) {
								doubleArray[k] = (Double) resultList.get(i)[k];
							}
							failedRecords.add(doubleArray);
							break;
						}
					}
				} else {
					Object value = resultList.get(i);
					Double doubleValue = (Double)value;
					if (doubleValue > failDirective) {
						Double[] doubleArray = new Double[1];
						doubleArray[0] = doubleValue;
						failedRecords.add(doubleArray);
					}
				}
			}
		} else if (OpenmrsUtil.nullSafeEquals(
				check.getFailDirectiveOperator(),
				DataIntegrityConstants.FAILURE_OPERATOR_EQUALS)) {
			for (int i=0; i<resultList.size(); i++) {
				if (columnCount > 1) {
					Double[] doubleArray = new Double[columnCount];
					for (int j=0; j<columnCount; j++) {
						Double value = (Double) resultList.get(i)[j];
						if (value.equals(failDirective)) {
							for (int k=0; k<columnCount; k++) {
								doubleArray[k] = (Double) resultList.get(i)[k];
							}
							failedRecords.add(doubleArray);
							break;
						}
					}
				} else {
					Object value = resultList.get(i);
					Double doubleValue = (Double)value;
					if (value.equals(failDirective)) {
						Double[] doubleArray = new Double[1];
						doubleArray[0] = doubleValue;
						failedRecords.add(doubleArray);
					}
				}
			}
		} else if (OpenmrsUtil.nullSafeEquals(
				check.getFailDirectiveOperator(),
				DataIntegrityConstants.FAILURE_OPERATOR_NOT_EQUALS)) {
			for (int i=0; i<resultList.size(); i++) {
				if (columnCount > 1) {
					Double[] doubleArray = new Double[columnCount];
					for (int j=0; j<columnCount; j++) {
						Double value = (Double) resultList.get(i)[j];
						if (!value.equals(failDirective)) {
							for (int k=0; k<columnCount; k++) {
								doubleArray[k] = (Double) resultList.get(i)[k];
							}
							failedRecords.add(doubleArray);
							break;
						}
					}
				} else {
					Object value = resultList.get(i);
					Double doubleValue = (Double)value;
					if (!value.equals(failDirective)) {
						Double[] doubleArray = new Double[1];
						doubleArray[0] = doubleValue;
						failedRecords.add(doubleArray);
					}
				}
			}
		} else {
			throw new Exception("Specified failure operator is unsupported");
		}
	}
}
