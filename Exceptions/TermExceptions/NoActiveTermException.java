package Exceptions.TermExceptions;

public class NoActiveTermException extends Exception {
    public NoActiveTermException(String termName) {
        super("Error: The term " + termName + " is not active");
    }
}
