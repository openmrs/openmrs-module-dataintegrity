package org.openmrs.module.dataintegrity.scheduler;

import java.util.Date;

import org.openmrs.module.dataintegrity.rule.DataIntegrityEvaluationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

/**
 * Implementation of a task that runs all the DI rules available.
 *
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
        }catch (Exception e){
            log.error("ERROR executing DataIntegrityTask : " + e.toString() + getStackTrace(e));
        }
        super.startExecuting();
    }

    public void shutdown() {
        log.debug("shutting down hello world task");
        this.stopExecuting();
    }
}
