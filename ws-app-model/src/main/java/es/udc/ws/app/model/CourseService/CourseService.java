package es.udc.ws.app.model.CourseService;

import es.udc.ws.app.model.CourseService.excepciones.*;
import es.udc.ws.app.model.Cursos.Course;
import es.udc.ws.app.model.Enrollment.Enrollment;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface CourseService {

    // Validaciones InputValidationException:
    // - course.city == null || course.city.length() == 0
    // - course.name == null || course.name.length() == 0
    // - course.startDate debe ser al menos 15 días posterior al día actual
    // - course.price <= 0
    // - course.maxSeats <= 0
    Course addCourse(Course course) throws InputValidationException;

    // Validaciones InputValidationException:
    // - city == null || city.length() == 0
    // - startDate < día actual
    List<Course> findCoursesByCityAndDate(String city, LocalDate startDate) throws InputValidationException;

    // Validaciones InstanceNotFoundException:
    // - Si el curso no existe
    Course findCourseById(Long courseId) throws InstanceNotFoundException;

    // Validaciones InputValidationException:
    // - userEmail == null || userEmail no sigue el patrón user@domain
    // - creditCardNumber no tiene 16 dígitos
    // Validaciones NotEnoughSeatsException:
    // - Si no hay plazas disponibles
    // Validaciones CourseAlreadyStartedException:
    // - Si el curso ya ha comenzado
    Enrollment enrollInCourse(String userEmail, Long courseId, String creditCardNumber)
            throws InstanceNotFoundException, InputValidationException, NotEnoughSeatsException, CourseAlreadyStartedException;

    // Validaciones InputValidationException:
    // - userEmail == null || userEmail no sigue el patrón user@domain
    // Validaciones InstanceNotFoundException:
    // - Si la inscripción no existe
    // Validaciones EnrollmentAlreadyCancelledException:
    // - Si la inscripción ya fue cancelada
    // Validaciones LateCancellationException:
    // - Si faltan menos de 7 días para el comienzo del curso
    void cancelEnrollment(Long enrollmentId, String userEmail)
            throws InstanceNotFoundException, InputValidationException, LateCancellationException, EnrollmentAlreadyCancelledException, UserMismatchException;

    // Validaciones InputValidationException:
    // - userEmail == null || userEmail no sigue el patrón user@domain
    List<Enrollment> findUserEnrollments(String userEmail) throws InputValidationException;
}
