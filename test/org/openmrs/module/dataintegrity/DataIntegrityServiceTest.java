package org.openmrs.module.dataintegrity;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;
import org.springframework.util.Assert;

public class DataIntegrityServiceTest extends BaseModuleContextSensitiveTest {

	/**
	 * Do not use the in memory database.
	 * 
	 * @return
	 */
	public Boolean useInMemoryDatabase() {
		return false;
	}

	@Before
	public void runBeforeEachTest() throws Exception {
		authenticate();
	}

	// @Test
	public void shouldGetOneDataIntegrityTemplate() throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context
				.getService(DataIntegrityService.class);
		DataIntegrityCheckTemplate template = service
				.getDataIntegrityCheckTemplate(19);
		Assert.notNull(template);
	}

	// @Test
	public void shouldGetAllDataIntegrityTemplates() throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context
				.getService(DataIntegrityService.class);
		List<DataIntegrityCheckTemplate> templates = service
				.getAllDataIntegrityCheckTemplates();
		System.out.println("Templates received: " + templates);
		Assert.notNull(templates);
	}

	// @Test
	public void shouldSaveNewDataIntegrityTemplate() throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context
				.getService(DataIntegrityService.class);
		List<DataIntegrityCheckTemplate> templates = service
				.getAllDataIntegrityCheckTemplates();
		int templateCountBeforeAdding = templates.size();
		DataIntegrityCheckTemplate temp = new DataIntegrityCheckTemplate();
		temp.setCheckCode("temp code");
		temp.setFailDirective("temp fail directive");
		temp.setFailDirectiveOperator("temp fail op");
		temp.setName("temp name");
		temp.setRepairParameters("temp params");
		temp.setRepairDirective("temp repair directive");
		temp.setRepairType("temp repair type");
		temp.setResultType("temp result type");
		temp.setCheckType("temp check type");
		service.saveDataIntegrityCheckTemplate(temp);
		int templateCountAfterAdding = service
				.getAllDataIntegrityCheckTemplates().size();
		Assert.isTrue(templateCountBeforeAdding == (templateCountAfterAdding - 1));
	}

	// @Test
	public void shouldDeleteIntegrityTemplates() throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context
				.getService(DataIntegrityService.class);
		List<DataIntegrityCheckTemplate> templates = service
				.getAllDataIntegrityCheckTemplates();
		int templateCountBeforeAdding = templates.size();
		DataIntegrityCheckTemplate temp = service
				.getDataIntegrityCheckTemplate(19);
		service.deleteDataIntegrityCheckTemplate(temp);
		int templateCountAfterAdding = service
				.getAllDataIntegrityCheckTemplates().size();
		Assert.isTrue(templateCountBeforeAdding == (templateCountAfterAdding + 1));
	}

	@Test
	public void shouldExecuteIntegrityCheck() throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context
				.getService(DataIntegrityService.class);
		DataIntegrityCheckTemplate template = service
				.getDataIntegrityCheckTemplate(19);
		DataIntegrityCheckResultTemplate result = service.runIntegrityCheck(
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
		List<DataIntegrityCheckTemplate> templates = service
				.getAllDataIntegrityCheckTemplates();
		int templateCountBeforeAdding = templates.size();

		File file = new File(
				"F:\\GSOC2009\\workspaces\\DataIntegrityModule\\test\\org\\openmrs\\module\\dataintegrity\\test\\test.xml");
		DataIntegrityXmlFileParser parser = new DataIntegrityXmlFileParser(file);
		List<IDataIntegrityCheckUpload> checks = parser.getChecksToAdd();
		for (int i = 0; i < checks.size(); i++) {
			IDataIntegrityCheckUpload check = checks.get(i);
			DataIntegrityCheckTemplate template = new DataIntegrityCheckTemplate();
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
			service.saveDataIntegrityCheckTemplate(template);
		}

		int templateCountAfterAdding = service
				.getAllDataIntegrityCheckTemplates().size();
		Assert.isTrue(templateCountAfterAdding > templateCountBeforeAdding);
	}

	/**
	 * @see {@link DataIntegrityService#getResults(Integer)}
	 * 
	 */
	@Test
	@Verifies(value = "should retrieve results if a check exists", method = "getResults(Integer)")
	public void getResults_shouldRetrieveResultsIfACheckExists()
			throws Exception {
		//TODO auto-generated
		throw new Exception("Not yet implemented");
	}

	/**
	 * @see {@link DataIntegrityService#getResults(Integer)}
	 * 
	 */
	@Test
	@Verifies(value = "should return null if results for a check do not exist", method = "getResults(Integer)")
	public void getResults_shouldReturnNullIfResultsForACheckDoNotExist()
			throws Exception {
		//TODO auto-generated
		throw new Exception("Not yet implemented");
	}

	/**
	 * @see {@link DataIntegrityService#saveResults(DataIntegrityCheckResultTemplate)}
	 * 
	 */
	@Test
	@Verifies(value = "should not throw an error saving results", method = "saveResults(DataIntegrityCheckResultTemplate)")
	public void saveResults_shouldNotThrowAnErrorSavingResults()
			throws Exception {
		//TODO auto-generated
		throw new Exception("Not yet implemented");
	}

}
