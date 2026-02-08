package Exceptions.AssignmentExceptions;

public class AssignmentNotGradedException extends Exception {
    public AssignmentNotGradedException(String assignmentName) {
        super("Error: Assignment " + assignmentName + " not graded yet");
    }
}
