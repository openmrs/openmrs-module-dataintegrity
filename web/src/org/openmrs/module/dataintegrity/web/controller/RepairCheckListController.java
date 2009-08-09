package org.openmrs.module.dataintegrity.web.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.DataIntegrityCheckTemplate;
import org.openmrs.module.dataintegrity.DataIntegrityConstants;
import org.openmrs.module.dataintegrity.DataIntegrityService;
import org.openmrs.module.dataintegrity.IntegrityCheckUtil;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class RepairCheckListController extends SimpleFormController {
	private DataIntegrityService getDataIntegrityService() {
        return (DataIntegrityService)Context.getService(DataIntegrityService.class);
    }
	
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		return "not used";
    }
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		MessageSourceAccessor msa = getMessageSourceAccessor();
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		String stringCheckId = request.getParameter("checkId");
		if (stringCheckId != null) {
			//First get the check which was executed
			int checkId = Integer.valueOf(stringCheckId);
			DataIntegrityCheckTemplate template = getDataIntegrityService().getDataIntegrityCheckTemplate(checkId);
			map.put("checkName", template.getIntegrityCheckName());
			map.put("checkId", stringCheckId);
			String repairType = template.getIntegrityCheckRepairType();
			//Check its repair type
			if (repairType.equals(DataIntegrityConstants.REPAIR_TYPE_INSTRUCTIONS)) {
				map.put("repairCheckInstructions", template.getIntegrityCheckRepairDirective());
			} else if (repairType.equals(DataIntegrityConstants.REPAIR_TYPE_SCRIPT)) {
				try {
					getDataIntegrityService().repairDataIntegrityCheckViaScript(template);
					String success = msa.getMessage("dataintegrity.repair.success") + " " + template.getIntegrityCheckName();
					map.put("repairCheckScript", success);
				} catch (Exception e) {
					String error = msa.getMessage("dataintegrity.repair.error") + " " + template.getIntegrityCheckName() + ". Message: " + e.getMessage();
					map.put("repairCheckScript", error);
					Writer writer = new StringWriter();
					PrintWriter printWriter = new PrintWriter(writer);
					e.printStackTrace(printWriter);
					session.setAttribute(DataIntegrityConstants.DATA_INTEGRITY_ERROR_STACK_TRACE, writer.toString());
				}
			} else if (repairType.equals(DataIntegrityConstants.REPAIR_TYPE_LINK)) {
				if (session.getAttribute("failedRecords") != null && template.getIntegrityCheckRepairDirective().contains("{result}")) {
					Map<Integer, List<Object[]>> recordMap = (Map<Integer, List<Object[]>>) session.getAttribute("failedRecords");
					List<Object[]> records = recordMap.get(checkId);
					String repairDirective = template.getIntegrityCheckRepairDirective();
					StringBuffer repairList = new StringBuffer();
					repairList.append("<table><tr><th>" + msa.getMessage("dataintegrity.checksList.columns.id") + "</th><th>" + msa.getMessage("dataintegrity.repair.repair") + "</th></tr>");
					String url = IntegrityCheckUtil.getWebAppUrl(request);
					String rowStyle = "";
					for (int rowIndex=0; rowIndex<records.size(); rowIndex++) {
						if (rowIndex % 2 == 0) {
							rowStyle = "evenRow";
						} else {
							rowStyle = "oddRow";
						}
						repairList.append("<tr class=\"" + rowStyle + "\">");
						Object[] rec = records.get(rowIndex);
						String id = rec[0].toString();
						repairList.append("<td>" + id + "</td>");
						String newUrl = repairDirective.startsWith("/") ? url + repairDirective.replace("{result}", id) : url + "/" + repairDirective.replace("{result}", id);
						String href = "<a target=\"_blank\" href=\"" + newUrl + "\">" + newUrl + "</a>";
						repairList.append("<td>" + href + "</td>");
						repairList.append("</tr>");
					}
					repairList.append("</table>");
					map.put("repairCheckLink", repairList.toString());
				} else {
					String href = "<a target=\"_blank\" href=\"" + template.getIntegrityCheckRepairDirective() + "\">" + template.getIntegrityCheckRepairDirective() + "</a>";
					map.put("repairCheckLink", href);
				}
			} else if (repairType.equals(DataIntegrityConstants.REPAIR_TYPE_NONE)) {
				map.put("repairCheckNone", "none");
			}
		}
		return map;
	}
	
}
