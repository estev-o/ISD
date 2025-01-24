package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientCourseService;
import es.udc.ws.app.client.service.ClientCourseServiceFactory;
import es.udc.ws.app.client.service.dto.ClientCourseDto;
import es.udc.ws.app.client.service.dto.ClientEnrollmentDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppServiceClient {

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsageAndExit();
        }
        ClientCourseService clientCourseService =
                ClientCourseServiceFactory.getService();
        if ("-addCourse".equalsIgnoreCase(args[0])) {
            // [add] AppServiceClient -addCourse <city> <name> <startDate> <prize> <maxSeats>
            validateArgs(args, 6, new int[]{4, 5});
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            try {
                Long courseId = clientCourseService.addCourse(new ClientCourseDto(null, args[1],
                        args[2], LocalDateTime.parse(args[3], formatter), Float.parseFloat(args[4]), Integer.parseInt(args[5])));
                System.out.println("Course id=" + courseId + " created sucessfully");
            } catch (InputValidationException | NumberFormatException ex) {
                System.err.println("Error: " + ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if ("-findCourses".equalsIgnoreCase(args[0])) {
            // [find] CourseServiceClient -findCourse <courseId>
            validateArgs(args, 2, new int[]{});
            try {
                List<ClientCourseDto> courses = clientCourseService.findCoursesByCity(args[1]);
                System.out.println("Found " + courses.size() +
                        " course(s) with city '" + args[1] + "'");
                for (int i = 0; i < courses.size(); i++) {
                    ClientCourseDto courseDto = courses.get(i);
                    System.out.println("Id: " + courseDto.getCourseId() +
                            ", ReservedSeats: " + courseDto.getReservedSeats() +
                            ", MaxSeats: " + courseDto.getMaxSeats() +
                            ", Price: " + courseDto.getPrice() +
                            ", Name: " + courseDto.getName() +
                            ", Date: " + courseDto.getStartDate());
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }else if ("-findCourse".equalsIgnoreCase(args[0])) {
            // [find] AppServiceClient -findCourse <courseId>
            validateArgs(args, 2, new int[]{1});
            try {
                ClientCourseDto courseDto = clientCourseService.findCourseById(Long.parseLong(args[1]));
                //mostramos la información del curso encontrado
                System.out.println("Id: " + courseDto.getCourseId() +
                        ", Name: " + courseDto.getName() +
                        ", City: " + courseDto.getCity() +
                        ", Start Date: " + courseDto.getStartDate() +
                        ", Price: " + courseDto.getPrice() +
                        ", Max Seats: " + courseDto.getMaxSeats() +
                        ", Reserved Seats: " + courseDto.getReservedSeats());
            } catch (NumberFormatException | InstanceNotFoundException e) {
                System.err.println("Error: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        } else if ("-inscribe".equalsIgnoreCase(args[0])) {
            // [inscribe] AppServiceClient -inscribe <userEmail> <courseId> <creditCardNumber>
            validateArgs(args, 4, new int[]{2});
            try {
                Long enrollmentId = clientCourseService.enrollInCourse(
                        Long.parseLong(args[2]), args[1], args[3]);

                System.out.println("InscID=" + enrollmentId + " creada");
            } catch (NumberFormatException | InputValidationException | InstanceNotFoundException |
                     ClientCourseAlreadyStartedException | ClientNotEnoughSeatsException e) {
                System.err.println("Error: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        } else if ("-cancel".equalsIgnoreCase(args[0])) {
            // [cancel] CourseServiceClient -cancel <enrollmentId> <userEmail>
            validateArgs(args, 3, new int[]{1});
            try{
                clientCourseService.cancelEnrollment(Long.parseLong(args[1]), args[2]);
                System.out.println("Enrollment id="+ args[1] +" cancelled successfully");
            }catch (InstanceNotFoundException | InputValidationException | ClientLateCancellationException |
                    ClientEnrollmentAlreadyCancelledException | ClientUserMismatchException ex) { //Están todas las excepciones?
                System.err.println("Error: " + ex.getMessage());
            }

        } else if ("-findInscriptions".equalsIgnoreCase(args[0])) {
            //[findUserEnrollment]    MovieServiceClient -findInscriptions <userEmail>
            validateArgs(args, 2, new int[]{});
            try {
                List<ClientEnrollmentDto> enrollments = clientCourseService.findUserEnrollment(args[1]);
                System.out.println("Found " + enrollments.size() +
                        " enrollment(s) with userEmail '" + args[1] + "'");
                for (int i = 0; i < enrollments.size(); i++) {
                    ClientEnrollmentDto enrollmentDto = enrollments.get(i);
                    System.out.println("EnrollmentId: " + enrollmentDto.getEnrollmentId() +
                            ", CourseId: " + enrollmentDto.getCourseId() +
                            ", " + (enrollmentDto.getCancellationDate() != null
                            ? "Canceled (" + enrollmentDto.getCancellationDate() + ")"
                            : "Not Canceled") +
                            ", CreditCardNumber: " + enrollmentDto.getCreditCardNumber());
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
    }

    public static void validateArgs(String[] args, int expectedArgs, int[] numericArgs) {
        if (args.length != expectedArgs) {
            printUsageAndExit();
        }
        for(int position : numericArgs){
            try{
                Double.parseDouble(args[position]); //CHEQUEAR
            } catch (NumberFormatException e) {
                System.err.println("Error: El argumento en la posición " + position + " debe ser un número.");
                printUsageAndExit();
            }
        }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [add]    CourseServiceClient -addCourse <city> <name> <startDate> <prize> <maxSeats>\n" +
                "    [findByCity] CourseServiceClient -findCourses <city>\n" +
                "    [find]    CourseServiceClient -findCourse <courseId>\n" +
                "    [cancel]   CourseServiceClient -cancel <enrollmentId> <userEmail>\n" +
                "    [inscribe]    CourseServiceClient -inscribe <userEmail> <courseId> <creditCardNumber>\n" +
                "    [findUserEnrollment]    MovieServiceClient -findInscriptions <userEmail>\n");
    }

}