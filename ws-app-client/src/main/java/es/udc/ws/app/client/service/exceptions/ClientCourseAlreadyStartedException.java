package es.udc.ws.app.client.service.exceptions;

public class ClientCourseAlreadyStartedException extends RuntimeException {
    private Long courseId;
    public ClientCourseAlreadyStartedException(Long courseId) {
        super("CourseAlreadyStarted El curso con id=\"" + courseId + "\" ya ha comenzado.");
        this.courseId = courseId;
    }
    public Long getCourseId() {
        return courseId;
    }
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
