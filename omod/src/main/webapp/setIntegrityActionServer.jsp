<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Integrity Checks" otherwise="/login.htm" redirect="/module/dataintegrity/setIntegrityActionServer.htm"/>

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<br />
<h2><spring:message code="dataintegrity.actionServer.title"/></h2>
<br />

<openmrs:portlet
    url="globalProperties"
    parameters="title=${title}|propertyPrefix=dataintegrity.actionServerUrl"/>
    
<br />
<spring:message code="dataintegrity.actionServer.instruction"/>
<br />
<br />

<%@ include file="/WEB-INF/template/footer.jsp" %>