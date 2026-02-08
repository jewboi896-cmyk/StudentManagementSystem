package Exceptions.SystemExceptions;

public class IllegalStateException extends RuntimeException {
    public IllegalStateException(String state) {
        super("Error: An illegal state "  + state + " has occurred. Please try again.");
    }
}
