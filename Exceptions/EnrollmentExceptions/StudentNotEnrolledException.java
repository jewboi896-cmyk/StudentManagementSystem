package Exceptions.EnrollmentExceptions;

public class StudentNotEnrolledException extends RuntimeException {
    public StudentNotEnrolledException(String courseName, String studentName) {
        super("Error: The student " + studentName + " is not enrolled in course " + courseName);
    }
}
