package es.udc.ws.app.model.CourseService.excepciones;

//Se usa en el caso de uso 4
public class CourseAlreadyStartedException extends RuntimeException {
    private Long courseId;

    public CourseAlreadyStartedException(Long courseId) {
        super("El curso con id=\"" + courseId + "\" ya ha comenzado.");
        this.courseId = courseId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}

