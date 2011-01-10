package org.openmrs.module.dataintegrity.executors;

import org.openmrs.module.dataintegrity.DataIntegrityConstants;
import org.openmrs.module.dataintegrity.IntegrityCheck;
import org.openmrs.module.dataintegrity.IntegrityCheckUtil;
import org.openmrs.module.dataintegrity.QueryResults;
import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;
import org.openmrs.util.OpenmrsUtil;

/**
 * this executor examines the string value of each column and will add the row
 * to the list of failures according to the specified operator and directive
 */
public class StringCheckExecutor implements ICheckExecutor {
	private IntegrityCheck check;
	private DataIntegrityDAO dao;
	private String parameterValues;
	private QueryResults failedRecords;
	
	/**
	 * basic constructor to set private DAO
	 * 
	 * @param dao
	 */
	public StringCheckExecutor(DataIntegrityDAO dao) {
		this.dao = dao;
	}

	/**
	 * @see ICheckExecutor#initializeExecutor(IntegrityCheck, String)
	 */
	public void initializeExecutor(IntegrityCheck check,
			String parameterValues) {
		this.check = check;
		this.parameterValues = parameterValues;
		this.failedRecords = new QueryResults();
	}
	
	/**
	 * @see ICheckExecutor#executeCheck()
	 */
	public void executeCheck() throws Exception {
		// get the check code
		String checkCode = IntegrityCheckUtil.getModifiedCheckCode(
				this.check.getCheckCode(),
				this.check.getRepairParameters(), this.parameterValues);

		// run the query
		QueryResults resultList = dao.getQueryResults(checkCode);

		// quit early if there are no results
		if (resultList.size() <= 0)
			throw new Exception("Query returned no results");

		// get the column count
		int columnCount = resultList.getColumnCount();

		// find the fail directive
		String failDirective = this.check.getFailDirective();

		// execute the check logic
		if (OpenmrsUtil.nullSafeEquals(
				check.getFailDirectiveOperator(),
				DataIntegrityConstants.FAILURE_OPERATOR_CONTAINS)) {
			for (int i=0; i<resultList.size(); i++) {
				if (columnCount > 1) {
					String[] stringArray = new String[columnCount];
					for (int j=0; j<columnCount; j++) {
						Object valueObject = resultList.get(i)[j];
						String valueString = valueObject == null ? "" : valueObject.toString();
						if (valueString.contains(failDirective)) {
							for (int k=0; k<columnCount; k++) {
								stringArray[k] = resultList.get(i)[k].toString();
							}
							failedRecords.add(stringArray);
							break;
						}
					}
				} else {
					Object valueObject = resultList.get(i);
					String valueString = valueObject.toString();
					if (valueString.contains(failDirective)) {
						String[] stringArray = new String[1];
						stringArray[0] = valueString;
						failedRecords.add(stringArray);
					}
				}
			}
		} else if (OpenmrsUtil.nullSafeEquals(
				check.getFailDirectiveOperator(),
				DataIntegrityConstants.FAILURE_OPERATOR_NOT_CONTAINS)) {
			for (int i=0; i<resultList.size(); i++) {
				if (columnCount > 1) {
					String[] stringArray = new String[columnCount];
					for (int j=0; j<columnCount; j++) {
						Object valueObject = resultList.get(i)[j];
						String valueString = valueObject.toString();
						if (!valueString.contains(failDirective)) {
							for (int k=0; k<columnCount; k++) {
								stringArray[k] = resultList.get(i)[k].toString();
							}
							failedRecords.add(stringArray);
							break;
						}
					}
				} else {
					Object valueObject = resultList.get(i);
					String valueString = valueObject.toString();
					if (!valueString.contains(failDirective)) {
						String[] stringArray = new String[1];
						stringArray[0] = valueString;
						failedRecords.add(stringArray);
					}
				}
			}
		} else if (OpenmrsUtil.nullSafeEquals(
				check.getFailDirectiveOperator(),
				DataIntegrityConstants.FAILURE_OPERATOR_EQUALS)) {
			for (int i=0; i<resultList.size(); i++) {
				if (columnCount > 1) {
					String[] stringArray = new String[columnCount];
					for (int j=0; j<columnCount; j++) {
						Object valueObject = resultList.get(i)[j];
						String valueString = valueObject.toString();
						if (valueString.equals(failDirective)) {
							for (int k=0; k<columnCount; k++) {
								stringArray[k] = resultList.get(i)[k].toString();
							}
							failedRecords.add(stringArray);
							break;
						}
					}
				} else {
					Object valueObject = resultList.get(i);
					String valueString = valueObject.toString();
					if (valueString.equals(failDirective)) {
						String[] stringArray = new String[1];
						stringArray[0] = valueString;
						failedRecords.add(stringArray);
					}
				}
			}
		} else if (OpenmrsUtil.nullSafeEquals(
				check.getFailDirectiveOperator(),
				DataIntegrityConstants.FAILURE_OPERATOR_NOT_EQUALS)) {
			for (int i=0; i<resultList.size(); i++) {
				if (columnCount > 1) {
					String[] stringArray = new String[columnCount];
					for (int j=0; j<columnCount; j++) {
						Object valueObject = resultList.get(i)[j];
						String valueString = valueObject.toString();
						if (!valueString.equals(failDirective)) {
							for (int k=0; k<columnCount; k++) {
								stringArray[k] = resultList.get(i)[k].toString();
							}
							failedRecords.add(stringArray);
							break;
						}
					}
				} else {
					Object valueObject = resultList.get(i);
					String valueString = valueObject.toString();
					if (!valueObject.equals(failDirective)) {
						String[] stringArray = new String[1];
						stringArray[0] = valueString;
						failedRecords.add(stringArray);
					}
				}
			}
		} else {
			throw new Exception("Specified failure operator is unsupported");
		}

		// set the columns if found
		if (failedRecords.size() > 0)
			failedRecords.setColumns(resultList.getColumns());
	}

	public QueryResults getFailedRecords() {
		return this.failedRecords;
	}

	public boolean getCheckResult() {
		boolean checkPassed = (failedRecords.size() > 0) ? false : true;
		return checkPassed;
	}
}
