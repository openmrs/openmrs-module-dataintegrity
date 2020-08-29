package org.openmrs.module.dataintegrity.web.rest.search;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.DataIntegrityResult;
import org.openmrs.module.dataintegrity.api.DataIntegrityService;
import org.openmrs.module.dataintegrity.web.rest.controller.DataIntegrityRestController;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.api.SearchConfig;
import org.openmrs.module.webservices.rest.web.resource.api.SearchHandler;
import org.openmrs.module.webservices.rest.web.resource.api.SearchQuery;
import org.openmrs.module.webservices.rest.web.resource.impl.EmptySearchResult;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ResultsByPatientSearchHandler implements SearchHandler {

    private static final String PATIENT = "patient";

    private final SearchConfig searchConfig = new SearchConfig("default", RestConstants.VERSION_1 + DataIntegrityRestController.DATA_INTEGRITY_REST_NAMESPACE + "/integrityresults",
            Arrays.asList("1.8.*", "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.0.*", "2.1.*", "2.3.*", "2.4.*"),
            Arrays.asList(new SearchQuery.Builder("Allows you to get data integrity results by patient")
                    .withRequiredParameters(PATIENT)
                    .build()));

    @Override
    public SearchConfig getSearchConfig() {
        return this.searchConfig;
    }

    @Override
    public PageableResult search(RequestContext requestContext) throws ResponseException {
        String patientUuid = requestContext.getParameter(PATIENT);
        if(StringUtils.isBlank(patientUuid)){
            return new EmptySearchResult();
        }
        List<DataIntegrityResult> resultsByPatientUuid = Context.getService(DataIntegrityService.class).getResultsByPatientUuid(patientUuid);
        if(!resultsByPatientUuid.isEmpty()){
            return new NeedsPaging<>(resultsByPatientUuid, requestContext);
        } else {
            return new EmptySearchResult();
        }
    }
}
