package hu.psprog.leaflet.cbfs.web.controller;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * {@link TestController} simply for testing purpose.
 * TODO remove when not needed anymore
 *
 * @author Peter Smith
 */
@Component
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    public TestClass testMapping() {

        LOGGER.info("Controller called.");
        return new TestClass("Valami", 10, true);
    }

    public static class TestClass {
        private String firstField;
        private int secondField;
        private boolean thirdField;

        public TestClass(String firstField, int secondField, boolean thirdField) {
            this.firstField = firstField;
            this.secondField = secondField;
            this.thirdField = thirdField;
        }

        public String getFirstField() {
            return firstField;
        }

        public int getSecondField() {
            return secondField;
        }

        public boolean isThirdField() {
            return thirdField;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("firstField", firstField)
                    .append("secondField", secondField)
                    .append("thirdField", thirdField)
                    .toString();
        }
    }
}
