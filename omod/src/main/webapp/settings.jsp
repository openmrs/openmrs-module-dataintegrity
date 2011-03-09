<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Integrity Checks" otherwise="/login.htm" redirect="/module/dataintegrity/setIntegrityActionServer.htm"/>

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

<c:set var="serverPath" value="${pageContext.request.requestURL}"/>
<c:set var="serverGP" value=""/>
<openmrs:globalProperty key="dataintegrity.mail.serverpath" var="serverGP" />

<script type="text/javascript">

$j(document).ready(function(){
	var url = '${pageContext.request.requestURL}';
	var context = '${pageContext.request.contextPath}';
	var gp = '${serverGP}';
	var serverPath = url.substr(0, url.indexOf(context)) + context;
	if (gp != serverPath) {
		$j("#message #serverpath").html(serverPath);
		$j("#message").fadeIn();
	}
});

</script>

<br />
<h2><spring:message code="dataintegrity.settings.title"/></h2>
<br />

<b class="boxHeader"><spring:message code="GlobalProperty.list.title"/></b>
<div class="box">
	<div id="message" class="ui-widget" style="display:none; margin:1em;">
		<div class="ui-state-error ui-corner-all" style="padding: 0.5em;">
			<span class="ui-icon ui-icon-alert"
				style="float: left; margin-right: 0.3em;"></span> 
			<span class="content"> Check the <b>dataintegrity.mail.serverpath</b>
				setting. Your server URL is <em><b><span id="serverpath"></span></b></em> 
			</span>
		</div>
	</div>
	<openmrs:portlet url="globalProperties" 
		parameters="propertyPrefix=dataintegrity|excludePrefix=dataintegrity.started;dataintegrity.mandatory;dataintegrity.database_version"/>
</div>

<br />
<br />

<%@ include file="/WEB-INF/template/footer.jsp" %>