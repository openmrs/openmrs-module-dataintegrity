package org.openmrs.module.dataintegrity.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.DataIntegrityCheckTemplate;
import org.openmrs.module.dataintegrity.DataIntegrityService;
import org.openmrs.web.WebConstants;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class RunSingleCheckListController extends SimpleFormController {
	private DataIntegrityService getDataIntegrityService() {
        return (DataIntegrityService)Context.getService(DataIntegrityService.class);
    }
	
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
        List<DataIntegrityCheckTemplate> checks = new ArrayList<DataIntegrityCheckTemplate>();
        if (Context.isAuthenticated()) {
        	checks = getDataIntegrityService().getAllDataIntegrityCheckTemplates(); 
        }
        return checks;
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
				success = "ela kiri";
			} else {
				error = "huta";
			}
			
			if (!success.equals(""))
				httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, success);
			if (!error.equals(""))
				httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, error);
		}
		view = "results.list";
		System.out.println("*******************************" + view);
		ModelAndView model = new ModelAndView(new RedirectView(view));
		//model.addObject("attribute", "baduma thamai");
		return model;
	}
}
