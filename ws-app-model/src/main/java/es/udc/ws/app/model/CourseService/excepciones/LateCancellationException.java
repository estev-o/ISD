package es.udc.ws.app.model.CourseService.excepciones;

//Se usa en el caso de uso  5
public class LateCancellationException extends RuntimeException {
    private Long enrollmentId;

    public LateCancellationException(Long enrollmentId) {
        super("Cancelación para la inscripccionId=\"" + enrollmentId + "\" es muy tarde.");
        this.enrollmentId = enrollmentId;
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }
}

