package org.openmrs.module.dataintegrity;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.util.Assert;
import org.openmrs.module.dataintegrity.DataIntegrityService;
import org.openmrs.module.dataintegrity.DataIntegrityCheckTemplate;


public class DataIntegrityServiceTest extends BaseModuleContextSensitiveTest{

    /**
     * Do not use the in memory database. 
     * @return
     */
    public Boolean useInMemoryDatabase() {
        return false;
    }
    
    @Before
    public void runBeforeEachTest() throws Exception {
        authenticate();
    }
    
	//@Test
	public void shouldGetOneDataIntegrityTemplate() throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context.getService(DataIntegrityService.class);
		DataIntegrityCheckTemplate template = service.getDataIntegrityCheckTemplate(19);
		Assert.notNull(template);
	}
	
	//@Test
	public void shouldGetAllDataIntegrityTemplates() throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context.getService(DataIntegrityService.class);
		List<DataIntegrityCheckTemplate> templates = service.getAllDataIntegrityCheckTemplates();
		System.out.println("Templates received: " + templates);
		Assert.notNull(templates);
	}
	
	//@Test
	public void shouldSaveNewDataIntegrityTemplate() throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context.getService(DataIntegrityService.class);
		List<DataIntegrityCheckTemplate> templates = service.getAllDataIntegrityCheckTemplates();
		int templateCountBeforeAdding = templates.size();
		DataIntegrityCheckTemplate temp = new DataIntegrityCheckTemplate();
		temp.setIntegrityCheckCode("temp code");
		temp.setIntegrityCheckFailDirective("temp fail directive");
		temp.setIntegrityCheckFailDirectiveOperator("temp fail op");
		temp.setIntegrityCheckName("temp name");
		temp.setIntegrityCheckParameters("temp params");
		temp.setIntegrityCheckRepairDirective("temp repair directive");
		temp.setIntegrityCheckRepairType("temp repair type");
		temp.setIntegrityCheckResultType("temp result type");
		temp.setIntegrityCheckType("temp check type");
		service.saveDataIntegrityCheckTemplate(temp);
		int templateCountAfterAdding = service.getAllDataIntegrityCheckTemplates().size();
		Assert.isTrue(templateCountBeforeAdding == (templateCountAfterAdding - 1));
	}
	
	//@Test
	public void shouldDeleteIntegrityTemplates() throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context.getService(DataIntegrityService.class);
		List<DataIntegrityCheckTemplate> templates = service.getAllDataIntegrityCheckTemplates();
		int templateCountBeforeAdding = templates.size();
		DataIntegrityCheckTemplate temp = service.getDataIntegrityCheckTemplate(19);
		service.deleteDataIntegrityCheckTemplate(temp);
		int templateCountAfterAdding = service.getAllDataIntegrityCheckTemplates().size();
		Assert.isTrue(templateCountBeforeAdding == (templateCountAfterAdding + 1));
	}
	
	//@Test
	public void shouldExecuteIntegrityCheck() throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context.getService(DataIntegrityService.class);
		DataIntegrityCheckTemplate template = service.getDataIntegrityCheckTemplate(30);
		DataIntegrityCheckResultTemplate result = service.runIntegrityCheck(template, null);
		Assert.notNull(result);
		List<Object[]> records = result.getFailedRecords();
		
		for (int i=0; i<records.size(); i++) {
				System.out.println(records.get(i));
		}
	}
	
	@Test
	public void shouldUploadDataIntegrityCheck() throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context.getService(DataIntegrityService.class);
		List<DataIntegrityCheckTemplate> templates = service.getAllDataIntegrityCheckTemplates();
		int templateCountBeforeAdding = templates.size();
		
		File file = new File("F:\\GSOC2009\\workspaces\\DataIntegrityModule\\test\\org\\openmrs\\module\\dataintegrity\\test\\test.xml");
		DataIntegrityXmlFileParser parser = new DataIntegrityXmlFileParser(file);
		List<IDataIntegrityCheckUpload> checks = parser.getChecksToAdd();
		for (int i=0; i<checks.size(); i++) {
			IDataIntegrityCheckUpload check = checks.get(i);
			DataIntegrityCheckTemplate template = new DataIntegrityCheckTemplate();
			template.setIntegrityCheckType(check.getCheckType());
			template.setIntegrityCheckCode(check.getCheckCode());
			template.setIntegrityCheckFailDirective(check.getCheckFailDirective());
			template.setIntegrityCheckFailDirectiveOperator(check.getCheckFailDirectiveOperator());
			template.setIntegrityCheckName(check.getCheckName());
			template.setIntegrityCheckRepairDirective(check.getCheckRepairDirective());
			template.setIntegrityCheckResultType(check.getCheckResultType());
			template.setIntegrityCheckRepairType(check.getCheckRepairType());
			template.setIntegrityCheckParameters(check.getCheckParameters());
			service.saveDataIntegrityCheckTemplate(template);
		}
		
		int templateCountAfterAdding = service.getAllDataIntegrityCheckTemplates().size();
		Assert.isTrue(templateCountAfterAdding > templateCountBeforeAdding);
	}
	
	
}
