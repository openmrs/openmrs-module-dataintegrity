package org.openmrs.module.dataintegrity.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.DataIntegrityService;
import org.openmrs.module.dataintegrity.DataIntegrityTemplate;
import org.openmrs.web.WebConstants;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class IntegrityCheckFormController extends SimpleFormController {
	private DataIntegrityService getDataIntegrityService() {
        return (DataIntegrityService)Context.getService(DataIntegrityService.class);
    }
	
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
        return "not used";
    }
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (request.getParameter("checkId") != null) {
			map.put("existingCheck", getDataIntegrityService().getDataIntegrityTemplate(Integer.parseInt(request.getParameter("checkId"))));
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
			System.out.println(request.getRequestURL());
			String success = "";
			String error = "";
			String checkName = "";
			
			if (checkId != null) {
				try {
					checkName = request.getParameter("name");
					String checkSql = request.getParameter("sql");
					Double checkScore= Double.parseDouble(request.getParameter("score"));
					DataIntegrityTemplate check = new DataIntegrityTemplate();
					check.setIntegrityCheckId(Integer.parseInt(checkId));
					check.setIntegrityCheckName(checkName);
					check.setIntegrityCheckSql(checkSql);
					check.setIntegrityCheckScore(checkScore);
					DataIntegrityService service = (DataIntegrityService)Context.getService(DataIntegrityService.class);
					service.saveDataIntegrityTemplate(check);
					success = checkName + " " + msa.getMessage("dataintegrity.addeditCheck.saved");
				} catch (Exception e) {
					error = msa.getMessage("dataintegrity.addeditCheck.failed") + " " + checkName;
				}
			} else {
				try {
					checkName = request.getParameter("name");
					String checkSql = request.getParameter("sql");
					Double checkScore= Double.parseDouble(request.getParameter("score"));
					DataIntegrityTemplate check = new DataIntegrityTemplate();
					check.setIntegrityCheckName(checkName);
					check.setIntegrityCheckSql(checkSql);
					check.setIntegrityCheckScore(checkScore);
					DataIntegrityService service = (DataIntegrityService)Context.getService(DataIntegrityService.class);
					service.saveDataIntegrityTemplate(check);
					success = checkName + " " + msa.getMessage("dataintegrity.addeditCheck.saved");
				} catch (Exception e) {
					error = msa.getMessage("dataintegrity.addeditCheck.failed") + " " + checkName;
				}
			}
			
			view = getSuccessView();
			if (!success.equals(""))
				httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, success);
			if (!error.equals(""))
				httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, error);
		}
		
		return new ModelAndView(new RedirectView(view));
	}
}
