package org.openmrs.module.dataintegrity;

import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.util.Assert;


public class DataIntegrityServiceTest extends BaseModuleContextSensitiveTest{

	@Test
	public void shouldGetDataIntegrityModuleWithNullParameter() throws Exception {
		DataIntegrityService service = (DataIntegrityService) Context.getService(DataIntegrityService.class);
		DataIntegrityTemplate template = service.getDataIntegrityTemplate(1);
		Assert.notNull(template);
	}
}
