package org.openmrs.module.dataintegrity.page.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.dataintegrity.api.DataIntegrityService;
import org.openmrs.module.dataintegrity.rule.DataIntegrityEvaluationService;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Show the list of rules
 */
public class ResultsPageController {
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * @param model
	 * @param dataintegrityService
	 * @param dataintegrityEvaluationService
	 * @param ruleId
	 */
	public void get(PageModel model,
	                @SpringBean("dataintegrityService") DataIntegrityService dataintegrityService,
	                @SpringBean("dataintegrityEvaluationService") DataIntegrityEvaluationService dataintegrityEvaluationService,
	                @RequestParam(value = "ruleId", required = false) String ruleId,
	                @RequestParam(value = "action", required = false) String action) {
		// default action load all results
		if ("fireRules".equals(action)) {
			log.info("Firing all data integrity rules");
			dataintegrityEvaluationService.fireRules();
		}
		if (ruleId == null || ruleId.equals("")) {
			model.addAttribute("results", dataintegrityService.getAllResults());
			model.addAttribute("rule", null);
		} else {
			log.info("Generating results for rule with uuid " + ruleId);
			// rerun a specific rule and load its results
			dataintegrityEvaluationService.fireRule(ruleId);
			model.addAttribute("results", dataintegrityService.getResultsForRuleByUuid(ruleId));
			model.addAttribute("rule", dataintegrityService.getRuleByUuid(ruleId));
		}
	}
}
