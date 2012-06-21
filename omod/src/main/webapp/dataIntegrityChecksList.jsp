<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="View Integrity Checks" otherwise="/login.htm" redirect="/module/dataintegrity/list.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<openmrs:htmlInclude file="/dwr/interface/DWRDataIntegrityService.js"/>
<openmrs:htmlInclude file="/moduleResources/dataintegrity/js/jquery.easy-confirm-dialog.js" />

<style>
	#integrityCheckTable td { padding: 0.5em; vertical-align: middle; }
	#integrityCheckTable th { padding: 0.5em; }
	#integrityCheckTable td.action, #integrityCheckTable td.action * { text-decoration: none; }
	
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
		
		// configure confirmation on all .confirm links
		$j(".confirm").easyconfirm({
			locale: {
				text: "<spring:message code="dataintegrity.delete.areyousure"/>"
			}
		});
		
		// magic for the run icon
		$j('input.runner').click(function(){
			var el = this;
			var checkId = $j(el).parents('td').find('input[name=checkId]').val();
			if (checkId == null || checkId == "")
				return;
			$j(el).attr('src', '${pageContext.request.contextPath}/images/loading.gif');
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
					$j(el).attr('src', '${pageContext.request.contextPath}/images/play.gif');
				},
				errorHandler: function(msg, ex){ 
					handler(msg, ex);
					$j(el).attr('src', '${pageContext.request.contextPath}/images/play.gif');
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
	<table id="integrityCheckTable" cellpadding="10">
		<thead>
		<tr>
			<th width="80px"><spring:message code="general.action"/></th>
			<th><spring:message code="general.name"/></th>
			<th><spring:message code="general.description"/></th>
			<th><spring:message code="dataintegrity.list.columns.results"/></th>
		</tr>
		</thead>
		<c:forEach items="${checks}" var="check" varStatus="varStatus">
		<tr class="<c:choose><c:when test="${varStatus.index % 2 == 0}">oddRow</c:when><c:otherwise>evenRow</c:otherwise></c:choose> <c:if test="${check.retired}">retired</c:if>">
			<td class="action"><input type="hidden" name="checkId" value="${check.id}"/>
				<a href="view.htm?checkId=${check.id}"><input type="image" src="${pageContext.request.contextPath}/images/open.gif"/></a>
				<openmrs:hasPrivilege privilege="Manage Integrity Checks">
					<a href="edit.htm?checkId=${check.id}"><input type="image" src="${pageContext.request.contextPath}/images/edit.gif"/></a>
				</openmrs:hasPrivilege>
				<openmrs:hasPrivilege privilege="Run Integrity Checks">
					<span class="runner">
						<input class="runner" type="image" src="${pageContext.request.contextPath}/images/play.gif"/>
					</span>
				</openmrs:hasPrivilege>
				<openmrs:hasPrivilege privilege="Manage Integrity Checks">
					<c:if test="${not check.retired}">
						<a class="retireLink" href="" checkId="${check.id}"><input type="image" src="${pageContext.request.contextPath}/images/trash.gif"/></a>
					</c:if>
					<c:if test="${check.retired}">
						<a class="unretireLink" href="" checkId="${check.id}"><input type="image" src="${pageContext.request.contextPath}/images/lookup.gif"/></a>
					</c:if>
				</openmrs:hasPrivilege>
				</td>
			<td>${check.name}</td>
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