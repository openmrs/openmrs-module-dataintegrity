<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Integrity Checks" otherwise="/login.htm" redirect="/module/dataintegrity/summaryEmailSchedule.form" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<!-- import jquery for openmrs 1.6.x -->
<openmrs:htmlInclude file="/scripts/jquery/jquery-1.3.2.min.js" />
<openmrs:htmlInclude file="/scripts/jquery-ui/js/jquery-ui-1.7.2.custom.min.js" />
<openmrs:htmlInclude file="/scripts/jquery-ui/css/redmond/jquery-ui-1.7.2.custom.css" />
 
<script type="text/javascript">
        var $j = jQuery.noConflict();
</script>
<!-- end 1.6.x required prep -->

<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />
<openmrs:htmlInclude file="/moduleResources/dataintegrity/jquery-ui-timepicker-addon.min.js" />
<openmrs:htmlInclude file="/moduleResources/dataintegrity/jquery.validate.min.js" />
<openmrs:htmlInclude file="/moduleResources/dataintegrity/common.css" />

<script type="text/javascript">
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
	
	// add the multiemail validation method
	jQuery.validator.addMethod(
		"multiemail", 
		function(value, element) {
			var emails = value.split(';'); 
			valid = true; 
			for(var i in emails) { 
				valid = valid && 
					jQuery.validator.methods.email.call(this, emails[i], element); 
			}
			return valid; 
		},
		"Please specify the correct domain for your documents"
	);
	
	// create form validator
	$j("#summaryEmailsForm").validate({
		rules: {
			txtname: "required",
			txtdescription: "required",
			emailsVal: {
				required: true,
				multiemail: true
			},
			startTime: {
				required: true,
				date: true
			},
			repeatInterval: {
				required: true,
				digits: true,
				min: 1
			}
		},
		messages: {
			txtname: '<spring:message code="dataintegrity.schedule.nameerror" javaScriptEscape="true"/>',
			txtdescription: '<spring:message code="dataintegrity.schedule.descriptionerror" javaScriptEscape="true"/>',
			emailsVal: '<spring:message code="dataintegrity.schedule.emaillisterror" javaScriptEscape="true"/>',
			startTime: '<spring:message code="dataintegrity.schedule.starttimeerror" javaScriptEscape="true"/>',
			repeatInterval: {
				required: '<spring:message code="dataintegrity.schedule.intervalerror1" javaScriptEscape="true"/>',
				digits: '<spring:message code="dataintegrity.schedule.intervalerror2" javaScriptEscape="true"/>',
				min: '<spring:message code="dataintegrity.schedule.intervalerror2" javaScriptEscape="true"/>'
			}
		},
		errorPlacement: function(error, element) {
			if (element.attr("name") == "repeatInterval")
				error.insertAfter("#repeatIntervalUnits");
			else
				error.insertAfter(element);
		}
	});
	
	// the following code only works in 1.7.x+ ... so don't expect anything after it to work on 1.6.x
	$j(".button").button();
});
</script>

<h2>
	<c:if test="${taskId == null}"><spring:message code="dataintegrity.schedule.email.new"/></c:if>
	<c:if test="${taskId != null}"><spring:message code="dataintegrity.schedule.email.edit"/></c:if>
</h2>

<br />

<b class="boxHeader">
	<spring:message code="dataintegrity.schedule.email.edit.header" />
</b>
<div class="box">
    <form method="post" name="summaryEmailsForm" id="summaryEmailsForm" style="padding: 0.5em 1em;">
 		<input type="hidden" name="taskId" value=""/>
		<fieldset>
			<legend><spring:message code="dataintegrity.schedule.parameter"/></legend>
			<div>
				<label class="above" for="txtname"><spring:message code="dataintegrity.schedule.name" /> *</label>
				<input type="text" name="txtname" size="30" id="txtname" value="${txtname}"/>
			</div>
			<div>
				<label class="above" for="txtdescription"><spring:message code="general.description" /> *</label>
				<textarea name="txtdescription" id="txtdescription" rows="3"
						cols="60">${txtdescription}</textarea>
			</div>
			<div>
				<label class="above" for="emailsVal"><spring:message code="dataintegrity.schedule.toemail"/> *</label>
				<div class="help"><spring:message code="dataintegrity.summary.emailformat" /></div>
				<textarea name="emailsVal" id="emailsVal" rows="3" cols="60" 
					required="true"
					message="">${emailsVal}</textarea>
			</div>
		</fieldset>
		<fieldset>
			<legend><spring:message code="dataintegrity.schedule.timing"/></legend>
			<div>
				<label class="above" for="startTimePattern">
					<spring:message code="Scheduler.scheduleForm.startTimePattern"/>
				</label>
				<input type="text" id="startTimePattern" name="startTimePattern" size="25" value="${startTimePattern}" disabled />
			</div>
			<div>
				<label class="above" for="startTime">
					<spring:message code="dataintegrity.schedule.date"/> *
				</label>
				<input type="text" id="startTime" name="startTime" size="25" value="${startTime}"/>
			</div>
			<div>
				<label class="above" for="repeatInterval">
					<spring:message code="dataintegrity.schedule.repeatinterval"/> *
				</label>
				<input type="text" id="repeatInterval" name="repeatInterval" size="10" value="${repeatInterval}" />
				<select id="repeatIntervalUnits" name="repeatIntervalUnits">
					<option value="seconds"
						<c:if test="${repeatIntervalUnits=='seconds'}">selected</c:if>>seconds</option>
					<option value="minutes"
						<c:if test="${repeatIntervalUnits=='minutes'}">selected</c:if>>minutes</option>
					<option value="hours"
						<c:if test="${repeatIntervalUnits=='hours'}">selected</c:if>>hours</option>
					<option value="days"
						<c:if test="${repeatIntervalUnits=='days'}">selected</c:if>>days</option>
					<option value="weeks"
						<c:if test="${repeatIntervalUnits=='weeks'}">selected</c:if>>weeks</option>
				</select>
			</div>
		</fieldset>
		<input class="button" type="submit" value="<spring:message code="general.save"/>"/>
    </form>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>