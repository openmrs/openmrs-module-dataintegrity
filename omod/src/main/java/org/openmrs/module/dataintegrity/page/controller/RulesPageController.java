package org.openmrs.module.dataintegrity.page.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.dataintegrity.api.DataIntegrityService;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;

/**
 * Show the list of rules
 */
public class RulesPageController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * @param model
	 * @param dataintegrityService
	 */
	public void get(PageModel model, @SpringBean("dataintegrityService") DataIntegrityService dataintegrityService) {
		model.addAttribute("rules", dataintegrityService.getAllRules());
	}
	
	
}
