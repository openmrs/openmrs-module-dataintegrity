<%@ include file="/WEB-INF/template/include.jsp" %>


<%@page import="org.openmrs.module.dataintegrity.DataIntegrityCheckResultTemplate"%>
<%@page import="java.util.List"%><openmrs:require privilege="Run Integrity Checks" otherwise="/login.htm" redirect="/admin/index.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="dataintegrity.results.title"/></h2>
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
		<td colspan="3"><spring:message code="dataintegrity.results.records"/></td>
	</tr>
	<tr>
		<td colspan="3">
			<table>
				<c:forEach items="${results.failedRecords}" var="failedRecord">
				<tr>
					<c:forEach items="${failedRecord}" var="record">
						<td>${record}</td>
					</c:forEach>
				</tr>
				</c:forEach>
			</table>
		</td>
	</tr>
	</c:if>
	<tr>
		<td colspan="3">
			<c:if test="${single == false}">
				<form method="post" target="_blank">
					<input type="hidden" value="${results.checkId}" name="checkId" />
					<input type="submit" value="<spring:message code="dataintegrity.results.runAgain"/>"/>
				</form>
			</c:if>
			<c:if test="${single == true}">
				<form method="post">
					<input type="hidden" value="${results.checkId}" name="checkId" />
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