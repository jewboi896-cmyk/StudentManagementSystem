package Exceptions.AttendenceExceptions;

public class InvalidAttendanceDateException extends Exception {
    public InvalidAttendanceDateException(String courseName) {
        super("Error: The attendance date for " + courseName + " is not valid");
    }
}
