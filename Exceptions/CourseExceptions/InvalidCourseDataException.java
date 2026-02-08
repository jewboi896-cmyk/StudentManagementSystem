package Exceptions.CourseExceptions;

/*
Thrown when course credits are negative,
course name is empty,
or course capacity is invalid
 */

public class InvalidCourseDataException extends Exception {
    public InvalidCourseDataException(String courseName) {
        super("Error: Course " + courseName + " data is invalid");
    }
}
