package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;

public class ClientCourseDto {

    private Long courseId;
    private String city;
    private String name;
    private LocalDateTime startDate;
    private Float price;
    private int maxSeats;
    private int reservedSeats;
    private LocalDateTime creationDate;

    public ClientCourseDto() {
    }

    public ClientCourseDto(Long courseId, String city, String name, LocalDateTime startDate, Float price,
                           int maxSeats) {
        this.courseId = courseId;
        this.city = city;
        this.name = name;
        this.startDate = (startDate != null) ? startDate.withNano(0) : null;
        this.price = price;
        this.maxSeats = maxSeats;
        this.reservedSeats = reservedSeats+1;
        this.creationDate = (creationDate != null) ? creationDate.withNano(0) : null;

    }

    public Long getCourseId() {
        return this.courseId;
    }
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
    public String getCity() {
        return this.city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public LocalDateTime getStartDate() {
        return this.startDate;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = (startDate != null) ? startDate.withNano(0) : null;
    }
    public Float getPrice() {
        return this.price;
    }
    public void setPrice(Float price) {
        this.price = price;
    }
    public int getMaxSeats() {
        return this.maxSeats;
    }
    public void setMaxSeats(int maxSeats) {
        this.maxSeats = maxSeats;
    }
    public int getReservedSeats() {
        return this.reservedSeats;
    }
    public void setReservedSeats(int reservedSeats) {
        this.reservedSeats = reservedSeats;
    }
    public LocalDateTime getCreationDate() {
        return this.creationDate;
    }
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = (creationDate != null) ? creationDate.withNano(0) : null;
    }

    public int getAvailableSeats() {
        return this.maxSeats - this.reservedSeats;
    }


}
