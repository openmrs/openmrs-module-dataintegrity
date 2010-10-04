package org.openmrs.module.dataintegrity.web.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openmrs.api.context.Context;
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

public class TransferCheckListController extends SimpleFormController {
	private DataIntegrityService getDataIntegrityService() {
        return (DataIntegrityService)Context.getService(DataIntegrityService.class);
    }
	
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		return "not used";
    }
	
	@Override
	protected Map<String, Object> referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (Context.isAuthenticated()) {
			map.put("existingChecks", getDataIntegrityService().getAllDataIntegrityCheckTemplates());
		}
        return map; //return all existing integrity checks
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object obj,
            BindException errors) throws Exception {
		HttpSession httpSession = request.getSession(); 
		MessageSourceAccessor msa = getMessageSourceAccessor();
		String success = "";
		String error = "";
		String view = getFormView();
		String[] checkList = request.getParameterValues("integrityCheckId"); //Get the list of integrity check IDs
		if (checkList == null) { //when uploading checkList = null
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
								template.setCheckType(check.getCheckType());
								template.setCheckCode(check.getCheckCode());
								template.setFailDirective(check.getCheckFailDirective());
								template.setFailDirectiveOperator(check.getCheckFailDirectiveOperator());
								template.setName(check.getCheckName());
								template.setRepairDirective(check.getCheckRepairDirective());
								template.setResultType(check.getCheckResultType());
								template.setRepairType(check.getCheckRepairType());
								template.setRepairParameters(check.getCheckParameters());
								getDataIntegrityService().saveDataIntegrityCheckTemplate(template);
								success += check.getCheckName() + " " + msa.getMessage("dataintegrity.upload.success") + "<br />";
							}
						}
						catch (Exception e) {
							error = msa.getMessage("dataintegrity.upload.fail") + ". Message: " + e.getMessage();
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
		} else {
			//exporting integrity checks to a XML file 
			try {
				DataIntegrityService service = getDataIntegrityService();
				File exportFile = IntegrityCheckUtil.getExportIntegrityCheckFile();
				FileWriter fstream = new FileWriter(exportFile);
		        BufferedWriter out = new BufferedWriter(fstream);
				StringBuffer exportString = new StringBuffer();
				exportString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<checks>\r\n");
				for (String checkId : checkList) {
					DataIntegrityCheckTemplate template = service.getDataIntegrityCheckTemplate(Integer.valueOf(checkId));
					exportString.append("\t<check type=\"" + template.getCheckType() + "\">\r\n");
					exportString.append("\t\t<name>" + template.getName() + "</name>\r\n");
					exportString.append("\t\t<code>" + template.getCheckCode() + "</code>\r\n");
					exportString.append("\t\t<resultType>" + template.getResultType() + "</resultType>\r\n");
					exportString.append("\t\t<fail operator=\"" + template.getFailDirectiveOperator() + "\">" + template.getFailDirective() + "</fail>\r\n");
					if (!template.getRepairType().equals("none")) {
						exportString.append("\t\t<repair type=\"" + template.getRepairType() + "\">" + template.getRepairDirective() + "</repair>\r\n");
					}
					if (!template.getRepairParameters().equals("")) {
						exportString.append("\t\t<parameters>" + template.getRepairParameters() + "</parameters>\r\n");
					}
					exportString.append("\t</check>\r\n");
				}
				exportString.append("</checks>\r\n");
				out.write(exportString.toString());
				out.close();
				
				//Zip the file
				File zipFile = zipExportFile(exportFile);
				//Downloading the file
				FileInputStream fileToDownload = new FileInputStream(zipFile);
				ServletOutputStream output = response.getOutputStream();
				response.setContentType("application/octet-stream");
				String exportFileName = request.getParameter("fileName");
				String header = (exportFileName == null || exportFileName.equals("")) ? "attachment; filename=IntegrityChecks.zip" : "attachment; filename=" + exportFileName + ".zip";
				response.setHeader("Content-Disposition", header);
				response.setContentLength(fileToDownload.available());
				int c;
				while ((c = fileToDownload.read()) != -1)
				{
					output.write(c);
				}
				output.flush();
				output.close();
				fileToDownload.close();
				zipFile.delete();
				exportFile.delete();
			} catch (Exception e) {
				error = msa.getMessage("dataintegrity.upload.export.error") + ". Message: " + e.getMessage();
			}
		}
		view = getSuccessView();
		if (!success.equals(""))
			httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, success);
		
		if (!error.equals(""))
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, error);
		return new ModelAndView(new RedirectView(view));
	}
	
	private File zipExportFile(File file) throws Exception {
	    byte[] buf = new byte[1024];
	    
	    try {
	    	File zipFile = IntegrityCheckUtil.getZippedExportIntegrityCheckFile();
	        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
	    
	        // Compress the files
            FileInputStream in = new FileInputStream(file);
    
            // Add ZIP entry to output stream.
            out.putNextEntry(new ZipEntry(file.getName()));
    
            // Transfer bytes from the file to the ZIP file
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
    
            // Complete the entry
            out.closeEntry();
            in.close();
	    
	        // Complete the ZIP file
	        out.close();
	        return zipFile;
	    } catch (Exception e) {
	    	throw new Exception("Failed to zip file. " + e.getMessage());
	    }
		

	}

}
