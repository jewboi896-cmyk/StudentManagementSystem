package Exceptions.CourseExceptions;

public class CourseSectionNotFoundException extends Exception {
    public CourseSectionNotFoundException(String courseName) {
        super("Error: Course " + courseName + " does not exist");
    }
}
