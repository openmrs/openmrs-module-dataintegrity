<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Add/Edit Integrity Tests" otherwise="/login.htm" redirect="/admin/index.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="dataintegrity.addeditCheck"/></h2>

<script type="text/javascript">
	function changeScore(operation) {
		var score = document.getElementById('scoreTxt');
		if (operation == "increase") {
			if (Number(score.value) != 10) {
				score.value = Number(score.value) + 0.5;
			}
		} else {
			if (Number(score.value) != 0) {
				score.value = Number(score.value) - 0.5;
			}
		}
	}
</script>
<form method="post">
	<c:if test="${empty existingCheck}">
		<table>
			<tr>
				<td><spring:message code="dataintegrity.checksList.columns.name"/></td>
				<td><input type="text" name="name" value="" size="50" /></td>
				<td><i><spring:message code="dataintegrity.checksList.columns.name.help"/></i></td>
			</tr>
			<tr>
				<td valign="top"><spring:message code="dataintegrity.checksList.columns.sql"/></td>
				<td><textarea rows="5" cols="50" name="sql"></textarea></td>
				<td><i><spring:message code="dataintegrity.checksList.columns.sql.help"/></i></td>
			</tr>	
			<tr>
				<td><spring:message code="dataintegrity.checksList.columns.score"/></td>
				<td>
					<input type="text" name="score" size="5" id="scoreTxt" readonly="true" value="0"/>
					<input type="button" onclick="changeScore('increase')" value="+"/>
					<input type="button" onclick="changeScore('decrease')" value="-" />
				</td>
				<td><i><spring:message code="dataintegrity.checksList.columns.score.help"/></i></td>
			</tr>
			<tr>
				<td>
					<input type="submit" value="<spring:message code="dataintegrity.addeditCheck.save"/>"/>
				</td>
			</tr>
		</table>
	</c:if>
	<c:if test="${not empty existingCheck}">
		<table>
			<tr>
				<td><spring:message code="dataintegrity.checksList.columns.name"/></td>
				<td><input type="text" name="name" value="${existingCheck.integrityCheckName}" size="50" /></td>
				<td><i><spring:message code="dataintegrity.checksList.columns.name.help"/></i></td>
			</tr>
			<tr>
				<td valign="top"><spring:message code="dataintegrity.checksList.columns.sql"/></td>
				<td><textarea rows="5" cols="47" name="sql">${existingCheck.integrityCheckSql}</textarea></td>
				<td><i><spring:message code="dataintegrity.checksList.columns.sql.help"/></i></td>
			</tr>	
			<tr>
				<td><spring:message code="dataintegrity.checksList.columns.score"/></td>
				<td>
					<input type="text" name="score" size="5" value="${existingCheck.integrityCheckScore}" id="scoreTxt" readonly="true" value="0"/>
					<input type="button" onclick="changeScore('increase')" value="+" />
					<input type="button" onclick="changeScore('decrease')" value="-" />
				</td>
				<td><i><spring:message code="dataintegrity.checksList.columns.score.help"/></i></td>
			</tr>
			<tr>
				<td>
					<input type="hidden" name="checkId" value="${existingCheck.integrityCheckId}" />
					<input type="submit" value="<spring:message code="dataintegrity.addeditCheck.save"/>"/>
				</td>
			</tr>
		</table>
	</c:if>
</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>