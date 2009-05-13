package org.openmrs.module.dataintegrity;

import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.util.Assert;
import org.openmrs.module.dataintegrity.DataIntegrityService;
import org.openmrs.module.dataintegrity.DataIntegrityTemplate;


public class DataIntegrityServiceTest extends BaseModuleContextSensitiveTest{

	@Test
	public void shouldGetDataIntegrityModuleWithNullParameter() throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context.getService(DataIntegrityService.class);
		DataIntegrityTemplate template = service.getDataIntegrityTemplate(1);
		Assert.notNull(template);
	}
}
