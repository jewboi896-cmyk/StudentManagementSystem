package Exceptions.AssignmentExceptions;

public class InvalidAssignmentDataException extends Exception {
    public InvalidAssignmentDataException(String assignmentName) {
        super("Error: Assignment " + assignmentName + " is invalid");
    }
}
