package org.openmrs.module.dataintegrity.web.rest.resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.web.rest.controller.DataIntegrityRestController;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.api.RestService;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.List;
import java.util.logging.Logger;

public class IntegrityRuleCategoryResourceTest extends BaseModuleWebContextSensitiveTest {

    private static final String DATAINTEGRITY_DATASET_XML = "org/openmrs/module/dataintegrity/include/DataIntegrityServiceTest-rules.xml";
    private static Logger logger = Logger.getLogger("logger");

    @Before
    public void setUp() throws Exception {
        initializeInMemoryDatabase();
        executeDataSet(DATAINTEGRITY_DATASET_XML);
    }

    private IntegrityRuleCategoryResource getResource() {
        return (IntegrityRuleCategoryResource) Context.getService(RestService.class).getResourceByName(RestConstants.VERSION_1  + DataIntegrityRestController.DATA_INTEGRITY_REST_NAMESPACE + "/rulebycategory");
    }

    @Test
    public void doGetAll_shouldReturnValidRules() throws Exception {
        String ruleCategory = "program";
        NeedsPaging results = (NeedsPaging) getResource().doGetAll(buildRequestContext("ruleCategory", ruleCategory));
        List dataintegrityRules = results.getPageOfResults();
        Assert.assertEquals(1, dataintegrityRules.size());
    }

    private RequestContext buildRequestContext(String paramName, String paramValue) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContext context = new RequestContext();
        request.addParameter(paramName, paramValue);
        context.setRequest(request);
        return context;
    }
}
