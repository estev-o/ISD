package es.udc.ws.app.model.Enrollment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlEnrollmentDao extends AbstractSqlEnrollmentDao {

    @Override
    public Enrollment create(Connection connection, Enrollment enrollment) {

        /* Create "queryString". */
        String queryString = "INSERT INTO Enrollment"
                + " (courseId, userEmail, creditCardNumber, enrollmentDate, cancellationDate)"
                + " VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                        queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, enrollment.getCourseId());
            preparedStatement.setString(i++, enrollment.getUserEmail());
            preparedStatement.setString(i++, enrollment.getCreditCardNumber());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(enrollment.getEnrollmentDate()));
            Timestamp cancellationDate = enrollment.getCancellationDate() != null ? Timestamp.valueOf(enrollment.getCancellationDate()) : null;
            preparedStatement.setTimestamp(i++, cancellationDate);

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException("JDBC driver did not return generated key.");
            }
            Long enrollmentId = resultSet.getLong(1);

            /* Return enrollment. */
            return new Enrollment(enrollmentId, enrollment.getUserEmail(), enrollment.getCourseId(),
                    enrollment.getCreditCardNumber(), enrollment.getEnrollmentDate(), enrollment.getCancellationDate());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}