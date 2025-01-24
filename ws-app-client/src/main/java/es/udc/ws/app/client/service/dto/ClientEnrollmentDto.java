package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;

public class ClientEnrollmentDto {
    private Long enrollmentId;
    private String userEmail;
    private Long courseId;
    private LocalDateTime enrollmentDate;
    private LocalDateTime cancellationDate;
    private String creditCardNumber;

    public ClientEnrollmentDto( Long courseId, LocalDateTime cancellationDate, String userEmail, String creditCardNumber, LocalDateTime enrollmentDate){
        this.userEmail= userEmail;
        this.courseId=courseId;
        this.cancellationDate=cancellationDate;
        this.creditCardNumber=creditCardNumber;
        this.enrollmentDate=enrollmentDate;
    }
    public ClientEnrollmentDto(Long enrollmentId, Long courseId, LocalDateTime cancellationDate, String userEmail, String creditCardNumber, LocalDateTime enrollmentDate){
        this.enrollmentId=enrollmentId;
        this.userEmail= userEmail;
        this.courseId=courseId;
        this.cancellationDate=cancellationDate;
        this.creditCardNumber=creditCardNumber;
        this.enrollmentDate=enrollmentDate;
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDateTime getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(LocalDateTime cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

}
