namespace java es.udc.ws.app.thrift

struct ThriftCourseDto {
    1: i64 courseId,
    2: string city,
    3: string name,
    4: string startDate,
    5: i32 availableSeats,
    6: i32 totalSeats,
    7: double price
}

struct ThriftEnrollmentDto {
    1: i64 enrollmentId,
    2: string userEmail,
    3: string creditCardNumber,
    4: string cancellationDate,
    5: i64 courseId,
    6: string enrollmentDate
}

exception ThriftInputValidationException {
    1: string message
}

exception ThriftInstanceNotFoundException {
    1: string instanceId,
    2: string instanceType
}

exception ThriftUserMismatchException {
    1: i64 enrollmentId,
    2: string userEmail
}

exception ThriftEnrollmentAlreadyCancelledException {
    1: i64 enrollmentId
}

exception ThriftLateCancellationException {
    1: i64 enrollmentId
}

service ThriftCourseService {
    ThriftCourseDto findCourseById(1: i64 courseId) throws (1: ThriftInstanceNotFoundException e1)

    i64 enrollInCourse(
        1: i64 courseId,
        2: string userEmail,
        3: string creditCardNumber
    ) throws (1: ThriftInputValidationException e1, 2: ThriftLateCancellationException e2, 3: ThriftEnrollmentAlreadyCancelledException e3, 4: ThriftUserMismatchException e4)
}
