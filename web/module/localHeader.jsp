<ul id="menu">
	<li class="first">
		<a href="${pageContext.request.contextPath}/admin"><spring:message code="admin.title.short"/></a>
	</li>
	<openmrs:hasPrivilege privilege="Manage Integrity Checks">
		<li <c:if test='<%= request.getRequestURI().contains("dataIntegrityChecks") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/dataintegrity/dataIntegrityChecks.list">
				<spring:message code="dataintegrity.manage.link"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
</ul>