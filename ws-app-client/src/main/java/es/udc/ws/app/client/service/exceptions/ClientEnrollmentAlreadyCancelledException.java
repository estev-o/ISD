package es.udc.ws.app.client.service.exceptions;

public class ClientEnrollmentAlreadyCancelledException extends RuntimeException {
  private Long enrollmentId;

  public ClientEnrollmentAlreadyCancelledException(Long enrollmentId) {
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
