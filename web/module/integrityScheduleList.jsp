<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<openmrs:require privilege="Manage Integrity Checks" otherwise="/login.htm" redirect="/module/dataintegrity/IntegritySchedule.list" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="localHeader.jsp"%>

<h2><spring:message code="dataintegrity.title" /></h2>

<script type="text/javascript">

function validate(obj){
	var check = document.getElementsByName("taskId");
	var count = 0;
	for(var i=0;i<check.length;i++){
		if(check[i].checked == true){
			++count;
		}
	}

	if(count <= 0){
		alert("<spring:message code="dataintegrity.schedule.taskselected" />"+obj.value);
		return false;
	}else{
		return true;
	}
}

</script>

<p><a href="integritySchedule.form"><spring:message code="dataintegrity.schedule.new" /></a></p>

<b class="boxHeader"><spring:message code="dataintegrity.schedule.list" /></b>

<c:if test="${fn:length(allTasks) == 0}">
	<div class="box">
		<p style="text-align: center;"><em><spring:message code="dataintegrity.schedule.none" /></em></p>
	</div>
</c:if>

<c:if test="${fn:length(allTasks) > 0}">
	<form id="scheduleForm" method="post" class="box">
		<table cellpadding="4" cellspacing="2" width="98%">
			<tr>
				<th></th>
				<th>Status</th>
				<th>Tasks</th>
				<th>Schedule</th>
				<th>Last Execution</th>
			</tr>
			<c:forEach items="${allTasks}" var="selectedTask" varStatus="taskIndex">
				<c:set var="rowColor" value="oddRow" />
				<c:if test="${taskIndex.index % 2 == 0}">
					<c:set var="rowColor" value="evenRow" />
				</c:if>
				<tr class="${rowColor}">
					<td>
						<input type="checkbox" size="3" name="taskId" value="${selectedTask.task.id}" />
					</td>
					<td>
						<c:choose>
							<c:when test="${selectedTask.task.started}">
								<font color="green"><strong>Started</strong></font>
								<br>
								<c:if test="${selectedTask.task.startTime!=null}">
									<i><spring:message code="dataintegrity.schedule.runagain" /><strong>${selectedTask.task.secondsUntilNextExecutionTime}s</strong></i>
								</c:if>
							</c:when>
							<c:otherwise>
								<font color="red"><strong><spring:message
									code="dataintegrity.schedule.stopped" /></strong></font>
							</c:otherwise>
						</c:choose>
					</td>
					<td class="left"><a
						href="integritySchedule.form?taskId=${selectedTask.task.id}"><strong>${selectedTask.name}</strong></a></td>
					<td class="left">Runs every <strong>${intervals[selectedTask.task]}</strong>
					<c:if test="${selectedTask.task.startTime!=null}">
						<fmt:formatDate var="taskStartTime" pattern="hh:mm:ssa" value="${selectedTask.task.startTime}" />
						<fmt:formatDate var="taskStartDate" pattern="MMM dd yyyy" value="${selectedTask.task.startTime}" />							 	
						 	 from <strong>${taskStartTime}</strong>, 
						 	<br />starting on <strong>${taskStartDate}</strong>
					</c:if></td>
					<td class="left"><openmrs:formatDate
						date="${selectedTask.task.lastExecutionTime}" type="long" /></td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="6" align="center">
					<input type="submit" value="<spring:message code="Scheduler.taskList.start"/>" name="action" onclick="return validate(this);"> 
					<input type="submit" value="<spring:message code="Scheduler.taskList.stop"/>" name="action" onclick="return validate(this);"> 
					<input type="submit" value="<spring:message code="Scheduler.taskList.delete"/>" name="action" onclick="return validate(this);">
				</td>
			</tr>
		</table>
	</form>
</c:if>
<br />
<%@ include file="/WEB-INF/template/footer.jsp"%>