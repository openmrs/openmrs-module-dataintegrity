package org.openmrs.module.dataintegrity.web.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.DataIntegrityCheckResultTemplate;
import org.openmrs.module.dataintegrity.DataIntegrityCheckTemplate;
import org.openmrs.module.dataintegrity.DataIntegrityService;
import org.openmrs.web.WebConstants;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class CopyCheckFormController extends SimpleFormController {
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
				try {
					int id = Integer.valueOf(checkId);
					DataIntegrityCheckTemplate template = getDataIntegrityService().getDataIntegrityCheckTemplate(id);
					DataIntegrityCheckTemplate check = new DataIntegrityCheckTemplate();
					checkName = template.getIntegrityCheckName();
					check.setIntegrityCheckName(template.getIntegrityCheckName() + " Copy");
					check.setIntegrityCheckCode(template.getIntegrityCheckCode());
					check.setIntegrityCheckType(template.getIntegrityCheckType());
					check.setIntegrityCheckResultType(template.getIntegrityCheckResultType());
					check.setIntegrityCheckFailDirective(template.getIntegrityCheckFailDirective());
					check.setIntegrityCheckFailDirectiveOperator(template.getIntegrityCheckFailDirectiveOperator());
					check.setIntegrityCheckRepairType(template.getIntegrityCheckRepairType());
					check.setIntegrityCheckRepairDirective(template.getIntegrityCheckRepairDirective());
					check.setIntegrityCheckParameters(template.getIntegrityCheckParameters());
					getDataIntegrityService().saveDataIntegrityCheckTemplate(check);
					
					success = checkName + " " + msa.getMessage("dataintegrity.copyCheck.success");
					view = getSuccessView();
				} catch (Exception e) {
					error = msa.getMessage("dataintegrity.copyCheck.error") + " " + checkName + ". Message: " + e.getMessage();
					view = "copyCheck.form";
				}
			} else {
				error = msa.getMessage("dataintegrity.runSingleCheck.blank");
				view = "copyCheck.form";
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
