<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Integrity Checks" otherwise="/login.htm" redirect="/module/dataintegrity/copyCheck.form" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="dataintegrity.copyCheck.link"/></h2>
<br />
<b class="boxHeader"><spring:message code="dataintegrity.copyCheck.title"/></b>
<form class="box" method="post">
	<c:if test="${not empty copyCheckForm}">
	<table>
		<tr>
			<td> <spring:message code="dataintegrity.copyCheck.choose"/> </td>
			<td>
				<select name="checkId" id="checkIdSelect">
					<c:forEach items="${copyCheckForm}" var="checkList">
						<option value="${checkList.integrityCheckId}">${checkList.integrityCheckName}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<br />
				<input type="submit" value="<spring:message code="dataintegrity.copyCheck.run"/>"/> 
			</td>
		</tr>
	</table>
	</c:if>
	
	<c:if test="${empty copyCheckForm}"><spring:message code="dataintegrity.checksList.empty"/></c:if>
</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>