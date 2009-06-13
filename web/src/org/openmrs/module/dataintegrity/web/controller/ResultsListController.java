package org.openmrs.module.dataintegrity.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openmrs.PersonAttributeType;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.DataIntegrityCheckResultTemplate;
import org.openmrs.module.dataintegrity.DataIntegrityService;
import org.openmrs.module.dataintegrity.DataIntegrityCheckTemplate;
import org.openmrs.web.WebConstants;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class ResultsListController extends SimpleFormController {
	private DataIntegrityService getDataIntegrityService() {
        return (DataIntegrityService)Context.getService(DataIntegrityService.class);
    }
	
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		return "not used";
    }
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		List<DataIntegrityCheckResultTemplate> results;
		if (session.getAttribute("singleCheckResults") != null) {
			results = (List<DataIntegrityCheckResultTemplate>) session.getAttribute("singleCheckResults");
			map.put("checkResults", results);
			map.put("single", true);
			session.removeAttribute("singleCheckResults");
		} else if (session.getAttribute("multipleCheckResults") != null) {
			results = (List<DataIntegrityCheckResultTemplate>) session.getAttribute("multipleCheckResults");
			session.removeAttribute("multipleCheckResults");
			map.put("single", false);
			map.put("checkResults", results);
		}
		return map;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object obj,
            BindException errors) throws Exception {
		HttpSession httpSession = request.getSession();
		MessageSourceAccessor msa = getMessageSourceAccessor();
		
		String view = getFormView();
		if (Context.isAuthenticated()) {
			String checkId = request.getParameter("checkId");
			String success = "";
			String error = "";
			String checkName = "";
			
			if (checkId != null) {
				try {
					int id = Integer.valueOf(checkId);
					DataIntegrityCheckTemplate template = getDataIntegrityService().getDataIntegrityCheckTemplate(id);
					checkName = template.getIntegrityCheckName();
					DataIntegrityCheckResultTemplate resultTemplate = getDataIntegrityService().runIntegrityCheck(template);
					List<DataIntegrityCheckResultTemplate> result = new ArrayList<DataIntegrityCheckResultTemplate>();
					result.add(resultTemplate);
					httpSession.setAttribute("singleCheckResults", result);
					success = checkName + " " + msa.getMessage("dataintegrity.runSingleCheck.success");
					view = getSuccessView();
				} catch (Exception e) {
					error = msa.getMessage("dataintegrity.runSingleCheck.error") + " " + checkName;
					view = "runSingleCheck.list";
				}
			} else {
				error = msa.getMessage("dataintegrity.runSingleCheck.blank");
				view = "runSingleCheck.list";
			}
			
			if (!success.equals(""))
				httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, success);
			if (!error.equals(""))
				httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, error);
		}
		view = getSuccessView();
		ModelAndView model = new ModelAndView(new RedirectView(view));
		return model;
	}
	
	
}
