<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Run Integrity Checks" otherwise="/login.htm" redirect="/admin/index.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="dataintegrity.results.title"/></h2>
<br />
<c:if test="${not empty checkResults}">
<b class="boxHeader">${checkResults.checkName} <spring:message code="dataintegrity.results.results"/></b>
<table class="box">
	<c:if test="${checkResults.checkPassed == true}">
		<tr>
			<td width="20%"><spring:message code="dataintegrity.results.count"/></td>
			<td width="25%">${checkResults.failedRecordCount}</td>
			<td width="55%"></td>
		</tr>
		<tr>
			<td width="20%"><spring:message code="dataintegrity.results.status"/></td>
			<td bgcolor="Green" width="25%"></td>
			<td width="55%"></td>
		</tr>
		<!--  
		<tr>
			<td width="20%"><spring:message code="dataintegrity.results.records"/></td>
			<td width="25%"></td>
			<td width="55%"></td>
		</tr>
		-->
	</c:if>
	<c:if test="${checkResults.checkPassed == false}">
		<tr>
			<td width="20%"><spring:message code="dataintegrity.results.count"/></td>
			<td width="25%">${checkResults.failedRecordCount}</td>
			<td width="55%"></td>
		</tr>
		<tr>
			<td width="20%"><spring:message code="dataintegrity.results.status"/></td>
			<td bgcolor="Red" width="25%"></td>
			<td width="55%"></td>
		</tr>
		<!--  
		<tr>
			<td width="20%"><spring:message code="dataintegrity.results.records"/></td>
			<td width="25%"></td>
			<td width="55%"></td>
		</tr>
		-->
	</c:if>
</table>
</c:if>

<%@ include file="/WEB-INF/template/footer.jsp" %>