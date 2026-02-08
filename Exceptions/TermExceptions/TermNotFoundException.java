package Exceptions.TermExceptions;

public class TermNotFoundException extends Exception {
    public TermNotFoundException(String termName) {
        super("Error: The term " + termName + " is not found");
    }
}
