package es.udc.ws.app.model.Enrollment;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlEnrollmentDao {

    public Enrollment create(Connection connection, Enrollment enrollment);

    public Enrollment find(Connection connection, Long enrollmentId)
            throws InstanceNotFoundException;

    public List<Enrollment> findByUserEmail (Connection connection, String userEmail);

    public void update(Connection connection, Enrollment enrollment)
            throws InstanceNotFoundException;

    public void remove(Connection connection, Long enrollmentId)
            throws InstanceNotFoundException;
}