package es.udc.ws.app.model.Cursos;

import java.time.LocalDateTime;

public class Course {
    private Long courseId;
    private String city;
    private String name;
    private LocalDateTime startDate;
    private Float price;
    private int maxSeats;
    private int reservedSeats;
    private LocalDateTime creationDate;

    //Constructor sin Id
    public Course(String city, String name, LocalDateTime startDate,
                  Float price, int maxSeats, int reservedSeats, LocalDateTime creationDate) {
        this.city = city;
        this.name = name;
        this.startDate = (startDate != null) ? startDate.withNano(0) : null;
        this.price = price;
        this.maxSeats = maxSeats;
        this.reservedSeats = reservedSeats;
        this.creationDate = (creationDate != null) ? creationDate.withNano(0) : null;
    }

    public Course (Long courseId, String city, String name, LocalDateTime startDate, Float price,
                   int maxSeats, int reservedSeats) {
        this.courseId = courseId;
        this.city = city;
        this.name = name;
        this.startDate = (startDate != null) ? startDate.withNano(0) : null;
        this.price = price;
        this.maxSeats = maxSeats;
        this.reservedSeats = reservedSeats;
    }

    public Course(Long courseId, String city, String name, LocalDateTime startDate,
                  Float price, int maxSeats, int reservedSeats, LocalDateTime creationDate) {
        this(city, name, startDate, price, maxSeats, reservedSeats, creationDate);
        this.courseId = courseId;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((courseId == null) ? 0 : courseId.hashCode());
        result = prime * result + ((city == null) ? 0 : city.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + Float.floatToIntBits(price);
        result = prime * result + maxSeats;
        result = prime * result + reservedSeats;
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Course other = (Course) obj;
        if (courseId == null) {
            if (other.courseId != null)
                return false;
        } else if (!courseId.equals(other.courseId))
            return false;
        if (city == null) {
            if (other.city != null)
                return false;
        } else if (!city.equals(other.city))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (startDate == null) {
            if (other.startDate != null)
                return false;
        } else if (!startDate.equals(other.startDate))
            return false;
        if (price == null) {
            if (other.price != null)
                return false;
        } else if (!price.equals(other.price))
            return false;
        if (maxSeats != other.maxSeats) {
            return false;
        }
        if (reservedSeats != other.reservedSeats) {
            return false;
        }
        if (creationDate == null) {
            if (other.creationDate != null)
                return false;
        } else if (!creationDate.equals(other.creationDate))
            return false;
        return true;
    }
}
