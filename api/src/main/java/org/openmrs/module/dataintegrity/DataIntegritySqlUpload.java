package org.openmrs.module.dataintegrity;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class DataIntegritySqlUpload implements IDataIntegrityCheckUpload {
	private String code;
	private String failDirective;
	private String failDirectiveOperator;
	private String name;
	private String repairDirective;
	private String resultType;
	private String checkType;
	private String repairType;
	private String parameters;
	
	public DataIntegritySqlUpload(Element element) throws Exception {
		this.checkType = DataIntegrityConstants.CHECK_LANGUAGE_SQL;
		
		NodeList nameList = element.getElementsByTagName("name");
		Element nameNode = (Element) nameList.item(0);
		this.name = nameNode.getFirstChild().getNodeValue();
		
		NodeList codeList = element.getElementsByTagName("code");
		Element codeNode = (Element) codeList.item(0);
		this.code = codeNode.getFirstChild().getNodeValue();
		
		String codeCopy = this.code;
		if (codeCopy.toLowerCase().contains("delete") || codeCopy.toLowerCase().contains("update") || codeCopy.toLowerCase().contains("insert")) {
			throw new Exception("Code will modify the database hence not allowed");
		}
		
		NodeList resultTypeList = element.getElementsByTagName("resultType");
		Element resultTypeNode = (Element) resultTypeList.item(0);
		this.resultType = resultTypeNode.getFirstChild().getNodeValue().toLowerCase();
		
		NodeList failList = element.getElementsByTagName("fail");
		Element failNode = (Element) failList.item(0);
		this.failDirective = failNode.getFirstChild().getNodeValue();
		if (!this.resultType.equals(DataIntegrityConstants.FAILURE_TYPE_BOOLEAN)) {
			this.failDirectiveOperator = failNode.getAttribute("operator");
		} else {
			this.failDirectiveOperator = "equals";
		}
		
		NodeList repairList = element.getElementsByTagName("repair");
		if (repairList.getLength() > 0) {
			Element repairNode = (Element) repairList.item(0);
			this.repairDirective = repairNode.getFirstChild().getNodeValue();
			this.repairType = repairNode.getAttribute("type");
		} else {
			this.repairDirective = "";
			this.repairType = "none";
		}
		
		NodeList parameterList = element.getElementsByTagName("parameters");
		if (parameterList.getLength() > 0) {
			Element parameterNode = (Element) parameterList.item(0);
			this.parameters = parameterNode.getFirstChild().getNodeValue();
		} else {
			this.parameters = "";
		}
	}
	
	public String getCheckCode() {
		return this.code;
	}

	public String getCheckFailDirective() {
		return this.failDirective;
	}

	public String getCheckName() {
		return this.name;
	}

	public String getCheckRepairDirective() {
		return this.repairDirective;
	}

	public String getCheckResultType() {
		return this.resultType;
	}

	public String getCheckType() {
		return this.checkType;
	}
	
	public String getCheckFailDirectiveOperator() {
		return this.failDirectiveOperator;
	}
	public String getCheckParameters() {
		return this.parameters;
	}
	public String getCheckRepairType() {
		return this.repairType;
	}

}
