<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Run Integrity Checks" otherwise="/login.htm" redirect="/admin/index.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="dataintegrity.runSingleCheck.link"/></h2>
<br />

<%
	String name = (String)session.getAttribute("name");
	pageContext.setAttribute("name", name);
	session.removeAttribute("name");
%>
<p>This is my message ${name}</p>

<%@ include file="/WEB-INF/template/footer.jsp" %>