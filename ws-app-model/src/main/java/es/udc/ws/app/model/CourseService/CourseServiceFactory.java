package es.udc.ws.app.model.CourseService;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class CourseServiceFactory {

    private final static String CLASS_NAME_PARAMETER = "CourseServiceFactory.className";
    private static CourseService service = null;

    private CourseServiceFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static CourseService getInstance() {
        try {
            String serviceClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class serviceClass = Class.forName(serviceClassName);
            return (CourseService) serviceClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static CourseService getService() {
        if (service == null) {
            service = getInstance();
        }
        return service;
    }
}
