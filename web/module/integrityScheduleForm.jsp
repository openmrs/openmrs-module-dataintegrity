<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Integrity Checks" otherwise="/login.htm" redirect="/module/dataintegrity/IntegritySchedule.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="localHeader.jsp"%>

<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />
<openmrs:htmlInclude file="/moduleResources/dataintegrity/jquery-ui-timepicker-addon.min.js" />

<h2><spring:message code="dataintegrity.schedule.title" /></h2>

<br />

<script type="text/javascript">
window.onload = blockListDisplay;

function blockListDisplay() {
	var selected = "${integrityCheckId}";
	if(selected != "") {
		var listSelected = selected.split(",");
		for(var i=0;i<listSelected.length-1;i++){
			document.getElementById(listSelected[i]-1).checked = true;
		}
	}
}

function checkAll(field) {
	for (i = 0; i < field.length; i++) {
		field[i].checked = true ;
	}
}

function uncheckAll(field) {
	for (i = 0; i < field.length; i++) {
		field[i].checked = false ;
	}
}

function trim(myString) {
	return myString.replace(/^ +/,'').replace(/ +$/,'');
}

function validate() {
	
	var count = 0;
	var check = document.getElementsByName("integrityCheckIdChk");
	var startTime = document.getElementById("startTime").value;
	var repeatInterval = trim(document.getElementById("repeatInterval").value);
	var reg1 = /[0-1][0-9]\/[0-3][0-9]\/[0-9]{4} [0-2][0-9]:[0-6][0-9]:[0-6][0-9]/;
	var reg2 = /^[1-9][0-9]*/;
	if(repeatInterval != ""){
		var interval = reg2.exec(repeatInterval);
		document.getElementById("intervalError1").style.display = "none";
	}else if(repeatInterval == ""){
		interval = repeatInterval; 
		document.getElementById("intervalError1").style.display = "";
		++count;
	}
	var selected = false;
	for(var i=0;i<check.length;i++){
		if(check[i].checked==true){
			selected = true;
		}
	}
	
	if(trim(document.getElementById("name").value)==""){
		document.getElementById("nameError").style.display = "";
		++count;
	}else{
		document.getElementById("nameError").style.display = "none";
	}

	if(trim(document.getElementById("description").value)==""){
		document.getElementById("descriptionError").style.display = "";
		++count;
	}else{
		document.getElementById("descriptionError").style.display = "none";
	}

	if(!selected){
		document.getElementById("blockListError").style.display = "";
		++count;
	}else{
		document.getElementById("blockListError").style.display = "none";
	}
	
	if(!(reg1.test(startTime))){
		document.getElementById("startTimeError").style.display = "";
		++count;
	}else{
		document.getElementById("startTimeError").style.display = "none";
	}

	if(interval==null||interval!=repeatInterval){
		document.getElementById("intervalError2").style.display = "";
		++count;
	}else{
		document.getElementById("intervalError2").style.display = "none";
	}

	if(count>0){
		return false;
	}else{
		return true;
	}
	 
}

$j(document).ready(function(){
	// configure a date time picker for the start time field
	$j("#startTime").datetimepicker({ 
		ampm: false, 
		timeFormat: 'hh:mm:ss', 
		showSecond: true, 
		hourGrid: 6, 
		minuteGrid: 10, 
		secondGrid: 10 
	});
});

</script>

<b class="boxHeader"><spring:message
	code="dataintegrity.schedule.new" /></b>
<div class="box">
	<form method="post" name="multipleCheckForm">
		<input type="hidden" name="taskId" value="" />
		<table cellspacing="2">
			<tr>
				<th colspan="2"><spring:message
					code="dataintegrity.schedule.parameter" /></th>
			</tr>
			<tr>
				<td><spring:message code="dataintegrity.schedule.name" />:</td>
				<td><input type="text" name="name" size="30" id="name"
					value="${name}" />&nbsp;
				<div class="error" id="nameError" style="display: none"><spring:message
					code="dataintegrity.schedule.nameerror" /></div>
				</td>
			</tr>
			<tr>
				<td><spring:message code="general.description" />:</td>
				<td><textarea name="description" id="description" rows="3"
					cols="60">${description}</textarea>&nbsp;
				<div class="error" id="descriptionError" style="display: none"><spring:message
					code="dataintegrity.schedule.descriptionerror" /></div>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div>
						<b><spring:message code="dataintegrity.schedule.blocking" /></b>
						<div class="error" id="blockListError" style="display: none">
							<spring:message code="dataintegrity.schedule.blocklisterror" />
						</div>
					</div>
					<ol>
						<%int checkCount = 0; %>
						<c:forEach items="${runMultipleChecksList}"
							var="integrityChecksObj">
							<input type="checkbox" id="<%=checkCount%>"
									name="integrityCheckIdChk"
									value="${integrityChecksObj.integrityCheckId}">
							<label for="<%= checkCount %>">${integrityChecksObj.integrityCheckName}</label>
							<% checkCount++; %>
						</c:forEach>
					</ol>
					<div>
						<a href="#" onclick="checkAll(document.multipleCheckForm.integrityCheckIdChk)"><spring:message
							code="dataintegrity.runMultipleChecks.selectAll" /></a> |
						<a href="#"	onclick="uncheckAll(document.multipleCheckForm.integrityCheckIdChk)"><spring:message
							code="dataintegrity.runMultipleChecks.selectNone" /></a>
					</div>
				</td>
			</tr>
			<tr>
				<td><spring:message
					code="Scheduler.scheduleForm.startTimePattern" />:</td>
				<td><input type="text" id="startTimePattern"
					name="startTimePattern" size="25" value="${startTimePattern}"
					disabled /></td>
			</tr>
			<tr>
				<td><spring:message code="dataintegrity.schedule.date" />:</td>
				<td><input type="text" id="startTime" name="startTime" size="25"
					value="${startTime}" /> &nbsp;
				<div class="error" id="startTimeError" style="display: none"><spring:message
					code="dataintegrity.schedule.starttimeerror" /></div>
				</td>
			</tr>
			<tr>
				<td><spring:message code="dataintegrity.schedule.repeatinterval" /></td>
				<td><input type="text" id="repeatInterval" name="repeatInterval"
					size="10" value="${repeatInterval}" /> &nbsp;<select
					name="repeatIntervalUnits">
					<option value="days"
						<c:if test="${repeatIntervalUnits=='days'}">selected</c:if>>days</option>
					<option value="weeks"
						<c:if test="${repeatIntervalUnits=='weeks'}">selected</c:if>>weeks</option>
				</select>&nbsp;
				<div class="error" id="intervalError1" style="display: none"><spring:message
					code="dataintegrity.schedule.intervalerror1" /></div>
				<div class="error" id="intervalError2" style="display: none"><spring:message
					code="dataintegrity.schedule.intervalerror2" /></div>
				</td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit"
					value="<spring:message code="general.save"/>"
					onclick="return validate();" /></td>
			</tr>
		</table>
	</form>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>