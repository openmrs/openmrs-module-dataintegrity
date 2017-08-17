package org.openmrs.module.dataintegrity.web.rest.resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.DataIntegrityResult;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.api.RestService;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.List;
import java.util.logging.Logger;

public class IntegrityResultResourceTest extends BaseModuleWebContextSensitiveTest {

    private static final String DATAINTEGRITY_DATASET_XML = "org/openmrs/module/dataintegrity/include/DataIntegrityServiceTest-rules.xml";
    private static Logger logger = Logger.getLogger("logger");
    private static final String DATAINTEGRITY_RESULT_UUID = "33585310-e1a6-41da-8d60-d35acd386bf0";

    @Before
    public void setUp() throws Exception {
        initializeInMemoryDatabase();
        executeDataSet(DATAINTEGRITY_DATASET_XML);
    }

    private IntegrityResultResource getResource() {
        return (IntegrityResultResource) Context.getService(RestService.class).getResourceBySupportedClass(DataIntegrityResult.class);
    }

    @Test
    public void doGetAll_shouldReturnValidResults() throws Exception {
        NeedsPaging results = (NeedsPaging) getResource().doGetAll(buildRequestContext());
        List dataintegrityResults = results.getPageOfResults();
        Assert.assertEquals(2, dataintegrityResults.size());
    }

    @Test
    public void getByUniqueId_shouldReturnValidResult() throws Exception {
        DataIntegrityResult dataIntegrityResult = getResource().getByUniqueId(DATAINTEGRITY_RESULT_UUID);
        Assert.assertEquals(DATAINTEGRITY_RESULT_UUID, dataIntegrityResult.getUuid());
    }

    private RequestContext buildRequestContext() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContext context = new RequestContext();
        context.setRequest(request);
        return context;
    }
}
