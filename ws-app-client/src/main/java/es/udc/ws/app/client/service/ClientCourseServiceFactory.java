package es.udc.ws.app.client.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

import java.lang.reflect.InvocationTargetException;

public class ClientCourseServiceFactory {

    private final static String CLASS_NAME_PARAMETER = "ClientCourseServiceFactory.className";
    private static Class<ClientCourseService> serviceClass = null;

    private ClientCourseServiceFactory() {
    }

    @SuppressWarnings("unchecked")
    private synchronized static Class<ClientCourseService> getServiceClass() {

        if (serviceClass == null) {
            try {
                String serviceClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
                serviceClass = (Class<ClientCourseService>) Class.forName(serviceClassName);
            } catch (ClassNotFoundException | ClassCastException e) {
                throw new RuntimeException("Error loading service class: " + e.getMessage(), e);
            }
        }
        return serviceClass;
    }

    public static ClientCourseService getService() {

        try {
            return getServiceClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Error creating service instance: " + e.getMessage(), e);
        }
    }
}

