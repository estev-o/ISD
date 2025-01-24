package es.udc.ws.app.model.Enrollment;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

/**
 * A factory to get
 * <code>SqlEnrollmentDao</code> objects. <p> Required configuration parameters: <ul>
 * <li><code>SqlEnrollmentDaoFactory.className</code>: it must specify the full class
 * name of the class implementing
 * <code>SqlEnrollmentDao</code>.</li> </ul>
 */
public class SqlEnrollmentDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlEnrollmentDaoFactory.className";
    private static SqlEnrollmentDao dao = null;

    private SqlEnrollmentDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SqlEnrollmentDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlEnrollmentDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlEnrollmentDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }
}
