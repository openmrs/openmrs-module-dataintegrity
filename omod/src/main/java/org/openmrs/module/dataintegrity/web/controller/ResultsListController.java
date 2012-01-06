package org.openmrs.module.dataintegrity.web.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.DataIntegrityConstants;
import org.openmrs.module.dataintegrity.DataIntegrityService;
import org.openmrs.module.dataintegrity.IntegrityCheck;
import org.openmrs.module.dataintegrity.IntegrityCheckResult;
import org.openmrs.module.dataintegrity.IntegrityCheckResults;
import org.openmrs.module.dataintegrity.QueryResults;
import org.openmrs.web.WebConstants;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class ResultsListController extends SimpleFormController {
	private DataIntegrityService getDataIntegrityService() {
		return (DataIntegrityService) Context
				.getService(DataIntegrityService.class);
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws ServletException {
		return "not used";
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map<String, Object> referenceData(HttpServletRequest request,
			Object command, Errors errors) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		List<IntegrityCheckResults> results = null;
		HttpSession session = request.getSession();

		// check for specifically requested results
		String checkId = request.getParameter("checkId");
		Integer checkNo = null;
		if (StringUtils.hasText(checkId)) {
			try {
				checkNo = Integer.parseInt(checkId);
			} catch (NumberFormatException e) {
				throw new APIException("cannot find IntegrityCheck #" + checkId);
			}
			DataIntegrityService ds = (DataIntegrityService) Context.getService(DataIntegrityService.class);
			IntegrityCheck check = ds.getIntegrityCheck(checkNo);
			Set<IntegrityCheckResult> res = check.getIntegrityCheckResults();
			if (res != null) {
				results = new ArrayList<IntegrityCheckResults>();
				// results.add(res);
				map.put("checkResults", results);
				map.put("single", true);
			}
		} else {
			// try the old way ...
			if (session.getAttribute("singleCheckResults") != null) {
				results = (List<IntegrityCheckResults>) session
						.getAttribute("singleCheckResults");
				map.put("checkResults", results);
				map.put("single", true);
				session.removeAttribute("singleCheckResults");
			} else if (session.getAttribute("multipleCheckResults") != null) {
				results = (List<IntegrityCheckResults>) session
						.getAttribute("multipleCheckResults");
				session.removeAttribute("multipleCheckResults");
				map.put("single", false);
				map.put("checkResults", results);
			}
		}
		if (results != null) {
			Map<Integer, QueryResults> failedRecordMap = new HashMap<Integer, QueryResults>();
			for (int i = 0; i < results.size(); i++) {
				IntegrityCheckResults resultTemplate = results
						.get(i);
				failedRecordMap.put(resultTemplate.getIntegrityCheck().getId(),
						resultTemplate.getFailedRecords());
			}
			session.setAttribute("failedRecords", failedRecordMap);
		}
		return map;
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object obj, BindException errors)
			throws Exception {
		HttpSession httpSession = request.getSession();
		MessageSourceAccessor msa = getMessageSourceAccessor();

		if (httpSession.getAttribute("failedRecords") != null) {
			httpSession.removeAttribute("failedRecords");
		}

		String view = getFormView();
		if (Context.isAuthenticated()) {
			String checkId = request.getParameter("checkId");
			String success = "";
			String error = "";
			String checkName = "";
			String stack = "";

			if (checkId != null) {
				try {
					int id = Integer.valueOf(checkId);
					IntegrityCheck template = getDataIntegrityService()
							.getIntegrityCheck(id);
					checkName = template.getName();
					getDataIntegrityService().runIntegrityCheck(template);
					List<IntegrityCheckResults> result = new ArrayList<IntegrityCheckResults>();
					// result.add(resultTemplate);
					httpSession.setAttribute("singleCheckResults", result);
					success = checkName
							+ " "
							+ msa.getMessage("dataintegrity.runSingleCheck.success");
					view = getSuccessView();
				} catch (Exception e) {
					error = msa
							.getMessage("dataintegrity.runSingleCheck.error")
							+ " "
							+ checkName
							+ ". Message: "
							+ e.getMessage()
							+ "<br />";
					Writer writer = new StringWriter();
					PrintWriter printWriter = new PrintWriter(writer);
					e.printStackTrace(printWriter);
					stack = writer.toString();
					view = "runSingleCheck.list";
				}
			} else {
				error = msa.getMessage("dataintegrity.runSingleCheck.blank");
				view = "runSingleCheck.list";
			}

			if (StringUtils.hasText(success))
				httpSession
						.setAttribute(WebConstants.OPENMRS_MSG_ATTR, success);
			if (StringUtils.hasText(error))
				httpSession
						.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, error);
			if (StringUtils.hasText(stack))
				httpSession
						.setAttribute(
								DataIntegrityConstants.DATA_INTEGRITY_ERROR_STACK_TRACE,
								stack);
		}
		view = getSuccessView();
		ModelAndView model = new ModelAndView(new RedirectView(view));
		return model;
	}

}
