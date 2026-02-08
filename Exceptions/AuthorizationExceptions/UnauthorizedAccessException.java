package Exceptions.AuthorizationExceptions;

public class UnauthorizedAccessException extends Exception {
    public UnauthorizedAccessException(String userName) {
        super("Error: User " + userName + " is invalid");
    }
}
