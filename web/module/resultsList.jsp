<%@ include file="/WEB-INF/template/include.jsp" %>


<%@page import="org.openmrs.module.dataintegrity.DataIntegrityCheckResultTemplate"%>
<%@page import="java.util.List"%><openmrs:require privilege="Run Integrity Checks" otherwise="/login.htm" redirect="/admin/index.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="dataintegrity.results.title"/></h2>

<script type="text/javascript">
	function checkParameters() {
		var parameters = document.getElementById("paramHidden").value
		if (parameters != "") {
			var message = "Enter parameter value(s) for: " + parameters;
			var parameterValues = window.prompt(message,"");
			if (parameterValues != null) {
				document.getElementById("paramValueHidden").value = parameterValues;
			} else {
				return false;
			}
		}
		return true;
	}

	function openFailedRecordsPopUp()
	{
		var url = document.location.href;
		var components = url.split('/');
		var last = components[components.length - 1];
		var checkId = document.getElementById("checkIdHidden").value;
		var newUrl = url.replace(last, 'failedRecords.list') + '?checkId=' + checkId;
	   	window.open(newUrl, 'Failed Records', 'width=600,height=400,menubar=no,status=no,location=no,toolbar=no,scrollbars=yes');
	}
		
</script>

<br />
<c:if test="${not empty checkResults}">
<c:forEach items="${checkResults}" var="results">
<b class="boxHeader">${results.checkName} <spring:message code="dataintegrity.results.results"/></b>
<table class="box">
	<c:if test="${results.checkPassed == true}">
		<tr>
			<td width="20%"><spring:message code="dataintegrity.results.count"/></td>
			<td width="25%">${results.failedRecordCount}</td>
			<td width="55%"></td>
		</tr>
		<tr>
			<td width="20%"><spring:message code="dataintegrity.results.status"/></td>
			<td align="center" bgcolor="Green" width="25%" style="font-weight: bold; color: white;"><spring:message code="dataintegrity.results.pass"/></td>
			<td width="55%"></td>
		</tr>
	</c:if>
	<c:if test="${results.checkPassed == false}">
		<tr>
			<td width="20%"><spring:message code="dataintegrity.results.count"/></td>
			<td width="25%">${results.failedRecordCount}</td>
			<td width="55%"></td>
		</tr>
		<tr>
			<td width="20%"><spring:message code="dataintegrity.results.status"/></td>
			<td align="center" bgcolor="Red" width="25%" style="font-weight: bold; color: white;"><spring:message code="dataintegrity.results.fail"/></td>
			<td width="55%"></td>
		</tr>
	</c:if>
	<c:if test="${results.failedRecordCount > 0}">
	<tr>
		<td colspan="3">
			<input type="hidden" value="${results.checkId}" id="checkIdHidden"/>
			<a href="#" onclick="openFailedRecordsPopUp();"><spring:message code="dataintegrity.results.failedRecords"/></a>
		</td>
	</tr>
	</c:if>
	<tr>
		<td colspan="3">
			<c:if test="${single == false}">
				<form method="post" target="_blank" onsubmit="return checkParameters();">
					<input type="hidden" value="${results.checkId}" name="checkId"/>
					<input type="hidden" value="${results.parameters}" id="paramHidden"/>
					<input type="hidden" value="" id="paramValueHidden" name="checkParameter${results.checkId}"/>"
					<input type="submit" value="<spring:message code="dataintegrity.results.runAgain"/>"/>
				</form>
			</c:if>
			<c:if test="${single == true}">
				<form method="post" onsubmit="return checkParameters();">
					<input type="hidden" value="${results.checkId}" name="checkId" />
					<input type="hidden" value="${results.parameters}" id="paramHidden"/>
					<input type="hidden" value="" id="paramValueHidden" name="checkParameter${results.checkId}"/>
					<input type="submit" value="<spring:message code="dataintegrity.results.runAgain"/>"/>
				</form>
			</c:if>
		</td>
	</tr>
</table>
<br />
</c:forEach>
</c:if>

<%@ include file="/WEB-INF/template/footer.jsp" %>