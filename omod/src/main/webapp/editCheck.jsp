<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Integrity Checks" otherwise="/login.htm" redirect="/module/dataintegrity/edit.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<openmrs:htmlInclude file="/moduleResources/dataintegrity/jquery.dataTables.min.js" />
<openmrs:htmlInclude file="/scripts/jquery/dataTables/css/dataTables.css" />
<openmrs:htmlInclude file="/scripts/jquery/dataTables/css/dataTables_jui.css" />

<openmrs:htmlInclude file="/moduleResources/dataintegrity/jquery.tools.min.js" />
<openmrs:htmlInclude file="/moduleResources/dataintegrity/jquery.formparams.min.js" />

<openmrs:htmlInclude file="/dwr/interface/DWRDataIntegrityService.js"/>

<c:set var="new" value="${empty check.id}"/>

<h2>
	<c:if test="${new}"><spring:message code="dataintegrity.addCheck"/></c:if>
	<c:if test="${not new}"><spring:message code="dataintegrity.editCheck"/></c:if>
</h2>

<script type="text/javascript">
    var testResultsTables = {};
    var columnTable = null;
    var maxRows = 25;
    
    function testCode(code, wrapper){
        $j(wrapper + " .results").fadeOut("fast", function(){
            $j(wrapper + " .loading").fadeIn("fast", function(){
                $j(wrapper).fadeIn();
                DWRDataIntegrityService.testCode(code, maxRows, {
                    callback: function(results){ populateTestResults(results, wrapper); },
                    errorHandler: function(msg, ex){ $j(wrapper).fadeOut(); handler(msg, ex); }
                });
            });
        });
    }

    function hideCode(wrapper) {
        $j(wrapper).fadeOut();
    }

    function populateTestResults(results, wrapper) {
        if (results == null)
            return;
        
        // build the column defs
        var columns = new Array();
        var i = 0;
        $j(results.columns).each(function(){
            columns[i] = new Object();
            columns[i++].sTitle = this;
        });

        // get rid of the previous results if they exist
        if (wrapper in testResultsTables && testResultsTables[wrapper] != null) {
            testResultsTables[wrapper].fnDestroy();
            testResultsTables[wrapper].empty();
        }
                
        // render a new table
        // TODO: get this styled properly so it works every time
        testResultsTables[wrapper] = $j(wrapper + " .results").dataTable({
            aaData: results.data,
            aoColumns: columns,
            bPaginate: false,
            bLengthChange: false,
            bFilter: false,
            bSort: true,
            bInfo: false,
            bAutoWidth: false,
            sScrollX: $j(wrapper).width() + "px",
            bScrollCollapse: true
        });

        // display the results
        $j(wrapper + " .loading").fadeOut("fast", function(){
            $j(wrapper + " .results").fadeIn("fast", function(){
                $j(wrapper + " button").fadeIn("fast");
                testResultsTables[wrapper].fnDraw();
            });
        });
    }

	function generateColumnsFromCode(code) {
		DWRDataIntegrityService.getColumnsFromCode(code, function(results){

			// TODO: throw an error if nothing comes back
			if (results == null)
				return;

			generateColumns(results.columns);
		});
	}

    function generateColumns(columns){
        $j("#columns").fadeOut();

		if (columnTable != null) {
			columnTable.fnDestroy();
			columnTable.empty();
		}

		columnTable = $j("#columnTable").dataTable({
			bPaginate: false,
			bLengthChange: false,
			bFilter: false,
			bSort: false,
			bInfo: false,
			bAutoWidth: false,
			aoColumns: [
				{ sName: "id", bVisible: false },
				{
					sName: "show",
					sClass: "centered",
					sTitle: "<spring:message code="dataintegrity.edit.columns.show"/>",
					fnRender: function(data){
						return '<input name="columns[' + data.aData[0] 
							+ '][id]" type="hidden" value="' + data.aData[1] + '"/>'
							+ '<input type="hidden" name="columns[' + data.aData[0]
							+ '][uuid]" type="hidden" value="' + data.aData[7] + '"/>'
							+ '<input name="columns[' + data.aData[0] + '][show]" type="checkbox"'
							+ (data.aData[2] ? ' checked' : '') + ' value="true" />';
					}
				},
				{
					sName: "uid",
					sClass: "centered",
					sTitle: "<spring:message code="dataintegrity.edit.columns.uniqueidentifier"/>",
					fnRender: function(data){
						return '<input name="columns[' + data.aData[0] + '][uid]" type="checkbox"'
							+ (data.aData[3] ? ' checked' : '')
							+ ' value="true"/>';
					}
				},
				{ sName: "name", sTitle: "<spring:message code="dataintegrity.edit.columns.name"/>",
					fnRender: function(data){
						return '<input type="hidden" name="columns[' + data.aData[0] + '][name]" value="' + data.aData[4] + '"/>' + data.aData[4];
					}
				},
				{
					sName: "columnDisplayName",
					sTitle: "<spring:message code="dataintegrity.edit.columns.displayname"/>",
					fnRender: function(data){
						return '<input name="columns[' + data.aData[0] + '][display]" type="text" value="' + data.aData[5] + '"/>';
					}
				},
				{
					sName: "datatype",
					sTitle: "<spring:message code="dataintegrity.edit.columns.datatype"/>",
					fnRender: function(data){
						return renderDatatypeSelection(data.aData[0], data.aData[6]);
					}
				}
			]
		});

		// fill in the table with columns
		$j(columns).each(function(index, column){
			if (column.datatype == null || column.datatype == "") {
				column.datatype = bestPossibleDatatypeFor(column.name);
			}
			columnTable.fnAddData([index, column.columnId, column.showInResults, 
				column.usedInUid, column.name, column.displayName, column.datatype, column.uuid]);
		});

		$j("#columns").fadeIn();
    }

	function renderDatatypeSelection(index, value) {
		var selectbox = '<select name="columns[' + index + '][datatype]">';
		selectbox += '<option value=""><spring:message code="dataintegrity.edit.columnDatatypeNotUsed"/></option>';
		<c:forEach items="${columnDatatypes}" var="datatype">
				selectbox += '<option value="${datatype}"' + (value == "${datatype}" ? ' selected="selected"' : "") + ">${datatype}</option>"
		</c:forEach>
		selectbox += '</select>'
		return selectbox;
	}

	var columnDatatypeMap = {
		user_id: "User",
		creator: "User",
		changed_by: "User",
		voided_by: "User",
		retired_by: "User",
		person_id: "Person",
		patient_id: "Patient",
		obs_id: "Observation",
		concept_id: "Concept",
		encounter_id: "Encounter",
		date_created: "Date",
		date_changed: "Date",
		date_voided: "Date",
		date_retired: "Date",
		voided: "Yes/No",
		birthdate_estimated: "Yes/No",
		retired: "Yes/No",
		dead: "Yes/No"
	}

	function bestPossibleDatatypeFor(column) {
		if (column in columnDatatypeMap)
			return columnDatatypeMap[column];
	}

    $j(document).ready(function(){
        $j("a.help").click(function(){ return false; });
        $j("a.help").addClass("ui-state-default ui-corner-all");
        $j("a.help").tooltip({
            effect: "fade",
            opacity: 0.85,
            position: "center right",
            layout: '<div class="tooltip-wrapper ui-corner-all"><div/></div>'
        });

        $j("a.help").hover(
            function() { $j(this).addClass('ui-state-hover'); },
            function() { $j(this).removeClass('ui-state-hover'); }
        );
			
		// hide columns
        $j("#columns").hide();

        // hide test tables and such
        var stuffToHide = [
            "#checkCodeTestResults", "#totalCodeTestResults", "#resultsCodeTestResults",
            "#closeCheckCode", "#closeTotalCode", "#closeResultsCode"
        ];
        for (i in stuffToHide)
            $j(stuffToHide[i]).hide();
        
		// hide code if it does not exist
		if (${empty check.totalCode})
            $j("#totalWrapper").hide();		
		if (${empty check.resultsCode})
            $j("#resultsWrapper").hide();
			
        // assign targets to buttons
        $j("#testCheckCode").click(function(){ 
			testCode($j("#checkCode").val(), "#checkCodeTestResults"); 
			$j("#closeCheckCode").fadeIn(); return false; });
        $j("#testTotalCode").click(function(){ 
			testCode($j("#totalCode").val(), "#totalCodeTestResults"); 
			$j("#closeTotalCode").fadeIn(); return false; });
        $j("#testResultsCode").click(function(){ 
			testCode($j("#resultsCode").val(), "#resultsCodeTestResults"); 
			$j("#closeResultsCode").fadeIn(); return false; });
		
        $j("#closeCheckCode").click(function(){ 
			hideCode("#checkCodeTestResults"); 
			$j(this).fadeOut(); return false; });
        $j("#closeTotalCode").click(function(){ 
			hideCode("#totalCodeTestResults"); 
			$j(this).fadeOut(); return false; });
        $j("#closeResultsCode").click(function(){ 
			hideCode("#resultsCodeTestResults"); 
			$j(this).fadeOut(); return false; });

        $j("#generateColumns").click(function(){
			// TODO add something here to verify overwriting existing columns
			// TODO also do some kind of subscription to check and results code change events
            var code = $j("#useDiscoveryForResults").is(":checked") ? $j("#checkCode").val() : $j("#resultsCode").val();
            generateColumnsFromCode(code);
            return false;
        });

        // toggle checkboxes
        $j("#useTotal").change(function(){ $j("#totalWrapper").toggle("slow"); });
        $j("#useDiscoveryForResults").change(function(){ $j("#resultsWrapper").toggle("slow"); });

		// set click action on submit button
		$j("#submitButton").click(function(){ 
			var data = $j("#checkEditForm").formParams(false);
			// convert columns into serialized strings
			var cols = data.columns;
			var newcols = new Array();
			var i = 0;
			for (var key in cols) {
				var col = cols[key];
				newcols[i++] = [col.id, col.show, col.uid, col.name, 
					col.datatype, col.uuid, col.display].join(':');
			}
			data.columns = newcols;
			// submit
			$j.post("save.htm", data, function(){ window.location = "list.htm"; });
			return false;
		});

		// build the initial set of columns if they already exist
		<c:if test="${not empty check.resultsColumns}">
			var columns = new Array();
			<c:forEach var="col" items="${check.resultsColumns}">
				columns.push({ columnId: ${col.columnId}, showInResults: ${col.showInResults}, 
					usedInUid: ${col.usedInUid}, name: '${col.name}', 
					displayName: '${col.displayName}', datatype: '${col.datatype}',
					uuid: '${col.uuid}' });
			</c:forEach>
			generateColumns(columns);
		</c:if>

    });
