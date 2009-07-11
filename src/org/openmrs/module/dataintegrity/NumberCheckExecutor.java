package org.openmrs.module.dataintegrity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;

public class NumberCheckExecutor implements ICheckExecutor {
	private DataIntegrityCheckTemplate check;
	private DataIntegrityDAO dao;
	private String parameterValues;
	private List<Double[]> failedRecords;
	
	public NumberCheckExecutor(DataIntegrityDAO dao) {
		this.dao = dao;
	}
	
	public void initializeExecutor(DataIntegrityCheckTemplate check, String parameterValues) {
		this.check = check;
		this.parameterValues = parameterValues;
		this.failedRecords = new ArrayList<Double[]>();
	}
	
	public List<Double[]> getFailedRecords() {
		return this.failedRecords;
	}

	public void executeCheck() throws Exception {
		try {
			Double failDirective = Double.valueOf(this.check.getIntegrityCheckFailDirective());
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
				}
			} else {
				throw new Exception("Query returned no results");
			}
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
		
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
