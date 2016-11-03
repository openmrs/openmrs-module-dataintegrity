/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 *
 */

package org.openmrs.module.dataintegrity.scheduler;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.rule.DataIntegrityEvaluationService;
import org.openmrs.scheduler.tasks.AbstractTask;

/**
 * Implementation of a task that runs all the DI rules available.
 */
public class DataIntegrityTask extends AbstractTask {

	private static Log log = LogFactory.getLog(DataIntegrityTask.class);

	public DataIntegrityTask() {
		log.debug("DataIntegrityTask created at " + new Date());
	}

	public void execute() {
		try {
			DataIntegrityEvaluationService evaluationService = Context.getService(DataIntegrityEvaluationService.class);
			evaluationService.fireRules();
			log.debug("executing DataIntegrityTask");
		}
		catch (Exception e) {
			log.error("ERROR executing DataIntegrityTask : " + e.toString() + getStackTrace(e));
		}
		super.startExecuting();
	}

	public void shutdown() {
		log.debug("shutting down hello world task");
		this.stopExecuting();
	}
}
