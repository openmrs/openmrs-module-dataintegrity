<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Run Integrity Checks" otherwise="/login.htm" redirect="/admin/index.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="dataintegrity.runSingleCheck.link"/></h2>
<br />

<b class="boxHeader"><spring:message code="dataintegrity.results.title"/></b>
<form class="box" method="post">
	<table>
		<tr>
			<td>Check Name</td>
			<td>${checkResults.checkName}</td>
		</tr>
		<c:if test="${checkResults.checkPassed == true}">
		<tr>
			<td>Pass/Fail</td>
			<td bgcolor="Green"></td>
		</tr>
		</c:if>
		<c:if test="${checkResults.checkPassed == false}">
		<tr>
			<td>Pass/Fail</td>
			<td bgcolor="Red"></td>
		</tr>
		</c:if>
	</table>

</form>
<%@ include file="/WEB-INF/template/footer.jsp" %>