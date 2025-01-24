package es.udc.ws.app.model.Cursos;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

/**
 * A factory to get
 * <code>SqlCourseDao</code> objects. <p> Required configuration parameters: <ul>
 * <li><code>SqlCourseDaoFactory.className</code>: it must specify the full class
 * name of the class implementing
 * <code>SqlCourseDao</code>.</li> </ul>
 */
public class SqlCourseDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlCourseDaoFactory.className";
    private static SqlCourseDao dao = null;

    private SqlCourseDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SqlCourseDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlCourseDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlCourseDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }
}
