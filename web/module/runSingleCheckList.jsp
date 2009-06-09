<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Run Integrity Checks" otherwise="/login.htm" redirect="/admin/index.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="dataintegrity.runSingleCheck.link"/></h2>
<br />

<b class="boxHeader"><spring:message code="dataintegrity.runSingleCheck.title"/></b>
<form class="box" method="post">
	<c:if test="${not empty runSingleCheckList}">
	<table>
		<tr>
			<td> <spring:message code="dataintegrity.runSingleCheck.choose"/> </td>
			<td>
				<select name="checkId">
					<c:forEach items="${runSingleCheckList}" var="checkList">
						<option value="${checkList.integrityCheckId}">${checkList.integrityCheckName}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<br />
				<input type="submit" value="<spring:message code="dataintegrity.runSingleCheck.run"/>"/> 
			</td>
		</tr>
	</table>
	</c:if>
	
	<c:if test="${empty runSingleCheckList}"><spring:message code="dataintegrity.checksList.empty"/></c:if>
</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>