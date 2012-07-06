/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.dataintegrity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;
import org.springframework.util.StringUtils;


/**
 * This class is called by the scheduler whenever the time to run the Data
 * Integrity checks is attained.
 */
public class RunScheduledIntegrityChecksTask extends AbstractTask {

	private final Log log = LogFactory.getLog(getClass());

	private DataIntegrityService getDataIntegrityService() {
		return (DataIntegrityService) Context
				.getService(DataIntegrityService.class);
	}

	public void execute() {

		Context.openSession();
		if (!Context.isAuthenticated()) {
			authenticate();
		}

		if (Context.isAuthenticated()) {
			DataIntegrityService service = getDataIntegrityService();
			String checkIDs = taskDefinition
					.getProperty(DataIntegrityConstants.SCHEDULED_CHECKS_PROPERTY);
			if (StringUtils.hasText(checkIDs)) {
				if (checkIDs != null) {
					String[] checkList = checkIDs.split(",");

					if (checkList != null) {
						for (String checkId : checkList) {
							try {
								int id = Integer.valueOf(checkId);
								if (service.getIntegrityCheck(id) != null) {
									IntegrityCheck template = service
											.getIntegrityCheck(id);
									service.runIntegrityCheck(template);
									log.info("Ran integrity check id #" + id);
								} else {
									log.error("The integrity check id #"
											+ checkId + " is missing.");
								}
							} catch (NumberFormatException e) {
								log.error("Improperly formatted checkId: " + checkId, e);
							} catch (Exception e) {
								log.error(e);
							}
						}
					} else {
						log.error("The checklist could not be split: " + checkIDs);
					}
				}
			} else {
				log.error("The are no integrity checks scheduled");
			}

		}
	}
}
