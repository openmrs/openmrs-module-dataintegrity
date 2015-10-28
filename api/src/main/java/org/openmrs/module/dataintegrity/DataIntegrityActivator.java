package org.openmrs.module.dataintegrity;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.dataintegrity.api.DataIntegrityService;
import org.openmrs.module.dataintegrity.rule.RuleDefinition;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
public class DataIntegrityActivator extends BaseModuleActivator {

    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public void started() {
        log.info("Starting Data Integrity Module");
        // load all the rules from context
        Iterator i = Context.getRegisteredComponents(RuleDefinition.class).iterator();
        DataIntegrityService dis = Context.getService(DataIntegrityService.class);
    
        while(i.hasNext()) {
            RuleDefinition definition = (RuleDefinition) i.next();
	        // TODO: Is the check for Interface and Abstract classes necessary?
            if (definition.getRule() != null) {
                DataIntegrityRule rule = dis.saveRule(definition.getRule());
                log.info("DataIntegrityRule " + rule.toString() + " saved to database");
            }
        }
        log.info("Data Integrity Module Started");
    }

    @Override
    public void stopped() {
        log.info("Shutting down Data Integrity Module");
    }

}