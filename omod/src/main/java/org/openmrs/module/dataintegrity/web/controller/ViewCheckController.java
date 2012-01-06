/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.dataintegrity.web.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.DataIntegrityConstants;
import org.openmrs.module.dataintegrity.DataIntegrityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author jkeiper
 */
@Controller
public class ViewCheckController {
	
	@RequestMapping(value="/module/dataintegrity/view.htm")
	public String viewCheck(@RequestParam(value="checkId", required=true) Integer checkId, ModelMap modelMap) {
		DataIntegrityService service = Context.getService(DataIntegrityService.class);
		modelMap.put("check", service.getIntegrityCheck(checkId));
		return "/module/dataintegrity/viewCheck";
	}

}
