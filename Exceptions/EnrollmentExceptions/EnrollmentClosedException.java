package Exceptions.EnrollmentExceptions;

public class EnrollmentClosedException extends Exception {
    public EnrollmentClosedException(String courseName) {
        super("Error: The enrollment period for " + courseName + " has already closed");
    }
}
