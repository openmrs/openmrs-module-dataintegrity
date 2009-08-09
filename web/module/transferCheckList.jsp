<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Integrity Checks" otherwise="/login.htm" redirect="/admin/index.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="dataintegrity.upload.link"/></h2><br />

<script type="text/javascript">
	function checkAll(field)
	{
		for (i = 0; i < field.length; i++) {
			field[i].checked = true ;
		}
	}
	
	function uncheckAll(field)
	{
		for (i = 0; i < field.length; i++) {
			field[i].checked = false ;
		}
	}

</script>

<b class="boxHeader"><spring:message code="dataintegrity.upload.add" /></b>
<div class="box">
	<form id="checkAddForm" action="transferCheck.list" method="post" enctype="multipart/form-data">
		<spring:message code="dataintegrity.upload.file"/>: 
		<input type="file" name="checkFile" size="40" /> <br /><br />
		<input type="submit" value='<spring:message code="dataintegrity.upload.btn"/>'/>
	</form>
</div>
<br />
<b class="boxHeader"><spring:message code="dataintegrity.upload.export" /></b>
<form method="post" class="box" name="exportForm">
	<c:if test="${not empty existingChecks}">
		<table>
			<tr>
				<th></th>
				<th align="left"><spring:message code="dataintegrity.checksList.columns.name"/></th>
				<th></th>
			</tr>
			<c:forEach items="${existingChecks}" var="integrityChecksObj">
			<tr>
				<td valign="top"><input type="checkbox" name="integrityCheckId" value="${integrityChecksObj.integrityCheckId}"></td>
				<td align="left" colspan="2">${integrityChecksObj.integrityCheckName}</td>
			</tr>
			</c:forEach>
			<tr>
				<td colspan="3">
					<table><tr>
						<td>
							<a href="#" onclick="checkAll(document.exportForm.integrityCheckId)"><spring:message code="dataintegrity.runMultipleChecks.selectAll"/></a>
						</td>
						<td>
							<a href="#" onclick="uncheckAll(document.exportForm.integrityCheckId)"><spring:message code="dataintegrity.runMultipleChecks.selectNone"/></a>
						</td>
					</tr></table>
				</td>
			</tr>
			<tr>
				<td colspan="2"><br /><spring:message code="dataintegrity.upload.exportFile"/></td>
				<td><input type="text" name="fileName" value="">.zip</td>
			</tr>
			<tr>
				<td colspan="3"><input type="submit" value='<spring:message code="dataintegrity.upload.exportBtn"/>'></td>
			</tr>
		</table>
	</c:if>
	<c:if test="${empty existingChecks}"><spring:message code="dataintegrity.checksList.empty"/></c:if>
</form>