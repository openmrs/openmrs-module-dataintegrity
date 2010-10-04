<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Integrity Tests" otherwise="/login.htm" redirect="/admin/index.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<head>
<openmrs:htmlInclude file="/moduleResources/dataintegrity/gs_sortable.js" />
</head>
<script type="text/javascript">
	var TSort_Data = new Array ('integrityCheckTable', 'i', 'h', '', '', '', '', 'h', 'h');
	tsRegister();
</script>

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
	<table id="integrityCheckTable" cellpadding="10">
		<thead>
		<tr>
			<th><spring:message code="dataintegrity.checksList.columns.id"/></th>
			<th><spring:message code="dataintegrity.checksList.columns.name"/></th>
			<th><spring:message code="dataintegrity.checksList.columns.code"/></th>
			<th><spring:message code="dataintegrity.checksList.columns.resultType"/></th>
			<th><spring:message code="dataintegrity.checksList.columns.failOp"/></th>
			<th><spring:message code="dataintegrity.checksList.columns.fail"/></th>
			<th><spring:message code="dataintegrity.checksList.columns.resultsPassed"/></th>
			<th><spring:message code="dataintegrity.checksList.columns.resultsDate"/></th>
		</tr>
		</thead>
		<c:forEach items="${dataIntegrityChecksList}" var="integrityChecksObj" varStatus="varStatus">
		<tr class="<c:choose><c:when test="${varStatus.index % 2 == 0}">oddRow</c:when><c:otherwise>evenRow</c:otherwise></c:choose>" id="${module.moduleId}">
			<td valign="top" width="20">${integrityChecksObj.integrityCheckId}</td>
			<td valign="top" width="225"><a href="integrityCheck.form?checkId=${integrityChecksObj.integrityCheckId}">${integrityChecksObj.integrityCheckName}</a></td>
			<td valign="top" width="250" height="55"><div style="overflow: auto; height: 55px;">${integrityChecksObj.integrityCheckCode}</div></td>
			<td valign="top" width="60">${integrityChecksObj.integrityCheckResultType}</td>
			<td valign="top" width="80">${integrityChecksObj.integrityCheckFailDirectiveOperator}</td>
			<td valign="top" width="80">${integrityChecksObj.integrityCheckFailDirective}</td>
			<td valign="top" width="8em" align="center"><c:if test="${not empty integrityChecksObj.latestResults}">
				<c:if test="${integrityChecksObj.latestResults.checkPassed}">
					<div style="text-align:center; background-color:green; font-weight:bold; color:white; width:8em; padding:0.5em 0;">
						<spring:message code="dataintegrity.results.pass"/>
					</div>
				</c:if>
				<c:if test="${not integrityChecksObj.latestResults.checkPassed}">
					<div style="text-align:center; background-color:red; font-weight:bold; color:white; width:8em; padding:0.5em 0;">
						<spring:message code="dataintegrity.results.fail"/>
					</div>
				</c:if>
			</c:if></td>
			<td valign="top" width="200"><c:if test="${not empty integrityChecksObj.latestResults}">
				<a href="results.list?checkId=${integrityChecksObj.id}" title="<spring:message code="dataintegrity.checksList.seeResults"/>">
					<openmrs:formatDate type="long" date="${integrityChecksObj.latestResults.dateOccurred}"/>
				</a>
			</c:if></td>
		</tr>
		</c:forEach>
	</table>
	</c:if>
	
	<c:if test="${empty dataIntegrityChecksList}"><spring:message code="dataintegrity.checksList.empty"/></c:if>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>