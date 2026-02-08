package Exceptions.TermExceptions;

public class InvalidTermDateException extends Exception {
    public InvalidTermDateException(String termName, String termDate) {
        super("Error: The term date " + termDate + " for the term " + termName + " is invalid");
    }
}
