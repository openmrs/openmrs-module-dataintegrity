package org.openmrs.module.dataintegrity.web.rest.controller;

import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.MainResourceController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rest/" + RestConstants.VERSION_1 + DataIntegrityRestController.DATA_INTEGRITY_REST_NAMESPACE)
public class DataIntegrityRestController extends MainResourceController {

    public static final String DATA_INTEGRITY_REST_NAMESPACE = "/dataintegrity";

    @Override
    public String getNamespace() {
        return RestConstants.VERSION_1 + DATA_INTEGRITY_REST_NAMESPACE;
    }
}
