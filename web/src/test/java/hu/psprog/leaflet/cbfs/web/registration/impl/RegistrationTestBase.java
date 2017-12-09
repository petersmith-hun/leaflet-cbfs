package hu.psprog.leaflet.cbfs.web.registration.impl;

import org.springframework.util.ReflectionUtils;
import spark.Service;
import spark.Spark;
import spark.route.HttpMethod;
import spark.route.Routes;
import spark.routematch.RouteMatch;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Unit test base class for registration tests.
 *
 * @author Peter Smith
 */
public abstract class RegistrationTestBase {

    private static final String ACCEPT_TYPE = "*/*";
    private static final String FIELD_ROUTES = "routes";
    private static final String METHOD_GET_INSTANCE = "getInstance";

    protected RouteMatch getRouteMatch(HttpMethod method, String url) {
        return getRegisteredRoutes().find(method, url, ACCEPT_TYPE);
    }

    private Routes getRegisteredRoutes() {

        Field routesField = ReflectionUtils.findField(Service.class, FIELD_ROUTES);
        routesField.setAccessible(true);

        try {
            return (Routes) routesField.get(getServiceInstance());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to extract registered routes.");
        }
    }

    private Service getServiceInstance() {

        Method getInstanceMethod = ReflectionUtils.findMethod(Spark.class, METHOD_GET_INSTANCE);
        getInstanceMethod.setAccessible(true);

        try {
            return (Service) getInstanceMethod.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Failed to extract Spark Service instance.", e);
        }
    }
}
