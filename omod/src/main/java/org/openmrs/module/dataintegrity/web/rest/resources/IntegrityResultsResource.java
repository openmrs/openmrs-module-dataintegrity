package org.openmrs.module.dataintegrity.web.rest.resources;

import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.DataIntegrityResult;
import org.openmrs.module.dataintegrity.api.DataIntegrityService;
import org.openmrs.module.dataintegrity.web.rest.controller.DataIntegrityRestController;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@Resource(name = RestConstants.VERSION_1  + DataIntegrityRestController.DATA_INTEGRITY_REST_NAMESPACE + "/integrityresults", supportedClass = DataIntegrityResult.class, supportedOpenmrsVersions = { "1.8.*",
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.0.*", "2.1.*", "2.3.*", "2.4.*"})
public class IntegrityResultsResource extends DelegatingCrudResource<DataIntegrityResult> {

    @Override
    public DataIntegrityResult newDelegate() {
        return new DataIntegrityResult();
    }

    @Override
    public DataIntegrityResult getByUniqueId(String s) {
        throw new ResourceDoesNotSupportOperationException();
    }

    @Override
    protected void delete(DataIntegrityResult dataIntegrityResult, String s, RequestContext requestContext) throws ResponseException {
        throw new ResourceDoesNotSupportOperationException();
    }

    @Override
    public void purge(DataIntegrityResult dataIntegrityResult, RequestContext requestContext) throws ResponseException {
        throw new ResourceDoesNotSupportOperationException();
    }

    @Override
    public DataIntegrityResult save(DataIntegrityResult dataIntegrityResult) {
        throw new ResourceDoesNotSupportOperationException();
    }

    @Override
    public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
        throw new ResourceDoesNotSupportOperationException();
    }

    @Override
    public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
        throw new ResourceDoesNotSupportOperationException();
    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
        if (representation instanceof FullRepresentation) {
            DelegatingResourceDescription description = new DelegatingResourceDescription();
            description.addProperty("uuid");
            description.addProperty("actionUrl");
            description.addProperty("notes");
            description.addProperty("patient");
            description.addProperty("patientProgram");
            description.addProperty("rule");
            description.addLink("ref", ".?v=" + RestConstants.REPRESENTATION_REF);
            description.addSelfLink();
            return description;
        } else if (representation instanceof DefaultRepresentation) {
            DelegatingResourceDescription description = new DelegatingResourceDescription();
            description.addProperty("uuid");
            description.addProperty("actionUrl");
            description.addProperty("rule");
            description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
            description.addLink("ref", ".?v=" + RestConstants.REPRESENTATION_REF);
            description.addSelfLink();
            return description;
        } else if (representation instanceof RefRepresentation) {
            DelegatingResourceDescription description = new DelegatingResourceDescription();
            description.addProperty("uuid");
            description.addProperty("actionUrl");
            description.addSelfLink();
            return description;
        }
        return null;
    }

    @Override
    protected PageableResult doGetAll(RequestContext context) throws ResponseException {
        return new NeedsPaging<>(getDataIntegrityService().getAllResults(), context);
    }

    private DataIntegrityService getDataIntegrityService() {
        return Context.getService(DataIntegrityService.class);
    }

}
