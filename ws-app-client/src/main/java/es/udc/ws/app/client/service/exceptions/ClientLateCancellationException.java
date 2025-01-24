package es.udc.ws.app.client.service.exceptions;

public class ClientLateCancellationException extends RuntimeException {
  private Long enrollmentId;

  public ClientLateCancellationException(Long enrollmentId) {
    super("Imposible la cancelación para la inscripccionId=\"" + enrollmentId + "\" ,es muy tarde.");
    this.enrollmentId = enrollmentId;
  }
  public Long getEnrollmentId() {
    return enrollmentId;
  }
  public void setEnrollmentId(Long enrollmentId) {
    this.enrollmentId = enrollmentId;
  }
}
