package org.openmrs.module.dataintegrity;

import java.io.File;
import java.util.List;
import org.junit.Ignore;

import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;
import org.springframework.util.Assert;

public class DataIntegrityServiceTest extends BaseModuleContextSensitiveTest {

	@Test
	public void shouldSaveNewDataIntegrityTemplate() throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context
				.getService(DataIntegrityService.class);
		List<IntegrityCheck> templates = service.getAllIntegrityChecks();
		int templateCountBeforeAdding = templates.size();
		IntegrityCheck integrityCheck = new IntegrityCheck();
		integrityCheck.setName("sample integrity check");
		integrityCheck.setCheckLanguage(DataIntegrityConstants.CHECK_LANGUAGE_SQL);
		integrityCheck.setCheckCode("select 1");
		integrityCheck.setFailureType(DataIntegrityConstants.FAILURE_TYPE_COUNT);
		integrityCheck.setFailureOperator(DataIntegrityConstants.FAILURE_OPERATOR_GREATER_THAN);
		integrityCheck.setFailureThreshold("0");
                integrityCheck.setResultsLanguage(DataIntegrityConstants.RESULTS_LANGUAGE_SAME);
		integrityCheck = service.saveIntegrityCheck(integrityCheck);
		service.saveIntegrityCheck(integrityCheck);
		int templateCountAfterAdding = service
				.getAllIntegrityChecks().size();
		Assert.isTrue(templateCountBeforeAdding == (templateCountAfterAdding - 1));
	}

	// @Test
	public void shouldDeleteIntegrityTemplates() throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context
				.getService(DataIntegrityService.class);
		List<IntegrityCheck> templates = service
				.getAllIntegrityChecks();
		int templateCountBeforeAdding = templates.size();
		IntegrityCheck temp = service
				.getIntegrityCheck(19);
		service.deleteIntegrityCheck(temp);
		int templateCountAfterAdding = service
				.getAllIntegrityChecks().size();
		Assert.isTrue(templateCountBeforeAdding == (templateCountAfterAdding + 1));
	}

	// @Test
	public void shouldExecuteIntegrityCheck() throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context
				.getService(DataIntegrityService.class);
		IntegrityCheck template = service.getIntegrityCheck(19);
		service.runIntegrityCheck(template);
		Assert.notNull(template.getIntegrityCheckRuns());
	}

	/**
	 * @see {@link DataIntegrityService#runIntegrityCheck(IntegrityCheck,String)}
	 */
	@Test
	@Verifies(value = "should return results with linked integrity check", method = "runIntegrityCheck(IntegrityCheck,String)")
	public void runIntegrityCheck_shouldReturnResultsWithLinkedIntegrityCheck()
			throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context.getService(DataIntegrityService.class);

		IntegrityCheck integrityCheck = new IntegrityCheck();
		integrityCheck.setName("sample integrity check");
		integrityCheck.setCheckLanguage(DataIntegrityConstants.CHECK_LANGUAGE_SQL);
		integrityCheck.setCheckCode("select 1 from person");
		integrityCheck.setFailureType(DataIntegrityConstants.FAILURE_TYPE_COUNT);
		integrityCheck.setFailureOperator(DataIntegrityConstants.FAILURE_OPERATOR_GREATER_THAN);
		integrityCheck.setFailureThreshold("0");
                integrityCheck.setResultsLanguage(DataIntegrityConstants.RESULTS_LANGUAGE_SAME);
		integrityCheck = service.saveIntegrityCheck(integrityCheck);

		service.runIntegrityCheck(integrityCheck);
	}

}
