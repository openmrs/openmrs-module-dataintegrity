<%@ include file="/WEB-INF/template/include.jsp" %>
<openmrs:require privilege="Run Integrity Checks" otherwise="/login.htm" redirect="/admin/index.htm" />
<%@page import="org.openmrs.module.dataintegrity.DataIntegrityConstants"%>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<%
	pageContext.setAttribute("stack", session.getAttribute(DataIntegrityConstants.DATA_INTEGRITY_ERROR_STACK_TRACE));
  	session.removeAttribute(DataIntegrityConstants.DATA_INTEGRITY_ERROR_STACK_TRACE);
%>
<c:if test="${stack != null}">
	<div id="openmrs_error"><spring:message code="${stack}" text="${stack}"/></div>
</c:if>

<h2><spring:message code="dataintegrity.results.title"/></h2>

<script type="text/javascript">
	function checkParameters(buttonId) {
		var hiddenId = "paramHidden" + buttonId;
		var parameters = document.getElementById(hiddenId).value
		if (parameters != "") {
			var message = "Enter parameter value(s) for: " + parameters;
			var parameterValues = window.prompt(message,"");
			if (parameterValues != null) {
				var hiddenValueId = "paramValueHidden" + buttonId;
				document.getElementById(hiddenValueId).value = parameterValues;
			} else {
				return false;
			}
		}
		return true;
	}

	function openFailedRecordsPopUp(buttonId)
	{
		var url = document.location.href;
		var components = url.split('/');
		var last = components[components.length - 1];
		var hiddenId = "checkIdHidden" + buttonId;
		var checkId = document.getElementById(hiddenId).value;
		var newUrl = url.replace(last, 'failedRecords.list') + '?checkId=' + checkId;
	   	window.open(newUrl, 'Failed_Records', 'width=600,height=400,menubar=no,status=no,location=no,toolbar=no,scrollbars=yes');
	}
		
</script>

<br />
<c:if test="${not empty checkResults}">
<%int checkCount = 0; %>
<c:forEach items="${checkResults}" var="results">
<b class="boxHeader">${results.checkName} <spring:message code="dataintegrity.results.results"/></b>
<table class="box">
	<tr>
		<td width="20%"><spring:message code="dataintegrity.results.count"/></td>
		<td width="25%">${results.failedRecordCount}</td>
		<td width="55%"></td>
	</tr>
	<tr>
		<td><spring:message code="dataintegrity.checksList.columns.failOp"/></td>
		<td>${results.failOperator}</td>
		<td></td>
	</tr>
	<tr>
		<td><spring:message code="dataintegrity.checksList.columns.fail"/></td>
		<td>${results.failDirective}</td>
		<td></td>
	</tr>
	<c:if test="${results.checkPassed == true}">
		<tr>
			<td width="20%"><spring:message code="dataintegrity.results.status"/></td>
			<td align="center" bgcolor="Green" width="25%" style="font-weight: bold; color: white;"><spring:message code="dataintegrity.results.pass"/></td>
			<td width="55%"></td>
		</tr>
	</c:if>
	<c:if test="${results.checkPassed == false}">
		<tr>
			<td width="20%"><spring:message code="dataintegrity.results.status"/></td>
			<td align="center" bgcolor="Red" width="25%" style="font-weight: bold; color: white;"><spring:message code="dataintegrity.results.fail"/></td>
			<td width="55%"></td>
		</tr>
	</c:if>
	<c:if test="${results.failedRecordCount > 0}">
	<tr>
		<td colspan="3">
			<input type="hidden" value="${results.checkId}" id="<%="checkIdHidden" + checkCount%>"/>
			<a onclick="openFailedRecordsPopUp(this.id);" onmouseover="this.style.cursor='pointer'" id="<%=checkCount%>"><spring:message code="dataintegrity.results.failedRecords"/></a>
		</td>
	</tr>
	<tr>
		<td colspan="3">
			<a href="repairCheck.list?checkId=${results.checkId}" target="_blank"><spring:message code="dataintegrity.repair.failedRecords"/></a>
		</td>
	</tr>
	</c:if>
	<tr>
		<td colspan="3">
			<c:if test="${single == false}">
				<form method="post" target="_blank" onsubmit="return checkParameters(this.id);" id="<%=checkCount%>">
					<input type="hidden" value="${results.checkId}" name="checkId"/>
					<input type="hidden" value="${results.parameters}" id="<%="paramHidden" + checkCount%>"/>
					<input type="hidden" value="" id="<%="paramValueHidden" + checkCount%>" name="checkParameter${results.checkId}"/>
					<input type="submit" value="<spring:message code="dataintegrity.results.runAgain"/>"/>
				</form>
			</c:if>
			<c:if test="${single == true}">
				<form method="post" onsubmit="return checkParameters(this.id);" id="<%=checkCount%>">
					<input type="hidden" value="${results.checkId}" name="checkId"/>
					<input type="hidden" value="${results.parameters}" id="<%="paramHidden" + checkCount%>"/>
					<input type="hidden" value="" id="<%="paramValueHidden" + checkCount%>" name="checkParameter${results.checkId}"/>
					<input type="submit" value="<spring:message code="dataintegrity.results.runAgain"/>"/>
				</form>
			</c:if>
		</td>
	</tr>
</table>
<br />
<%checkCount++; %>
</c:forEach>
</c:if>

<%@ include file="/WEB-INF/template/footer.jsp" %>