package es.udc.ws.app.model.Enrollment;

import java.time.LocalDateTime;
import java.util.List;

public class Enrollment {
    private Long enrollmentId;
    private String userEmail;
    private Long courseId;
    private String creditCardNumber; //Solo se almacenan los últimos 4 dígitos
    private LocalDateTime enrollmentDate;
    private LocalDateTime cancellationDate;


    public void setEnrollmentId(Long enrollmentId){
        this.enrollmentId=enrollmentId;
    }
    public Long getEnrollmentId() {
        return enrollmentId;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setCancellationDate(LocalDateTime cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public LocalDateTime getCancellationDate() {
        return cancellationDate;
    }


    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + ((enrollmentId == null) ? 0 : enrollmentId.hashCode());
        result = prime * result + ((userEmail == null) ? 0 : userEmail.hashCode());
        result = prime * result + ((courseId == null) ? 0 : courseId.hashCode());
        result = prime * result + ((creditCardNumber == null) ? 0 : creditCardNumber.hashCode());
        result = prime * result + ((enrollmentDate == null) ? 0 : enrollmentDate.hashCode());
        result = prime * result + ((cancellationDate == null) ? 0 : cancellationDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Enrollment other = (Enrollment) obj;
        if (enrollmentId == null){
            if (other.enrollmentId != null)
                return false;
        } else if (!enrollmentId.equals(other.enrollmentId))
            return false;
        if (userEmail == null){
            if (other.userEmail != null)
                return false;
        } else if (!userEmail.equals(other.userEmail))
            return false;
        if (courseId == null){
            if (other.courseId != null)
                return false;
        } else if (!courseId.equals(other.courseId))
            return false;
        if (creditCardNumber == null){
            if (other.creditCardNumber != null)
                return false;
        } else if (!creditCardNumber.equals(other.creditCardNumber))
            return false;
        if (enrollmentDate == null){
            if (other.enrollmentDate != null)
                return false;
        } else if (!enrollmentDate.equals(other.enrollmentDate))
            return false;
        if (cancellationDate == null){
            return other.cancellationDate == null;
        } else return cancellationDate.equals(other.cancellationDate);
    }

    //Constructor sin Id
    public Enrollment(String userEmail, Long courseId, String creditCardNumber, LocalDateTime enrollmentDate, LocalDateTime cancellationDate){
        this.userEmail = userEmail;
        this.courseId = courseId;
        this.creditCardNumber = creditCardNumber;
        this.enrollmentDate = (enrollmentDate!=null)?enrollmentDate.withNano(0):null;
        this.cancellationDate = (cancellationDate!=null)?cancellationDate.withNano(0):null;
    }
    //Constructor con Id
    public Enrollment(Long enrollmentId, String userEmail, Long courseId, String creditCardNumber, LocalDateTime enrollmentDate, LocalDateTime cancellationDate){
        this(userEmail, courseId, creditCardNumber, enrollmentDate, cancellationDate);
        this.enrollmentId = enrollmentId;
    }

}
