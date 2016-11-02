package org.openmrs.module.dataintegrity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.BaseModuleActivator;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
public class Activator extends BaseModuleActivator {

    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public void started() {
        log.info("Starting Data Integrity Module");
    }

    @Override
    public void stopped() {
        log.info("Shutting down Data Integrity Module");
    }

}