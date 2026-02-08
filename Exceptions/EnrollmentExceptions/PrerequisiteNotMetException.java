package Exceptions.EnrollmentExceptions;

public class PrerequisiteNotMetException extends Exception {
    public PrerequisiteNotMetException(String studentName, String courseName) {
        super("Error: Student " + studentName + " has not met the prerequisite for " + courseName);
    }
}
