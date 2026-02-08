package Exceptions.SystemExceptions;

public class InitializationException extends RuntimeException {
    public InitializationException(String errorMessage) {
        super("Error: A critical error " + errorMessage + " has occurred. Please try again.");
    }
}
