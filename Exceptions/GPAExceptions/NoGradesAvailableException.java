package Exceptions.GPAExceptions;

public class NoGradesAvailableException extends Exception {
    public NoGradesAvailableException(String courseName) {
        super("Error: No grades available for " + courseName + ".");
    }
}
