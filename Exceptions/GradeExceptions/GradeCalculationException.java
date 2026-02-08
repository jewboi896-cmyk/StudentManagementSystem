package Exceptions.GradeExceptions;

public class GradeCalculationException extends RuntimeException {
    public GradeCalculationException(String grade) {
        super("Error: Grade " + grade + " cannot be calculated.");
    }
}
