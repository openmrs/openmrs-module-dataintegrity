<%@ include file="/WEB-INF/template/include.jsp" %>
<openmrs:require privilege="View Integrity Checks" otherwise="/login.htm" redirect="/module/dataintegrity/results.list" />
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

	function openFailedRecordsPopUp(buttonId) {
		var url = document.location.href;
		var components = url.split('/');
		var last = components[components.length - 1];
		var hiddenId = "checkIdHidden" + buttonId;
		var checkId = document.getElementById(hiddenId).value;
		var newUrl = url.replace(last, 'failedRecords.list') + '?checkId=' + checkId;
	   	window.open(newUrl, 'Failed_Records', 'width=600,height=400,menubar=no,status=no,location=no,toolbar=no,scrollbars=yes');
	}
		
	function millisToEnglish(millis) {
		hours = Math.floor(millis/3600000);
		millis -= hours*3600000;
		minutes = Math.floor(millis/60000);
		millis -= minutes*60000;
		seconds = millis/1000;
		out = "";
		if (hours > 0)
			out += hours + " hours, ";
		if (hours > 0 || minutes > 0)
			out += minutes + " minutes, ";
		if (hours > 0 || minutes > 0 || seconds > 0)
			out += seconds + " seconds";
		return out;
	}
</script>

<br />
<c:if test="${not empty checkResults}">
<%int checkCount = 0; %>
<c:forEach items="${checkResults}" var="results">
<b class="boxHeader">${results.integrityCheck.name} <spring:message code="dataintegrity.results.results"/></b>
<table class="box">
	<tr>
		<td width="20%"><spring:message code="dataintegrity.results.count"/></td>
		<td width="25%">${results.failedRecordCount}</td>
		<td width="55%"></td>
	</tr>
	<tr>
		<td><spring:message code="dataintegrity.checksList.columns.failOp"/></td>
		<td>${results.integrityCheck.failDirectiveOperator}</td>
		<td></td>
	</tr>
	<tr>
		<td><spring:message code="dataintegrity.checksList.columns.fail"/></td>
		<td>${results.integrityCheck.failDirective}</td>
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
	<c:if test="${not empty results.dateOccurred}">
		<tr>
			<td width="20%"><spring:message code="dataintegrity.results.date"/></td>
			<td width="75%" colspan="3"><openmrs:formatDate type="long" date="${results.dateOccurred}"/></td>
		</tr>
	</c:if>
	<c:if test="${not empty results.duration}">
		<tr>
			<td width="20%"><spring:message code="dataintegrity.results.duration"/></td>
			<td width="75%" colspan="3"><script type="text/javascript">document.write(millisToEnglish(${results.duration}));</script></td>
		</tr>
	</c:if>
	<c:if test="${results.failedRecordCount > 0}">
	<openmrs:hasPrivilege privilege="View Integrity Check Results">
	<tr>
		<td colspan="3">
			<input type="hidden" value="${results.integrityCheck.id}" id="<%="checkIdHidden" + checkCount%>"/>
			<a onclick="openFailedRecordsPopUp(this.id);" onmouseover="this.style.cursor='pointer'" id="<%=checkCount%>"><spring:message code="dataintegrity.results.failedRecords"/></a>
		</td>
	</tr>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="Run Integrity Check Repairs">
	<tr>
		<td colspan="3">
			<a href="repairCheck.list?checkId=${results.integrityCheck.id}" target="_blank"><spring:message code="dataintegrity.repair.failedRecords"/></a>
		</td>
	</tr>
	</openmrs:hasPrivilege>
	</c:if>
	<openmrs:hasPrivilege privilege="Run Integrity Checks">
	<tr>
		<td colspan="3">
			<form method="post" target="<c:out value="${single == true ? '' : '_blank'}"/>" onsubmit="return checkParameters(this.id);" id="<%=checkCount%>">
				<input type="hidden" value="${results.integrityCheck.id}" name="checkId"/>
				<input type="hidden" value="${results.integrityCheck.repairParameters}" id="<%="paramHidden" + checkCount%>"/>
				<input type="hidden" value="" id="<%="paramValueHidden" + checkCount%>" name="checkParameter${results.integrityCheck.id}"/>
				<input type="submit" value="<spring:message code="dataintegrity.results.runAgain"/>"/>
			</form>
		</td> 	
	</tr>
	</openmrs:hasPrivilege>
</table>
<br />
<%checkCount++; %>
</c:forEach>
</c:if>

<%@ include file="/WEB-INF/template/footer.jsp" %>