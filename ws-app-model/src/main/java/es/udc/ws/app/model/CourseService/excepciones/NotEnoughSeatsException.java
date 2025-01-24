package es.udc.ws.app.model.CourseService.excepciones;

public class NotEnoughSeatsException extends Exception {
    private Long courseId;
    private int availableSeats;
    private int requestedSeats;

    public NotEnoughSeatsException(Long courseId, int availableSeats, int requestedSeats) {
        super("Curso con id=\"" + courseId + "\" no tiene suficientes plazas. " +
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

