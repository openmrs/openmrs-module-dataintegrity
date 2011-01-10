<%@ include file="/WEB-INF/template/include.jsp" %>
<%@page import="org.openmrs.module.dataintegrity.IntegrityCheck"%>

<openmrs:require privilege="Manage Integrity Checks" otherwise="/login.htm" redirect="/module/dataintegrity/integrityCheck.form" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="dataintegrity.addeditCheck"/></h2>

<script type="text/javascript">
	function enableRepairCode() {
		var selectElement = document.getElementById("repairCodeTypeSelect");
		var row = document.getElementById("repairCodeRow");
		var row2 = document.getElementById("repairCodeParameters");
		if (selectElement.selectedIndex == 1) {
			row.style.display = 'none';
			row2.style.display = 'none';
		} else {
			row.style.display = '';
			row2.style.display = '';
		}
	}

	function enableRepairDirective() {
		var selectElement = document.getElementById("repairTypeSelect");
		var row = document.getElementById("repairRow");
		if (selectElement.selectedIndex == 3) {
			row.style.display = 'none';
		} else {
			row.style.display = '';
		}
	}

	function changeSelectContents() {
		var resultTypeSelectBox = document.getElementById("resultTypeSelect");
		var failOpSelectBox = document.getElementById("failOpSelect");
		if (failOpSelectBox != null)
		{
			failOpSelectBox.innerHTML = "";
			var selected = resultTypeSelectBox.selectedIndex;
			switch (selected)
			{
			case 0:
			case 1:
				var opt1 = document.createElement("option");
			    opt1.value = "less than";
			    opt1.text = "Less Than";
			    failOpSelectBox.add(opt1, null);
			    var opt2 = document.createElement("option");
			    opt2.value = "greater than";
			    opt2.text = "Greater Than";
			    failOpSelectBox.add(opt2, null);
			    var opt3 = document.createElement("option");
			    opt3.value = "equals";
			    opt3.text = "Equals";
			    failOpSelectBox.add(opt3, null);
			    var opt4 = document.createElement("option");
			    opt4.value = "not equals";
			    opt4.text = "Not Equals";
			    failOpSelectBox.add(opt4, null);
			    break;
			case 2:
				var opt = document.createElement("option");
			    opt.value = "equals";
			    opt.text = "Equals";
			    failOpSelectBox.add(opt, null);
			    break;
			case 3:
				var opt1 = document.createElement("option");
			    opt1.value = "equals";
			    opt1.text = "Equals";
			    failOpSelectBox.add(opt1, null);
			    var opt2 = document.createElement("option");
			    opt2.value = "not equals";
			    opt2.text = "Not Equals";
			    failOpSelectBox.add(opt2, null);
			    var opt3 = document.createElement("option");
			    opt3.value = "contains";
			    opt3.text = "Contains";
			    failOpSelectBox.add(opt3, null);
			    var opt4 = document.createElement("option");
			    opt4.value = "not contains";
			    opt4.text = "Not Contains";
			    failOpSelectBox.add(opt4, null);
			    break;
			}
		}
	}

	function inputValidator() {
		var errorDivElement = document.getElementById("errorDiv");
		var name = document.getElementById("nameTxt").value;
		var code = document.getElementById("codeTxt").value;
		var fail = document.getElementById("failTxt").value;
		var repair = document.getElementById("repairTxt").value;
		var repairType = document.getElementById("repairTypeSelect").selectedIndex;
		
		if (repairType == 3) {
			if (name == "" || code == "" || fail == "") {
				errorDivElement.style.display = '';
				return false;
			} else {
				return true;
			}
		} else {
			if (name == "" || code == "" || fail == "" || repair == "") {
				errorDivElement.style.display = '';
				return false;
			} else {
				return true;
			}
		}
	}
