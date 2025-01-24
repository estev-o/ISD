package es.udc.ws.app.model.Cursos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlCourseDao extends AbstractSqlCourseDao {

    @Override
    public Course create(Connection connection, Course course) {

        /* Create "queryString". */
        String queryString = "INSERT INTO Course"
                + " (city, name, startDate, price, maxSeats, reservedSeats, creationDate)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                        queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, course.getCity());
            preparedStatement.setString(i++, course.getName());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(course.getStartDate()));
            preparedStatement.setFloat(i++, course.getPrice());
            preparedStatement.setInt(i++, course.getMaxSeats());
            preparedStatement.setInt(i++, course.getReservedSeats());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(course.getCreationDate()));

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException("JDBC driver did not return generated key.");
            }
            Long courseId = resultSet.getLong(1);

            /* Return course. */
            return new Course(courseId, course.getCity(), course.getName(),
                    course.getStartDate(), course.getPrice(), course.getMaxSeats(),
                    course.getReservedSeats(), course.getCreationDate());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}