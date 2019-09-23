<%
    ui.decorateWith("appui", "standardEmrPage")
    ui.includeCss("dataintegrity", "dintExportIcons.css")
    ui.includeJavascript("dataintegrity", "jquery.dataTables.min-1.10.15.js")
    ui.includeJavascript("dataintegrity", "dataTables.buttons.min-1.3.1.js")
    ui.includeJavascript("dataintegrity", "buttons.html5.min-1.3.1.js")
    ui.includeJavascript("dataintegrity", "jszip.min-3.1.3.js")
    ui.includeJavascript("dataintegrity", "pdfmake.min-0.1.27.js")
    ui.includeJavascript("dataintegrity", "vfs_fonts-0.1.27.js")
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
    .DataTables_sort_icon, .dt-buttons {
        display: inline-block;
        margin-left: 5px;
    }
    #filter-area {
        margin-bottom: 10px;
    }
    .export-btn > * {
        margin: 5px;
        color: black;
    }
    .hidden {
        display: none;
    }
    .col-identifier {
        width: 15%;
    }
    .col-rulename {
        width: 20%
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
        <% if (binding.variables.containsKey('rules')) { %>
        {
            label: "${ ui.message('dataintegrity.result.all.label')}",
            link: '${ui.pageLink("dataintegrity", "results")}'
        },
        {
            label: "${ui.message('dataintegrity.result.resultFor')} ${rules.ruleName}"
        }
        <% } else { %>
        {
            label: "${ ui.message("dataintegrity.result.all.label")}"
        },
        {
            label: "${ui.message('dataintegrity.result.resultFor')} ${rule.ruleName}"
        }
        <% } %>
    ];

    <% if (results.size() > 0) { %>
    const PATIENT_COLUMN = 3; // index of patient/program name column, used to filter result by patient name
    const EXPORT_COLUMNS = [ 3,1,2,4 ]; // index of columns which will get exported

    jq(document).ready(function () {
        var resultTable = jq("#list-results").DataTable({
            bFilter: true,
            bJQueryUI: true,
            bLengthChange: false,
            iDisplayLength: 10,
            sPaginationType: "full_numbers",
            bAutoWidth: false,
            sDom: 'Bt<"fg-toolbar ui-toolbar ui-corner-bl ui-corner-br ui-helper-clearfix datatables-info-and-pg "ip>',
            buttons: [
                {
                    extend: 'excelHtml5',
                    text: '<i class="icon-file-excel" title="${ui.message('dataintegrity.result.exportAsExcel')}"></i>',
                    className: 'export-btn',
                    exportOptions: {
                        columns: EXPORT_COLUMNS
                    },
                    title: "${ui.message('dataintegrity.result.documentTitle')}",
                    filename: getFileName()
                },
                {
                    extend: 'csvHtml5',
                    text: '<i class="icon-doc-text" title="${ui.message('dataintegrity.result.exportAsCsv')}"></i>',
                    className: 'export-btn',
                    exportOptions: {
                        columns: EXPORT_COLUMNS
                    },
                    title: "${ui.message('dataintegrity.result.documentTitle')}",
                    filename: getFileName()
                },
                {
                    extend: 'pdfHtml5',
                    text: '<i class="icon-file-pdf" title="${ui.message('dataintegrity.result.exportAsPdf')}"></i>',
                    className: 'export-btn',
                    exportOptions: {
                        columns: EXPORT_COLUMNS
                    },
                    title: "${ui.message('dataintegrity.result.documentTitle')}",
                    filename: getFileName()
                }
            ]
        });

        jq.fn.dataTableExt.ofnSearch['html'] = function (sData) {
            return jq(sData).val();
        };

        jq('#rule-filter').change(function () {
            resultTable.columns(0).search(jq(this).val()).draw();
            var patientList = resultTable.column(PATIENT_COLUMN, {filter : 'applied'}).data().reduce(function(a, b){
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
                    resultTable.columns(PATIENT_COLUMN).search("").draw();
                }
            },
            select: function () {
                resultTable.columns(PATIENT_COLUMN).search(jq(this).val()).draw();
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
            jq('#patient-filter').autocomplete('option', 'source', patients);
        });

        function getFileName() {
            var datetime = new Date();
            var date = getFormattedValue(datetime.getDate());
            var month = getFormattedValue(datetime.getMonth()+1);
            var year = datetime.getFullYear();
            var hours = getFormattedValue(datetime.getHours());
            var minutes = getFormattedValue(datetime.getMinutes());

            return "DataQualityViolations-" +  date + month + year + "-" + hours + minutes;
        }

        function getFormattedValue(value) {
            return value > 9 ? value : '0' + value;
        }

        resultTable.buttons().container().appendTo(jq('#export-section'), resultTable.table().container());
    });
    <% } %>
</script>

<% if (binding.variables.containsKey('rules')) { %>
    <h2>${ui.message("dataintegrity.result.all.label")}</h2>
<% } else { %>
    <h2>${ui.message('dataintegrity.result.resultFor')} ${rule.ruleName}</h2>
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
        <div id="export-section">
            <label>Export: </label>
        </div>
    </div>
<% } %>

<%
def prettify = { "$it".replaceAll("null", "").replaceAll("\\s*,\\s", " ") }
%>
<% if (results.size() == 0) { %>
    ${ui.message('dataintegrity.result.noResult')}
    <a href="results.page?action=fireRules">
        ${ ui.message('dataintegrity.rule.runAllRules') }
    </a>
    <p>${ui.message('dataintegrity.rule.runAllRulesNotes')}</p>
<% } else { %>
<table id="list-results" class="dataTable">
    <thead>
    <tr>
        <th class="hidden"></th>
        <th class="col-identifier">${ui.message('dataintegrity.result.identifier')}</th>
        <th class="col-rulename">${ui.message("dataintegrity.rule.name")}</th>
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
        <td class="hidden">rule_${it.rule.ruleId}</td>

        <td>
            <% if (it.patientProgram) { %>
                ${it.patient_program_id}
            <% } else { %>
                ${it.patient.patientIdentifier}
            <% } %>
        </td>

        <td>${ui.encodeHtmlContent(it.rule.ruleName)}</td>

        <td>
            <% if (it.patientProgram) { %>
                ${it.patientProgram}
            <% } else { %>
                <a href="/${contextPath}/coreapps/clinicianfacing/patient.page?patientId=${it.patient.uuid}">${prettify(ui.encodeHtmlContent(it.name))}</a>
            <% } %>
        </td>

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
