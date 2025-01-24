package es.udc.ws.app.model.Enrollment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlEnrollmentDao implements SqlEnrollmentDao {

    protected AbstractSqlEnrollmentDao() {
    }

    @Override
    public Enrollment find(Connection connection, Long enrollmentId)
            throws InstanceNotFoundException {

        String queryString = "SELECT courseId, userEmail, creditCardNumber, enrollmentDate, cancellationDate FROM Enrollment WHERE enrollmentId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i++, enrollmentId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(enrollmentId, Enrollment.class.getName());
            }

            i = 1;
            Long courseId = resultSet.getLong(i++);
            String userEmail = resultSet.getString(i++);
            String creditCardNumber = resultSet.getString(i++);
            Timestamp enrollmentDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime enrollmentDate = enrollmentDateAsTimestamp.toLocalDateTime();
            Timestamp cancellationDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime cancellationDate = (cancellationDateAsTimestamp != null) ? cancellationDateAsTimestamp.toLocalDateTime() : null;

            return new Enrollment(enrollmentId, userEmail, courseId, creditCardNumber, enrollmentDate, cancellationDate);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Enrollment> findByCourseId(Connection connection, Long courseId) {

        String queryString = "SELECT enrollmentId, userEmail, creditCardNumber, enrollmentDate, cancellationDate FROM Enrollment WHERE courseId = ? ORDER BY enrollmentDate";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            preparedStatement.setLong(1, courseId);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Enrollment> enrollments = new ArrayList<>();

            while (resultSet.next()) {

                int i = 1;
                Long enrollmentId = resultSet.getLong(i++);
                String userEmail = resultSet.getString(i++);
                String creditCardNumber = resultSet.getString(i++);
                Timestamp enrollmentDateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime enrollmentDate = enrollmentDateAsTimestamp.toLocalDateTime();
                Timestamp cancellationDateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime cancellationDate = (cancellationDateAsTimestamp != null) ? cancellationDateAsTimestamp.toLocalDateTime() : null;

                enrollments.add(new Enrollment(enrollmentId, userEmail, courseId, creditCardNumber, enrollmentDate, cancellationDate));
            }

            return enrollments;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Enrollment> findByUserEmail (Connection connection, String userEmail) {
        String queryString = "SELECT enrollmentId, courseId, creditCardNumber, enrollmentDate, cancellationDate FROM Enrollment WHERE userEmail = ? ORDER BY enrollmentDate DESC";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            preparedStatement.setString(1, userEmail);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Enrollment> enrollments = new ArrayList<>();

            while (resultSet.next()) {

                int i = 1;
                Long enrollmentId = resultSet.getLong(i++);
                Long courseId = resultSet.getLong(i++);
                String creditCardNumber = resultSet.getString(i++);
                Timestamp enrollmentDateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime enrollmentDate = enrollmentDateAsTimestamp.toLocalDateTime();
                Timestamp cancellationDateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime cancellationDate = (cancellationDateAsTimestamp != null) ? cancellationDateAsTimestamp.toLocalDateTime() : null;

                enrollments.add(new Enrollment(enrollmentId, userEmail, courseId, creditCardNumber, enrollmentDate, cancellationDate));
            }

            return enrollments;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Connection connection, Enrollment enrollment)
            throws InstanceNotFoundException {

        String queryString = "UPDATE Enrollment SET courseId = ?, userEmail = ?, creditCardNumber = ?, enrollmentDate = ?, cancellationDate = ? WHERE enrollmentId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i++, enrollment.getCourseId());
            preparedStatement.setString(i++, enrollment.getUserEmail());
            preparedStatement.setString(i++, enrollment.getCreditCardNumber());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(enrollment.getEnrollmentDate()));
            Timestamp cancellationDate = (enrollment.getCancellationDate() != null) ? Timestamp.valueOf(enrollment.getCancellationDate()) : null;
            preparedStatement.setTimestamp(i++, cancellationDate);
            preparedStatement.setLong(i++, enrollment.getEnrollmentId());

            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(enrollment.getEnrollmentId(), Enrollment.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long enrollmentId)
            throws InstanceNotFoundException {

        String queryString = "DELETE FROM Enrollment WHERE enrollmentId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i++, enrollmentId);

            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(enrollmentId, Enrollment.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}