package org.openmrs.module.dataintegrity.executors;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.openmrs.module.dataintegrity.DataIntegrityCheckTemplate;
import org.openmrs.module.dataintegrity.DataIntegrityConstants;
import org.openmrs.module.dataintegrity.IntegrityCheckUtil;
import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;
import org.openmrs.util.OpenmrsUtil;

public class StringCheckExecutor implements ICheckExecutor {
	private DataIntegrityCheckTemplate check;
	private DataIntegrityDAO dao;
	private String parameterValues;
	private List<String[]> failedRecords;
	
	public StringCheckExecutor(DataIntegrityDAO dao) {
		this.dao = dao;
	}
	
	@SuppressWarnings("unchecked")
	public void executeCheck() throws Exception {
		try {
			String failDirective = this.check.getFailDirective();
			String checkCode = IntegrityCheckUtil.getModifiedCheckCode(
					this.check.getCheckCode(),
					this.check.getRepairParameters(), this.parameterValues);
			SessionFactory factory = this.dao.getSessionFactory();
			SQLQuery query = factory.getCurrentSession().createSQLQuery(checkCode);
			List<Object[]> resultList = query.list();
			if (resultList.size() > 0) {
				int columnCount;
				try {
					Object[] records = resultList.get(0);
					columnCount = records.length;
				} catch (Exception e) {
					columnCount = 1;
				}
				if (OpenmrsUtil.nullSafeEquals(
						check.getFailDirectiveOperator(),
						DataIntegrityConstants.FAILURE_OPERATOR_CONTAINS)) {
					for (int i=0; i<resultList.size(); i++) {
						if (columnCount > 1) {
							String[] stringArray = new String[columnCount];
							for (int j=0; j<columnCount; j++) {
								Object valueObject = resultList.get(i)[j];
								String valueString = valueObject.toString();
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
			} else {
				throw new Exception("Query returned no results");
			}
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List<String[]> getFailedRecords() {
		return this.failedRecords;
	}

	public void initializeExecutor(DataIntegrityCheckTemplate check,
			String parameterValues) {
		this.check = check;
		this.parameterValues = parameterValues;
		this.failedRecords = new ArrayList<String[]>();
	}

	public boolean getCheckResult() {
		boolean checkPassed = (failedRecords.size() > 0) ? false : true;
		return checkPassed;
	}
}
