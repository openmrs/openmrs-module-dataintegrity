package org.openmrs.module.dataintegrity.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.IntegrityCheck;
import org.openmrs.module.dataintegrity.DataIntegrityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DataIntegrityChecksListController {
	private DataIntegrityService getDataIntegrityService() {
        return (DataIntegrityService)Context.getService(DataIntegrityService.class);
    }
	
	@RequestMapping(value="/module/dataintegrity/list.htm")
	public String listIntegrityChecks(ModelMap modelMap) throws ServletException {
        List<IntegrityCheck> checks = new ArrayList<IntegrityCheck>();
        if (Context.isAuthenticated()) {
        	checks = getDataIntegrityService().getAllIntegrityChecks(); 
        }
		modelMap.put("checks", checks);
		
        return "/module/dataintegrity/dataIntegrityChecksList";
    }
}
