package Exceptions.AssignmentExceptions;

public class DuplicateAssignmentException extends Exception {
    public DuplicateAssignmentException(String assignmentName) {
        super("Error: Assignment " + assignmentName + " is already in use");
    }
}
