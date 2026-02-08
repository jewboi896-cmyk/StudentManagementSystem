package Exceptions.AttendenceExceptions;

public class AttendanceRecordNotFoundException extends Exception {
    public AttendanceRecordNotFoundException(String courseName) {
        super("Error: The attendance record for " + courseName + " does not exist");
    }
}