</script>

<style>
    .fullwidth { width: 99% !important; }
    .vertical-spacing { margin-bottom: 1em; }
    .description { font-style: italic; }
    .code { border: 1px dashed #aaa; background: #eee; padding: 0.25em 1em; }
    .centered { text-align: center; vertical-align: middle; }
    .nowrap { white-space: nowrap; }
    a.help { display: inline-block; }
    #checkEditForm input[type="text"], #checkEditForm textarea { border-color: #999; width: 100%; }
    #checkEditForm h3 { padding: 0; font-size: 1em; color: black; line-height: 2em; font-style: normal; border-bottom: 1px dashed #ccc; }
    #columnTable { margin: 0 auto; }
    #columnTable input[type=text] { width: 10em; }
    .tooltip-wrapper { display:none; border: 1px solid black; background: #333; color: white; padding: 0.5em .75em; width: 10em; }

    /* datatables */
    .dataTables_info { padding-top: 0; }
    .dataTables_paginate { padding-top: 0; }
    .css_right { float: right; }
    .dataTables_wrapper { font-size: 0.85em; }
</style>

<div class="error" id="errorDiv" style="display: none"><spring:message code="dataintegrity.checksList.blank"/></div>

<form id="checkEditForm" action="save.htm" method="post" onsubmit="return inputValidator()">

    <div class="vertical-spacing">
        <label for="name"><spring:message code="dataintegrity.edit.name"/><span style="color: red">*</span></label>
        <input class="fullwidth" type="text" name="name" value="${check.name}" maxlength="100" id="nameTxt"/>
    </div>

    <div class="vertical-spacing">
        <label><spring:message code="dataintegrity.edit.description"/><span style="color: red">*</span></label>
        <textarea class="fullwidth" type="text" name="description" maxlength="255" id="descriptionTxt">${check.description}</textarea>
    </div>

    <h3 class="vertical-spacing description">
		<spring:message code="dataintegrity.edit.step.1"/>
		<spring:message code="dataintegrity.discovery.title"/>
	</h3>

    <div class="description vertical-spacing"><spring:message code="dataintegrity.edit.discovery.description"/></div>

    <!--
    <div class="vertical-spacing">
        <label><spring:message code="dataintegrity.edit.discovery.language"/><span style="color: red">*</span></label>
        <a class="help" href="#" title="<spring:message code="dataintegrity.edit.discovery.language.help"/>"><span class="ui-icon ui-icon-help"></span></a>
        <br />
        <select name="checkLanguage" id="typeSelect" style="width: 130px">
            <option value="sql">SQL</option>
        </select>
    </div>
    -->
    <input type="hidden" name="checkLanguage" value="sql"/>

    <div class="vertical-spacing">
        <label for="checkCode"><spring:message code="dataintegrity.edit.discovery.code"/> <span style="color: red">*</span></label>
        <a class="help" href="#" title="<spring:message code="dataintegrity.edit.discovery.code.help"/>"><span class="ui-icon ui-icon-help"></span></a>
        <br />
        <textarea type="text" rows="10" name="checkCode" id="checkCode" class="fullwidth">${check.checkCode}</textarea>
        <br />
        <button id="testCheckCode"><spring:message code="dataintegrity.edit.testCode"/></button>
        <button id="closeCheckCode"><spring:message code="dataintegrity.edit.hideResults"/></button>
        <div id="checkCodeTestResults">
            <div class="loading"><spring:message code="dataintegrity.edit.loading"/></div>
            <table class="results"></table>
        </div>
    </div>

    <h3 class="vertical-spacing description">
		<spring:message code="dataintegrity.edit.step.2"/>
		<spring:message code="dataintegrity.failure.title"/>
	</h3>

    <div class="description vertical-spacing"><spring:message code="dataintegrity.edit.failure.threshold.description"/></div>

    <div class="vertical-spacing">
        <select name="failureType" id="failureType" style="width: 130px">
            <option value="count" <c:if test="${check.failureType == 'count'}">selected</c:if>>Count</option>
            <option value="value" <c:if test="${check.failureType == 'value'}">selected</c:if>>Value</option>
            </select>

        <spring:message code="dataintegrity.edit.failure.is"/>

        <select name="failureOperator" id="failureOperator" style="width: 130px">
            <option value="greater than" <c:if test="${check.failureOperator == 'greater than'}">selected</c:if>>Greater Than</option>
            <option value="less than" <c:if test="${check.failureOperator == 'less than'}">selected</c:if>>Less Than</option>
            <option value="equal to" <c:if test="${check.failureOperator == 'equal to'}">selected</c:if>>Equal To</option>
            <option value="not equal to" <c:if test="${check.failureOperator == 'not equal to'}">selected</c:if>>Not Equal To</option>
            </select>

            <input type="text" name="failureThreshold" value="${check.failureThreshold == null ? 0 : check.failureThreshold}" style="width: 10em !important;"/>
    </div>

    <div class="vertical-spacing">
		<c:if test="${empty check.totalCode}"><input id="useTotal" type="checkbox" value="true"/></c:if>
		<c:if test="${not empty check.totalCode}"><input id="useTotal" type="checkbox" value="true" checked/></c:if>
		<label for="useTotal"><spring:message code="dataintegrity.edit.failure.use.total"/></label>
    </div>

    <div id="totalWrapper">
    
        <p class="description"><spring:message code="dataintegrity.edit.failure.total.description"/></p>

        <!--
        <div class="vertical-spacing">
            <label><spring:message code="dataintegrity.edit.failure.total.language"/><span style="color: red">*</span></label>
            <a class="help" href="#" title="<spring:message code="dataintegrity.edit.failure.total.language.help"/>"><span class="ui-icon ui-icon-help"></span></a>
            <br />
            <select name="totalLanguage" id="typeSelect" style="width: 130px">
                <option value="sql">SQL</option>
            </select>
        </div>
        -->
        <input type="hidden" name="totalLanguage" value="sql"/>

        <div class="vertical-spacing">
            <label for="totalCode"><spring:message code="dataintegrity.edit.failure.total.code"/> <span style="color: red">*</span></label>
            <a class="help" href="#" title="<spring:message code="dataintegrity.edit.falure.total.code.help"/>"><span class="ui-icon ui-icon-help"></span></a>
            <br />
            <textarea type="text" rows="10" name="totalCode" id="totalCode" class="fullwidth">${check.totalCode}</textarea>
            <br />
            <button id="testTotalCode"><spring:message code="dataintegrity.edit.testCode"/></button>
            <button id="closeTotalCode"><spring:message code="dataintegrity.edit.hideResults"/></button>
            <div id="totalCodeTestResults">
                <div class="loading"><spring:message code="dataintegrity.edit.loading"/></div>
                <table class="results"></table>
            </div>
        </div>

    </div>

    <h3 class="vertical-spacing description">
		<spring:message code="dataintegrity.edit.step.3"/>
		<spring:message code="dataintegrity.results.title"/>
	</h3>

    <div class="description vertical-spacing"><spring:message code="dataintegrity.edit.results.code.description"/></div>

    <div class="vertical-spacing">
		<c:if test="${empty check.resultsCode}"><input id="useDiscoveryForResults" name="useDiscoveryForResults" type="checkbox" value="true" checked/></c:if>
		<c:if test="${not empty check.resultsCode}"><input id="useDiscoveryForResults" name="useDiscoveryForResults" type="checkbox" value="true"/></c:if> 
		<label for="useDiscoveryForResults"><spring:message code="dataintegrity.edit.results.use.query"/></label>
    </div>

    <div id="resultsWrapper">

        <!--
        <div class="vertical-spacing">
            <label><spring:message code="dataintegrity.edit.results.language"/><span style="color: red">*</span></label>
            <a class="help" href="#" title="<spring:message code="dataintegrity.edit.results.language.help"/>"><span class="ui-icon ui-icon-help"></span></a>
            <br />
            <select name="resultsLanguage" id="typeSelect" style="width: 130px">
                <option value="sql">SQL</option>
            </select>
        </div>
        -->
        <input type="hidden" name="resultsLanguage" value="sql"/>

        <div class="vertical-spacing">
            <label for="resultsCode"><spring:message code="dataintegrity.edit.results.code"/> <span style="color: red">*</span></label>
            <a class="help" href="#" title="<spring:message code="dataintegrity.edit.results.code.help"/>"><span class="ui-icon ui-icon-help"></span></a>
            <br />
            <textarea type="text" rows="10" name="resultsCode" id="resultsCode" class="fullwidth">${check.resultsCode}</textarea>
            <br />
            <button id="testResultsCode"><spring:message code="dataintegrity.edit.testCode"/></button>
            <button id="closeResultsCode"><spring:message code="dataintegrity.edit.hideResults"/></button>
            <div id="resultsCodeTestResults">
                <div class="loading"><spring:message code="dataintegrity.edit.loading"/></div>
                <table class="results"></table>
            </div>
        </div>

    </div>

    <div class="description vertical-spacing"><spring:message code="dataintegrity.edit.results.columns.description"/></div>

    <button id="generateColumns">Generate Column List &raquo;</button>
    <div id="columns">
        <table id="columnTable" class="vertical-spacing"></table>
    </div>

    <input type="hidden" name="checkId" value="${check.id}" />
    <input type="submit" id="submitButton" value="<spring:message code="dataintegrity.edit.save"/>"/>
</form>
<br/>

<b class="boxHeader"><spring:message code="dataintegrity.edit.help" /></b>
<div class="box">
    <ul>
        <li><i><spring:message code="dataintegrity.edit.help.query"/></i>
        <li><i><spring:message code="dataintegrity.edit.help.basis1"/></i>
        <li><i><spring:message code="dataintegrity.edit.help.basis2"/></i>
        <li><i><spring:message code="dataintegrity.edit.help.score"/></i>
    </ul>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>