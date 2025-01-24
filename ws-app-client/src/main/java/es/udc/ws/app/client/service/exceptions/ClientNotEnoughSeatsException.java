package es.udc.ws.app.client.service.exceptions;

public class ClientNotEnoughSeatsException extends RuntimeException {
    private Long courseId;
    private int availableSeats;
    private int requestedSeats;

    public ClientNotEnoughSeatsException(Long courseId, int availableSeats, int requestedSeats) {
        super("NotEnoughSeatsException: Curso con id=\"" + courseId + "\" no tiene suficientes plazas. " +
                "Disponibles: " + availableSeats + ", Solicitadas: " + requestedSeats);
        this.courseId = courseId;
        this.availableSeats = availableSeats;
        this.requestedSeats = requestedSeats;
    }
    public Long getCourseId() {
        return courseId;
    }
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
    public int getAvailableSeats() {
        return availableSeats;
    }
    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
    public int getRequestedSeats() {
        return requestedSeats;
    }
    public void setRequestedSeats(int requestedSeats) {
        this.requestedSeats = requestedSeats;
    }
}
