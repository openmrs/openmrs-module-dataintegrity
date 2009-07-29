<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Run Integrity Checks" otherwise="/login.htm" redirect="/admin/index.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="dataintegrity.runSingleCheck.link"/></h2>
<br />

<script type="text/javascript">
	var parameterArray;
	
	/*window.onload = function()
	{
		var selectElement = document.getElementById("checkIdSelect");
		selectElement.selectedIndex = 1;
		selectElement.selectedIndex = 0;
	}*/

	function visibleDataRow() 
	{
		document.getElementById("errorDiv").style.display = 'none';
		var selectElement = document.getElementById("checkIdSelect");
		var i=1;
		for (i=1; i<selectElement.length; i++) {
			var rowIdOfCheck = "checkRow" + i;
			var checkRow = document.getElementById(rowIdOfCheck);
			checkRow.style.display = 'none';
		}
		var selectedCheck = selectElement.selectedIndex;
		var rowId = "checkRow" + selectedCheck;
		var row = document.getElementById(rowId);
		var tbId = "checkParameterTxt" + selectedCheck;
		var parameterTxt = document.getElementById(tbId).value;
		if (parameterTxt != 'none') {
			row.style.display = '';
		} else {
			row.style.display = 'none';
		}
	}

	function validateSelect() {
		var errorDivElement = document.getElementById("errorDiv");
		var errorDivPElement = document.getElementById("errorDivParameter");
		var selectedCheck = document.getElementById("checkIdSelect").selectedIndex;
		if (selectedCheck == 0) {
			errorDivElement.style.display = '';
			return false;
		} else {
			var tbId = "checkParameterValueTxt" + selectedCheck;
			var parameterValue = document.getElementById(tbId).value;
			if (parameterValue == '') {
				errorDivPElement.style.display = '';
				return false;
			}
		}
		return true;
	}
</script>

<div class="error" id="errorDiv" style="display: none"><spring:message code="dataintegrity.runSingleCheck.blank"/></div>
<div class="error" id="errorDivParameter" style="display: none"><spring:message code="dataintegrity.runSingleCheck.parameter.blank"/></div>
<b class="boxHeader"><spring:message code="dataintegrity.runSingleCheck.title"/></b>
<form class="box" method="post" onsubmit="return validateSelect();">
	<c:if test="${not empty runSingleCheckList}">
	<table>
		<tr>
			<td> <spring:message code="dataintegrity.runSingleCheck.choose"/> </td>
			<td colspan="2">
				<select name="checkId" id="checkIdSelect" onchange="visibleDataRow();">
					<option value="">--<spring:message code="dataintegrity.runSingleCheck.pick"/>--</option>
					<c:forEach items="${runSingleCheckList}" var="checkList">
						<option value="${checkList.integrityCheckId}">${checkList.integrityCheckName}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<%int checkCount = 1; %>
		<c:forEach items="${runSingleCheckList}" var="checkList">
			<tr id="<%="checkRow" + checkCount%>" style="display: none;">
				<td> <spring:message code="dataintegrity.runSingleCheck.parameters"/> </td>
				<td>
					<c:if test="${checkList.integrityCheckParameters == ''}">
						<input type="text" value="none" id="<%="checkParameterTxt" + checkCount%>"/>
					</c:if>
					<c:if test="${checkList.integrityCheckParameters != ''}">
						<input type="text" value="${checkList.integrityCheckParameters}" id="<%="checkParameterTxt" + checkCount%>"/>
					</c:if>
				</td>
				<td>
					<c:if test="${checkList.integrityCheckParameters == ''}">
						<input type="text" value="none" id="<%="checkParameterValueTxt" + checkCount%>"/>
					</c:if>
					<c:if test="${checkList.integrityCheckParameters != ''}">
						<input type="text" value="" name="checkParameter${checkList.integrityCheckId}" id="<%="checkParameterValueTxt" + checkCount%>"/>
					</c:if>
				</td>
			</tr>
			<%checkCount++; %>
		</c:forEach>
		<tr>
			<td colspan="3">
				<br />
				<input type="submit" value="<spring:message code="dataintegrity.runSingleCheck.run"/>"/> 
			</td>
		</tr>
	</table>
	</c:if>
	
	<c:if test="${empty runSingleCheckList}"><spring:message code="dataintegrity.checksList.empty"/></c:if>
</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>