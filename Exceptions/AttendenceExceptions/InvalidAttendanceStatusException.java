package Exceptions.AttendenceExceptions;

public class InvalidAttendanceStatusException extends Exception {
    public InvalidAttendanceStatusException(String courseName) {
        super("Error: The attendance status for " + courseName + " is not valid");
    }
}
