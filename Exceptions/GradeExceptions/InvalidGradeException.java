package Exceptions.GradeExceptions;

public class InvalidGradeException extends Exception {
    public InvalidGradeException(String grade) {
        super("Error: Grade " + grade + " is invalid.");
    }
}
