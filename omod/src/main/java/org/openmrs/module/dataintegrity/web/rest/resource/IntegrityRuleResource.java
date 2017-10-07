package org.openmrs.module.dataintegrity.web.rest.resource;

import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.DataIntegrityRule;
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
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@Resource(name = RestConstants.VERSION_1  + DataIntegrityRestController.DATA_INTEGRITY_REST_NAMESPACE + "/rule", supportedClass = DataIntegrityRule.class, supportedOpenmrsVersions = { "1.8.*",
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.0.*", "2.1.*" })
public class IntegrityRuleResource extends DelegatingCrudResource<DataIntegrityRule> {
    @Override
    public DataIntegrityRule getByUniqueId(String uuid) {
        return getDataIntegrityService().getRuleByUuid(uuid);
    }

    @Override
    protected void delete(DataIntegrityRule dataIntegrityRule, String s, RequestContext requestContext) throws ResponseException {
        purge(dataIntegrityRule, requestContext);
    }

    @Override
    public DataIntegrityRule newDelegate() {
        return new DataIntegrityRule();
    }

    @Override
    public DataIntegrityRule save(DataIntegrityRule dataIntegrityRule) {
        return getDataIntegrityService().saveRule(dataIntegrityRule);
    }

    @Override
    public void purge(DataIntegrityRule dataIntegrityRule, RequestContext requestContext) throws ResponseException {
        getDataIntegrityService().deleteRule(dataIntegrityRule);
    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
        if (representation instanceof FullRepresentation) {
            DelegatingResourceDescription description = new DelegatingResourceDescription();
            description.addProperty("uuid");
            description.addProperty("ruleName");
            description.addProperty("ruleCategory");
            description.addLink("ref", ".?v=" + RestConstants.REPRESENTATION_REF);
            description.addSelfLink();
            return description;
        } else if (representation instanceof DefaultRepresentation) {
            DelegatingResourceDescription description = new DelegatingResourceDescription();
            description.addProperty("uuid");
            description.addProperty("ruleName");
            description.addProperty("ruleCategory");
            description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
            description.addLink("ref", ".?v=" + RestConstants.REPRESENTATION_REF);
            description.addSelfLink();
            return description;
        } else if (representation instanceof RefRepresentation) {
            DelegatingResourceDescription description = new DelegatingResourceDescription();
            description.addProperty("uuid");
            description.addProperty("ruleName");
            description.addSelfLink();
            return description;
        }
        return null;
    }

    @Override
    protected PageableResult doGetAll(RequestContext context) throws ResponseException {
        return new NeedsPaging<>(getDataIntegrityService().getAllRules(), context);
    }

    private DataIntegrityService getDataIntegrityService() {
        return Context.getService(DataIntegrityService.class);
    }
}
