package org.survey.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.survey.ServiceRestTestConfig;
import org.survey.service.file.FileServiceTestBase;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:spring-config-rest-service-test.xml", inheritLocations=false)
@ContextHierarchy(@ContextConfiguration(classes = ServiceRestTestConfig.class))
public class FileServiceRestIT extends FileServiceTestBase {
    /**
     * Override method because expected exception is wrapped in
     * SOAPFaultException in WebService.
     */
    @Test
    public void existsWithNull() {
        super.existsWithNull();
    }
}
