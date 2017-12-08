package org.survey.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.survey.ServiceTestConfig;
import org.survey.service.file.FileServiceTestBase;

/**
 * Test PersonManagement using WebService. Spring injects userService with
 * WebService. Must use SpringJUnit4ClassRunner to enable spring injection.
 * Loaded Spring configuration is defined by ContextConfiguration.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:spring-config-service-test.xml", inheritLocations=false)
@ContextHierarchy(@ContextConfiguration(classes = ServiceTestConfig.class))
public class FileServiceIT extends FileServiceTestBase {
    /**
     * Override method because expected exception is wrapped in
     * SOAPFaultException in WebService.
     */
    @Test
    public void existsWithNull() {
        super.existsWithNull();
    }
}
