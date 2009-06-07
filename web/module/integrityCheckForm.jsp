<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Integrity Checks" otherwise="/login.htm" redirect="/admin/index.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="dataintegrity.addeditCheck"/></h2>

<script type="text/javascript">
	function enableScore() {
		var selectElement = document.getElementById("baseSelect");
		var row = document.getElementById("scoreRow");
		if (selectElement.selectedIndex == 0) {
			row.style.display = 'none';
		} else {
			row.style.display = '';
		}
	}

	function isNumberKey(evt)
    {
       var charCode = (evt.which) ? evt.which : event.keyCode
       if (charCode > 31 && (charCode < 48 || charCode > 57))
          return false;

       return true;
    }
</script>
<form method="post">
	<c:if test="${empty existingCheck}">
		<table>
			<tr>
				<td><spring:message code="dataintegrity.checksList.columns.name"/></td>
				<td><input type="text" name="name" value="" size="50" maxlength="45"/></td>
				<td><i><spring:message code="dataintegrity.checksList.columns.name.help"/></i></td>
			</tr>
			<tr>
				<td valign="top"><spring:message code="dataintegrity.checksList.columns.sql"/></td>
				<td><textarea rows="5" cols="50" name="sql"></textarea></td>
				<td><i><spring:message code="dataintegrity.checksList.columns.sql.help"/></i></td>
			</tr>
			<tr>
				<td valign="top"><spring:message code="dataintegrity.checksList.columns.base"/></td>
				<td>
					<select name="base" onchange="enableScore()" id="baseSelect">
						<option value="1"><spring:message code="dataintegrity.addeditCheck.base.all"/></option>
						<option value="2"><spring:message code="dataintegrity.addeditCheck.base.some"/></option>
					</select>
				</td>
				<td><i><spring:message code="dataintegrity.checksList.columns.base.help"/></i></td>
			</tr>
			<tr id="scoreRow" style="display: none">
				<td><spring:message code="dataintegrity.checksList.columns.score"/></td>
				<td>
					<input type="text" name="score" size="5" id="scoreTxt" onkeypress="return isNumberKey(event)"/>
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
				<td valign="top"><spring:message code="dataintegrity.checksList.columns.base"/></td>
				<td>
					<select name="base" onchange="enableScore()" id="baseSelect">
						<option value="1" <c:if test="${existingCheck.integrityCheckBaseForFailure == 1}">selected</c:if>><spring:message code="dataintegrity.addeditCheck.base.all"/></option>
						<option value="2" <c:if test="${existingCheck.integrityCheckBaseForFailure == 2}">selected</c:if>><spring:message code="dataintegrity.addeditCheck.base.some"/></option>
					</select>
				</td>
				<td><i><spring:message code="dataintegrity.checksList.columns.base.help"/></i></td>
			</tr>
			<c:if test="${existingCheck.integrityCheckBaseForFailure == 1}">
				<tr id="scoreRow" style="display: none">
					<td><spring:message code="dataintegrity.checksList.columns.score"/></td>
					<td>
						<input type="text" name="score" size="5" id="scoreTxt" onkeypress="return isNumberKey(event)" value=""/>
					</td>
					<td><i><spring:message code="dataintegrity.checksList.columns.score.help"/></i></td>
				</tr>
			</c:if>
			<c:if test="${existingCheck.integrityCheckBaseForFailure == 2}">
				<tr id="scoreRow">
					<td><spring:message code="dataintegrity.checksList.columns.score"/></td>
					<td>
						<input type="text" name="score" size="5" id="scoreTxt" onkeypress="return isNumberKey(event)" value="${existingCheck.integrityCheckScore}"/>
					</td>
					<td><i><spring:message code="dataintegrity.checksList.columns.score.help"/></i></td>
				</tr>
			</c:if>
			<tr>
				<td>
					<input type="hidden" name="checkId" value="${existingCheck.integrityCheckId}" />
					<input type="submit" value="<spring:message code="dataintegrity.addeditCheck.save"/>"/>
				</td>
			</tr>
		</table>
	</c:if>
</form>
<br/>

<b class="boxHeader"><spring:message code="dataintegrity.addeditCheck.help" /></b>
<div class="box">
	<ul>
		<li><i><spring:message code="dataintegrity.addeditCheck.help.query"/></i>
		<li><i><spring:message code="dataintegrity.addeditCheck.help.basis1"/></i>
		<li><i><spring:message code="dataintegrity.addeditCheck.help.basis2"/></i>
		<li><i><spring:message code="dataintegrity.addeditCheck.help.score"/></i>
	</ul>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>