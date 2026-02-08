package Exceptions.GPAExceptions;

public class InsufficientGradeDataException extends Exception {
    public InsufficientGradeDataException(String grade) {
        super("Error: Insufficient grade data for " + grade + ".");
    }
}
