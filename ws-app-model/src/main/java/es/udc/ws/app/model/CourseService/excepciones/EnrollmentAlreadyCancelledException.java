package es.udc.ws.app.model.CourseService.excepciones;

//Se usa en el caso de uso 5
public class EnrollmentAlreadyCancelledException extends RuntimeException {
    private Long enrollmentId;

    public EnrollmentAlreadyCancelledException(Long enrollmentId) {
        super("La inscripción con id=\"" + enrollmentId + "\" ya ha sido cancelada.");
        this.enrollmentId = enrollmentId;
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }
}

