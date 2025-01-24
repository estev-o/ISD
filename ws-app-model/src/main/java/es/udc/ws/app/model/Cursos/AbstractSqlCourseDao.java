package es.udc.ws.app.model.Cursos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlCourseDao implements SqlCourseDao {

    protected AbstractSqlCourseDao() {
    }

    @Override
    public Course find(Connection connection, Long courseId)
            throws InstanceNotFoundException {

        String queryString = "SELECT city, name, startDate, price, maxSeats, reservedSeats, creationDate FROM Course WHERE courseId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i++, courseId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(courseId, Course.class.getName());
            }

            i = 1;
            String city = resultSet.getString(i++);
            String name = resultSet.getString(i++);
            Timestamp startDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime startDate = startDateAsTimestamp.toLocalDateTime();
            Float price = resultSet.getFloat(i++);
            int maxSeats = resultSet.getInt(i++);
            int reservedSeats = resultSet.getInt(i++);
            Timestamp creationDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime creationDate = creationDateAsTimestamp.toLocalDateTime();

            return new Course(courseId, city, name, startDate, price, maxSeats, reservedSeats, creationDate);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> findCoursesByCityAndDate(Connection connection, String city, LocalDate startDate) {
        String queryString = "SELECT courseId, city, name, startDate, price, maxSeats, reservedSeats, creationDate FROM Course";

        queryString += " WHERE  city = ?";
        queryString += " AND startDate>?";
        queryString += " ORDER BY name";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            preparedStatement.setString( 1,  city);
            preparedStatement.setString( 2,  startDate.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Course> courses = new ArrayList<>();

            while (resultSet.next()) {

                int i = 1;
                Long courseIdResult = resultSet.getLong(i++);
                String cityResult = resultSet.getString(i++);
                String nameResult = resultSet.getString(i++);
                Timestamp startDateAsTimestampResult = resultSet.getTimestamp(i++);
                LocalDateTime startDateResult = startDateAsTimestampResult.toLocalDateTime();
                Float priceResult = resultSet.getFloat(i++);
                int maxSeatsResult = resultSet.getInt(i++);
                int reservedSeatsResult = resultSet.getInt(i++);
                Timestamp creationDateAsTimestampResult = resultSet.getTimestamp(i++);
                LocalDateTime creationDateResult = creationDateAsTimestampResult.toLocalDateTime();

                courses.add(new Course(courseIdResult, cityResult, nameResult, startDateResult, priceResult, maxSeatsResult, reservedSeatsResult, creationDateResult));
            }

            return courses;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Connection connection, Course course)
            throws InstanceNotFoundException {

        String queryString = "UPDATE Course SET city = ?, name = ?, startDate = ?, price = ?, maxSeats = ?, reservedSeats = ? WHERE courseId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setString(i++, course.getCity());
            preparedStatement.setString(i++, course.getName());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(course.getStartDate()));
            preparedStatement.setFloat(i++, course.getPrice());
            preparedStatement.setInt(i++, course.getMaxSeats());
            preparedStatement.setInt(i++, course.getReservedSeats());
            preparedStatement.setLong(i++, course.getCourseId());

            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(course.getCourseId(), Course.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long courseId)
            throws InstanceNotFoundException {

        String queryString = "DELETE FROM Course WHERE courseId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i++, courseId);

            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(courseId, Course.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}