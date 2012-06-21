<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Integrity Checks" otherwise="/login.htm" redirect="/module/dataintegrity/view.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<openmrs:htmlInclude file="/moduleResources/dataintegrity/js/jquery.dataTables.min.js" />
<openmrs:htmlInclude file="/moduleResources/dataintegrity/js/jquery.corner.js" />
<openmrs:htmlInclude file="/moduleResources/dataintegrity/js/ColVis.js" />
<openmrs:htmlInclude file="/moduleResources/dataintegrity/TableTools/js/TableTools.min.js" />

<openmrs:htmlInclude file="/moduleResources/dataintegrity/js/highcharts.js" />

<openmrs:htmlInclude file="/scripts/jquery/dataTables/css/dataTables.css" />
<openmrs:htmlInclude file="/moduleResources/dataintegrity/css/smoothness/jquery-ui-1.8.16.custom.css" />
<openmrs:htmlInclude file="/moduleResources/dataintegrity/css/dataTables_jui.css" />
<openmrs:htmlInclude file="/moduleResources/dataintegrity/css/ColVis.css" />
<openmrs:htmlInclude file="/moduleResources/dataintegrity/TableTools/css/TableTools.css" />

<openmrs:htmlInclude file="/dwr/interface/DWRDataIntegrityService.js"/>

