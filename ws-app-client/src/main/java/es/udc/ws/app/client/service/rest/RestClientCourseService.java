package es.udc.ws.app.client.service.rest;

import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.app.client.service.rest.json.JsonToClientExceptionConversor;
import org.apache.hc.client5.http.fluent.Form;
import org.apache.hc.core5.http.*;

import java.io.*;

import es.udc.ws.app.client.service.ClientCourseService;
import es.udc.ws.app.client.service.dto.ClientCourseDto;
import es.udc.ws.app.client.service.dto.ClientEnrollmentDto;
import es.udc.ws.app.client.service.rest.json.JsonToClientCourseDtoConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientEnrollmentDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpStatus;

//IMPORTS para la función toInputStream
import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.ws.util.json.ObjectMapperFactory;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

public class RestClientCourseService implements ClientCourseService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientCourseService.endpointAddress";
    private String endpointAddress;

    @Override
    public Long addCourse(ClientCourseDto course) throws InputValidationException{
        try{
            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .post(getEndpointAddress() +"course") //TODO ver si es courses o course
                    .bodyStream(toInputStream(course), ContentType.create("application/json"))
                    .execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);
            return JsonToClientCourseDtoConversor.toClientCourseDto(response.getEntity().getContent()).getCourseId();
        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientCourseDto> findCoursesByCity(String city) throws InputValidationException {
        String fechaActual = LocalDateTime.now().toString();
        if (city.isBlank()) {
            throw new InputValidationException("Invalid argument");
        }

        try {
            //Realiza busqueda segun ciudad y fecha
            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .get(getEndpointAddress() + "course/?city=" + URLEncoder.encode(city, "UTF-8")
                            + "&startDate=" + URLEncoder.encode(fechaActual, "UTF-8"))
                    .execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientCourseDtoConversor.toClientCourseDtos(response.getEntity()
                    .getContent());

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientCourseDto findCourseById(Long courseId) throws InstanceNotFoundException {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .get(getEndpointAddress() + "course/" + courseId)
                    .execute().returnResponse();
            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientCourseDtoConversor.toClientCourseDto(response.getEntity().getContent());
        } catch (InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long enrollInCourse(Long courseId, String userEmail, String creditCardNumber)
            throws ClientCourseAlreadyStartedException, ClientNotEnoughSeatsException, InputValidationException, InstanceNotFoundException {
        try {
            ClientEnrollmentDto enrollmentDto = new ClientEnrollmentDto(
                    courseId, null, userEmail, creditCardNumber, LocalDateTime.now());

            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .post(getEndpointAddress() + "enrollment")
                    .bodyStream(toInputStream(enrollmentDto), ContentType.create("application/json"))
                    .execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientEnrollmentDtoConversor.toClientEnrollmentDto(response.getEntity().getContent()).getEnrollmentId();

        } catch (InputValidationException | ClientCourseAlreadyStartedException | ClientNotEnoughSeatsException  | InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelEnrollment(Long enrollmentId, String userEmail) throws InstanceNotFoundException,
            InputValidationException, ClientLateCancellationException, ClientEnrollmentAlreadyCancelledException {
        try{
            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .post(getEndpointAddress() + "enrollment/" + enrollmentId.toString() + "/cancel")
                    .bodyForm(Form.form().add("userEmail",userEmail).build())
                    .execute().returnResponse();
            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);
        }catch (InstanceNotFoundException | InputValidationException | ClientCourseAlreadyStartedException | ClientEnrollmentAlreadyCancelledException |
                ClientUserMismatchException | ClientLateCancellationException e){
            throw  e;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientEnrollmentDto> findUserEnrollment(String userEmail) throws InputValidationException {

        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request
                   .get(getEndpointAddress() + "enrollment/?userEmail=" + URLEncoder.encode(userEmail, "UTF-8"))
                 //   .get(getEndpointAddress() + "enrollment/?userEmail=estevo@estevo.com")
                    .execute().returnResponse();
            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientEnrollmentDtoConversor.toClienEnrollmentsDtos(response.getEntity()
                    .getContent());

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private InputStream toInputStream(ClientCourseDto course) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientCourseDtoConversor.toObjectNode(course));

            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream toInputStream(ClientEnrollmentDto enrollmentDto) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance(); // Usamos el ObjectMapper para convertir el DTO a JSON
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientEnrollmentDtoConversor.toObjectNode(enrollmentDto));

            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateStatusCode(int successCode, ClassicHttpResponse response) throws Exception {

        try {
            int statusCode = response.getCode();
            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {
                case HttpStatus.SC_NOT_FOUND -> throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_BAD_REQUEST -> throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                        response.getEntity().getContent());
               //Hay que ver si estas se añaden
                case HttpStatus.SC_FORBIDDEN -> throw JsonToClientExceptionConversor.fromForbiddenErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_GONE -> throw JsonToClientExceptionConversor.fromGoneErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_CONFLICT -> throw JsonToClientExceptionConversor.fromConflictErrorCode(
                        response.getEntity().getContent());
                default -> throw new RuntimeException("HTTP error; status code = "
                        + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
