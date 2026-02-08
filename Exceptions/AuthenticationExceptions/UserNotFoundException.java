package Exceptions.AuthenticationExceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String userName) {
        super("Error: User " + userName + " is not found");
    }
}
