package org.openmrs.module.dataintegrity.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.IntegrityCheckResults;
import org.openmrs.module.dataintegrity.IntegrityCheck;
import org.openmrs.module.dataintegrity.DataIntegrityService;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.web.WebConstants;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class IntegrityCheckFormController extends SimpleFormController {
	private String success = "";
	private String error = "";
	
	private DataIntegrityService getDataIntegrityService() {
        return (DataIntegrityService)Context.getService(DataIntegrityService.class);
    }
	
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		return "not used";
    }
	
	@Override
	protected Map<String, Object> referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (request.getParameter("checkId") != null) {
			map.put("existingCheck", getDataIntegrityService().getIntegrityCheck(Integer.parseInt(request.getParameter("checkId"))));
		}
        return map;
	}
	
	private String saveIntegrityCheck(HttpServletRequest request) {
		String checkName = "";
		String view = "";
		MessageSourceAccessor msa = getMessageSourceAccessor();
		
		try {
			String checkId = request.getParameter("checkId");
			checkName = request.getParameter("name");
			String code = request.getParameter("code");
			String checkType = request.getParameter("checkType");
			String resultType = request.getParameter("resultType");
			String fail = request.getParameter("fail");
			String failOp = request.getParameter("failOp");
			String repairType = request.getParameter("repairType");
			String clearResults = request.getParameter("clearResults"); 
			String repair = OpenmrsUtil.nullSafeEquals(repairType, "none") ? ""
					: request.getParameter("repair");
			String parameters = request.getParameter("parameters");
			
			IntegrityCheck check = new IntegrityCheck();
			if (checkId != null) {
				check.setId(Integer.valueOf(checkId));
			}
			
			String codeCopy = code;
			if (codeCopy.toLowerCase().contains("delete") || codeCopy.toLowerCase().contains("update") || codeCopy.toLowerCase().contains("insert")) {
				throw new Exception("Code will modify the database hence not allowed");
			}
			check.setName(checkName);
			check.setCheckCode(code);
			check.setCheckType(checkType);
			check.setResultType(resultType);
			check.setFailDirective(fail);
			check.setFailDirectiveOperator(failOp);
			check.setRepairType(repairType);
			check.setRepairDirective(repair);
			check.setRepairParameters(parameters);
			
			DataIntegrityService service = (DataIntegrityService)Context.getService(DataIntegrityService.class);
			service.saveIntegrityCheck(check);

			if (StringUtils.hasText(clearResults)) {
				// re-get integrity check to grab latest results
				IntegrityCheckResults results = service.getResultsForIntegrityCheck(check);
				if (results != null)
					service.deleteResults(results);
			}
			
			success = checkName + " " + msa.getMessage("dataintegrity.addeditCheck.saved");
			
			view = getSuccessView();
			return view;
			
		} catch (Exception e) {
			error = msa.getMessage("dataintegrity.addeditCheck.failed") + " " + checkName + ". Message: " + e.getMessage();
			view = "integrityCheck.form";
			return view;
		}
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object obj,
            BindException errors) throws Exception {
		HttpSession httpSession = request.getSession();
		
		String view = getSuccessView();
		if (Context.isAuthenticated()) {
			view = saveIntegrityCheck(request);
		}
		if (!success.equals("")) {
			httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, success);
			success = "";
		}
		if (!error.equals("")) {
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, error);
			error = "";
		}
		
		return new ModelAndView(new RedirectView(view));
	}
}
