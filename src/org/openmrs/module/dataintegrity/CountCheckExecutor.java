package org.openmrs.module.dataintegrity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;

public class CountCheckExecutor implements ICheckExecutor {
	private DataIntegrityCheckTemplate check;
	private DataIntegrityDAO dao;
	private String parameterValues;
	private List<Object[]> failedRecords;
	
	public CountCheckExecutor(DataIntegrityDAO dao) {
		this.dao = dao;
	}

	public void executeCheck() throws Exception {
		try {
			int failDirective = Integer.valueOf(this.check.getIntegrityCheckFailDirective());
			String checkCode = getModifiedCheckCode();
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
				if (check.getIntegrityCheckFailDirectiveOperator().equals(DataIntegrityConstants.FAILURE_OPERATOR_LESS_THAN)) {
					if (resultList.size() < failDirective) {
						for (int i=0; i<resultList.size(); i++) {
							if (columnCount > 1) {
								Object[] objectArray = new Object[columnCount];
								for (int j=0; j<columnCount; j++) {
									Object value = resultList.get(i)[j];
									objectArray[j] = value;
								}
								failedRecords.add(objectArray);
							} else {
								Object value = resultList.get(i);
								Object[] objectArray = new Object[1];
								objectArray[0] = value;
								failedRecords.add(objectArray);
							}
						}
					}
				} else if (check.getIntegrityCheckFailDirectiveOperator().equals(DataIntegrityConstants.FAILURE_OPERATOR_GREATER_THAN)) {
					if (resultList.size() > failDirective) {
						for (int i=0; i<resultList.size(); i++) {
							if (columnCount > 1) {
								Object[] objectArray = new Object[columnCount];
								for (int j=0; j<columnCount; j++) {
									Object value = resultList.get(i)[j];
									objectArray[j] = value;
								}
								failedRecords.add(objectArray);
							} else {
								Object value = resultList.get(i);
								Object[] objectArray = new Object[1];
								objectArray[0] = value;
								failedRecords.add(objectArray);
							}
						}
					}
				} else if (check.getIntegrityCheckFailDirectiveOperator().equals(DataIntegrityConstants.FAILURE_OPERATOR_EQUALS)) {
					if (resultList.size() == failDirective) {
						for (int i=0; i<resultList.size(); i++) {
							if (columnCount > 1) {
								Object[] objectArray = new Object[columnCount];
								for (int j=0; j<columnCount; j++) {
									Object value = resultList.get(i)[j];
									objectArray[j] = value;
								}
								failedRecords.add(objectArray);
							} else {
								Object value = resultList.get(i);
								Object[] objectArray = new Object[1];
								objectArray[0] = value;
								failedRecords.add(objectArray);
							}
						}
					}
				} else if (check.getIntegrityCheckFailDirectiveOperator().equals(DataIntegrityConstants.FAILURE_OPERATOR_NOT_EQUALS)) {
					if (resultList.size() != failDirective) {
						for (int i=0; i<resultList.size(); i++) {
							if (columnCount > 1) {
								Object[] objectArray = new Object[columnCount];
								for (int j=0; j<columnCount; j++) {
									Object value = resultList.get(i)[j];
									objectArray[j] = value;
								}
								failedRecords.add(objectArray);
							} else {
								Object value = resultList.get(i);
								Object[] objectArray = new Object[1];
								objectArray[0] = value;
								failedRecords.add(objectArray);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

	}

	public List<Object[]> getFailedRecords() {
		return this.failedRecords;
	}

	public void initializeExecutor(DataIntegrityCheckTemplate check,
			String parameterValues) {
		this.check = check;
		this.parameterValues = parameterValues;
		this.failedRecords = new ArrayList<Object[]>();
	}
	
	private String getModifiedCheckCode() {
		String checkCode = this.check.getIntegrityCheckCode();
		String[] parameters = this.check.getIntegrityCheckParameters().split(";");
		if (this.parameterValues != null) {
			String[] parameterValuesArray = this.parameterValues.split(";");
			for (int i=0; i<parameters.length; i++) {
				checkCode.replace(parameters[i], parameterValuesArray[i]);
			}
		}
		return checkCode;
	}

}
