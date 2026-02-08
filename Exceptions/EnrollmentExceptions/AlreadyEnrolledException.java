package Exceptions.EnrollmentExceptions;

public class AlreadyEnrolledException extends Exception {
    public AlreadyEnrolledException(String studentName, String courseName) {
        super("Error: Student " + studentName + " is already enrolled in " + courseName);
    }
}
