<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Integrity Tests" otherwise="/login.htm" redirect="/admin/index.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="dataintegrity.manage.title"/></h2>
<a href="integrityCheck.form"><spring:message code="dataintegrity.addCheck"/></a> 
<c:if test="${not empty dataIntegrityChecksList}">
<a href="deleteIntegrityCheck.form"><spring:message code="dataintegrity.deleteCheck"/></a>
<a href="copyCheck.form"><spring:message code="dataintegrity.copyCheck"/></a>
</c:if>
<br /><br />

<b class="boxHeader"><spring:message code="dataintegrity.checksList.title"/></b>
<div class="box">
	<c:if test="${not empty dataIntegrityChecksList}">
	<table>
		<tr>
			<th><spring:message code="dataintegrity.checksList.columns.name"/></th>
			<th><spring:message code="dataintegrity.checksList.columns.checkType"/></th>
			<th><spring:message code="dataintegrity.checksList.columns.code"/></th>
			<th><spring:message code="dataintegrity.checksList.columns.resultType"/></th>
			<th><spring:message code="dataintegrity.checksList.columns.failOp"/></th>
			<th><spring:message code="dataintegrity.checksList.columns.fail"/></th>
			<th><spring:message code="dataintegrity.checksList.columns.repairType"/></th>
			<th><spring:message code="dataintegrity.checksList.columns.repair"/></th>
			<th><spring:message code="dataintegrity.checksList.columns.parameters"/></th>
		</tr>
		<c:forEach items="${dataIntegrityChecksList}" var="integrityChecksObj" varStatus="varStatus">
		<tr class="<c:choose><c:when test="${varStatus.index % 2 == 0}">oddRow</c:when><c:otherwise>evenRow</c:otherwise></c:choose>" id="${module.moduleId}">
			<td valign="top"><a href="integrityCheck.form?checkId=${integrityChecksObj.integrityCheckId}">${integrityChecksObj.integrityCheckName}</a></td>
			<td valign="top">${integrityChecksObj.integrityCheckType}</td>
			<td valign="top" width="20%" height="40"><div style="overflow: auto; height: 40px;">${integrityChecksObj.integrityCheckCode}</div></td>
			<td valign="top">${integrityChecksObj.integrityCheckResultType}</td>
			<td valign="top">${integrityChecksObj.integrityCheckFailDirectiveOperator}</td>
			<td valign="top">${integrityChecksObj.integrityCheckFailDirective}</td>
			<td valign="top">${integrityChecksObj.integrityCheckRepairType}</td>
			<td valign="top">${integrityChecksObj.integrityCheckRepairDirective}</td>
			<td valign="top">${integrityChecksObj.integrityCheckParameters}</td>
		</tr>
		</c:forEach>
	</table>
	</c:if>
	
	<c:if test="${empty dataIntegrityChecksList}"><spring:message code="dataintegrity.checksList.empty"/></c:if>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>