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

import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.scheduler.TaskDefinition;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;

public class SendIntegrityCheckSummaryEmailTaskTest extends BaseModuleContextSensitiveTest {

	/**
	 * @see {@link SendIntegrityCheckSummaryEmailTask#execute()}
	 */
	@Ignore
	@Verifies(value = "should send an email and not fail", method = "execute()")
	public void execute_shouldSendAnEmailAndNotFail() throws Exception {
		DataIntegrityService service = Context.getService(DataIntegrityService.class);
		
		// generate demo data
		IntegrityCheck integrityCheck = new IntegrityCheck();
		integrityCheck.setCheckCode("select 1 from person");
		integrityCheck.setFailureThreshold("0");
		integrityCheck.setFailureOperator(DataIntegrityConstants.FAILURE_OPERATOR_GREATER_THAN);
		integrityCheck.setName("sample integrity check");
		integrityCheck.setFailureType(DataIntegrityConstants.FAILURE_TYPE_COUNT);
		integrityCheck.setCheckLanguage(DataIntegrityConstants.CHECK_LANGUAGE_SQL);
		integrityCheck = service.saveIntegrityCheck(integrityCheck);
		service.runIntegrityCheck(integrityCheck);
		
		IntegrityCheck integrityCheck2 = new IntegrityCheck();
		integrityCheck2.setCheckCode("select 1 from person");
		integrityCheck2.setFailureThreshold("10");
		integrityCheck2.setFailureOperator(DataIntegrityConstants.FAILURE_OPERATOR_GREATER_THAN);
		integrityCheck2.setName("another check");
		integrityCheck2.setFailureType(DataIntegrityConstants.FAILURE_TYPE_COUNT);
		integrityCheck2.setCheckLanguage(DataIntegrityConstants.CHECK_LANGUAGE_SQL);
		integrityCheck2 = service.saveIntegrityCheck(integrityCheck2);
		service.runIntegrityCheck(integrityCheck2);
		
		// set global properties for sending email
		AdministrationService as = Context.getAdministrationService();
		as.setGlobalProperty("dataintegrity.mail.host", "mail-relay.iu.edu");
		as.setGlobalProperty("dataintegrity.mail.port", "25");
		as.setGlobalProperty("dataintegrity.mail.protocol", "smtp");
		as.setGlobalProperty("dataintegrity.mail.auth", "true");
		as.setGlobalProperty("dataintegrity.mail.password", "I love my wife dearly");
		as.setGlobalProperty("dataintegrity.mail.user", "jkeiper");
		as.setGlobalProperty("dataintegrity.mail.tls", "true");
		as.setGlobalProperty("dataintegrity.mail.from", "jkeiper@iupui.edu");
		as.setGlobalProperty("dataintegrity.mail.subject", "Woo!!!!1");
		as.setGlobalProperty("dataintegrity.mail.format", "html");
		as.setGlobalProperty("dataintegrity.mail.serverpath", "http://172.31.16.16:8080/amrs");
		
		SendIntegrityCheckSummaryEmailTask task = new SendIntegrityCheckSummaryEmailTask();
		TaskDefinition definition = new TaskDefinition();
		definition.setProperty("emailsVal", "jeremy@openmrs.org;jkeiper@regenstrief.org");
		task.initialize(definition);

		task.execute();
	}

    @Test
    public void fakeTest(){
        // pass
    }
}