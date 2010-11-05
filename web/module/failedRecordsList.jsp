<%@ include file="/WEB-INF/template/include.jsp" %>
<openmrs:require privilege="View Integrity Check Results" otherwise="/login.htm" redirect="/module/dataintegrity/failedRecords.list" />
<%@ include file="/WEB-INF/template/header.jsp" %>

<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>

<h2><spring:message code="dataintegrity.results.failedRecords"/></h2>
<br />
<table class="box">
<%
int checkId = -1;
Map<Integer, List<Object[]>> recordMap = null;
if (request.getParameter("checkId") != null) {
	checkId = Integer.parseInt(request.getParameter("checkId"));
}

if (session.getAttribute("failedRecords") != null && checkId != -1) {
	recordMap = (Map<Integer, List<Object[]>>) session.getAttribute("failedRecords");
	List<Object[]> records = recordMap.get(checkId);
	String rowStyle = "";
	for (int rowIndex=0; rowIndex<records.size(); rowIndex++) {
		if (rowIndex % 2 == 0) {
			rowStyle = "evenRow";
		} else {
			rowStyle = "oddRow";
		}
		%> <tr class="<%=rowStyle %>"> <%
		Object[] rec = records.get(rowIndex);
		for (int columnIndex=0; columnIndex<rec.length; columnIndex++) {
			%> <td><%=rec[columnIndex]%></td> <%
		}
		%> </tr> <%
	}
}
%>
</table>
<%@ include file="/WEB-INF/template/footer.jsp" %>
