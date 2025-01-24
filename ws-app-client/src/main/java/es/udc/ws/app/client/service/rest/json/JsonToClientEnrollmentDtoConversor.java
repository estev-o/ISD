package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientEnrollmentDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientEnrollmentDtoConversor {

    public static ClientEnrollmentDto toClientEnrollmentDto(InputStream jsonEnrollment) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonEnrollment);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientEnrollmentDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
    public static ObjectNode toObjectNode(ClientEnrollmentDto enrollmentDto) {
        ObjectNode enrollmentObject = JsonNodeFactory.instance.objectNode();

        enrollmentObject.put("courseId", enrollmentDto.getCourseId())
                .put("userEmail", enrollmentDto.getUserEmail())
                .put("creditCardNumber", enrollmentDto.getCreditCardNumber())
                .put("enrollmentDate", enrollmentDto.getEnrollmentDate().toString());

        if (enrollmentDto.getCancellationDate() != null) {
            enrollmentObject.put("cancellationDate", enrollmentDto.getCancellationDate().toString());
        } else {
            enrollmentObject.set("cancellationDate", null);
        }
        return enrollmentObject;
    }

    public static List<ClientEnrollmentDto> toClienEnrollmentsDtos(InputStream jsonEnrollment) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonEnrollment);

            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode EnrollmentsArray = (ArrayNode) rootNode;
                List<ClientEnrollmentDto> enrollmentDtos = new ArrayList<>(EnrollmentsArray.size());
                for (JsonNode enrollmentNode : EnrollmentsArray) {
                    enrollmentDtos.add(toClientEnrollmentDto(enrollmentNode));
                }
                return enrollmentDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientEnrollmentDto toClientEnrollmentDto(JsonNode rootNode) throws ParsingException {
        if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            try {
                Long enrollmentId = rootNode.has("enrollmentId") && !rootNode.get("enrollmentId").isNull()
                        ? rootNode.get("enrollmentId").longValue()
                        : null;

                Long courseId = rootNode.get("courseId").longValue();
                String userEmail = rootNode.get("userEmail").textValue();
                String creditCardNumber = rootNode.get("creditCardNumber").textValue();
                LocalDateTime enrollmentDate = LocalDateTime.parse(rootNode.get("enrollmentDate").textValue());

                JsonNode cancellationDateNode = rootNode.get("cancellationDate");
                LocalDateTime cancellationDate = (cancellationDateNode != null && !cancellationDateNode.isNull())
                        ? LocalDateTime.parse(cancellationDateNode.textValue())
                        : null;

                return new ClientEnrollmentDto(enrollmentId, courseId, cancellationDate, userEmail, creditCardNumber, enrollmentDate);
            } catch (Exception e) {
                throw new ParsingException("Error parsing ClientEnrollmentDto JSON: " + e.getMessage(), e);
            }
        }
    }


}













