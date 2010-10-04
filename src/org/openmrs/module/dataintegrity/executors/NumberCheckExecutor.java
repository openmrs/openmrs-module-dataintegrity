package org.openmrs.module.dataintegrity.executors;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.openmrs.module.dataintegrity.IntegrityCheck;
import org.openmrs.module.dataintegrity.DataIntegrityConstants;
import org.openmrs.module.dataintegrity.IntegrityCheckUtil;
import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;
import org.openmrs.util.OpenmrsUtil;

public class NumberCheckExecutor implements ICheckExecutor {
	private IntegrityCheck check;
	private DataIntegrityDAO dao;
	private String parameterValues;
	private List<Double[]> failedRecords;
	
	public NumberCheckExecutor(DataIntegrityDAO dao) {
		this.dao = dao;
	}
	
	public void initializeExecutor(IntegrityCheck check, String parameterValues) {
		this.check = check;
		this.parameterValues = parameterValues;
		this.failedRecords = new ArrayList<Double[]>();
	}
	
	@SuppressWarnings("unchecked")
	public List<Double[]> getFailedRecords() {
		return this.failedRecords;
	}
	
	public boolean getCheckResult() {
		boolean checkPassed = (failedRecords.size() > 0) ? false : true;
		return checkPassed;
	}

	@SuppressWarnings("unchecked")
	public void executeCheck() throws Exception {
		try {
			Double failDirective = Double.valueOf(this.check.getFailDirective());
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
			} else {
				throw new Exception("Query returned no results");
			}
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}
}
