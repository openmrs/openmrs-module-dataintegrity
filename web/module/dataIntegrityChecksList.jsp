<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Integrity Tests" otherwise="/login.htm" redirect="/admin/index.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="dataintegrity.manage.title"/></h2>
<a href="integrityCheck.form"><spring:message code="dataintegrity.addCheck"/></a> 
<c:if test="${not empty dataIntegrityChecksList}">
<a href="deleteIntegrityCheck.form"><spring:message code="dataintegrity.deleteCheck"/></a>
</c:if>
<br /><br />

<b class="boxHeader"><spring:message code="dataintegrity.checksList.title"/></b>
<div class="box">
	<c:if test="${not empty dataIntegrityChecksList}">
	<table>
		<tr>
			<th> <spring:message code="dataintegrity.checksList.columns.name"/> </th>
			<th> <spring:message code="dataintegrity.checksList.columns.sql"/> </th>
			<th> <spring:message code="dataintegrity.checksList.columns.score"/> </th>
		</tr>
		<c:forEach items="${dataIntegrityChecksList}" var="integrityChecksObj">
		<tr>
			<td align="left"><a href="integrityCheck.form?checkId=${integrityChecksObj.integrityCheckId}">${integrityChecksObj.integrityCheckName}</a></td>
			<td align="left">${integrityChecksObj.integrityCheckSql}</td>
			<td align="center">${integrityChecksObj.integrityCheckScore}</td>
		</tr>
		</c:forEach>
	</table>
	</c:if>
	
	<c:if test="${empty dataIntegrityChecksList}"><spring:message code="dataintegrity.checksList.empty"/></c:if>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>