</script>
<div class="error" id="errorDiv" style="display: none"><spring:message code="dataintegrity.checksList.blank"/></div>
<form method="post" onsubmit="return inputValidator()">
	<table>
		<tr>
			<td><spring:message code="dataintegrity.checksList.columns.name"/></td>
			<td><input type="text" name="name" value="${integrityCheck.name}" size="52" maxlength="100" id="nameTxt"/></td>
			<td valign="top"><div style="color: red">*</div></td>
		</tr>
		<tr>
			<td valign="top"><spring:message code="dataintegrity.checksList.columns.checkType"/></td>
			<td colspan="2">
				<select name="checkType" id="typeSelect" style="width: 130px">
					<option value="sql" <c:if test="${integrityCheck.integrityCheckType == 'sql'}">selected</c:if>>SQL</option>
					<!-- Other check types to be implemented later
					<option value="groovy">Groovy</option>
					<option value="java">Java</option>
					-->
				</select>
			</td>
		</tr>
		<tr>
			<td valign="top"><spring:message code="dataintegrity.checksList.columns.code"/></td>
			<td><textarea rows="5" cols="50" name="checkCode" id="codeTxt">${integrityCheck.checkCode}</textarea></td>
			<td valign="top"><div style="color: red">*</div></td>
		</tr>
		<tr>
			<td><spring:message code="dataintegrity.checksList.columns.checkParameters"/></td>
			<td colspan="2"><input type="text" name="checkParameters" value="${integrityCheck.checkParameters}" size="52" maxlength="500"/></td>
		</tr>
		<tr>
			<td colspan="3"><hr /></td>
		</tr>
		<tr>
			<td valign="top"><spring:message code="dataintegrity.checksList.columns.resultType"/></td>
			<td colspan="2">
				<select name="resultType" id="resultTypeSelect" style="width: 130px" onchange="changeSelectContents()">
					<option value="count" <c:if test="${integrityCheck.resultType == 'count'}">selected</c:if>>Count</option>
					<option value="number" <c:if test="${integrityCheck.resultType == 'number'}">selected</c:if>>Number</option>
					<option value="boolean" <c:if test="${integrityCheck.resultType == 'boolean'}">selected</c:if>>Boolean</option>
					<option value="string" <c:if test="${integrityCheck.resultType == 'string'}">selected</c:if>>String</option>
				</select>
			</td>
		</tr>
		<tr>
			<td valign="top"><spring:message code="dataintegrity.checksList.columns.failOp"/></td>
			<td colspan="2">
				<select name="failOp" id="failOpSelect" style="width: 130px">
			    <c:choose>
					<c:when test="${integrityCheck.resultType == 'boolean'}">
						<option value="equals" <c:if test="${integrityCheck.failDirectiveOperator == 'equals'}">selected</c:if>>Equals</option>
					</c:when>
					<c:when test="${integrityCheck.resultType == 'string'}">
						<option value="equals" <c:if test="${integrityCheck.failDirectiveOperator == 'equals'}">selected</c:if>>Equals</option>
						<option value="not equals" <c:if test="${integrityCheck.failDirectiveOperator == 'not equals'}">selected</c:if>>Not Equals</option>
						<option value="contains" <c:if test="${integrityCheck.failDirectiveOperator == 'contains'}">selected</c:if>>Contains</option>
						<option value="not contains" <c:if test="${integrityCheck.failDirectiveOperator == 'not contains'}">selected</c:if>>Not Contains</option>
					</c:when>
					<c:otherwise>
						<option value="less than" <c:if test="${integrityCheck.failDirectiveOperator == 'less than'}">selected</c:if>>Less Than</option>
						<option value="greater than" <c:if test="${integrityCheck.failDirectiveOperator == 'greater than'}">selected</c:if>>Greater Than</option>
						<option value="equals" <c:if test="${integrityCheck.failDirectiveOperator == 'equals'}">selected</c:if>>Equals</option>
						<option value="not equals" <c:if test="${integrityCheck.failDirectiveOperator == 'not equals'}">selected</c:if>>Not Equals</option>
					</c:otherwise>
				</c:choose>
				</select>
			</td>
		</tr>
		<tr>
			<td><spring:message code="dataintegrity.checksList.columns.fail"/></td>
			<td><input type="text" name="fail" value="${integrityCheck.failDirective}" size="52" maxlength="100" id="failTxt"/></td>
			<td valign="top"><div style="color: red">*</div></td>
		</tr>
		<tr>
			<td colspan="3"><hr /></td>
		</tr>
		<tr>
			<td valign="top"><spring:message code="dataintegrity.addeditCheck.repair.code.type"/></td>
			<td colspan="2">
				<select name="repairCodeType" id="repairCodeTypeSelect" style="width: 130px" onchange="enableRepairCode()">
					<option value="sql" <c:if test="${integrityCheck.repairCodeType == 'sql'}">selected</c:if>>SQL</option>
					<option value="none" <c:if test="${integrityCheck.repairCodeType == 'none'}">selected</c:if>>Use Check Code</option>
					<!-- Other check types to be implemented later
					<option value="groovy">Groovy</option>
					<option value="java">Java</option>
					-->
				</select>
			</td>
		</tr>
		<c:if test="${integrityCheck.repairCodeType == 'none'}">
		<tr id="repairCodeRow" style="display: none;">
			<td valign="top"><spring:message code="dataintegrity.addeditCheck.repair.code"/></td>
			<td><textarea rows="5" cols="50" name="repairCode" id="repairCode">${integrityCheck.repairCode}</textarea></td>
			<td valign="top"><div style="color: red">*</div></td>
		</tr>
		<tr id="repairCodeParameters" style="display: none;">
			<td><spring:message code="dataintegrity.checksList.columns.parameters"/></td>
			<td colspan="2"><input type="text" name="repairParameters" value="${integrityCheck.repairParameters}" size="52" maxlength="500"/></td>
		</tr>
		</c:if>
		<c:if test="${integrityCheck.repairCodeType != 'none'}">
		<tr id="repairCodeRow">
			<td valign="top"><spring:message code="dataintegrity.addeditCheck.repair.code"/></td>
			<td><textarea rows="5" cols="50" name="repairCode" id="repairCode">${integrityCheck.repairCode}</textarea></td>
			<td valign="top"><div style="color: red">*</div></td>
		</tr>
		<tr id="repairCodeParameters">
			<td><spring:message code="dataintegrity.checksList.columns.parameters"/></td>
			<td colspan="2"><input type="text" name="repairParameters" value="${integrityCheck.repairParameters}" size="52" maxlength="500"/></td>
		</tr>
		</c:if>
		<tr>
			<td colspan="3"><hr /></td>
		</tr>
		<tr>
			<td valign="top"><spring:message code="dataintegrity.checksList.columns.repairType"/></td>
			<td colspan="2">
				<select name="repairType" id="repairTypeSelect" style="width: 130px" onchange="enableRepairDirective()">
					<option value="link" <c:if test="${integrityCheck.repairType == 'link'}">selected</c:if>>Link</option>
					<option value="script" <c:if test="${integrityCheck.repairType == 'script'}">selected</c:if>>Script</option>
					<option value="instructions" <c:if test="${integrityCheck.repairType == 'instructions'}">selected</c:if>>Instructions</option>
					<option value="none" <c:if test="${integrityCheck.repairType == 'none'}">selected</c:if>>None</option>
				</select>
			</td>
		</tr>
		<c:if test="${integrityCheck.repairType == 'none'}">
		<tr id="repairRow" style="display: none;">
			<td valign="top"><spring:message code="dataintegrity.checksList.columns.repair"/></td>
			<td><textarea rows="5" cols="50" name="repair" id="repairTxt">${integrityCheck.repairDirective}</textarea></td>
			<td valign="top"><div style="color: red;">*</div></td>
		</tr>
		</c:if>
		<c:if test="${integrityCheck.repairType != 'none'}">
		<tr id="repairRow">
			<td valign="top"><spring:message code="dataintegrity.checksList.columns.repair"/></td>
			<td><textarea rows="5" cols="50" name="repair" id="repairTxt">${integrityCheck.repairDirective}</textarea></td>
			<td valign="top"><div style="color: red">*</div></td>
		</tr>
		</c:if>
		<tr>
			<td colspan="3"><hr /></td>
		</tr>
		<tr>
			<td><spring:message code="dataintegrity.addeditCheck.clearResults"/></td>
			<td colspan="2"><input type="checkbox" name="clearResults" /></td>
		</tr>
		<tr>
			<td colspan="3">
				<input type="hidden" name="checkId" value="${integrityCheck.id}" />
				<input type="submit" value="<spring:message code="dataintegrity.addeditCheck.save"/>"/>
			</td>
		</tr>
	</table>
</form>
<br/>

<b class="boxHeader"><spring:message code="dataintegrity.addeditCheck.help" /></b>
<div class="box">
	<ul>
		<li><i><spring:message code="dataintegrity.addeditCheck.help.query"/></i>
		<li><i><spring:message code="dataintegrity.addeditCheck.help.basis1"/></i>
		<li><i><spring:message code="dataintegrity.addeditCheck.help.basis2"/></i>
		<li><i><spring:message code="dataintegrity.addeditCheck.help.score"/></i>
	</ul>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>