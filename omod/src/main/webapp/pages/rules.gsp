<%
    ui.decorateWith("appui", "standardEmrPage")
    ui.includeCss("adminui", "adminui.css")
%>

<script type="text/javascript">
    var breadcrumbs = [
        {icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm'},
        {
            label: "${ ui.message('dataintegrity.result.all.label')}",
            link: '${ui.pageLink("dataintegrity", "results")}'
        },
        {label: "${ ui.message("dataintegrity.rule.label")}"}
    ];
</script>
<h1>Test</h1>
<h2>${ ui.message("dataintegrity.rule.label")}</h2>
<% if (rules.size() == 0) { %>
    ${ ui.message('dataintegrity.rule.noRule')}
<% } else { %>
<table id="list-rules" cellspacing="2" cellpadding="2">
    <thead>
    <tr>
        <th>${ui.message("dataintegrity.rule.name")}</th>
        <th>${ui.message("dataintegrity.result.count")}</th>
        <th class="adminui-action-column">${ui.message("general.action")}</th>
    </tr>
    </thead>
    <tbody>
    <% rules.each {  %>
    <tr>
        <td>${ ui.encodeHtmlContent(it.ruleName ?: '') }</td>
        <td>${it.results.size()}</td>
        <td class="adminui-action-column">
            <a href="results.page?action=run&ruleId=${it.uuid}" class="icon-play confirm" title="${ui.message("dataintegrity.action.run")}"></a>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>

<br>
    <a href="results.page?action=fireRules">
        ${ ui.message('dataintegrity.rule.runAllRules') }
    </a>
    <p>${ui.message('dataintegrity.rule.runAllRulesNotes')}</p>
<% } %>