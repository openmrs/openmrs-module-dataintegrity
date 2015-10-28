<%
    ui.decorateWith("appui", "standardEmrPage")
    ui.includeJavascript("uicommons", "datatables/jquery.dataTables.min.js")
%>

<script type="text/javascript">
    var breadcrumbs = [
        {icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm'},
        {
            label: "${ ui.message('dataintegrity.rule.label')}",
            link: '${ui.pageLink("dataintegrity", "rules")}'
        },
        <% if(rule!= null) { %>
        {
            label: "${ ui.message('dataintegrity.result.all.label')}",
            link: '${ui.pageLink("dataintegrity", "results")}'
        },
        {label: "Results for ${ rule.ruleName}"}
        <% } else { %>
        {label: "${ ui.message("dataintegrity.result.all.label")}"}
        <% } %>
    ];

    <% if (results.size() > 0) { %>
    jq(document).ready(function () {
        jq("#list-results").dataTable({
                                          "bFilter": true,
                                          "bJQueryUI": true,
                                          "bLengthChange": false,
                                          "iDisplayLength": 10,
                                          "sPaginationType": "full_numbers",
                                          "bAutoWidth": false,
                                          "sDom": 'ft<"fg-toolbar ui-toolbar ui-corner-bl ui-corner-br ui-helper-clearfix datatables-info-and-pg "ip>'
                                      });
    });
    <% } %>
</script>
<% if (rule == null) {
%>
<h2>${ui.message("dataintegrity.result.all.label")}</h2>
<% } else { %>
<h2>Results for ${rule.ruleName}</h2>
<% } %>
<% if (results.size() == 0) { %>
    There are no results to show. <a href="results.page?action=fireRules"><i class="icon-play"></i>&nbsp;Click here</a> to run all the validation rules, please note that it will take some time and slow down the server.
<% } else { %>
<table id="list-results" cellspacing="0" cellpadding="2" width="100%" class="dataTable">
    <thead>
    <tr>
        <th>${ui.message("dataintegrity.rule.name")}</th>
        <th>${ui.message("dataintegrity.result.id")}</th>
        <th>${ui.message("dataintegrity.result.name")}</th>
        <th>${ui.message("dataintegrity.result.notes")}</th>
        <th class="adminui-action-column">${ui.message("general.action")}</th>
    </tr>
    </thead>
    <tbody>
    <% results.each { %>
    <tr>
        <td>${ui.encodeHtmlContent(it.rule.ruleName ?: '')}</td>
        <td>${it.patient.patientId ?: it.patientProgram.patientProgramId}</td>
        <td>${ui.encodeHtmlContent(it.name ?: '')}</td>
        <td>${ui.encodeHtmlContent(it.notes ?: '')}</td>
        <td class="adminui-action-column">
            <% if (it.actionUrl != "") { %>
            <a class="icon-edit" title="${ui.message("dataintegrity.action.fix")}"
               href="/${contextPath}/${it.actionUrl}"></a>
            <% } %>

        </td>
    </tr>
    <% } %>
    </tbody>
</table>
<% } %>