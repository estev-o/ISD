package es.udc.ws.app.model.Cursos;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;
public interface SqlCourseDao {

    public Course create(Connection connection, Course course);

    public Course find(Connection connection, Long courseId)
            throws InstanceNotFoundException;

    public List<Course> findCoursesByCityAndDate(Connection connection,
                                                 String city,
                                                 LocalDate startDate);

    public void update(Connection connection, Course course)
            throws InstanceNotFoundException;

    public void remove(Connection connection, Long courseId)
            throws InstanceNotFoundException;

}
