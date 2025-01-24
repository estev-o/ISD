package es.udc.ws.app.client.service.exceptions;

public class ClientUserMismatchException extends RuntimeException {
    private Long enrollmentId;
    private String userEmail;

    public ClientUserMismatchException(Long enrollmentId, String userEmail) {
        super("El usuario con email=\"" + userEmail + "\" no puede cancelar la inscripción con id=\"" + enrollmentId + "\" porque no le pertenece.");
        this.enrollmentId = enrollmentId;
        this.userEmail = userEmail;
    }
    public Long getEnrollmentId() {
        return enrollmentId;
    }
    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
