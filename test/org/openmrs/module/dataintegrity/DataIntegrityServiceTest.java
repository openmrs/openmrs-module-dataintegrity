package org.openmrs.module.dataintegrity;

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
		DataIntegrityCheckTemplate template = service.getDataIntegrityCheckTemplate(1);
		System.out.println("Template received: Id= " + template.getIntegrityCheckId() + " Name= " + template.getIntegrityCheckName() + " Sql= " + template.getIntegrityCheckSql());
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
		temp.setIntegrityCheckBaseForFailure(1);
		temp.setIntegrityCheckScore(5);
		temp.setIntegrityCheckName("Sample Test");
		temp.setIntegrityCheckSql("Sample SQL string");
		service.saveDataIntegrityCheckTemplate(temp);
		int templateCountAfterAdding = service.getAllDataIntegrityCheckTemplates().size();
		Assert.isTrue(templateCountBeforeAdding == (templateCountAfterAdding - 1));
	}
	
	//@Test
	public void shouldDeleteIntegrityTemplates() throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context.getService(DataIntegrityService.class);
		List<DataIntegrityCheckTemplate> templates = service.getAllDataIntegrityCheckTemplates();
		int templateCountBeforeAdding = templates.size();
		DataIntegrityCheckTemplate temp = service.getDataIntegrityCheckTemplate(1);
		service.deleteDataIntegrityCheckTemplate(temp);
		int templateCountAfterAdding = service.getAllDataIntegrityCheckTemplates().size();
		Assert.isTrue(templateCountBeforeAdding == (templateCountAfterAdding + 1));
	}
	
	@Test
	public void shouldExecuteIntegrityCheck() throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context.getService(DataIntegrityService.class);
		DataIntegrityCheckTemplate template = service.getDataIntegrityCheckTemplate(2);
		DataIntegrityCheckResultTemplate result = service.runIntegrityCheck(template);
		Assert.notNull(result);
		List<Object[]> records = result.getFailedRecords();
		
		for (int i=0; i<records.size(); i++) {
			if (result.getColumnCount() > 1) {
				Object[] record = records.get(i);
				for (int j=0; j<record.length; j++) {
					System.out.println(record[j].toString());
				}
			} else {
				System.out.println(records.get(i));
			}
		}
	}
	
	
}
