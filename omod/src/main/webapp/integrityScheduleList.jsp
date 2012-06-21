<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<openmrs:require privilege="Manage Integrity Checks" otherwise="/login.htm" redirect="/module/dataintegrity/integritySchedule.list" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="localHeader.jsp"%>

<!-- import jquery for openmrs 1.6.x -->
<openmrs:htmlInclude file="/scripts/jquery/jquery-1.3.2.min.js" />
 
<script type="text/javascript">
        var $j = jQuery.noConflict();
</script>
<!-- end 1.6.x required prep -->

<openmrs:htmlInclude file="/moduleResources/dataintegrity/js/jquery.showtime.js" />

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
		alert('<spring:message code="dataintegrity.schedule.taskselected" javaScriptEscape="true"/>' + obj.value);
		return false;
	}else{
		return true;
	}
}

$j(document).ready(function(){
	// convert seconds to something meaningful
	$j(".secs").showTime({ 
		div_hours:"h ", 
		div_minutes: "m ", 
		div_seconds: "s " 
	});
});

</script>

<p>
	<a href="integritySchedule.form"><spring:message code="dataintegrity.schedule.new" /></a> |
	<a href="integrityScheduleEmail.form"><spring:message code="dataintegrity.schedule.email.new" /></a>
</p>

<br />

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
				<th>Task Type</th>
				<th>Task Name</th>
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
									<i><spring:message code="dataintegrity.schedule.runagain" />
									<strong class="secs">${selectedTask.task.secondsUntilNextExecutionTime}</strong>
									</i>
								</c:if>
							</c:when>
							<c:otherwise>
								<font color="red"><strong><spring:message code="dataintegrity.schedule.stopped" /></strong></font>
							</c:otherwise>
						</c:choose>
					</td>
					<c:if test="${selectedTask.type == 'email'}">
					<td class="left">
						<spring:message code="dataintegrity.schedule.type.email"/>
					</td>
					<td class="left">
						<a href="integrityScheduleEmail.form?taskId=${selectedTask.task.id}"><strong>${selectedTask.name}</strong></a>
					</td>
					</c:if>
					<c:if test="${selectedTask.type == 'schedule'}">
					<td class="left">
						<spring:message code="dataintegrity.schedule.type.schedule"/>
					</td>
					<td class="left">
	            		<a href="integritySchedule.form?taskId=${selectedTask.task.id}"><strong>${selectedTask.name}</strong></a>
					</td>
	           		</c:if>
					<td class="left">Runs every <strong class="secs">${selectedTask.task.repeatInterval}</strong>
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