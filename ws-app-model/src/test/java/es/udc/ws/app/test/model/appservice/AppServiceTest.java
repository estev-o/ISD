package es.udc.ws.app.test.model.appservice;

import es.udc.ws.app.model.CourseService.CourseService;
import es.udc.ws.app.model.CourseService.CourseServiceFactory;
import es.udc.ws.app.model.CourseService.excepciones.*;
import es.udc.ws.app.model.Cursos.Course;
import es.udc.ws.app.model.Cursos.SqlCourseDao;
import es.udc.ws.app.model.Cursos.SqlCourseDaoFactory;
import es.udc.ws.app.model.Enrollment.SqlEnrollmentDao;
import es.udc.ws.app.model.Enrollment.SqlEnrollmentDaoFactory;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import es.udc.ws.app.model.Enrollment.Enrollment;

import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;
import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;

public class AppServiceTest {

    private static final String VALID_EMAIL = "estevo.hugo.nico@udc.com";
    private static final String INVALID_EMAIL = "estevohugonicoudccom";
    private static final String VALID_CREDIT_CARD = "1234567890123456";
    private static final String SAVED_VALID_CREDIT_CARD = "3456"; // Solo se guarda los últimos 4 dígitos
    private static final String INVALID_CREDIT_CARD = "12345";
    private static final Course VALID_COURSE1 = new Course("A Coruña", "Tutorial de ISD", LocalDateTime.now().plusDays(20), 10.0f, 20, 0, LocalDateTime.now());
    private static final Course VALID_COURSE2 = new Course("A Coruña", "Tutorial de XP", LocalDateTime.now().plusDays(25), 10.0f, 20, 0, LocalDateTime.now());
    private static final Course VALID_COURSE3 = new Course("A Coruña", "Tutorial de LSI", LocalDateTime.now().plusDays(30), 10.0f, 20, 0, LocalDateTime.now());
    private static final Course FULL_COURSE = new Course("A Coruña", "Tutorial de ISD", LocalDateTime.now().plusDays(20), 10.0f, 2, 2, LocalDateTime.now());

    private static CourseService courseService;
    private static SqlCourseDao courseDao;
    private static SqlEnrollmentDao enrollmentDao;
    private static DataSource dataSource;

    @BeforeAll
    public static void init() {
        dataSource = new SimpleDataSource();
        DataSourceLocator.addDataSource(APP_DATA_SOURCE, dataSource);
        courseService = CourseServiceFactory.getService();
        courseDao = SqlCourseDaoFactory.getDao();
        enrollmentDao = SqlEnrollmentDaoFactory.getDao();
    }

