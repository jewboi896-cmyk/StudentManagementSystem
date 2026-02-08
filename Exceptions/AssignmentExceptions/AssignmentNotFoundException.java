package Exceptions.AssignmentExceptions;

public class AssignmentNotFoundException extends Exception {
    public AssignmentNotFoundException(String assignmentName) {
        super("Error: Assignment " + assignmentName + " not found");
    }
}
