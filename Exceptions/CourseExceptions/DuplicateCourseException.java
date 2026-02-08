package Exceptions.CourseExceptions;

/*
Thrown when attempting to add a new course to a course that has an existing course ID
or when a newly created course is already in catalog
 */

public class DuplicateCourseException extends Exception {
    public DuplicateCourseException(String courseName) {
        super("Error: Course " + courseName + " is already in use");
    }
}