<style>
	.dataTables_info { font-weight: normal; }
	.ui-widget-header { font-weight: inherit; }
	#resultsTable, #historyTable, #summaryColumns { width: 100% !important; }
	.css_right { float: right; }
	.css_left { float: left; }
	.ColVis { float: left; margin-bottom: 0 }
	.dataTables_length { width: auto; }
	.centered { text-align: center; }
	.retiredMessage { margin-bottom: 1em; }

	tr.status-1, tr.status-1 a { color: #999 !important; }
	tr.status-2, tr.status-2 a { color: #a44 !important; }

	.settings { display: inline-block; position: absolute; margin: 3px 0 0 7px; }
	.settings.mini { }
	.settings.full { display: none; z-index: 9999; }
	.settings.full ul { list-style: none; padding: 0; margin: 0; padding: 0; border: 1px solid #aaa; background: #eee; margin-left: 16px; }
	.settings.full li { font-size: 10pt; font-weight: normal; }
	.settings.full li a { display: block; padding: 0.25em 0.5em; text-decoration: none; color: #b0bed9; }
	.settings.full li a:hover { background: #b0bed9; color: black; }

	#resultsTableFilters { padding-top: 0.5em; }
	#resultsTableFilters label { margin-right: 0.5em; }

	#historyChartWrapper { text-align: center; padding-top: 1.5em; }
	#historyChart { width: 80% !important; height: 20em !important; margin: 0 auto 1.5em; }

	#summaryTab .fieldValue { margin-bottom: 1em; }
	#summaryTab .code { font-family: "courier"; padding: 0.5em; font-size: 0.85em; border: 1px dashed black; background: #ddd; }

	#summaryTab > div { padding: 0; }
	#summaryTab h3 { border-bottom: 1px solid black; margin: 1em 0; }

	/* required for rendering history chart on a hidden tab */
	.ui-tabs .ui-tabs-hide {
		position: absolute;
		left: -10000px;
	}

	/* retire dialog */
	#retireWrapper { text-align: center; }
	#retireForm { margin: 1.5em auto; text-align: left; width: 34em; }
	#retireForm label { margin-right: 1em; }
	#retireForm input { width: 25em; }
</style>

<script type="text/javascript">

	jQuery.fn.dataTableExt.afnFiltering.push(
		function( oSettings, aData, iDataIndex ) {
			var viewVoided = $j("#viewVoided").is(":checked");
			var viewIgnored = $j("#viewIgnored").is(":checked");
			if ( !viewVoided && aData[aData.length-1] == "Voided") { return false; }
			if ( !viewIgnored && aData[aData.length-1] == "Ignored") { return false; }
			return true;
		}
	);

	var resultsTable = null;
	var totalData = [];
	var newData = [];
	var ignoredData = [];
	var voidedData = [];
	var durationData = [];
	
	$j(document).ready(function(){
		
		<c:set var="lastColumn" value="1"/>

		$j("#retireDialog").dialog({
			autoOpen: false,
			width: "38em",
			modal: true,
			buttons: {
				"<spring:message code="general.retire"/>": function() {
					$j.post("retire.htm", {
						checkId: ${check.id},
						retireReason: $j("input[name=retireReason]").val()
					}, function(){ window.location = "list.htm"; });
					return false;
				},
				Cancel: function() {
					$j(this).dialog("close");
				}
			},
			close: function() {
				$j("input[name=retireReason]").val("");
			}
		});

		$j("#retireLink").click(function() {
			$j("#retireDialog").dialog("open");
			return false;
		});

		$j("#unretireLink").click(function() {
			$j.post("unretire.htm", { checkId: ${check.id} }, function(){ window.location = "list.htm"; });
			return false;
		});

		// format the results datatable
		resultsTable = $j("#resultsTable").dataTable({
			sDom: '<"H"Clfr>t<"F"ip>',
			bJQueryUI: true,
			bAutoWidth: true,
			aoColumns: [
				{ 
					sName: "action", 
					sTitle: "Action", 
					bVisible: true, 
					bSortable: false, 
					sClass: "centered",
					fnRender: function(data){ return renderActions(); }
				},
				{ sName: "uid", sTitle: "Unique Identifier", bVisible: false },
				<c:forEach items="${check.resultsColumns}" var="column" varStatus="colNo">
					{
						sName: "${column.name}",
						sTitle: "${column.displayName}",
						sType: "${column.datatype == 'Date' ? 'date' : 'html'}",
						bVisible: ${column.showInResults},
						bUseRendered: false,
						fnRender: function(data){
							return renderCell(data.aData[${colNo.index + 2}], "${column.datatype}"); 
						}
					},
				<c:set var="lastColumn" value="${colNo.index + 2}"/>
				</c:forEach>
				{ 
					sName: "status", 
					sTitle: "Status", 
					fnRender: function(data){ return renderStatus(data.aData[${lastColumn+1}]); }
				}
			]
		});	

		// format the results datatable
		var historyTable = $j("#historyTable").dataTable({
			bJQueryUI: true,
			bAutoWidth: true,
			aoColumns: [
				{ sName: "date", asSorting: [ "desc", "asc", "desc" ] },
				{ sName: "passed", fnRender: function(data){ return renderPassed(data.aData[1]); } },
				{ sName: "failures" },
				{ sName: "duration", fnRender: function(data){ return (data.aData[3] / 1000) + " sec"; } },
				{ sName: "who" }
			]
		});

		// format the summary columns datatable
		var summaryColumnsTable = $j("#summaryColumns").dataTable({
			bJQueryUI: true,
			bAutoWidth: true,
			bSort: false,
			aoColumns: [
				{ sName: "id", bVisible: false },
				{ sName: "show", sClass: "centered", 
					fnRender: function(data){ return renderDisabledCheckbox(data.aData[1]); } },
				{ sName: "uid", sClass: "centered", 
					fnRender: function(data){ return renderDisabledCheckbox(data.aData[2]); } },
				{ sName: "column" },
				{ sName: "display" }
			]
		});

		var resultsTableTools = new TableTools(resultsTable, {
			buttons: [ 
				"copy", 
				"xls", 
				{
					sExtends: "pdf",
					sPdfOrientation: "landscape",
					sPdfMessage: "Your custom message would go here." 
				},
				"print" ],
			sSwfPath: "http://datatables.net/release-datatables/extras/TableTools/media/swf/copy_cvs_xls_pdf.swf"
		});

		$j('#tableTools').html(resultsTableTools.dom.container);
		
		$j("#resultsTableFilters input").click(function(){ resultsTable.fnDraw(); });
		
		// history chart
		<c:forEach items="${check.integrityCheckRuns}" var="run" varStatus="runCount">
			totalData.push([<openmrs:formatDate date="${run.dateCreated}" type="milliseconds"/>, ${run.totalCount}]);
			newData.push([<openmrs:formatDate date="${run.dateCreated}" type="milliseconds"/>, ${run.newCount}]);
			voidedData.push([<openmrs:formatDate date="${run.dateCreated}" type="milliseconds"/>, -1*${run.voidedCount}]);
			ignoredData.push([<openmrs:formatDate date="${run.dateCreated}" type="milliseconds"/>, -1*${run.ignoredCount}]);
			durationData.push([<openmrs:formatDate date="${run.dateCreated}" type="milliseconds"/>, ${run.duration / 1000}]);
		</c:forEach>
		
		<c:set var="aboveColor" value="${check.failureOperator == 'greater than' ? 'rgb(255,0,0)' : 'rgb(0,255,0)'}"/>
		<c:set var="belowColor" value="${check.failureOperator == 'less than' ? 'rgb(255,0,0)' : 'rgb(0,255,0)'}"/>
		
		var historyChart = new Highcharts.Chart({
			chart: {
				renderTo: 'historyChart',
				zoomType: 'x',
				defaultSeriesType: 'areaspline'
			},
			title: {
				text: 'Data Integrity Over Time'
			},
			subtitle: {
				text: 'Errors detected at various points'
			},
			xAxis: {
				type: 'datetime',
				dateTimeLabelFormats: { // don't display the dummy year
					month: '%e. %b',
					year: '%b'
				}
			},
			yAxis: {
				title: { text: 'Count' }
			},
			tooltip: {
				formatter: function() {
					return '<b>'+ this.series.name +'</b><br/>'+
						Highcharts.dateFormat('%e. %b', this.x) +': '+ this.y;
				}
			},
			series: [
				{ id: 0, name: 'Unresolved', data: totalData }, 
				{ id: 1, name: 'New', data: newData }, 
				{ id: 2, name: 'Ignored', data: ignoredData }, 
				{ id: 3, name: 'Resolved', data: voidedData }
			]
		});
		historyChart.get(2).hide();

		var tabs = $j("#tabs").tabs();

		$j(".settings.mini a.gear").click(function(){ 
			$j(this).parent().parent().find(".settings.full").fadeIn();
		});

		$j(".settings.full").mouseleave(function(){
			$j(this).fadeOut();
		});
		
		$j("a.ignore").live('click', function(){
			// get the parent tr for this cell
			var tr = $j(this).closest("tr");
			// find it in the table
			var row = resultsTable.fnGetPosition(tr[0]);
			// get the table data for that row
			var data = resultsTable.fnGetData(row);
			// pull the uid
			var uid = data[1];
			// ignore it in the backend
			DWRDataIntegrityService.ignoreResult(${check.id}, uid, function(success){
				if (success) {
					// change the table data to reflect ignored status
					data[data.length-1] = "Ignored";
					// hacky but effective way to switch to "ignored" class
					$j(tr).removeClass("status-0");
					$j(tr).removeClass("status-2");
					$j(tr).addClass("status-1");
					// change the contents of the actual table cell
					$j(tr).find("td:last").html("Ignored");
					// redraw the table (hopefully apply the filters but stay on that page)
					resultsTable.fnDraw();
				} else {
					alert("ERROR!");
				}
			});
		})
	});		

	function renderActions() {
		return '<a href="#" class="ignore">ignore</a>';
	}
	
	function renderPassed(data) {
		return data == "true" ? "PASSED" : "FAILED";
	}

	function renderStatus(status) {
		if(status == "0") { return "New"; } 
		if(status == "1") { return "Ignored"; } 
		if(status == "2") { return "Voided"; } 
		return "Other";
	}

	function renderCell(data, colDatatype) {
		if (colDatatype == "Person")
			return '<a target="new" href="<openmrs:contextPath/>/personDashboard.form?personId=' + data + '">' + data + '</a>';
		if (colDatatype == "Patient")
			return '<a target="new" href="<openmrs:contextPath/>/patientDashboard.form?patientId=' + data + '">' + data + '</a>';
		if (colDatatype == "Concept")
			return '<a target="new" href="<openmrs:contextPath/>/dictionary/concept.htm?conceptId=' + data + '">' + data + '</a>';
		if (colDatatype == "User")
			return '<a target="new" href="<openmrs:contextPath/>/admin/users/user.form?userId=' + data + '">' + data + '</a>';
		if (colDatatype == "Encounter")
			return '<a target="new" href="<openmrs:contextPath/>/admin/encounters/encounter.form?encounterId=' + data + '">' + data + '</a>';
		if (colDatatype == "Observation")
			return '<a target="new" href="<openmrs:contextPath/>/admin/observations/obs.form?obsId=' + data + '">' + data + '</a>';
		if (colDatatype == "Date") {
			// TODO find a good way of doing this from javascript
			return data;
		}
		if (colDatatype == "Yes/No")
			return data == "1" ? "Yes" : "No";
		return data;
	}
	
	function renderDisabledCheckbox(value) {
		return '<input type="checkbox" disabled' + ((value == "true") ? ' checked/>' : '/>');
	}
	
</script>

<c:if test="${check.retired}">
	<div class="retiredMessage"><div><spring:message code="dataintegrity.retiredMessage"/></div></div>
</c:if>

<h2 class="name">
	${check.name}
	<span class="settings mini">
		<a class="gear" href="#" title="Open Settings"><span class="ui-icon ui-icon-gear"></span></a>
	</span>
	<span class="settings full">
		<span class="ui-icon ui-icon-gear"></span>
		<ul>
			<li><a href="edit.htm?checkId=${check.id}">Edit</a></li>
			<li><a href="run.htm?checkId=${check.id}">Run</a></li>
			<c:if test="${not check.retired}">
				<li><a id="retireLink" href="#"><spring:message code="general.retire"/></a></li>
			</c:if>
			<c:if test="${check.retired}">
				<li><a id="unretireLink" href="#"><spring:message code="general.unretire"/></a></li>
			</c:if>
			<li><a href="duplicate.htm?checkId=${check.id}">Duplicate</a></li>
		</ul>
	</span>
</h2>
	
<p class="description">${check.description}</p>

<div id="tabs">
	<ul>
		<li><a href="#resultsTab">Results</a></li>
		<li><a href="#historyTab">History</a></li>
		<li><a href="#summaryTab">Summary</a></li>
	</ul>

	<div id="resultsTab">
		<div id="tableTools"></div>
		<div id="resultsTableFilters">
			<input type="checkbox" id="viewVoided"></input>
			<label for="viewVoided">View Voided Records</label>
			<input type="checkbox" id="viewIgnored"></input>
			<label for="viewIgnored">View Ignored Records</label>
		</div>
		<div id="resultsTableWrap">
			<table id="resultsTable" class="display">
				<thead>
					<tr>
						<th>Action</th>
						<th>Unique Identifier</th>
						<c:forEach items="${check.resultsColumns}" var="column">
								<th>${column.displayName}</th>
						</c:forEach>
						<th>Status</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${check.integrityCheckResults}" var="result">
						<tr class="status-${result.status}">
							<td></td>
							<td>${result.uniqueIdentifier}</td>
							<c:forEach items="${check.resultsColumns}" var="column" varStatus="colNo">
								<td>${result.data[column.name]}</td>
							</c:forEach>
							<td class="status">${result.status}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<div id="historyTab">
		<div id="historyChartWrapper">
			<div id="historyChart"></div>
		</div>
		<table id="historyTable" class="display">
			<thead>
				<tr>
					<th>Date Ran</th>
					<th>Pass / Fail</th>
					<th>Failures</th>
					<th>Duration</th>
					<th>Who Ran</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${check.integrityCheckRuns}" var="run">
					<tr>
						<td>${run.dateCreated}</td>
						<td>${run.checkPassed}</td>
						<td>${run.totalCount}</td>
						<td>${run.duration}</td>
						<td>${run.creator.personName}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div id="summaryTab">
		<div id="summaryAuditInfo">
			<h3><spring:message code="dataintegrity.auditInfo"/></h3>
			
			<h4><spring:message code="general.creator"/></h4>
			<div class="fieldValue">${check.creator.personName}</div>

			<h4><spring:message code="general.dateCreated"/></h4>
			<div class="fieldValue">${check.dateCreated}</div>

			<c:if test="${not empty check.changedBy}">
				<h4><spring:message code="general.changedBy"/></h4>
				<div class="fieldValue">${check.changedBy.personName}</div>

				<h4><spring:message code="general.dateChanged"/></h4>
				<div class="fieldValue">${check.dateChanged}</div>
			</c:if>
				
			<c:if test="${check.retired}">
				<h4><spring:message code="general.retiredBy"/></h4>
				<div class="fieldValue">${check.retiredBy.personName}</div>

				<h4><spring:message code="dataintegrity.auditInfo.dateRetired"/></h4>
				<div class="fieldValue">${check.dateRetired}</div>

				<h4><spring:message code="general.retiredReason"/></h4>
				<div class="fieldValue">${check.retireReason}</div>
			</c:if>
		</div>
		
		<div id="summaryDiscovery">
			<h3><spring:message code="dataintegrity.discovery.title"/></h3>

			<h4><spring:message code="dataintegrity.edit.discovery.code"/>:</h4>
			<div class="fieldValue code">${check.checkCode}</div>
		</div>

		<div id="summaryFailure">
			<h3><spring:message code="dataintegrity.failure.title"/></h3>
		
			<h4>Failure Detection Rule:</h4>
			<div class="fieldValue">
				${check.failureType} 
				<spring:message code="dataintegrity.edit.failure.is"/>
				${check.failureOperator}
				${check.failureThreshold}
			</div>
			
			<c:if test="${not empty check.totalCode}">
				<h4><spring:message code="dataintegrity.edit.failure.total.code"/>:</h4>
				<div class="fieldValue code">${check.totalCode}</div>
			</c:if>
				
			<c:if test="${empty check.totalCode}">
				<h4>Includes a Total Query:</h4>
				<div class="fieldValue"><spring:message code="general.no"/></div>
			</c:if>
		</div>
		
		<div id="summaryResults" class="display">
			<h3><spring:message code="dataintegrity.results.title"/></h3>

			<c:if test="${empty check.resultsCode}">
				<h4>Includes a Different Results Query:</h4>
				<div class="fieldValue"><spring:message code="general.no"/></div>
			</c:if>
				
			<c:if test="${not empty check.resultsCode}">
				<h4><spring:message code="dataintegrity.edit.results.code"/>:</h4>
				<div class="fieldValue code">${check.resultsCode}</div>
			</c:if>
				
			<h4>Result Columns:</h4>
			<div class="fieldValue">
				<table id="summaryColumns" class="display">
					<thead>
						<tr>
							<th>Column ID</th>
							<th>Show In Results</th>
							<th>Use in UID</th>
							<th>Column</th>
							<th>Display As</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${check.resultsColumns}" var="column">
							<tr>
								<td>${column.columnId}</td>
								<td>${column.showInResults}</td>
								<td>${column.usedInUid}</td>
								<td>${column.name}</td>
								<td>${column.displayName}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>

<div id="retireDialog" class="hidden">
	<div id="retireWrapper">
		<div id="retireForm">
			<label for="retireReason"><spring:message code="dataintegrity.auditInfo.retireReason"/></label>
			<input type="text" name="retireReason" id="retireReason" size="100"/>
		</div>
	</div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>