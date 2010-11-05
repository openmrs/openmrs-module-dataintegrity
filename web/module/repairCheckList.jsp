<%@ include file="/WEB-INF/template/include.jsp" %>
<openmrs:require privilege="Run Integrity Check Repairs" otherwise="/login.htm" redirect="/module/dataintegrity/repairCheck.list" />
<%@page import="org.openmrs.module.dataintegrity.DataIntegrityConstants"%>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<%
	pageContext.setAttribute("stack", session.getAttribute(DataIntegrityConstants.DATA_INTEGRITY_ERROR_STACK_TRACE));
  	session.removeAttribute(DataIntegrityConstants.DATA_INTEGRITY_ERROR_STACK_TRACE);
%>

<h2><spring:message code="dataintegrity.repair.title"/></h2>
<br />
<c:if test="${repairCheckNone != null}">
	<b class="boxHeader"><spring:message code="dataintegrity.repair.none"/> ${checkName}</b>
	<div class="box">
		<spring:message code="dataintegrity.repair.none1"/>
		<a href="integrityCheck.form?checkId=${checkId}"><spring:message code="dataintegrity.repair.click"/></a>
		<spring:message code="dataintegrity.repair.none2"/>
	</div>
</c:if>
<c:if test="${repairCheckInstructions != null}">
	<b class="boxHeader"><spring:message code="dataintegrity.repair.instructions"/> ${checkName}</b>
	<div class="box">${repairCheckInstructions}</div>
</c:if>
<c:if test="${repairCheckScript != null}">
	<b class="boxHeader"><spring:message code="dataintegrity.repair.script"/> ${checkName}</b>
	<c:if test="${stack == null}">
		<div class="box">${repairCheckScript}</div><br />
	</c:if>
	<c:if test="${stack != null}">
		<div class="error">${repairCheckScript}</div><br />
		<div id="openmrs_error"><spring:message code="${stack}" text="${stack}"/></div>
	</c:if>
</c:if>
<c:if test="${repairCheckLink != null}">
	<b class="boxHeader"><spring:message code="dataintegrity.repair.link"/> ${checkName}</b>
	<div class="box">${repairCheckLink}</div>
</c:if>
<%@ include file="/WEB-INF/template/footer.jsp" %>