<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Integrity Checks" otherwise="/login.htm" redirect="/admin/index.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="dataintegrity.addeditCheck"/></h2>

<script type="text/javascript">
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
			    opt1.value = "less";
			    opt1.text = "Less Than";
			    failOpSelectBox.add(opt1, null);
			    var opt2 = document.createElement("option");
			    opt2.value = "great";
			    opt2.text = "Greater Than";
			    failOpSelectBox.add(opt2, null);
			    var opt3 = document.createElement("option");
			    opt3.value = "equal";
			    opt3.text = "Equals";
			    failOpSelectBox.add(opt3, null);
			    var opt4 = document.createElement("option");
			    opt4.value = "notEqual";
			    opt4.text = "Not Equals";
			    failOpSelectBox.add(opt4, null);
			    break;
			case 2:
				var opt = document.createElement("option");
			    opt.value = "equal";
			    opt.text = "Equals";
			    failOpSelectBox.add(opt, null);
			    break;
			case 3:
				var opt1 = document.createElement("option");
			    opt1.value = "equal";
			    opt1.text = "Equals";
			    failOpSelectBox.add(opt1, null);
			    var opt2 = document.createElement("option");
			    opt2.value = "notEqual";
			    opt2.text = "Not Equals";
			    failOpSelectBox.add(opt2, null);
			    var opt3 = document.createElement("option");
			    opt3.value = "contain";
			    opt3.text = "Contains";
			    failOpSelectBox.add(opt3, null);
			    var opt4 = document.createElement("option");
			    opt4.value = "notContain";
			    opt4.text = "Not Contains";
			    failOpSelectBox.add(opt4, null);
			    break;
			}
		}
	}

	function inputValidator() {
		var errorDivElement = document.getElementById("errorDiv");
		var nameElement = document.getElementById("nameTxt");
		if (nameElement.value == "") {
			errorDivElement.style.display = '';
			return false;
		} else {
			errorDivElement.style.display = 'none';
			return true;
		}
	}
</script>
<div class="error" id="errorDiv" style="display: none"><spring:message code="dataintegrity.checksList.blank"/></div>
<form method="post" onsubmit="return inputValidator()">
	<c:if test="${empty existingCheck}">
		<table>
			<tr>
				<td valign="top"><spring:message code="dataintegrity.checksList.columns.checkType"/></td>
				<td>
					<select name="checkType" id="typeSelect" style="width: 130px">
						<option value="sql">SQL</option>
						<!-- Other check types to be implemented later
						<option value="groovy">Groovy</option>
						<option value="java">Java</option>
						-->
					</select>
				</td>
			</tr>
			<tr>
				<td><spring:message code="dataintegrity.checksList.columns.name"/></td>
				<td><input type="text" name="name" value="" size="52" maxlength="100" id="nameTxt"/></td>
			</tr>
			<tr>
				<td valign="top"><spring:message code="dataintegrity.checksList.columns.code"/></td>
				<td><textarea rows="5" cols="50" name="code"></textarea></td>
			</tr>
			<tr>
				<td valign="top"><spring:message code="dataintegrity.checksList.columns.resultType"/></td>
				<td>
					<select name="resultType" id="resultTypeSelect" style="width: 130px" onchange="changeSelectContents()">
						<option value="count">Count</option>
						<option value="number">Number</option>
						<option value="boolean">Boolean</option>
						<option value="string">String</option>
					</select>
				</td>
			</tr>
			<tr>
				<td valign="top"><spring:message code="dataintegrity.checksList.columns.failOp"/></td>
				<td>
					<select name="failOp" id="failOpSelect" style="width: 130px">
						<option value="less">Less Than</option>
						<option value="great">Greater Than</option>
						<option value="equal">Equals</option>
						<option value="notEqual">Not Equals</option>
					</select>
				</td>
			</tr>
			<tr>
				<td><spring:message code="dataintegrity.checksList.columns.fail"/></td>
				<td><input type="text" name="fail" value="" size="52" maxlength="100"/></td>
			</tr>
			<tr>
				<td valign="top"><spring:message code="dataintegrity.checksList.columns.repairType"/></td>
				<td>
					<select name="repairType" id="repairTypeSelect" style="width: 130px" onchange="enableRepairDirective()">
						<option value="link">Link</option>
						<option value="script">Script</option>
						<option value="instruction">Instructions</option>
						<option value="none">None</option>
					</select>
				</td>
			</tr>
			<tr id="repairRow">
				<td valign="top"><spring:message code="dataintegrity.checksList.columns.repair"/></td>
				<td><textarea rows="5" cols="50" name="repair"></textarea></td>
			</tr>
			<tr>
				<td><spring:message code="dataintegrity.checksList.columns.parameters"/></td>
				<td><input type="text" name="parameters" value="" size="52" maxlength="500"/></td>
			</tr>
			<tr>
				<td>
					<input type="submit" value="<spring:message code="dataintegrity.addeditCheck.save"/>"/>
				</td>
			</tr>
		</table>
	</c:if>
	<c:if test="${not empty existingCheck}">
		<table>
			<tr>
				<td><spring:message code="dataintegrity.checksList.columns.name"/></td>
				<td><input type="text" name="name" value="${existingCheck.integrityCheckName}" size="50" /></td>
				<td><i><spring:message code="dataintegrity.checksList.columns.name.help"/></i></td>
			</tr>
			<tr>
				<td valign="top"><spring:message code="dataintegrity.checksList.columns.sql"/></td>
				<td><textarea rows="5" cols="47" name="sql">${existingCheck.integrityCheckSql}</textarea></td>
				<td><i><spring:message code="dataintegrity.checksList.columns.sql.help"/></i></td>
			</tr>	
			<tr>
				<td valign="top"><spring:message code="dataintegrity.checksList.columns.base"/></td>
				<td>
					<select name="base" onchange="enableScore()" id="baseSelect">
						<option value="1" <c:if test="${existingCheck.integrityCheckBaseForFailure == 1}">selected</c:if>><spring:message code="dataintegrity.addeditCheck.base.all"/></option>
						<option value="2" <c:if test="${existingCheck.integrityCheckBaseForFailure == 2}">selected</c:if>><spring:message code="dataintegrity.addeditCheck.base.some"/></option>
					</select>
				</td>
				<td><i><spring:message code="dataintegrity.checksList.columns.base.help"/></i></td>
			</tr>
			<c:if test="${existingCheck.integrityCheckBaseForFailure == 1}">
				<tr id="scoreRow" style="display: none">
					<td><spring:message code="dataintegrity.checksList.columns.score"/></td>
					<td>
						<input type="text" name="score" size="5" id="scoreTxt" onkeypress="return isNumberKey(event)" value=""/>
					</td>
					<td><i><spring:message code="dataintegrity.checksList.columns.score.help"/></i></td>
				</tr>
			</c:if>
			<c:if test="${existingCheck.integrityCheckBaseForFailure == 2}">
				<tr id="scoreRow">
					<td><spring:message code="dataintegrity.checksList.columns.score"/></td>
					<td>
						<input type="text" name="score" size="5" id="scoreTxt" onkeypress="return isNumberKey(event)" value="${existingCheck.integrityCheckScore}"/>
					</td>
					<td><i><spring:message code="dataintegrity.checksList.columns.score.help"/></i></td>
				</tr>
			</c:if>
			<tr>
				<td>
					<input type="hidden" name="checkId" value="${existingCheck.integrityCheckId}" />
					<input type="submit" value="<spring:message code="dataintegrity.addeditCheck.save"/>"/>
				</td>
			</tr>
		</table>
	</c:if>
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