package org.openmrs.module.dataintegrity.web.rest.resource;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
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

public class IntegrityResultPatientProgramResourceTest extends BaseModuleWebContextSensitiveTest {

    private static final String DATAINTEGRITY_DATASET_XML = "org/openmrs/module/dataintegrity/include/DataIntegrityServiceTest-rules.xml";
    private static Logger logger = Logger.getLogger("logger");
    private static final String PROGRAM_ID = "1";

    @Before
    public void setUp() throws Exception {
        initializeInMemoryDatabase();
        executeDataSet(DATAINTEGRITY_DATASET_XML);
    }

    private IntegrityResultPatientProgramResource getResource() {
        return (IntegrityResultPatientProgramResource) Context.getService(RestService.class).getResourceByName(RestConstants.VERSION_1  + DataIntegrityRestController.DATA_INTEGRITY_REST_NAMESPACE + "/result_by_patient_program");
    }

    @Test
    public void doGetAll_shouldReturnValidResults() throws Exception {
        NeedsPaging results = (NeedsPaging) getResource().doGetAll(buildRequestContext("programId", PROGRAM_ID));
        List dataintegrityResults = results.getPageOfResults();
        Assert.assertEquals(1, dataintegrityResults.size());
    }

    private RequestContext buildRequestContext(String paramName, String paramValue) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter(paramName, paramValue);
        RequestContext context = new RequestContext();
        context.setRequest(request);
        return context;
    }
}

