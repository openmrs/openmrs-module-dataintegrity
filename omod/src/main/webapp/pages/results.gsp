<%
    ui.decorateWith("appui", "standardEmrPage")
    ui.includeJavascript("dataintegrity", "jquery.dataTables.min-1.10.15.js")
%>
<style>
    .ui-autocomplete {
        max-height: 100px;
        overflow-y: auto;
        overflow-x: hidden;
    }
    #patient-filter, #free-text-search {
        padding: 5px 10px;
        border: 1px solid #ccc;
        width: 20%;
    }
    #patient-filter-label, #free-text-search-label {
        margin-left: 5px;
        margin-right: 5px;
    }
    .DataTables_sort_icon {
        display: inline-block;
        margin-left: 5px;
    }
    #filter-area {
        margin-bottom: 10px;
    }
</style>
<script type="text/javascript">
    var patients = [];
    var breadcrumbs = [
        {icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm'},
        {
            label: "${ ui.message('dataintegrity.rule.label')}",
            link: '${ui.pageLink("dataintegrity", "rules")}'
        },
        <% if (binding.variables.containsKey('rules') && rules) { %>
        {
            label: "${ ui.message('dataintegrity.result.all.label')}",
            link: '${ui.pageLink("dataintegrity", "results")}'
        },
        {
            label: "Results for ${rules.ruleName}"
        }
        <% } else { %>
        {
            label: "${ ui.message("dataintegrity.result.all.label")}"
        },
        {
            label: "Results for ${rule.ruleName}"
        }
        <% } %>
    ];

    <% if (results.size() > 0) { %>
    jq(document).ready(function () {
        var resultTable = jq("#list-results").DataTable({
            bFilter: true,
            bJQueryUI: true,
            bLengthChange: false,
            iDisplayLength: 10,
            sPaginationType: "full_numbers",
            bAutoWidth: false,
            sDom: 't<"fg-toolbar ui-toolbar ui-corner-bl ui-corner-br ui-helper-clearfix datatables-info-and-pg "ip>'
        });

        jq.fn.dataTableExt.ofnSearch['html'] = function (sData) {
            return jq(sData).val();
        };

        jq('#rule-filter').change(function () {
            resultTable.columns(0).search(jq(this).val()).draw();
            var patientList = resultTable.column(2, {filter : 'applied'}).data().reduce(function(a, b){
                var link = jq(b);
                a.push(link.text());
                return a;
            }, []);
            jq('#patient-filter').autocomplete('option', 'source', patientList);
        });

        jq("#patient-filter").autocomplete({
            minLength: 0,
            delay: 0,
            source: Array.from(new Set(patients)),
            close: function () {
                if (jq(this).val() === "") {
                    resultTable.columns(2).search("").draw();
                }
            },
            select: function () {
                resultTable.columns(2).search(jq(this).val()).draw();
            }
        });

        jq('#free-text-search').keyup(function(){
            resultTable.search(jq(this).val()).draw();
        });

        jq('#reset-filters').click(function(){
            // Reset the table and clear all the filters
            resultTable.search('').columns().search('').draw();
            jq('#rule-filter').prop('selectedIndex', 0);
            jq('#patient-filter').val("");
            jq('#free-text-search').val("");
        });
    });
    <% } %>
</script>

<% if (binding.variables.containsKey('rules')) { %>
    <h2>${ui.message("dataintegrity.result.all.label")}</h2>
<% } else { %>
    <h2>Results for ${rule.ruleName}</h2>
<% } %>

<% if (results.size() > 0) { %>
    <div id="filter-area">
        <% if (binding.variables.containsKey('rules') && rules) { %>
        <label for="rule-filter">Rule:
            <select id="rule-filter">
                <option value="">Select rule</option>
                <% rules.each { %>
                <option value="rule_${it.ruleId}">${ui.encodeHtmlContent(it.ruleName)}</option>
                <% } %>
            </select>
        </label>
        <% } %>
        <label id="patient-filter-label" for="patient-filter">Patient:
            <input id="patient-filter">
        </label>
        <label id="free-text-search-label" for="free-text-search">Search:
            <input id="free-text-search">
        </label>
        <button id="reset-filters">Clear</button>
    </div>
<% } %>

<%
def prettify = { "$it".replaceAll("null", "").replaceAll("\\s*,\\s", " ") }
%>
<% if (results.size() == 0) { %>
    There are no results to show. <a href="results.page?action=fireRules"><i class="icon-play"></i>&nbsp;Click here</a> to run all the validation rules, please note that it will take some time and slow down the server.
<% } else { %>
<table id="list-results" cellspacing="0" cellpadding="2" width="100%" class="dataTable">
    <thead>
    <tr>
        <th style="display: none"></th>
        <th>${ui.message("dataintegrity.rule.name")}</th>
        <th>${ui.message("dataintegrity.result.name")}</th>
        <th>${ui.message("dataintegrity.result.notes")}</th>
        <th class="adminui-action-column">${ui.message("general.action")}</th>
    </tr>
    </thead>
    <tbody>
    <% results.each { %>
    <script>
        patients.push("${prettify(ui.encodeHtmlContent(it.name))}");
    </script>
    <tr>
        <td style="display: none">rule_${it.rule.ruleId}</td>
        <td>${ui.encodeHtmlContent(it.rule.ruleName)}</td>
        <% if (it.patientProgram) { %>
            <td>${it.patientProgram}</td>
        <% } else { %>
            <td><a href="/${contextPath}/coreapps/clinicianfacing/patient.page?patientId=${it.patient.uuid}">${prettify(ui.encodeHtmlContent(it.name))}</a></td>
        <% } %>
        <td>${ui.encodeHtmlContent(it.notes ?: '')}</td>
        <td class="adminui-action-column">
            <% if (it.actionUrl != "") { %>
            <a href="/${contextPath}/${it.actionUrl}"><span class="icon-edit"></span>${ui.message("dataintegrity.action.fix")}</a>
            <% } %>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>
<% } %>
