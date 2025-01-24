package es.udc.ws.app.model.CourseService;

import es.udc.ws.app.model.CourseService.excepciones.*;
import es.udc.ws.app.model.Cursos.Course;
import es.udc.ws.app.model.Cursos.SqlCourseDao;
import es.udc.ws.app.model.Cursos.SqlCourseDaoFactory;
import es.udc.ws.app.model.Enrollment.Enrollment;
import es.udc.ws.app.model.Enrollment.SqlEnrollmentDao;
import es.udc.ws.app.model.Enrollment.SqlEnrollmentDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;
import static es.udc.ws.app.model.util.ModelConstants.MAX_PRICE;

public class CourseServiceImpl implements CourseService {
    private final DataSource dataSource;
    private final SqlCourseDao courseDao;
    private final SqlEnrollmentDao enrollmentDao;

    public CourseServiceImpl() {
        dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        courseDao = SqlCourseDaoFactory.getDao();
        enrollmentDao = SqlEnrollmentDaoFactory.getDao();
    }

    @Override
    public Course addCourse(Course course) throws InputValidationException {
        //Validaciones del curso
        PropertyValidator.validateMandatoryString("city", course.getCity());
        PropertyValidator.validateMandatoryString("name", course.getName());
        PropertyValidator.validateDouble("price", course.getPrice(), 0, MAX_PRICE);

        if (course.getStartDate().isBefore(LocalDateTime.now().plusDays(15))) {
            throw new InputValidationException("La fecha de inicio tiene que ser al menos 15 dias despues de la fecha actual.");
        }
        if (course.getPrice() <= 0) {
            throw new InputValidationException("EL precio tiene que ser mayor que 0.");
        }
        if (course.getMaxSeats() <= 0) {
            throw new InputValidationException("El número maximo de asientos tiene que ser mayor que 0.");
        }

        course.setCreationDate(LocalDateTime.now());

        try(Connection connection = dataSource.getConnection()) {
            try {
                //Preparamos la conexión
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                //Creamos el curso
                Course createdCourse = courseDao.create(connection, course);

                //Commit
                connection.commit();

                return createdCourse;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Course> findCoursesByCityAndDate(String city, LocalDate startDate) {
        try (Connection connection = dataSource.getConnection()) {
            return courseDao.findCoursesByCityAndDate(connection, city, startDate);
        } catch (SQLException e) {
            throw new RuntimeException("Error getting connection or accessing data", e);
        }
    }

    @Override
    public Course findCourseById(Long courseId) throws InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            return courseDao.find(connection, courseId);
        } catch (SQLException e) {
            throw new RuntimeException("Error getting connection or accessing data", e);
        }
    }

    @Override
    public Enrollment enrollInCourse(String userEmail, Long courseId, String creditCardNumber)
            throws InstanceNotFoundException, InputValidationException, NotEnoughSeatsException, CourseAlreadyStartedException {
        // Validación de entrada (Email y tarjeta de crédito)
        PropertyValidator.validateMandatoryString("userEmail", userEmail);
        if (!userEmail.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) { //Confirmamos que use el patrón de un email
            throw new InputValidationException("El email no sigue el formato user@domain");
        }
        PropertyValidator.validateCreditCard(creditCardNumber);

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                //buscar el curso
                Course course = courseDao.find(connection, courseId);

                //Vemos si se puede inscribir
                if (course.getStartDate().isBefore(LocalDateTime.now())) {
                    throw new CourseAlreadyStartedException(courseId);
                }
                if (course.getAvailableSeats()<= 0) {
                    throw new NotEnoughSeatsException(courseId, course.getMaxSeats(), course.getReservedSeats());
                }

                //Creamos la inscripción y la guardamos
                Enrollment enrollment =new Enrollment(userEmail, courseId,
                        creditCardNumber.substring(creditCardNumber.length()-4), //Guardamos los ultimos 4 digitos de la tarjeta
                        LocalDateTime.now(), null);
                Enrollment createdEnrollment = enrollmentDao.create(connection, enrollment);

                //Actualizamos las plazas reservadas para el curso
                course.setReservedSeats(course.getReservedSeats() + 1);
                courseDao.update(connection, course);

                connection.commit();
                return createdEnrollment;
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errror con la conexión o accediendo a los datos", e);
        }
    }


    @Override
    public void cancelEnrollment(Long enrollmentId, String userEmail) throws InstanceNotFoundException, InputValidationException, LateCancellationException , EnrollmentAlreadyCancelledException, UserMismatchException {
        PropertyValidator.validateMandatoryString("userEmail", userEmail); //COmprobamos que el email no sea nulo u esté vaio
        if(!userEmail.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) { //Aqui comprobamos que el email siga el patrón de un email con la funcion matches
            throw new InputValidationException("El email no sigue el formato user@domain");
        }
        try(Connection connection = dataSource.getConnection()){
            try{
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Enrollment enrollment = enrollmentDao.find(connection, enrollmentId);
                if(!enrollment.getUserEmail().equals(userEmail)){ //Comprobanos que el usuario intenta cancelar su propia inscripcion
                    throw new UserMismatchException(enrollmentId,userEmail);
                }

                Course course = courseDao.find(connection, enrollment.getCourseId());
                if (course.getStartDate().isBefore(LocalDateTime.now().plusDays(7))) { //Comprobamos que no falten menos de 7 días para el comienzo del curso
                    throw new LateCancellationException(enrollmentId);
                }

                if (enrollment.getCancellationDate()!=null){ //Comprobamos que la inscripción no haya sido cancelada
                    throw new EnrollmentAlreadyCancelledException(enrollmentId);
                }

                enrollment.setCancellationDate(LocalDateTime.now());
                enrollmentDao.update(connection, enrollment);

                course.setReservedSeats(course.getReservedSeats()-1);
                courseDao.update(connection, course);
                connection.commit();
            }catch (UserMismatchException |InstanceNotFoundException | LateCancellationException | EnrollmentAlreadyCancelledException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        }catch (SQLException e){
            throw new RuntimeException("Error con la conexión o accediendo a los datos", e);
        }
    }

    @Override
    public List<Enrollment> findUserEnrollments(String userEmail) throws InputValidationException {
        if (userEmail == null || !userEmail.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) { //Confirmamos que use el patrón de un email
            throw new InputValidationException("El email no sigue el formato user@domain");
        }
        try (Connection connection = dataSource.getConnection()) {
            return enrollmentDao.findByUserEmail(connection, userEmail);
        } catch (SQLException e) {
            throw new RuntimeException("Error getting connection or accessing data", e);
        }
    }

}