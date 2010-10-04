package org.openmrs.module.dataintegrity.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.DataIntegrityCheckTemplate;
import org.openmrs.module.dataintegrity.DataIntegrityService;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class DataIntegrityChecksListController extends SimpleFormController{
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
}
