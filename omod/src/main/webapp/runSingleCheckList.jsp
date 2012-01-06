<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Run Integrity Checks" otherwise="/login.htm" redirect="/module/dataintegrity/runSingleCheck.list" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="dataintegrity.runSingleCheck.link"/></h2>
<br />

<script type="text/javascript">
	function visibleDataRow() 
	{
		document.getElementById("errorDiv").style.display = 'none';
		document.getElementById("errorDivParameter").style.display = 'none';
		var selectElement = document.getElementById("checkIdSelect");
		var i=1;
		for (i=1; i<selectElement.length; i++) {
			var rowIdOfCheck = "checkRow" + i;
			var checkRow = document.getElementById(rowIdOfCheck);
			checkRow.style.display = 'none';
		}
		var selectedCheck = selectElement.selectedIndex;
		var rowId = "checkRow" + document.getElementById("checkIdSelect").selectedIndex;
		var row = document.getElementById(rowId);
		var tbId = "checkParameterTxt" + document.getElementById("checkIdSelect").selectedIndex;
		var parameterTxt = document.getElementById(tbId).textContent;
		if (parameterTxt != '') {
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
			var paramId = "checkParameterTxt" + document.getElementById("checkIdSelect").selectedIndex;
			var parameterTxt = document.getElementById(paramId).textContent
			if (parameterTxt != '') {
				var tbId = "checkParameterValueTxt" + selectedCheck;
				var parameterValue = document.getElementById(tbId).value;
				if (parameterValue == '') {
					errorDivPElement.style.display = '';
					return false;
				}
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
			<td>
				<select name="checkId" id="checkIdSelect" onchange="visibleDataRow();">
					<option value="">--<spring:message code="dataintegrity.runSingleCheck.pick"/>--</option>
					<c:forEach items="${runSingleCheckList}" var="check">
						<option value="${check.id}">${check.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<br />
				<input type="submit" value="<spring:message code="dataintegrity.runSingleCheck.run"/>"/> 
			</td>
		</tr>
	</table>
	</c:if>
	
	<c:if test="${empty runSingleCheckList}"><spring:message code="dataintegrity.checksList.empty"/></c:if>
</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>