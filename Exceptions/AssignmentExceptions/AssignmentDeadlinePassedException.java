package Exceptions.AssignmentExceptions;

public class AssignmentDeadlinePassedException extends Exception {
    public AssignmentDeadlinePassedException(String assignmentName) {
        super("Error: Assignment " + assignmentName + "'s deadline has passed.");
    }
}
