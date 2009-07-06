<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Integrity Checks" otherwise="/login.htm" redirect="/admin/index.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="dataintegrity.upload.link"/></h2><br />

<b class="boxHeader"><spring:message code="dataintegrity.upload.add" /></b>
<div class="box">
	<form id="checkAddForm" action="uploadCheck.list" method="post" enctype="multipart/form-data">
		<spring:message code="dataintegrity.upload.file"/>: 
		<input type="file" name="checkFile" size="40" /> <br /><br />
		<input type="submit" value='<spring:message code="dataintegrity.upload.btn"/>'/>
	</form>
</div>