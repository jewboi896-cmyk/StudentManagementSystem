package Exceptions.AuthorizationExceptions;

public class SessionExpiredException extends Exception {
    public SessionExpiredException(String sessionName) {
        super("Error: Session " + sessionName + " is expired");
    }
}
