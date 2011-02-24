package org.openmrs.module.dataintegrity;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;
import org.springframework.util.Assert;

public class DataIntegrityServiceTest extends BaseModuleContextSensitiveTest {

//	@Before
//	public void runBeforeEachTest() throws Exception {
//		authenticate();
//	}

	// @Test
	public void shouldGetOneDataIntegrityTemplate() throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context
				.getService(DataIntegrityService.class);
		IntegrityCheck template = service
				.getIntegrityCheck(19);
		Assert.notNull(template);
	}

	// @Test
	public void shouldGetAllDataIntegrityTemplates() throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context
				.getService(DataIntegrityService.class);
		List<IntegrityCheck> templates = service
				.getAllIntegrityChecks();
		System.out.println("Templates received: " + templates);
		Assert.notNull(templates);
	}

	@Test
	public void shouldSaveNewDataIntegrityTemplate() throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context
				.getService(DataIntegrityService.class);
		List<IntegrityCheck> templates = service.getAllIntegrityChecks();
		int templateCountBeforeAdding = templates.size();
		IntegrityCheck integrityCheck = new IntegrityCheck();
		integrityCheck.setCheckCode("select 1");
		integrityCheck.setFailDirective("0");
		integrityCheck
				.setFailDirectiveOperator(DataIntegrityConstants.FAILURE_OPERATOR_GREATER_THAN);
		integrityCheck.setName("sample integrity check");
		integrityCheck.setRepairParameters(null);
		integrityCheck.setRepairDirective(null);
		integrityCheck.setRepairType(DataIntegrityConstants.REPAIR_TYPE_NONE);
		integrityCheck.setResultType(DataIntegrityConstants.RESULT_TYPE_COUNT);
		integrityCheck.setCheckType(DataIntegrityConstants.CHECK_TYPE_SQL);
		integrityCheck.setRepairCodeType(DataIntegrityConstants.NONE);
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
		IntegrityCheckResults result = service.runIntegrityCheck(
				template, "1");
		Assert.notNull(result);
		List<Object[]> records = result.getFailedRecords();

		for (int i = 0; i < records.size(); i++) {
			System.out.println(records.get(i));
		}
	}

	// @Test
	public void shouldUploadDataIntegrityCheck() throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context
				.getService(DataIntegrityService.class);
		List<IntegrityCheck> templates = service
				.getAllIntegrityChecks();
		int templateCountBeforeAdding = templates.size();

		File file = new File(
				"F:\\GSOC2009\\workspaces\\DataIntegrityModule\\test\\org\\openmrs\\module\\dataintegrity\\test\\test.xml");
		DataIntegrityXmlFileParser parser = new DataIntegrityXmlFileParser(file);
		List<IDataIntegrityCheckUpload> checks = parser.getChecksToAdd();
		for (int i = 0; i < checks.size(); i++) {
			IDataIntegrityCheckUpload check = checks.get(i);
			IntegrityCheck template = new IntegrityCheck();
			template.setCheckType(check.getCheckType());
			template.setCheckCode(check.getCheckCode());
			template.setFailDirective(check
					.getCheckFailDirective());
			template.setFailDirectiveOperator(check
					.getCheckFailDirectiveOperator());
			template.setName(check.getCheckName());
			template.setRepairDirective(check
					.getCheckRepairDirective());
			template.setResultType(check.getCheckResultType());
			template.setRepairType(check.getCheckRepairType());
			template.setRepairParameters(check.getCheckParameters());
			service.saveIntegrityCheck(template);
		}

		int templateCountAfterAdding = service
				.getAllIntegrityChecks().size();
		Assert.isTrue(templateCountAfterAdding > templateCountBeforeAdding);
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
		integrityCheck.setCheckCode("select 1 from person");
		integrityCheck.setFailDirective("0");
		integrityCheck
				.setFailDirectiveOperator(DataIntegrityConstants.FAILURE_OPERATOR_GREATER_THAN);
		integrityCheck.setName("sample integrity check");
		integrityCheck.setRepairParameters(null);
		integrityCheck.setRepairDirective(null);
		integrityCheck.setRepairType(DataIntegrityConstants.REPAIR_TYPE_NONE);
		integrityCheck.setResultType(DataIntegrityConstants.RESULT_TYPE_COUNT);
		integrityCheck.setCheckType(DataIntegrityConstants.CHECK_TYPE_SQL);
		integrityCheck.setRepairCodeType(DataIntegrityConstants.NONE);
		integrityCheck = service.saveIntegrityCheck(integrityCheck);

		IntegrityCheckResults results = service.runIntegrityCheck(integrityCheck, null);
		
		Assert.isTrue(results.getIntegrityCheck() != null);
	}

}
