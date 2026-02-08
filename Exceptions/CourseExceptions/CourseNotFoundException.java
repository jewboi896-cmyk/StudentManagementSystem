package Exceptions.CourseExceptions;

/*
Thrown when user searches for course that doesn't exist,
course ID not found in system,
or when trying to access a deleted course
 */

public class CourseNotFoundException extends Exception {
    CourseNotFoundException(String courseName) {
        super("Error: Course " + courseName + " not found");
    }
}
