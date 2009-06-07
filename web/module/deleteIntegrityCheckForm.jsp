<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Integrity Checks" otherwise="/login.htm" redirect="/admin/index.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="dataintegrity.deleteCheck"/></h2>

<b class="boxHeader"><spring:message code="dataintegrity.checksList.title"/></b>
<form method="post" class="box">
	<c:if test="${not empty existingChecks}">
		<table>
			<tr>
				<th></th>
				<th align="left"> <spring:message code="dataintegrity.checksList.columns.name"/> </th>
				<th align="left"> <spring:message code="dataintegrity.checksList.columns.sql"/> </th>
				<th align="left"> <spring:message code="dataintegrity.checksList.columns.base"/> </th>
				<th align="left"> <spring:message code="dataintegrity.checksList.columns.score"/> </th>
			</tr>
			<c:forEach items="${existingChecks}" var="integrityChecksObj">
			<tr>
				<td valign="top"><input type="checkbox" name="integrityCheckId" value="${integrityChecksObj.integrityCheckId}"></td>
				<td align="left">${integrityChecksObj.integrityCheckName}</td>
				<td align="left">${integrityChecksObj.integrityCheckSql}</td>
				<td align="left">
					<c:if test="${integrityChecksObj.integrityCheckBaseForFailure == 1}"><spring:message code="dataintegrity.addeditCheck.base.all"/></c:if>
					<c:if test="${integrityChecksObj.integrityCheckBaseForFailure == 2}"><spring:message code="dataintegrity.addeditCheck.base.some"/></c:if>
				</td>
				<td align="left">${integrityChecksObj.integrityCheckScore}</td>
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