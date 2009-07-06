package org.openmrs.module.dataintegrity;

public interface IDataIntegrityCheckUpload {
	public String getCheckName();
	public String getCheckType();
	public String getCheckCode();
	public String getCheckResultType();
	public String getCheckFailDirective();
	public String getCheckRepairDirective();
	public String getCheckRepairType();
	public String getCheckFailDirectiveOperator();
	public String getCheckParameters();
}
