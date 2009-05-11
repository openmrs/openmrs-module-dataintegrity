package org.openmrs.module.dataintegrity.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PersonAddress;
import org.openmrs.User;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.DataIntegrityService;
import org.openmrs.module.dataintegrity.DataIntegrityTemplate;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class DataIntegrityServiceDatabaseTest extends BaseModuleContextSensitiveTest {


    @Before
    public void runBeforeEachTest() throws Exception {
        authenticate();
    }

    /**
     * Do not use the in memory database. 
     * @return
     */
    public Boolean useInMemoryDatabase() {
        return false;
    }

    /**
     * Test to see if the user is authenticated.
     */
    @Test
    public void shouldGetAuthenticatedUser() {
        User u = Context.getAuthenticatedUser();
        assertNotNull(u);
    }

    /**
     * Test to see if we can get some locations from the openmrs real database
     */
    @Test
    public void shouldGetAListOfLocations() {
        List<Location> locations = Context.getLocationService().getAllLocations();
        System.out.println(locations);
        assertNotNull(locations);
    }


    
    /**
     * Test to see if we get any IdcardsTemplates from the database.
     */
    @Test
    public void shouldGetAllDataIntegrityTemplates() {
        DataIntegrityService service = (DataIntegrityService)Context.getService(DataIntegrityService.class);
        List<DataIntegrityTemplate> templates = service.getAllDataIntegrityTemplates();
        System.out.println("DataIntegrityTemplates: " + templates);
        assertNotNull(templates);
        DataIntegrityTemplate template = service.getDataIntegrityTemplate(1);
        System.out.println("One DataIntegrityTemplate " + template);
        assertNotNull(template);
    }
}