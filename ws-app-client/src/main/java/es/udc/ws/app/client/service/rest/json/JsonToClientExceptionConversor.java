package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;

public class JsonToClientExceptionConversor {

    public static Exception fromBadRequestErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InputValidation")) {
                    return toInputValidationException(rootNode);
                }
                throw new ParsingException("Unrecognized error type: " + errorType);
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static Exception fromNotFoundErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InstanceNotFound")) {
                    return toInstanceNotFoundException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static Exception fromConflictErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if ("NotEnoughSeats".equals(errorType)) {
                    return toNotEnoughSeatsException(rootNode);
                } else if ("EnrollmentAlreadyCancelled".equals(errorType)) {
                    return toEnrollmentAlreadyCancelledException(rootNode);
                }else if ("CourseAlreadyStarted".equals(errorType)){
                    return toCourseAlreadyStartedException(rootNode);
                }else{
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }



    public static Exception fromForbiddenErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                return switch (errorType) {
                    case "UserMismatch" -> toUserMismatchException(rootNode);
                    default -> throw new ParsingException("Unrecognized error type: " + errorType);
                };
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static Exception fromGoneErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            }

            String errorType = rootNode.get("errorType").textValue();
            if ("LateCancellationException".equals(errorType)) {
                return toLateCancellationException(rootNode);
            } else {
                throw new ParsingException("Unrecognized error type: " + errorType);
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InputValidationException toInputValidationException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }

    public static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) throws ParsingException {
        try {
            String instanceType = rootNode.get("instanceType").textValue();
            JsonNode instanceIdNode = rootNode.get("instanceId");
            Object instanceId = (instanceIdNode != null && !instanceIdNode.isNull())
                    ? instanceIdNode.textValue()
                    : null;

            return new InstanceNotFoundException(instanceId, instanceType);
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }


    private static ClientUserMismatchException toUserMismatchException(JsonNode rootNode) {
        Long enrollmentId = rootNode.has("enrollmentId") && !rootNode.get("enrollmentId").isNull()
                ? rootNode.get("enrollmentId").longValue()
                : null;

        String actualUserEmail = rootNode.has("actualUserEmail") && !rootNode.get("actualUserEmail").isNull()
                ? rootNode.get("actualUserEmail").textValue()
                : null;

        return new ClientUserMismatchException(enrollmentId, actualUserEmail);
    }

    private static ClientLateCancellationException toLateCancellationException(JsonNode rootNode) {
        Long enrollmentId = rootNode.get("enrollmentId").longValue();
        return new ClientLateCancellationException(enrollmentId);
    }

    private static ClientNotEnoughSeatsException toNotEnoughSeatsException(JsonNode rootNode) {
        Long courseId = rootNode.get("courseId").longValue();
        return new ClientNotEnoughSeatsException(courseId,0,1);
    }

    private static ClientCourseAlreadyStartedException toCourseAlreadyStartedException(JsonNode rootNode) {
        Long courseId = rootNode.get("courseId").longValue();
        return new ClientCourseAlreadyStartedException(courseId);
    }

    private static ClientEnrollmentAlreadyCancelledException toEnrollmentAlreadyCancelledException(JsonNode rootNode) {
        Long enrollmentId = rootNode.get("enrollmentId").longValue();
        return new ClientEnrollmentAlreadyCancelledException(enrollmentId);
    }
}

