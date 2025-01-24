-- ----------------------------------------------------------------------------
-- Course Model
-------------------------------------------------------------------------------

DROP TABLE IF EXISTS Enrollment;
DROP TABLE IF EXISTS Course;

-- --------------------------------- Course ------------------------------------
CREATE TABLE Course (
    courseId BIGINT NOT NULL AUTO_INCREMENT,
    city VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    startDate DATETIME NOT NULL,
    price FLOAT NOT NULL,
    maxSeats INT NOT NULL,
    reservedSeats INT NOT NULL DEFAULT 0,
    creationDate DATETIME NOT NULL,
    CONSTRAINT CoursePK PRIMARY KEY (courseId),
    CONSTRAINT validCoursePrice CHECK (price > 0),
    CONSTRAINT validSeats CHECK (maxSeats > 0 AND reservedSeats >= 0 AND reservedSeats <= maxSeats)
) ENGINE = InnoDB;

-- --------------------------------- Enrollment ------------------------------------
CREATE TABLE Enrollment (
    enrollmentId BIGINT NOT NULL AUTO_INCREMENT,
    userEmail VARCHAR(255) NOT NULL,
    courseId BIGINT NOT NULL,
    creditCardNumber VARCHAR(4) NOT NULL, -- Only storing last 4 digits
    enrollmentDate DATETIME NOT NULL,
    cancellationDate DATETIME,
    CONSTRAINT EnrollmentPK PRIMARY KEY (enrollmentId),
    CONSTRAINT EnrollmentCourseIdFK FOREIGN KEY (courseId)
        REFERENCES Course(courseId) ON DELETE CASCADE
) ENGINE = InnoDB;