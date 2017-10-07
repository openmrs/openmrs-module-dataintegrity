package org.openmrs.module.dataintegrity.web.rest.resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.DataIntegrityRule;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.api.RestService;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.List;
import java.util.logging.Logger;

public class IntegrityRuleResourceTest extends BaseModuleWebContextSensitiveTest {

    private static final String DATAINTEGRITY_DATASET_XML = "org/openmrs/module/dataintegrity/include/DataIntegrityServiceTest-rules.xml";
    private static Logger logger = Logger.getLogger("logger");
    private static final String DATAINTEGRITY_RULE_UUID = "097fe53a-36b4-475c-95ea-a429c9512014";

    @Before
    public void setUp() throws Exception {
        initializeInMemoryDatabase();
        executeDataSet(DATAINTEGRITY_DATASET_XML);
    }

    private IntegrityRuleResource getResource() {
        return (IntegrityRuleResource) Context.getService(RestService.class).getResourceBySupportedClass(DataIntegrityRule.class);
    }

    @Test
    public void doGetAll_shouldReturnValidRules() throws Exception {
        NeedsPaging results = (NeedsPaging) getResource().doGetAll(buildRequestContext());
        List dataintegrityRules = results.getPageOfResults();
        Assert.assertEquals(3, dataintegrityRules.size());
    }

    @Test
    public void getByUniqueId_shouldReturnValidRule() throws Exception {
        DataIntegrityRule dataIntegrityRule = getResource().getByUniqueId(DATAINTEGRITY_RULE_UUID);
        Assert.assertEquals(DATAINTEGRITY_RULE_UUID, dataIntegrityRule.getUuid());
    }

    private RequestContext buildRequestContext() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContext context = new RequestContext();
        context.setRequest(request);
        return context;
    }
}
