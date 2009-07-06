package org.openmrs.module.dataintegrity.web.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openmrs.api.context.Context;
import org.openmrs.module.Module;
import org.openmrs.module.ModuleException;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.ModuleUtil;
import org.openmrs.module.dataintegrity.DataIntegrityCheckTemplate;
import org.openmrs.module.dataintegrity.DataIntegrityService;
import org.openmrs.module.dataintegrity.DataIntegrityXmlFileParser;
import org.openmrs.module.dataintegrity.IDataIntegrityCheckUpload;
import org.openmrs.module.dataintegrity.IntegrityCheckUtil;
import org.openmrs.web.WebConstants;
import org.openmrs.web.WebUtil;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class UploadCheckListController extends SimpleFormController {
	private DataIntegrityService getDataIntegrityService() {
        return (DataIntegrityService)Context.getService(DataIntegrityService.class);
    }
	
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		return "not used";
    }
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
        return map;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object obj,
            BindException errors) throws Exception {
		HttpSession httpSession = request.getSession(); 
		MessageSourceAccessor msa = getMessageSourceAccessor();
		String success = "";
		String error = "";
		String view = getFormView();
		if (Context.isAuthenticated() && request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile multipartCheckFile = multipartRequest.getFile("checkFile");
			if (multipartCheckFile != null && !multipartCheckFile.isEmpty()) {
				String filename = WebUtil.stripFilename(multipartCheckFile.getOriginalFilename());
				if (filename.toLowerCase().endsWith("xml")) {
					InputStream inputStream = null;
					File checkFile = null;
					try {
						inputStream = multipartCheckFile.getInputStream();
						checkFile = IntegrityCheckUtil.uploadIntegrityCheckFile(inputStream, filename);
						DataIntegrityXmlFileParser fileParser = new DataIntegrityXmlFileParser(checkFile);
						List<IDataIntegrityCheckUpload> checksToUpload = fileParser.getChecksToAdd();
						for (int i=0; i<checksToUpload.size(); i++) {
							IDataIntegrityCheckUpload check = checksToUpload.get(i);
							DataIntegrityCheckTemplate template = new DataIntegrityCheckTemplate();
							template.setIntegrityCheckType(check.getCheckType());
							template.setIntegrityCheckCode(check.getCheckCode());
							template.setIntegrityCheckFailDirective(check.getCheckFailDirective());
							template.setIntegrityCheckFailDirectiveOperator(check.getCheckFailDirectiveOperator());
							template.setIntegrityCheckName(check.getCheckName());
							template.setIntegrityCheckRepairDirective(check.getCheckRepairDirective());
							template.setIntegrityCheckResultType(check.getCheckResultType());
							template.setIntegrityCheckRepairType(check.getCheckRepairType());
							template.setIntegrityCheckParameters(check.getCheckParameters());
							getDataIntegrityService().saveDataIntegrityCheckTemplate(template);
							success += check.getCheckName() + " " + msa.getMessage("dataintegrity.upload.success") + "<br />";
						}
					}
					catch (Exception e) {
						error = msa.getMessage("dataintegrity.upload.fail") + ". " + e.getMessage();
						if (checkFile != null) {
							checkFile.delete();
						}
					}
					finally {
						// clean up the check repository folder
						try {
							if (inputStream != null)
								inputStream.close();
						}
						catch (IOException io) {
						}
					}
				} else {
					error = msa.getMessage("dataintegrity.upload.xml");
				}
			}
		}
		view = getSuccessView();
		if (!success.equals(""))
			httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, success);
		
		if (!error.equals(""))
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, error);
		return new ModelAndView(new RedirectView(view));
	}

}
