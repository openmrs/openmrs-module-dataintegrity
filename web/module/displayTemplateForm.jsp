<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="View Hello World" otherwise="/login.htm" redirect="/module/helloworld/viewHelloWorld.htm" />
	
<%@ include file="/WEB-INF/template/header.jsp" %>

<br/>
<h2>Add New Template</h2>
<form method="post" action="${pageContext.request.contextPath}/moduleServlet/dataintegrity/template">

	<spring:message code="dataintegrity.addTemplate"/>: 
	<input type="text" name="tempName" value="" />
	
	<br/><br/>
	
	<input type="submit" value="<spring:message code="dataintegrity.submit" />" />
</form>
<br/>

<h2>Existing Templates</h2>
<table border="1">
<tr>
<td>Id</td><td>Name</td><td>SQL</td><td>Score</td>
</tr>
<c:forEach items="${integrityTemplates}" var="templateObj">
	<tr>
		<td>${templateObj.integrityCheckId}</td>
		<td>${templateObj.integrityCheckName}</td>
		<td>${templateObj.integrityCheckSql}</td>
		<td>${templateObj.integrityCheckScore}</td>
	</tr>
</c:forEach>
</table>
<br/>

<%@ include file="/WEB-INF/template/footer.jsp" %>