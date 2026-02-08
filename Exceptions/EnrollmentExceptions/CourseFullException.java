package Exceptions.EnrollmentExceptions;

public class CourseFullException extends Exception {
    public CourseFullException(String courseName) {
        super("Error: Course " + courseName + " is already full.");
    }
}
