package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientCourseDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientCourseDtoConversor {

    public static ObjectNode toObjectNode(ClientCourseDto course) {
        ObjectNode courseObject = JsonNodeFactory.instance.objectNode();

        if (course.getCourseId() != null) {
            courseObject.put("courseId", course.getCourseId());
        }
        courseObject.put("city", course.getCity())
                .put("name", course.getName())
                .put("startDate", course.getStartDate().toString())
                .put("price", course.getPrice())
                .put("maxSeats", course.getMaxSeats())
                .put("reservedSeats", course.getReservedSeats());

        return courseObject;
    }

    public static ClientCourseDto toClientCourseDto(InputStream jsonCourse) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonCourse);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientCourseDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientCourseDto> toClientCourseDtos(InputStream jsonCourses) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonCourses);

            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode coursesArray = (ArrayNode) rootNode;
                List<ClientCourseDto> courseDtos = new ArrayList<>(coursesArray.size());
                for (JsonNode courseNode : coursesArray) {
                    courseDtos.add(toClientCourseDto(courseNode));
                }
                return courseDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientCourseDto toClientCourseDto(JsonNode courseNode) throws ParsingException {
        if (courseNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode courseObject = (ObjectNode) courseNode;

            JsonNode courseIdNode = courseObject.get("courseId");
            Long courseId = (courseIdNode != null) ? courseIdNode.longValue() : null;

            String city = courseObject.get("city").textValue().trim();
            String name = courseObject.get("name").textValue().trim();
            LocalDateTime startDate = LocalDateTime.parse(courseObject.get("startDate").textValue());
            float price = courseObject.get("price").floatValue();
            int maxSeats = courseObject.get("maxSeats").intValue();

            return new ClientCourseDto(courseId, city, name, startDate, price, maxSeats);
        }
    }
}

