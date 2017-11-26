package hu.psprog.leaflet.cbfs.web.registration.impl.controller;

import hu.psprog.leaflet.cbfs.web.controller.TestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Spark;

/**
 * Registration for {@link TestController}.
 * TODO remove when not needed anymore
 *
 * @author Peter Smith
 */
@Component
public class TestControllerRegistration extends AbstractJSONControllerRegistration<TestController.TestClass> {

    private TestController testController;

    @Autowired
    public TestControllerRegistration(TestController testController) {
        this.testController = testController;
    }

    @Override
    public void register() {
        Spark.get("/test", (request, response) -> json(testController.testMapping()));
    }
}
