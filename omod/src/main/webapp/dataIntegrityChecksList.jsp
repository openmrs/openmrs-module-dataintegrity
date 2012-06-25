<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="View Integrity Checks" otherwise="/login.htm" redirect="/module/dataintegrity/list.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<openmrs:htmlInclude file="/dwr/interface/DWRDataIntegrityService.js"/>

<openmrs:htmlInclude file="/moduleResources/dataintegrity/css/smoothness/jquery-ui-1.8.16.custom.css" />

<style>
	#integrityCheckTable td { padding: 0.5em; vertical-align: middle; }
	#integrityCheckTable th { padding: 0.5em; }
	
	td.checkbox { text-align: center; }

	td.run .details { white-space: nowrap; }
	td.run .passed { width: 7em; display: inline-block; text-align: center; line-height: 2em; font-weight: bold; text-transform: uppercase; }
	td.run .true { background: #afa; }
	td.run .false { background: #faa; }
	td.run .dateRan { margin-left: 0.5em; white-space: nowrap; }
	td.run .expander { display: none; }
	
	span.runner { width: 18px; text-align: right; }
	
	/* retire dialog */
	#retireWrapper { text-align: center; }
	#retireForm { margin: 1.5em auto; text-align: left; width: 34em; }
	#retireForm label { margin-right: 1em; }
	#retireForm input { width: 25em; }
	
	/* context settings */
	td.action {text-decoration: none; width: 25px; }
	td.action * { text-decoration: none; }
	td.action .settings { display: inline-block; position: absolute; margin: -10px 0 0; }
	td.action .settings.mini a { display: block; background: #ddd; border: 1px solid #ccc; padding: 2px 4px 2px 2px; }
	td.action .settings.mini a:hover { background: #eee; }
	td.action .settings.full { display: none; z-index: 9999; margin: 5px 0 0 5px;}
	td.action .settings.full ul { list-style: none; padding: 0; margin: 0; padding: 0; border: 1px solid #aaa; background: #eee; margin-left: 16px; }
	td.action .settings.full li { font-size: 10pt; font-weight: normal; }
	td.action .settings.full li a { display: block; padding: 0.25em 0.5em; text-decoration: none; color: #b0bed9; }
	td.action .settings.full li a:hover { background: #b0bed9; color: black; }
	
	tr.retired, tr.retired * { text-decoration: inherit; font-style: italic; }
	tr.retired td.action { text-decoration: inherit; font-style: normal; }
	
</style>

<script>
	$j(document).ready(function(){
		// hide retired checks
		toggleRowVisibilityForClass('integrityCheckTable', 'retired');

		// hide previous results
		$j("td.run .history").hide();
		
		$j("td.run span.true").html("PASSED");
		$j("td.run span.false").html("FAILED");
		
		// magic for expanding results list
		$j("td.run .expander").click(function(){
			toggleHistory(this);
		});
		$j('td.run .expander').hover(
			function() { $j(this).css('cursor', 'pointer'); }, 
			function() { $j(this).css('cursor', 'auto');	}
		);

		$j(".settings.mini a.gear").click(function(){ 
			$j(this).parent().parent().find(".settings.full").fadeIn();
		});

		$j(".settings.full").mouseleave(function(){
			$j(this).fadeOut();
		});
		
		// magic for the run link
		$j('a.runner').click(function(){
			var el = this;
			if ($j(el).html() == "Running ...")
				return;
			
			var checkId = $j(el).attr("checkId");
			if (checkId == null || checkId == "")
				return;
			
			$j(el).html("Running ...");
			DWRDataIntegrityService.runIntegrityCheck(checkId, {
				callback: function(run){ 
					if (run != null) {
						var parent = $j(el).parents("tr");
						var latest = $j(parent).find(".latest");
						var lastrun = '<div class="details">' + $j(latest).html()+ '</div>';
						$j(parent).find(".history").prepend(lastrun);
						$j(parent).find(".latest .passed").html(run.checkPassed ? "PASSED" : "FAILED");
						$j(parent).find(".latest .dateRan").html(run.dateCreated.toLocaleString());
					}
					$j(el).html("<spring:message code="dataintegrity.run"/>");
				},
				errorHandler: function(msg, ex){ 
					handler(msg, ex);
					$j(el).html("<spring:message code="dataintegrity.run"/>");
				}
			});
		});
		
		$j("#retireDialog").dialog({
			autoOpen: false,
			width: "38em",
			modal: true,
			buttons: {
				"<spring:message code="general.retire"/>": function() {
					$j.post("retire.htm", {
						checkId: $j("input[name=checkId]").val(),
						retireReason: $j("input[name=retireReason]").val()
					}, function(){ window.location = "list.htm"; });
					return false;
				},
				Cancel: function() {
					$j(this).dialog("close");
				}
			},
			close: function() {
				$j("#retireDialog input").val("");
			}
		});

		$j(".retireLink").click(function() {
			$j("#retireDialog input[name=checkId]").val($j(this).attr("checkId"));
			$j("#retireDialog").dialog("open");
			return false;
		});

		$j(".unretireLink").click(function() {
			$j.post("unretire.htm", { checkId: $j(this).attr("checkId") }, function(){ window.location = "list.htm"; });
			return false;
		});

	});

	function toggleHistory(el) {
		var parent = $j(el).parent();
		if ($j(parent).find(".history").is(":visible")) {
			$j(parent).find(".history").hide("blind");
			$j(el).toggleClass("ui-icon-triangle-1-s", false);
			$j(el).toggleClass("ui-icon-triangle-1-e", true);
		} else {
			$j(parent).find(".history").show("blind");
			$j(el).toggleClass("ui-icon-triangle-1-e", false);
			$j(el).toggleClass("ui-icon-triangle-1-s", true);
		}
	}
</script>

<h2><spring:message code="dataintegrity.manage.title"/></h2>

<openmrs:hasPrivilege privilege="Manage Integrity Checks">
<p><a href="new.htm"><spring:message code="dataintegrity.addCheck"/></a></p>
</openmrs:hasPrivilege>

<b class="boxHeader">
	<span style="float: right">
		<a href="#" id="showRetired" onclick="return toggleRowVisibilityForClass('integrityCheckTable', 'retired');">
			<spring:message code="general.toggle.retired"/>
		</a>
	</span>
	<spring:message code="dataintegrity.list.title"/>
</b>
<div class="box">
	<c:if test="${not empty checks}">
	<table id="integrityCheckTable" cellpadding="10" cellspacing="0">
		<thead>
		<tr>
			<th></th>
			<th><spring:message code="general.name"/></th>
			<th><spring:message code="general.description"/></th>
			<th><spring:message code="dataintegrity.list.columns.results"/></th>
		</tr>
		</thead>
		<c:forEach items="${checks}" var="check" varStatus="varStatus">
		<tr class="<c:choose><c:when test="${varStatus.index % 2 == 0}">oddRow</c:when><c:otherwise>evenRow</c:otherwise></c:choose> <c:if test="${check.retired}">retired</c:if>">
			<td class="action">
				<span class="settings mini">
					<a class="gear" href="#" title="Open Settings"><span class="ui-icon ui-icon-gear"></span></a>
				</span>
				<span class="settings full">
					<ul>
						<li><a href="view.htm?checkId=${check.id}"><spring:message code="general.view"/></a></li>
						<openmrs:hasPrivilege privilege="Run Integrity Checks">
							<li><a class="runner" href="#" checkId="${check.id}"><spring:message code="dataintegrity.run"/></a></li>
						</openmrs:hasPrivilege>
						<openmrs:hasPrivilege privilege="Manage Integrity Checks">
							<li><a href="edit.htm?checkId=${check.id}"><spring:message code="general.edit"/></a></li>
							<li><a href="duplicate.htm?checkId=${check.id}"><spring:message code="dataintegrity.duplicate"/></a></li>
							<li>
							<c:if test="${not check.retired}">
								<a class="retireLink" href="" checkId="${check.id}"><spring:message code="general.retire"/></a>
							</c:if>
							<c:if test="${check.retired}">
								<a class="unretireLink" href="" checkId="${check.id}"><spring:message code="general.unretire"/></a>
							</c:if>
							</li>
						</openmrs:hasPrivilege>
					</ul>
				</span>
			</td>
			<td><a href="view.htm?checkId=${check.id}">${check.name}</a></td>
			<td>${check.description}</td>
			<td class="run">
				<c:if test="${not empty check.integrityCheckRuns}">
					<div class="runWrapper">
						<span class="expander ui-icon ui-icon-triangle-1-e"></span>
						<c:forEach items="${check.integrityCheckRuns}" var="run" varStatus="runStatus">
							<c:if test="${runStatus.index == 0}">
								<div class="latest details">
									<span class="passed ${run.checkPassed}">${run.checkPassed}</span>
									<span class="dateRan"><openmrs:formatDate date="${run.dateCreated}" type="long"/></span>
								</div>
								<div class="history">
							</c:if>
							<c:if test="${runStatus.index != 0}">
								<div class="details">
									<span class="passed ${run.checkPassed}">${run.checkPassed}</span>
									<abbr class="dateRan"><openmrs:formatDate date="${run.dateCreated}" type="long"/></span>
								</div>
							</c:if>
						</c:forEach>
						</div>
					</div>
				</c:if>
			</td>
		</tr>
		</c:forEach>
	</table>
	</c:if>
	
	<c:if test="${empty checks}"><spring:message code="dataintegrity.list.empty"/></c:if>
</div>

<div id="retireDialog" class="hidden">
	<div id="retireWrapper">
		<div id="retireForm">
			<label for="retireReason"><spring:message code="dataintegrity.auditInfo.retireReason"/></label>
			<input type="text" name="retireReason" id="retireReason" size="100"/>
			<input type="hidden" name="checkId" id="checkId"/>
		</div>
	</div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>