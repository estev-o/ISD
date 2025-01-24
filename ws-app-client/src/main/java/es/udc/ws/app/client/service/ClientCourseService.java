package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientCourseDto;
import es.udc.ws.app.client.service.dto.ClientEnrollmentDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.List;

public interface ClientCourseService {

    Long addCourse(ClientCourseDto course) throws InputValidationException;

    List<ClientCourseDto> findCoursesByCity(String city)
            throws InputValidationException;

    ClientCourseDto findCourseById(Long courseId) throws InstanceNotFoundException;

    Long enrollInCourse(Long courseId, String userEmail, String creditCardNumber)
            throws InstanceNotFoundException, InputValidationException, ClientCourseAlreadyStartedException, ClientNotEnoughSeatsException;

    void cancelEnrollment(Long enrollmentId, String userEmail)
            throws InstanceNotFoundException, InputValidationException, ClientLateCancellationException, ClientEnrollmentAlreadyCancelledException, ClientUserMismatchException;

    List<ClientEnrollmentDto> findUserEnrollment(String userEmail)
            throws InputValidationException;

}

