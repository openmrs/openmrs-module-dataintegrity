package org.openmrs.module.dataintegrity.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.DataIntegrityCheckTemplate;
import org.openmrs.module.dataintegrity.DataIntegrityService;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.web.WebConstants;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class DeleteIntegrityCheckFormController extends SimpleFormController{
	private DataIntegrityService getDataIntegrityService() {
        return (DataIntegrityService)Context.getService(DataIntegrityService.class);
    }
	
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		List<DataIntegrityCheckTemplate> list = new ArrayList<DataIntegrityCheckTemplate>();
		return list;
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (Context.isAuthenticated()) {
			map.put("existingChecks", getDataIntegrityService().getAllDataIntegrityCheckTemplates());
		}
        return map;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object obj,
            BindException errors) throws Exception {
		HttpSession httpSession = request.getSession();
		
		String view = getFormView();
		if (Context.isAuthenticated()) {
			
			MessageSourceAccessor msa = getMessageSourceAccessor();
			
			String success = "";
			String error = "";
			
			DataIntegrityService service = getDataIntegrityService();
			
			String[] checkList = request.getParameterValues("integrityCheckId");
			if (checkList != null) {
				String check = msa.getMessage("dataintegrity.deleteCheck.title");
				String deleted = msa.getMessage("dataintegrity.deleteCheck.delete");
				String notDeleted = msa.getMessage("dataintegrity.deleteCheck.notdelete");
				for (String checkId : checkList) {
					try {
						DataIntegrityCheckTemplate template = service.getDataIntegrityCheckTemplate(Integer.valueOf(checkId));
						service.deleteDataIntegrityCheckTemplate(template);
						
						if (!success.equals(""))
							success += "<br/>";
						success += check + " #" + checkId + " " + deleted;
					}
					catch (Exception e) {
						if (!error.equals(""))
							error += "<br/>";
						error += check + " #" + checkId + " " + notDeleted + e.getMessage();
					}
				}
				view = error.equals("") ? getSuccessView() : "deleteIntegrityCheck.form";
				
			} else { 
				error = msa.getMessage("dataintegrity.deleteCheck.error");
				view = "deleteIntegrityCheck.form";
			}
		
			
			if (!success.equals(""))
				httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, success);
			if (!error.equals(""))
				httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, error);
		}
		
		return new ModelAndView(new RedirectView(view));
	}
}