    private void removeCourse(Long courseId) {
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                courseDao.remove(connection, courseId);


                connection.commit();
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException("Course not found: " + courseId, e);
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error removing course", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting database connection", e);
        }
    }
    private void updateCourse(Course course) {
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                courseDao.update(connection, course);
                connection.commit();
            } catch (SQLException | InstanceNotFoundException e) {
                connection.rollback(); // Revertir cambios en caso de error
                throw new RuntimeException("Error updating course", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting database connection", e);
        }
    }
    private void removeEnrollment(Long enrollmentId) {
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                enrollmentDao.remove(connection, enrollmentId);
                connection.commit();
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException("Enrollment not found: "+enrollmentId, e);
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error removing enrollment",e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting database connection", e);
        }
    }
    @Test
    public void testAddCourseBeforeStartDate(){
        Course course = new Course("A Coruña", "Turorial de ISD", LocalDateTime.now().minusDays(1), 10.0f, 20, 0, LocalDateTime.now());
        assertThrows(InputValidationException.class, () -> courseService.addCourse(course));
    }

    @Test
    public void testAddCourseInvalidPrice() {
        Course course = new Course("A Coruña", "Turorial de ISD", LocalDateTime.now().plusDays(20), -10.0f, 20, 0, LocalDateTime.now());
        assertThrows(InputValidationException.class, () -> courseService.addCourse(course));
    }

    @Test
    public void testAddCourseInvalidMaxSeats() {
        Course course = new Course("A Coruña", "Turorial de ISD", LocalDateTime.now().plusDays(20), 10.0f, -20, 0, LocalDateTime.now());
        assertThrows(InputValidationException.class, () -> courseService.addCourse(course));
    }

    @Test
    public void testAddCourseAndFindCourseByCityAndDate() throws InputValidationException {
        Course addedCourse1 = null;
        Course addedCourse2 = null;
        Course addedCourse3 = null;

        try {
            addedCourse1 = courseService.addCourse(VALID_COURSE1);
            addedCourse2 = courseService.addCourse(VALID_COURSE2);
            addedCourse3 = courseService.addCourse(VALID_COURSE3);

            List<Course> foundCourse = courseService.findCoursesByCityAndDate("A Coruña", LocalDate.from(LocalDateTime.now().plusDays(22)));

            assertEquals(addedCourse3, foundCourse.get(0));
            assertEquals(addedCourse2, foundCourse.get(1));

        } finally {
            // Eliminamos el curso añadido para ayudar a la repetibilidad de las pruebas
            if (addedCourse1 != null) {
                removeCourse(addedCourse1.getCourseId());
            }
            if (addedCourse2 != null) {
                removeCourse(addedCourse2.getCourseId());
            }
            if (addedCourse3 != null) {
                removeCourse(addedCourse3.getCourseId());
            }
        }
    }

    @Test
    public void testAddCourseAndFindCourseById() throws InputValidationException, InstanceNotFoundException {
        Course course = VALID_COURSE1;
        Course addedCourse = courseService.addCourse(course);

        try {

            Course foundCourse = courseService.findCourseById(addedCourse.getCourseId());

            assertEquals(addedCourse, foundCourse);
            assertEquals(foundCourse.getCity(), course.getCity());
            assertEquals(foundCourse.getName(), course.getName());
            assertEquals(foundCourse.getPrice(), course.getPrice());
            assertEquals(foundCourse.getMaxSeats(), course.getMaxSeats());
            assertEquals(foundCourse.getReservedSeats(), course.getReservedSeats());
        } finally {
            // Eliminamos el curso añadido para ayudar a la repetibilidad de las pruebas
            if (addedCourse != null) {
                removeCourse(addedCourse.getCourseId());
            }
        }
    }

    @Test
    public void testFindNonExistentCourse() {
        // Verifica que se lanza una excepción al intentar encontrar un curso que no existe
        assertThrows(InstanceNotFoundException.class, () -> courseService.findCourseById(66L));
    }

    @Test
    public void testFindCourseInvalidNumber() {
        // Verifica que se lanza una excepción al intentar encontrar un curso con un número inválido
        assertThrows(InstanceNotFoundException.class, () -> courseService.findCourseById(-1L));
    }

    @Test
    public void testEnrollInCourse() throws InputValidationException, InstanceNotFoundException, NotEnoughSeatsException {
        //testeamos la inscripción en un curso correctamente
        Course course = VALID_COURSE1;
        Course addedCourse = courseService.addCourse(course);
        Enrollment enrollment = null;
        try {
            courseService.addCourse(course);
            enrollment = courseService.enrollInCourse(VALID_EMAIL, addedCourse.getCourseId(), VALID_CREDIT_CARD);
            assertNotNull(enrollment);
            assertEquals(VALID_EMAIL, enrollment.getUserEmail());
            assertEquals(SAVED_VALID_CREDIT_CARD, enrollment.getCreditCardNumber());
            assertEquals(addedCourse.getCourseId(), enrollment.getCourseId());
        } finally {
            assert enrollment != null;
            removeEnrollment(enrollment.getEnrollmentId());
            removeCourse(addedCourse.getCourseId());
        }
    }

    @Test
    public void testEnrollInInvalidCourse() {
        //testeamos que da fallo la inscripción en un curso que no existe
        assertThrows(InstanceNotFoundException.class, () -> courseService.enrollInCourse(VALID_EMAIL, 123L, VALID_CREDIT_CARD));
    }

    @Test
    public void testEnrollInCourseAlreadyStarted() throws InputValidationException {
        //testeamos que da fallo la inscripción en un curso que ya ha comenzado
        Course addedCourse = courseService.addCourse(VALID_COURSE1);
        // Cambiamos la fecha de inicio del curso válido para que ya haya comenzado
        addedCourse.setCreationDate(LocalDateTime.now().minusDays(30));
        addedCourse.setStartDate(LocalDateTime.now().minusDays(14));
        updateCourse(addedCourse);

        try {
            assertThrows(CourseAlreadyStartedException.class, () ->
                    courseService.enrollInCourse(VALID_EMAIL, addedCourse.getCourseId(), VALID_CREDIT_CARD));
        } finally {
            removeCourse(addedCourse.getCourseId());
        }
    }

    @Test
    public void testEnrollInCourseNoAvailableSeats() throws InputValidationException {
        //testeamos que da fallo la inscripción en un curso sin plazas disponibles
        Course addedCourse = courseService.addCourse(FULL_COURSE);
        try {
            assertThrows(NotEnoughSeatsException.class, () ->
                    courseService.enrollInCourse(VALID_EMAIL, addedCourse.getCourseId(), VALID_CREDIT_CARD));
        } finally {
            removeCourse(addedCourse.getCourseId());
        }
    }

    @Test
    public void testEnrollInCourseInvalidEmail() throws InputValidationException {
        //testeamos que da fallo la inscripción en un curso con un email inválido
        Course addedCourse = courseService.addCourse(VALID_COURSE1);
        try {
            assertThrows(InputValidationException.class, () ->
                    courseService.enrollInCourse(INVALID_EMAIL, addedCourse.getCourseId(), VALID_CREDIT_CARD));
            //Probamos con emails que no siguen el patrón de un email
            assertThrows(InputValidationException.class, () ->
                    courseService.enrollInCourse("estevo.udccom", addedCourse.getCourseId(), VALID_CREDIT_CARD));
            assertThrows(InputValidationException.class, () ->
                    courseService.enrollInCourse("est.evoud@ccom", addedCourse.getCourseId(), VALID_CREDIT_CARD));
        } finally {
            removeCourse(addedCourse.getCourseId());
        }
    }

    @Test
    public void testEnrollInCourseInvalidCreditCard() throws InputValidationException {
        Course addedCourse = courseService.addCourse(VALID_COURSE1);
        try {
            assertThrows(InputValidationException.class, () ->
                    courseService.enrollInCourse(VALID_EMAIL, addedCourse.getCourseId(), INVALID_CREDIT_CARD));
        } finally {
            removeCourse(addedCourse.getCourseId());
        }

    }

    @Test
    public void testFindEnrollmentByUserEmail() throws InputValidationException, InstanceNotFoundException, NotEnoughSeatsException {
        Enrollment enrollment1, enrollment2, enrollment3;
        Course addedCourse1 = courseService.addCourse(VALID_COURSE1);
        Course addedCourse2 = courseService.addCourse(VALID_COURSE2);
        Course addedCourse3 = courseService.addCourse(VALID_COURSE3);

        try {

            enrollment1 = courseService.enrollInCourse(VALID_EMAIL, addedCourse1.getCourseId(), VALID_CREDIT_CARD);
            Thread.sleep(1000); //Le añadimos retraso para comprobar que se ordenan correctamente
            enrollment2 = courseService.enrollInCourse(VALID_EMAIL, addedCourse2.getCourseId(), VALID_CREDIT_CARD);
            Thread.sleep(1000);
            enrollment3 = courseService.enrollInCourse(VALID_EMAIL, addedCourse3.getCourseId(), VALID_CREDIT_CARD);

            List<Enrollment> foundEnrollment;
            foundEnrollment = courseService.findUserEnrollments(VALID_EMAIL);

            assertEquals(3, foundEnrollment.size());

            assertEquals(enrollment3, foundEnrollment.get(0));
            assertEquals(enrollment2, foundEnrollment.get(1));
            assertEquals(enrollment1, foundEnrollment.get(2));

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            removeCourse(addedCourse1.getCourseId());
            removeCourse(addedCourse2.getCourseId());
            removeCourse(addedCourse3.getCourseId());
        }
    }

    @Test
    public void testCancelEnrollment() throws InputValidationException, InstanceNotFoundException, LateCancellationException, EnrollmentAlreadyCancelledException, NotEnoughSeatsException {
        Course addedCourse = courseService.addCourse(VALID_COURSE1);
        Enrollment enrollment;
        try{
            enrollment = courseService.enrollInCourse(VALID_EMAIL, addedCourse.getCourseId(), VALID_CREDIT_CARD);
            assertNotNull(enrollment, "La incscripción no se ha realizado correctamente");
            assertNotNull(enrollment.getEnrollmentId(), "El ID de la inscripción es null");
            courseService.cancelEnrollment(enrollment.getEnrollmentId(), VALID_EMAIL);
            //Las siguientes lineas son dificiles de entender. Se intenta buscar la inscripción cancelada en la lista de inscripciones del usuario
            Enrollment canceledEnrollment = courseService.findUserEnrollments(VALID_EMAIL).stream().filter(e -> e.getEnrollmentId().equals(enrollment.getEnrollmentId())).findFirst()
                    .orElseThrow(() -> new InstanceNotFoundException(enrollment.getEnrollmentId(), Enrollment.class.getName()));
            assertNotNull(canceledEnrollment.getCancellationDate(), "La fecha de cancelación es null");
        }finally {
            if (addedCourse != null) {
                removeCourse(addedCourse.getCourseId());
            }
        }
    }

    @Test
    public void testCancelEnrollmentInvalidOrDifferentEmail() throws InputValidationException, InstanceNotFoundException, LateCancellationException, EnrollmentAlreadyCancelledException, NotEnoughSeatsException {
        Course addedCourse = courseService.addCourse(VALID_COURSE1);
        Enrollment enrollment;
        try{
            enrollment = courseService.enrollInCourse(VALID_EMAIL, addedCourse.getCourseId(), VALID_CREDIT_CARD);
            assertThrows(InputValidationException.class, () -> courseService.cancelEnrollment(enrollment.getEnrollmentId(), INVALID_EMAIL));
            assertThrows(UserMismatchException.class, () -> courseService.cancelEnrollment(enrollment.getEnrollmentId(), "validemail2@udc.com"));
        }finally {
            if (addedCourse != null) {
                removeCourse(addedCourse.getCourseId());
            }
        }
    }
    @Test
    public void testCancelEnrollmentLateCancellation() throws InputValidationException, InstanceNotFoundException, NotEnoughSeatsException {
        Course addedCourse = courseService.addCourse(VALID_COURSE1);
        Enrollment enrollment;
        try{
            enrollment = courseService.enrollInCourse(VALID_EMAIL, addedCourse.getCourseId(), VALID_CREDIT_CARD);
            //Para probar el error primero hay que cambiar la fecha de inicio del curso válido para que ya haya comenzado
            addedCourse.setCreationDate(LocalDateTime.now().minusDays(30));
            addedCourse.setStartDate(LocalDateTime.now().minusDays(14));
            updateCourse(addedCourse);
            assertThrows(LateCancellationException.class, () -> courseService.cancelEnrollment(enrollment.getEnrollmentId(), VALID_EMAIL));
        }finally {
            if (addedCourse != null) {
                removeCourse(addedCourse.getCourseId());
            }
        }
    }
    @Test
    public void testCancelEnrollmentAlreadyCancelled() throws InputValidationException, InstanceNotFoundException, NotEnoughSeatsException {
        Course addedCourse = courseService.addCourse(VALID_COURSE1);
        Enrollment enrollment;
        try{
            enrollment = courseService.enrollInCourse(VALID_EMAIL, addedCourse.getCourseId(), VALID_CREDIT_CARD);
            courseService.cancelEnrollment(enrollment.getEnrollmentId(), VALID_EMAIL);
            assertThrows(EnrollmentAlreadyCancelledException.class, () -> courseService.cancelEnrollment(enrollment.getEnrollmentId(), VALID_EMAIL));
        }finally {
            if (addedCourse != null) {
                removeCourse(addedCourse.getCourseId());
            }
        }
    }
}
