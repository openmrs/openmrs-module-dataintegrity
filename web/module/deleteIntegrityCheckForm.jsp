<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Integrity Tests" otherwise="/login.htm" redirect="/admin/index.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="dataintegrity.deleteCheck"/></h2>

<b class="boxHeader"><spring:message code="dataintegrity.checksList.title"/></b>
<form method="post" class="box">
	<c:if test="${not empty existingChecks}">
		<table>
			<tr>
				<th></th>
				<th> <spring:message code="dataintegrity.checksList.columns.name"/> </th>
				<th> <spring:message code="dataintegrity.checksList.columns.sql"/> </th>
				<th> <spring:message code="dataintegrity.checksList.columns.score"/> </th>
			</tr>
			<c:forEach items="${existingChecks}" var="integrityChecksObj">
			<tr>
				<td valign="top"><input type="checkbox" name="integrityCheckId" value="${integrityChecksObj.integrityCheckId}"></td>
				<td align="left">${integrityChecksObj.integrityCheckName}</td>
				<td align="left">${integrityChecksObj.integrityCheckSql}</td>
				<td align="center">${integrityChecksObj.integrityCheckScore}</td>
			</tr>
			</c:forEach>
			<tr>
				<td colspan="4"><input type="submit" value='<spring:message code="dataintegrity.deleteCheck.delete.button"/>'></td>
			</tr>
		</table>
	</c:if>
	<c:if test="${empty existingChecks}"><spring:message code="dataintegrity.checksList.empty"/></c:if>
</form>




<%@ include file="/WEB-INF/template/footer.jsp" %>