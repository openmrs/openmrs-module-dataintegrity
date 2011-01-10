package org.openmrs.module.dataintegrity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.DAOException;
import org.openmrs.scheduler.tasks.AbstractTask;


/**
 * This class is called by the scheduler whenever the time to run the Data
 * Integrity checks is attained.
 */
public class ScheduledIntegrityChecks extends AbstractTask {

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
			if (taskDefinition.getProperty("integrityCheckId") != null) {
				String checkID = taskDefinition.getProperty("integrityCheckId");
				if (checkID != null) {
					String[] checkList = checkID.split(",");

					if (checkList != null) {
						for (String checkId : checkList) {
							try {
								int id = Integer.valueOf(checkId);
								if (service.getIntegrityCheck(id) != null) {
									IntegrityCheck template = service
											.getIntegrityCheck(id);
									String parameterValues = "";
									// TODO : Handle parameter values
									service.runIntegrityCheck(template,
											parameterValues);
								} else {
									log.error("The integrity check id ["
											+ checkId + "] is missing.");
								}
							} catch (NumberFormatException n) {
								log.error("Improperly formatted checkId: "
										+ checkId);
							} catch (DAOException d) {
								log.error("The task has encountered a database problem: "
										+ d.toString());
							} catch (Exception e) {
								log.error("FAILED: The integrity check could not be performed."
										+ e.toString());
							}
						}

					}
				}
			} else {
				log.error("The are no integrity checks scheduled");
			}

		}
	}
}
