package Exceptions.TermExceptions;

public class TermLockedException extends Exception {
    public TermLockedException(String termName) {
        super("Error: The term " + termName + " is locked");
    }
}
