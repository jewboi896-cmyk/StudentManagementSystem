package Exceptions.AttendenceExceptions;

public class AttendanceAlreadyRecordedException extends Exception {
    public AttendanceAlreadyRecordedException(String courseName) {
        super("Error: The attendance status for " + courseName + " is already recorded");
    }
}
